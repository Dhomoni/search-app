package com.dhomoni.search.service;

import com.dhomoni.search.domain.WeeklyVisitingHour;
import com.dhomoni.search.repository.WeeklyVisitingHourRepository;
import com.dhomoni.search.repository.search.WeeklyVisitingHourSearchRepository;
import com.dhomoni.search.service.dto.WeeklyVisitingHourDTO;
import com.dhomoni.search.service.mapper.WeeklyVisitingHourMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing WeeklyVisitingHour.
 */
@Service
@Transactional
public class WeeklyVisitingHourService {

    private final Logger log = LoggerFactory.getLogger(WeeklyVisitingHourService.class);

    private final WeeklyVisitingHourRepository weeklyVisitingHourRepository;

    private final WeeklyVisitingHourMapper weeklyVisitingHourMapper;

    private final WeeklyVisitingHourSearchRepository weeklyVisitingHourSearchRepository;

    public WeeklyVisitingHourService(WeeklyVisitingHourRepository weeklyVisitingHourRepository, WeeklyVisitingHourMapper weeklyVisitingHourMapper, WeeklyVisitingHourSearchRepository weeklyVisitingHourSearchRepository) {
        this.weeklyVisitingHourRepository = weeklyVisitingHourRepository;
        this.weeklyVisitingHourMapper = weeklyVisitingHourMapper;
        this.weeklyVisitingHourSearchRepository = weeklyVisitingHourSearchRepository;
    }

    /**
     * Save a weeklyVisitingHour.
     *
     * @param weeklyVisitingHourDTO the entity to save
     * @return the persisted entity
     */
    public WeeklyVisitingHourDTO save(WeeklyVisitingHourDTO weeklyVisitingHourDTO) {
        log.debug("Request to save WeeklyVisitingHour : {}", weeklyVisitingHourDTO);

        WeeklyVisitingHour weeklyVisitingHour = weeklyVisitingHourMapper.toEntity(weeklyVisitingHourDTO);
        weeklyVisitingHour = weeklyVisitingHourRepository.save(weeklyVisitingHour);
        WeeklyVisitingHourDTO result = weeklyVisitingHourMapper.toDto(weeklyVisitingHour);
        weeklyVisitingHourSearchRepository.save(weeklyVisitingHour);
        return result;
    }

    /**
     * Get all the weeklyVisitingHours.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WeeklyVisitingHourDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WeeklyVisitingHours");
        return weeklyVisitingHourRepository.findAll(pageable)
            .map(weeklyVisitingHourMapper::toDto);
    }


    /**
     * Get one weeklyVisitingHour by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<WeeklyVisitingHourDTO> findOne(Long id) {
        log.debug("Request to get WeeklyVisitingHour : {}", id);
        return weeklyVisitingHourRepository.findById(id)
            .map(weeklyVisitingHourMapper::toDto);
    }

    /**
     * Delete the weeklyVisitingHour by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WeeklyVisitingHour : {}", id);
        weeklyVisitingHourRepository.deleteById(id);
        weeklyVisitingHourSearchRepository.deleteById(id);
    }

    /**
     * Search for the weeklyVisitingHour corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WeeklyVisitingHourDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WeeklyVisitingHours for query {}", query);
        return weeklyVisitingHourSearchRepository.search(queryStringQuery(query), pageable)
            .map(weeklyVisitingHourMapper::toDto);
    }
}
