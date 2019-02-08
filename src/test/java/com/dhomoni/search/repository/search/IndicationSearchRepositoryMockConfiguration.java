package com.dhomoni.search.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of IndicationSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class IndicationSearchRepositoryMockConfiguration {

    @MockBean
    private IndicationSearchRepository mockIndicationSearchRepository;

}
