package edu.ncsu.csc.iTrust2.models;

import java.time.ZonedDateTime;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.iTrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.iTrust2.adapters.ZonedDateTimeAttributeConverter;

/**
 * This is the validated database-persisted vaccination visit representation
 *
 * @author Rohan Rehman
 *
 */

@Entity
public class VaccinationVisit extends OfficeVisit {
    // /**
    // * The patient of this vaccination visit
    // */
    // @NotNull
    // @ManyToOne
    // @JoinColumn ( name = "patient_id", columnDefinition = "varchar(100)" )
    // private Patient patient;

    // /**
    // * The hcp of this vaccination visit
    // */
    // @NotNull
    // @ManyToOne
    // @JoinColumn ( name = "hcp_id", columnDefinition = "varchar(100)" )
    // private User hcp;

    /**
     * Prescriptions associated with this OfficeVisit
     */
    @OneToOne
    private Vaccine       vaccine;

    /**
     * whether a followup vaccination was requested
     */
    public Boolean        followUpRequested;

    /**
     * The follow up date
     */
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = ZonedDateTimeAttributeConverter.class )
    @JsonAdapter ( ZonedDateTimeAdapter.class )
    private ZonedDateTime followupDate;

    /** For Hibernate/Thymeleaf _must_ be an empty constructor */
    public VaccinationVisit () {
    }
    //
    // /**
    // * @return the patient
    // */
    // @Override
    // public User getPatient () {
    // return patient;
    // }
    //
    // /**
    // * @param patient
    // * the patient to set
    // */
    // public void setPatient ( final Patient patient ) {
    // this.patient = patient;
    // }

    // /**
    // * @return the hcp
    // */
    // @Override
    // public User getHcp () {
    // return hcp;
    // }
    //
    // /**
    // * @param hcp
    // * the hcp to set
    // */
    // @Override
    // public void setHcp ( final User hcp ) {
    // this.hcp = hcp;
    // }

    /**
     * @return the vaccine
     */
    public Vaccine getVaccine () {
        return vaccine;
    }

    public Boolean getFollowUpRequested () {
        return followUpRequested;
    }

    public void setFollowUpRequested ( final Boolean followUpRequested ) {
        this.followUpRequested = followUpRequested;
    }

    public ZonedDateTime getFollowupDate () {
        return followupDate;
    }

    public void setFollowupDate ( final ZonedDateTime followupDate ) {
        this.followupDate = followupDate;
    }

    public void setVaccine ( final Vaccine vaccine ) {
        this.vaccine = vaccine;
    }

}
