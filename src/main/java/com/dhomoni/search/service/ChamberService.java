package com.dhomoni.search.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhomoni.search.domain.Chamber;
import com.dhomoni.search.repository.ChamberRepository;
import com.dhomoni.search.service.dto.ChamberDTO;
import com.dhomoni.search.service.mapper.ChamberMapper;

/**
 * Service Implementation for managing Chamber.
 */
@Service
@Transactional
public class ChamberService {

    private final Logger log = LoggerFactory.getLogger(ChamberService.class);

    private final ChamberRepository chamberRepository;

    private final ChamberMapper chamberMapper;

//    private final ChamberSearchRepository chamberSearchRepository;

    public ChamberService(ChamberRepository chamberRepository, ChamberMapper chamberMapper) {
        this.chamberRepository = chamberRepository;
        this.chamberMapper = chamberMapper;
    }

    /**
     * Save a chamber.
     *
     * @param chamberDTO the entity to save
     * @return the persisted entity
     */
    public ChamberDTO save(ChamberDTO chamberDTO) {
        log.debug("Request to save Chamber : {}", chamberDTO);

        Chamber chamber = chamberMapper.toEntity(chamberDTO);
        chamber = chamberRepository.save(chamber);
        ChamberDTO result = chamberMapper.toDto(chamber);
        return result;
    }

    /**
     * Get all the chambers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChamberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Chambers");
        return chamberRepository.findAll(pageable)
            .map(chamberMapper::toDto);
    }


    /**
     * Get one chamber by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ChamberDTO> findOne(Long id) {
        log.debug("Request to get Chamber : {}", id);
        return chamberRepository.findById(id)
            .map(chamberMapper::toDto);
    }

    /**
     * Delete the chamber by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Chamber : {}", id);
        chamberRepository.deleteById(id);
    }
}
