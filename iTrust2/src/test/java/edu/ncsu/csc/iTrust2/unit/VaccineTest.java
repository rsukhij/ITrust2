/**
 *
 */
package edu.ncsu.csc.iTrust2.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.models.Vaccine;

import edu.ncsu.csc.iTrust2.models.Vaccine;

/**
 * @author sarasophiamasood
 *
 */
@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class VaccineTest {

    private final List<Vaccine> vaccineList = new ArrayList<Vaccine>();

    /**
     * Test method for {@link edu.ncsu.csc.iTrust2.models.Vaccine#hashCode()}.
     */
    @Test
    @Transactional
    void testHashCode () {
        final Vaccine v = new Vaccine();
        v.setName( "Pfizer" );
        v.setAgeMax( 25 );
        v.setAgeMin( 20 );
        v.setDoseNumber( 1 );
        v.setIfSecondDose( false );
        v.setDaysBetween( 0 );
        v.setIfAvailable( true );

        assertEquals( "Pfizer", v.getName() );
        assertEquals( 25, v.getAgeMax() );
        assertEquals( 20, v.getAgeMin() );
        assertEquals( 1, v.getDoseNumber() );
        assertEquals( false, v.getIfSecondDose() );
        assertEquals( 0, v.getDaysBetween() );
        assertEquals( true, v.getIfAvailable() );

        final Vaccine v2 = new Vaccine();
        v.setName( "Moderna" );
        v.setAgeMax( 35 );
        v.setAgeMin( 30 );
        v.setDoseNumber( 1 );
        v.setIfSecondDose( false );
        v.setDaysBetween( 0 );
        v.setIfAvailable( true );

        final Vaccine v4 = new Vaccine();
        v4.setName( "Pfizer" );
        v4.setAgeMax( 25 );
        v4.setAgeMin( 20 );
        v4.setDoseNumber( 1 );
        v4.setIfSecondDose( false );
        v4.setDaysBetween( 0 );
        v4.setIfAvailable( true );

        assertEquals( "Pfizer", v4.getName() );
        assertEquals( 25, v4.getAgeMax() );
        assertEquals( 20, v4.getAgeMin() );
        assertEquals( 1, v4.getDoseNumber() );
        assertEquals( false, v4.getIfSecondDose() );
        assertEquals( 0, v4.getDaysBetween() );
        assertEquals( true, v4.getIfAvailable() );

        assertFalse( v.hashCode() == v2.hashCode() );
        assertFalse( v.hashCode() == v4.hashCode() );
        assertEquals( v.hashCode(), v.hashCode() );

    }

    /**
     * Test method for {@link edu.ncsu.csc.iTrust2.models.Vaccine#Vaccine()}.
     */
    @Test
    @Transactional
    void testVaccine () {
        final Vaccine v = new Vaccine();
        v.setName( "Pfizer" );
        v.setAgeMax( 25 );
        v.setAgeMin( 20 );
        v.setDoseNumber( 1 );
        v.setIfSecondDose( false );
        v.setDaysBetween( 0 );
        v.setIfAvailable( true );

        assertEquals( "Pfizer", v.getName() );
        assertEquals( 25, v.getAgeMax() );
        assertEquals( 20, v.getAgeMin() );
        assertEquals( 1, v.getDoseNumber() );
        assertEquals( false, v.getIfSecondDose() );
        assertEquals( 0, v.getDaysBetween() );
        assertEquals( true, v.getIfAvailable() );

    }

    /**
     * Test method for
     * {@link edu.ncsu.csc.iTrust2.models.Vaccine#addVaccine(java.lang.String, int, int, int, boolean, int, boolean)}.
     */
    @Test
    @Transactional
    void testAddVaccine () {
        final Vaccine v2 = new Vaccine();
        v2.setName( "Moderna" );
        v2.setAgeMax( 35 );
        v2.setAgeMin( 30 );
        v2.setDoseNumber( 1 );
        v2.setIfSecondDose( false );
        v2.setDaysBetween( 0 );
        v2.setIfAvailable( true );

        final Vaccine v3 = new Vaccine();
        v3.setName( "Pfizer" );
        v3.setAgeMax( 25 );
        v3.setAgeMin( 20 );
        v3.setDoseNumber( 1 );
        v3.setIfSecondDose( false );
        v3.setDaysBetween( 0 );
        v3.setIfAvailable( true );

        vaccineList.add( v2 );
        vaccineList.add( v3 );
        assertEquals( 2, vaccineList.size() );

        try {
            final Vaccine v4 = new Vaccine();
            v4.setName( "Pfizer" );
            v4.setAgeMax( 25 );
            v4.setAgeMin( 20 );
            v4.setDoseNumber( 1 );
            v4.setIfSecondDose( false );
            v4.setDaysBetween( 0 );
            v4.setIfAvailable( true );
        }
        catch ( final Exception e ) {
            assertEquals( e.getMessage(), "Duplicate Vaccines are not allowed." );
        }

    }

    /**
     * Test method for
     * {@link edu.ncsu.csc.iTrust2.models.Vaccine#editVaccine(java.lang.String, int, int, int, boolean, int, boolean)}.
     */
    @Test
    @Transactional
    void testEditVaccine () {
        // final Vaccine v = new Vaccine();
        // v.setName( "Pfizer" );
        // v.setAgeMax( 25 );
        // v.setAgeMin( 20 );
        // v.setDoseNumber( 1 );
        // v.setIfSecondDose( false );
        // v.setDaysBetween( 0 );
        // v.setIfAvailable( true );
        // v.editVaccine( "Pfizer", 45, 20, 2, true, 3, false );
        // assertEquals( v.getIfAvailable(), false );
        // assertEquals( v.getAgeMax(), 45 );

    }

    /**
     * Test method for
     * {@link edu.ncsu.csc.iTrust2.models.Vaccine#updateVaccine(java.lang.String, boolean)}.
     */
    @Test
    @Transactional
    void testUpdateVaccine () {
        // final Vaccine v = new Vaccine();
        // v.setName( "Pfizer" );
        // v.setAgeMax( 25 );
        // v.setAgeMin( 20 );
        // v.setDoseNumber( 1 );
        // v.setIfSecondDose( false );
        // v.setDaysBetween( 0 );
        // v.setIfAvailable( false );
        //
        // vaccineList.add( v );
        // assertEquals( vaccineList.size(), 1 );
        // v.updateVaccine( "Pfizer", true );
        // assertEquals( v.getIfAvailable(), true );
        //
        // final Vaccine v2 = new Vaccine();
        // v2.setName( "Moderna" );
        // v2.setAgeMax( 35 );
        // v2.setAgeMin( 30 );
        // v2.setDoseNumber( 2 );
        // v2.setIfSecondDose( false );
        // v2.setDaysBetween( 2 );
        // v2.setIfAvailable( true );
        //
        // vaccineList.add( v2 );
        // assertEquals( vaccineList.size(), 2 );
        // v2.updateVaccine( "Moderna", false );
        // assertEquals( v2.getIfAvailable(), false );

    }

    /**
     * Test method for
     * {@link edu.ncsu.csc.iTrust2.models.Vaccine#listVaccinesAvailable()}.
     */
    @Test
    @Transactional
    void testListVaccinesAvailable () {
        final Vaccine v = new Vaccine();
        v.setName( "Pfizer" );
        v.setAgeMax( 25 );
        v.setAgeMin( 20 );
        v.setDoseNumber( 1 );
        v.setIfSecondDose( false );
        v.setDaysBetween( 0 );
        v.setIfAvailable( true );
        vaccineList.add( v );
        final Vaccine v2 = new Vaccine();
        v2.setName( "Moderna" );
        v2.setAgeMax( 35 );
        v2.setAgeMin( 30 );
        v2.setDoseNumber( 1 );
        v2.setIfSecondDose( false );
        v2.setDaysBetween( 0 );
        v2.setIfAvailable( true );

        final Vaccine v3 = new Vaccine();
        v3.setName( "Pfizer" );
        v3.setAgeMax( 25 );
        v3.setAgeMin( 20 );
        v3.setDoseNumber( 1 );
        v3.setIfSecondDose( false );
        v3.setDaysBetween( 0 );
        v3.setIfAvailable( true );

        vaccineList.add( v2 );

        assertEquals( vaccineList.size(), 2 );
        vaccineList.add( v3 );

        assertEquals( vaccineList.size(), 3 );

    }

    /**
     * Test method for
     * {@link edu.ncsu.csc.iTrust2.models.Vaccine#equals(java.lang.Object)}.
     */
    @Test
    @Transactional
    void testEqualsObject () {
        final Vaccine v = new Vaccine();
        v.setName( "Pfizer" );
        v.setAgeMax( 25 );
        v.setAgeMin( 20 );
        v.setDoseNumber( 1 );
        v.setIfSecondDose( false );
        v.setDaysBetween( 0 );
        v.setIfAvailable( true );

        assertEquals( "Pfizer", v.getName() );
        assertEquals( 25, v.getAgeMax() );
        assertEquals( 20, v.getAgeMin() );
        assertEquals( 1, v.getDoseNumber() );
        assertEquals( false, v.getIfSecondDose() );
        assertEquals( 0, v.getDaysBetween() );
        assertEquals( true, v.getIfAvailable() );

        final Vaccine v2 = new Vaccine();
        v2.setName( "Moderna" );
        v2.setAgeMax( 35 );
        v2.setAgeMin( 30 );
        v2.setDoseNumber( 1 );
        v2.setIfSecondDose( false );
        v2.setDaysBetween( 0 );
        v2.setIfAvailable( true );

        final Vaccine v3 = new Vaccine();
        v3.setName( "Pfizer" );
        v3.setAgeMax( 25 );
        v3.setAgeMin( 20 );
        v3.setDoseNumber( 1 );
        v3.setIfSecondDose( false );
        v3.setDaysBetween( 0 );
        v3.setIfAvailable( true );

        assertFalse( v.equals( v2 ) );
        assertTrue( v.equals( v3 ) );
        assertTrue( v.equals( v ) );

    }

    /**
     * Test method for {@link edu.ncsu.csc.iTrust2.models.Vaccine#toString()}.
     */
    @Test
    @Transactional
    void testToString () {
        final Vaccine v = new Vaccine();
        v.setName( "Pfizer" );
        v.setAgeMax( 25 );
        v.setAgeMin( 20 );
        v.setDoseNumber( 1 );
        v.setIfSecondDose( false );
        v.setDaysBetween( 0 );
        v.setIfAvailable( true );

        assertEquals( "Pfizer", v.getName() );
        assertEquals( 25, v.getAgeMax() );
        assertEquals( 20, v.getAgeMin() );
        assertEquals( 1, v.getDoseNumber() );
        assertEquals( false, v.getIfSecondDose() );
        assertEquals( 0, v.getDaysBetween() );
        assertEquals( true, v.getIfAvailable() );

        assertEquals(
                "Vaccine [name=" + "Pfizer" + ", ageMin=" + 20 + ", ageMax=" + 25 + ", doseNumber=" + 1
                        + ", ifSecondDose=" + false + ", daysBetween=" + 0 + ", ifAvailable=" + true + "]",
                v.toString() );

    }

}
