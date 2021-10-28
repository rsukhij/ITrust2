package edu.ncsu.csc.iTrust2.controllers.routing;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ncsu.csc.iTrust2.models.enums.Role;

/**
 * Controller class responsible for managing the behavior for the Vaccinator
 * Landing Screen
 *
 * @author Rohan Rehman
 *
 */
@Controller
public class VaccinatorController {

    /**
     * Returns the Landing screen for the Vaccinator
     *
     * @param model
     *            Data from the front end
     * @return The page to display
     */
    @RequestMapping ( value = "vaccinator/index" )
    @PreAuthorize ( "hasAnyRole('ROLE_VACCINATOR')" )
    public String index ( final Model model ) {
        return Role.ROLE_VACCINATOR.getLanding();
    }

    /**
     * Method responsible for Vaccinator's Accept/Reject requested appointment
     * functionality. This prepares the page.
     *
     * @param model
     *            Data for the front end
     * @return The page to display to the user
     */
    @GetMapping ( "/vaccinator/appointmentRequests" )
    @PreAuthorize ( "hasAnyRole('ROLE_VACCINATOR')" )
    public String requestAppointmentForm ( final Model model ) {
        return "vaccinator/appointmentRequests";
    }

    /**
     * Returns the form page for a Vaccinator to document a VaccinationVisit
     *
     * @param model
     *            The data for the front end
     * @return Page to display to the user
     */
    @GetMapping ( "/vaccinator/documentVaccinationVisit" )
    @PreAuthorize ( "hasAnyRole('ROLE_VACCINATOR')" )
    public String documentVaccinationVisit ( final Model model ) {
        return "/vaccinator/documentVaccinationVisit";
    }

}
