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
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhomoni.search.domain.Patient;
import com.dhomoni.search.repository.PatientRepository;
import com.dhomoni.search.repository.search.PatientSearchRepository;
import com.dhomoni.search.service.dto.PatientDTO;
import com.dhomoni.search.service.dto.SearchDTO;
import com.dhomoni.search.service.mapper.PatientMapper;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

/**
 * Service Implementation for managing Patient.
 */
@Service
@Transactional
public class PatientService {

    private final Logger log = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;

    private final PatientMapper patientMapper;

    private final PatientSearchRepository patientSearchRepository;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper, PatientSearchRepository patientSearchRepository) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.patientSearchRepository = patientSearchRepository;
    }

    /**
     * Save a patient.
     *
     * @param patientDTO the entity to save
     * @return the persisted entity
     */
    public PatientDTO save(PatientDTO patientDTO) {
        log.debug("Request to save Patient : {}", patientDTO);

        Patient patient = patientMapper.toEntity(patientDTO);
        patient = patientRepository.save(patient);
        PatientDTO result = patientMapper.toDto(patient);
        patientSearchRepository.save(patient);
        return result;
    }

    /**
     * Get all the patients.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PatientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Patients");
        return patientRepository.findAll(pageable)
            .map(patientMapper::toDto);
    }


    /**
     * Get one patient by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PatientDTO> findOne(Long id) {
        log.debug("Request to get Patient : {}", id);
        return patientRepository.findById(id)
            .map(patientMapper::toDto);
    }

    /**
     * Delete the patient by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Patient : {}", id);
        patientRepository.deleteById(id);
        patientSearchRepository.deleteById(id);
    }

    /**
     * Search for the patient corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PatientDTO> search(SearchDTO searchDTO, Pageable pageable) {
        log.debug("Request to search for a page of Patients for query {}", searchDTO.getQuery());
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        String[] excludeFields = { "registrationId" };
        BoolQueryBuilder boolQuery = boolQuery().should(simpleQueryStringQuery(searchDTO.getQuery()));
		Iterable<String> tokens = Splitter.on(CharMatcher.anyOf(", ")).omitEmptyStrings().split(searchDTO.getQuery());
		for (String token : tokens) {
			if (StringUtils.isNumeric(token)) {
				boolQuery.should(constantScoreQuery(termQuery("id", token).boost(-1f)));
			}
		}
		SearchQuery searchQuery = queryBuilder.withQuery(boolQuery)
				.withSourceFilter(new FetchSourceFilterBuilder().withExcludes(excludeFields).build())
				.withPageable(pageable).withMinScore(0.00001f).build();
        return patientSearchRepository.search(searchQuery)
            .map(patientMapper::toDto);
    }
    
	/**
	 * Search for the doctor corresponding to the id.
	 *
	 * @param query    the query of the search
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Optional<PatientDTO> search(long id) {
		log.debug("Request to search for a doctor for query {}", id);
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		String[] excludeFields = { "registrationId" };
		SearchQuery searchQuery = queryBuilder.withQuery(idsQuery().addIds(Long.toString(id)))
				.withSourceFilter(new FetchSourceFilterBuilder().withExcludes(excludeFields).build())
				.build();
		Page<Patient> patients = patientSearchRepository.search(searchQuery);
		return patients.map(patientMapper::toDto).stream().findFirst();
	}
}
