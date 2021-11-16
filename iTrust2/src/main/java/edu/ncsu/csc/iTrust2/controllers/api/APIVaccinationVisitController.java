package edu.ncsu.csc.iTrust2.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.forms.AppointmentRequestForm;
import edu.ncsu.csc.iTrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccinationAppointmentRequest;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;
import edu.ncsu.csc.iTrust2.models.enums.PatientVaccinationStatus;
import edu.ncsu.csc.iTrust2.models.enums.Status;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.PatientService;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.services.VaccinationAppointmentRequestService;
import edu.ncsu.csc.iTrust2.services.VaccinationVisitService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIVaccinationVisitController extends APIController {

    /** OfficeVisit service */
    @Autowired
    private VaccinationVisitService<VaccinationVisit>                           vaccinationVisitService;

    /** Vaccination request service */
    @Autowired
    private VaccinationAppointmentRequestService<VaccinationAppointmentRequest> vaccReqService;

    /** User service */
    @Autowired
    private UserService<User>                                                   userService;

    /** Patient service */
    @Autowired
    private PatientService<Patient>                                             patientService;

    /** LoggerUtil */
    @Autowired
    private LoggerUtil                                                          loggerUtil;

    /**
     * Creates and saves a new VaccinationVisit from the RequestBody provided.
     *
     * @param visitForm
     *            The vaccination visit to be validated and saved
     * @return response
     */
    @PostMapping ( BASE_PATH + "/vaccinationvisits" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP','ROLE_VACCINATOR')" )
    public ResponseEntity createVaccinationVisit ( @RequestBody final OfficeVisitForm visitForm ) {
        try {
            final VaccinationVisit visit = vaccinationVisitService.build( visitForm );

            if ( null != visit.getId() && vaccinationVisitService.existsById( visit.getId() ) ) {
                return new ResponseEntity(
                        errorResponse( "Vaccination visit with the id " + visit.getId() + " already exists" ),
                        HttpStatus.CONFLICT );
            }
            final Patient p = (Patient) visit.getPatient();
            // check if patient is alreqdy fully vaxxed
            if ( p.getVaccinationStatus() == PatientVaccinationStatus.FULLY_VACCINATED ) {
                return new ResponseEntity( errorResponse( "Patient is already vaccinated" ), HttpStatus.BAD_REQUEST );
            }
            // check if patient is too old or young for vax
            if ( !visit.getVaccine().isEligible( p ) ) {
                return new ResponseEntity( errorResponse( "Patient is not eligible based on age" ),
                        HttpStatus.BAD_REQUEST );
            }

            // edit patient vaxx status accordingly
            if ( p.getVaccinationStatus() == PatientVaccinationStatus.NO_VACCINATION
                    || p.getVaccinationStatus() == null ) {
                if ( visit.getVaccine().getIfSecondDose() ) {
                    p.setVaccinationStatus( PatientVaccinationStatus.PARTIALLY_VACCINATED );
                }
                else {
                    p.setVaccinationStatus( PatientVaccinationStatus.FULLY_VACCINATED );
                }
            }
            else if ( p.getVaccinationStatus() == PatientVaccinationStatus.PARTIALLY_VACCINATED ) {
                p.setVaccinationStatus( PatientVaccinationStatus.FULLY_VACCINATED );
            }

            // save new patient vaccination status
            patientService.save( p );
            // save validated vaccination visit
            vaccinationVisitService.save( visit );

            // schedule followup appt if requested
            if ( visit.getFollowUpRequested() ) {
                final AppointmentRequestForm appointmentForm = new AppointmentRequestForm();
                // 2030-11-19 4:50 AM EST
                appointmentForm.setDate( visitForm.getFollowUpDate() );

                appointmentForm.setType( "VACCINATION" );
                appointmentForm.setHcp( visitForm.getHcp() );
                appointmentForm.setPatient( visitForm.getPatient() );
                appointmentForm.setComments( "" );
                appointmentForm.setVaccineType( visitForm.getVaccine() );
                final VaccinationAppointmentRequest req = (VaccinationAppointmentRequest) vaccReqService
                        .build( appointmentForm );
                req.setStatus( Status.APPROVED );
                vaccReqService.save( req );
            }
            loggerUtil.log( TransactionType.VACCINATION_VISIT_CREATE, LoggerUtil.currentUser(),
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
