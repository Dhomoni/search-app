package com.dhomoni.search.web.rest;

import com.dhomoni.search.SearchApp;

import com.dhomoni.search.config.SecurityBeanOverrideConfiguration;

import com.dhomoni.search.domain.Medicine;
import com.dhomoni.search.repository.MedicineRepository;
import com.dhomoni.search.repository.search.MedicineSearchRepository;
import com.dhomoni.search.service.MedicineService;
import com.dhomoni.search.service.dto.MedicineDTO;
import com.dhomoni.search.service.mapper.MedicineMapper;
import com.dhomoni.search.web.rest.errors.ExceptionTranslator;
import com.dhomoni.search.service.dto.MedicineCriteria;
import com.dhomoni.search.service.MedicineQueryService;

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

import com.dhomoni.search.domain.enumeration.MedicineType;
/**
 * Test class for the MedicineResource REST controller.
 *
 * @see MedicineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SearchApp.class})
public class MedicineResourceIntTest {

    private static final String DEFAULT_TRADE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_QUANTITY = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_QUANTITY = "BBBBBBBBBB";

    private static final String DEFAULT_GENERIC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GENERIC_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CHEMICAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHEMICAL_NAME = "BBBBBBBBBB";

    private static final MedicineType DEFAULT_TYPE = MedicineType.TABLET;
    private static final MedicineType UPDATED_TYPE = MedicineType.CAPSULE;

    private static final String DEFAULT_MANUFACTURER = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER = "BBBBBBBBBB";

    private static final Double DEFAULT_MRP = 1D;
    private static final Double UPDATED_MRP = 2D;

    private static final String DEFAULT_INDICATIONS = "AAAAAAAAAA";
    private static final String UPDATED_INDICATIONS = "BBBBBBBBBB";

    private static final String DEFAULT_DOSE_AND_ADMIN = "AAAAAAAAAA";
    private static final String UPDATED_DOSE_AND_ADMIN = "BBBBBBBBBB";

    private static final String DEFAULT_PREPARATION = "AAAAAAAAAA";
    private static final String UPDATED_PREPARATION = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_URL = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedicineMapper medicineMapper;

    @Autowired
    private MedicineService medicineService;

    /**
     * This repository is mocked in the com.dhomoni.search.repository.search test package.
     *
     * @see com.dhomoni.search.repository.search.MedicineSearchRepositoryMockConfiguration
     */
    @Autowired
    private MedicineSearchRepository mockMedicineSearchRepository;

    @Autowired
    private MedicineQueryService medicineQueryService;

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

    private MockMvc restMedicineMockMvc;

    private Medicine medicine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MedicineResource medicineResource = new MedicineResource(medicineService, medicineQueryService);
        this.restMedicineMockMvc = MockMvcBuilders.standaloneSetup(medicineResource)
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
    public static Medicine createEntity(EntityManager em) {
        Medicine medicine = new Medicine()
            .tradeName(DEFAULT_TRADE_NAME)
            .unitQuantity(DEFAULT_UNIT_QUANTITY)
            .genericName(DEFAULT_GENERIC_NAME)
            .chemicalName(DEFAULT_CHEMICAL_NAME)
            .type(DEFAULT_TYPE)
            .manufacturer(DEFAULT_MANUFACTURER)
            .mrp(DEFAULT_MRP)
            .indications(DEFAULT_INDICATIONS)
            .doseAndAdmin(DEFAULT_DOSE_AND_ADMIN)
            .preparation(DEFAULT_PREPARATION)
            .productUrl(DEFAULT_PRODUCT_URL)
            .active(DEFAULT_ACTIVE);
        return medicine;
    }

    @Before
    public void initTest() {
        medicine = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedicine() throws Exception {
        int databaseSizeBeforeCreate = medicineRepository.findAll().size();

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);
        restMedicineMockMvc.perform(post("/api/medicines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isCreated());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeCreate + 1);
        Medicine testMedicine = medicineList.get(medicineList.size() - 1);
        assertThat(testMedicine.getTradeName()).isEqualTo(DEFAULT_TRADE_NAME);
        assertThat(testMedicine.getUnitQuantity()).isEqualTo(DEFAULT_UNIT_QUANTITY);
        assertThat(testMedicine.getGenericName()).isEqualTo(DEFAULT_GENERIC_NAME);
        assertThat(testMedicine.getChemicalName()).isEqualTo(DEFAULT_CHEMICAL_NAME);
        assertThat(testMedicine.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMedicine.getManufacturer()).isEqualTo(DEFAULT_MANUFACTURER);
        assertThat(testMedicine.getMrp()).isEqualTo(DEFAULT_MRP);
        assertThat(testMedicine.getIndications()).isEqualTo(DEFAULT_INDICATIONS);
        assertThat(testMedicine.getDoseAndAdmin()).isEqualTo(DEFAULT_DOSE_AND_ADMIN);
        assertThat(testMedicine.getPreparation()).isEqualTo(DEFAULT_PREPARATION);
        assertThat(testMedicine.getProductUrl()).isEqualTo(DEFAULT_PRODUCT_URL);
        assertThat(testMedicine.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Medicine in Elasticsearch
        verify(mockMedicineSearchRepository, times(1)).save(testMedicine);
    }

    @Test
    @Transactional
    public void createMedicineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicineRepository.findAll().size();

        // Create the Medicine with an existing ID
        medicine.setId(1L);
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicineMockMvc.perform(post("/api/medicines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeCreate);

        // Validate the Medicine in Elasticsearch
        verify(mockMedicineSearchRepository, times(0)).save(medicine);
    }

    @Test
    @Transactional
    public void checkTradeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicineRepository.findAll().size();
        // set the field null
        medicine.setTradeName(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc.perform(post("/api/medicines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenericNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicineRepository.findAll().size();
        // set the field null
        medicine.setGenericName(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc.perform(post("/api/medicines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChemicalNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicineRepository.findAll().size();
        // set the field null
        medicine.setChemicalName(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc.perform(post("/api/medicines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkManufacturerIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicineRepository.findAll().size();
        // set the field null
        medicine.setManufacturer(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc.perform(post("/api/medicines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedicines() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList
        restMedicineMockMvc.perform(get("/api/medicines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicine.getId().intValue())))
            .andExpect(jsonPath("$.[*].tradeName").value(hasItem(DEFAULT_TRADE_NAME.toString())))
            .andExpect(jsonPath("$.[*].unitQuantity").value(hasItem(DEFAULT_UNIT_QUANTITY.toString())))
            .andExpect(jsonPath("$.[*].genericName").value(hasItem(DEFAULT_GENERIC_NAME.toString())))
            .andExpect(jsonPath("$.[*].chemicalName").value(hasItem(DEFAULT_CHEMICAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER.toString())))
            .andExpect(jsonPath("$.[*].mrp").value(hasItem(DEFAULT_MRP.doubleValue())))
            .andExpect(jsonPath("$.[*].indications").value(hasItem(DEFAULT_INDICATIONS.toString())))
            .andExpect(jsonPath("$.[*].doseAndAdmin").value(hasItem(DEFAULT_DOSE_AND_ADMIN.toString())))
            .andExpect(jsonPath("$.[*].preparation").value(hasItem(DEFAULT_PREPARATION.toString())))
            .andExpect(jsonPath("$.[*].productUrl").value(hasItem(DEFAULT_PRODUCT_URL.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getMedicine() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get the medicine
        restMedicineMockMvc.perform(get("/api/medicines/{id}", medicine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medicine.getId().intValue()))
            .andExpect(jsonPath("$.tradeName").value(DEFAULT_TRADE_NAME.toString()))
            .andExpect(jsonPath("$.unitQuantity").value(DEFAULT_UNIT_QUANTITY.toString()))
            .andExpect(jsonPath("$.genericName").value(DEFAULT_GENERIC_NAME.toString()))
            .andExpect(jsonPath("$.chemicalName").value(DEFAULT_CHEMICAL_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.manufacturer").value(DEFAULT_MANUFACTURER.toString()))
            .andExpect(jsonPath("$.mrp").value(DEFAULT_MRP.doubleValue()))
            .andExpect(jsonPath("$.indications").value(DEFAULT_INDICATIONS.toString()))
            .andExpect(jsonPath("$.doseAndAdmin").value(DEFAULT_DOSE_AND_ADMIN.toString()))
            .andExpect(jsonPath("$.preparation").value(DEFAULT_PREPARATION.toString()))
            .andExpect(jsonPath("$.productUrl").value(DEFAULT_PRODUCT_URL.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllMedicinesByTradeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where tradeName equals to DEFAULT_TRADE_NAME
        defaultMedicineShouldBeFound("tradeName.equals=" + DEFAULT_TRADE_NAME);

        // Get all the medicineList where tradeName equals to UPDATED_TRADE_NAME
        defaultMedicineShouldNotBeFound("tradeName.equals=" + UPDATED_TRADE_NAME);
    }

    @Test
    @Transactional
    public void getAllMedicinesByTradeNameIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where tradeName in DEFAULT_TRADE_NAME or UPDATED_TRADE_NAME
        defaultMedicineShouldBeFound("tradeName.in=" + DEFAULT_TRADE_NAME + "," + UPDATED_TRADE_NAME);

        // Get all the medicineList where tradeName equals to UPDATED_TRADE_NAME
        defaultMedicineShouldNotBeFound("tradeName.in=" + UPDATED_TRADE_NAME);
    }

    @Test
    @Transactional
    public void getAllMedicinesByTradeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where tradeName is not null
        defaultMedicineShouldBeFound("tradeName.specified=true");

        // Get all the medicineList where tradeName is null
        defaultMedicineShouldNotBeFound("tradeName.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedicinesByUnitQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where unitQuantity equals to DEFAULT_UNIT_QUANTITY
        defaultMedicineShouldBeFound("unitQuantity.equals=" + DEFAULT_UNIT_QUANTITY);

        // Get all the medicineList where unitQuantity equals to UPDATED_UNIT_QUANTITY
        defaultMedicineShouldNotBeFound("unitQuantity.equals=" + UPDATED_UNIT_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllMedicinesByUnitQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where unitQuantity in DEFAULT_UNIT_QUANTITY or UPDATED_UNIT_QUANTITY
        defaultMedicineShouldBeFound("unitQuantity.in=" + DEFAULT_UNIT_QUANTITY + "," + UPDATED_UNIT_QUANTITY);

        // Get all the medicineList where unitQuantity equals to UPDATED_UNIT_QUANTITY
        defaultMedicineShouldNotBeFound("unitQuantity.in=" + UPDATED_UNIT_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllMedicinesByUnitQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where unitQuantity is not null
        defaultMedicineShouldBeFound("unitQuantity.specified=true");

        // Get all the medicineList where unitQuantity is null
        defaultMedicineShouldNotBeFound("unitQuantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedicinesByGenericNameIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where genericName equals to DEFAULT_GENERIC_NAME
        defaultMedicineShouldBeFound("genericName.equals=" + DEFAULT_GENERIC_NAME);

        // Get all the medicineList where genericName equals to UPDATED_GENERIC_NAME
        defaultMedicineShouldNotBeFound("genericName.equals=" + UPDATED_GENERIC_NAME);
    }

    @Test
    @Transactional
    public void getAllMedicinesByGenericNameIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where genericName in DEFAULT_GENERIC_NAME or UPDATED_GENERIC_NAME
        defaultMedicineShouldBeFound("genericName.in=" + DEFAULT_GENERIC_NAME + "," + UPDATED_GENERIC_NAME);

        // Get all the medicineList where genericName equals to UPDATED_GENERIC_NAME
        defaultMedicineShouldNotBeFound("genericName.in=" + UPDATED_GENERIC_NAME);
    }

    @Test
    @Transactional
    public void getAllMedicinesByGenericNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where genericName is not null
        defaultMedicineShouldBeFound("genericName.specified=true");

        // Get all the medicineList where genericName is null
        defaultMedicineShouldNotBeFound("genericName.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedicinesByChemicalNameIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where chemicalName equals to DEFAULT_CHEMICAL_NAME
        defaultMedicineShouldBeFound("chemicalName.equals=" + DEFAULT_CHEMICAL_NAME);

        // Get all the medicineList where chemicalName equals to UPDATED_CHEMICAL_NAME
        defaultMedicineShouldNotBeFound("chemicalName.equals=" + UPDATED_CHEMICAL_NAME);
    }

    @Test
    @Transactional
    public void getAllMedicinesByChemicalNameIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where chemicalName in DEFAULT_CHEMICAL_NAME or UPDATED_CHEMICAL_NAME
        defaultMedicineShouldBeFound("chemicalName.in=" + DEFAULT_CHEMICAL_NAME + "," + UPDATED_CHEMICAL_NAME);

        // Get all the medicineList where chemicalName equals to UPDATED_CHEMICAL_NAME
        defaultMedicineShouldNotBeFound("chemicalName.in=" + UPDATED_CHEMICAL_NAME);
    }

    @Test
    @Transactional
    public void getAllMedicinesByChemicalNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where chemicalName is not null
        defaultMedicineShouldBeFound("chemicalName.specified=true");

        // Get all the medicineList where chemicalName is null
        defaultMedicineShouldNotBeFound("chemicalName.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedicinesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where type equals to DEFAULT_TYPE
        defaultMedicineShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the medicineList where type equals to UPDATED_TYPE
        defaultMedicineShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllMedicinesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultMedicineShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the medicineList where type equals to UPDATED_TYPE
        defaultMedicineShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllMedicinesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where type is not null
        defaultMedicineShouldBeFound("type.specified=true");

        // Get all the medicineList where type is null
        defaultMedicineShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedicinesByManufacturerIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where manufacturer equals to DEFAULT_MANUFACTURER
        defaultMedicineShouldBeFound("manufacturer.equals=" + DEFAULT_MANUFACTURER);

        // Get all the medicineList where manufacturer equals to UPDATED_MANUFACTURER
        defaultMedicineShouldNotBeFound("manufacturer.equals=" + UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    public void getAllMedicinesByManufacturerIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where manufacturer in DEFAULT_MANUFACTURER or UPDATED_MANUFACTURER
        defaultMedicineShouldBeFound("manufacturer.in=" + DEFAULT_MANUFACTURER + "," + UPDATED_MANUFACTURER);

        // Get all the medicineList where manufacturer equals to UPDATED_MANUFACTURER
        defaultMedicineShouldNotBeFound("manufacturer.in=" + UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    public void getAllMedicinesByManufacturerIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where manufacturer is not null
        defaultMedicineShouldBeFound("manufacturer.specified=true");

        // Get all the medicineList where manufacturer is null
        defaultMedicineShouldNotBeFound("manufacturer.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedicinesByMrpIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where mrp equals to DEFAULT_MRP
        defaultMedicineShouldBeFound("mrp.equals=" + DEFAULT_MRP);

        // Get all the medicineList where mrp equals to UPDATED_MRP
        defaultMedicineShouldNotBeFound("mrp.equals=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    public void getAllMedicinesByMrpIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where mrp in DEFAULT_MRP or UPDATED_MRP
        defaultMedicineShouldBeFound("mrp.in=" + DEFAULT_MRP + "," + UPDATED_MRP);

        // Get all the medicineList where mrp equals to UPDATED_MRP
        defaultMedicineShouldNotBeFound("mrp.in=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    public void getAllMedicinesByMrpIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where mrp is not null
        defaultMedicineShouldBeFound("mrp.specified=true");

        // Get all the medicineList where mrp is null
        defaultMedicineShouldNotBeFound("mrp.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedicinesByIndicationsIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where indications equals to DEFAULT_INDICATIONS
        defaultMedicineShouldBeFound("indications.equals=" + DEFAULT_INDICATIONS);

        // Get all the medicineList where indications equals to UPDATED_INDICATIONS
        defaultMedicineShouldNotBeFound("indications.equals=" + UPDATED_INDICATIONS);
    }

    @Test
    @Transactional
    public void getAllMedicinesByIndicationsIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where indications in DEFAULT_INDICATIONS or UPDATED_INDICATIONS
        defaultMedicineShouldBeFound("indications.in=" + DEFAULT_INDICATIONS + "," + UPDATED_INDICATIONS);

        // Get all the medicineList where indications equals to UPDATED_INDICATIONS
        defaultMedicineShouldNotBeFound("indications.in=" + UPDATED_INDICATIONS);
    }

    @Test
    @Transactional
    public void getAllMedicinesByIndicationsIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where indications is not null
        defaultMedicineShouldBeFound("indications.specified=true");

        // Get all the medicineList where indications is null
        defaultMedicineShouldNotBeFound("indications.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedicinesByDoseAndAdminIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where doseAndAdmin equals to DEFAULT_DOSE_AND_ADMIN
        defaultMedicineShouldBeFound("doseAndAdmin.equals=" + DEFAULT_DOSE_AND_ADMIN);

        // Get all the medicineList where doseAndAdmin equals to UPDATED_DOSE_AND_ADMIN
        defaultMedicineShouldNotBeFound("doseAndAdmin.equals=" + UPDATED_DOSE_AND_ADMIN);
    }

    @Test
    @Transactional
    public void getAllMedicinesByDoseAndAdminIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where doseAndAdmin in DEFAULT_DOSE_AND_ADMIN or UPDATED_DOSE_AND_ADMIN
        defaultMedicineShouldBeFound("doseAndAdmin.in=" + DEFAULT_DOSE_AND_ADMIN + "," + UPDATED_DOSE_AND_ADMIN);

        // Get all the medicineList where doseAndAdmin equals to UPDATED_DOSE_AND_ADMIN
        defaultMedicineShouldNotBeFound("doseAndAdmin.in=" + UPDATED_DOSE_AND_ADMIN);
    }

    @Test
    @Transactional
    public void getAllMedicinesByDoseAndAdminIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where doseAndAdmin is not null
        defaultMedicineShouldBeFound("doseAndAdmin.specified=true");

        // Get all the medicineList where doseAndAdmin is null
        defaultMedicineShouldNotBeFound("doseAndAdmin.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedicinesByPreparationIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where preparation equals to DEFAULT_PREPARATION
        defaultMedicineShouldBeFound("preparation.equals=" + DEFAULT_PREPARATION);

        // Get all the medicineList where preparation equals to UPDATED_PREPARATION
        defaultMedicineShouldNotBeFound("preparation.equals=" + UPDATED_PREPARATION);
    }

    @Test
    @Transactional
    public void getAllMedicinesByPreparationIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where preparation in DEFAULT_PREPARATION or UPDATED_PREPARATION
        defaultMedicineShouldBeFound("preparation.in=" + DEFAULT_PREPARATION + "," + UPDATED_PREPARATION);

        // Get all the medicineList where preparation equals to UPDATED_PREPARATION
        defaultMedicineShouldNotBeFound("preparation.in=" + UPDATED_PREPARATION);
    }

    @Test
    @Transactional
    public void getAllMedicinesByPreparationIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where preparation is not null
        defaultMedicineShouldBeFound("preparation.specified=true");

        // Get all the medicineList where preparation is null
        defaultMedicineShouldNotBeFound("preparation.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedicinesByProductUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where productUrl equals to DEFAULT_PRODUCT_URL
        defaultMedicineShouldBeFound("productUrl.equals=" + DEFAULT_PRODUCT_URL);

        // Get all the medicineList where productUrl equals to UPDATED_PRODUCT_URL
        defaultMedicineShouldNotBeFound("productUrl.equals=" + UPDATED_PRODUCT_URL);
    }

    @Test
    @Transactional
    public void getAllMedicinesByProductUrlIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where productUrl in DEFAULT_PRODUCT_URL or UPDATED_PRODUCT_URL
        defaultMedicineShouldBeFound("productUrl.in=" + DEFAULT_PRODUCT_URL + "," + UPDATED_PRODUCT_URL);

        // Get all the medicineList where productUrl equals to UPDATED_PRODUCT_URL
        defaultMedicineShouldNotBeFound("productUrl.in=" + UPDATED_PRODUCT_URL);
    }

    @Test
    @Transactional
    public void getAllMedicinesByProductUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where productUrl is not null
        defaultMedicineShouldBeFound("productUrl.specified=true");

        // Get all the medicineList where productUrl is null
        defaultMedicineShouldNotBeFound("productUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedicinesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where active equals to DEFAULT_ACTIVE
        defaultMedicineShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the medicineList where active equals to UPDATED_ACTIVE
        defaultMedicineShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMedicinesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultMedicineShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the medicineList where active equals to UPDATED_ACTIVE
        defaultMedicineShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMedicinesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where active is not null
        defaultMedicineShouldBeFound("active.specified=true");

        // Get all the medicineList where active is null
        defaultMedicineShouldNotBeFound("active.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMedicineShouldBeFound(String filter) throws Exception {
        restMedicineMockMvc.perform(get("/api/medicines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicine.getId().intValue())))
            .andExpect(jsonPath("$.[*].tradeName").value(hasItem(DEFAULT_TRADE_NAME.toString())))
            .andExpect(jsonPath("$.[*].unitQuantity").value(hasItem(DEFAULT_UNIT_QUANTITY.toString())))
            .andExpect(jsonPath("$.[*].genericName").value(hasItem(DEFAULT_GENERIC_NAME.toString())))
            .andExpect(jsonPath("$.[*].chemicalName").value(hasItem(DEFAULT_CHEMICAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER.toString())))
            .andExpect(jsonPath("$.[*].mrp").value(hasItem(DEFAULT_MRP.doubleValue())))
            .andExpect(jsonPath("$.[*].indications").value(hasItem(DEFAULT_INDICATIONS.toString())))
            .andExpect(jsonPath("$.[*].doseAndAdmin").value(hasItem(DEFAULT_DOSE_AND_ADMIN.toString())))
            .andExpect(jsonPath("$.[*].preparation").value(hasItem(DEFAULT_PREPARATION.toString())))
            .andExpect(jsonPath("$.[*].productUrl").value(hasItem(DEFAULT_PRODUCT_URL.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restMedicineMockMvc.perform(get("/api/medicines/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMedicineShouldNotBeFound(String filter) throws Exception {
        restMedicineMockMvc.perform(get("/api/medicines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicineMockMvc.perform(get("/api/medicines/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMedicine() throws Exception {
        // Get the medicine
        restMedicineMockMvc.perform(get("/api/medicines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicine() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        int databaseSizeBeforeUpdate = medicineRepository.findAll().size();

        // Update the medicine
        Medicine updatedMedicine = medicineRepository.findById(medicine.getId()).get();
        // Disconnect from session so that the updates on updatedMedicine are not directly saved in db
        em.detach(updatedMedicine);
        updatedMedicine
            .tradeName(UPDATED_TRADE_NAME)
            .unitQuantity(UPDATED_UNIT_QUANTITY)
            .genericName(UPDATED_GENERIC_NAME)
            .chemicalName(UPDATED_CHEMICAL_NAME)
            .type(UPDATED_TYPE)
            .manufacturer(UPDATED_MANUFACTURER)
            .mrp(UPDATED_MRP)
            .indications(UPDATED_INDICATIONS)
            .doseAndAdmin(UPDATED_DOSE_AND_ADMIN)
            .preparation(UPDATED_PREPARATION)
            .productUrl(UPDATED_PRODUCT_URL)
            .active(UPDATED_ACTIVE);
        MedicineDTO medicineDTO = medicineMapper.toDto(updatedMedicine);

        restMedicineMockMvc.perform(put("/api/medicines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isOk());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeUpdate);
        Medicine testMedicine = medicineList.get(medicineList.size() - 1);
        assertThat(testMedicine.getTradeName()).isEqualTo(UPDATED_TRADE_NAME);
        assertThat(testMedicine.getUnitQuantity()).isEqualTo(UPDATED_UNIT_QUANTITY);
        assertThat(testMedicine.getGenericName()).isEqualTo(UPDATED_GENERIC_NAME);
        assertThat(testMedicine.getChemicalName()).isEqualTo(UPDATED_CHEMICAL_NAME);
        assertThat(testMedicine.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMedicine.getManufacturer()).isEqualTo(UPDATED_MANUFACTURER);
        assertThat(testMedicine.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testMedicine.getIndications()).isEqualTo(UPDATED_INDICATIONS);
        assertThat(testMedicine.getDoseAndAdmin()).isEqualTo(UPDATED_DOSE_AND_ADMIN);
        assertThat(testMedicine.getPreparation()).isEqualTo(UPDATED_PREPARATION);
        assertThat(testMedicine.getProductUrl()).isEqualTo(UPDATED_PRODUCT_URL);
        assertThat(testMedicine.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Medicine in Elasticsearch
        verify(mockMedicineSearchRepository, times(1)).save(testMedicine);
    }

    @Test
    @Transactional
    public void updateNonExistingMedicine() throws Exception {
        int databaseSizeBeforeUpdate = medicineRepository.findAll().size();

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicineMockMvc.perform(put("/api/medicines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Medicine in Elasticsearch
        verify(mockMedicineSearchRepository, times(0)).save(medicine);
    }

    @Test
    @Transactional
    public void deleteMedicine() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        int databaseSizeBeforeDelete = medicineRepository.findAll().size();

        // Get the medicine
        restMedicineMockMvc.perform(delete("/api/medicines/{id}", medicine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Medicine in Elasticsearch
        verify(mockMedicineSearchRepository, times(1)).deleteById(medicine.getId());
    }

    @Test
    @Transactional
    public void searchMedicine() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);
        when(mockMedicineSearchRepository.search(queryStringQuery("id:" + medicine.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(medicine), PageRequest.of(0, 1), 1));
        // Search the medicine
        restMedicineMockMvc.perform(get("/api/_search/medicines?query=id:" + medicine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicine.getId().intValue())))
            .andExpect(jsonPath("$.[*].tradeName").value(hasItem(DEFAULT_TRADE_NAME)))
            .andExpect(jsonPath("$.[*].unitQuantity").value(hasItem(DEFAULT_UNIT_QUANTITY)))
            .andExpect(jsonPath("$.[*].genericName").value(hasItem(DEFAULT_GENERIC_NAME)))
            .andExpect(jsonPath("$.[*].chemicalName").value(hasItem(DEFAULT_CHEMICAL_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER)))
            .andExpect(jsonPath("$.[*].mrp").value(hasItem(DEFAULT_MRP.doubleValue())))
            .andExpect(jsonPath("$.[*].indications").value(hasItem(DEFAULT_INDICATIONS)))
            .andExpect(jsonPath("$.[*].doseAndAdmin").value(hasItem(DEFAULT_DOSE_AND_ADMIN)))
            .andExpect(jsonPath("$.[*].preparation").value(hasItem(DEFAULT_PREPARATION)))
            .andExpect(jsonPath("$.[*].productUrl").value(hasItem(DEFAULT_PRODUCT_URL)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicine.class);
        Medicine medicine1 = new Medicine();
        medicine1.setId(1L);
        Medicine medicine2 = new Medicine();
        medicine2.setId(medicine1.getId());
        assertThat(medicine1).isEqualTo(medicine2);
        medicine2.setId(2L);
        assertThat(medicine1).isNotEqualTo(medicine2);
        medicine1.setId(null);
        assertThat(medicine1).isNotEqualTo(medicine2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicineDTO.class);
        MedicineDTO medicineDTO1 = new MedicineDTO();
        medicineDTO1.setId(1L);
        MedicineDTO medicineDTO2 = new MedicineDTO();
        assertThat(medicineDTO1).isNotEqualTo(medicineDTO2);
        medicineDTO2.setId(medicineDTO1.getId());
        assertThat(medicineDTO1).isEqualTo(medicineDTO2);
        medicineDTO2.setId(2L);
        assertThat(medicineDTO1).isNotEqualTo(medicineDTO2);
        medicineDTO1.setId(null);
        assertThat(medicineDTO1).isNotEqualTo(medicineDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(medicineMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(medicineMapper.fromId(null)).isNull();
    }
}
