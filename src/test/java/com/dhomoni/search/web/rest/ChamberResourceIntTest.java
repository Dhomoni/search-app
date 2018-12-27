package com.dhomoni.search.web.rest;

import com.dhomoni.search.SearchApp;

import com.dhomoni.search.config.SecurityBeanOverrideConfiguration;

import com.dhomoni.search.domain.Chamber;
import com.dhomoni.search.domain.Doctor;
import com.dhomoni.search.domain.WeeklyVisitingHour;
import com.dhomoni.search.repository.ChamberRepository;
import com.dhomoni.search.repository.search.ChamberSearchRepository;
import com.dhomoni.search.service.ChamberService;
import com.dhomoni.search.service.dto.ChamberDTO;
import com.dhomoni.search.service.mapper.ChamberMapper;
import com.dhomoni.search.web.rest.errors.ExceptionTranslator;
import com.dhomoni.search.service.dto.ChamberCriteria;
import com.dhomoni.search.service.ChamberQueryService;

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
 * Test class for the ChamberResource REST controller.
 *
 * @see ChamberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SearchApp.class})
public class ChamberResourceIntTest {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Double DEFAULT_FEE = 10000D;
    private static final Double UPDATED_FEE = 20000D;

    @Autowired
    private ChamberRepository chamberRepository;

    @Autowired
    private ChamberMapper chamberMapper;

    @Autowired
    private ChamberService chamberService;

    /**
     * This repository is mocked in the com.dhomoni.search.repository.search test package.
     *
     * @see com.dhomoni.search.repository.search.ChamberSearchRepositoryMockConfiguration
     */
    @Autowired
    private ChamberSearchRepository mockChamberSearchRepository;

    @Autowired
    private ChamberQueryService chamberQueryService;

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

    private MockMvc restChamberMockMvc;

    private Chamber chamber;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChamberResource chamberResource = new ChamberResource(chamberService, chamberQueryService);
        this.restChamberMockMvc = MockMvcBuilders.standaloneSetup(chamberResource)
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
    public static Chamber createEntity(EntityManager em) {
        Chamber chamber = new Chamber()
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .fee(DEFAULT_FEE);
        return chamber;
    }

    @Before
    public void initTest() {
        chamber = createEntity(em);
    }

    @Test
    @Transactional
    public void createChamber() throws Exception {
        int databaseSizeBeforeCreate = chamberRepository.findAll().size();

        // Create the Chamber
        ChamberDTO chamberDTO = chamberMapper.toDto(chamber);
        restChamberMockMvc.perform(post("/api/chambers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chamberDTO)))
            .andExpect(status().isCreated());

        // Validate the Chamber in the database
        List<Chamber> chamberList = chamberRepository.findAll();
        assertThat(chamberList).hasSize(databaseSizeBeforeCreate + 1);
        Chamber testChamber = chamberList.get(chamberList.size() - 1);
        assertThat(testChamber.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testChamber.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testChamber.getFee()).isEqualTo(DEFAULT_FEE);

        // Validate the Chamber in Elasticsearch
        verify(mockChamberSearchRepository, times(1)).save(testChamber);
    }

    @Test
    @Transactional
    public void createChamberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chamberRepository.findAll().size();

        // Create the Chamber with an existing ID
        chamber.setId(1L);
        ChamberDTO chamberDTO = chamberMapper.toDto(chamber);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChamberMockMvc.perform(post("/api/chambers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chamberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Chamber in the database
        List<Chamber> chamberList = chamberRepository.findAll();
        assertThat(chamberList).hasSize(databaseSizeBeforeCreate);

        // Validate the Chamber in Elasticsearch
        verify(mockChamberSearchRepository, times(0)).save(chamber);
    }

    @Test
    @Transactional
    public void getAllChambers() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);

        // Get all the chamberList
        restChamberMockMvc.perform(get("/api/chambers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chamber.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getChamber() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);

        // Get the chamber
        restChamberMockMvc.perform(get("/api/chambers/{id}", chamber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chamber.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.fee").value(DEFAULT_FEE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllChambersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);

        // Get all the chamberList where address equals to DEFAULT_ADDRESS
        defaultChamberShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the chamberList where address equals to UPDATED_ADDRESS
        defaultChamberShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllChambersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);

        // Get all the chamberList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultChamberShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the chamberList where address equals to UPDATED_ADDRESS
        defaultChamberShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllChambersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);

        // Get all the chamberList where address is not null
        defaultChamberShouldBeFound("address.specified=true");

        // Get all the chamberList where address is null
        defaultChamberShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    public void getAllChambersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);

        // Get all the chamberList where phone equals to DEFAULT_PHONE
        defaultChamberShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the chamberList where phone equals to UPDATED_PHONE
        defaultChamberShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllChambersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);

        // Get all the chamberList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultChamberShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the chamberList where phone equals to UPDATED_PHONE
        defaultChamberShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllChambersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);

        // Get all the chamberList where phone is not null
        defaultChamberShouldBeFound("phone.specified=true");

        // Get all the chamberList where phone is null
        defaultChamberShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllChambersByFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);

