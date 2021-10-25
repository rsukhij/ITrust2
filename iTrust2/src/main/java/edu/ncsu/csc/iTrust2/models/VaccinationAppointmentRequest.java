package edu.ncsu.csc.iTrust2.models;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

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
    @NotEmpty
    @Length ( max = 64 )
    private String vaccineType;

    /**
     * Sets the vaccine type
     *
     * @return return the vaccine type
     */
    public String getVaccineType () {
        return vaccineType;
    }

    /**
     * Gets the vaccineType
     *
     * @param vaccineType
     *            the vaccine type
     */
    public void setVaccineType ( final String vaccineType ) {
        this.vaccineType = vaccineType;
    }
}
