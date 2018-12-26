package com.dhomoni.search.repository;

import com.dhomoni.search.domain.Disease;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Disease entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long>, JpaSpecificationExecutor<Disease> {

}
