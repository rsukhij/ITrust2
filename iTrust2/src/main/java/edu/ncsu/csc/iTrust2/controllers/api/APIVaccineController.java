package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.models.Vaccine;
import edu.ncsu.csc.iTrust2.services.VaccineService;

@RestController
public class APIVaccineController extends APIController {

    @Autowired
    private VaccineService service;

    @GetMapping ( BASE_PATH + "/addVaccine" )
    public List<Vaccine> getVaccines () {
        return service.findAll();
    }

    /**
     * Gets a list of vaccines in the system
     *
     * @return list of vaccines
     */
    @GetMapping ( BASE_PATH + "/addVaccine/vaccines" )
    public List<Vaccine> getVaccine () {
        return service.findAll();
    }

    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @PostMapping ( BASE_PATH + "/addVaccine" )
    public ResponseEntity addVaccine ( @RequestBody final Vaccine vaccine ) {
        if ( Vaccine.getIfAvailable() == false ) {
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

    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @PutMapping ( BASE_PATH + "/addVaccine/{name}" )
    public ResponseEntity editVaccine ( @PathVariable ( "name" ) final Vaccine v ) {
        final Vaccine vaccine = service.findByVaccineName( "v" );
        if ( vaccine == null ) {
            return new ResponseEntity( errorResponse( "No vaccine found with name" + v ), HttpStatus.NOT_FOUND );
        }
        service.save( vaccine );
        return new ResponseEntity( vaccine, HttpStatus.OK );
    }

    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @DeleteMapping ( BASE_PATH + "/addVaccine/{name}" )
    public ResponseEntity deleteVaccine ( @PathVariable final String name ) {
        final Vaccine vaccine = service.findByVaccineName( name );
        if ( null == vaccine ) {
            return new ResponseEntity( errorResponse( "No vaccine found for name " + name ), HttpStatus.NOT_FOUND );
        }
        service.delete( vaccine );

        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
    }

}
