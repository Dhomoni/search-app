package com.dhomoni.search.web.rest;

import com.dhomoni.search.SearchApp;

import com.dhomoni.search.config.SecurityBeanOverrideConfiguration;

import com.dhomoni.search.domain.Disease;
import com.dhomoni.search.domain.Symptom;
import com.dhomoni.search.domain.MedicalDepartment;
import com.dhomoni.search.repository.DiseaseRepository;
import com.dhomoni.search.repository.search.DiseaseSearchRepository;
import com.dhomoni.search.service.DiseaseService;
import com.dhomoni.search.service.dto.DiseaseDTO;
import com.dhomoni.search.service.mapper.DiseaseMapper;
import com.dhomoni.search.web.rest.errors.ExceptionTranslator;
import com.dhomoni.search.service.dto.DiseaseCriteria;
import com.dhomoni.search.service.DiseaseQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.dhomoni.search.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DiseaseResource REST controller.
 *
 * @see DiseaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SearchApp.class})
public class DiseaseResourceIntTest {

    private static final String DEFAULT_MEDICAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MEDICAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENERAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GENERAL_NAME = "BBBBBBBBBB";

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Mock
    private DiseaseRepository diseaseRepositoryMock;

    @Autowired
    private DiseaseMapper diseaseMapper;

    @Mock
    private DiseaseService diseaseServiceMock;

    @Autowired
    private DiseaseService diseaseService;

    /**
     * This repository is mocked in the com.dhomoni.search.repository.search test package.
     *
     * @see com.dhomoni.search.repository.search.DiseaseSearchRepositoryMockConfiguration
     */
    @Autowired
    private DiseaseSearchRepository mockDiseaseSearchRepository;

    @Autowired
    private DiseaseQueryService diseaseQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDiseaseMockMvc;

