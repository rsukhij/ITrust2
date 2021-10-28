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
public class VaccinationAppointmentRequestService <T extends AppointmentRequest>
        extends AppointmentRequestService<VaccinationAppointmentRequest> {
    /**
     * Repository for CRUD operations
     */
    @Autowired
    private VaccinationAppointmentRequestRepository<VaccinationAppointmentRequest> repository;

    @Override
    protected JpaRepository<VaccinationAppointmentRequest, Long> getRepository () {
        return repository;
    }

}
