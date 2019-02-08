package com.dhomoni.search.repository.search;

import com.dhomoni.search.domain.Indication;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Indication entity.
 */
public interface IndicationSearchRepository extends ElasticsearchRepository<Indication, Long> {
}