        // Get all the chamberList where fee equals to DEFAULT_FEE
        defaultChamberShouldBeFound("fee.equals=" + DEFAULT_FEE);

        // Get all the chamberList where fee equals to UPDATED_FEE
        defaultChamberShouldNotBeFound("fee.equals=" + UPDATED_FEE);
    }

    @Test
    @Transactional
    public void getAllChambersByFeeIsInShouldWork() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);

        // Get all the chamberList where fee in DEFAULT_FEE or UPDATED_FEE
        defaultChamberShouldBeFound("fee.in=" + DEFAULT_FEE + "," + UPDATED_FEE);

        // Get all the chamberList where fee equals to UPDATED_FEE
        defaultChamberShouldNotBeFound("fee.in=" + UPDATED_FEE);
    }

    @Test
    @Transactional
    public void getAllChambersByFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);

        // Get all the chamberList where fee is not null
        defaultChamberShouldBeFound("fee.specified=true");

        // Get all the chamberList where fee is null
        defaultChamberShouldNotBeFound("fee.specified=false");
    }

    @Test
    @Transactional
    public void getAllChambersByDoctorIsEqualToSomething() throws Exception {
        // Initialize the database
        Doctor doctor = DoctorResourceIntTest.createEntity(em);
        em.persist(doctor);
        em.flush();
        chamber.setDoctor(doctor);
        chamberRepository.saveAndFlush(chamber);
        Long doctorId = doctor.getId();

        // Get all the chamberList where doctor equals to doctorId
        defaultChamberShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the chamberList where doctor equals to doctorId + 1
        defaultChamberShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }


    @Test
    @Transactional
    public void getAllChambersByWeeklyVisitingHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        WeeklyVisitingHour weeklyVisitingHours = WeeklyVisitingHourResourceIntTest.createEntity(em);
        em.persist(weeklyVisitingHours);
        em.flush();
        chamber.addWeeklyVisitingHours(weeklyVisitingHours);
        chamberRepository.saveAndFlush(chamber);
        Long weeklyVisitingHoursId = weeklyVisitingHours.getId();

        // Get all the chamberList where weeklyVisitingHours equals to weeklyVisitingHoursId
        defaultChamberShouldBeFound("weeklyVisitingHoursId.equals=" + weeklyVisitingHoursId);

        // Get all the chamberList where weeklyVisitingHours equals to weeklyVisitingHoursId + 1
        defaultChamberShouldNotBeFound("weeklyVisitingHoursId.equals=" + (weeklyVisitingHoursId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultChamberShouldBeFound(String filter) throws Exception {
        restChamberMockMvc.perform(get("/api/chambers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chamber.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.doubleValue())));

        // Check, that the count call also returns 1
        restChamberMockMvc.perform(get("/api/chambers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
//            .andExpect(content().string("1")); // For prepopulated data this test can not be done
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultChamberShouldNotBeFound(String filter) throws Exception {
        restChamberMockMvc.perform(get("/api/chambers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChamberMockMvc.perform(get("/api/chambers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChamber() throws Exception {
        // Get the chamber
        restChamberMockMvc.perform(get("/api/chambers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChamber() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);

        int databaseSizeBeforeUpdate = chamberRepository.findAll().size();

        // Update the chamber
        Chamber updatedChamber = chamberRepository.findById(chamber.getId()).get();
        // Disconnect from session so that the updates on updatedChamber are not directly saved in db
        em.detach(updatedChamber);
        updatedChamber
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .fee(UPDATED_FEE);
        ChamberDTO chamberDTO = chamberMapper.toDto(updatedChamber);

        restChamberMockMvc.perform(put("/api/chambers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chamberDTO)))
            .andExpect(status().isOk());

        // Validate the Chamber in the database
        List<Chamber> chamberList = chamberRepository.findAll();
        assertThat(chamberList).hasSize(databaseSizeBeforeUpdate);
        Chamber testChamber = chamberList.get(chamberList.size() - 1);
        assertThat(testChamber.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testChamber.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testChamber.getFee()).isEqualTo(UPDATED_FEE);

        // Validate the Chamber in Elasticsearch
        verify(mockChamberSearchRepository, times(1)).save(testChamber);
    }

    @Test
    @Transactional
    public void updateNonExistingChamber() throws Exception {
        int databaseSizeBeforeUpdate = chamberRepository.findAll().size();

        // Create the Chamber
        ChamberDTO chamberDTO = chamberMapper.toDto(chamber);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChamberMockMvc.perform(put("/api/chambers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chamberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Chamber in the database
        List<Chamber> chamberList = chamberRepository.findAll();
        assertThat(chamberList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Chamber in Elasticsearch
        verify(mockChamberSearchRepository, times(0)).save(chamber);
    }

    @Test
    @Transactional
    public void deleteChamber() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);

        int databaseSizeBeforeDelete = chamberRepository.findAll().size();

        // Get the chamber
        restChamberMockMvc.perform(delete("/api/chambers/{id}", chamber.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Chamber> chamberList = chamberRepository.findAll();
        assertThat(chamberList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Chamber in Elasticsearch
        verify(mockChamberSearchRepository, times(1)).deleteById(chamber.getId());
    }

    @Test
    @Transactional
    public void searchChamber() throws Exception {
        // Initialize the database
        chamberRepository.saveAndFlush(chamber);
        when(mockChamberSearchRepository.search(queryStringQuery("id:" + chamber.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(chamber), PageRequest.of(0, 1), 1));
        // Search the chamber
        restChamberMockMvc.perform(get("/api/_search/chambers?query=id:" + chamber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chamber.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chamber.class);
        Chamber chamber1 = new Chamber();
        chamber1.setId(1L);
        Chamber chamber2 = new Chamber();
        chamber2.setId(chamber1.getId());
        assertThat(chamber1).isEqualTo(chamber2);
        chamber2.setId(2L);
        assertThat(chamber1).isNotEqualTo(chamber2);
        chamber1.setId(null);
        assertThat(chamber1).isNotEqualTo(chamber2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChamberDTO.class);
        ChamberDTO chamberDTO1 = new ChamberDTO();
        chamberDTO1.setId(1L);
        ChamberDTO chamberDTO2 = new ChamberDTO();
        assertThat(chamberDTO1).isNotEqualTo(chamberDTO2);
        chamberDTO2.setId(chamberDTO1.getId());
        assertThat(chamberDTO1).isEqualTo(chamberDTO2);
        chamberDTO2.setId(2L);
        assertThat(chamberDTO1).isNotEqualTo(chamberDTO2);
        chamberDTO1.setId(null);
        assertThat(chamberDTO1).isNotEqualTo(chamberDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chamberMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chamberMapper.fromId(null)).isNull();
    }
}
