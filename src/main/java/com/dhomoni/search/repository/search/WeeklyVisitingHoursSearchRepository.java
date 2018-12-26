package com.dhomoni.search.repository.search;

import com.dhomoni.search.domain.WeeklyVisitingHours;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WeeklyVisitingHours entity.
 */
public interface WeeklyVisitingHoursSearchRepository extends ElasticsearchRepository<WeeklyVisitingHours, Long> {
}
