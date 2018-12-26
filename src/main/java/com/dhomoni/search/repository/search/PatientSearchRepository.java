package com.dhomoni.search.repository.search;

import com.dhomoni.search.domain.Patient;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Patient entity.
 */
public interface PatientSearchRepository extends ElasticsearchRepository<Patient, Long> {
}
