package edu.ncsu.csc.iTrust2.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.forms.VaccinationVisitForm;
import edu.ncsu.csc.iTrust2.models.Hospital;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;
import edu.ncsu.csc.iTrust2.models.Vaccine;
import edu.ncsu.csc.iTrust2.models.enums.BloodType;
import edu.ncsu.csc.iTrust2.models.enums.Ethnicity;
import edu.ncsu.csc.iTrust2.models.enums.Gender;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.models.enums.State;
import edu.ncsu.csc.iTrust2.services.HospitalService;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.services.VaccinationVisitService;
import edu.ncsu.csc.iTrust2.services.VaccineService;

/**
 * Test for the API functionality for interacting with vaccination visits.
 * Based off of OfficeVisitTest.
 *
 * @author Justin Takamiya (jjtakami@ncsu.edu)
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIVaccinationVisitTest {

    private MockMvc                   mvc;

    @Autowired
    private WebApplicationContext     context;
    
    @Autowired
    private VaccinationVisitService   vaccinationVisitService;

    @Autowired
    private UserService               userService;


    @Autowired
    private HospitalService           hospitalService;

    
    @Autowired
    private VaccineService            vaccineService;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        final User patient = new Patient( new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 ) );

//        final User hcp = new Personnel( new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 ) );
        final User hcp = new Personnel( new UserForm( "hcp", "123456", Role.ROLE_VACCINATOR, 1 ) );

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
     * Helper method for building a Patient.
     * @return the patient that was created
     */
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

    /**
     * Tests getting a non existent vaccination visit and ensures that the correct
     * status is returned.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testGetNonExistentVaccinationVisit () throws Exception {
        mvc.perform( get( "/api/v1/vaccinationvisits/-1" ) ).andExpect( status().isNotFound() );
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

        Assert.assertEquals( 0, vaccinationVisitService.count() );

        final VaccinationVisitForm visit = new VaccinationVisitForm();
        visit.setDate( "2030-11-19T04:50:00.000-05:00" );
        visit.setHcp( "hcp" );
        visit.setPatient( "patient" );
        
        // Create Vaccine
        final Vaccine pfizer = new Vaccine();
        pfizer.setName( "Pfizer" );
        pfizer.setAgeMax( 70 );
        pfizer.setAgeMin( 12 );
        pfizer.setDoseNumber( 1 );
        pfizer.setIfSecondDose( true );
        pfizer.setDaysBetween( 21 );
        pfizer.setIfAvailable( true );
        vaccineService.save( pfizer );
        
        visit.setType( pfizer );
        visit.setHospital( "iTrust Test Hospital 2" );

        /* Create the Vaccination Visit */
        mvc.perform( post( "/api/v1/vaccinationvisits" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( visit ) ) ).andExpect( status().isOk() );

        Assert.assertEquals( 1, vaccinationVisitService.count() );

        mvc.perform( get( "/api/v1/vaccinationvisits" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) );

        /* Test getForHCP and getForHCPAndPatient */
        VaccinationVisit v = vaccinationVisitService.build(visit);
        List<VaccinationVisit> vList = vaccinationVisitService.findByHcp( v.getHcp() );
        assertEquals( vList.get( 0 ).getHcp(), v.getHcp() );
        vList = vaccinationVisitService.findByHcpAndPatient( v.getHcp(), v.getPatient() );
        assertEquals( vList.get( 0 ).getHcp(), v.getHcp() );
        assertEquals( vList.get( 0 ).getPatient(), v.getPatient() );
        
    }

}