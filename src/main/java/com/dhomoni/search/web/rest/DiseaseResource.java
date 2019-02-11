package com.dhomoni.search.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import com.dhomoni.search.service.DiseaseQueryService;
import com.dhomoni.search.service.DiseaseService;
import com.dhomoni.search.service.dto.DiseaseCriteria;
import com.dhomoni.search.service.dto.DiseaseDTO;
import com.dhomoni.search.service.dto.SearchDTO;
import com.dhomoni.search.web.rest.errors.BadRequestAlertException;
import com.dhomoni.search.web.rest.util.HeaderUtil;
import com.dhomoni.search.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Disease.
 */
@RestController
@RequestMapping("/api")
public class DiseaseResource {

    private final Logger log = LoggerFactory.getLogger(DiseaseResource.class);

    private static final String ENTITY_NAME = "searchDisease";

    private final DiseaseService diseaseService;

    private final DiseaseQueryService diseaseQueryService;

    public DiseaseResource(DiseaseService diseaseService, DiseaseQueryService diseaseQueryService) {
        this.diseaseService = diseaseService;
        this.diseaseQueryService = diseaseQueryService;
    }

    /**
     * POST  /diseases : Create a new disease.
     *
     * @param diseaseDTO the diseaseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new diseaseDTO, or with status 400 (Bad Request) if the disease has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/diseases")
    @Timed
    public ResponseEntity<DiseaseDTO> createDisease(@Valid @RequestBody DiseaseDTO diseaseDTO) throws URISyntaxException {
        log.debug("REST request to save Disease : {}", diseaseDTO);
        if (diseaseDTO.getId() != null) {
            throw new BadRequestAlertException("A new disease cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DiseaseDTO result = diseaseService.save(diseaseDTO);
        return ResponseEntity.created(new URI("/api/diseases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /diseases : Updates an existing disease.
     *
     * @param diseaseDTO the diseaseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated diseaseDTO,
     * or with status 400 (Bad Request) if the diseaseDTO is not valid,
     * or with status 500 (Internal Server Error) if the diseaseDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/diseases")
    @Timed
    public ResponseEntity<DiseaseDTO> updateDisease(@Valid @RequestBody DiseaseDTO diseaseDTO) throws URISyntaxException {
        log.debug("REST request to update Disease : {}", diseaseDTO);
        if (diseaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DiseaseDTO result = diseaseService.save(diseaseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, diseaseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /diseases : get all the diseases.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of diseases in body
     */
    @GetMapping("/diseases")
    @Timed
    public ResponseEntity<List<DiseaseDTO>> getAllDiseases(DiseaseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Diseases by criteria: {}", criteria);
        Page<DiseaseDTO> page = diseaseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/diseases");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * SEARCH  /_search/diseases?query=:query : search for the disease corresponding
     * to the query.
     *
     * @param query the query of the disease search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @PostMapping("/_search/diseases")
    @Timed
    public ResponseEntity<List<DiseaseDTO>> searchDiseases(@Valid @RequestBody SearchDTO searchDTO, Pageable pageable) {
        log.debug("REST request to search for a page of Diseases for searchDTO {}", searchDTO);
        Page<DiseaseDTO> page = diseaseService.search(searchDTO, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(searchDTO.getQuery(), page, "/api/_search/diseases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
    * GET  /diseases/count : count all the diseases.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/diseases/count")
    @Timed
    public ResponseEntity<Long> countDiseases(DiseaseCriteria criteria) {
        log.debug("REST request to count Diseases by criteria: {}", criteria);
        return ResponseEntity.ok().body(diseaseQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /diseases/:id : get the "id" disease.
     *
     * @param id the id of the diseaseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the diseaseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/diseases/{id}")
    @Timed
    public ResponseEntity<DiseaseDTO> getDisease(@PathVariable Long id) {
        log.debug("REST request to get Disease : {}", id);
        Optional<DiseaseDTO> diseaseDTO = diseaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(diseaseDTO);
    }

    /**
     * DELETE  /diseases/:id : delete the "id" disease.
     *
     * @param id the id of the diseaseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/diseases/{id}")
    @Timed
    public ResponseEntity<Void> deleteDisease(@PathVariable Long id) {
        log.debug("REST request to delete Disease : {}", id);
        diseaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
