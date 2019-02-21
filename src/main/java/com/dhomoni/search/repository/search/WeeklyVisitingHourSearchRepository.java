package com.dhomoni.search.repository.search;

import com.dhomoni.search.domain.WeeklyVisitingHour;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WeeklyVisitingHour entity.
 */
public interface WeeklyVisitingHourSearchRepository extends ElasticsearchRepository<WeeklyVisitingHour, Long> {
}
