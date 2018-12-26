package com.dhomoni.search.service;

import com.dhomoni.search.domain.ProfessionalDegree;
import com.dhomoni.search.repository.ProfessionalDegreeRepository;
import com.dhomoni.search.repository.search.ProfessionalDegreeSearchRepository;
import com.dhomoni.search.service.dto.ProfessionalDegreeDTO;
import com.dhomoni.search.service.mapper.ProfessionalDegreeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProfessionalDegree.
 */
@Service
@Transactional
public class ProfessionalDegreeService {

    private final Logger log = LoggerFactory.getLogger(ProfessionalDegreeService.class);

    private final ProfessionalDegreeRepository professionalDegreeRepository;

    private final ProfessionalDegreeMapper professionalDegreeMapper;

    private final ProfessionalDegreeSearchRepository professionalDegreeSearchRepository;

    public ProfessionalDegreeService(ProfessionalDegreeRepository professionalDegreeRepository, ProfessionalDegreeMapper professionalDegreeMapper, ProfessionalDegreeSearchRepository professionalDegreeSearchRepository) {
        this.professionalDegreeRepository = professionalDegreeRepository;
        this.professionalDegreeMapper = professionalDegreeMapper;
        this.professionalDegreeSearchRepository = professionalDegreeSearchRepository;
    }

    /**
     * Save a professionalDegree.
     *
     * @param professionalDegreeDTO the entity to save
     * @return the persisted entity
     */
    public ProfessionalDegreeDTO save(ProfessionalDegreeDTO professionalDegreeDTO) {
        log.debug("Request to save ProfessionalDegree : {}", professionalDegreeDTO);

        ProfessionalDegree professionalDegree = professionalDegreeMapper.toEntity(professionalDegreeDTO);
        professionalDegree = professionalDegreeRepository.save(professionalDegree);
        ProfessionalDegreeDTO result = professionalDegreeMapper.toDto(professionalDegree);
        professionalDegreeSearchRepository.save(professionalDegree);
        return result;
    }

    /**
     * Get all the professionalDegrees.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalDegreeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProfessionalDegrees");
        return professionalDegreeRepository.findAll(pageable)
            .map(professionalDegreeMapper::toDto);
    }


    /**
     * Get one professionalDegree by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionalDegreeDTO> findOne(Long id) {
        log.debug("Request to get ProfessionalDegree : {}", id);
        return professionalDegreeRepository.findById(id)
            .map(professionalDegreeMapper::toDto);
    }

    /**
     * Delete the professionalDegree by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProfessionalDegree : {}", id);
        professionalDegreeRepository.deleteById(id);
        professionalDegreeSearchRepository.deleteById(id);
    }

    /**
     * Search for the professionalDegree corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalDegreeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProfessionalDegrees for query {}", query);
        return professionalDegreeSearchRepository.search(queryStringQuery(query), pageable)
            .map(professionalDegreeMapper::toDto);
    }
}
