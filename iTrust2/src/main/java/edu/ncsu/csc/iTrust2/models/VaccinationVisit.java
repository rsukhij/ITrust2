package edu.ncsu.csc.iTrust2.models;

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

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
     * The vaccinator of this vaccination visit
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "vaccinator_id", columnDefinition = "varchar(100)" )
    private User                          vaccinator;

    @NotNull
    @ManyToOne
    @JoinColumn ( name = "vaccine_name", columnDefinition = "varchar(100)" )
    private Vaccine                       vaccine;

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
     * @return the vaccines
     */
    public Vaccine getVaccines () {
        return vaccine;
    }

    /**
     * Set vaccine to whatever vaccine is selected by the HCP or Vaccinator
     *
     * @param vaccines
     *            the vaccines to set
     */
    public void setVaccines ( final Vaccine vaccine ) {
        this.vaccine = vaccine;
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
        final Patient p = (Patient) super.getPatient();
        final LocalDate appointmentDate = super.getDate().toLocalDate();
        final Period period = Period.between( p.getDateOfBirth(), appointmentDate );
        final int age = period.getYears();

        if ( ! ( age > vaccine.getAgeMin() && age < vaccine.getAgeMax() ) ) {
            throw new IllegalArgumentException( "Invalid age for this vaccine" );
        }
    }

    /**
     * Updates the vaccination status based on a change in visitNumber
     */
    public void updatePatientVaccinationStatus () {
        final Patient p = (Patient) super.getPatient();
        if ( !vaccine.ifSecondDose
                || vaccine.ifSecondDose && p.getVaccinationStatus() == PatientVaccinationStatus.PARTIALLY_VACCINATED ) {
            p.setVaccinationStatus( PatientVaccinationStatus.FULLY_VACCINATED );
        }

    }
}
