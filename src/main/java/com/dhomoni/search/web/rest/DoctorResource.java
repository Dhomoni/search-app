package com.dhomoni.search.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dhomoni.search.service.DoctorService;
import com.dhomoni.search.web.rest.errors.BadRequestAlertException;
import com.dhomoni.search.web.rest.util.HeaderUtil;
import com.dhomoni.search.web.rest.util.PaginationUtil;
import com.dhomoni.search.service.dto.DoctorDTO;
import com.dhomoni.search.service.dto.SearchDTO;
import com.dhomoni.search.service.dto.DoctorCriteria;
import com.dhomoni.search.service.DoctorQueryService;
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

/**
 * REST controller for managing Doctor.
 */
@RestController
@RequestMapping("/api")
public class DoctorResource {

    private final Logger log = LoggerFactory.getLogger(DoctorResource.class);

    private static final String ENTITY_NAME = "searchDoctor";

    private final DoctorService doctorService;

    private final DoctorQueryService doctorQueryService;

    public DoctorResource(DoctorService doctorService, DoctorQueryService doctorQueryService) {
        this.doctorService = doctorService;
        this.doctorQueryService = doctorQueryService;
    }

    /**
     * POST  /doctors : Create a new doctor.
     *
     * @param doctorDTO the doctorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new doctorDTO, or with status 400 (Bad Request) if the doctor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/doctors")
    @Timed
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO) throws URISyntaxException {
        log.debug("REST request to save Doctor : {}", doctorDTO);
        if (doctorDTO.getId() != null) {
            throw new BadRequestAlertException("A new doctor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoctorDTO result = doctorService.save(doctorDTO);
        return ResponseEntity.created(new URI("/api/doctors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /doctors : Updates an existing doctor.
     *
     * @param doctorDTO the doctorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated doctorDTO,
     * or with status 400 (Bad Request) if the doctorDTO is not valid,
     * or with status 500 (Internal Server Error) if the doctorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/doctors")
    @Timed
    public ResponseEntity<DoctorDTO> updateDoctor(@Valid @RequestBody DoctorDTO doctorDTO) throws URISyntaxException {
        log.debug("REST request to update Doctor : {}", doctorDTO);
        if (doctorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DoctorDTO result = doctorService.save(doctorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, doctorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /doctors : get all the doctors.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of doctors in body
     */
    @GetMapping("/doctors")
    @Timed
    public ResponseEntity<List<DoctorDTO>> getAllDoctors(DoctorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Doctors by criteria: {}", criteria);
        Page<DoctorDTO> page = doctorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/doctors");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * SEARCH  /_search/doctors?query=:query : search for the doctor corresponding
     * to the query.
     *
     * @param query the query of the doctor search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/doctors")
    @Timed
    public ResponseEntity<List<DoctorDTO>> searchDoctors(@Valid SearchDTO searchDTO, Pageable pageable) {
        log.debug("REST request to search for a page of Doctors for query {}", searchDTO.getQuery());
        Page<DoctorDTO> page = doctorService.search(searchDTO, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(searchDTO.getQuery(), page, "/api/_search/doctors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * SEARCH  /_search/doctor?id=:doctorId : search for the doctor corresponding
     * to the id.
     *
     * @param query the query of the doctor search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/doctors/{id}")
    @Timed
    public ResponseEntity<Optional<DoctorDTO>> searchDoctor(@PathVariable Long id) {
        log.debug("REST request to search for a doctor for query {}", id);
        Optional<DoctorDTO> doctorDTO = doctorService.search(id);
        return new ResponseEntity<>(doctorDTO, HttpStatus.OK);
    }

    /**
    * GET  /doctors/count : count all the doctors.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/doctors/count")
    @Timed
    public ResponseEntity<Long> countDoctors(DoctorCriteria criteria) {
        log.debug("REST request to count Doctors by criteria: {}", criteria);
        return ResponseEntity.ok().body(doctorQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /doctors/:id : get the "id" doctor.
     *
     * @param id the id of the doctorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the doctorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/doctors/{id}")
    @Timed
    public ResponseEntity<DoctorDTO> getDoctor(@PathVariable Long id) {
        log.debug("REST request to get Doctor : {}", id);
        Optional<DoctorDTO> doctorDTO = doctorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorDTO);
    }

    /**
     * DELETE  /doctors/:id : delete the "id" doctor.
     *
     * @param id the id of the doctorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/doctors/{id}")
    @Timed
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        log.debug("REST request to delete Doctor : {}", id);
        doctorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
