package com.dhomoni.search.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dhomoni.search.service.ProfessionalDegreeService;
import com.dhomoni.search.web.rest.errors.BadRequestAlertException;
import com.dhomoni.search.web.rest.util.HeaderUtil;
import com.dhomoni.search.web.rest.util.PaginationUtil;
import com.dhomoni.search.service.dto.ProfessionalDegreeDTO;
import com.dhomoni.search.service.dto.ProfessionalDegreeCriteria;
import com.dhomoni.search.service.ProfessionalDegreeQueryService;
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
 * REST controller for managing ProfessionalDegree.
 */
@RestController
@RequestMapping("/api")
public class ProfessionalDegreeResource {

    private final Logger log = LoggerFactory.getLogger(ProfessionalDegreeResource.class);

    private static final String ENTITY_NAME = "searchProfessionalDegree";

    private final ProfessionalDegreeService professionalDegreeService;

    private final ProfessionalDegreeQueryService professionalDegreeQueryService;

    public ProfessionalDegreeResource(ProfessionalDegreeService professionalDegreeService, ProfessionalDegreeQueryService professionalDegreeQueryService) {
        this.professionalDegreeService = professionalDegreeService;
        this.professionalDegreeQueryService = professionalDegreeQueryService;
    }

    /**
     * POST  /professional-degrees : Create a new professionalDegree.
     *
     * @param professionalDegreeDTO the professionalDegreeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new professionalDegreeDTO, or with status 400 (Bad Request) if the professionalDegree has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/professional-degrees")
    @Timed
    public ResponseEntity<ProfessionalDegreeDTO> createProfessionalDegree(@RequestBody ProfessionalDegreeDTO professionalDegreeDTO) throws URISyntaxException {
        log.debug("REST request to save ProfessionalDegree : {}", professionalDegreeDTO);
        if (professionalDegreeDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalDegree cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfessionalDegreeDTO result = professionalDegreeService.save(professionalDegreeDTO);
        return ResponseEntity.created(new URI("/api/professional-degrees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /professional-degrees : Updates an existing professionalDegree.
     *
     * @param professionalDegreeDTO the professionalDegreeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated professionalDegreeDTO,
     * or with status 400 (Bad Request) if the professionalDegreeDTO is not valid,
     * or with status 500 (Internal Server Error) if the professionalDegreeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/professional-degrees")
    @Timed
    public ResponseEntity<ProfessionalDegreeDTO> updateProfessionalDegree(@RequestBody ProfessionalDegreeDTO professionalDegreeDTO) throws URISyntaxException {
        log.debug("REST request to update ProfessionalDegree : {}", professionalDegreeDTO);
        if (professionalDegreeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProfessionalDegreeDTO result = professionalDegreeService.save(professionalDegreeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, professionalDegreeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /professional-degrees : get all the professionalDegrees.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of professionalDegrees in body
     */
    @GetMapping("/professional-degrees")
    @Timed
    public ResponseEntity<List<ProfessionalDegreeDTO>> getAllProfessionalDegrees(ProfessionalDegreeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProfessionalDegrees by criteria: {}", criteria);
        Page<ProfessionalDegreeDTO> page = professionalDegreeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/professional-degrees");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /professional-degrees/count : count all the professionalDegrees.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/professional-degrees/count")
    @Timed
    public ResponseEntity<Long> countProfessionalDegrees(ProfessionalDegreeCriteria criteria) {
        log.debug("REST request to count ProfessionalDegrees by criteria: {}", criteria);
        return ResponseEntity.ok().body(professionalDegreeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /professional-degrees/:id : get the "id" professionalDegree.
     *
     * @param id the id of the professionalDegreeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the professionalDegreeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/professional-degrees/{id}")
    @Timed
    public ResponseEntity<ProfessionalDegreeDTO> getProfessionalDegree(@PathVariable Long id) {
        log.debug("REST request to get ProfessionalDegree : {}", id);
        Optional<ProfessionalDegreeDTO> professionalDegreeDTO = professionalDegreeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalDegreeDTO);
    }

    /**
     * DELETE  /professional-degrees/:id : delete the "id" professionalDegree.
     *
     * @param id the id of the professionalDegreeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/professional-degrees/{id}")
    @Timed
    public ResponseEntity<Void> deleteProfessionalDegree(@PathVariable Long id) {
        log.debug("REST request to delete ProfessionalDegree : {}", id);
        professionalDegreeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/professional-degrees?query=:query : search for the professionalDegree corresponding
     * to the query.
     *
     * @param query the query of the professionalDegree search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/professional-degrees")
    @Timed
    public ResponseEntity<List<ProfessionalDegreeDTO>> searchProfessionalDegrees(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProfessionalDegrees for query {}", query);
        Page<ProfessionalDegreeDTO> page = professionalDegreeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/professional-degrees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
