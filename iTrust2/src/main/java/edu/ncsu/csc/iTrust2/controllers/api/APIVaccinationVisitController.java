package edu.ncsu.csc.iTrust2.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;
import edu.ncsu.csc.iTrust2.models.enums.PatientVaccinationStatus;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.services.VaccinationVisitService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIVaccinationVisitController extends APIController {

    /** OfficeVisit service */
    @Autowired
    private VaccinationVisitService<VaccinationVisit> vaccinationVisitService;

    /** User service */
    @Autowired
    private UserService<User>                         userService;

    /** LoggerUtil */
    @Autowired
    private LoggerUtil                                loggerUtil;

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
            if ( visit.getVaccine().getIfSecondDose() ) {
                if ( p.getVaccinationStatus() == PatientVaccinationStatus.NO_VACCINATION
                        || p.getVaccinationStatus() == null ) {
                    return new ResponseEntity(
                            errorResponse(
                                    "Can't document a second appointment if there has not been a previous one." ),
                            HttpStatus.BAD_REQUEST );
                }

            }
            if ( p.getVaccinationStatus() == PatientVaccinationStatus.FULLY_VACCINATED ) {
                return new ResponseEntity( errorResponse( "Patient is already vaccinated" ), HttpStatus.BAD_REQUEST );
            }
            if ( !visit.getVaccine().isEligible( p ) ) {
                return new ResponseEntity( errorResponse( "Patient is not eligible based on age" ),
                        HttpStatus.BAD_REQUEST );
            }

            if ( p.getVaccinationStatus() == PatientVaccinationStatus.NO_VACCINATION ) {
                if ( visit.getVaccine().getIfSecondDose() ) {
                    p.setVaccinationStatus( PatientVaccinationStatus.PARTIALLY_VACCINATED );
                }
                else {
                    p.setVaccinationStatus( PatientVaccinationStatus.FULLY_VACCINATED );
                }
            }

            if ( p.getVaccinationStatus() == PatientVaccinationStatus.PARTIALLY_VACCINATED ) {
                p.setVaccinationStatus( PatientVaccinationStatus.FULLY_VACCINATED );
            }
            userService.save( p );
            vaccinationVisitService.save( visit );
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
