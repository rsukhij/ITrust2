package edu.ncsu.csc.iTrust2.models;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import edu.ncsu.csc.iTrust2.models.enums.PatientVaccinationStatus;

/**
 * Model class that creates the vaccine and the methods needed for it
 *
 * @author Sara Sophia Masood, Lily Cummings, Rohan Sukhij
 *
 */
@Entity
public class Vaccine extends DomainObject {

    /**
     * String field to hold the name
     */
    @Id
    String  name;

    /**
     * int field to hold the minimum age
     */
    int     ageMin;
    /**
     * int field to hold the maximum age
     */
    int     ageMax;
    // String ageRange;
    /**
     * int field to hold the number of doses
     */
    int     doseNumber;
    /**
     * boolean field to hold if the patient needs a second dose
     */
    boolean ifSecondDose;
    /**
     * int field to hold the number of days between vaccinations
     */
    int     daysBetween;
    /**
     * boolean field to hold if the vaccine is available for use
     */
    boolean ifAvailable;

    /**
     * Constructor method for the vaccine, sets all of the fields initially
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
     * Getter method for the name
     *
     * @return the name of the vaccine
     */
    public String getName () {
        return name;
    }

    /**
     * Setter method for the name
     *
     * @param name
     *            the name of the vaccine
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Getter method for the minimum age
     *
     * @return the minimum age
     */
    public int getAgeMin () {
        return ageMin;
    }

    /**
     * Setter method for the minimum age
     *
     * @param ageMin
     *            the minimum age
     */
    public void setAgeMin ( final int ageMin ) {
        this.ageMin = ageMin;
    }

    /**
     * Getter method for the maximum age
     *
     * @return the maximum age
     */
    public int getAgeMax () {
        return ageMax;
    }

    /**
     * Setter method for the maximum age
     *
     * @param ageMax
     */
    public void setAgeMax ( final int ageMax ) {
        this.ageMax = ageMax;
    }

    /**
     * Getter method for the dose number
     *
     * @return the dose number
     */
    public int getDoseNumber () {
        return doseNumber;
    }

    /**
     * Setter method for the dose number
     *
     * @param doseNumber
     *            the number of doses for the vaccine
     */
    public void setDoseNumber ( final int doseNumber ) {
        this.doseNumber = doseNumber;
    }

    /**
     * Getter method for the second dosage checker
     *
     * @return true if second dose needed
     */
    public boolean getIfSecondDose () {
        return ifSecondDose;
    }

    /**
     * Setter method for the second dosage checker
     *
     * @param ifSecondDose
     *            true if second dose needed
     */
    public void setIfSecondDose ( final boolean ifSecondDose ) {
        this.ifSecondDose = ifSecondDose;
    }

    /**
     * Getter method for the days between vaccinations
     *
     * @return the days between vaccinations
     */
    public int getDaysBetween () {
        return daysBetween;
    }

    /**
     * Setter method for the days between vaccinations
     *
     * @param daysBetween
     *            the number of days between vaccinations
     */
    public void setDaysBetween ( final int daysBetween ) {
        this.daysBetween = daysBetween;
    }

    /**
     * Getter method for if the vaccine is available for administration
     *
     * @return true if the vaccine is available
     */
    public boolean getIfAvailable () {
        return ifAvailable;
    }

    /**
     * Setter method for the availability of the vaccine
     *
     * @param ifAvailable
     *            true if the vaccine is available
     */
    public void setIfAvailable ( final boolean ifAvailable ) {
        this.ifAvailable = ifAvailable;
    }

    /**
     * Edit method for the vaccine once created
     *
     * @param name
     *            the name of the vaccine
     * @param ageMin
     *            the minimum age the vaccine can be administered
     * @param ageMax
     *            the maximum age the vaccine can be administered
     * @param doseNumber
     *            the number of vaccine doses
     * @param ifSecondDose
     *            boolean for whether the vaccine requires a second dose
     * @param daysBetween
     *            the number of days between vaccinations
     * @param ifAvailable
     *            the availability of the vaccine
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
     * Hashcode method for the vaccine model
     *
     * @return the generated hash code for the vaccine
     */
    @Override
    public int hashCode () {
        return Objects.hash( ageMax, ageMin, daysBetween, doseNumber, ifAvailable, ifSecondDose, name );
    }

    /**
     * Equals method for the vaccine model
     *
     * @param obj
     *            the object to compare to
     * @return boolean as to if the object is equal
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
     * Method to determine a patient's eligibility for the vaccine
     *
     * @param patient
     *            the patient to check eligibility for
     * @return boolean as to if the patient is eligible or not
     */
    public boolean isEligible ( final Patient patient ) {
        if ( patient.getDateOfBirth() == null ) {
            return false;
        }
        final long daysAfterBirth = Duration
                .between( patient.getDateOfBirth().atStartOfDay(), LocalDate.now().atStartOfDay() ).toDays();
        final int approxAge = (int) ( daysAfterBirth / 365.2425 );
        if ( approxAge < ageMin || approxAge > ageMax ) {
            return false;
        }
        if ( patient.getVaccinationStatus() == PatientVaccinationStatus.FULLY_VACCINATED ) {
            return false;
        }
        return true;
    }

    /**
     * toString method for the vaccine to output its information in string
     * format
     *
     * @return String with vaccine information
     */
    @Override
    public String toString () {
        return "Vaccine [name=" + name + ", ageMin=" + ageMin + ", ageMax=" + ageMax + ", doseNumber=" + doseNumber
                + ", ifSecondDose=" + ifSecondDose + ", daysBetween=" + daysBetween + ", ifAvailable=" + ifAvailable
                + "]";
    }

    /**
     * Getter method for the id; not implemented yet
     */
    @Override
    public Serializable getId () {
        // TODO Auto-generated method stub
        return null;
    }

}
