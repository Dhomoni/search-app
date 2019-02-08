package com.dhomoni.search.repository;

import com.dhomoni.search.domain.Symptom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Symptom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SymptomRepository extends JpaRepository<Symptom, Long>, JpaSpecificationExecutor<Symptom> {

}
