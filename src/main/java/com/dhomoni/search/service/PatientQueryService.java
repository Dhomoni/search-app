package com.dhomoni.search.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.dhomoni.search.domain.Patient;
import com.dhomoni.search.domain.*; // for static metamodels
import com.dhomoni.search.repository.PatientRepository;
import com.dhomoni.search.repository.search.PatientSearchRepository;
import com.dhomoni.search.service.dto.PatientCriteria;
import com.dhomoni.search.service.dto.PatientDTO;
import com.dhomoni.search.service.mapper.PatientMapper;

/**
 * Service for executing complex queries for Patient entities in the database.
 * The main input is a {@link PatientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PatientDTO} or a {@link Page} of {@link PatientDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatientQueryService extends QueryService<Patient> {

    private final Logger log = LoggerFactory.getLogger(PatientQueryService.class);

    private final PatientRepository patientRepository;

    private final PatientMapper patientMapper;

    private final PatientSearchRepository patientSearchRepository;

    public PatientQueryService(PatientRepository patientRepository, PatientMapper patientMapper, PatientSearchRepository patientSearchRepository) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.patientSearchRepository = patientSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PatientDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PatientDTO> findByCriteria(PatientCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientMapper.toDto(patientRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PatientDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientDTO> findByCriteria(PatientCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.findAll(specification, page)
            .map(patientMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatientCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.count(specification);
    }

    /**
     * Function to convert PatientCriteria to a {@link Specification}
     */
    private Specification<Patient> createSpecification(PatientCriteria criteria) {
        Specification<Patient> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Patient_.id));
            }
            if (criteria.getRegistrationId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegistrationId(), Patient_.registrationId));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Patient_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Patient_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Patient_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Patient_.phone));
            }
            if (criteria.getSex() != null) {
                specification = specification.and(buildSpecification(criteria.getSex(), Patient_.sex));
            }
            if (criteria.getBirthTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthTimestamp(), Patient_.birthTimestamp));
            }
            if (criteria.getBloodGroup() != null) {
                specification = specification.and(buildSpecification(criteria.getBloodGroup(), Patient_.bloodGroup));
            }
            if (criteria.getWeightInKG() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeightInKG(), Patient_.weightInKG));
            }
            if (criteria.getHeightInInch() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeightInInch(), Patient_.heightInInch));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Patient_.address));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), Patient_.activated));
            }
        }
        return specification;
    }
}
