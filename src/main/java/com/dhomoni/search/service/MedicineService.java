package com.dhomoni.search.service;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.idsQuery;
import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhomoni.search.domain.Medicine;
import com.dhomoni.search.repository.MedicineRepository;
import com.dhomoni.search.repository.search.MedicineSearchRepository;
import com.dhomoni.search.service.dto.MedicineDTO;
import com.dhomoni.search.service.dto.SearchDTO;
import com.dhomoni.search.service.mapper.MedicineMapper;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

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
     * Get all the Medicine with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<MedicineDTO> findAllWithEagerRelationships(Pageable pageable) {
        return medicineRepository.findAllWithEagerRelationships(pageable).map(medicineMapper::toDto);
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
        return medicineRepository.findOneWithEagerRelationships(id)
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
     * Search for the patient corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MedicineDTO> search(SearchDTO searchDTO, Pageable pageable) {
        log.debug("Request to search for a page of Medicines for query {}", searchDTO.getQuery());
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQuery = boolQuery().should(simpleQueryStringQuery(searchDTO.getQuery()));
		Iterable<String> tokens = Splitter.on(CharMatcher.anyOf(", ")).omitEmptyStrings().split(searchDTO.getQuery());
		for (String token : tokens) {
			if (StringUtils.isNumeric(token)) {
				boolQuery.should(constantScoreQuery(termQuery("id", token).boost(-1f)));
			}
		}
		SearchQuery searchQuery = queryBuilder.withQuery(boolQuery)
				.withPageable(pageable).withMinScore(0.00001f).build();
        return medicineSearchRepository.search(searchQuery)
            .map(medicineMapper::toDto);
    }
    
	/**
	 * Search for the doctor corresponding to the id.
	 *
	 * @param query    the query of the search
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Optional<MedicineDTO> search(long id) {
		log.debug("Request to search for a doctor for query {}", id);
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		SearchQuery searchQuery = queryBuilder.withQuery(idsQuery().addIds(Long.toString(id))).build();
		Page<Medicine> medicines = medicineSearchRepository.search(searchQuery);
		return medicines.map(medicineMapper::toDto).stream().findFirst();
	}
}
