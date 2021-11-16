package edu.ncsu.csc.iTrust2.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.iTrust2.models.AppointmentRequest;
import edu.ncsu.csc.iTrust2.models.OfficeVisit;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;
import edu.ncsu.csc.iTrust2.models.Vaccine;
import edu.ncsu.csc.iTrust2.models.enums.AppointmentType;
import edu.ncsu.csc.iTrust2.repositories.VaccinationVisitRepository;

@Component
@Transactional
public class VaccinationVisitService <T extends OfficeVisit> extends OfficeVisitService<VaccinationVisit> {

    /**
     * Repository for CRUD operations
     */
    @Autowired
    private VaccinationVisitRepository repository;

    /**
     * User service
     */
    @Autowired
    private UserService<User>          userService;

    /**
     * AppointmentRequest service
     */
    @Autowired
    private AppointmentRequestService  appointmentRequestService;

    /**
     * Vaccine service
     */
    @Autowired
    private VaccineService             vaccineService;

    /**
     * Hospital Service
     */
    @Autowired
    private HospitalService            hospitalService;

    @Override
    protected JpaRepository<VaccinationVisit, Long> getRepository () {
        return repository;
    }

    /**
     * Builds an VaccinatoinVisit based on the deserialised VaccinationVisitForm
     *
     * @param ovf
     *            Form to build from
     * @return Constructed VaccinationVisit
     */
    @Override
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

        ov.setType( AppointmentType.VACCINATION );

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

        final Vaccine vaccine = vaccineService.findByVaccineName( ovf.getVaccine() );

        ov.setVaccine( vaccine );
        ov.setHospital( hospitalService.findByName( ovf.getHospital() ) );

        if ( ovf.getFollowUpRequested() != null && ovf.getFollowUpRequested().equals( "yes" ) ) {
            ov.setFollowupDate( ZonedDateTime.parse( ovf.getFollowUpDate() ) );
        }

        return ov;
    }

}
