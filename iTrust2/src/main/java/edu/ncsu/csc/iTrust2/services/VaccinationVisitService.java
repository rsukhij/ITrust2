package edu.ncsu.csc.iTrust2.services;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.forms.VaccinationVisitForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccinationAppointmentRequest;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;
import edu.ncsu.csc.iTrust2.models.enums.AppointmentType;
import edu.ncsu.csc.iTrust2.models.enums.Status;
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
     * Hospital Service
     */
    @Autowired
    private HospitalService                      hospitalService;

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
     * @param visitForm
     *            Form to build from
     * @return Constructed VaccinationVisit
     */
    public VaccinationVisit build ( final VaccinationVisitForm visitForm ) {
        final VaccinationVisit v = new VaccinationVisit();

        v.setPatient( userService.findByName( visitForm.getPatient() ) );
        v.setHcp( userService.findByName( visitForm.getHcp() ) );

        final ZonedDateTime visitDate = ZonedDateTime.parse( visitForm.getDate() );
        v.setDate( visitDate );

        if ( null != visitForm.getPreScheduled() && visitForm.getAppointment() == null ) {

            throw new IllegalArgumentException( "Marked as preschedule but no match can be found" );

        }
        if ( visitForm.getAppointment() != null ) {
            final ZonedDateTime appDate = ZonedDateTime.parse( visitForm.getAppointment() );

            if ( appDate.minusHours( 3 ).isAfter( visitDate ) || appDate.plusHours( 3 ).isBefore( visitDate ) ) {
                throw new IllegalArgumentException(
                        "Vaccination Visit must be within 3 hours of associated Appointment" );
            }
        }

        if ( visitForm.getFdate() != null ) {

            final ZonedDateTime followDate = ZonedDateTime.parse( visitForm.getFdate() );

            final VaccinationAppointmentRequest app = new VaccinationAppointmentRequest();
            app.setComments( null );
            app.setDate( followDate );
            app.setHcp( v.getHcp() );
            app.setPatient( userService.findByName( visitForm.getPatient() ) );
            app.setStatus( Status.APPROVED );
            app.setType( AppointmentType.VACCINATION );
            app.setVaccineType( visitForm.getType() );
            appointmentRequestService.save( app );
        }

        v.setType( AppointmentType.VACCINATION );
        v.setVaccinator( v.getHcp() );
        v.setVaccines( visitForm.getType() );
        v.setHospital( hospitalService.findByName( visitForm.getHospital() ) );

        final Patient p = (Patient) v.getPatient();
        if ( p == null || p.getDateOfBirth() == null ) {
            return v; // we're done, patient can't be tested against
        }
        final LocalDate dob = p.getDateOfBirth();
        int age = v.getDate().getYear() - dob.getYear();
        // Remove the -1 when changing the dob to OffsetDateTime
        if ( v.getDate().getMonthValue() < dob.getMonthValue() ) {
            age -= 1;
        }
        else if ( v.getDate().getMonthValue() == dob.getMonthValue() ) {
            if ( v.getDate().getDayOfMonth() < dob.getDayOfMonth() ) {
                age -= 1;
            }
        }

        return v;
    }

}
