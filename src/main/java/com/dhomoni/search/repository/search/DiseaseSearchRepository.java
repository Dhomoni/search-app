package com.dhomoni.search.repository.search;

import com.dhomoni.search.domain.Disease;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Disease entity.
 */
public interface DiseaseSearchRepository extends ElasticsearchRepository<Disease, Long> {
}
