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

import com.dhomoni.search.domain.Doctor;
import com.dhomoni.search.domain.*; // for static metamodels
import com.dhomoni.search.repository.DoctorRepository;
import com.dhomoni.search.repository.search.DoctorSearchRepository;
import com.dhomoni.search.service.dto.DoctorCriteria;
import com.dhomoni.search.service.dto.DoctorDTO;
import com.dhomoni.search.service.mapper.DoctorMapper;

/**
 * Service for executing complex queries for Doctor entities in the database.
 * The main input is a {@link DoctorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DoctorDTO} or a {@link Page} of {@link DoctorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DoctorQueryService extends QueryService<Doctor> {

    private final Logger log = LoggerFactory.getLogger(DoctorQueryService.class);

    private final DoctorRepository doctorRepository;

    private final DoctorMapper doctorMapper;

    private final DoctorSearchRepository doctorSearchRepository;

    public DoctorQueryService(DoctorRepository doctorRepository, DoctorMapper doctorMapper, DoctorSearchRepository doctorSearchRepository) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
        this.doctorSearchRepository = doctorSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DoctorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DoctorDTO> findByCriteria(DoctorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Doctor> specification = createSpecification(criteria);
        return doctorMapper.toDto(doctorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DoctorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DoctorDTO> findByCriteria(DoctorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Doctor> specification = createSpecification(criteria);
        return doctorRepository.findAll(specification, page)
            .map(doctorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DoctorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Doctor> specification = createSpecification(criteria);
        return doctorRepository.count(specification);
    }

    /**
     * Function to convert DoctorCriteria to a {@link Specification}
     */
    private Specification<Doctor> createSpecification(DoctorCriteria criteria) {
        Specification<Doctor> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Doctor_.id));
            }
            if (criteria.getLicenceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLicenceNumber(), Doctor_.licenceNumber));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Doctor_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Doctor_.lastName));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Doctor_.phone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Doctor_.email));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Doctor_.type));
            }
            if (criteria.getDesignation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesignation(), Doctor_.designation));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), Doctor_.activated));
            }
            if (criteria.getMedicalDepartmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getMedicalDepartmentId(),
                    root -> root.join(Doctor_.medicalDepartment, JoinType.LEFT).get(MedicalDepartment_.id)));
            }
            if (criteria.getChambersId() != null) {
                specification = specification.and(buildSpecification(criteria.getChambersId(),
                    root -> root.join(Doctor_.chambers, JoinType.LEFT).get(Chamber_.id)));
            }
            if (criteria.getProfessionalDegreesId() != null) {
                specification = specification.and(buildSpecification(criteria.getProfessionalDegreesId(),
                    root -> root.join(Doctor_.professionalDegrees, JoinType.LEFT).get(ProfessionalDegree_.id)));
            }
        }
        return specification;
    }
}
