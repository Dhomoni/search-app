package com.dhomoni.search.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ChamberSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ChamberSearchRepositoryMockConfiguration {

    @MockBean
    private ChamberSearchRepository mockChamberSearchRepository;

}
