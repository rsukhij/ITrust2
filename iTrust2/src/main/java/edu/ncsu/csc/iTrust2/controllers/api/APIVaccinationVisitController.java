package edu.ncsu.csc.iTrust2.controllers.api;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.forms.VaccinationVisitForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccinationAppointmentRequest;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;
import edu.ncsu.csc.iTrust2.models.enums.AppointmentType;
import edu.ncsu.csc.iTrust2.models.enums.PatientVaccinationStatus;
import edu.ncsu.csc.iTrust2.models.enums.Status;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.services.VaccinationAppointmentRequestService;
import edu.ncsu.csc.iTrust2.services.VaccinationVisitService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIVaccinationVisitController extends APIController {

    /** OfficeVisit service */
    @Autowired
    private VaccinationVisitService              vaccinationVisitService;

    /** User service */
    @Autowired
    private UserService<User>                    userService;

    /** LoggerUtil */
    @Autowired
    private LoggerUtil                           loggerUtil;

    /**
     * AppointmentRequest service
     */
    @Autowired
    private VaccinationAppointmentRequestService appointmentRequestService;

    /**
     * Retrieves a list of all VaccinationVisits in the database
     *
     * @return list of vaccination visits
     */
    @GetMapping ( BASE_PATH + "/vaccinationvisits" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" + "|| hasAnyRole('ROLE_VACCINATOR')" )
    public List<VaccinationVisit> getVaccinationVisits () {
        loggerUtil.log( TransactionType.VIEW_ALL_VACCINATION_VISITS, LoggerUtil.currentUser() );
        return vaccinationVisitService.findAll();
    }

    /**
     * Retrieves all of the vaccination visits for the current HCP.
     *
     * @return all of the vaccination visits for the current HCP.
     */
    @GetMapping ( BASE_PATH + "/vaccinationvisits/HCP" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" + "|| hasAnyRole('ROLE_VACCINATOR')" )
    public List<VaccinationVisit> getVaccinationVisitsForHCP () {
        final User self = userService.findByName( LoggerUtil.currentUser() );
        loggerUtil.log( TransactionType.VIEW_ALL_VACCINATION_VISITS, self );
        final List<VaccinationVisit> visits = vaccinationVisitService.findByHcp( self );
        return visits;
    }

    /**
     * Retrieves a list of all VaccinationVisits in the database for the current
     * patient
     *
     * @return list of vaccination visits
     */
    @GetMapping ( BASE_PATH + "/vaccinationvisits/myvaccinationvisits" )
    @PreAuthorize ( "hasAnyRole('ROLE_PATIENT')" )
    public List<VaccinationVisit> getMyVaccinationVisits () {
        final User self = userService.findByName( LoggerUtil.currentUser() );
        loggerUtil.log( TransactionType.VIEW_ALL_VACCINATION_VISITS, self );
        return vaccinationVisitService.findByPatient( self );
    }

    /**
     * Retrieves a specific VaccinationVisit in the database, with the given ID
     *
     * @param id
     *            ID of the vaccination visit to retrieve
     * @return list of vaccination visits
     */
    @GetMapping ( BASE_PATH + "/vaccinationvisits/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" + "|| hasAnyRole('ROLE_VACCINATOR')" )
    public ResponseEntity getVaccinationVisit ( @PathVariable final Long id ) {
        final User self = userService.findByName( LoggerUtil.currentUser() );
        loggerUtil.log( TransactionType.GENERAL_CHECKUP_HCP_VIEW, self );
        if ( !vaccinationVisitService.existsById( id ) ) {
            return new ResponseEntity( HttpStatus.NOT_FOUND );
        }

        return new ResponseEntity( vaccinationVisitService.findById( id ), HttpStatus.OK );
    }

    /**
     * Creates and saves a new VaccinationVisit from the RequestBody provided.
     *
     * @param visitForm
     *            The vaccination visit to be validated and saved
     * @return response
     */
    @PostMapping ( BASE_PATH + "/vaccinationvisits" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" + "|| hasAnyRole('ROLE_VACCINATOR')" )
    public ResponseEntity createVaccinationVisit ( @RequestBody final VaccinationVisitForm visitForm ) {
        try {
            final VaccinationVisit visit = vaccinationVisitService.build( visitForm );

            if ( null != visitForm.getPreScheduled() && visitForm.getAppointment() == null ) {
                return new ResponseEntity(
                        errorResponse( "Marked as Prescheduled but no associated appointment was selected. \n" ),
                        HttpStatus.BAD_REQUEST );
            }

            // Checks if visit is within 3 hours of associated appointment
            if ( visitForm.getAppointment() != null ) {
                final ZonedDateTime appDate = ZonedDateTime.parse( visitForm.getAppointment() );

                if ( appDate.minusHours( 3 ).isAfter( visit.getDate() )
                        || appDate.plusHours( 3 ).isBefore( visit.getDate() ) ) {
                    return new ResponseEntity(
                            errorResponse( "Vaccination Visit must be within 3 hours of associated Appointment \n" ),
                            HttpStatus.BAD_REQUEST );
                }
            }

            final Patient p = (Patient) visit.getPatient();
            PatientVaccinationStatus stat = p.getVaccinationStatus();
            if ( stat == null ) {
                stat = PatientVaccinationStatus.NO_VACCINATION;
            }

            // Can't create a visit if patient is fully vaccinated
            if ( stat == PatientVaccinationStatus.FULLY_VACCINATED ) {
                return new ResponseEntity( errorResponse( "Patient is already fully vaccinated \n" ),
                        HttpStatus.BAD_REQUEST );
            }

            // Checks if user entered a follow up appointment
            if ( visitForm.getFdate() != null ) {
                // If user is already partially vaccinated, this visit
                // would make them fully vaccinated
                // Therefore a followup appointment isn't needed
                if ( stat == PatientVaccinationStatus.PARTIALLY_VACCINATED ) {
                    return new ResponseEntity( errorResponse(
                            "Patient does not need a follow up appointment, this visit makes them fully vaccinated \n" ),
                            HttpStatus.BAD_REQUEST );
                }
                // If patient has a followup appointment, the vaccine is two
                // dose
                // so if they are not vaccinated already they would be partially
                // from this visit
                if ( stat == PatientVaccinationStatus.NO_VACCINATION ) {
                    stat = PatientVaccinationStatus.PARTIALLY_VACCINATED;
                }

                final ZonedDateTime followDate = ZonedDateTime.parse( visitForm.getFdate() );
                // Create followup appointment
                final VaccinationAppointmentRequest app = new VaccinationAppointmentRequest();
                app.setComments( null );
                app.setDate( followDate );
                app.setHcp( visit.getHcp() );
                app.setPatient( userService.findByName( visitForm.getPatient() ) );
                app.setStatus( Status.APPROVED );
                app.setType( AppointmentType.VACCINATION );
                app.setVaccineType( visitForm.getType() );
                appointmentRequestService.save( app );
            }
            else {
                // Vaccine is 2 dose, patient is now fully vaccinated
                if ( visitForm.getType().getDoseNumber() == 2
                        && stat == PatientVaccinationStatus.PARTIALLY_VACCINATED ) {
                    stat = PatientVaccinationStatus.FULLY_VACCINATED;
                }
                if ( visitForm.getType().getDoseNumber() == 2 && stat == PatientVaccinationStatus.NO_VACCINATION ) {
                    stat = PatientVaccinationStatus.PARTIALLY_VACCINATED;
                }
                // Vaccine is one dose, patient is now fully vaccinated
                if ( visitForm.getType().getDoseNumber() == 1 && stat == PatientVaccinationStatus.NO_VACCINATION ) {
                    stat = PatientVaccinationStatus.FULLY_VACCINATED;
                }
                p.setVaccinationStatus( stat );
                userService.save( p );

            }

            p.setVaccinationStatus( stat );
            if ( visit.getHcp() == null ) {
                visit.setHcp( userService.findByName( LoggerUtil.currentUser() ) );
            }

            vaccinationVisitService.save( visit );
            loggerUtil.log( TransactionType.GENERAL_CHECKUP_CREATE, LoggerUtil.currentUser(),
                    visit.getPatient().getUsername() );
            return new ResponseEntity( visit, HttpStatus.OK );

        }
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse(
                            "Could not validate or save the VaccinationVisit provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

}
