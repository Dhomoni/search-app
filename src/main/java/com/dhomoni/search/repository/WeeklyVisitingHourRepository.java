package com.dhomoni.search.repository;

import com.dhomoni.search.domain.WeeklyVisitingHour;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WeeklyVisitingHour entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeeklyVisitingHourRepository extends JpaRepository<WeeklyVisitingHour, Long>, JpaSpecificationExecutor<WeeklyVisitingHour> {

}
