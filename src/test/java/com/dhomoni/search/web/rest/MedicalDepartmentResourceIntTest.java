package com.dhomoni.search.web.rest;

import static com.dhomoni.search.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import com.dhomoni.search.SearchApp;
import com.dhomoni.search.config.SecurityBeanOverrideConfiguration;
import com.dhomoni.search.domain.Disease;
import com.dhomoni.search.domain.MedicalDepartment;
import com.dhomoni.search.repository.MedicalDepartmentRepository;
import com.dhomoni.search.service.MedicalDepartmentQueryService;
import com.dhomoni.search.service.MedicalDepartmentService;
import com.dhomoni.search.service.dto.MedicalDepartmentDTO;
import com.dhomoni.search.service.mapper.MedicalDepartmentMapper;
import com.dhomoni.search.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the MedicalDepartmentResource REST controller.
 *
 * @see MedicalDepartmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SearchApp.class})
public class MedicalDepartmentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private MedicalDepartmentRepository medicalDepartmentRepository;

    @Autowired
    private MedicalDepartmentMapper medicalDepartmentMapper;

    @Autowired
    private MedicalDepartmentService medicalDepartmentService;

    @Autowired
    private MedicalDepartmentQueryService medicalDepartmentQueryService;

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

    private MockMvc restMedicalDepartmentMockMvc;

    private MedicalDepartment medicalDepartment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MedicalDepartmentResource medicalDepartmentResource = new MedicalDepartmentResource(medicalDepartmentService, medicalDepartmentQueryService);
        this.restMedicalDepartmentMockMvc = MockMvcBuilders.standaloneSetup(medicalDepartmentResource)
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
    public static MedicalDepartment createEntity(EntityManager em) {
        MedicalDepartment medicalDepartment = new MedicalDepartment()
            .name(DEFAULT_NAME);
        return medicalDepartment;
    }

    @Before
    public void initTest() {
        medicalDepartment = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedicalDepartment() throws Exception {
        int databaseSizeBeforeCreate = medicalDepartmentRepository.findAll().size();

        // Create the MedicalDepartment
        MedicalDepartmentDTO medicalDepartmentDTO = medicalDepartmentMapper.toDto(medicalDepartment);
        restMedicalDepartmentMockMvc.perform(post("/api/medical-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalDepartmentDTO)))
            .andExpect(status().isCreated());

        // Validate the MedicalDepartment in the database
        List<MedicalDepartment> medicalDepartmentList = medicalDepartmentRepository.findAll();
        assertThat(medicalDepartmentList).hasSize(databaseSizeBeforeCreate + 1);
        MedicalDepartment testMedicalDepartment = medicalDepartmentList.get(medicalDepartmentList.size() - 1);
        assertThat(testMedicalDepartment.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createMedicalDepartmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicalDepartmentRepository.findAll().size();

        // Create the MedicalDepartment with an existing ID
        medicalDepartment.setId(1L);
        MedicalDepartmentDTO medicalDepartmentDTO = medicalDepartmentMapper.toDto(medicalDepartment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalDepartmentMockMvc.perform(post("/api/medical-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalDepartmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalDepartment in the database
        List<MedicalDepartment> medicalDepartmentList = medicalDepartmentRepository.findAll();
        assertThat(medicalDepartmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicalDepartmentRepository.findAll().size();
        // set the field null
        medicalDepartment.setName(null);

        // Create the MedicalDepartment, which fails.
        MedicalDepartmentDTO medicalDepartmentDTO = medicalDepartmentMapper.toDto(medicalDepartment);

        restMedicalDepartmentMockMvc.perform(post("/api/medical-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalDepartmentDTO)))
            .andExpect(status().isBadRequest());

        List<MedicalDepartment> medicalDepartmentList = medicalDepartmentRepository.findAll();
        assertThat(medicalDepartmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedicalDepartments() throws Exception {
        // Initialize the database
        medicalDepartmentRepository.saveAndFlush(medicalDepartment);

        // Get all the medicalDepartmentList
        restMedicalDepartmentMockMvc.perform(get("/api/medical-departments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalDepartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getMedicalDepartment() throws Exception {
        // Initialize the database
        medicalDepartmentRepository.saveAndFlush(medicalDepartment);

        // Get the medicalDepartment
        restMedicalDepartmentMockMvc.perform(get("/api/medical-departments/{id}", medicalDepartment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medicalDepartment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllMedicalDepartmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        medicalDepartmentRepository.saveAndFlush(medicalDepartment);

        // Get all the medicalDepartmentList where name equals to DEFAULT_NAME
        defaultMedicalDepartmentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the medicalDepartmentList where name equals to UPDATED_NAME
        defaultMedicalDepartmentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMedicalDepartmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        medicalDepartmentRepository.saveAndFlush(medicalDepartment);

        // Get all the medicalDepartmentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMedicalDepartmentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the medicalDepartmentList where name equals to UPDATED_NAME
        defaultMedicalDepartmentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMedicalDepartmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicalDepartmentRepository.saveAndFlush(medicalDepartment);

        // Get all the medicalDepartmentList where name is not null
        defaultMedicalDepartmentShouldBeFound("name.specified=true");

        // Get all the medicalDepartmentList where name is null
        defaultMedicalDepartmentShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedicalDepartmentsByDiseasesIsEqualToSomething() throws Exception {
        // Initialize the database
        Disease diseases = DiseaseResourceIntTest.createEntity(em);
        em.persist(diseases);
        em.flush();
        medicalDepartment.addDiseases(diseases);
        medicalDepartmentRepository.saveAndFlush(medicalDepartment);
        Long diseasesId = diseases.getId();

        // Get all the medicalDepartmentList where diseases equals to diseasesId
        defaultMedicalDepartmentShouldBeFound("diseasesId.equals=" + diseasesId);

        // Get all the medicalDepartmentList where diseases equals to diseasesId + 1
        defaultMedicalDepartmentShouldNotBeFound("diseasesId.equals=" + (diseasesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMedicalDepartmentShouldBeFound(String filter) throws Exception {
        restMedicalDepartmentMockMvc.perform(get("/api/medical-departments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalDepartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));

        // Check, that the count call also returns 1
        restMedicalDepartmentMockMvc.perform(get("/api/medical-departments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
//            .andExpect(content().string("1")); // In case of preloaded data this test causing problem
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMedicalDepartmentShouldNotBeFound(String filter) throws Exception {
        restMedicalDepartmentMockMvc.perform(get("/api/medical-departments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicalDepartmentMockMvc.perform(get("/api/medical-departments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMedicalDepartment() throws Exception {
        // Get the medicalDepartment
        restMedicalDepartmentMockMvc.perform(get("/api/medical-departments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicalDepartment() throws Exception {
        // Initialize the database
        medicalDepartmentRepository.saveAndFlush(medicalDepartment);

        int databaseSizeBeforeUpdate = medicalDepartmentRepository.findAll().size();

        // Update the medicalDepartment
        MedicalDepartment updatedMedicalDepartment = medicalDepartmentRepository.findById(medicalDepartment.getId()).get();
        // Disconnect from session so that the updates on updatedMedicalDepartment are not directly saved in db
        em.detach(updatedMedicalDepartment);
        updatedMedicalDepartment
            .name(UPDATED_NAME);
        MedicalDepartmentDTO medicalDepartmentDTO = medicalDepartmentMapper.toDto(updatedMedicalDepartment);

        restMedicalDepartmentMockMvc.perform(put("/api/medical-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalDepartmentDTO)))
            .andExpect(status().isOk());

        // Validate the MedicalDepartment in the database
        List<MedicalDepartment> medicalDepartmentList = medicalDepartmentRepository.findAll();
        assertThat(medicalDepartmentList).hasSize(databaseSizeBeforeUpdate);
        MedicalDepartment testMedicalDepartment = medicalDepartmentList.get(medicalDepartmentList.size() - 1);
        assertThat(testMedicalDepartment.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingMedicalDepartment() throws Exception {
        int databaseSizeBeforeUpdate = medicalDepartmentRepository.findAll().size();

        // Create the MedicalDepartment
        MedicalDepartmentDTO medicalDepartmentDTO = medicalDepartmentMapper.toDto(medicalDepartment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalDepartmentMockMvc.perform(put("/api/medical-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalDepartmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalDepartment in the database
        List<MedicalDepartment> medicalDepartmentList = medicalDepartmentRepository.findAll();
        assertThat(medicalDepartmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMedicalDepartment() throws Exception {
        // Initialize the database
        medicalDepartmentRepository.saveAndFlush(medicalDepartment);

        int databaseSizeBeforeDelete = medicalDepartmentRepository.findAll().size();

        // Get the medicalDepartment
        restMedicalDepartmentMockMvc.perform(delete("/api/medical-departments/{id}", medicalDepartment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MedicalDepartment> medicalDepartmentList = medicalDepartmentRepository.findAll();
        assertThat(medicalDepartmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalDepartment.class);
        MedicalDepartment medicalDepartment1 = new MedicalDepartment();
        medicalDepartment1.setId(1L);
        MedicalDepartment medicalDepartment2 = new MedicalDepartment();
        medicalDepartment2.setId(medicalDepartment1.getId());
        assertThat(medicalDepartment1).isEqualTo(medicalDepartment2);
        medicalDepartment2.setId(2L);
        assertThat(medicalDepartment1).isNotEqualTo(medicalDepartment2);
        medicalDepartment1.setId(null);
        assertThat(medicalDepartment1).isNotEqualTo(medicalDepartment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalDepartmentDTO.class);
        MedicalDepartmentDTO medicalDepartmentDTO1 = new MedicalDepartmentDTO();
        medicalDepartmentDTO1.setId(1L);
        MedicalDepartmentDTO medicalDepartmentDTO2 = new MedicalDepartmentDTO();
        assertThat(medicalDepartmentDTO1).isNotEqualTo(medicalDepartmentDTO2);
        medicalDepartmentDTO2.setId(medicalDepartmentDTO1.getId());
        assertThat(medicalDepartmentDTO1).isEqualTo(medicalDepartmentDTO2);
        medicalDepartmentDTO2.setId(2L);
        assertThat(medicalDepartmentDTO1).isNotEqualTo(medicalDepartmentDTO2);
        medicalDepartmentDTO1.setId(null);
        assertThat(medicalDepartmentDTO1).isNotEqualTo(medicalDepartmentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(medicalDepartmentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(medicalDepartmentMapper.fromId(null)).isNull();
    }
}
