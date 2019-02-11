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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.dhomoni.search.service.MedicineQueryService;
import com.dhomoni.search.service.MedicineService;
import com.dhomoni.search.service.dto.MedicineCriteria;
import com.dhomoni.search.service.dto.MedicineDTO;
import com.dhomoni.search.web.rest.errors.BadRequestAlertException;
import com.dhomoni.search.web.rest.util.HeaderUtil;
import com.dhomoni.search.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Medicine.
 */
@RestController
@RequestMapping("/api")
public class MedicineResource {

    private final Logger log = LoggerFactory.getLogger(MedicineResource.class);

    private static final String ENTITY_NAME = "searchMedicine";

    private final MedicineService medicineService;

    private final MedicineQueryService medicineQueryService;

    public MedicineResource(MedicineService medicineService, MedicineQueryService medicineQueryService) {
        this.medicineService = medicineService;
        this.medicineQueryService = medicineQueryService;
    }

    /**
     * POST  /medicines : Create a new medicine.
     *
     * @param medicineDTO the medicineDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicineDTO, or with status 400 (Bad Request) if the medicine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/medicines")
    @Timed
    public ResponseEntity<MedicineDTO> createMedicine(@Valid @RequestBody MedicineDTO medicineDTO) throws URISyntaxException {
        log.debug("REST request to save Medicine : {}", medicineDTO);
        if (medicineDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MedicineDTO result = medicineService.save(medicineDTO);
        return ResponseEntity.created(new URI("/api/medicines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medicines : Updates an existing medicine.
     *
     * @param medicineDTO the medicineDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medicineDTO,
     * or with status 400 (Bad Request) if the medicineDTO is not valid,
     * or with status 500 (Internal Server Error) if the medicineDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medicines")
    @Timed
    public ResponseEntity<MedicineDTO> updateMedicine(@Valid @RequestBody MedicineDTO medicineDTO) throws URISyntaxException {
        log.debug("REST request to update Medicine : {}", medicineDTO);
        if (medicineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MedicineDTO result = medicineService.save(medicineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medicineDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medicines : get all the medicines.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of medicines in body
     */
    @GetMapping("/medicines")
    @Timed
    public ResponseEntity<List<MedicineDTO>> getAllMedicines(MedicineCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Medicines by criteria: {}", criteria);
        Page<MedicineDTO> page = medicineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medicines");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /medicines/count : count all the medicines.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/medicines/count")
    @Timed
    public ResponseEntity<Long> countMedicines(MedicineCriteria criteria) {
        log.debug("REST request to count Medicines by criteria: {}", criteria);
        return ResponseEntity.ok().body(medicineQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /medicines/:id : get the "id" medicine.
     *
     * @param id the id of the medicineDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicineDTO, or with status 404 (Not Found)
     */
    @GetMapping("/medicines/{id}")
    @Timed
    public ResponseEntity<MedicineDTO> getMedicine(@PathVariable Long id) {
        log.debug("REST request to get Medicine : {}", id);
        Optional<MedicineDTO> medicineDTO = medicineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicineDTO);
    }

    /**
     * DELETE  /medicines/:id : delete the "id" medicine.
     *
     * @param id the id of the medicineDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/medicines/{id}")
    @Timed
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        log.debug("REST request to delete Medicine : {}", id);
        medicineService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/medicines?query=:query : search for the medicine corresponding
     * to the query.
     *
     * @param query the query of the medicine search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/medicines")
    @Timed
    public ResponseEntity<List<MedicineDTO>> searchMedicines(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Medicines for query {}", query);
        Page<MedicineDTO> page = medicineService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/medicines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
