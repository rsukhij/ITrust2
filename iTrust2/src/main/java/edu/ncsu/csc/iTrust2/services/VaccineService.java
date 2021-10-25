package edu.ncsu.csc.iTrust2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.Vaccine;
import edu.ncsu.csc.iTrust2.repositories.VaccineRepository;

/**
 * Service class for interacting with Vaccine model.
 * 
 * @author Justin Takamiya (jjtakami@ncsu.edu)
 *
 */
public class VaccineService extends Service<Vaccine, Long> {
	 @Autowired
	    private VaccineRepository vaccineRepository;
	    
	    
	    @Override
	    protected JpaRepository<Vaccine, Long> getRepository() {
	        return vaccineRepository;
	    }

	    
	    public Vaccine findByName ( final String name ) {
	        if (vaccineRepository.findByName( name ).isPresent())
	            return vaccineRepository.findByName( name ).get();
	        else
	            return null;
	    }

}
