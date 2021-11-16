package edu.ncsu.csc.iTrust2.models;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

// import edu.ncsu.csc.iTrust2.models.enums.PatientVaccinationStatus;

/**
 * Vaccine Class that extends the DomainObject Class. Has instance variables for
 * ageMin and ageMax, as well as dose number, ifSecondDose, daysBetween, and
 * ifAvailable. This class is used by authenticated administrators only, and
 * includes the information they are inputting for a vaccine.
 *
 * @author Sara Sophia Masood
 *
 */
@Entity
public class Vaccine extends DomainObject {

    /**
     * Name of Vaccine
     */
    @Id
    String  name;

    /** Approved Vaccine age minimum */
    int     ageMin;
    /** Approved Vaccine age maximum */
    int     ageMax;
    /** Dose of the vaccine the patient is recieving(either first or second) */
    int     doseNumber;
    /** If this is the second dose of the vaccine */
    boolean ifSecondDose;
    /** Number of days between vaccination dates */
    int     daysBetween;
    /** If the vaccine is currently available */
    boolean ifAvailable;

    /**
     * Constructor for Vaccine class.
     */
    public Vaccine () {
        setName( name );
        setAgeMin( ageMin );
        setAgeMax( ageMax );
        setDoseNumber( doseNumber );
        setIfSecondDose( ifSecondDose );
        setDaysBetween( daysBetween );
        setIfAvailable( ifAvailable );
        // vaccineList = new ArrayList<Vaccine>();
    }

    /**
     * Getter method for vaccineName
     *
     * @return name vaccine name
     */
    public String getName () {
        return name;
    }

    /**
     * Setter method for vaccineName
     *
     * @param name
     *            vaccine name
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Getter method for ageMin
     *
     * @return ageMin approved vaccine age minimum
     */
    public int getAgeMin () {
        return ageMin;
    }

    /**
     * Setter method for ageMin
     *
     * @param ageMin
     *            approved vaccine age minimum
     */
    public void setAgeMin ( final int ageMin ) {
        this.ageMin = ageMin;
    }

    /**
     * Getter method for ageMax
     *
     * @return ageMax approved vaccine age maximum
     */
    public int getAgeMax () {
        return ageMax;
    }

    /**
     * Setter method for ageMax
     *
     * @param ageMax
     *            approved vaccine age maximum
     */
    public void setAgeMax ( final int ageMax ) {
        this.ageMax = ageMax;
    }

    /**
     * Getter method for doseNumber
     *
     * @return doseNumber what dose of the vaccine the patient is
     *         recieving(either first or second)
     */
    public int getDoseNumber () {
        return doseNumber;
    }

    /**
     * Setter method for doseNumber
     *
     * @param doseNumber
     *            what dose of the vaccine the patient is recieving(either first
     *            or second)
     */
    public void setDoseNumber ( final int doseNumber ) {
        this.doseNumber = doseNumber;
    }

    /**
     * Getter method for secondDose
     *
     * @return ifSecondDose if this is the second dose of the vaccine
     */
    public boolean getIfSecondDose () {
        return ifSecondDose;
    }

    /**
     * Setter method for secondDose
     *
     * @param ifSecondDose
     *            if this is the second dose of the vaccine
     */
    public void setIfSecondDose ( final boolean ifSecondDose ) {
        this.ifSecondDose = ifSecondDose;
    }

    /**
     * Getter method for daysBetween
     *
     * @return daysBetween number of days between vaccination dates
     */
    public int getDaysBetween () {
        return daysBetween;
    }

    /**
     * Setter method for daysBetween
     *
     * @param daysBetween
     *            number of days between vaccination dates
     */
    public void setDaysBetween ( final int daysBetween ) {
        this.daysBetween = daysBetween;
    }

    /**
     * Getter method for ifAvailable
     *
     * @return ifAvailable if vaccine is currently available
     */
    public boolean getIfAvailable () {
        return ifAvailable;
    }

    /**
     * Setter method for ifAvailable
     *
     * @param ifAvailable
     *            if vaccine is currently available
     */
    public void setIfAvailable ( final boolean ifAvailable ) {
        this.ifAvailable = ifAvailable;
    }

    /**
     * Edit vaccine method. This enables the administrator to go back to the
     * saved vaccine and further edit it.
     *
     * @param name
     *            vaccine name
     * @param ageMin
     *            approved vaccine age minimum
     * @param ageMax
     *            approved vaccine age maximum
     * @param doseNumber
     *            what dose of the vaccine the patient is recieving(either first
     *            or second)
     * @param ifSecondDose
     *            if this is the second dose of the vaccine
     * @param daysBetween
     *            number of days between vaccination dates
     * @param ifAvailable
     *            if the vaccine is currently available
     */
    public void editVaccine ( final String name, final int ageMin, final int ageMax, final int doseNumber,
            final boolean ifSecondDose, final int daysBetween, final boolean ifAvailable ) {

        setName( name );
        setAgeMin( ageMin );
        setAgeMax( ageMax );
        setDoseNumber( doseNumber );
        setIfSecondDose( ifSecondDose );
        setDaysBetween( daysBetween );
        setIfAvailable( ifAvailable );

    }

    /**
     * Hashcode method.
     */
    @Override
    public int hashCode () {
        return Objects.hash( ageMax, ageMin, daysBetween, doseNumber, ifAvailable, ifSecondDose, name );
    }

    /**
     * Equals object.
     */
    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Vaccine other = (Vaccine) obj;
        return ageMax == other.ageMax && ageMin == other.ageMin && daysBetween == other.daysBetween
                && doseNumber == other.doseNumber && ifAvailable == other.ifAvailable
                && ifSecondDose == other.ifSecondDose && Objects.equals( name, other.name );
    }

    /**
     * Method to check id the patient is eligible for receiving a vaccine
     *
     * @param patient
     *            being treated
     * @return true if patient is eligible
     */
    public boolean isEligible ( final Patient patient ) {
        final long daysAfterBirth = Duration
                .between( patient.getDateOfBirth().atStartOfDay(), LocalDate.now().atStartOfDay() ).toDays();
        final int approxAge = (int) ( daysAfterBirth / 365.2425 );
        if ( approxAge < ageMin || approxAge > ageMax ) {
            return false;
        }
        // if ( patient.getVaccinationStatus() ==
        // PatientVaccinationStatus.FULLY_VACCINATED ) {
        // return false;
        // }
        return true;
    }

    /**
     * ToString method that returns a string of the vaccine information.
     *
     * @return String of vaccine information
     */
    @Override
    public String toString () {
        return "Vaccine [name=" + name + ", ageMin=" + ageMin + ", ageMax=" + ageMax + ", doseNumber=" + doseNumber
                + ", ifSecondDose=" + ifSecondDose + ", daysBetween=" + daysBetween + ", ifAvailable=" + ifAvailable
                + "]";
    }

    /**
     * GetId method.
     */
    @Override
    public Serializable getId () {
        // TODO Auto-generated method stub
        return null;
    }

}
