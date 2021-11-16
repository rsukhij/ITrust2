package edu.ncsu.csc.iTrust2.forms;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import edu.ncsu.csc.iTrust2.models.VaccinationVisit;

/**
 * Vaccination Visit form used to document an Vaccination Visit by the HCP or
 * Vaccinator. This will be validated and converted to a VaccinationVisit to be
 * stored in the database.
 *
 * @author Lily Cummings
 *
 */
public class VaccinationVisitForm implements Serializable {
    /**
     * Serial Version of the Form. For the Serializable
     */
    private static final long serialVersionUID = 1L;

    /**
     * Empty constructor so that we can create an Vaccination Visit form for the
     * user to fill out
     */
    public VaccinationVisitForm () {
    }

    /**
     * Name of the Patient involved in the VaccinationVisit
     */
    @NotEmpty
    private String patient;

    /**
     * Name of the HCP involved in the VaccinationVisit
     */
    @NotEmpty
    private String hcp;

    /**
     * Date at which the VaccinationVisit occurred
     */
    @NotEmpty
    private String date;

    /**
     * ID of the VaccinationVisit
     */
    private String id;

    /**
     * Type of the Vaccine.
     */
    @NotEmpty
    private String type;

    /**
     * Dose Number
     */
    @NotEmpty
    private int    dose;

    /**
     * Whether the VaccinationVisit was prescheduled or not
     */
    public String  preScheduled;

    /**
     * Creates an VaccinationVisitForm from the VaccinationVisit provided
     *
     * @param ov
     *            VaccinationVisit to turn into an VaccinationVisitForm
     */
    public VaccinationVisitForm ( final VaccinationVisit ov ) {
        setPatient( ov.getPatient().getUsername() );
        setHcp( ov.getHcp().getUsername() );
        setDate( ov.getDate().toString() );
        setPreScheduled( ( (Boolean) ( ov.getAppointment() != null ) ).toString() );
    }

    /**
     * Get the patient in the VaccinationVisit
     *
     * @return The patient's username
     */
    public String getPatient () {
        return this.patient;
    }

    /**
     * Sets a patient on the VaccinationVisitForm
     *
     * @param patient
     *            The patient's username
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

    /**
     * Retrieves the HCP on the VaccinationVisit
     *
     * @return Username of the HCP on the VaccinationVisit
     */
    public String getHcp () {
        return this.hcp;
    }

    /**
     * Set a HCP on the VaccinationVisitForm
     *
     * @param hcp
     *            The HCP's username
     */
    public void setHcp ( final String hcp ) {
        this.hcp = hcp;
    }

    /**
     * Retrieves the date that the VaccinationVisit occurred at
     *
     * @return Date of the VaccinationVisit
     */
    public String getDate () {
        return this.date;
    }

    /**
     * Sets the date that the VaccinationVisit occurred at
     *
     * @param date
     *            The date of the vaccination visit
     */
    public void setDate ( final String date ) {
        this.date = date;
    }

    public int getDose () {
        return dose;
    }

    public void setDose ( final int dose ) {
        this.dose = dose;
    }

    /**
     * Gets the ID of the VaccinationVisit
     *
     * @return ID of the Visit
     */
    public String getId () {
        return this.id;
    }

    /**
     * Sets the ID of the VaccinationVisit
     *
     * @param id
     *            The ID of the VaccinationVisit
     */
    public void setId ( final String id ) {
        this.id = id;
    }

    /**
     * Gets the Type of the VaccinationVisit
     *
     * @return Type of the visit
     */
    public String getType () {
        return this.type;
    }

    /**
     * Sets the Type of the VaccinationVisit
     *
     * @param type
     *            New Type to set
     */
    public void setType ( final String type ) {
        this.type = type;
    }

    /**
     * Sets whether the visit was prescheduled
     *
     * @param prescheduled
     *            Whether the Visit is prescheduled or not
     */
    public void setPreScheduled ( final String prescheduled ) {
        this.preScheduled = prescheduled;
    }

    /**
     * Gets whether the visit was prescheduled or not
     *
     * @return Whether the visit was prescheduled
     */
    public String getPreScheduled () {
        return this.preScheduled;
    }

}
