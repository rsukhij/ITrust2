package edu.ncsu.csc.iTrust2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.iTrust2.models.OfficeVisit;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccinationVisit;

public class VaccinationVisitRepository extends JpaRepository<VaccinationVisit, Long> {
    /**
     * Find vaccination visits for a given patient
     *
     * @param hcp
     *            HCP to search by
     * @return Matching visits
     */
    public List<VaccinationVisit> findByHcp ( User hcp );

    /**
     * Find vaccination visits for a given HCP
     *
     * @param patient
     *            Patient to search by
     * @return Matching visits
     */
    public List<OfficeVisit> findByPatient ( User patient );

    /**
     * Find office visits for a given HCP and patient
     *
     * @param hcp
     *            HCP to search by
     * @param patient
     *            Patient to search by
     * @return Matching visits
     */
    public List<VaccinationVisit> findByHcpAndPatient ( User hcp, User patient );

}
