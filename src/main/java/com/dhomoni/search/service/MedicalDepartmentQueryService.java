package com.dhomoni.search.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.dhomoni.search.domain.MedicalDepartment;
import com.dhomoni.search.domain.*; // for static metamodels
import com.dhomoni.search.repository.MedicalDepartmentRepository;
import com.dhomoni.search.repository.search.MedicalDepartmentSearchRepository;
import com.dhomoni.search.service.dto.MedicalDepartmentCriteria;
import com.dhomoni.search.service.dto.MedicalDepartmentDTO;
import com.dhomoni.search.service.mapper.MedicalDepartmentMapper;

/**
 * Service for executing complex queries for MedicalDepartment entities in the database.
 * The main input is a {@link MedicalDepartmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MedicalDepartmentDTO} or a {@link Page} of {@link MedicalDepartmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicalDepartmentQueryService extends QueryService<MedicalDepartment> {

    private final Logger log = LoggerFactory.getLogger(MedicalDepartmentQueryService.class);

    private final MedicalDepartmentRepository medicalDepartmentRepository;

    private final MedicalDepartmentMapper medicalDepartmentMapper;

    private final MedicalDepartmentSearchRepository medicalDepartmentSearchRepository;

    public MedicalDepartmentQueryService(MedicalDepartmentRepository medicalDepartmentRepository, MedicalDepartmentMapper medicalDepartmentMapper, MedicalDepartmentSearchRepository medicalDepartmentSearchRepository) {
        this.medicalDepartmentRepository = medicalDepartmentRepository;
        this.medicalDepartmentMapper = medicalDepartmentMapper;
        this.medicalDepartmentSearchRepository = medicalDepartmentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MedicalDepartmentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MedicalDepartmentDTO> findByCriteria(MedicalDepartmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MedicalDepartment> specification = createSpecification(criteria);
        return medicalDepartmentMapper.toDto(medicalDepartmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MedicalDepartmentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicalDepartmentDTO> findByCriteria(MedicalDepartmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MedicalDepartment> specification = createSpecification(criteria);
        return medicalDepartmentRepository.findAll(specification, page)
            .map(medicalDepartmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicalDepartmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MedicalDepartment> specification = createSpecification(criteria);
        return medicalDepartmentRepository.count(specification);
    }

    /**
     * Function to convert MedicalDepartmentCriteria to a {@link Specification}
     */
    private Specification<MedicalDepartment> createSpecification(MedicalDepartmentCriteria criteria) {
        Specification<MedicalDepartment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MedicalDepartment_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MedicalDepartment_.name));
            }
            if (criteria.getDiseasesId() != null) {
                specification = specification.and(buildSpecification(criteria.getDiseasesId(),
                    root -> root.join(MedicalDepartment_.diseases, JoinType.LEFT).get(Disease_.id)));
            }
        }
        return specification;
    }
}
