package com.dhomoni.search.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dhomoni.search.service.IndicationService;
import com.dhomoni.search.web.rest.errors.BadRequestAlertException;
import com.dhomoni.search.web.rest.util.HeaderUtil;
import com.dhomoni.search.web.rest.util.PaginationUtil;
import com.dhomoni.search.service.dto.IndicationDTO;
import com.dhomoni.search.service.dto.IndicationCriteria;
import com.dhomoni.search.service.IndicationQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Indication.
 */
@RestController
@RequestMapping("/api")
public class IndicationResource {

    private final Logger log = LoggerFactory.getLogger(IndicationResource.class);

    private static final String ENTITY_NAME = "searchIndication";

    private final IndicationService indicationService;

    private final IndicationQueryService indicationQueryService;

    public IndicationResource(IndicationService indicationService, IndicationQueryService indicationQueryService) {
        this.indicationService = indicationService;
        this.indicationQueryService = indicationQueryService;
    }

    /**
     * POST  /indications : Create a new indication.
     *
     * @param indicationDTO the indicationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new indicationDTO, or with status 400 (Bad Request) if the indication has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/indications")
    @Timed
    public ResponseEntity<IndicationDTO> createIndication(@RequestBody IndicationDTO indicationDTO) throws URISyntaxException {
        log.debug("REST request to save Indication : {}", indicationDTO);
        if (indicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new indication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IndicationDTO result = indicationService.save(indicationDTO);
        return ResponseEntity.created(new URI("/api/indications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /indications : Updates an existing indication.
     *
     * @param indicationDTO the indicationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated indicationDTO,
     * or with status 400 (Bad Request) if the indicationDTO is not valid,
     * or with status 500 (Internal Server Error) if the indicationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/indications")
    @Timed
    public ResponseEntity<IndicationDTO> updateIndication(@RequestBody IndicationDTO indicationDTO) throws URISyntaxException {
        log.debug("REST request to update Indication : {}", indicationDTO);
        if (indicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IndicationDTO result = indicationService.save(indicationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, indicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /indications : get all the indications.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of indications in body
     */
    @GetMapping("/indications")
    @Timed
    public ResponseEntity<List<IndicationDTO>> getAllIndications(IndicationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Indications by criteria: {}", criteria);
        Page<IndicationDTO> page = indicationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/indications");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /indications/count : count all the indications.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/indications/count")
    @Timed
    public ResponseEntity<Long> countIndications(IndicationCriteria criteria) {
        log.debug("REST request to count Indications by criteria: {}", criteria);
        return ResponseEntity.ok().body(indicationQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /indications/:id : get the "id" indication.
     *
     * @param id the id of the indicationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the indicationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/indications/{id}")
    @Timed
    public ResponseEntity<IndicationDTO> getIndication(@PathVariable Long id) {
        log.debug("REST request to get Indication : {}", id);
        Optional<IndicationDTO> indicationDTO = indicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indicationDTO);
    }

    /**
     * DELETE  /indications/:id : delete the "id" indication.
     *
     * @param id the id of the indicationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/indications/{id}")
    @Timed
    public ResponseEntity<Void> deleteIndication(@PathVariable Long id) {
        log.debug("REST request to delete Indication : {}", id);
        indicationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/indications?query=:query : search for the indication corresponding
     * to the query.
     *
     * @param query the query of the indication search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/indications")
    @Timed
    public ResponseEntity<List<IndicationDTO>> searchIndications(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Indications for query {}", query);
        Page<IndicationDTO> page = indicationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/indications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
