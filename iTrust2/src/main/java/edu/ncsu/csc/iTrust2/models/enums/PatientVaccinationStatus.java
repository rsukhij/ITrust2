package edu.ncsu.csc.iTrust2.models.enums;

/**
 * Enum for the different COVID-19 vaccination statuses a patient can have
 *
 * @author sukhi
 *
 */
public enum PatientVaccinationStatus {

    NON_APPLICABLE ( 0 ),
    /**
     * Patient has no vaccination doses
     */
    NO_VACCINATION ( 1 ),
    /**
     * Patient has some but not all vaccination doses
     */
    PARTIALLY_VACCINATED ( 2 ),
    /**
     * Patient has received all vaccination doses
     */
    FULLY_VACCINATED ( 3 );

    /**
     * Code of the status
     */
    private int code;

    /**
     * Create a Status from the numerical code.
     *
     * @param code
     *            Code of the Status
     */
    private PatientVaccinationStatus ( final int code ) {
        this.code = code;
    }

    /**
     * Gets the numerical Code of the Status
     *
     * @return Code of the Status
     */
    public int getCode () {
        return code;
    }

    /**
     * Converts a code to a named smoking status.
     *
     * @param code
     *            The smoking code.
     * @return The string represented by the code.
     */
    public static String getName ( final int code ) {
        return PatientVaccinationStatus.parseValue( code ).toString();
    }

    /**
     * Returns the PatientSmokingStatus enum that matches the given code.
     *
     * @param code
     *            The code to match
     * @return Corresponding PatientSmokingStatus object.
     */
    public static PatientVaccinationStatus parseValue ( final int code ) {
        for ( final PatientVaccinationStatus status : values() ) {
            if ( status.getCode() == code ) {
                return status;
            }
        }
        return PatientVaccinationStatus.NON_APPLICABLE;
    }

}
