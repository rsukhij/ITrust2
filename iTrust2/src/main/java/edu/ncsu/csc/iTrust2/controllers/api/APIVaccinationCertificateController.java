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

import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.services.VaccinationVisitService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

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

    @SuppressWarnings ( { "rawtypes", "unchecked" } )
    @GetMapping ( BASE_PATH + "/vaccinecertificate" )
    // TODO Do we need a PreAuthorize here?
    public ResponseEntity downloadCertificate () {
        final User self = userService.findByName( LoggerUtil.currentUser() );
        loggerUtil.log( TransactionType.VIEW_ALL_VACCINATION_VISITS, self );
        final List<VaccinationVisit> visits = visitService.findByPatient( self );

        OutputStream output = null;
        try {
            output = new FileOutputStream( new File( "Certificate.pdf" ) );
            final Document certificate = new Document();

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
