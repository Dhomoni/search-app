package com.dhomoni.search.repository;

import com.dhomoni.search.domain.ProfessionalDegree;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProfessionalDegree entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessionalDegreeRepository extends JpaRepository<ProfessionalDegree, Long>, JpaSpecificationExecutor<ProfessionalDegree> {

}
