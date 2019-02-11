package com.dhomoni.search.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhomoni.search.domain.Disease;
import com.dhomoni.search.repository.DiseaseRepository;
import com.dhomoni.search.repository.search.DiseaseSearchRepository;
import com.dhomoni.search.service.dto.DiseaseDTO;
import com.dhomoni.search.service.dto.SearchDTO;
import com.dhomoni.search.service.mapper.DiseaseMapper;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

/**
 * Service Implementation for managing Disease.
 */
@Service
@Transactional
public class DiseaseService {

    private final Logger log = LoggerFactory.getLogger(DiseaseService.class);

    private final DiseaseRepository diseaseRepository;

    private final DiseaseMapper diseaseMapper;

    private final DiseaseSearchRepository diseaseSearchRepository;

    public DiseaseService(DiseaseRepository diseaseRepository, DiseaseMapper diseaseMapper, DiseaseSearchRepository diseaseSearchRepository) {
        this.diseaseRepository = diseaseRepository;
        this.diseaseMapper = diseaseMapper;
        this.diseaseSearchRepository = diseaseSearchRepository;
    }

    /**
     * Save a disease.
     *
     * @param diseaseDTO the entity to save
     * @return the persisted entity
     */
    public DiseaseDTO save(DiseaseDTO diseaseDTO) {
        log.debug("Request to save Disease : {}", diseaseDTO);

        Disease disease = diseaseMapper.toEntity(diseaseDTO);
        disease = diseaseRepository.save(disease);
        DiseaseDTO result = diseaseMapper.toDto(disease);
        diseaseSearchRepository.save(disease);
        return result;
    }

    /**
     * Get all the diseases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DiseaseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Diseases");
        return diseaseRepository.findAll(pageable)
            .map(diseaseMapper::toDto);
    }

    /**
     * Get all the Disease with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<DiseaseDTO> findAllWithEagerRelationships(Pageable pageable) {
        return diseaseRepository.findAllWithEagerRelationships(pageable).map(diseaseMapper::toDto);
    }
    

    /**
     * Get one disease by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DiseaseDTO> findOne(Long id) {
        log.debug("Request to get Disease : {}", id);
        return diseaseRepository.findOneWithEagerRelationships(id)
            .map(diseaseMapper::toDto);
    }

    /**
     * Delete the disease by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Disease : {}", id);
        diseaseRepository.deleteById(id);
        diseaseSearchRepository.deleteById(id);
    }

    /**
     * Search for the disease corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DiseaseDTO> search(SearchDTO searchDTO, Pageable pageable) {
        log.debug("Request to search for a page of Diseases for query {}", searchDTO.getQuery());        
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQuery = boolQuery().should(simpleQueryStringQuery(searchDTO.getQuery()));
		Iterable<String> tokens = Splitter.on(CharMatcher.anyOf(", ")).omitEmptyStrings().split(searchDTO.getQuery());
		for (String token : tokens) {
			if (StringUtils.isNumeric(token)) {
				boolQuery.should(constantScoreQuery(termQuery("id", token).boost(-1f)));
			}
		}
		SearchQuery searchQuery = queryBuilder.withQuery(boolQuery)
				.withPageable(pageable).withMinScore(0.00001f).build();
        return diseaseSearchRepository.search(searchQuery)
            .map(diseaseMapper::toDto);
    }
}
