package com.dhomoni.search.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dhomoni.search.service.WeeklyVisitingHoursService;
import com.dhomoni.search.web.rest.errors.BadRequestAlertException;
import com.dhomoni.search.web.rest.util.HeaderUtil;
import com.dhomoni.search.web.rest.util.PaginationUtil;
import com.dhomoni.search.service.dto.WeeklyVisitingHoursDTO;
import com.dhomoni.search.service.dto.WeeklyVisitingHoursCriteria;
import com.dhomoni.search.service.WeeklyVisitingHoursQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing WeeklyVisitingHours.
 */
@RestController
@RequestMapping("/api")
public class WeeklyVisitingHoursResource {

    private final Logger log = LoggerFactory.getLogger(WeeklyVisitingHoursResource.class);

    private static final String ENTITY_NAME = "searchWeeklyVisitingHours";

    private final WeeklyVisitingHoursService weeklyVisitingHoursService;

    private final WeeklyVisitingHoursQueryService weeklyVisitingHoursQueryService;

    public WeeklyVisitingHoursResource(WeeklyVisitingHoursService weeklyVisitingHoursService, WeeklyVisitingHoursQueryService weeklyVisitingHoursQueryService) {
        this.weeklyVisitingHoursService = weeklyVisitingHoursService;
        this.weeklyVisitingHoursQueryService = weeklyVisitingHoursQueryService;
    }

    /**
     * POST  /weekly-visiting-hours : Create a new weeklyVisitingHours.
     *
     * @param weeklyVisitingHoursDTO the weeklyVisitingHoursDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new weeklyVisitingHoursDTO, or with status 400 (Bad Request) if the weeklyVisitingHours has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/weekly-visiting-hours")
    @Timed
    public ResponseEntity<WeeklyVisitingHoursDTO> createWeeklyVisitingHours(@Valid @RequestBody WeeklyVisitingHoursDTO weeklyVisitingHoursDTO) throws URISyntaxException {
        log.debug("REST request to save WeeklyVisitingHours : {}", weeklyVisitingHoursDTO);
        if (weeklyVisitingHoursDTO.getId() != null) {
            throw new BadRequestAlertException("A new weeklyVisitingHours cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeeklyVisitingHoursDTO result = weeklyVisitingHoursService.save(weeklyVisitingHoursDTO);
        return ResponseEntity.created(new URI("/api/weekly-visiting-hours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /weekly-visiting-hours : Updates an existing weeklyVisitingHours.
     *
     * @param weeklyVisitingHoursDTO the weeklyVisitingHoursDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated weeklyVisitingHoursDTO,
     * or with status 400 (Bad Request) if the weeklyVisitingHoursDTO is not valid,
     * or with status 500 (Internal Server Error) if the weeklyVisitingHoursDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/weekly-visiting-hours")
    @Timed
    public ResponseEntity<WeeklyVisitingHoursDTO> updateWeeklyVisitingHours(@Valid @RequestBody WeeklyVisitingHoursDTO weeklyVisitingHoursDTO) throws URISyntaxException {
        log.debug("REST request to update WeeklyVisitingHours : {}", weeklyVisitingHoursDTO);
        if (weeklyVisitingHoursDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WeeklyVisitingHoursDTO result = weeklyVisitingHoursService.save(weeklyVisitingHoursDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, weeklyVisitingHoursDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /weekly-visiting-hours : get all the weeklyVisitingHours.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of weeklyVisitingHours in body
     */
    @GetMapping("/weekly-visiting-hours")
    @Timed
    public ResponseEntity<List<WeeklyVisitingHoursDTO>> getAllWeeklyVisitingHours(WeeklyVisitingHoursCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WeeklyVisitingHours by criteria: {}", criteria);
        Page<WeeklyVisitingHoursDTO> page = weeklyVisitingHoursQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/weekly-visiting-hours");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /weekly-visiting-hours/count : count all the weeklyVisitingHours.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/weekly-visiting-hours/count")
    @Timed
    public ResponseEntity<Long> countWeeklyVisitingHours(WeeklyVisitingHoursCriteria criteria) {
        log.debug("REST request to count WeeklyVisitingHours by criteria: {}", criteria);
        return ResponseEntity.ok().body(weeklyVisitingHoursQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /weekly-visiting-hours/:id : get the "id" weeklyVisitingHours.
     *
     * @param id the id of the weeklyVisitingHoursDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the weeklyVisitingHoursDTO, or with status 404 (Not Found)
     */
    @GetMapping("/weekly-visiting-hours/{id}")
    @Timed
    public ResponseEntity<WeeklyVisitingHoursDTO> getWeeklyVisitingHours(@PathVariable Long id) {
        log.debug("REST request to get WeeklyVisitingHours : {}", id);
        Optional<WeeklyVisitingHoursDTO> weeklyVisitingHoursDTO = weeklyVisitingHoursService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weeklyVisitingHoursDTO);
    }

    /**
     * DELETE  /weekly-visiting-hours/:id : delete the "id" weeklyVisitingHours.
     *
     * @param id the id of the weeklyVisitingHoursDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/weekly-visiting-hours/{id}")
    @Timed
    public ResponseEntity<Void> deleteWeeklyVisitingHours(@PathVariable Long id) {
        log.debug("REST request to delete WeeklyVisitingHours : {}", id);
        weeklyVisitingHoursService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/weekly-visiting-hours?query=:query : search for the weeklyVisitingHours corresponding
     * to the query.
     *
     * @param query the query of the weeklyVisitingHours search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/weekly-visiting-hours")
    @Timed
    public ResponseEntity<List<WeeklyVisitingHoursDTO>> searchWeeklyVisitingHours(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WeeklyVisitingHours for query {}", query);
        Page<WeeklyVisitingHoursDTO> page = weeklyVisitingHoursService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/weekly-visiting-hours");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
