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

import com.dhomoni.search.domain.Symptom;
import com.dhomoni.search.domain.*; // for static metamodels
import com.dhomoni.search.repository.SymptomRepository;
import com.dhomoni.search.repository.search.SymptomSearchRepository;
import com.dhomoni.search.service.dto.SymptomCriteria;
import com.dhomoni.search.service.dto.SymptomDTO;
import com.dhomoni.search.service.mapper.SymptomMapper;

/**
 * Service for executing complex queries for Symptom entities in the database.
 * The main input is a {@link SymptomCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SymptomDTO} or a {@link Page} of {@link SymptomDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SymptomQueryService extends QueryService<Symptom> {

    private final Logger log = LoggerFactory.getLogger(SymptomQueryService.class);

    private final SymptomRepository symptomRepository;

    private final SymptomMapper symptomMapper;

    private final SymptomSearchRepository symptomSearchRepository;

    public SymptomQueryService(SymptomRepository symptomRepository, SymptomMapper symptomMapper, SymptomSearchRepository symptomSearchRepository) {
        this.symptomRepository = symptomRepository;
        this.symptomMapper = symptomMapper;
        this.symptomSearchRepository = symptomSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SymptomDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SymptomDTO> findByCriteria(SymptomCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Symptom> specification = createSpecification(criteria);
        return symptomMapper.toDto(symptomRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SymptomDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SymptomDTO> findByCriteria(SymptomCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Symptom> specification = createSpecification(criteria);
        return symptomRepository.findAll(specification, page)
            .map(symptomMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SymptomCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Symptom> specification = createSpecification(criteria);
        return symptomRepository.count(specification);
    }

    /**
     * Function to convert SymptomCriteria to a {@link Specification}
     */
    private Specification<Symptom> createSpecification(SymptomCriteria criteria) {
        Specification<Symptom> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Symptom_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Symptom_.name));
            }
            if (criteria.getDiseaseId() != null) {
                specification = specification.and(buildSpecification(criteria.getDiseaseId(),
                    root -> root.join(Symptom_.disease, JoinType.LEFT).get(Disease_.id)));
            }
        }
        return specification;
    }
}
