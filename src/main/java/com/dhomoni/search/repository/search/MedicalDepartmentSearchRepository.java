package com.dhomoni.search.repository.search;

import com.dhomoni.search.domain.MedicalDepartment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MedicalDepartment entity.
 */
public interface MedicalDepartmentSearchRepository extends ElasticsearchRepository<MedicalDepartment, Long> {
}
