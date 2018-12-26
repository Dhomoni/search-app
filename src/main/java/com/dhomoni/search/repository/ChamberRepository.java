package com.dhomoni.search.repository;

import com.dhomoni.search.domain.Chamber;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Chamber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChamberRepository extends JpaRepository<Chamber, Long>, JpaSpecificationExecutor<Chamber> {

}
