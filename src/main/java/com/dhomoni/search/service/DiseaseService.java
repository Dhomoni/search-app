package com.dhomoni.search.service;

import com.dhomoni.search.domain.Disease;
import com.dhomoni.search.repository.DiseaseRepository;
import com.dhomoni.search.repository.search.DiseaseSearchRepository;
import com.dhomoni.search.service.dto.DiseaseDTO;
import com.dhomoni.search.service.mapper.DiseaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
     * Get one disease by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DiseaseDTO> findOne(Long id) {
        log.debug("Request to get Disease : {}", id);
        return diseaseRepository.findById(id)
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
    public Page<DiseaseDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Diseases for query {}", query);
        return diseaseSearchRepository.search(queryStringQuery(query), pageable)
            .map(diseaseMapper::toDto);
    }
}
