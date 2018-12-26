package com.dhomoni.search.web.rest;

import com.dhomoni.search.SearchApp;

import com.dhomoni.search.config.SecurityBeanOverrideConfiguration;

import com.dhomoni.search.domain.Doctor;
import com.dhomoni.search.domain.MedicalDepartment;
import com.dhomoni.search.domain.Chamber;
import com.dhomoni.search.domain.ProfessionalDegree;
import com.dhomoni.search.repository.DoctorRepository;
import com.dhomoni.search.repository.search.DoctorSearchRepository;
import com.dhomoni.search.service.DoctorService;
import com.dhomoni.search.service.dto.DoctorDTO;
import com.dhomoni.search.service.mapper.DoctorMapper;
import com.dhomoni.search.web.rest.errors.ExceptionTranslator;
import com.dhomoni.search.service.dto.DoctorCriteria;
import com.dhomoni.search.service.DoctorQueryService;

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
import org.springframework.util.Base64Utils;
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

import com.dhomoni.search.domain.enumeration.DoctorType;
/**
 * Test class for the DoctorResource REST controller.
 *
 * @see DoctorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SearchApp.class})
public class DoctorResourceIntTest {

    private static final String DEFAULT_LICENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LICENCE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final DoctorType DEFAULT_TYPE = DoctorType.PHYSICIAN;
    private static final DoctorType UPDATED_TYPE = DoctorType.SURGEON;

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private DoctorService doctorService;

    /**
     * This repository is mocked in the com.dhomoni.search.repository.search test package.
     *
     * @see com.dhomoni.search.repository.search.DoctorSearchRepositoryMockConfiguration
     */
    @Autowired
    private DoctorSearchRepository mockDoctorSearchRepository;

    @Autowired
    private DoctorQueryService doctorQueryService;

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

    private MockMvc restDoctorMockMvc;

    private Doctor doctor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoctorResource doctorResource = new DoctorResource(doctorService, doctorQueryService);
        this.restDoctorMockMvc = MockMvcBuilders.standaloneSetup(doctorResource)
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
    public static Doctor createEntity(EntityManager em) {
        Doctor doctor = new Doctor()
            .licenceNumber(DEFAULT_LICENCE_NUMBER)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .type(DEFAULT_TYPE)
            .designation(DEFAULT_DESIGNATION)
            .description(DEFAULT_DESCRIPTION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .activated(DEFAULT_ACTIVATED);
        return doctor;
    }

    @Before
    public void initTest() {
        doctor = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctor() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);
        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isCreated());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate + 1);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getLicenceNumber()).isEqualTo(DEFAULT_LICENCE_NUMBER);
        assertThat(testDoctor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testDoctor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testDoctor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testDoctor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDoctor.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDoctor.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testDoctor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDoctor.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testDoctor.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testDoctor.isActivated()).isEqualTo(DEFAULT_ACTIVATED);

        // Validate the Doctor in Elasticsearch
        verify(mockDoctorSearchRepository, times(1)).save(testDoctor);
    }

    @Test
    @Transactional
    public void createDoctorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // Create the Doctor with an existing ID
        doctor.setId(1L);
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate);

        // Validate the Doctor in Elasticsearch
        verify(mockDoctorSearchRepository, times(0)).save(doctor);
    }

    @Test
    @Transactional
    public void checkLicenceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setLicenceNumber(null);

        // Create the Doctor, which fails.
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDoctors() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList
        restDoctorMockMvc.perform(get("/api/doctors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].licenceNumber").value(hasItem(DEFAULT_LICENCE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doctor.getId().intValue()))
            .andExpect(jsonPath("$.licenceNumber").value(DEFAULT_LICENCE_NUMBER.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllDoctorsByLicenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where licenceNumber equals to DEFAULT_LICENCE_NUMBER
        defaultDoctorShouldBeFound("licenceNumber.equals=" + DEFAULT_LICENCE_NUMBER);

        // Get all the doctorList where licenceNumber equals to UPDATED_LICENCE_NUMBER
        defaultDoctorShouldNotBeFound("licenceNumber.equals=" + UPDATED_LICENCE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDoctorsByLicenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where licenceNumber in DEFAULT_LICENCE_NUMBER or UPDATED_LICENCE_NUMBER
        defaultDoctorShouldBeFound("licenceNumber.in=" + DEFAULT_LICENCE_NUMBER + "," + UPDATED_LICENCE_NUMBER);

        // Get all the doctorList where licenceNumber equals to UPDATED_LICENCE_NUMBER
        defaultDoctorShouldNotBeFound("licenceNumber.in=" + UPDATED_LICENCE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDoctorsByLicenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where licenceNumber is not null
        defaultDoctorShouldBeFound("licenceNumber.specified=true");

        // Get all the doctorList where licenceNumber is null
        defaultDoctorShouldNotBeFound("licenceNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoctorsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName equals to DEFAULT_FIRST_NAME
        defaultDoctorShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the doctorList where firstName equals to UPDATED_FIRST_NAME
        defaultDoctorShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultDoctorShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the doctorList where firstName equals to UPDATED_FIRST_NAME
        defaultDoctorShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName is not null
        defaultDoctorShouldBeFound("firstName.specified=true");

        // Get all the doctorList where firstName is null
        defaultDoctorShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoctorsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName equals to DEFAULT_LAST_NAME
        defaultDoctorShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the doctorList where lastName equals to UPDATED_LAST_NAME
        defaultDoctorShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultDoctorShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the doctorList where lastName equals to UPDATED_LAST_NAME
        defaultDoctorShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName is not null
        defaultDoctorShouldBeFound("lastName.specified=true");

        // Get all the doctorList where lastName is null
        defaultDoctorShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoctorsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone equals to DEFAULT_PHONE
        defaultDoctorShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the doctorList where phone equals to UPDATED_PHONE
        defaultDoctorShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllDoctorsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultDoctorShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the doctorList where phone equals to UPDATED_PHONE
        defaultDoctorShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllDoctorsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone is not null
        defaultDoctorShouldBeFound("phone.specified=true");

        // Get all the doctorList where phone is null
        defaultDoctorShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoctorsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where email equals to DEFAULT_EMAIL
        defaultDoctorShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the doctorList where email equals to UPDATED_EMAIL
        defaultDoctorShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllDoctorsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultDoctorShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the doctorList where email equals to UPDATED_EMAIL
        defaultDoctorShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllDoctorsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where email is not null
        defaultDoctorShouldBeFound("email.specified=true");

        // Get all the doctorList where email is null
        defaultDoctorShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoctorsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where type equals to DEFAULT_TYPE
        defaultDoctorShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the doctorList where type equals to UPDATED_TYPE
        defaultDoctorShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDoctorsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultDoctorShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the doctorList where type equals to UPDATED_TYPE
        defaultDoctorShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDoctorsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where type is not null
        defaultDoctorShouldBeFound("type.specified=true");

        // Get all the doctorList where type is null
        defaultDoctorShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoctorsByDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where designation equals to DEFAULT_DESIGNATION
        defaultDoctorShouldBeFound("designation.equals=" + DEFAULT_DESIGNATION);

        // Get all the doctorList where designation equals to UPDATED_DESIGNATION
        defaultDoctorShouldNotBeFound("designation.equals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    public void getAllDoctorsByDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where designation in DEFAULT_DESIGNATION or UPDATED_DESIGNATION
        defaultDoctorShouldBeFound("designation.in=" + DEFAULT_DESIGNATION + "," + UPDATED_DESIGNATION);

        // Get all the doctorList where designation equals to UPDATED_DESIGNATION
        defaultDoctorShouldNotBeFound("designation.in=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    public void getAllDoctorsByDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where designation is not null
        defaultDoctorShouldBeFound("designation.specified=true");

        // Get all the doctorList where designation is null
        defaultDoctorShouldNotBeFound("designation.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoctorsByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where activated equals to DEFAULT_ACTIVATED
        defaultDoctorShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the doctorList where activated equals to UPDATED_ACTIVATED
        defaultDoctorShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void getAllDoctorsByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultDoctorShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the doctorList where activated equals to UPDATED_ACTIVATED
        defaultDoctorShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void getAllDoctorsByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where activated is not null
        defaultDoctorShouldBeFound("activated.specified=true");

        // Get all the doctorList where activated is null
        defaultDoctorShouldNotBeFound("activated.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoctorsByMedicalDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        MedicalDepartment medicalDepartment = MedicalDepartmentResourceIntTest.createEntity(em);
        em.persist(medicalDepartment);
        em.flush();
        doctor.setMedicalDepartment(medicalDepartment);
        doctorRepository.saveAndFlush(doctor);
        Long medicalDepartmentId = medicalDepartment.getId();

        // Get all the doctorList where medicalDepartment equals to medicalDepartmentId
        defaultDoctorShouldBeFound("medicalDepartmentId.equals=" + medicalDepartmentId);

        // Get all the doctorList where medicalDepartment equals to medicalDepartmentId + 1
        defaultDoctorShouldNotBeFound("medicalDepartmentId.equals=" + (medicalDepartmentId + 1));
    }


    @Test
    @Transactional
    public void getAllDoctorsByChambersIsEqualToSomething() throws Exception {
        // Initialize the database
        Chamber chambers = ChamberResourceIntTest.createEntity(em);
        em.persist(chambers);
        em.flush();
        doctor.addChambers(chambers);
        doctorRepository.saveAndFlush(doctor);
        Long chambersId = chambers.getId();

        // Get all the doctorList where chambers equals to chambersId
        defaultDoctorShouldBeFound("chambersId.equals=" + chambersId);

        // Get all the doctorList where chambers equals to chambersId + 1
        defaultDoctorShouldNotBeFound("chambersId.equals=" + (chambersId + 1));
    }


    @Test
    @Transactional
    public void getAllDoctorsByProfessionalDegreesIsEqualToSomething() throws Exception {
        // Initialize the database
        ProfessionalDegree professionalDegrees = ProfessionalDegreeResourceIntTest.createEntity(em);
        em.persist(professionalDegrees);
        em.flush();
        doctor.addProfessionalDegrees(professionalDegrees);
        doctorRepository.saveAndFlush(doctor);
        Long professionalDegreesId = professionalDegrees.getId();

        // Get all the doctorList where professionalDegrees equals to professionalDegreesId
        defaultDoctorShouldBeFound("professionalDegreesId.equals=" + professionalDegreesId);

        // Get all the doctorList where professionalDegrees equals to professionalDegreesId + 1
        defaultDoctorShouldNotBeFound("professionalDegreesId.equals=" + (professionalDegreesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDoctorShouldBeFound(String filter) throws Exception {
        restDoctorMockMvc.perform(get("/api/doctors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].licenceNumber").value(hasItem(DEFAULT_LICENCE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())));

        // Check, that the count call also returns 1
        restDoctorMockMvc.perform(get("/api/doctors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDoctorShouldNotBeFound(String filter) throws Exception {
        restDoctorMockMvc.perform(get("/api/doctors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDoctorMockMvc.perform(get("/api/doctors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDoctor() throws Exception {
        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor
        Doctor updatedDoctor = doctorRepository.findById(doctor.getId()).get();
        // Disconnect from session so that the updates on updatedDoctor are not directly saved in db
        em.detach(updatedDoctor);
        updatedDoctor
            .licenceNumber(UPDATED_LICENCE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .designation(UPDATED_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .activated(UPDATED_ACTIVATED);
        DoctorDTO doctorDTO = doctorMapper.toDto(updatedDoctor);

        restDoctorMockMvc.perform(put("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getLicenceNumber()).isEqualTo(UPDATED_LICENCE_NUMBER);
        assertThat(testDoctor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testDoctor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testDoctor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testDoctor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDoctor.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDoctor.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testDoctor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDoctor.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testDoctor.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testDoctor.isActivated()).isEqualTo(UPDATED_ACTIVATED);

        // Validate the Doctor in Elasticsearch
        verify(mockDoctorSearchRepository, times(1)).save(testDoctor);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorMockMvc.perform(put("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Doctor in Elasticsearch
        verify(mockDoctorSearchRepository, times(0)).save(doctor);
    }

    @Test
    @Transactional
    public void deleteDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeDelete = doctorRepository.findAll().size();

        // Get the doctor
        restDoctorMockMvc.perform(delete("/api/doctors/{id}", doctor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Doctor in Elasticsearch
        verify(mockDoctorSearchRepository, times(1)).deleteById(doctor.getId());
    }

    @Test
    @Transactional
    public void searchDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);
        when(mockDoctorSearchRepository.search(queryStringQuery("id:" + doctor.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(doctor), PageRequest.of(0, 1), 1));
        // Search the doctor
        restDoctorMockMvc.perform(get("/api/_search/doctors?query=id:" + doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].licenceNumber").value(hasItem(DEFAULT_LICENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Doctor.class);
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        Doctor doctor2 = new Doctor();
        doctor2.setId(doctor1.getId());
        assertThat(doctor1).isEqualTo(doctor2);
        doctor2.setId(2L);
        assertThat(doctor1).isNotEqualTo(doctor2);
        doctor1.setId(null);
        assertThat(doctor1).isNotEqualTo(doctor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorDTO.class);
        DoctorDTO doctorDTO1 = new DoctorDTO();
        doctorDTO1.setId(1L);
        DoctorDTO doctorDTO2 = new DoctorDTO();
        assertThat(doctorDTO1).isNotEqualTo(doctorDTO2);
        doctorDTO2.setId(doctorDTO1.getId());
        assertThat(doctorDTO1).isEqualTo(doctorDTO2);
        doctorDTO2.setId(2L);
        assertThat(doctorDTO1).isNotEqualTo(doctorDTO2);
        doctorDTO1.setId(null);
        assertThat(doctorDTO1).isNotEqualTo(doctorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(doctorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(doctorMapper.fromId(null)).isNull();
    }
}
