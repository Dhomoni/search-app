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

import com.dhomoni.search.domain.WeeklyVisitingHour;
import com.dhomoni.search.domain.*; // for static metamodels
import com.dhomoni.search.repository.WeeklyVisitingHourRepository;
import com.dhomoni.search.service.dto.WeeklyVisitingHourCriteria;
import com.dhomoni.search.service.dto.WeeklyVisitingHourDTO;
import com.dhomoni.search.service.mapper.WeeklyVisitingHourMapper;

/**
 * Service for executing complex queries for WeeklyVisitingHour entities in the database.
 * The main input is a {@link WeeklyVisitingHourCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WeeklyVisitingHourDTO} or a {@link Page} of {@link WeeklyVisitingHourDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WeeklyVisitingHourQueryService extends QueryService<WeeklyVisitingHour> {

    private final Logger log = LoggerFactory.getLogger(WeeklyVisitingHourQueryService.class);

    private final WeeklyVisitingHourRepository weeklyVisitingHourRepository;

    private final WeeklyVisitingHourMapper weeklyVisitingHourMapper;

    public WeeklyVisitingHourQueryService(WeeklyVisitingHourRepository weeklyVisitingHourRepository, WeeklyVisitingHourMapper weeklyVisitingHourMapper) {
        this.weeklyVisitingHourRepository = weeklyVisitingHourRepository;
        this.weeklyVisitingHourMapper = weeklyVisitingHourMapper;
    }

    /**
     * Return a {@link List} of {@link WeeklyVisitingHourDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WeeklyVisitingHourDTO> findByCriteria(WeeklyVisitingHourCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WeeklyVisitingHour> specification = createSpecification(criteria);
        return weeklyVisitingHourMapper.toDto(weeklyVisitingHourRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WeeklyVisitingHourDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WeeklyVisitingHourDTO> findByCriteria(WeeklyVisitingHourCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WeeklyVisitingHour> specification = createSpecification(criteria);
        return weeklyVisitingHourRepository.findAll(specification, page)
            .map(weeklyVisitingHourMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WeeklyVisitingHourCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WeeklyVisitingHour> specification = createSpecification(criteria);
        return weeklyVisitingHourRepository.count(specification);
    }

    /**
     * Function to convert WeeklyVisitingHourCriteria to a {@link Specification}
     */
    private Specification<WeeklyVisitingHour> createSpecification(WeeklyVisitingHourCriteria criteria) {
        Specification<WeeklyVisitingHour> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), WeeklyVisitingHour_.id));
            }
            if (criteria.getWeekDay() != null) {
                specification = specification.and(buildSpecification(criteria.getWeekDay(), WeeklyVisitingHour_.weekDay));
            }
            if (criteria.getStartHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartHour(), WeeklyVisitingHour_.startHour));
            }
            if (criteria.getStartMinute() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartMinute(), WeeklyVisitingHour_.startMinute));
            }
            if (criteria.getEndHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndHour(), WeeklyVisitingHour_.endHour));
            }
            if (criteria.getEndMinute() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndMinute(), WeeklyVisitingHour_.endMinute));
            }
            if (criteria.getChamberId() != null) {
                specification = specification.and(buildSpecification(criteria.getChamberId(),
                    root -> root.join(WeeklyVisitingHour_.chamber, JoinType.LEFT).get(Chamber_.id)));
            }
        }
        return specification;
    }
}
