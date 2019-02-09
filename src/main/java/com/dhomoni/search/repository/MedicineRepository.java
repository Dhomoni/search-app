package com.dhomoni.search.repository;

import com.dhomoni.search.domain.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Medicine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long>, JpaSpecificationExecutor<Medicine> {

    @Query(value = "select distinct medicine from Medicine medicine left join fetch medicine.indications",
        countQuery = "select count(distinct medicine) from Medicine medicine")
    Page<Medicine> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct medicine from Medicine medicine left join fetch medicine.indications")
    List<Medicine> findAllWithEagerRelationships();

    @Query("select medicine from Medicine medicine left join fetch medicine.indications where medicine.id =:id")
    Optional<Medicine> findOneWithEagerRelationships(@Param("id") Long id);

}
