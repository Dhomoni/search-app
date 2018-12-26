package com.dhomoni.search.repository;

import com.dhomoni.search.domain.WeeklyVisitingHours;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WeeklyVisitingHours entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeeklyVisitingHoursRepository extends JpaRepository<WeeklyVisitingHours, Long>, JpaSpecificationExecutor<WeeklyVisitingHours> {

}
