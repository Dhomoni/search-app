package com.dhomoni.search.repository;

import com.dhomoni.search.domain.MedicalDepartment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MedicalDepartment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalDepartmentRepository extends JpaRepository<MedicalDepartment, Long>, JpaSpecificationExecutor<MedicalDepartment> {

}
