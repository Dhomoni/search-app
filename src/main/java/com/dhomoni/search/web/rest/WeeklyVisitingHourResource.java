package com.dhomoni.search.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.dhomoni.search.service.WeeklyVisitingHourQueryService;
import com.dhomoni.search.service.WeeklyVisitingHourService;
import com.dhomoni.search.service.dto.WeeklyVisitingHourCriteria;
import com.dhomoni.search.service.dto.WeeklyVisitingHourDTO;
import com.dhomoni.search.web.rest.errors.BadRequestAlertException;
import com.dhomoni.search.web.rest.util.HeaderUtil;
import com.dhomoni.search.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing WeeklyVisitingHour.
 */
@RestController
@RequestMapping("/api")
public class WeeklyVisitingHourResource {

    private final Logger log = LoggerFactory.getLogger(WeeklyVisitingHourResource.class);

    private static final String ENTITY_NAME = "searchWeeklyVisitingHour";

    private final WeeklyVisitingHourService weeklyVisitingHourService;

    private final WeeklyVisitingHourQueryService weeklyVisitingHourQueryService;

    public WeeklyVisitingHourResource(WeeklyVisitingHourService weeklyVisitingHourService, WeeklyVisitingHourQueryService weeklyVisitingHourQueryService) {
        this.weeklyVisitingHourService = weeklyVisitingHourService;
        this.weeklyVisitingHourQueryService = weeklyVisitingHourQueryService;
    }

    /**
     * POST  /weekly-visiting-hours : Create a new weeklyVisitingHour.
     *
     * @param weeklyVisitingHourDTO the weeklyVisitingHourDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new weeklyVisitingHourDTO, or with status 400 (Bad Request) if the weeklyVisitingHour has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/weekly-visiting-hours")
    @Timed
    public ResponseEntity<WeeklyVisitingHourDTO> createWeeklyVisitingHour(@Valid @RequestBody WeeklyVisitingHourDTO weeklyVisitingHourDTO) throws URISyntaxException {
        log.debug("REST request to save WeeklyVisitingHour : {}", weeklyVisitingHourDTO);
        if (weeklyVisitingHourDTO.getId() != null) {
            throw new BadRequestAlertException("A new weeklyVisitingHour cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeeklyVisitingHourDTO result = weeklyVisitingHourService.save(weeklyVisitingHourDTO);
        return ResponseEntity.created(new URI("/api/weekly-visiting-hours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /weekly-visiting-hours : Updates an existing weeklyVisitingHour.
     *
     * @param weeklyVisitingHourDTO the weeklyVisitingHourDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated weeklyVisitingHourDTO,
     * or with status 400 (Bad Request) if the weeklyVisitingHourDTO is not valid,
     * or with status 500 (Internal Server Error) if the weeklyVisitingHourDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/weekly-visiting-hours")
    @Timed
    public ResponseEntity<WeeklyVisitingHourDTO> updateWeeklyVisitingHour(@Valid @RequestBody WeeklyVisitingHourDTO weeklyVisitingHourDTO) throws URISyntaxException {
        log.debug("REST request to update WeeklyVisitingHour : {}", weeklyVisitingHourDTO);
        if (weeklyVisitingHourDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WeeklyVisitingHourDTO result = weeklyVisitingHourService.save(weeklyVisitingHourDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, weeklyVisitingHourDTO.getId().toString()))
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
    public ResponseEntity<List<WeeklyVisitingHourDTO>> getAllWeeklyVisitingHours(WeeklyVisitingHourCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WeeklyVisitingHours by criteria: {}", criteria);
        Page<WeeklyVisitingHourDTO> page = weeklyVisitingHourQueryService.findByCriteria(criteria, pageable);
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
    public ResponseEntity<Long> countWeeklyVisitingHours(WeeklyVisitingHourCriteria criteria) {
        log.debug("REST request to count WeeklyVisitingHours by criteria: {}", criteria);
        return ResponseEntity.ok().body(weeklyVisitingHourQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /weekly-visiting-hours/:id : get the "id" weeklyVisitingHour.
     *
     * @param id the id of the weeklyVisitingHourDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the weeklyVisitingHourDTO, or with status 404 (Not Found)
     */
    @GetMapping("/weekly-visiting-hours/{id}")
    @Timed
    public ResponseEntity<WeeklyVisitingHourDTO> getWeeklyVisitingHour(@PathVariable Long id) {
        log.debug("REST request to get WeeklyVisitingHour : {}", id);
        Optional<WeeklyVisitingHourDTO> weeklyVisitingHourDTO = weeklyVisitingHourService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weeklyVisitingHourDTO);
    }

    /**
     * DELETE  /weekly-visiting-hours/:id : delete the "id" weeklyVisitingHour.
     *
     * @param id the id of the weeklyVisitingHourDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/weekly-visiting-hours/{id}")
    @Timed
    public ResponseEntity<Void> deleteWeeklyVisitingHour(@PathVariable Long id) {
        log.debug("REST request to delete WeeklyVisitingHour : {}", id);
        weeklyVisitingHourService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
