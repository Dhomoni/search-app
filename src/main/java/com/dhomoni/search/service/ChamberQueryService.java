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

import com.dhomoni.search.domain.Chamber;
import com.dhomoni.search.domain.*; // for static metamodels
import com.dhomoni.search.repository.ChamberRepository;
import com.dhomoni.search.repository.search.ChamberSearchRepository;
import com.dhomoni.search.service.dto.ChamberCriteria;
import com.dhomoni.search.service.dto.ChamberDTO;
import com.dhomoni.search.service.mapper.ChamberMapper;

/**
 * Service for executing complex queries for Chamber entities in the database.
 * The main input is a {@link ChamberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChamberDTO} or a {@link Page} of {@link ChamberDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChamberQueryService extends QueryService<Chamber> {

    private final Logger log = LoggerFactory.getLogger(ChamberQueryService.class);

    private final ChamberRepository chamberRepository;

    private final ChamberMapper chamberMapper;

    private final ChamberSearchRepository chamberSearchRepository;

    public ChamberQueryService(ChamberRepository chamberRepository, ChamberMapper chamberMapper, ChamberSearchRepository chamberSearchRepository) {
        this.chamberRepository = chamberRepository;
        this.chamberMapper = chamberMapper;
        this.chamberSearchRepository = chamberSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ChamberDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChamberDTO> findByCriteria(ChamberCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Chamber> specification = createSpecification(criteria);
        return chamberMapper.toDto(chamberRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChamberDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChamberDTO> findByCriteria(ChamberCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Chamber> specification = createSpecification(criteria);
        return chamberRepository.findAll(specification, page)
            .map(chamberMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChamberCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Chamber> specification = createSpecification(criteria);
        return chamberRepository.count(specification);
    }

    /**
     * Function to convert ChamberCriteria to a {@link Specification}
     */
    private Specification<Chamber> createSpecification(ChamberCriteria criteria) {
        Specification<Chamber> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Chamber_.id));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Chamber_.address));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Chamber_.phone));
            }
            if (criteria.getFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFee(), Chamber_.fee));
            }
            if (criteria.getDoctorId() != null) {
                specification = specification.and(buildSpecification(criteria.getDoctorId(),
                    root -> root.join(Chamber_.doctor, JoinType.LEFT).get(Doctor_.id)));
            }
            if (criteria.getWeeklyVisitingHoursId() != null) {
                specification = specification.and(buildSpecification(criteria.getWeeklyVisitingHoursId(),
                    root -> root.join(Chamber_.weeklyVisitingHours, JoinType.LEFT).get(WeeklyVisitingHour_.id)));
            }
        }
        return specification;
    }
}
