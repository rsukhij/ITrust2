package edu.ncsu.csc.iTrust2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;

import edu.ncsu.csc.iTrust2.config.SchemaValidateIntegrator;

/**
 * Main entrypoint to the iTrust2 application
 *
 * @author Kai Presler-Marshall
 *
 */
@SpringBootApplication ( scanBasePackages = { "edu.ncsu.csc.iTrust2" }, exclude = { ErrorMvcAutoConfiguration.class } )
@EnableAutoConfiguration ( exclude = { ErrorMvcAutoConfiguration.class } )
public class ITrust2Application {

    /**
     * Main method
     *
     * @param args
     *            command-line arguments for Spring
     */
    public static void main ( final String[] args ) {
        SpringApplication.run( ITrust2Application.class, args );
    }

    /**
     * Used to register a Schema Validator to make sure the database is
     * up-to-date
     *
     * @param schemaValidateIntegrator
     *            Schema Validator to register
     * @return Updated Hibernate properties file
     */
    @Bean
    public HibernatePropertiesCustomizer registerSchemaValidator (
            final SchemaValidateIntegrator schemaValidateIntegrator ) {
        return ( prop -> {
            prop.put( "hibernate.integrator_provider", schemaValidateIntegrator );
        } );
    }
}
