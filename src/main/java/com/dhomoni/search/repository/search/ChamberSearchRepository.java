package com.dhomoni.search.repository.search;

import com.dhomoni.search.domain.Chamber;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Chamber entity.
 */
public interface ChamberSearchRepository extends ElasticsearchRepository<Chamber, Long> {
}
