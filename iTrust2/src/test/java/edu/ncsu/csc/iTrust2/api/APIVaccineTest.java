package edu.ncsu.csc.iTrust2.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.Test;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.ncsu.csc.iTrust2.common.TestUtils;
import edu.ncsu.csc.iTrust2.models.Vaccine;
import edu.ncsu.csc.iTrust2.services.VaccineService;

/**
 * Class for testing vaccine API.
 *
 * @author Team 5 section 202 Lily Cummings
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
class APIVaccineTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private VaccineService        service;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        service.deleteAll();
    }

    /**
     * Tests basic vaccine API functionality.
     *
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = { "USER", "ADMIN" } )
    public void testVaccineAPI () throws UnsupportedEncodingException, Exception {
        // Create vaccines for testing
        final Vaccine vac1 = new Vaccine();
        vac1.setName( "Pfizer" );
        vac1.setAgeMax( 85 );
        vac1.setAgeMin( 12 );
        vac1.setDoseNumber( 2 );
        vac1.setDaysBetween( 28 );
        vac1.setIfAvailable( true );
        vac1.setIfSecondDose( false );

        final Vaccine vac2 = new Vaccine();
        vac2.setName( "Johnson & Johnson" );
        vac2.setAgeMax( 90 );
        vac2.setAgeMin( 12 );
        vac2.setDoseNumber( 1 );
        vac2.setDaysBetween( 35 );
        vac2.setIfAvailable( true );
        vac2.setIfSecondDose( false );

        // Add vaccine1 to system
        final String content1 = mvc
                .perform( post( "/iTrust2/api/v1/addVaccine" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( vac1 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        // Parse response as Vaccine object
        final Gson gson = new GsonBuilder().create();
        final Vaccine vaccine1 = gson.fromJson( content1, Vaccine.class );
        assertEquals( vac1.getAgeMax(), vaccine1.getAgeMax() );
        assertEquals( vac1.getName(), vaccine1.getName() );
        assertEquals( vac1.getAgeMin(), vaccine1.getAgeMin() );
        assertEquals( vac1.getDaysBetween(), vaccine1.getDaysBetween() );
        assertEquals( vac1.getDoseNumber(), vaccine1.getDoseNumber() );
        assertEquals( vac1.getIfAvailable(), vaccine1.getIfAvailable() );
        assertEquals( vac1.getIfSecondDose(), vaccine1.getIfSecondDose() );

        // Attempt to add same vaccine twice
        mvc.perform( post( "/iTrust2/api/v1/addVaccine" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( vac1 ) ) ).andExpect( status().isConflict() );

        // Add vaccine2 to system
        final String content2 = mvc
                .perform( post( "/iTrust2/api/v1/addVaccine" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( vac2 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final Vaccine vaccine2 = gson.fromJson( content2, Vaccine.class );
        assertEquals( vac2.getAgeMax(), vaccine2.getAgeMax() );
        assertEquals( vac2.getName(), vaccine2.getName() );
        assertEquals( vac2.getAgeMin(), vaccine2.getAgeMin() );
        assertEquals( vac2.getDaysBetween(), vaccine2.getDaysBetween() );
        assertEquals( vac2.getDoseNumber(), vaccine2.getDoseNumber() );
        assertEquals( vac2.getIfAvailable(), vaccine2.getIfAvailable() );
        assertEquals( vac2.getIfSecondDose(), vaccine2.getIfSecondDose() );

        // // Verify vaccines have been added
        mvc.perform( get( "/iTrust2/api/v1/addVaccine" ) ).andExpect( status().isOk() )
                .andExpect( content().string( Matchers.containsString( vac1.getName() ) ) )
                .andExpect( content().string( Matchers.containsString( vac2.getName() ) ) );

        // Edit first vaccine's minimum age
        vaccine1.setAgeMin( 9 );
        final String editContent = mvc
                .perform( put( "/iTrust2/api/v1/addVaccine" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( vaccine1 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final Vaccine editedVaccine = gson.fromJson( editContent, Vaccine.class );
        assertEquals( vac1.getAgeMax(), editedVaccine.getAgeMax() );
        assertEquals( vac1.getName(), editedVaccine.getName() );
        assertEquals( 9, vaccine1.getAgeMin() );
        assertEquals( vac1.getDaysBetween(), editedVaccine.getDaysBetween() );
        assertEquals( vac1.getDoseNumber(), editedVaccine.getDoseNumber() );
        assertEquals( vac1.getIfAvailable(), editedVaccine.getIfAvailable() );
        assertEquals( vac1.getIfSecondDose(), editedVaccine.getIfSecondDose() );

        // Attempt invalid edit
        vaccine2.setAgeMin( 91 );
        mvc.perform( put( "/iTrust2/api/v1/addVaccine" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( vaccine2 ) ) ).andExpect( status().isConflict() );

        // Follow up with valid edit
        vaccine2.setAgeMin( 12 );
        mvc.perform( put( "/iTrust2/api/v1/addVaccine" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( vaccine2 ) ) ).andExpect( status().isOk() );

    }

}
