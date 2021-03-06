package edu.ncsu.csc.iTrust2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.AppointmentRequest;
import edu.ncsu.csc.iTrust2.models.VaccinationAppointmentRequest;

/**
 * Repository for interacting with VaccinationAppointmentRequest model. Method
 * implementations generated by Spring
 *
 * @author Rohan Sukhiaj
 * @param <T>
 *            Type of AppointmentRequest
 *
 */
public interface VaccinationAppointmentRequestRepository <T extends AppointmentRequest>
        extends JpaRepository<VaccinationAppointmentRequest, Long> {

}
