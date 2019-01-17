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

import com.dhomoni.search.domain.Disease;
import com.dhomoni.search.domain.*; // for static metamodels
import com.dhomoni.search.repository.DiseaseRepository;
import com.dhomoni.search.repository.search.DiseaseSearchRepository;
import com.dhomoni.search.service.dto.DiseaseCriteria;
import com.dhomoni.search.service.dto.DiseaseDTO;
import com.dhomoni.search.service.mapper.DiseaseMapper;

/**
 * Service for executing complex queries for Disease entities in the database.
 * The main input is a {@link DiseaseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DiseaseDTO} or a {@link Page} of {@link DiseaseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DiseaseQueryService extends QueryService<Disease> {

    private final Logger log = LoggerFactory.getLogger(DiseaseQueryService.class);

    private final DiseaseRepository diseaseRepository;

    private final DiseaseMapper diseaseMapper;

    private final DiseaseSearchRepository diseaseSearchRepository;

    public DiseaseQueryService(DiseaseRepository diseaseRepository, DiseaseMapper diseaseMapper, DiseaseSearchRepository diseaseSearchRepository) {
        this.diseaseRepository = diseaseRepository;
        this.diseaseMapper = diseaseMapper;
        this.diseaseSearchRepository = diseaseSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DiseaseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DiseaseDTO> findByCriteria(DiseaseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Disease> specification = createSpecification(criteria);
        return diseaseMapper.toDto(diseaseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DiseaseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DiseaseDTO> findByCriteria(DiseaseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Disease> specification = createSpecification(criteria);
        return diseaseRepository.findAll(specification, page)
            .map(diseaseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DiseaseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Disease> specification = createSpecification(criteria);
        return diseaseRepository.count(specification);
    }

    /**
     * Function to convert DiseaseCriteria to a {@link Specification}
     */
    private Specification<Disease> createSpecification(DiseaseCriteria criteria) {
        Specification<Disease> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Disease_.id));
            }
            if (criteria.getMedicalName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMedicalName(), Disease_.medicalName));
            }
            if (criteria.getGeneralName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGeneralName(), Disease_.generalName));
            }
            if (criteria.getSymptoms() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSymptoms(), Disease_.symptoms));
            }
            if (criteria.getDeptId() != null) {
                specification = specification.and(buildSpecification(criteria.getDeptId(),
                    root -> root.join(Disease_.medicalDepartment, JoinType.LEFT).get(MedicalDepartment_.id)));
            }
        }
        return specification;
    }
}
