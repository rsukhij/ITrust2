package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.services.VaccineService;
import edu.ncsu.csc.iTrust2.vaccine.Vaccine;

@RestController
public class APIVaccineController extends APIController {

    @Autowired
    private VaccineService service;

    @GetMapping ( BASE_PATH + "/addVaccine" )
    public List<Vaccine> getVaccines () {
        return service.findAll();
    }

    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @GetMapping ( BASE_PATH + "/addVaccine/{name}" )
    public ResponseEntity getVaccine ( @PathVariable ( "name" ) final String name ) {
        final Vaccine vaccine = service.findByVaccineName( name );
        return null == vaccine
                ? new ResponseEntity( errorResponse( "No vaccine found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( vaccine, HttpStatus.OK );
    }

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

    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @PostMapping ( BASE_PATH + "/addVaccine/{name}" )
    public ResponseEntity editVaccine ( @PathVariable ( "name" ) final String name, @RequestBody final Integer minAge,
            @RequestBody final Integer maxAge, @RequestBody final Integer doseNumber,
            @RequestBody final Boolean ifSecondDose, @RequestBody final Integer timeBetween,
            @RequestBody final Boolean ifAvailable ) {
        final Vaccine vaccine = service.findByVaccineName( name );
        if ( vaccine == null ) {
            return new ResponseEntity( errorResponse( "No vaccine found with name" + name ), HttpStatus.NOT_FOUND );
        }
        vaccine.setName( "Pfizer" );
        vaccine.setAgeMax( 100 );
        vaccine.setAgeMin( 18 );
        vaccine.setDoseNumber( 1 );
        vaccine.setIfSecondDose( false );
        vaccine.setDaysBetween( 0 );
        vaccine.setIfAvailable( true );
        service.save( vaccine );
        return new ResponseEntity( vaccine, HttpStatus.OK );
    }

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
