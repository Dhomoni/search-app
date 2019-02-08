package com.dhomoni.search.service;

import com.dhomoni.search.domain.Symptom;
import com.dhomoni.search.repository.SymptomRepository;
import com.dhomoni.search.repository.search.SymptomSearchRepository;
import com.dhomoni.search.service.dto.SymptomDTO;
import com.dhomoni.search.service.mapper.SymptomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Symptom.
 */
@Service
@Transactional
public class SymptomService {

    private final Logger log = LoggerFactory.getLogger(SymptomService.class);

    private final SymptomRepository symptomRepository;

    private final SymptomMapper symptomMapper;

    private final SymptomSearchRepository symptomSearchRepository;

    public SymptomService(SymptomRepository symptomRepository, SymptomMapper symptomMapper, SymptomSearchRepository symptomSearchRepository) {
        this.symptomRepository = symptomRepository;
        this.symptomMapper = symptomMapper;
        this.symptomSearchRepository = symptomSearchRepository;
    }

    /**
     * Save a symptom.
     *
     * @param symptomDTO the entity to save
     * @return the persisted entity
     */
    public SymptomDTO save(SymptomDTO symptomDTO) {
        log.debug("Request to save Symptom : {}", symptomDTO);

        Symptom symptom = symptomMapper.toEntity(symptomDTO);
        symptom = symptomRepository.save(symptom);
        SymptomDTO result = symptomMapper.toDto(symptom);
        symptomSearchRepository.save(symptom);
        return result;
    }

    /**
     * Get all the symptoms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SymptomDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Symptoms");
        return symptomRepository.findAll(pageable)
            .map(symptomMapper::toDto);
    }


    /**
     * Get one symptom by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SymptomDTO> findOne(Long id) {
        log.debug("Request to get Symptom : {}", id);
        return symptomRepository.findById(id)
            .map(symptomMapper::toDto);
    }

    /**
     * Delete the symptom by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Symptom : {}", id);
        symptomRepository.deleteById(id);
        symptomSearchRepository.deleteById(id);
    }

    /**
     * Search for the symptom corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SymptomDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Symptoms for query {}", query);
        return symptomSearchRepository.search(queryStringQuery(query), pageable)
            .map(symptomMapper::toDto);
    }
}
