package com.dhomoni.search.service;

import com.dhomoni.search.domain.MedicalDepartment;
import com.dhomoni.search.repository.MedicalDepartmentRepository;
import com.dhomoni.search.repository.search.MedicalDepartmentSearchRepository;
import com.dhomoni.search.service.dto.MedicalDepartmentDTO;
import com.dhomoni.search.service.mapper.MedicalDepartmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MedicalDepartment.
 */
@Service
@Transactional
public class MedicalDepartmentService {

    private final Logger log = LoggerFactory.getLogger(MedicalDepartmentService.class);

    private final MedicalDepartmentRepository medicalDepartmentRepository;

    private final MedicalDepartmentMapper medicalDepartmentMapper;

    private final MedicalDepartmentSearchRepository medicalDepartmentSearchRepository;

    public MedicalDepartmentService(MedicalDepartmentRepository medicalDepartmentRepository, MedicalDepartmentMapper medicalDepartmentMapper, MedicalDepartmentSearchRepository medicalDepartmentSearchRepository) {
        this.medicalDepartmentRepository = medicalDepartmentRepository;
        this.medicalDepartmentMapper = medicalDepartmentMapper;
        this.medicalDepartmentSearchRepository = medicalDepartmentSearchRepository;
    }

    /**
     * Save a medicalDepartment.
     *
     * @param medicalDepartmentDTO the entity to save
     * @return the persisted entity
     */
    public MedicalDepartmentDTO save(MedicalDepartmentDTO medicalDepartmentDTO) {
        log.debug("Request to save MedicalDepartment : {}", medicalDepartmentDTO);

        MedicalDepartment medicalDepartment = medicalDepartmentMapper.toEntity(medicalDepartmentDTO);
        medicalDepartment = medicalDepartmentRepository.save(medicalDepartment);
        MedicalDepartmentDTO result = medicalDepartmentMapper.toDto(medicalDepartment);
        medicalDepartmentSearchRepository.save(medicalDepartment);
        return result;
    }

    /**
     * Get all the medicalDepartments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MedicalDepartmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MedicalDepartments");
        return medicalDepartmentRepository.findAll(pageable)
            .map(medicalDepartmentMapper::toDto);
    }


    /**
     * Get one medicalDepartment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<MedicalDepartmentDTO> findOne(Long id) {
        log.debug("Request to get MedicalDepartment : {}", id);
        return medicalDepartmentRepository.findById(id)
            .map(medicalDepartmentMapper::toDto);
    }

    /**
     * Delete the medicalDepartment by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MedicalDepartment : {}", id);
        medicalDepartmentRepository.deleteById(id);
        medicalDepartmentSearchRepository.deleteById(id);
    }

    /**
     * Search for the medicalDepartment corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MedicalDepartmentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MedicalDepartments for query {}", query);
        return medicalDepartmentSearchRepository.search(queryStringQuery(query), pageable)
            .map(medicalDepartmentMapper::toDto);
    }
}
