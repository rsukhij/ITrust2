package edu.ncsu.csc.iTrust2.models;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.annotations.JsonAdapter;

import edu.ncsu.csc.iTrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.iTrust2.adapters.ZonedDateTimeAttributeConverter;
import edu.ncsu.csc.iTrust2.models.enums.PatientVaccinationStatus;

/**
 * This is the validated database-persisted vaccination visit representation
 *
 * @author Rohan Rehman
 *
 */

@Entity
public class VaccinationVisit extends OfficeVisit {
    /**
     * The patient of this vaccination visit
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "patient_id", columnDefinition = "varchar(100)" )
    private Patient                       patient;

    /**
     * The hcp of this vaccination visit
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "hcp_id", columnDefinition = "varchar(100)" )
    private User                          hcp;

    /**
     * The vaccinator of this vaccination visit
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "vaccinator_id", columnDefinition = "varchar(100)" )
    private User                          vaccinator;

    /**
     * The date of this vaccination visit
     */
    @NotNull
    @Basic
    // Allows the field to show up nicely in the database
    @Convert ( converter = ZonedDateTimeAttributeConverter.class )
    @JsonAdapter ( ZonedDateTimeAdapter.class )
    private ZonedDateTime                 date;

    /**
     * The id of this vaccination visit
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long                          id;

    /**
     * Prescriptions associated with this OfficeVisit
     */
    @OneToMany ( cascade = CascadeType.ALL )
    @JsonManagedReference
    private Vaccine                       vaccine;

    /**
     * The visit number
     */
    private int                           visitNumber;

    /**
     * The vaccination appointment of this office visit
     */
    @OneToOne
    @JoinColumn ( name = "appointment_id" )
    private VaccinationAppointmentRequest appointment;

    /** For Hibernate/Thymeleaf _must_ be an empty constructor */
    public VaccinationVisit () {
    }

    /**
     * @return the patient
     */
    @Override
    public User getPatient () {
        return patient;
    }

    /**
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final Patient patient ) {
        this.patient = patient;
    }

    /**
     * @return the hcp
     */
    @Override
    public User getHcp () {
        return hcp;
    }

    /**
     * @param hcp
     *            the hcp to set
     */
    @Override
    public void setHcp ( final User hcp ) {
        this.hcp = hcp;
    }

    /**
     * @return the vaccinator
     */
    public User getVaccinator () {
        return vaccinator;
    }

    /**
     * @param vaccinator
     *            the vaccinator to set
     */
    public void setVaccinator ( final User vaccinator ) {
        this.vaccinator = vaccinator;
    }

    /**
     * @return the date
     */
    @Override
    public ZonedDateTime getDate () {
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    @Override
    public void setDate ( final ZonedDateTime date ) {
        this.date = date;
    }

    /**
     * @return the id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    @Override
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * @return the vaccines
     */
    public Vaccine getVaccines () {
        return vaccine;
    }

    /**
     * Set vaccine to the choice of the patient in the vaccination appointment
     * request if it is available. Otherwise, set it to whatever vaccine is
     * selected by the HCP or Vaccinator
     *
     * @param vaccines
     *            the vaccines to set
     */
    public void setVaccines ( final Vaccine vaccine ) {
        if ( appointment.getVaccineType().getIfAvailable() ) {
            this.vaccine = appointment.getVaccineType();
        }
        else {
            this.vaccine = vaccine;
        }
    }

    /**
     * @return the visitNumber
     */
    public int getVisitNumber () {
        return visitNumber;
    }

    /**
     * @param visitNumber
     *            the visitNumber to set
     */
    public void setVisitNumber ( final int visitNumber ) {
        this.visitNumber = visitNumber;
    }

    /**
     * @return the appointment
     */
    @Override
    public VaccinationAppointmentRequest getAppointment () {
        return appointment;
    }

    /**
     * @param appointment
     *            the appointment to set
     */
    public void setAppointment ( final VaccinationAppointmentRequest appointment ) {
        this.appointment = appointment;
    }

    /**
     * Validate that the age of the patient is within the age range of the
     * vaccine they are receiving
     */
    public void validateAge () {
        final LocalDate appointmentDate = date.toLocalDate();
        final Period period = Period.between( patient.getDateOfBirth(), appointmentDate );
        final int age = period.getYears();

        if ( ! ( age > vaccine.getAgeMin() && age < vaccine.getAgeMax() ) ) {
            throw new IllegalArgumentException( "Invalid age for this vaccine" );
        }
    }

    /**
     * If the hcp/vaccinator attempts to document a follow-up visit before
     * documenting an initial visit, there will be an error
     */
    public void validateVisitNumber () {
        if ( vaccine.getIfSecondDose() ) {
            if ( patient.getVaccinationStatus().equals( 0 ) && visitNumber == 2 ) {
                throw new IllegalArgumentException(
                        "Can't document a second appointment if there has not been a previous one." );
            }
        }
    }

    /**
     * Updates the vaccination status based on a change in visitNumber
     */
    public void updatePatientVaccinationStatus () {
        if ( !vaccine.ifSecondDose && visitNumber == 1 || vaccine.ifSecondDose && visitNumber == 2 ) {
            patient.setVaccinationStatus( PatientVaccinationStatus.FULLY_VACCINATED );
        }
        if ( vaccine.ifSecondDose && visitNumber == 1 ) {
            patient.setVaccinationStatus( PatientVaccinationStatus.PARTIALLY_VACCINATED );
        }
    }
}
