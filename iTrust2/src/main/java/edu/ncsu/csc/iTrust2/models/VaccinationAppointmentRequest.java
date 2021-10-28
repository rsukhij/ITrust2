package edu.ncsu.csc.iTrust2.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;


/**
 * Represents a VaccinationAppointmentRequest in the system
 *
 * @author Rohan Sukhija
 *
 */
@Entity
public class VaccinationAppointmentRequest extends AppointmentRequest {

    /**
     * Name of the vaccine
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "vaccine_name", columnDefinition = "varchar(100)" )
    private Vaccine vaccineType;


    /**
     * Sets the vaccine type
     *
     * @return return the vaccine type
     */
    public Vaccine getVaccineType () {
        return vaccineType;
    }

    /**
     * Gets the vaccineType
     *
     * @param vaccineType
     *            the vaccine type
     */
    public void setVaccineType ( final Vaccine vaccineType ) {
        this.vaccineType = vaccineType;
    }
}
