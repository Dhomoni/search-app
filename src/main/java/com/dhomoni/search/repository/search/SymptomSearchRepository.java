package com.dhomoni.search.repository.search;

import com.dhomoni.search.domain.Symptom;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Symptom entity.
 */
public interface SymptomSearchRepository extends ElasticsearchRepository<Symptom, Long> {
}
