package edu.ncsu.csc.iTrust2.controllers.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.services.VaccinationVisitService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

/**
 * Class to implement UC19, the ability of a patient to download a vaccination
 * certificate
 *
 * @author Nicole Worth
 */
@RestController
public class APIVaccinationCertificateController extends APIController {

    /**
     * Field to hold the instance of VaccinationVisitService
     */
    @Autowired
    private VaccinationVisitService visitService;

    /**
     * Field to hold the instance of the user service
     */
    @Autowired
    private UserService<User>       userService;

    /**
     * Field to hold the instance of the LoggerUtil
     */
    @Autowired
    private LoggerUtil              loggerUtil;

    /**
     * Method to download a vaccine certificate for a user
     *
     * @return the success/failure message as to whether a pdf is created
     */
    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @GetMapping ( BASE_PATH + "/vaccinecertificate" )
    // TODO Do we need a PreAuthorize here?
    public ResponseEntity downloadCertificate () {
        final Patient self = (Patient) userService.findByName( LoggerUtil.currentUser() );
        loggerUtil.log( TransactionType.VIEW_ALL_VACCINATION_VISITS, self );
        final List<VaccinationVisit> visits = visitService.findByPatient( self );

        OutputStream output = null;
        try {
            output = new FileOutputStream( new File( "Certificate.pdf" ) );
            final Document certificate = new Document();
            PdfWriter.getInstance( certificate, output );
            certificate.open();
            certificate.addCreationDate();
            certificate.addTitle( "Vaccination Certificate" );
            certificate.addCreator( "iTrust Healthcare System" );

            certificate.add( new Paragraph( "Patient: " + self.getFirstName() + " " + self.getLastName() ) );
            boolean vaccinated = false;
            for ( final VaccinationVisit visit : visits ) {
                certificate.add( new Paragraph( "COVID-19 Vaccine: " + visit.getVaccines().getName() ) );
                certificate.add( new Paragraph( "Date: " + visit.getDate().getDayOfMonth() + "/"
                        + visit.getDate().getDayOfYear() + "/" + visit.getDate().getYear() ) );
                // TODO how specific do we want to get with the time? It's
                // currently in military format
                certificate.add(
                        new Paragraph( "Time: " + visit.getDate().getHour() + ":" + visit.getDate().getMinute() ) );
                certificate.add( new Paragraph( "Staff Member: " + visit.getVaccinator().getUsername() ) );
                int doses = 1;
                if ( visit.getVaccines().getIfSecondDose() ) {
                    doses = 2;
                }
                certificate.add( new Paragraph( "Dose: " + visit.getVisitNumber() + " of " + doses ) );

                if ( doses == 1 && visits.size() == 1 ) {
                    vaccinated = true;
                }
                else if ( doses == 2 && visits.size() == 2 ) {
                    vaccinated = true;
                }
            }
            if ( vaccinated ) {
                certificate.add( new Paragraph( "Vaccination Status: Fully Vaccinated" ) );
            }
            else {
                certificate.add( new Paragraph( "Vaccination Status: Not Fully Vaccinated" ) );
            }

            certificate.close();
            return new ResponseEntity( certificate, HttpStatus.OK );
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
            return new ResponseEntity( errorResponse( "Error with file creation: " + ex.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
        finally {
            try {
                if ( output != null ) {
                    output.close();
                }
            }
            catch ( final IOException io ) {
                io.printStackTrace();
                return new ResponseEntity( errorResponse( "Error closing file: " + io.getMessage() ),
                        HttpStatus.BAD_REQUEST );
            }
        }
    }
}
