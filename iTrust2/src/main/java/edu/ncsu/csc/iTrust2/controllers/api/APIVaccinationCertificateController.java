package edu.ncsu.csc.iTrust2.controllers.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
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
    @PreAuthorize ( "hasAnyRole('ROLE_PATIENT')" )
    public ResponseEntity<byte[]> downloadCertificate () {
        final Patient self = (Patient) userService.findByName( LoggerUtil.currentUser() );
        loggerUtil.log( TransactionType.VIEW_ALL_VACCINATION_VISITS, self );
        final List<VaccinationVisit> visits = visitService.findByPatient( self );

        final OutputStream output = null;
        try {

            final Document certificate = new Document( PageSize.LETTER, 0.75F, 0.75F, 0.75F, 0.75F );
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance( certificate, byteArrayOutputStream );
            certificate.open();
            certificate.addCreationDate();
            certificate.addTitle( "Vaccination Certificate" );
            certificate.addCreator( "iTrust Healthcare System" );

            certificate.add( new Paragraph( "Patient: " + self.getFirstName() + " " + self.getLastName() ) );
            final boolean vaccinated = false;
            int visitnum = 1;
            for ( final VaccinationVisit visit : visits ) {
                certificate.add( new Paragraph( "\nCOVID-19 Vaccine: " + visit.getVaccines().getName() ) );
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "MM/dd/yyyy 'at' HH:mm:ss z" );

                final String formattedDateTime = visit.getDate().format( formatter );
                certificate.add( new Paragraph( "Given on " + formattedDateTime ) );

                certificate.add( new Paragraph( "Dose: " + visitnum + " of " + visit.getVaccines().getDoseNumber() ) );
                visitnum++;
            }
            if ( self.getVaccinationStatus() != null ) {
                certificate.add( new Paragraph( "\nVaccination Status: " + self.getVaccinationStatus().toString() ) );
            }
            else {
                certificate.add( new Paragraph( "\nVaccination Status: " + "NO VACCINATION" ) );
            }

            certificate.close();

            final byte[] contents = byteArrayOutputStream.toByteArray();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType( MediaType.parseMediaType( "application/pdf" ) );
            final String filename = "test.pdf";
            headers.setContentDispositionFormData( filename, filename );
            final ResponseEntity<byte[]> response = new ResponseEntity<byte[]>( contents, headers, HttpStatus.OK );
            return response;

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
