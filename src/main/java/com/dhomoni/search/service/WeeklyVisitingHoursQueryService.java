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

import com.dhomoni.search.domain.WeeklyVisitingHours;
import com.dhomoni.search.domain.*; // for static metamodels
import com.dhomoni.search.repository.WeeklyVisitingHoursRepository;
import com.dhomoni.search.repository.search.WeeklyVisitingHoursSearchRepository;
import com.dhomoni.search.service.dto.WeeklyVisitingHoursCriteria;
import com.dhomoni.search.service.dto.WeeklyVisitingHoursDTO;
import com.dhomoni.search.service.mapper.WeeklyVisitingHoursMapper;

/**
 * Service for executing complex queries for WeeklyVisitingHours entities in the database.
 * The main input is a {@link WeeklyVisitingHoursCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WeeklyVisitingHoursDTO} or a {@link Page} of {@link WeeklyVisitingHoursDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WeeklyVisitingHoursQueryService extends QueryService<WeeklyVisitingHours> {

    private final Logger log = LoggerFactory.getLogger(WeeklyVisitingHoursQueryService.class);

    private final WeeklyVisitingHoursRepository weeklyVisitingHoursRepository;

    private final WeeklyVisitingHoursMapper weeklyVisitingHoursMapper;

    private final WeeklyVisitingHoursSearchRepository weeklyVisitingHoursSearchRepository;

    public WeeklyVisitingHoursQueryService(WeeklyVisitingHoursRepository weeklyVisitingHoursRepository, WeeklyVisitingHoursMapper weeklyVisitingHoursMapper, WeeklyVisitingHoursSearchRepository weeklyVisitingHoursSearchRepository) {
        this.weeklyVisitingHoursRepository = weeklyVisitingHoursRepository;
        this.weeklyVisitingHoursMapper = weeklyVisitingHoursMapper;
        this.weeklyVisitingHoursSearchRepository = weeklyVisitingHoursSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WeeklyVisitingHoursDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WeeklyVisitingHoursDTO> findByCriteria(WeeklyVisitingHoursCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WeeklyVisitingHours> specification = createSpecification(criteria);
        return weeklyVisitingHoursMapper.toDto(weeklyVisitingHoursRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WeeklyVisitingHoursDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WeeklyVisitingHoursDTO> findByCriteria(WeeklyVisitingHoursCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WeeklyVisitingHours> specification = createSpecification(criteria);
        return weeklyVisitingHoursRepository.findAll(specification, page)
            .map(weeklyVisitingHoursMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WeeklyVisitingHoursCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WeeklyVisitingHours> specification = createSpecification(criteria);
        return weeklyVisitingHoursRepository.count(specification);
    }

    /**
     * Function to convert WeeklyVisitingHoursCriteria to a {@link Specification}
     */
    private Specification<WeeklyVisitingHours> createSpecification(WeeklyVisitingHoursCriteria criteria) {
        Specification<WeeklyVisitingHours> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), WeeklyVisitingHours_.id));
            }
            if (criteria.getWeekDay() != null) {
                specification = specification.and(buildSpecification(criteria.getWeekDay(), WeeklyVisitingHours_.weekDay));
            }
            if (criteria.getStartHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartHour(), WeeklyVisitingHours_.startHour));
            }
            if (criteria.getStartMinute() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartMinute(), WeeklyVisitingHours_.startMinute));
            }
            if (criteria.getEndHour() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndHour(), WeeklyVisitingHours_.endHour));
            }
            if (criteria.getEndMinute() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndMinute(), WeeklyVisitingHours_.endMinute));
            }
            if (criteria.getChamberId() != null) {
                specification = specification.and(buildSpecification(criteria.getChamberId(),
                    root -> root.join(WeeklyVisitingHours_.chamber, JoinType.LEFT).get(Chamber_.id)));
            }
        }
        return specification;
    }
}
