package com.dhomoni.search.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dhomoni.search.service.ChamberService;
import com.dhomoni.search.web.rest.errors.BadRequestAlertException;
import com.dhomoni.search.web.rest.util.HeaderUtil;
import com.dhomoni.search.web.rest.util.PaginationUtil;
import com.dhomoni.search.service.dto.ChamberDTO;
import com.dhomoni.search.service.dto.ChamberCriteria;
import com.dhomoni.search.service.ChamberQueryService;
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
 * REST controller for managing Chamber.
 */
@RestController
@RequestMapping("/api")
public class ChamberResource {

    private final Logger log = LoggerFactory.getLogger(ChamberResource.class);

    private static final String ENTITY_NAME = "searchChamber";

    private final ChamberService chamberService;

    private final ChamberQueryService chamberQueryService;

    public ChamberResource(ChamberService chamberService, ChamberQueryService chamberQueryService) {
        this.chamberService = chamberService;
        this.chamberQueryService = chamberQueryService;
    }

    /**
     * POST  /chambers : Create a new chamber.
     *
     * @param chamberDTO the chamberDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chamberDTO, or with status 400 (Bad Request) if the chamber has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chambers")
    @Timed
    public ResponseEntity<ChamberDTO> createChamber(@RequestBody ChamberDTO chamberDTO) throws URISyntaxException {
        log.debug("REST request to save Chamber : {}", chamberDTO);
        if (chamberDTO.getId() != null) {
            throw new BadRequestAlertException("A new chamber cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChamberDTO result = chamberService.save(chamberDTO);
        return ResponseEntity.created(new URI("/api/chambers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chambers : Updates an existing chamber.
     *
     * @param chamberDTO the chamberDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chamberDTO,
     * or with status 400 (Bad Request) if the chamberDTO is not valid,
     * or with status 500 (Internal Server Error) if the chamberDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chambers")
    @Timed
    public ResponseEntity<ChamberDTO> updateChamber(@RequestBody ChamberDTO chamberDTO) throws URISyntaxException {
        log.debug("REST request to update Chamber : {}", chamberDTO);
        if (chamberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChamberDTO result = chamberService.save(chamberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chamberDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chambers : get all the chambers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of chambers in body
     */
    @GetMapping("/chambers")
    @Timed
    public ResponseEntity<List<ChamberDTO>> getAllChambers(ChamberCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Chambers by criteria: {}", criteria);
        Page<ChamberDTO> page = chamberQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/chambers");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /chambers/count : count all the chambers.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/chambers/count")
    @Timed
    public ResponseEntity<Long> countChambers(ChamberCriteria criteria) {
        log.debug("REST request to count Chambers by criteria: {}", criteria);
        return ResponseEntity.ok().body(chamberQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /chambers/:id : get the "id" chamber.
     *
     * @param id the id of the chamberDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chamberDTO, or with status 404 (Not Found)
     */
    @GetMapping("/chambers/{id}")
    @Timed
    public ResponseEntity<ChamberDTO> getChamber(@PathVariable Long id) {
        log.debug("REST request to get Chamber : {}", id);
        Optional<ChamberDTO> chamberDTO = chamberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chamberDTO);
    }

    /**
     * DELETE  /chambers/:id : delete the "id" chamber.
     *
     * @param id the id of the chamberDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chambers/{id}")
    @Timed
    public ResponseEntity<Void> deleteChamber(@PathVariable Long id) {
        log.debug("REST request to delete Chamber : {}", id);
        chamberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
