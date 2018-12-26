package com.dhomoni.search.repository.search;

import com.dhomoni.search.domain.ProfessionalDegree;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProfessionalDegree entity.
 */
public interface ProfessionalDegreeSearchRepository extends ElasticsearchRepository<ProfessionalDegree, Long> {
}
