package com.dhomoni.search.service;

import com.dhomoni.search.domain.Indication;
import com.dhomoni.search.repository.IndicationRepository;
import com.dhomoni.search.repository.search.IndicationSearchRepository;
import com.dhomoni.search.service.dto.IndicationDTO;
import com.dhomoni.search.service.mapper.IndicationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Indication.
 */
@Service
@Transactional
public class IndicationService {

    private final Logger log = LoggerFactory.getLogger(IndicationService.class);

    private final IndicationRepository indicationRepository;

    private final IndicationMapper indicationMapper;

    private final IndicationSearchRepository indicationSearchRepository;

    public IndicationService(IndicationRepository indicationRepository, IndicationMapper indicationMapper, IndicationSearchRepository indicationSearchRepository) {
        this.indicationRepository = indicationRepository;
        this.indicationMapper = indicationMapper;
        this.indicationSearchRepository = indicationSearchRepository;
    }

    /**
     * Save a indication.
     *
     * @param indicationDTO the entity to save
     * @return the persisted entity
     */
    public IndicationDTO save(IndicationDTO indicationDTO) {
        log.debug("Request to save Indication : {}", indicationDTO);

        Indication indication = indicationMapper.toEntity(indicationDTO);
        indication = indicationRepository.save(indication);
        IndicationDTO result = indicationMapper.toDto(indication);
        indicationSearchRepository.save(indication);
        return result;
    }

    /**
     * Get all the indications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IndicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Indications");
        return indicationRepository.findAll(pageable)
            .map(indicationMapper::toDto);
    }


    /**
     * Get one indication by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<IndicationDTO> findOne(Long id) {
        log.debug("Request to get Indication : {}", id);
        return indicationRepository.findById(id)
            .map(indicationMapper::toDto);
    }

    /**
     * Delete the indication by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Indication : {}", id);
        indicationRepository.deleteById(id);
        indicationSearchRepository.deleteById(id);
    }

    /**
     * Search for the indication corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IndicationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Indications for query {}", query);
        return indicationSearchRepository.search(queryStringQuery(query), pageable)
            .map(indicationMapper::toDto);
    }
}
