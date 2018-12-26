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

import com.dhomoni.search.domain.Medicine;
import com.dhomoni.search.domain.*; // for static metamodels
import com.dhomoni.search.repository.MedicineRepository;
import com.dhomoni.search.repository.search.MedicineSearchRepository;
import com.dhomoni.search.service.dto.MedicineCriteria;
import com.dhomoni.search.service.dto.MedicineDTO;
import com.dhomoni.search.service.mapper.MedicineMapper;

/**
 * Service for executing complex queries for Medicine entities in the database.
 * The main input is a {@link MedicineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MedicineDTO} or a {@link Page} of {@link MedicineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicineQueryService extends QueryService<Medicine> {

    private final Logger log = LoggerFactory.getLogger(MedicineQueryService.class);

    private final MedicineRepository medicineRepository;

    private final MedicineMapper medicineMapper;

    private final MedicineSearchRepository medicineSearchRepository;

    public MedicineQueryService(MedicineRepository medicineRepository, MedicineMapper medicineMapper, MedicineSearchRepository medicineSearchRepository) {
        this.medicineRepository = medicineRepository;
        this.medicineMapper = medicineMapper;
        this.medicineSearchRepository = medicineSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MedicineDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MedicineDTO> findByCriteria(MedicineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Medicine> specification = createSpecification(criteria);
        return medicineMapper.toDto(medicineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MedicineDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicineDTO> findByCriteria(MedicineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medicine> specification = createSpecification(criteria);
        return medicineRepository.findAll(specification, page)
            .map(medicineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Medicine> specification = createSpecification(criteria);
        return medicineRepository.count(specification);
    }

    /**
     * Function to convert MedicineCriteria to a {@link Specification}
     */
    private Specification<Medicine> createSpecification(MedicineCriteria criteria) {
        Specification<Medicine> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Medicine_.id));
            }
            if (criteria.getTradeName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTradeName(), Medicine_.tradeName));
            }
            if (criteria.getUnitQuantity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnitQuantity(), Medicine_.unitQuantity));
            }
            if (criteria.getGenericName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGenericName(), Medicine_.genericName));
            }
            if (criteria.getChemicalName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChemicalName(), Medicine_.chemicalName));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Medicine_.type));
            }
            if (criteria.getManufacturer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getManufacturer(), Medicine_.manufacturer));
            }
            if (criteria.getMrp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMrp(), Medicine_.mrp));
            }
            if (criteria.getIndications() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIndications(), Medicine_.indications));
            }
            if (criteria.getDoseAndAdmin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDoseAndAdmin(), Medicine_.doseAndAdmin));
            }
            if (criteria.getPreparation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPreparation(), Medicine_.preparation));
            }
            if (criteria.getProductUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductUrl(), Medicine_.productUrl));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Medicine_.active));
            }
        }
        return specification;
    }
}
