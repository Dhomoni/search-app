package com.dhomoni.search.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of WeeklyVisitingHourSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class WeeklyVisitingHourSearchRepositoryMockConfiguration {

    @MockBean
    private WeeklyVisitingHourSearchRepository mockWeeklyVisitingHourSearchRepository;

}