package com.dhomoni.search.web.rest;

import com.dhomoni.search.SearchApp;

import com.dhomoni.search.config.SecurityBeanOverrideConfiguration;

import com.dhomoni.search.domain.ProfessionalDegree;
import com.dhomoni.search.domain.Doctor;
import com.dhomoni.search.repository.ProfessionalDegreeRepository;
import com.dhomoni.search.repository.search.ProfessionalDegreeSearchRepository;
import com.dhomoni.search.service.ProfessionalDegreeService;
import com.dhomoni.search.service.dto.ProfessionalDegreeDTO;
import com.dhomoni.search.service.mapper.ProfessionalDegreeMapper;
import com.dhomoni.search.web.rest.errors.ExceptionTranslator;
import com.dhomoni.search.service.dto.ProfessionalDegreeCriteria;
import com.dhomoni.search.service.ProfessionalDegreeQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
 * Test class for the ProfessionalDegreeResource REST controller.
 *
 * @see ProfessionalDegreeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SearchApp.class})
public class ProfessionalDegreeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTE = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PASSING_YEAR = 2014;
    private static final Integer UPDATED_PASSING_YEAR = 2018;

    @Autowired
    private ProfessionalDegreeRepository professionalDegreeRepository;

    @Autowired
    private ProfessionalDegreeMapper professionalDegreeMapper;

    @Autowired
    private ProfessionalDegreeService professionalDegreeService;

    /**
     * This repository is mocked in the com.dhomoni.search.repository.search test package.
     *
     * @see com.dhomoni.search.repository.search.ProfessionalDegreeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProfessionalDegreeSearchRepository mockProfessionalDegreeSearchRepository;

    @Autowired
    private ProfessionalDegreeQueryService professionalDegreeQueryService;

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

    private MockMvc restProfessionalDegreeMockMvc;

    private ProfessionalDegree professionalDegree;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfessionalDegreeResource professionalDegreeResource = new ProfessionalDegreeResource(professionalDegreeService, professionalDegreeQueryService);
        this.restProfessionalDegreeMockMvc = MockMvcBuilders.standaloneSetup(professionalDegreeResource)
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
    public static ProfessionalDegree createEntity(EntityManager em) {
        ProfessionalDegree professionalDegree = new ProfessionalDegree()
            .name(DEFAULT_NAME)
            .institute(DEFAULT_INSTITUTE)
            .passingYear(DEFAULT_PASSING_YEAR);
        return professionalDegree;
    }

    @Before
    public void initTest() {
        professionalDegree = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfessionalDegree() throws Exception {
        int databaseSizeBeforeCreate = professionalDegreeRepository.findAll().size();

        // Create the ProfessionalDegree
        ProfessionalDegreeDTO professionalDegreeDTO = professionalDegreeMapper.toDto(professionalDegree);
        restProfessionalDegreeMockMvc.perform(post("/api/professional-degrees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalDegreeDTO)))
            .andExpect(status().isCreated());

        // Validate the ProfessionalDegree in the database
        List<ProfessionalDegree> professionalDegreeList = professionalDegreeRepository.findAll();
        assertThat(professionalDegreeList).hasSize(databaseSizeBeforeCreate + 1);
        ProfessionalDegree testProfessionalDegree = professionalDegreeList.get(professionalDegreeList.size() - 1);
        assertThat(testProfessionalDegree.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProfessionalDegree.getInstitute()).isEqualTo(DEFAULT_INSTITUTE);
        assertThat(testProfessionalDegree.getPassingYear()).isEqualTo(DEFAULT_PASSING_YEAR);

        // Validate the ProfessionalDegree in Elasticsearch
        verify(mockProfessionalDegreeSearchRepository, times(1)).save(testProfessionalDegree);
    }

    @Test
    @Transactional
    public void createProfessionalDegreeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = professionalDegreeRepository.findAll().size();

        // Create the ProfessionalDegree with an existing ID
        professionalDegree.setId(1L);
        ProfessionalDegreeDTO professionalDegreeDTO = professionalDegreeMapper.toDto(professionalDegree);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalDegreeMockMvc.perform(post("/api/professional-degrees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalDegreeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalDegree in the database
        List<ProfessionalDegree> professionalDegreeList = professionalDegreeRepository.findAll();
        assertThat(professionalDegreeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProfessionalDegree in Elasticsearch
        verify(mockProfessionalDegreeSearchRepository, times(0)).save(professionalDegree);
    }

    @Test
    @Transactional
    public void getAllProfessionalDegrees() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        // Get all the professionalDegreeList
        restProfessionalDegreeMockMvc.perform(get("/api/professional-degrees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalDegree.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].institute").value(hasItem(DEFAULT_INSTITUTE.toString())))
            .andExpect(jsonPath("$.[*].passingYear").value(hasItem(DEFAULT_PASSING_YEAR)));
    }
    
    @Test
    @Transactional
    public void getProfessionalDegree() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        // Get the professionalDegree
        restProfessionalDegreeMockMvc.perform(get("/api/professional-degrees/{id}", professionalDegree.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(professionalDegree.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.institute").value(DEFAULT_INSTITUTE.toString()))
            .andExpect(jsonPath("$.passingYear").value(DEFAULT_PASSING_YEAR));
    }

    @Test
    @Transactional
    public void getAllProfessionalDegreesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        // Get all the professionalDegreeList where name equals to DEFAULT_NAME
        defaultProfessionalDegreeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the professionalDegreeList where name equals to UPDATED_NAME
        defaultProfessionalDegreeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProfessionalDegreesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        // Get all the professionalDegreeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProfessionalDegreeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the professionalDegreeList where name equals to UPDATED_NAME
        defaultProfessionalDegreeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProfessionalDegreesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        // Get all the professionalDegreeList where name is not null
        defaultProfessionalDegreeShouldBeFound("name.specified=true");

        // Get all the professionalDegreeList where name is null
        defaultProfessionalDegreeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfessionalDegreesByInstituteIsEqualToSomething() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        // Get all the professionalDegreeList where institute equals to DEFAULT_INSTITUTE
        defaultProfessionalDegreeShouldBeFound("institute.equals=" + DEFAULT_INSTITUTE);

        // Get all the professionalDegreeList where institute equals to UPDATED_INSTITUTE
        defaultProfessionalDegreeShouldNotBeFound("institute.equals=" + UPDATED_INSTITUTE);
    }

    @Test
    @Transactional
    public void getAllProfessionalDegreesByInstituteIsInShouldWork() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        // Get all the professionalDegreeList where institute in DEFAULT_INSTITUTE or UPDATED_INSTITUTE
        defaultProfessionalDegreeShouldBeFound("institute.in=" + DEFAULT_INSTITUTE + "," + UPDATED_INSTITUTE);

        // Get all the professionalDegreeList where institute equals to UPDATED_INSTITUTE
        defaultProfessionalDegreeShouldNotBeFound("institute.in=" + UPDATED_INSTITUTE);
    }

    @Test
    @Transactional
    public void getAllProfessionalDegreesByInstituteIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        // Get all the professionalDegreeList where institute is not null
        defaultProfessionalDegreeShouldBeFound("institute.specified=true");

        // Get all the professionalDegreeList where institute is null
        defaultProfessionalDegreeShouldNotBeFound("institute.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfessionalDegreesByPassingYearIsEqualToSomething() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        // Get all the professionalDegreeList where passingYear equals to DEFAULT_PASSING_YEAR
        defaultProfessionalDegreeShouldBeFound("passingYear.equals=" + DEFAULT_PASSING_YEAR);

        // Get all the professionalDegreeList where passingYear equals to UPDATED_PASSING_YEAR
        defaultProfessionalDegreeShouldNotBeFound("passingYear.equals=" + UPDATED_PASSING_YEAR);
    }

    @Test
    @Transactional
    public void getAllProfessionalDegreesByPassingYearIsInShouldWork() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        // Get all the professionalDegreeList where passingYear in DEFAULT_PASSING_YEAR or UPDATED_PASSING_YEAR
        defaultProfessionalDegreeShouldBeFound("passingYear.in=" + DEFAULT_PASSING_YEAR + "," + UPDATED_PASSING_YEAR);

        // Get all the professionalDegreeList where passingYear equals to UPDATED_PASSING_YEAR
        defaultProfessionalDegreeShouldNotBeFound("passingYear.in=" + UPDATED_PASSING_YEAR);
    }

    @Test
    @Transactional
    public void getAllProfessionalDegreesByPassingYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        // Get all the professionalDegreeList where passingYear is not null
        defaultProfessionalDegreeShouldBeFound("passingYear.specified=true");

        // Get all the professionalDegreeList where passingYear is null
        defaultProfessionalDegreeShouldNotBeFound("passingYear.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfessionalDegreesByPassingYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        // Get all the professionalDegreeList where passingYear greater than or equals to DEFAULT_PASSING_YEAR
        defaultProfessionalDegreeShouldBeFound("passingYear.greaterOrEqualThan=" + DEFAULT_PASSING_YEAR);

        // Get all the professionalDegreeList where passingYear greater than or equals to UPDATED_PASSING_YEAR
        defaultProfessionalDegreeShouldNotBeFound("passingYear.greaterOrEqualThan=" + UPDATED_PASSING_YEAR);
    }

    @Test
    @Transactional
    public void getAllProfessionalDegreesByPassingYearIsLessThanSomething() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        // Get all the professionalDegreeList where passingYear less than or equals to DEFAULT_PASSING_YEAR
        defaultProfessionalDegreeShouldNotBeFound("passingYear.lessThan=" + DEFAULT_PASSING_YEAR);

        // Get all the professionalDegreeList where passingYear less than or equals to UPDATED_PASSING_YEAR
        defaultProfessionalDegreeShouldBeFound("passingYear.lessThan=" + UPDATED_PASSING_YEAR);
    }


    @Test
    @Transactional
    public void getAllProfessionalDegreesByDoctorIsEqualToSomething() throws Exception {
        // Initialize the database
        Doctor doctor = DoctorResourceIntTest.createEntity(em);
        em.persist(doctor);
        em.flush();
        professionalDegree.setDoctor(doctor);
        professionalDegreeRepository.saveAndFlush(professionalDegree);
        Long doctorId = doctor.getId();

        // Get all the professionalDegreeList where doctor equals to doctorId
        defaultProfessionalDegreeShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the professionalDegreeList where doctor equals to doctorId + 1
        defaultProfessionalDegreeShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProfessionalDegreeShouldBeFound(String filter) throws Exception {
        restProfessionalDegreeMockMvc.perform(get("/api/professional-degrees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalDegree.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].institute").value(hasItem(DEFAULT_INSTITUTE.toString())))
            .andExpect(jsonPath("$.[*].passingYear").value(hasItem(DEFAULT_PASSING_YEAR)));

        // Check, that the count call also returns 1
        restProfessionalDegreeMockMvc.perform(get("/api/professional-degrees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
//            .andExpect(content().string("1")); // In case of preloaded data this test causing problem
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProfessionalDegreeShouldNotBeFound(String filter) throws Exception {
        restProfessionalDegreeMockMvc.perform(get("/api/professional-degrees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfessionalDegreeMockMvc.perform(get("/api/professional-degrees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProfessionalDegree() throws Exception {
        // Get the professionalDegree
        restProfessionalDegreeMockMvc.perform(get("/api/professional-degrees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfessionalDegree() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        int databaseSizeBeforeUpdate = professionalDegreeRepository.findAll().size();

        // Update the professionalDegree
        ProfessionalDegree updatedProfessionalDegree = professionalDegreeRepository.findById(professionalDegree.getId()).get();
        // Disconnect from session so that the updates on updatedProfessionalDegree are not directly saved in db
        em.detach(updatedProfessionalDegree);
        updatedProfessionalDegree
            .name(UPDATED_NAME)
            .institute(UPDATED_INSTITUTE)
            .passingYear(UPDATED_PASSING_YEAR);
        ProfessionalDegreeDTO professionalDegreeDTO = professionalDegreeMapper.toDto(updatedProfessionalDegree);

        restProfessionalDegreeMockMvc.perform(put("/api/professional-degrees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalDegreeDTO)))
            .andExpect(status().isOk());

        // Validate the ProfessionalDegree in the database
        List<ProfessionalDegree> professionalDegreeList = professionalDegreeRepository.findAll();
        assertThat(professionalDegreeList).hasSize(databaseSizeBeforeUpdate);
        ProfessionalDegree testProfessionalDegree = professionalDegreeList.get(professionalDegreeList.size() - 1);
        assertThat(testProfessionalDegree.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProfessionalDegree.getInstitute()).isEqualTo(UPDATED_INSTITUTE);
        assertThat(testProfessionalDegree.getPassingYear()).isEqualTo(UPDATED_PASSING_YEAR);

        // Validate the ProfessionalDegree in Elasticsearch
        verify(mockProfessionalDegreeSearchRepository, times(1)).save(testProfessionalDegree);
    }

    @Test
    @Transactional
    public void updateNonExistingProfessionalDegree() throws Exception {
        int databaseSizeBeforeUpdate = professionalDegreeRepository.findAll().size();

        // Create the ProfessionalDegree
        ProfessionalDegreeDTO professionalDegreeDTO = professionalDegreeMapper.toDto(professionalDegree);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalDegreeMockMvc.perform(put("/api/professional-degrees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalDegreeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalDegree in the database
        List<ProfessionalDegree> professionalDegreeList = professionalDegreeRepository.findAll();
        assertThat(professionalDegreeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProfessionalDegree in Elasticsearch
        verify(mockProfessionalDegreeSearchRepository, times(0)).save(professionalDegree);
    }

    @Test
    @Transactional
    public void deleteProfessionalDegree() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);

        int databaseSizeBeforeDelete = professionalDegreeRepository.findAll().size();

        // Get the professionalDegree
        restProfessionalDegreeMockMvc.perform(delete("/api/professional-degrees/{id}", professionalDegree.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProfessionalDegree> professionalDegreeList = professionalDegreeRepository.findAll();
        assertThat(professionalDegreeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProfessionalDegree in Elasticsearch
        verify(mockProfessionalDegreeSearchRepository, times(1)).deleteById(professionalDegree.getId());
    }

    @Test
    @Transactional
    public void searchProfessionalDegree() throws Exception {
        // Initialize the database
        professionalDegreeRepository.saveAndFlush(professionalDegree);
        when(mockProfessionalDegreeSearchRepository.search(queryStringQuery("id:" + professionalDegree.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(professionalDegree), PageRequest.of(0, 1), 1));
        // Search the professionalDegree
        restProfessionalDegreeMockMvc.perform(get("/api/_search/professional-degrees?query=id:" + professionalDegree.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalDegree.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].institute").value(hasItem(DEFAULT_INSTITUTE)))
            .andExpect(jsonPath("$.[*].passingYear").value(hasItem(DEFAULT_PASSING_YEAR)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalDegree.class);
        ProfessionalDegree professionalDegree1 = new ProfessionalDegree();
        professionalDegree1.setId(1L);
        ProfessionalDegree professionalDegree2 = new ProfessionalDegree();
        professionalDegree2.setId(professionalDegree1.getId());
        assertThat(professionalDegree1).isEqualTo(professionalDegree2);
        professionalDegree2.setId(2L);
        assertThat(professionalDegree1).isNotEqualTo(professionalDegree2);
        professionalDegree1.setId(null);
        assertThat(professionalDegree1).isNotEqualTo(professionalDegree2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalDegreeDTO.class);
        ProfessionalDegreeDTO professionalDegreeDTO1 = new ProfessionalDegreeDTO();
        professionalDegreeDTO1.setId(1L);
        ProfessionalDegreeDTO professionalDegreeDTO2 = new ProfessionalDegreeDTO();
        assertThat(professionalDegreeDTO1).isNotEqualTo(professionalDegreeDTO2);
        professionalDegreeDTO2.setId(professionalDegreeDTO1.getId());
        assertThat(professionalDegreeDTO1).isEqualTo(professionalDegreeDTO2);
        professionalDegreeDTO2.setId(2L);
        assertThat(professionalDegreeDTO1).isNotEqualTo(professionalDegreeDTO2);
        professionalDegreeDTO1.setId(null);
        assertThat(professionalDegreeDTO1).isNotEqualTo(professionalDegreeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(professionalDegreeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(professionalDegreeMapper.fromId(null)).isNull();
    }
}
