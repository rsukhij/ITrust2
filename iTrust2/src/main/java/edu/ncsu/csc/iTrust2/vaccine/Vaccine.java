package edu.ncsu.csc.iTrust2.vaccine;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import edu.ncsu.csc.iTrust2.models.DomainObject;

public class Vaccine extends DomainObject {

    String                name;
    int                   ageMin;
    int                   ageMax;
    // String ageRange;
    static int            doseNumber;
    boolean               ifSecondDose;
    int                   daysBetween;
    static boolean        ifAvailable;

    private List<Vaccine> vaccineList;

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

    public String getName () {
        return name;
    }

    public void setName ( final String name ) {
        this.name = name;
    }

    public int getAgeMin () {
        return ageMin;
    }

    public void setAgeMin ( final int ageMin ) {
        this.ageMin = ageMin;
    }

    public int getAgeMax () {
        return ageMax;
    }

    public void setAgeMax ( final int ageMax ) {
        this.ageMax = ageMax;
    }

    public static int getDoseNumber () {
        return doseNumber;
    }

    public void setDoseNumber ( final int doseNumber ) {
        this.doseNumber = doseNumber;
    }

    public boolean getIfSecondDose () {
        return ifSecondDose;
    }

    public void setIfSecondDose ( final boolean ifSecondDose ) {
        this.ifSecondDose = ifSecondDose;
    }

    public int getDaysBetween () {
        return daysBetween;
    }

    public void setDaysBetween ( final int daysBetween ) {
        this.daysBetween = daysBetween;
    }

    public static boolean getIfAvailable () {
        return ifAvailable;
    }

    public void setIfAvailable ( final boolean ifAvailable ) {
        this.ifAvailable = ifAvailable;
    }

    public void addVaccine ( final String name, final int ageMin, final int ageMax, final int doseNumber,
            final boolean ifSecondDose, final int daysBetween, final boolean ifAvailable ) {
        final Vaccine vaccine = new Vaccine();
        for ( int i = 0; i < vaccineList.size(); i++ ) {
            if ( name.equals( ( i ) ) ) {
                throw new IllegalArgumentException( "Duplicate Vaccines are not allowed." );
            }
        }
        vaccine.setName( name );
        if ( ageMax < ageMin ) {
            throw new IllegalArgumentException();
        }
        vaccine.setAgeMin( ageMin );
        vaccine.setAgeMax( ageMax );
        vaccine.setDoseNumber( doseNumber );
        vaccine.setIfSecondDose( ifSecondDose );
        vaccine.setDaysBetween( daysBetween );
        vaccine.setIfAvailable( ifAvailable );

        vaccineList.add( vaccine );

    }

    public void editVaccine ( final String name, final int ageMin, final int ageMax, final int doseNumber,
            final boolean ifSecondDose, final int daysBetween, final boolean ifAvailable ) {

        final Vaccine vaccine = new Vaccine();
        vaccine.setName( name );
        vaccine.setAgeMin( ageMin );
        vaccine.setAgeMax( ageMax );
        vaccine.setDoseNumber( doseNumber );
        vaccine.setIfSecondDose( ifSecondDose );
        vaccine.setDaysBetween( daysBetween );
        vaccine.setIfAvailable( ifAvailable );

        vaccineList.add( vaccine );

    }

    public void updateVaccine ( final String vaccine, final boolean ifAvailable ) {
        for ( int i = 0; i < vaccineList.size(); i++ ) {
            if ( vaccineList.get( i ).getName().equals( ( vaccine ) ) ) {
                vaccineList.get( i ).setIfAvailable( ifAvailable );
            }
        }
    }

    public List<Vaccine> listVaccinesAvailable () {
        return vaccineList;
    }

    @Override
    public int hashCode () {
        return Objects.hash( ageMax, ageMin, daysBetween, doseNumber, ifAvailable, ifSecondDose, name );
    }

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

    @Override
    public String toString () {
        return "Vaccine [name=" + name + ", ageMin=" + ageMin + ", ageMax=" + ageMax + ", doseNumber=" + doseNumber
                + ", ifSecondDose=" + ifSecondDose + ", daysBetween=" + daysBetween + ", ifAvailable=" + ifAvailable
                + "]";
    }

    @Override
    public Serializable getId () {
        // TODO Auto-generated method stub
        return null;
    }

}
