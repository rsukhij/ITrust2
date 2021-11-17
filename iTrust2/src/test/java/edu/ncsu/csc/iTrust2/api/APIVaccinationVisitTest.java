package edu.ncsu.csc.iTrust2.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.iTrust2.common.TestUtils;
import edu.ncsu.csc.iTrust2.forms.AppointmentRequestForm;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.forms.VaccinationVisitForm;
import edu.ncsu.csc.iTrust2.models.AppointmentRequest;
import edu.ncsu.csc.iTrust2.models.Hospital;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.Vaccine;
import edu.ncsu.csc.iTrust2.models.enums.BloodType;
import edu.ncsu.csc.iTrust2.models.enums.Ethnicity;
import edu.ncsu.csc.iTrust2.models.enums.Gender;
import edu.ncsu.csc.iTrust2.models.enums.PatientVaccinationStatus;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.models.enums.State;
import edu.ncsu.csc.iTrust2.models.enums.Status;
import edu.ncsu.csc.iTrust2.services.HospitalService;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.services.VaccinationAppointmentRequestService;
import edu.ncsu.csc.iTrust2.services.VaccinationVisitService;
import edu.ncsu.csc.iTrust2.services.VaccineService;

/**
 * Test for the API functionality for interacting with vaccination visits
 *
 * @author Rohan Sukhija
 * @author Justin Takamiya
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIVaccinationVisitTest {

    private MockMvc                         mvc;

    @Autowired
    private WebApplicationContext           context;

    @Autowired
    private VaccinationVisitService              vaccVisitService;

    @Autowired
    private UserService                     userService;

    @Autowired
    private VaccinationAppointmentRequestService       vaccappointmentRequestService;

    @Autowired
    private HospitalService                 hospitalService;

    @Autowired
    private VaccineService                  vaccService;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        
        vaccService.deleteAll();

        vaccVisitService.deleteAll();

        vaccappointmentRequestService.deleteAll();

        final User patient = new Patient( new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 ) );

        final User hcp = new Personnel( new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 ) );

        final Patient antti = buildPatient();

        userService.saveAll( List.of( patient, hcp, antti ) );

        final Hospital hosp = new Hospital();
        hosp.setAddress( "123 Raleigh Road" );
        hosp.setState( State.NC );
        hosp.setZip( "27514" );
        hosp.setName( "iTrust Test Hospital 2" );

        hospitalService.save( hosp );

    }

    /**
     * Tests VaccinationVisitAPI
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testVaccinationVisitAPI () throws Exception {
    	
        Assert.assertEquals( 0, vaccVisitService.count() );
    	
        // Set patient date of birth to eligible for vaccine
        User patient = userService.findByName( "patient" );
        ( (Patient) patient ).setDateOfBirth( LocalDate.of( 2005, 5, 25 ) );

        // Create Vaccine
        final Vaccine v4 = new Vaccine();
        v4.setName( "Pfizer" );
        v4.setAgeMax( 70 );
        v4.setAgeMin( 12 );
        v4.setDoseNumber( 1 );
        v4.setIfSecondDose( true );
        v4.setDaysBetween( 21 );
        v4.setIfAvailable( true );
        vaccService.save( v4 );

        // Create appointment request for Pfizer
        final AppointmentRequestForm appointmentForm = new AppointmentRequestForm();
        // 2030-11-19 4:50 AM EST
        appointmentForm.setDate( "2030-11-19T04:50:00.000-05:00" );
        appointmentForm.setType( "VACCINATION" );
        appointmentForm.setHcp( "hcp" );
        appointmentForm.setPatient( "patient" );
        appointmentForm.setComments( "Test appointment please ignore" );
        appointmentForm.setVaccineType( "Pfizer" );
        final AppointmentRequest req = vaccappointmentRequestService.build( appointmentForm );
        req.setStatus( Status.APPROVED );
        vaccappointmentRequestService.save( req );

        // Create preschdeuled office visit for pfizer vaccine
        final VaccinationVisitForm visit = new VaccinationVisitForm();
        visit.setPreScheduled( "yes" );
        visit.setDate( "2030-11-09T04:50:00.000-05:00" );
        visit.setHcp( "hcp" );
        visit.setPatient( "patient" );
        visit.setType(v4);
        visit.setHospital( "iTrust Test Hospital 2" );
        visit.setFdate("2030-11-30T04:50:00.000-05:00");
        visit.setAppointment("appointment");
          
        mvc.perform( post( "/api/v1/vaccinationvisits" )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( visit ) ) ).andExpect(status().isOk());

        Assert.assertEquals( 1, vaccService.count());
        Assert.assertEquals( 1, vaccappointmentRequestService.count() );
        Assert.assertEquals( 1, vaccVisitService.count() );

        patient = userService.findByName( "patient" );

        // Make sure patient is now partially vaccinated
        Assert.assertEquals( PatientVaccinationStatus.PARTIALLY_VACCINATED,( (Patient) patient ).getVaccinationStatus() );

        vaccVisitService.deleteAll();

        Assert.assertEquals( 0, vaccVisitService.count() );

        visit.setDate( "2030-12-19T04:50:00.000-05:00" );
        
        // setting a pre-scheduled appointment that doesn't match should not
        // work.
        mvc.perform( post( "/api/v1/vaccinationvisits" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( visit ) ) ).andExpect( status().isBadRequest() );

    }

    private Patient buildPatient () {
        final Patient antti = new Patient( new UserForm( "antti", "123456", Role.ROLE_PATIENT, 1 ) );

        antti.setAddress1( "1 Test Street" );
        antti.setAddress2( "Some Location" );
        antti.setBloodType( BloodType.APos );
        antti.setCity( "Viipuri" );
        final LocalDate date = LocalDate.of( 1977, 6, 15 );
        antti.setDateOfBirth( date );
        antti.setEmail( "antti@itrust.fi" );
        antti.setEthnicity( Ethnicity.Caucasian );
        antti.setFirstName( "Antti" );
        antti.setGender( Gender.Male );
        antti.setLastName( "Walhelm" );
        antti.setPhone( "123-456-7890" );
        antti.setState( State.NC );
        antti.setZip( "27514" );

        return antti;
    }
}