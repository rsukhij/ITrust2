package edu.ncsu.csc.iTrust2.unit;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Hospital;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccinationAppointmentRequest;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;
import edu.ncsu.csc.iTrust2.models.Vaccine;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.HospitalService;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.services.VaccinationVisitService;
import edu.ncsu.csc.iTrust2.services.VaccineService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class VaccinationVisitTest {

    @Autowired
    private VaccinationVisitService        vaccinationVisitService;

    @Autowired
    private HospitalService           hospitalService;

    @Autowired
    private UserService               userService;
   
    @Autowired
    private VaccineService            vaccineService;

    @Before
    public void setup () {
        vaccinationVisitService.deleteAll();

        final User hcp = new Personnel( new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 ) );

        final User alice = new Patient( new UserForm( "AliceThirteen", "123456", Role.ROLE_PATIENT, 1 ) );
        
        final User vaccinator = new Personnel( new UserForm("vaccinator", "123456", Role.ROLE_VACCINATOR, 1));
        
        userService.saveAll( List.of( hcp, alice, vaccinator ) );
    }

    @Test
    @Transactional
    public void testVaccinationVisit () {
    	
    	vaccinationVisitService.deleteAll();
    	
        Assert.assertEquals( 0, vaccinationVisitService.count() );

        final Hospital hosp = new Hospital( "Dr. Jenkins' Insane Asylum", "123 Main St", "12345", "NC" );
        hospitalService.save( hosp );
        
        final Vaccine v = new Vaccine();
        v.setName( "Pfizer" );
        v.setAgeMax( 100 );
        v.setAgeMin( 12 );
        v.setDoseNumber( 1 );
        v.setIfSecondDose( true );
        v.setDaysBetween( 21 );
        v.setIfAvailable( true );
        
        vaccineService.save(v);
        
        final VaccinationAppointmentRequest var = new VaccinationAppointmentRequest();
        
        var.setVaccineType(v);

        final VaccinationVisit visit = new VaccinationVisit();
       
        // Adding information specific to VaccinationVisit.
        
        // Setting the vaccinator
        visit.setVaccinator( userService.findByName( "vaccinator" ));
        // Setting the vaccination appointment request
        visit.setAppointment(var);
        // Setting the vaccine
        visit.setVaccines(v);
                
        assertEquals(var, visit.getAppointment());
        
        assertEquals(v, visit.getVaccines());
        
        User vaccinator = visit.getVaccinator();
        
        assertEquals(vaccinator, visit.getVaccinator());
        
    }
}

