package edu.ncsu.csc.iTrust2.services;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.iTrust2.models.AppointmentRequest;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;
import edu.ncsu.csc.iTrust2.models.enums.AppointmentType;
import edu.ncsu.csc.iTrust2.repositories.VaccinationVisitRepository;

@Component
@Transactional
public class VaccinationVisitService extends Service<VaccinationVisit, Long> {

    /**
     * Repository for CRUD operations
     */
    @Autowired
    private VaccinationVisitRepository           repository;

    /**
     * User service
     */
    @Autowired
    private UserService<User>                    userService;

    /**
     * AppointmentRequest service
     */
    @Autowired
    private VaccinationAppointmentRequestService appointmentRequestService;

    /**
     * Vaccine service
     */
    @Autowired
    private VaccineService                       vaccineService;

    @Override
    protected JpaRepository<VaccinationVisit, Long> getRepository () {
        return repository;
    }

    /**
     * Finds all VaccinationVisits created by the specified HCP
     *
     * @param hcp
     *            HCP to search for
     * @return Matching VaccinationVisits
     */
    public List<VaccinationVisit> findByHcp ( final User hcp ) {
        return repository.findByHcp( hcp );
    }

    /**
     * Finds all VaccinationVisits for the specified Patient
     *
     * @param patient
     *            Patient to search for
     * @return Matching VaccinationVisits
     */
    public List<VaccinationVisit> findByPatient ( final User patient ) {
        return repository.findByPatient( patient );
    }

    /**
     * Find all VaccinationVisits for both the specified Patient and HCP
     *
     * @param hcp
     *            HCP to search for
     * @param patient
     *            Patient to search for
     * @return List of visits found
     */
    public List<VaccinationVisit> findByHcpAndPatient ( final User hcp, final User patient ) {
        return repository.findByHcpAndPatient( hcp, patient );
    }

    /**
     * Builds an VaccinatoinVisit based on the deserialised VaccinationVisitForm
     *
     * @param ovf
     *            Form to build from
     * @return Constructed VaccinationVisit
     */
    public VaccinationVisit build ( final OfficeVisitForm ovf ) {
        final VaccinationVisit ov = new VaccinationVisit();

        ov.setPatient( userService.findByName( ovf.getPatient() ) );
        ov.setHcp( userService.findByName( ovf.getHcp() ) );
        ov.setNotes( ovf.getNotes() );

        if ( ovf.getId() != null ) {
            ov.setId( Long.parseLong( ovf.getId() ) );
        }

        final ZonedDateTime visitDate = ZonedDateTime.parse( ovf.getDate() );
        ov.setDate( visitDate );

        AppointmentType at = null;
        try {
            at = AppointmentType.valueOf( ovf.getType() );
        }
        catch ( final NullPointerException npe ) {
            at = AppointmentType.VACCINATION; /*
                                               * If for some reason we don't
                                               * have a type, default to general
                                               * checkup
                                               */
        }
        ov.setType( at );

        if ( null != ovf.getPreScheduled() ) {
            final List<AppointmentRequest> requests = appointmentRequestService.findByHcpAndPatient( ov.getHcp(),
                    ov.getPatient() );
            try {
                final AppointmentRequest match = requests.stream().filter( e -> e.getDate().equals( ov.getDate() ) )
                        .collect( Collectors.toList() )
                        .get( 0 ); /*
                                    * We should have one and only one
                                    * appointment for the provided HCP & patient
                                    * and the time specified
                                    */
                ov.setAppointment( match );
            }
            catch ( final Exception e ) {
                throw new IllegalArgumentException( "Marked as preschedule but no match can be found" + e.toString() );
            }

        }

        // final List<PrescriptionForm> ps = ovf.getPrescriptions();
        // if ( ps != null ) {
        // ov.setPrescriptions( ps.stream().map( prescriptionService::build
        // ).collect( Collectors.toList() ) );
        // }

        final Patient p = (Patient) ov.getPatient();
        if ( p == null || p.getDateOfBirth() == null ) {
            return ov; // we're done, patient can't be tested against
        }
        final LocalDate dob = p.getDateOfBirth();
        int age = ov.getDate().getYear() - dob.getYear();
        // Remove the -1 when changing the dob to OffsetDateTime
        if ( ov.getDate().getMonthValue() < dob.getMonthValue() ) {
            age -= 1;
        }
        else if ( ov.getDate().getMonthValue() == dob.getMonthValue() ) {
            if ( ov.getDate().getDayOfMonth() < dob.getDayOfMonth() ) {
                age -= 1;
            }
        }
        return ov;
    }

}
