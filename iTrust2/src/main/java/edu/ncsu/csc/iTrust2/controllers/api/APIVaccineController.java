package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import edu.ncsu.csc.iTrust2.vaccine.Vaccine;

public class APIVaccineController extends APIController {

    private VaccineService service;

    @GetMapping ( BASE_PATH + "/addVaccine" )
    public List<Vaccine> getVaccines () {
        return service.findAll();
    }

    @GetMapping ( BASE_PATH + "/addVaccine/{name}" )
    public ResponseEntity getIngredient ( @PathVariable ( "name" ) final String name ) {
        final Vaccine vaccine = service.findByName( name );
        return null == vaccine
                ? new ResponseEntity( errorResponse( "No vaccine found with name " + name ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( vaccine, HttpStatus.OK );
    }

}
