package edu.ncsu.csc.iTrust2.controllers.api;

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

import edu.ncsu.csc.iTrust2.models.OfficeVisit;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.services.VaccinationVisitService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIVaccinationVisitController extends APIController {

    /** OfficeVisit service */
    @Autowired
    private VaccinationVisitService vaccinationVisitService;

    /** User service */
    @Autowired
    private UserService<User>       userService;

    /** LoggerUtil */
    @Autowired
    private LoggerUtil              loggerUtil;

    /**
     * Retrieves a list of all VaccinationVisits in the database
     *
     * @return list of vaccination visits
     */
    @GetMapping ( BASE_PATH + "/vaccinationvisits" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" )
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
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" )
    public List<VaccinationVisit> getVaccinationVisitsForHCP () {
        final User self = userService.findByName( LoggerUtil.currentUser() );
        loggerUtil.log( TransactionType.VIEW_ALL_VACCINATION_VISITS, self );
        final List<OfficeVisit> visits = vaccinationVisitService.findByHcp( self );
        return visits;
    }

    /**
     * Retrieves a list of all VaccinationVisits in the database for the current
     * patient
     *
     * @return list of office visits
     */
    @GetMapping ( BASE_PATH + "/officevisits/myofficevisits" )
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
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" )
    public ResponseEntity getVaccinationVisit ( @PathVariable final Long id ) {
        final User self = userService.findByName( LoggerUtil.currentUser() );
        loggerUtil.log( TransactionType.GENERAL_CHECKUP_HCP_VIEW, self );
        if ( !vacciantionVisitService.existsById( id ) ) {
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
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" )
    public ResponseEntity createVaccinationVisit ( @RequestBody final VaccinationVisitForm visitForm ) {
        try {
            final VaccinationVisit visit = vaccinationVisitService.build( visitForm );

            if ( null != visit.getId() && vaccinationVisitService.existsById( visit.getId() ) ) {
                return new ResponseEntity(
                        errorResponse( "Vaccination visit with the id " + visit.getId() + " already exists" ),
                        HttpStatus.CONFLICT );
            }
            VaccinationVisitService.save( visit );
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