    private Disease disease;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiseaseResource diseaseResource = new DiseaseResource(diseaseService, diseaseQueryService);
        this.restDiseaseMockMvc = MockMvcBuilders.standaloneSetup(diseaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disease createEntity(EntityManager em) {
        Disease disease = new Disease()
            .medicalName(DEFAULT_MEDICAL_NAME)
            .generalName(DEFAULT_GENERAL_NAME);
        return disease;
    }

    @Before
    public void initTest() {
        disease = createEntity(em);
    }

    @Test
    @Transactional
    public void createDisease() throws Exception {
        int databaseSizeBeforeCreate = diseaseRepository.findAll().size();

        // Create the Disease
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);
        restDiseaseMockMvc.perform(post("/api/diseases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diseaseDTO)))
            .andExpect(status().isCreated());

        // Validate the Disease in the database
        List<Disease> diseaseList = diseaseRepository.findAll();
        assertThat(diseaseList).hasSize(databaseSizeBeforeCreate + 1);
        Disease testDisease = diseaseList.get(diseaseList.size() - 1);
        assertThat(testDisease.getMedicalName()).isEqualTo(DEFAULT_MEDICAL_NAME);
        assertThat(testDisease.getGeneralName()).isEqualTo(DEFAULT_GENERAL_NAME);

        // Validate the Disease in Elasticsearch
        verify(mockDiseaseSearchRepository, times(1)).save(testDisease);
    }

    @Test
    @Transactional
    public void createDiseaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = diseaseRepository.findAll().size();

        // Create the Disease with an existing ID
        disease.setId(1L);
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiseaseMockMvc.perform(post("/api/diseases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diseaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Disease in the database
        List<Disease> diseaseList = diseaseRepository.findAll();
        assertThat(diseaseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Disease in Elasticsearch
        verify(mockDiseaseSearchRepository, times(0)).save(disease);
    }

    @Test
    @Transactional
    public void checkMedicalNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = diseaseRepository.findAll().size();
        // set the field null
        disease.setMedicalName(null);

        // Create the Disease, which fails.
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);

        restDiseaseMockMvc.perform(post("/api/diseases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diseaseDTO)))
            .andExpect(status().isBadRequest());

        List<Disease> diseaseList = diseaseRepository.findAll();
        assertThat(diseaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGeneralNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = diseaseRepository.findAll().size();
        // set the field null
        disease.setGeneralName(null);

        // Create the Disease, which fails.
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);

        restDiseaseMockMvc.perform(post("/api/diseases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diseaseDTO)))
            .andExpect(status().isBadRequest());

        List<Disease> diseaseList = diseaseRepository.findAll();
        assertThat(diseaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiseases() throws Exception {
        // Initialize the database
        diseaseRepository.saveAndFlush(disease);

        // Get all the diseaseList
        restDiseaseMockMvc.perform(get("/api/diseases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disease.getId().intValue())))
            .andExpect(jsonPath("$.[*].medicalName").value(hasItem(DEFAULT_MEDICAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].generalName").value(hasItem(DEFAULT_GENERAL_NAME.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllDiseasesWithEagerRelationshipsIsEnabled() throws Exception {
        DiseaseResource diseaseResource = new DiseaseResource(diseaseServiceMock, diseaseQueryService);
        when(diseaseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restDiseaseMockMvc = MockMvcBuilders.standaloneSetup(diseaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDiseaseMockMvc.perform(get("/api/diseases?eagerload=true"))
        .andExpect(status().isOk());

        verify(diseaseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDiseasesWithEagerRelationshipsIsNotEnabled() throws Exception {
        DiseaseResource diseaseResource = new DiseaseResource(diseaseServiceMock, diseaseQueryService);
            when(diseaseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restDiseaseMockMvc = MockMvcBuilders.standaloneSetup(diseaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDiseaseMockMvc.perform(get("/api/diseases?eagerload=true"))
        .andExpect(status().isOk());

            verify(diseaseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getDisease() throws Exception {
        // Initialize the database
        diseaseRepository.saveAndFlush(disease);

        // Get the disease
        restDiseaseMockMvc.perform(get("/api/diseases/{id}", disease.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(disease.getId().intValue()))
            .andExpect(jsonPath("$.medicalName").value(DEFAULT_MEDICAL_NAME.toString()))
            .andExpect(jsonPath("$.generalName").value(DEFAULT_GENERAL_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllDiseasesByMedicalNameIsEqualToSomething() throws Exception {
        // Initialize the database
        diseaseRepository.saveAndFlush(disease);

        // Get all the diseaseList where medicalName equals to DEFAULT_MEDICAL_NAME
        defaultDiseaseShouldBeFound("medicalName.equals=" + DEFAULT_MEDICAL_NAME);

        // Get all the diseaseList where medicalName equals to UPDATED_MEDICAL_NAME
        defaultDiseaseShouldNotBeFound("medicalName.equals=" + UPDATED_MEDICAL_NAME);
    }

    @Test
    @Transactional
    public void getAllDiseasesByMedicalNameIsInShouldWork() throws Exception {
        // Initialize the database
        diseaseRepository.saveAndFlush(disease);

        // Get all the diseaseList where medicalName in DEFAULT_MEDICAL_NAME or UPDATED_MEDICAL_NAME
        defaultDiseaseShouldBeFound("medicalName.in=" + DEFAULT_MEDICAL_NAME + "," + UPDATED_MEDICAL_NAME);

        // Get all the diseaseList where medicalName equals to UPDATED_MEDICAL_NAME
        defaultDiseaseShouldNotBeFound("medicalName.in=" + UPDATED_MEDICAL_NAME);
    }

    @Test
    @Transactional
    public void getAllDiseasesByMedicalNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        diseaseRepository.saveAndFlush(disease);

        // Get all the diseaseList where medicalName is not null
        defaultDiseaseShouldBeFound("medicalName.specified=true");

        // Get all the diseaseList where medicalName is null
        defaultDiseaseShouldNotBeFound("medicalName.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiseasesByGeneralNameIsEqualToSomething() throws Exception {
        // Initialize the database
        diseaseRepository.saveAndFlush(disease);

        // Get all the diseaseList where generalName equals to DEFAULT_GENERAL_NAME
        defaultDiseaseShouldBeFound("generalName.equals=" + DEFAULT_GENERAL_NAME);

        // Get all the diseaseList where generalName equals to UPDATED_GENERAL_NAME
        defaultDiseaseShouldNotBeFound("generalName.equals=" + UPDATED_GENERAL_NAME);
    }

    @Test
    @Transactional
    public void getAllDiseasesByGeneralNameIsInShouldWork() throws Exception {
        // Initialize the database
        diseaseRepository.saveAndFlush(disease);

        // Get all the diseaseList where generalName in DEFAULT_GENERAL_NAME or UPDATED_GENERAL_NAME
        defaultDiseaseShouldBeFound("generalName.in=" + DEFAULT_GENERAL_NAME + "," + UPDATED_GENERAL_NAME);

        // Get all the diseaseList where generalName equals to UPDATED_GENERAL_NAME
        defaultDiseaseShouldNotBeFound("generalName.in=" + UPDATED_GENERAL_NAME);
    }

    @Test
    @Transactional
    public void getAllDiseasesByGeneralNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        diseaseRepository.saveAndFlush(disease);

        // Get all the diseaseList where generalName is not null
        defaultDiseaseShouldBeFound("generalName.specified=true");

        // Get all the diseaseList where generalName is null
        defaultDiseaseShouldNotBeFound("generalName.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiseasesBySymptomsIsEqualToSomething() throws Exception {
        // Initialize the database
        Symptom symptoms = SymptomResourceIntTest.createEntity(em);
        em.persist(symptoms);
        em.flush();
        disease.addSymptoms(symptoms);
        diseaseRepository.saveAndFlush(disease);
        Long symptomsId = symptoms.getId();

        // Get all the diseaseList where symptoms equals to symptomsId
        defaultDiseaseShouldBeFound("symptomsId.equals=" + symptomsId);

        // Get all the diseaseList where symptoms equals to symptomsId + 1
        defaultDiseaseShouldNotBeFound("symptomsId.equals=" + (symptomsId + 1));
    }


    @Test
    @Transactional
    public void getAllDiseasesByMedicalDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        MedicalDepartment medicalDepartment = MedicalDepartmentResourceIntTest.createEntity(em);
        em.persist(medicalDepartment);
        em.flush();
        disease.setMedicalDepartment(medicalDepartment);
        diseaseRepository.saveAndFlush(disease);
        Long medicalDepartmentId = medicalDepartment.getId();

        // Get all the diseaseList where medicalDepartment equals to medicalDepartmentId
        defaultDiseaseShouldBeFound("medicalDepartmentId.equals=" + medicalDepartmentId);

        // Get all the diseaseList where medicalDepartment equals to medicalDepartmentId + 1
        defaultDiseaseShouldNotBeFound("medicalDepartmentId.equals=" + (medicalDepartmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDiseaseShouldBeFound(String filter) throws Exception {
        restDiseaseMockMvc.perform(get("/api/diseases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disease.getId().intValue())))
            .andExpect(jsonPath("$.[*].medicalName").value(hasItem(DEFAULT_MEDICAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].generalName").value(hasItem(DEFAULT_GENERAL_NAME.toString())));

        // Check, that the count call also returns 1
        restDiseaseMockMvc.perform(get("/api/diseases/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
//            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDiseaseShouldNotBeFound(String filter) throws Exception {
        restDiseaseMockMvc.perform(get("/api/diseases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDiseaseMockMvc.perform(get("/api/diseases/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDisease() throws Exception {
        // Get the disease
        restDiseaseMockMvc.perform(get("/api/diseases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDisease() throws Exception {
        // Initialize the database
        diseaseRepository.saveAndFlush(disease);

        int databaseSizeBeforeUpdate = diseaseRepository.findAll().size();

        // Update the disease
        Disease updatedDisease = diseaseRepository.findById(disease.getId()).get();
        // Disconnect from session so that the updates on updatedDisease are not directly saved in db
        em.detach(updatedDisease);
        updatedDisease
            .medicalName(UPDATED_MEDICAL_NAME)
            .generalName(UPDATED_GENERAL_NAME);
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(updatedDisease);

        restDiseaseMockMvc.perform(put("/api/diseases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diseaseDTO)))
            .andExpect(status().isOk());

        // Validate the Disease in the database
        List<Disease> diseaseList = diseaseRepository.findAll();
        assertThat(diseaseList).hasSize(databaseSizeBeforeUpdate);
        Disease testDisease = diseaseList.get(diseaseList.size() - 1);
        assertThat(testDisease.getMedicalName()).isEqualTo(UPDATED_MEDICAL_NAME);
        assertThat(testDisease.getGeneralName()).isEqualTo(UPDATED_GENERAL_NAME);

        // Validate the Disease in Elasticsearch
        verify(mockDiseaseSearchRepository, times(1)).save(testDisease);
    }

    @Test
    @Transactional
    public void updateNonExistingDisease() throws Exception {
        int databaseSizeBeforeUpdate = diseaseRepository.findAll().size();

        // Create the Disease
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiseaseMockMvc.perform(put("/api/diseases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diseaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Disease in the database
        List<Disease> diseaseList = diseaseRepository.findAll();
        assertThat(diseaseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Disease in Elasticsearch
        verify(mockDiseaseSearchRepository, times(0)).save(disease);
    }

    @Test
    @Transactional
    public void deleteDisease() throws Exception {
        // Initialize the database
        diseaseRepository.saveAndFlush(disease);

        int databaseSizeBeforeDelete = diseaseRepository.findAll().size();

        // Get the disease
        restDiseaseMockMvc.perform(delete("/api/diseases/{id}", disease.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Disease> diseaseList = diseaseRepository.findAll();
        assertThat(diseaseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Disease in Elasticsearch
        verify(mockDiseaseSearchRepository, times(1)).deleteById(disease.getId());
    }

//    @Test
//    @Transactional
//    public void searchDisease() throws Exception {
//        // Initialize the database
//        diseaseRepository.saveAndFlush(disease);
//        when(mockDiseaseSearchRepository.search(queryStringQuery("id:" + disease.getId()), PageRequest.of(0, 20)))
//            .thenReturn(new PageImpl<>(Collections.singletonList(disease), PageRequest.of(0, 1), 1));
//        // Search the disease
//        restDiseaseMockMvc.perform(get("/api/_search/diseases?query=id:" + disease.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(disease.getId().intValue())))
//            .andExpect(jsonPath("$.[*].medicalName").value(hasItem(DEFAULT_MEDICAL_NAME)))
//            .andExpect(jsonPath("$.[*].generalName").value(hasItem(DEFAULT_GENERAL_NAME)));
//    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disease.class);
        Disease disease1 = new Disease();
        disease1.setId(1L);
        Disease disease2 = new Disease();
        disease2.setId(disease1.getId());
        assertThat(disease1).isEqualTo(disease2);
        disease2.setId(2L);
        assertThat(disease1).isNotEqualTo(disease2);
        disease1.setId(null);
        assertThat(disease1).isNotEqualTo(disease2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiseaseDTO.class);
        DiseaseDTO diseaseDTO1 = new DiseaseDTO();
        diseaseDTO1.setId(1L);
        DiseaseDTO diseaseDTO2 = new DiseaseDTO();
        assertThat(diseaseDTO1).isNotEqualTo(diseaseDTO2);
        diseaseDTO2.setId(diseaseDTO1.getId());
        assertThat(diseaseDTO1).isEqualTo(diseaseDTO2);
        diseaseDTO2.setId(2L);
        assertThat(diseaseDTO1).isNotEqualTo(diseaseDTO2);
        diseaseDTO1.setId(null);
        assertThat(diseaseDTO1).isNotEqualTo(diseaseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(diseaseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(diseaseMapper.fromId(null)).isNull();
    }
}
