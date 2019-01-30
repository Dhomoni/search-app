package com.dhomoni.search.service;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.geoDistanceQuery;
import static org.elasticsearch.index.query.QueryBuilders.idsQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhomoni.search.domain.Doctor;
import com.dhomoni.search.repository.DoctorRepository;
import com.dhomoni.search.repository.search.DoctorSearchRepository;
import com.dhomoni.search.service.dto.DoctorDTO;
import com.dhomoni.search.service.dto.SearchDTO;
import com.dhomoni.search.service.mapper.DoctorMapper;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

/**
 * Service Implementation for managing Doctor.
 */
@Service
@Transactional
public class DoctorService {

	private final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepository doctorRepository;

    private final DoctorMapper doctorMapper;

    private final DoctorSearchRepository doctorSearchRepository;

    public DoctorService(DoctorRepository doctorRepository, DoctorMapper doctorMapper, DoctorSearchRepository doctorSearchRepository) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
        this.doctorSearchRepository = doctorSearchRepository;
    }

    /**
     * Save a doctor.
     *
     * @param doctorDTO the entity to save
     * @return the persisted entity
     */
    public DoctorDTO save(DoctorDTO doctorDTO) {
        log.debug("Request to save Doctor : {}", doctorDTO);

        Doctor doctor = doctorMapper.toEntity(doctorDTO);
        doctor = doctorRepository.save(doctor);
        DoctorDTO result = doctorMapper.toDto(doctor);
        doctorSearchRepository.save(doctor);
        return result;
    }

    /**
     * Get all the doctors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DoctorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Doctors");
        return doctorRepository.findAll(pageable)
            .map(doctorMapper::toDto);
    }


    /**
     * Get one doctor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DoctorDTO> findOne(Long id) {
        log.debug("Request to get Doctor : {}", id);
        return doctorRepository.findById(id)
            .map(doctorMapper::toDto);
    }

    /**
     * Delete the doctor by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Doctor : {}", id);
        doctorRepository.deleteById(id);
        doctorSearchRepository.deleteById(id);
    }

    /**
     * Search for the doctor corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DoctorDTO> search(SearchDTO searchDTO, Pageable pageable) {
        log.debug("Request to search for a page of Doctors for query {}", searchDTO.getQuery());
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = geoDistanceQuery("chambers.searchableLocation");
        searchDTO.getLocation().flatMap(loc -> {
        	geoDistanceQueryBuilder.point(loc.getY(), loc.getX());
        	return searchDTO.getDistance();
        }).ifPresent(distance -> {
        	geoDistanceQueryBuilder.distance(distance, DistanceUnit.KILOMETERS);
	        queryBuilder.withFilter(nestedQuery("chambers", geoDistanceQueryBuilder, ScoreMode.None));
        });
        String[] excludeFields = {"registrationId", "licenceNumber", "nationalId", "passportNo"};
        BoolQueryBuilder boolQuery = boolQuery()
        		.should(simpleQueryStringQuery(searchDTO.getQuery()))
        		.should(nestedQuery("professionalDegrees", constantScoreQuery(termQuery("professionalDegrees.institute.verbatim", searchDTO.getQuery())), ScoreMode.Avg))
        		.should(nestedQuery("professionalDegrees", matchQuery("professionalDegrees.institute", searchDTO.getQuery()), ScoreMode.Avg).boost(-1f));        
        Iterable<String> tokens = Splitter.on(CharMatcher.anyOf(", ")).omitEmptyStrings().split(searchDTO.getQuery());
        for(String token: tokens) {
        	if(StringUtils.isNumeric(token)) {
            	boolQuery.should(constantScoreQuery(termQuery("id", token).boost(-1f)))
            		.should(constantScoreQuery(termQuery("medicalDepartment.id", token).boost(-1f)))
            		.should(nestedQuery("chambers", constantScoreQuery(termQuery("chambers.id", token)), ScoreMode.Avg).boost(-1f))
            		.should(nestedQuery("chambers", nestedQuery("chambers.weeklyVisitingHours", constantScoreQuery(termQuery("chambers.weeklyVisitingHours.id", token)), ScoreMode.Avg), ScoreMode.Avg).boost(-1f))
            		.should(nestedQuery("professionalDegrees", constantScoreQuery(termQuery("professionalDegrees.id", token)), ScoreMode.Avg).boost(-1f));
            }
        }
        
        SearchQuery searchQuery = queryBuilder
        		.withQuery(boolQuery)
        		.withSourceFilter(new FetchSourceFilterBuilder().withExcludes(excludeFields).build())
        		.withPageable(pageable)
        		.withMinScore(0.00001f)
        		.build();
        Page<Doctor> doctors = doctorSearchRepository.search(searchQuery);
        return doctors.map(doctorMapper::toDto);
    }
    
    /**
     * Search for the doctor corresponding to the id.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Optional<DoctorDTO> search(long id) {
        log.debug("Request to search for a doctor for query {}", id);
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        String[] excludeFields = {"registrationId", "licenceNumber", "nationalId", "passportNo"};
        SearchQuery searchQuery = queryBuilder
        		.withQuery(idsQuery().addIds(Long.toString(id)))
        		.withSourceFilter(new FetchSourceFilterBuilder().withExcludes(excludeFields).build())
        		.withMinScore(0.00001f)
        		.build();
        Page<Doctor> doctors = doctorSearchRepository.search(searchQuery);
        return doctors.map(doctorMapper::toDto).stream().findFirst();
    }
}
