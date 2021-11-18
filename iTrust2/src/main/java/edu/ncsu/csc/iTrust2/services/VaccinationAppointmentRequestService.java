package edu.ncsu.csc.iTrust2.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.AppointmentRequest;
import edu.ncsu.csc.iTrust2.models.VaccinationAppointmentRequest;
import edu.ncsu.csc.iTrust2.repositories.VaccinationAppointmentRequestRepository;

@Component
@Transactional
/**
 * Service Class for the Vaccination Appointment Requests, extends
 * AppointmentRequest
 *
 * @author Rohan Sukhij
 *
 * @param <T>
 *            the instance of the service
 */
public class VaccinationAppointmentRequestService <T extends AppointmentRequest>
        extends AppointmentRequestService<VaccinationAppointmentRequest> {
    /**
     * Repository for CRUD operations, a repository to hold vaccine appointment
     * requests
     */
    @Autowired
    private VaccinationAppointmentRequestRepository<VaccinationAppointmentRequest> repository;

    /**
     * Getter method for the class; gets the instance of the repository and
     * returns it
     */
    @Override
    protected JpaRepository<VaccinationAppointmentRequest, Long> getRepository () {
        return repository;
    }

}
