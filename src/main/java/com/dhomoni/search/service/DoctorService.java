package com.dhomoni.search.service;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.geoShapeQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.CircleBuilder;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.common.unit.DistanceUnit;
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

import com.dhomoni.search.domain.Doctor;
import com.dhomoni.search.repository.DoctorRepository;
import com.dhomoni.search.repository.search.DoctorSearchRepository;
import com.dhomoni.search.service.dto.DoctorDTO;
import com.dhomoni.search.service.dto.SearchDTO;
import com.dhomoni.search.service.mapper.DoctorMapper;

/**
 * Service Implementation for managing Doctor.
 */
@Service
@Transactional
public class DoctorService {

    private static final int _SEARCH_REDIUS_IN_KM = 20;

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
        searchDTO.getLocation().ifPresent(loc -> {
			try {
		        CircleBuilder circleBuilder = ShapeBuilders.newCircleBuilder().radius(_SEARCH_REDIUS_IN_KM, DistanceUnit.KILOMETERS);
				queryBuilder.withFilter(geoShapeQuery("chambers.location", circleBuilder.center(loc.getY(), loc.getX()))
											.relation(ShapeRelation.WITHIN));
			} catch (IOException e) {
				log.debug(e.getMessage(), e);
			}
		});
        String[] excludeFields = {"registrationId", "licenceNumber", "nationalId", "passportNo"};
        BoolQueryBuilder boolQuery = boolQuery()
        		.should(simpleQueryStringQuery(searchDTO.getQuery()))
        		.should(nestedQuery("professionalDegrees", constantScoreQuery(termQuery("professionalDegrees.institute.verbatim", searchDTO.getQuery())), ScoreMode.Avg))
        		.should(nestedQuery("professionalDegrees", matchQuery("professionalDegrees.institute", searchDTO.getQuery()), ScoreMode.Avg).boost(-1f));        
        if(StringUtils.isNumeric(searchDTO.getQuery())) {
        	boolQuery.should(constantScoreQuery(termQuery("id", searchDTO.getQuery()).boost(-1f)))
        		.should(constantScoreQuery(termQuery("medicalDepartment.id", searchDTO.getQuery()).boost(-1f)))
        		.should(nestedQuery("chambers", constantScoreQuery(termQuery("chambers.id", searchDTO.getQuery())), ScoreMode.Avg).boost(-1f))
        		.should(nestedQuery("chambers", nestedQuery("chambers.weeklyVisitingHours", constantScoreQuery(termQuery("chambers.weeklyVisitingHours.id", searchDTO.getQuery())), ScoreMode.Avg), ScoreMode.Avg).boost(-1f))
        		.should(nestedQuery("professionalDegrees", constantScoreQuery(termQuery("professionalDegrees.id", searchDTO.getQuery())), ScoreMode.Avg).boost(-1f));
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
}
