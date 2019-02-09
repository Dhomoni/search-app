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

import com.dhomoni.search.domain.Indication;
import com.dhomoni.search.domain.*; // for static metamodels
import com.dhomoni.search.repository.IndicationRepository;
import com.dhomoni.search.repository.search.IndicationSearchRepository;
import com.dhomoni.search.service.dto.IndicationCriteria;
import com.dhomoni.search.service.dto.IndicationDTO;
import com.dhomoni.search.service.mapper.IndicationMapper;

/**
 * Service for executing complex queries for Indication entities in the database.
 * The main input is a {@link IndicationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IndicationDTO} or a {@link Page} of {@link IndicationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndicationQueryService extends QueryService<Indication> {

    private final Logger log = LoggerFactory.getLogger(IndicationQueryService.class);

    private final IndicationRepository indicationRepository;

    private final IndicationMapper indicationMapper;

    private final IndicationSearchRepository indicationSearchRepository;

    public IndicationQueryService(IndicationRepository indicationRepository, IndicationMapper indicationMapper, IndicationSearchRepository indicationSearchRepository) {
        this.indicationRepository = indicationRepository;
        this.indicationMapper = indicationMapper;
        this.indicationSearchRepository = indicationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link IndicationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IndicationDTO> findByCriteria(IndicationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Indication> specification = createSpecification(criteria);
        return indicationMapper.toDto(indicationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IndicationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndicationDTO> findByCriteria(IndicationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Indication> specification = createSpecification(criteria);
        return indicationRepository.findAll(specification, page)
            .map(indicationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndicationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Indication> specification = createSpecification(criteria);
        return indicationRepository.count(specification);
    }

    /**
     * Function to convert IndicationCriteria to a {@link Specification}
     */
    private Specification<Indication> createSpecification(IndicationCriteria criteria) {
        Specification<Indication> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Indication_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Indication_.name));
            }
        }
        return specification;
    }
}
