package edu.ncsu.csc.iTrust2.api;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.UserService;
// import edu.ncsu.csc.iTrust2.services.VaccinationVisitService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

/**
 * Test for API functionality for downloading a vaccination certificate
 *
 * @author Sara Sophia Masood
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIVaccinationCertificateTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    // @Autowired
    // private VaccinationVisitService visitservice;
    //
    // private APIVaccinatio
    // nCertificateController controller;

    private UserService<User>     userService;

    /**
     * Field to hold the instance of the LoggerUtil
     */
    @Autowired
    private LoggerUtil            loggerUtil;

    /**
     * Sets up tests
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        final User patient = new Patient( new UserForm( "patient", "123456", Role.ROLE_PATIENT, 1 ) );

    }

    /**
     * Method to download a vaccine certificate for a user
     *
     * @return the success/failure message as to whether a pdf is created
     */
    // @SuppressWarnings ( { "rawtypes", "unchecked" } )

    // @PreAuthorize ( "hasAnyRole('ROLE_PATIENT')" )
    // @Test
    // @WithMockUser ( username = "patient", roles = { "PATIENT" } )
    // @Transactional
    // public ResponseEntity<byte[]> testDownloadCertificate () {
    //
    // mvc.perform( get( "/api/v1/vaccinecertificate" ) ).andExpect(
    // status().isOk() )
    // .andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) );
    //
    // // .downloadCertificate();
    //
    // }
    //
    // @Test
    // @WithMockUser ( username = "patient", roles = { "PATIENT" } )
    // @Transactional
    // public ResponseEntity<byte[]> testDownloadCertificateFail () {
    //
    // mvc.perform( get( "/api/v1/vaccinecertificate" ) ).andExpect(
    // status().isNotFound() )
    // .andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) );
    //
    // try {
    //
    // }
    // catch ( final Exception e ) {
    //
    // }
    //
    // }
}
