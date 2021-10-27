package edu.ncsu.csc.iTrust2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.vaccine.Vaccine;

/**
 * VaccineRepository provides CRUD operations for the Vaccine model. Spring will
 * generate appropriate code with JPA.
 *
 * @author sarasophiamasood
 *
 */
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {

    /**
     * Finds a vaccine object with the provided name.
     * 
     * @param name
     *            name of vaccine
     * @return found vaccine, null if none
     */
    Vaccine findByName ( String name );

}
