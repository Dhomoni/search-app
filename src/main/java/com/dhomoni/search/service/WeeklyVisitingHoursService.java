package com.dhomoni.search.service;

import com.dhomoni.search.domain.WeeklyVisitingHours;
import com.dhomoni.search.repository.WeeklyVisitingHoursRepository;
import com.dhomoni.search.repository.search.WeeklyVisitingHoursSearchRepository;
import com.dhomoni.search.service.dto.WeeklyVisitingHoursDTO;
import com.dhomoni.search.service.mapper.WeeklyVisitingHoursMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing WeeklyVisitingHours.
 */
@Service
@Transactional
public class WeeklyVisitingHoursService {

    private final Logger log = LoggerFactory.getLogger(WeeklyVisitingHoursService.class);

    private final WeeklyVisitingHoursRepository weeklyVisitingHoursRepository;

    private final WeeklyVisitingHoursMapper weeklyVisitingHoursMapper;

    private final WeeklyVisitingHoursSearchRepository weeklyVisitingHoursSearchRepository;

    public WeeklyVisitingHoursService(WeeklyVisitingHoursRepository weeklyVisitingHoursRepository, WeeklyVisitingHoursMapper weeklyVisitingHoursMapper, WeeklyVisitingHoursSearchRepository weeklyVisitingHoursSearchRepository) {
        this.weeklyVisitingHoursRepository = weeklyVisitingHoursRepository;
        this.weeklyVisitingHoursMapper = weeklyVisitingHoursMapper;
        this.weeklyVisitingHoursSearchRepository = weeklyVisitingHoursSearchRepository;
    }

    /**
     * Save a weeklyVisitingHours.
     *
     * @param weeklyVisitingHoursDTO the entity to save
     * @return the persisted entity
     */
    public WeeklyVisitingHoursDTO save(WeeklyVisitingHoursDTO weeklyVisitingHoursDTO) {
        log.debug("Request to save WeeklyVisitingHours : {}", weeklyVisitingHoursDTO);

        WeeklyVisitingHours weeklyVisitingHours = weeklyVisitingHoursMapper.toEntity(weeklyVisitingHoursDTO);
        weeklyVisitingHours = weeklyVisitingHoursRepository.save(weeklyVisitingHours);
        WeeklyVisitingHoursDTO result = weeklyVisitingHoursMapper.toDto(weeklyVisitingHours);
        weeklyVisitingHoursSearchRepository.save(weeklyVisitingHours);
        return result;
    }

    /**
     * Get all the weeklyVisitingHours.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WeeklyVisitingHoursDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WeeklyVisitingHours");
        return weeklyVisitingHoursRepository.findAll(pageable)
            .map(weeklyVisitingHoursMapper::toDto);
    }


    /**
     * Get one weeklyVisitingHours by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<WeeklyVisitingHoursDTO> findOne(Long id) {
        log.debug("Request to get WeeklyVisitingHours : {}", id);
        return weeklyVisitingHoursRepository.findById(id)
            .map(weeklyVisitingHoursMapper::toDto);
    }

    /**
     * Delete the weeklyVisitingHours by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WeeklyVisitingHours : {}", id);
        weeklyVisitingHoursRepository.deleteById(id);
        weeklyVisitingHoursSearchRepository.deleteById(id);
    }

    /**
     * Search for the weeklyVisitingHours corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WeeklyVisitingHoursDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WeeklyVisitingHours for query {}", query);
        return weeklyVisitingHoursSearchRepository.search(queryStringQuery(query), pageable)
            .map(weeklyVisitingHoursMapper::toDto);
    }
}
