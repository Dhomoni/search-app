package com.dhomoni.search.repository.search;

import com.dhomoni.search.domain.Doctor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Doctor entity.
 */
public interface DoctorSearchRepository extends ElasticsearchRepository<Doctor, Long> {
}
