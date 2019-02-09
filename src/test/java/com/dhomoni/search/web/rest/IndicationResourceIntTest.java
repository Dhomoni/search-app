package com.dhomoni.search.web.rest;

import com.dhomoni.search.SearchApp;

import com.dhomoni.search.config.SecurityBeanOverrideConfiguration;

import com.dhomoni.search.domain.Indication;
import com.dhomoni.search.repository.IndicationRepository;
import com.dhomoni.search.repository.search.IndicationSearchRepository;
import com.dhomoni.search.service.IndicationService;
import com.dhomoni.search.service.dto.IndicationDTO;
import com.dhomoni.search.service.mapper.IndicationMapper;
import com.dhomoni.search.web.rest.errors.ExceptionTranslator;
import com.dhomoni.search.service.dto.IndicationCriteria;
import com.dhomoni.search.service.IndicationQueryService;

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
 * Test class for the IndicationResource REST controller.
 *
 * @see IndicationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SearchApp.class})
public class IndicationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private IndicationRepository indicationRepository;

    @Autowired
    private IndicationMapper indicationMapper;

    @Autowired
    private IndicationService indicationService;

    /**
     * This repository is mocked in the com.dhomoni.search.repository.search test package.
     *
     * @see com.dhomoni.search.repository.search.IndicationSearchRepositoryMockConfiguration
     */
    @Autowired
    private IndicationSearchRepository mockIndicationSearchRepository;

    @Autowired
    private IndicationQueryService indicationQueryService;

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

    private MockMvc restIndicationMockMvc;

    private Indication indication;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IndicationResource indicationResource = new IndicationResource(indicationService, indicationQueryService);
        this.restIndicationMockMvc = MockMvcBuilders.standaloneSetup(indicationResource)
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
    public static Indication createEntity(EntityManager em) {
        Indication indication = new Indication()
            .name(DEFAULT_NAME);
        return indication;
    }

    @Before
    public void initTest() {
        indication = createEntity(em);
    }

    @Test
    @Transactional
    public void createIndication() throws Exception {
        int databaseSizeBeforeCreate = indicationRepository.findAll().size();

        // Create the Indication
        IndicationDTO indicationDTO = indicationMapper.toDto(indication);
        restIndicationMockMvc.perform(post("/api/indications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indicationDTO)))
            .andExpect(status().isCreated());

        // Validate the Indication in the database
        List<Indication> indicationList = indicationRepository.findAll();
        assertThat(indicationList).hasSize(databaseSizeBeforeCreate + 1);
        Indication testIndication = indicationList.get(indicationList.size() - 1);
        assertThat(testIndication.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Indication in Elasticsearch
        verify(mockIndicationSearchRepository, times(1)).save(testIndication);
    }

    @Test
    @Transactional
    public void createIndicationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = indicationRepository.findAll().size();

        // Create the Indication with an existing ID
        indication.setId(1L);
        IndicationDTO indicationDTO = indicationMapper.toDto(indication);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndicationMockMvc.perform(post("/api/indications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Indication in the database
        List<Indication> indicationList = indicationRepository.findAll();
        assertThat(indicationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Indication in Elasticsearch
        verify(mockIndicationSearchRepository, times(0)).save(indication);
    }

    @Test
    @Transactional
    public void getAllIndications() throws Exception {
        // Initialize the database
        indicationRepository.saveAndFlush(indication);

        // Get all the indicationList
        restIndicationMockMvc.perform(get("/api/indications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indication.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getIndication() throws Exception {
        // Initialize the database
        indicationRepository.saveAndFlush(indication);

        // Get the indication
        restIndicationMockMvc.perform(get("/api/indications/{id}", indication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(indication.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllIndicationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        indicationRepository.saveAndFlush(indication);

        // Get all the indicationList where name equals to DEFAULT_NAME
        defaultIndicationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the indicationList where name equals to UPDATED_NAME
        defaultIndicationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllIndicationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        indicationRepository.saveAndFlush(indication);

        // Get all the indicationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultIndicationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the indicationList where name equals to UPDATED_NAME
        defaultIndicationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllIndicationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        indicationRepository.saveAndFlush(indication);

        // Get all the indicationList where name is not null
        defaultIndicationShouldBeFound("name.specified=true");

        // Get all the indicationList where name is null
        defaultIndicationShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultIndicationShouldBeFound(String filter) throws Exception {
        restIndicationMockMvc.perform(get("/api/indications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indication.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));

        // Check, that the count call also returns 1
        restIndicationMockMvc.perform(get("/api/indications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultIndicationShouldNotBeFound(String filter) throws Exception {
        restIndicationMockMvc.perform(get("/api/indications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndicationMockMvc.perform(get("/api/indications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingIndication() throws Exception {
        // Get the indication
        restIndicationMockMvc.perform(get("/api/indications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndication() throws Exception {
        // Initialize the database
        indicationRepository.saveAndFlush(indication);

        int databaseSizeBeforeUpdate = indicationRepository.findAll().size();

        // Update the indication
        Indication updatedIndication = indicationRepository.findById(indication.getId()).get();
        // Disconnect from session so that the updates on updatedIndication are not directly saved in db
        em.detach(updatedIndication);
        updatedIndication
            .name(UPDATED_NAME);
        IndicationDTO indicationDTO = indicationMapper.toDto(updatedIndication);

        restIndicationMockMvc.perform(put("/api/indications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indicationDTO)))
            .andExpect(status().isOk());

        // Validate the Indication in the database
        List<Indication> indicationList = indicationRepository.findAll();
        assertThat(indicationList).hasSize(databaseSizeBeforeUpdate);
        Indication testIndication = indicationList.get(indicationList.size() - 1);
        assertThat(testIndication.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Indication in Elasticsearch
        verify(mockIndicationSearchRepository, times(1)).save(testIndication);
    }

    @Test
    @Transactional
    public void updateNonExistingIndication() throws Exception {
        int databaseSizeBeforeUpdate = indicationRepository.findAll().size();

        // Create the Indication
        IndicationDTO indicationDTO = indicationMapper.toDto(indication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndicationMockMvc.perform(put("/api/indications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Indication in the database
        List<Indication> indicationList = indicationRepository.findAll();
        assertThat(indicationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Indication in Elasticsearch
        verify(mockIndicationSearchRepository, times(0)).save(indication);
    }

    @Test
    @Transactional
    public void deleteIndication() throws Exception {
        // Initialize the database
        indicationRepository.saveAndFlush(indication);

        int databaseSizeBeforeDelete = indicationRepository.findAll().size();

        // Get the indication
        restIndicationMockMvc.perform(delete("/api/indications/{id}", indication.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Indication> indicationList = indicationRepository.findAll();
        assertThat(indicationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Indication in Elasticsearch
        verify(mockIndicationSearchRepository, times(1)).deleteById(indication.getId());
    }

    @Test
    @Transactional
    public void searchIndication() throws Exception {
        // Initialize the database
        indicationRepository.saveAndFlush(indication);
        when(mockIndicationSearchRepository.search(queryStringQuery("id:" + indication.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(indication), PageRequest.of(0, 1), 1));
        // Search the indication
        restIndicationMockMvc.perform(get("/api/_search/indications?query=id:" + indication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indication.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Indication.class);
        Indication indication1 = new Indication();
        indication1.setId(1L);
        Indication indication2 = new Indication();
        indication2.setId(indication1.getId());
        assertThat(indication1).isEqualTo(indication2);
        indication2.setId(2L);
        assertThat(indication1).isNotEqualTo(indication2);
        indication1.setId(null);
        assertThat(indication1).isNotEqualTo(indication2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndicationDTO.class);
        IndicationDTO indicationDTO1 = new IndicationDTO();
        indicationDTO1.setId(1L);
        IndicationDTO indicationDTO2 = new IndicationDTO();
        assertThat(indicationDTO1).isNotEqualTo(indicationDTO2);
        indicationDTO2.setId(indicationDTO1.getId());
        assertThat(indicationDTO1).isEqualTo(indicationDTO2);
        indicationDTO2.setId(2L);
        assertThat(indicationDTO1).isNotEqualTo(indicationDTO2);
        indicationDTO1.setId(null);
        assertThat(indicationDTO1).isNotEqualTo(indicationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(indicationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(indicationMapper.fromId(null)).isNull();
    }
}
