package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.models.Vaccine;
import edu.ncsu.csc.iTrust2.services.VaccineService;

/**
 * APIVaccineController Class that extends APIController. Has methods for
 * getting the Vaccine, adding a Vaccine and editing a Vaccine.
 *
 * @author Sara Sophia Masood
 *
 */
@RestController
public class APIVaccineController extends APIController {

    /*
     * Vaccine service
     */
    @Autowired
    private VaccineService service;

    /**
     * Retrieves list of vaccines in database.
     *
     * @return list of vaccines
     */
    @GetMapping ( BASE_PATH + "/addVaccine" )
    public List<Vaccine> getVaccines () {
        return service.findAll();
    }

    /**
     * Adds vaccine to database.
     *
     * @param vaccine
     *            vaccine
     * @return response
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @PostMapping ( BASE_PATH + "/addVaccine" )
    public ResponseEntity addVaccine ( @RequestBody final Vaccine vaccine ) {

        if ( vaccine.getIfAvailable() == false ) {
            return new ResponseEntity(
                    errorResponse( "Vaccine with the name " + vaccine.getName() + " is not available" ),
                    HttpStatus.CONFLICT );
        }

        if ( null != service.findByVaccineName( vaccine.getName() ) ) {
            return new ResponseEntity(
                    errorResponse( "Vaccine with the name " + vaccine.getName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        else {
            service.save( vaccine );
            return new ResponseEntity( successResponse( vaccine.getName() + " successfully created" ), HttpStatus.OK );
        }
    }

    /**
     * Edits vaccine in database
     *
     * @param v
     *            vaccine
     * @return response
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @PutMapping ( BASE_PATH + "/addVaccine" )
    public ResponseEntity editVaccine ( @RequestBody final Vaccine v ) {
        final Vaccine vaccine = service.findByVaccineName( v.getName() );
        if ( vaccine == null ) {
            return new ResponseEntity( errorResponse( "No vaccine found with name " + v ), HttpStatus.NOT_FOUND );
        }
        vaccine.editVaccine( v.getName(), v.getAgeMin(), v.getAgeMax(), v.getDoseNumber(), v.getIfSecondDose(),
                v.getDaysBetween(), v.getIfAvailable() );
        service.save( vaccine );
        return new ResponseEntity( vaccine, HttpStatus.OK );

    }

}
