package com.dhomoni.search.service;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.elasticsearch.ResourceAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.codahale.metrics.annotation.Timed;
import com.dhomoni.search.domain.Disease;
import com.dhomoni.search.domain.Doctor;
import com.dhomoni.search.domain.Medicine;
import com.dhomoni.search.domain.Patient;
import com.dhomoni.search.repository.ChamberRepository;
import com.dhomoni.search.repository.DiseaseRepository;
import com.dhomoni.search.repository.DoctorRepository;
import com.dhomoni.search.repository.MedicineRepository;
import com.dhomoni.search.repository.PatientRepository;
import com.dhomoni.search.repository.search.DiseaseSearchRepository;
import com.dhomoni.search.repository.search.DoctorSearchRepository;
import com.dhomoni.search.repository.search.MedicineSearchRepository;
import com.dhomoni.search.repository.search.PatientSearchRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

@Service
@Transactional
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
	private final ChamberRepository chamberRepository;
	private final ElasticsearchOperations elasticsearchTemplate;

	public ElasticsearchIndexService(DiseaseRepository diseaseRepository,
			DiseaseSearchRepository diseaseSearchRepository, DoctorRepository doctorRepository,
			DoctorSearchRepository doctorSearchRepository, MedicineRepository medicineRepository,
			MedicineSearchRepository medicineSearchRepository, PatientRepository patientRepository,
			PatientSearchRepository patientSearchRepository, ChamberRepository chamberRepository,
			ElasticsearchOperations elasticsearchTemplate)
	{
		this.diseaseRepository = diseaseRepository;
		this.diseaseSearchRepository = diseaseSearchRepository;
		this.doctorRepository = doctorRepository;
		this.doctorSearchRepository = doctorSearchRepository;
		this.medicineRepository = medicineRepository;
		this.medicineSearchRepository = medicineSearchRepository;
		this.patientRepository = patientRepository;
		this.patientSearchRepository = patientSearchRepository;
		this.chamberRepository = chamberRepository;
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	@Async
	@Timed
	public void reindexAll() {
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
		loadImagesAndLocations(geometryFactory);
		if (reindexLock.tryLock()) {
			try {
				reindexForClass(Disease.class, diseaseRepository, diseaseSearchRepository, geometryFactory);
				reindexForDoctor(geometryFactory);
				reindexForClass(Medicine.class, medicineRepository, medicineSearchRepository, geometryFactory);
				reindexForClass(Patient.class, patientRepository, patientSearchRepository, geometryFactory);
				log.info("Elasticsearch: Successfully performed reindexing");
			} finally {
				reindexLock.unlock();
			}
		} else {
			log.info("Elasticsearch: concurrent reindexing attempt");
		}
	}

	private void loadImagesAndLocations(GeometryFactory geometryFactory) {
		try {
			Point location1 = geometryFactory.createPoint(new Coordinate(89.4125, 24.8103));
			Point location2 = geometryFactory.createPoint(new Coordinate(90.4125, 23.8103));
			List<Point> locations = Arrays.asList(location1, location2);
			Path imagePath = new ClassPathResource("static/images/pervez.jpg").getFile().toPath();
			byte[] image = Files.readAllBytes(imagePath);
			if (doctorRepository.count() > 0) {
				int size = 100;
				for (int i = 0; i <= doctorRepository.count() / size; i++) {
					Pageable page = PageRequest.of(i, size);
					log.info("Indexing doctor page {} of {}, size {}", i, doctorRepository.count() / size, size);
					Page<Doctor> results = doctorRepository.findAll(page);
					results.forEach(doctor -> {
						if (doctor.getId().equals(1L)) {
							doctor.setImage(image);
							doctor.setImageContentType(MediaType.IMAGE_JPEG_VALUE);
							doctor.setLocation(locations.get(0));
							doctorRepository.save(doctor);
							AtomicInteger count = new AtomicInteger(0);
							doctor.getChambers().forEach(chamber -> {
								chamber.setLocation(locations.get(count.getAndIncrement()));
								chamberRepository.save(chamber);
							});
						}
					});
				}
			}
			if (patientRepository.count() > 0) {
				int size = 100;
				for (int i = 0; i <= patientRepository.count() / size; i++) {
					Pageable page = PageRequest.of(i, size);
					log.info("Indexing patient page {} of {}, size {}", i, patientRepository.count() / size, size);
					Page<Patient> results = patientRepository.findAll(page);
					results.forEach(patient -> {
						if (patient.getId().equals(1L)) {
							patient.setImage(image);
							patient.setImageContentType(MediaType.IMAGE_JPEG_VALUE);
							patient.setLocation(locations.get(0));
							patientRepository.save(patient);
						}
					});
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

	}

	private void reindexForDoctor(GeometryFactory geometryFactory) {
		elasticsearchTemplate.deleteIndex(Doctor.class);
		try {
			elasticsearchTemplate.createIndex(Doctor.class);
		} catch (ResourceAlreadyExistsException e) {
			// Do nothing. Index was already concurrently recreated by some other service.
		}
		elasticsearchTemplate.putMapping(Doctor.class);
		if (doctorRepository.count() > 0) {
			int size = 100;
			for (int i = 0; i <= doctorRepository.count() / size; i++) {
				Pageable page = PageRequest.of(i, size);
				log.info("Indexing page {} of {}, size {}", i, doctorRepository.count() / size, size);
				Page<Doctor> results = doctorRepository.findAll(page);
				results.getContent().forEach(doctor -> {
					doctor.getChambers().stream().filter(chamber -> chamber.getLocation() != null).forEach(chamber -> {
						Point point = chamber.getLocation();
						String latLonString = point.getY() + ", " + point.getX();
						chamber.setSearchableLocation(latLonString);
					});
				});
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					objectMapper.registerModule(new JtsModule(geometryFactory));
					objectMapper.writeValueAsString(results.getContent());
					
				} catch (JsonProcessingException e) {
					log.error(e.getMessage(), e);
				}
				doctorSearchRepository.saveAll(results.getContent());
			}
		}
		log.info("Elasticsearch: Indexed all rows for {}", Doctor.class.getSimpleName());
	}
	
	private <T, ID extends Serializable> void reindexForClass(Class<T> entityClass, JpaRepository<T, ID> jpaRepository,
			ElasticsearchRepository<T, ID> elasticsearchRepository, GeometryFactory geometryFactory) {
		elasticsearchTemplate.deleteIndex(entityClass);
		try {
			elasticsearchTemplate.createIndex(entityClass);
		} catch (ResourceAlreadyExistsException e) {
			// Do nothing. Index was already concurrently recreated by some other service.
		}
		elasticsearchTemplate.putMapping(entityClass);
		if (jpaRepository.count() > 0) {
			int size = 100;
			for (int i = 0; i <= jpaRepository.count() / size; i++) {
				Pageable page = PageRequest.of(i, size);
				log.info("Indexing page {} of {}, size {}", i, jpaRepository.count() / size, size);
				Page<T> results = jpaRepository.findAll(page);
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					objectMapper.registerModule(new JtsModule(geometryFactory));
					objectMapper.writeValueAsString(results.getContent());
				} catch (JsonProcessingException e) {
					log.error(e.getMessage(), e);
				}
				elasticsearchRepository.saveAll(results.getContent());
			}
		}
		log.info("Elasticsearch: Indexed all rows for {}", entityClass.getSimpleName());
	}
}
