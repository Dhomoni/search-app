package com.dhomoni.search.service;

import com.dhomoni.search.domain.Medicine;
import com.dhomoni.search.repository.MedicineRepository;
import com.dhomoni.search.repository.search.MedicineSearchRepository;
import com.dhomoni.search.service.dto.MedicineDTO;
import com.dhomoni.search.service.mapper.MedicineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Medicine.
 */
@Service
@Transactional
public class MedicineService {

    private final Logger log = LoggerFactory.getLogger(MedicineService.class);

    private final MedicineRepository medicineRepository;

    private final MedicineMapper medicineMapper;

    private final MedicineSearchRepository medicineSearchRepository;

    public MedicineService(MedicineRepository medicineRepository, MedicineMapper medicineMapper, MedicineSearchRepository medicineSearchRepository) {
        this.medicineRepository = medicineRepository;
        this.medicineMapper = medicineMapper;
        this.medicineSearchRepository = medicineSearchRepository;
    }

    /**
     * Save a medicine.
     *
     * @param medicineDTO the entity to save
     * @return the persisted entity
     */
    public MedicineDTO save(MedicineDTO medicineDTO) {
        log.debug("Request to save Medicine : {}", medicineDTO);

        Medicine medicine = medicineMapper.toEntity(medicineDTO);
        medicine = medicineRepository.save(medicine);
        MedicineDTO result = medicineMapper.toDto(medicine);
        medicineSearchRepository.save(medicine);
        return result;
    }

    /**
     * Get all the medicines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MedicineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Medicines");
        return medicineRepository.findAll(pageable)
            .map(medicineMapper::toDto);
    }


    /**
     * Get one medicine by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<MedicineDTO> findOne(Long id) {
        log.debug("Request to get Medicine : {}", id);
        return medicineRepository.findById(id)
            .map(medicineMapper::toDto);
    }

    /**
     * Delete the medicine by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Medicine : {}", id);
        medicineRepository.deleteById(id);
        medicineSearchRepository.deleteById(id);
    }

    /**
     * Search for the medicine corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MedicineDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Medicines for query {}", query);
        return medicineSearchRepository.search(queryStringQuery(query), pageable)
            .map(medicineMapper::toDto);
    }
}
