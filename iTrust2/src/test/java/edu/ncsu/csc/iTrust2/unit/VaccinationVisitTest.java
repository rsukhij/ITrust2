package edu.ncsu.csc.iTrust2.unit;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;
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
import edu.ncsu.csc.iTrust2.models.BasicHealthMetrics;
import edu.ncsu.csc.iTrust2.models.Hospital;
import edu.ncsu.csc.iTrust2.models.OfficeVisit;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccinationAppointmentRequest;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;
import edu.ncsu.csc.iTrust2.models.Vaccine;
import edu.ncsu.csc.iTrust2.models.enums.AppointmentType;
import edu.ncsu.csc.iTrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.BasicHealthMetricsService;
import edu.ncsu.csc.iTrust2.services.HospitalService;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.services.VaccinationVisitService;
import edu.ncsu.csc.iTrust2.services.VaccineService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

/**
 * Tests Vaccination Visit.
 * Based off of OfficeVisitTest since VaccinationVisit extends OfficeVisit.
 * 
 * @author Justin Takamiya (jjtakami@ncsu.edu)
 */
public class VaccinationVisitTest {

    @Autowired
    private VaccinationVisitService        vaccinationVisitService;
    
    @Autowired
    private BasicHealthMetricsService basicHealthMetricsService;

    @Autowired
    private HospitalService           hospitalService;

    @Autowired
    private UserService               userService;
   
    @Autowired
    private VaccineService            vaccineService;
    
    private User vaccinator;

    @Before
    public void setup () {
        vaccinationVisitService.deleteAll();
        vaccineService.deleteAll();

        final User hcp = new Personnel( new UserForm( "hcp", "123456", Role.ROLE_HCP, 1 ) );

        final User alice = new Patient( new UserForm( "AliceThirteen", "123456", Role.ROLE_PATIENT, 1 ) );
        
        vaccinator = new Personnel( new UserForm("vaccinator", "123456", Role.ROLE_VACCINATOR, 1));
        
        userService.saveAll( List.of( hcp, alice, vaccinator ) );
    }

    @Test
    @Transactional
    public void testVaccinationVisit () {
    	
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

        final VaccinationVisit visit = new VaccinationVisit();

        final BasicHealthMetrics bhm = new BasicHealthMetrics();

        bhm.setDiastolic( 150 );
        bhm.setDiastolic( 100 );
        bhm.setHcp( userService.findByName( "hcp" ) );
        bhm.setPatient( userService.findByName( "AliceThirteen" ) );
        bhm.setHdl( 75 );
        bhm.setHeight( 75f );
        bhm.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );

        basicHealthMetricsService.save( bhm );

        visit.setBasicHealthMetrics( bhm );
        visit.setType( AppointmentType.GENERAL_CHECKUP );
        visit.setHospital( hosp );
        visit.setPatient( userService.findByName( "AliceThirteen" ) );
        visit.setHcp( userService.findByName( "AliceThirteen" ) );
        visit.setDate( ZonedDateTime.now() );
        visit.setVaccines(v);
        visit.setVaccinator(userService.findByName("vaccinator"));
        vaccinationVisitService.save( visit );
        
        Assert.assertEquals( 1, vaccinationVisitService.count() );
    }
}
