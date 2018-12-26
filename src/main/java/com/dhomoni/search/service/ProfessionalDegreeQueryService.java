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

import com.dhomoni.search.domain.ProfessionalDegree;
import com.dhomoni.search.domain.*; // for static metamodels
import com.dhomoni.search.repository.ProfessionalDegreeRepository;
import com.dhomoni.search.repository.search.ProfessionalDegreeSearchRepository;
import com.dhomoni.search.service.dto.ProfessionalDegreeCriteria;
import com.dhomoni.search.service.dto.ProfessionalDegreeDTO;
import com.dhomoni.search.service.mapper.ProfessionalDegreeMapper;

/**
 * Service for executing complex queries for ProfessionalDegree entities in the database.
 * The main input is a {@link ProfessionalDegreeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProfessionalDegreeDTO} or a {@link Page} of {@link ProfessionalDegreeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfessionalDegreeQueryService extends QueryService<ProfessionalDegree> {

    private final Logger log = LoggerFactory.getLogger(ProfessionalDegreeQueryService.class);

    private final ProfessionalDegreeRepository professionalDegreeRepository;

    private final ProfessionalDegreeMapper professionalDegreeMapper;

    private final ProfessionalDegreeSearchRepository professionalDegreeSearchRepository;

    public ProfessionalDegreeQueryService(ProfessionalDegreeRepository professionalDegreeRepository, ProfessionalDegreeMapper professionalDegreeMapper, ProfessionalDegreeSearchRepository professionalDegreeSearchRepository) {
        this.professionalDegreeRepository = professionalDegreeRepository;
        this.professionalDegreeMapper = professionalDegreeMapper;
        this.professionalDegreeSearchRepository = professionalDegreeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProfessionalDegreeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProfessionalDegreeDTO> findByCriteria(ProfessionalDegreeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProfessionalDegree> specification = createSpecification(criteria);
        return professionalDegreeMapper.toDto(professionalDegreeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProfessionalDegreeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalDegreeDTO> findByCriteria(ProfessionalDegreeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProfessionalDegree> specification = createSpecification(criteria);
        return professionalDegreeRepository.findAll(specification, page)
            .map(professionalDegreeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProfessionalDegreeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProfessionalDegree> specification = createSpecification(criteria);
        return professionalDegreeRepository.count(specification);
    }

    /**
     * Function to convert ProfessionalDegreeCriteria to a {@link Specification}
     */
    private Specification<ProfessionalDegree> createSpecification(ProfessionalDegreeCriteria criteria) {
        Specification<ProfessionalDegree> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProfessionalDegree_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProfessionalDegree_.name));
            }
            if (criteria.getInstitute() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInstitute(), ProfessionalDegree_.institute));
            }
            if (criteria.getPassingYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPassingYear(), ProfessionalDegree_.passingYear));
            }
            if (criteria.getDoctorId() != null) {
                specification = specification.and(buildSpecification(criteria.getDoctorId(),
                    root -> root.join(ProfessionalDegree_.doctor, JoinType.LEFT).get(Doctor_.id)));
            }
        }
        return specification;
    }
}
