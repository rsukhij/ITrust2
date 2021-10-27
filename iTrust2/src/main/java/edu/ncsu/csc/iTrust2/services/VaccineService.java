package edu.ncsu.csc.iTrust2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.vaccine.Vaccine;
import edu.ncsu.csc.iTrust2.repositories.VaccineRepository;

/**
 * Service class for interacting with Vaccine model, performing CRUD
 * (Create, Read, Update, and Delete) tasks with database.
 * 
 * @author Justin Takamiya (jjtakami@ncsu.edu)
 *
 */
public class VaccineService extends Service<Vaccine, Long> {
	
	/**
	 * Repository for CRUD task
	 */
	@Autowired
	private VaccineRepository vaccineRepository;

	/**
	 * Returns the vaccine repository
	 */
	@Override
	protected JpaRepository<Vaccine, Long> getRepository() {
		return vaccineRepository;
	}

	/**
	 * Finds the vaccine with the provided name
	 * @param name the name of the vaccine to find
	 * @return the vaccine, if found
	 */
	public Vaccine findByVaccineName(final String name) {
		return vaccineRepository.findByName(name);

	}

}
