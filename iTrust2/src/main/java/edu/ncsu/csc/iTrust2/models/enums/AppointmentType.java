package edu.ncsu.csc.iTrust2.models.enums;

/**
 * Enum of all of the types of appointments that are recognized by the system.
 *
 * @author Kai Presler-Marshall
 *
 */
public enum AppointmentType {

    /**
     * General Checkup
     */
    GENERAL_CHECKUP ( 1 ),

    /**
     * General Ophthalmology Appointment
     */
    GENERAL_OPHTHALMOLOGY ( 2 ),

    /**
     * Ophthalmology Surgery
     */
    OPHTHALMOLOGY_SURGERY ( 3 ),

    /**
     * Vaccination Appt
     */
    VACCINATION ( 4 );

    /**
     * Numerical code of the AppointmentType
     */
    private int code;

    /**
     * Creates the AppointmentType from its code.
     *
     * @param code
     *            Code of the AppointmentType
     */
    private AppointmentType ( final int code ) {
        this.code = code;
    }

    /**
     * Gets the numerical code of the AppointmentType
     *
     * @return Code of the AppointmentType
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
        return AppointmentType.parseValue( code ).toString();
    }

    /**
     * Returns the PatientSmokingStatus enum that matches the given code.
     *
     * @param code
     *            The code to match
     * @return Corresponding PatientSmokingStatus object.
     */
    public static AppointmentType parseValue ( final int code ) {
        for ( final AppointmentType status : values() ) {
            if ( status.getCode() == code ) {
                return status;
            }
        }
        return AppointmentType.GENERAL_CHECKUP;
    }
}
