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
import org.springframework.http.HttpStatus;
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
import com.dhomoni.search.service.PatientQueryService;
import com.dhomoni.search.service.PatientService;
import com.dhomoni.search.service.dto.PatientCriteria;
import com.dhomoni.search.service.dto.PatientDTO;
import com.dhomoni.search.service.dto.SearchDTO;
import com.dhomoni.search.web.rest.errors.BadRequestAlertException;
import com.dhomoni.search.web.rest.util.HeaderUtil;
import com.dhomoni.search.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Patient.
 */
@RestController
@RequestMapping("/api")
public class PatientResource {

    private final Logger log = LoggerFactory.getLogger(PatientResource.class);

    private static final String ENTITY_NAME = "searchPatient";

    private final PatientService patientService;

    private final PatientQueryService patientQueryService;

    public PatientResource(PatientService patientService, PatientQueryService patientQueryService) {
        this.patientService = patientService;
        this.patientQueryService = patientQueryService;
    }

    /**
     * POST  /patients : Create a new patient.
     *
     * @param patientDTO the patientDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new patientDTO, or with status 400 (Bad Request) if the patient has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/patients")
    @Timed
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patientDTO) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patientDTO);
        if (patientDTO.getId() != null) {
            throw new BadRequestAlertException("A new patient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PatientDTO result = patientService.save(patientDTO);
        return ResponseEntity.created(new URI("/api/patients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /patients : Updates an existing patient.
     *
     * @param patientDTO the patientDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated patientDTO,
     * or with status 400 (Bad Request) if the patientDTO is not valid,
     * or with status 500 (Internal Server Error) if the patientDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/patients")
    @Timed
    public ResponseEntity<PatientDTO> updatePatient(@Valid @RequestBody PatientDTO patientDTO) throws URISyntaxException {
        log.debug("REST request to update Patient : {}", patientDTO);
        if (patientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PatientDTO result = patientService.save(patientDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, patientDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /patients : get all the patients.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of patients in body
     */
    @GetMapping("/patients")
    @Timed
    public ResponseEntity<List<PatientDTO>> getAllPatients(PatientCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Patients by criteria: {}", criteria);
        Page<PatientDTO> page = patientQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/patients");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * SEARCH  /_search/patients?query=:query : search for the patient corresponding
     * to the query.
     *
     * @param query the query of the patient search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @PostMapping("/_search/patients")
    @Timed
    public ResponseEntity<List<PatientDTO>> searchPatients(@Valid @RequestBody SearchDTO searchDTO, Pageable pageable) {
        log.debug("REST request to search for a page of Patients for searchDTO {}", searchDTO);
        Page<PatientDTO> page = patientService.search(searchDTO, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(searchDTO.getQuery(), page, "/api/_search/patients");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * SEARCH  /_search/patient?id=:doctorId : search for the doctor corresponding
     * to the id.
     *
     * @param query the query of the doctor search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/patients/{id}")
    @Timed
    public ResponseEntity<Optional<PatientDTO>> searchPatient(@PathVariable Long id) {
        log.debug("REST request to search for a doctor for query {}", id);
        Optional<PatientDTO> patientDTO = patientService.search(id);
        return new ResponseEntity<>(patientDTO, HttpStatus.OK);
    }

    /**
    * GET  /patients/count : count all the patients.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/patients/count")
    @Timed
    public ResponseEntity<Long> countPatients(PatientCriteria criteria) {
        log.debug("REST request to count Patients by criteria: {}", criteria);
        return ResponseEntity.ok().body(patientQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /patients/:id : get the "id" patient.
     *
     * @param id the id of the patientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the patientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/patients/{id}")
    @Timed
    public ResponseEntity<PatientDTO> getPatient(@PathVariable Long id) {
        log.debug("REST request to get Patient : {}", id);
        Optional<PatientDTO> patientDTO = patientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patientDTO);
    }

    /**
     * DELETE  /patients/:id : delete the "id" patient.
     *
     * @param id the id of the patientDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/patients/{id}")
    @Timed
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        log.debug("REST request to delete Patient : {}", id);
        patientService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
