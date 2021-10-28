package edu.ncsu.csc.iTrust2.unit;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.Personnel;
import edu.ncsu.csc.iTrust2.models.VaccinationAppointmentRequest;
import edu.ncsu.csc.iTrust2.models.enums.Role;

/**
 * Class to test that AppointmentRequest are function properly
 *
 * @author Rohan Sukhija
 *
 */
@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class AppointmentRequestTest {

    private static final String USER_1 = "demoTestUser1";

    private static final String USER_2 = "demoTestUser2";

    private static final String PW     = "123456";

    @Test
    @Transactional
    public void testCreateAppointmentRequest () {
        final VaccinationAppointmentRequest req = new VaccinationAppointmentRequest();
        req.setComments( "test" );
        req.setHcp( new Personnel( new UserForm( "hcp", "hcp", Role.ROLE_HCP, 1 ) ) );
        req.setPatient( new Patient( new UserForm( USER_1, PW, Role.ROLE_PATIENT, 1 ) ) );
        assertEquals( "test", req.getComments() );
    }

}
