package edu.ncsu.csc.iTrust2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.OfficeVisit;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;

public interface VaccinationVisitRepository <T extends OfficeVisit> extends JpaRepository<VaccinationVisit, Long> {

}
