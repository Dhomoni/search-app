package com.dhomoni.search.service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.persistence.ManyToMany;

import org.elasticsearch.ResourceAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codahale.metrics.annotation.Timed;
import com.dhomoni.search.domain.Disease;
import com.dhomoni.search.domain.Doctor;
import com.dhomoni.search.domain.Medicine;
import com.dhomoni.search.domain.Patient;
import com.dhomoni.search.repository.DiseaseRepository;
import com.dhomoni.search.repository.DoctorRepository;
import com.dhomoni.search.repository.MedicineRepository;
import com.dhomoni.search.repository.PatientRepository;
import com.dhomoni.search.repository.search.DiseaseSearchRepository;
import com.dhomoni.search.repository.search.DoctorSearchRepository;
import com.dhomoni.search.repository.search.MedicineSearchRepository;
import com.dhomoni.search.repository.search.PatientSearchRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Service
@Transactional(readOnly = true)
public class ElasticsearchIndexService {

    private static final Lock reindexLock = new ReentrantLock();

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    private final DiseaseRepository diseaseRepository;

    private final DiseaseSearchRepository diseaseSearchRepository;

    private final DoctorRepository doctorRepository;

    private final DoctorSearchRepository doctorSearchRepository;

    private final MedicineRepository medicineRepository;

    private final MedicineSearchRepository medicineSearchRepository;

    private final PatientRepository patientRepository;

    private final PatientSearchRepository patientSearchRepository;

    private final ElasticsearchOperations elasticsearchTemplate;

    public ElasticsearchIndexService(
        DiseaseRepository diseaseRepository,
        DiseaseSearchRepository diseaseSearchRepository,
        DoctorRepository doctorRepository,
        DoctorSearchRepository doctorSearchRepository,
        MedicineRepository medicineRepository,
        MedicineSearchRepository medicineSearchRepository,
        PatientRepository patientRepository,
        PatientSearchRepository patientSearchRepository,
        ElasticsearchOperations elasticsearchTemplate) {
        this.diseaseRepository = diseaseRepository;
        this.diseaseSearchRepository = diseaseSearchRepository;
        this.doctorRepository = doctorRepository;
        this.doctorSearchRepository = doctorSearchRepository;
        this.medicineRepository = medicineRepository;
        this.medicineSearchRepository = medicineSearchRepository;
        this.patientRepository = patientRepository;
        this.patientSearchRepository = patientSearchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Async
    @Timed
    public void reindexAll() {
        if (reindexLock.tryLock()) {
            try {
                reindexForClass(Disease.class, diseaseRepository, diseaseSearchRepository);
                reindexForClass(Doctor.class, doctorRepository, doctorSearchRepository);
                reindexForClass(Medicine.class, medicineRepository, medicineSearchRepository);
                reindexForClass(Patient.class, patientRepository, patientSearchRepository);
                log.info("Elasticsearch: Successfully performed reindexing");
            } finally {
                reindexLock.unlock();
            }
        } else {
            log.info("Elasticsearch: concurrent reindexing attempt");
        }
    }

    @SuppressWarnings("unchecked")
    private <T, ID extends Serializable> void reindexForClass(Class<T> entityClass, JpaRepository<T, ID> jpaRepository,
                                                              ElasticsearchRepository<T, ID> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (ResourceAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            // if a JHipster entity field is the owner side of a many-to-many relationship, it should be loaded manually
            List<Method> relationshipGetters = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.getType().equals(Set.class))
                .filter(field -> field.getAnnotation(ManyToMany.class) == null
                		|| (field.getAnnotation(ManyToMany.class) != null 
                			&& field.getAnnotation(ManyToMany.class).mappedBy().isEmpty()))
                .filter(field -> field.getAnnotation(JsonIgnore.class) == null)
                .map(field -> {
                    try {
                        return new PropertyDescriptor(field.getName(), entityClass).getReadMethod();
                    } catch (IntrospectionException e) {
                        log.error("Error retrieving getter for class {}, field {}. Field will NOT be indexed",
                            entityClass.getSimpleName(), field.getName(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            int size = 100;
            for (int i = 0; i <= jpaRepository.count() / size; i++) {
                Pageable page = PageRequest.of(i, size);
                log.info("Indexing page {} of {}, size {}", i, jpaRepository.count() / size, size);
                Page<T> results = jpaRepository.findAll(page);
                results.map(result -> {
                    // if there are any relationships to load, do it now
                	relationshipGetters.forEach(method -> {
                        try {
                            // eagerly load the relationship set
                            ((Set) method.invoke(result)).size();
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                    });
                    return result;
                });
                elasticsearchRepository.saveAll(results.getContent());
            }
        }
        log.info("Elasticsearch: Indexed all rows for {}", entityClass.getSimpleName());
    }
}
