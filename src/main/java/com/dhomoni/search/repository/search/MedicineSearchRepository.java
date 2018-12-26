package com.dhomoni.search.repository.search;

import com.dhomoni.search.domain.Medicine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Medicine entity.
 */
public interface MedicineSearchRepository extends ElasticsearchRepository<Medicine, Long> {
}
