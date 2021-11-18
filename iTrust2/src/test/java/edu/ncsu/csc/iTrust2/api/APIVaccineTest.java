package edu.ncsu.csc.iTrust2.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import javax.transaction.Transactional;

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
import edu.ncsu.csc.iTrust2.models.Vaccine;
import edu.ncsu.csc.iTrust2.services.VaccineService;

/**
 * Class for testing drug API.
 *
 * @author Nicole Worth
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIVaccineTest {
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
     * Tests basic drug API functionality.
     *
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = { "USER", "ADMIN" } )
    public void testVaccineAPI () throws UnsupportedEncodingException, Exception {
        // Create vaccine for testing
        final Vaccine vac1 = new Vaccine();
        vac1.setName( "Pfizer" );
        vac1.setAgeMax( 85 );
        vac1.setAgeMin( 12 );
        vac1.setDoseNumber( 2 );
        vac1.setDaysBetween( 28 );
        vac1.setIfAvailable( true );
        vac1.setIfSecondDose( false );

        // Add vaccine 1 to system
        final String content1 = mvc
                .perform( post( "/api/v1/addVaccine" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( vac1 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertEquals( 1, service.count() );

        // Parse response as Vaccine object
        final Vaccine testVac = service.findByVaccineName( vac1.getName() );
        assertEquals( testVac.getAgeMax(), vac1.getAgeMax() );

        final Vaccine vac2 = new Vaccine();
        vac2.setName( "Johnson & Johnson" );
        vac2.setAgeMax( 90 );
        vac2.setAgeMin( 12 );
        vac2.setDoseNumber( 1 );
        vac2.setDaysBetween( 35 );
        vac2.setIfAvailable( true );
        vac2.setIfSecondDose( false );

        // Add vaccine 2 to system
        final String content2 = mvc
                .perform( post( "/api/v1/addVaccine" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( vac2 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertEquals( 2, service.count() );

        // Parse response as Vaccine object
        final Vaccine testVac2 = service.findByVaccineName( vac2.getName() );
        assertEquals( testVac2.getAgeMax(), vac2.getAgeMax() );

        // Edit existing vaccine
        vac1.setAgeMax( 95 );
        final String editContent = mvc
                .perform( put( "/api/v1/addVaccine" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( vac1 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        assertEquals( 2, service.count() );

        final Vaccine testVac1 = service.findByVaccineName( vac1.getName() );
        assertEquals( testVac1.getAgeMax(), vac1.getAgeMax() );
    }
}
