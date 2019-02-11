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
import com.dhomoni.search.service.MedicalDepartmentQueryService;
import com.dhomoni.search.service.MedicalDepartmentService;
import com.dhomoni.search.service.dto.MedicalDepartmentCriteria;
import com.dhomoni.search.service.dto.MedicalDepartmentDTO;
import com.dhomoni.search.web.rest.errors.BadRequestAlertException;
import com.dhomoni.search.web.rest.util.HeaderUtil;
import com.dhomoni.search.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing MedicalDepartment.
 */
@RestController
@RequestMapping("/api")
public class MedicalDepartmentResource {

    private final Logger log = LoggerFactory.getLogger(MedicalDepartmentResource.class);

    private static final String ENTITY_NAME = "searchMedicalDepartment";

    private final MedicalDepartmentService medicalDepartmentService;

    private final MedicalDepartmentQueryService medicalDepartmentQueryService;

    public MedicalDepartmentResource(MedicalDepartmentService medicalDepartmentService, MedicalDepartmentQueryService medicalDepartmentQueryService) {
        this.medicalDepartmentService = medicalDepartmentService;
        this.medicalDepartmentQueryService = medicalDepartmentQueryService;
    }

    /**
     * POST  /medical-departments : Create a new medicalDepartment.
     *
     * @param medicalDepartmentDTO the medicalDepartmentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicalDepartmentDTO, or with status 400 (Bad Request) if the medicalDepartment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/medical-departments")
    @Timed
    public ResponseEntity<MedicalDepartmentDTO> createMedicalDepartment(@Valid @RequestBody MedicalDepartmentDTO medicalDepartmentDTO) throws URISyntaxException {
        log.debug("REST request to save MedicalDepartment : {}", medicalDepartmentDTO);
        if (medicalDepartmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicalDepartment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MedicalDepartmentDTO result = medicalDepartmentService.save(medicalDepartmentDTO);
        return ResponseEntity.created(new URI("/api/medical-departments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medical-departments : Updates an existing medicalDepartment.
     *
     * @param medicalDepartmentDTO the medicalDepartmentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medicalDepartmentDTO,
     * or with status 400 (Bad Request) if the medicalDepartmentDTO is not valid,
     * or with status 500 (Internal Server Error) if the medicalDepartmentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medical-departments")
    @Timed
    public ResponseEntity<MedicalDepartmentDTO> updateMedicalDepartment(@Valid @RequestBody MedicalDepartmentDTO medicalDepartmentDTO) throws URISyntaxException {
        log.debug("REST request to update MedicalDepartment : {}", medicalDepartmentDTO);
        if (medicalDepartmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MedicalDepartmentDTO result = medicalDepartmentService.save(medicalDepartmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medicalDepartmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medical-departments : get all the medicalDepartments.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of medicalDepartments in body
     */
    @GetMapping("/medical-departments")
    @Timed
    public ResponseEntity<List<MedicalDepartmentDTO>> getAllMedicalDepartments(MedicalDepartmentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MedicalDepartments by criteria: {}", criteria);
        Page<MedicalDepartmentDTO> page = medicalDepartmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medical-departments");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /medical-departments/count : count all the medicalDepartments.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/medical-departments/count")
    @Timed
    public ResponseEntity<Long> countMedicalDepartments(MedicalDepartmentCriteria criteria) {
        log.debug("REST request to count MedicalDepartments by criteria: {}", criteria);
        return ResponseEntity.ok().body(medicalDepartmentQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /medical-departments/:id : get the "id" medicalDepartment.
     *
     * @param id the id of the medicalDepartmentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicalDepartmentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/medical-departments/{id}")
    @Timed
    public ResponseEntity<MedicalDepartmentDTO> getMedicalDepartment(@PathVariable Long id) {
        log.debug("REST request to get MedicalDepartment : {}", id);
        Optional<MedicalDepartmentDTO> medicalDepartmentDTO = medicalDepartmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicalDepartmentDTO);
    }

    /**
     * DELETE  /medical-departments/:id : delete the "id" medicalDepartment.
     *
     * @param id the id of the medicalDepartmentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/medical-departments/{id}")
    @Timed
    public ResponseEntity<Void> deleteMedicalDepartment(@PathVariable Long id) {
        log.debug("REST request to delete MedicalDepartment : {}", id);
        medicalDepartmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
