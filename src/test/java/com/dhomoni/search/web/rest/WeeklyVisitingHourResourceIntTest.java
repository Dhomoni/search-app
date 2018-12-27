package com.dhomoni.search.web.rest;

import com.dhomoni.search.SearchApp;

import com.dhomoni.search.config.SecurityBeanOverrideConfiguration;

import com.dhomoni.search.domain.WeeklyVisitingHour;
import com.dhomoni.search.domain.Chamber;
import com.dhomoni.search.repository.WeeklyVisitingHourRepository;
import com.dhomoni.search.repository.search.WeeklyVisitingHourSearchRepository;
import com.dhomoni.search.service.WeeklyVisitingHourService;
import com.dhomoni.search.service.dto.WeeklyVisitingHourDTO;
import com.dhomoni.search.service.mapper.WeeklyVisitingHourMapper;
import com.dhomoni.search.web.rest.errors.ExceptionTranslator;
import com.dhomoni.search.service.dto.WeeklyVisitingHourCriteria;
import com.dhomoni.search.service.WeeklyVisitingHourQueryService;

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

import com.dhomoni.search.domain.enumeration.WeekDay;
/**
 * Test class for the WeeklyVisitingHourResource REST controller.
 *
 * @see WeeklyVisitingHourResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SearchApp.class})
public class WeeklyVisitingHourResourceIntTest {

    private static final WeekDay DEFAULT_WEEK_DAY = WeekDay.SUN;
    private static final WeekDay UPDATED_WEEK_DAY = WeekDay.MON;

    private static final Integer DEFAULT_START_HOUR = 0;
    private static final Integer UPDATED_START_HOUR = 1;

    private static final Integer DEFAULT_START_MINUTE = 0;
    private static final Integer UPDATED_START_MINUTE = 1;

    private static final Integer DEFAULT_END_HOUR = 0;
    private static final Integer UPDATED_END_HOUR = 1;

    private static final Integer DEFAULT_END_MINUTE = 0;
    private static final Integer UPDATED_END_MINUTE = 1;

    @Autowired
    private WeeklyVisitingHourRepository weeklyVisitingHourRepository;

    @Autowired
    private WeeklyVisitingHourMapper weeklyVisitingHourMapper;

    @Autowired
    private WeeklyVisitingHourService weeklyVisitingHourService;

    /**
     * This repository is mocked in the com.dhomoni.search.repository.search test package.
     *
     * @see com.dhomoni.search.repository.search.WeeklyVisitingHourSearchRepositoryMockConfiguration
     */
    @Autowired
    private WeeklyVisitingHourSearchRepository mockWeeklyVisitingHourSearchRepository;

    @Autowired
    private WeeklyVisitingHourQueryService weeklyVisitingHourQueryService;

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

    private MockMvc restWeeklyVisitingHourMockMvc;

    private WeeklyVisitingHour weeklyVisitingHour;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WeeklyVisitingHourResource weeklyVisitingHourResource = new WeeklyVisitingHourResource(weeklyVisitingHourService, weeklyVisitingHourQueryService);
        this.restWeeklyVisitingHourMockMvc = MockMvcBuilders.standaloneSetup(weeklyVisitingHourResource)
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
    public static WeeklyVisitingHour createEntity(EntityManager em) {
        WeeklyVisitingHour weeklyVisitingHour = new WeeklyVisitingHour()
            .weekDay(DEFAULT_WEEK_DAY)
            .startHour(DEFAULT_START_HOUR)
            .startMinute(DEFAULT_START_MINUTE)
            .endHour(DEFAULT_END_HOUR)
            .endMinute(DEFAULT_END_MINUTE);
        return weeklyVisitingHour;
    }

    @Before
    public void initTest() {
        weeklyVisitingHour = createEntity(em);
    }

    @Test
    @Transactional
    public void createWeeklyVisitingHour() throws Exception {
        int databaseSizeBeforeCreate = weeklyVisitingHourRepository.findAll().size();

        // Create the WeeklyVisitingHour
        WeeklyVisitingHourDTO weeklyVisitingHourDTO = weeklyVisitingHourMapper.toDto(weeklyVisitingHour);
        restWeeklyVisitingHourMockMvc.perform(post("/api/weekly-visiting-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weeklyVisitingHourDTO)))
            .andExpect(status().isCreated());

        // Validate the WeeklyVisitingHour in the database
        List<WeeklyVisitingHour> weeklyVisitingHourList = weeklyVisitingHourRepository.findAll();
        assertThat(weeklyVisitingHourList).hasSize(databaseSizeBeforeCreate + 1);
        WeeklyVisitingHour testWeeklyVisitingHour = weeklyVisitingHourList.get(weeklyVisitingHourList.size() - 1);
        assertThat(testWeeklyVisitingHour.getWeekDay()).isEqualTo(DEFAULT_WEEK_DAY);
        assertThat(testWeeklyVisitingHour.getStartHour()).isEqualTo(DEFAULT_START_HOUR);
        assertThat(testWeeklyVisitingHour.getStartMinute()).isEqualTo(DEFAULT_START_MINUTE);
        assertThat(testWeeklyVisitingHour.getEndHour()).isEqualTo(DEFAULT_END_HOUR);
        assertThat(testWeeklyVisitingHour.getEndMinute()).isEqualTo(DEFAULT_END_MINUTE);

        // Validate the WeeklyVisitingHour in Elasticsearch
        verify(mockWeeklyVisitingHourSearchRepository, times(1)).save(testWeeklyVisitingHour);
    }

    @Test
    @Transactional
    public void createWeeklyVisitingHourWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = weeklyVisitingHourRepository.findAll().size();

        // Create the WeeklyVisitingHour with an existing ID
        weeklyVisitingHour.setId(1L);
        WeeklyVisitingHourDTO weeklyVisitingHourDTO = weeklyVisitingHourMapper.toDto(weeklyVisitingHour);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeeklyVisitingHourMockMvc.perform(post("/api/weekly-visiting-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weeklyVisitingHourDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WeeklyVisitingHour in the database
        List<WeeklyVisitingHour> weeklyVisitingHourList = weeklyVisitingHourRepository.findAll();
        assertThat(weeklyVisitingHourList).hasSize(databaseSizeBeforeCreate);

        // Validate the WeeklyVisitingHour in Elasticsearch
        verify(mockWeeklyVisitingHourSearchRepository, times(0)).save(weeklyVisitingHour);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHours() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList
        restWeeklyVisitingHourMockMvc.perform(get("/api/weekly-visiting-hours?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyVisitingHour.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekDay").value(hasItem(DEFAULT_WEEK_DAY.toString())))
            .andExpect(jsonPath("$.[*].startHour").value(hasItem(DEFAULT_START_HOUR)))
            .andExpect(jsonPath("$.[*].startMinute").value(hasItem(DEFAULT_START_MINUTE)))
            .andExpect(jsonPath("$.[*].endHour").value(hasItem(DEFAULT_END_HOUR)))
            .andExpect(jsonPath("$.[*].endMinute").value(hasItem(DEFAULT_END_MINUTE)));
    }
    
    @Test
    @Transactional
    public void getWeeklyVisitingHour() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get the weeklyVisitingHour
        restWeeklyVisitingHourMockMvc.perform(get("/api/weekly-visiting-hours/{id}", weeklyVisitingHour.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(weeklyVisitingHour.getId().intValue()))
            .andExpect(jsonPath("$.weekDay").value(DEFAULT_WEEK_DAY.toString()))
            .andExpect(jsonPath("$.startHour").value(DEFAULT_START_HOUR))
            .andExpect(jsonPath("$.startMinute").value(DEFAULT_START_MINUTE))
            .andExpect(jsonPath("$.endHour").value(DEFAULT_END_HOUR))
            .andExpect(jsonPath("$.endMinute").value(DEFAULT_END_MINUTE));
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByWeekDayIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where weekDay equals to DEFAULT_WEEK_DAY
        defaultWeeklyVisitingHourShouldBeFound("weekDay.equals=" + DEFAULT_WEEK_DAY);

        // Get all the weeklyVisitingHourList where weekDay equals to UPDATED_WEEK_DAY
        defaultWeeklyVisitingHourShouldNotBeFound("weekDay.equals=" + UPDATED_WEEK_DAY);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByWeekDayIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where weekDay in DEFAULT_WEEK_DAY or UPDATED_WEEK_DAY
        defaultWeeklyVisitingHourShouldBeFound("weekDay.in=" + DEFAULT_WEEK_DAY + "," + UPDATED_WEEK_DAY);

        // Get all the weeklyVisitingHourList where weekDay equals to UPDATED_WEEK_DAY
        defaultWeeklyVisitingHourShouldNotBeFound("weekDay.in=" + UPDATED_WEEK_DAY);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByWeekDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where weekDay is not null
        defaultWeeklyVisitingHourShouldBeFound("weekDay.specified=true");

        // Get all the weeklyVisitingHourList where weekDay is null
        defaultWeeklyVisitingHourShouldNotBeFound("weekDay.specified=false");
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartHourIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where startHour equals to DEFAULT_START_HOUR
        defaultWeeklyVisitingHourShouldBeFound("startHour.equals=" + DEFAULT_START_HOUR);

        // Get all the weeklyVisitingHourList where startHour equals to UPDATED_START_HOUR
        defaultWeeklyVisitingHourShouldNotBeFound("startHour.equals=" + UPDATED_START_HOUR);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartHourIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where startHour in DEFAULT_START_HOUR or UPDATED_START_HOUR
        defaultWeeklyVisitingHourShouldBeFound("startHour.in=" + DEFAULT_START_HOUR + "," + UPDATED_START_HOUR);

        // Get all the weeklyVisitingHourList where startHour equals to UPDATED_START_HOUR
        defaultWeeklyVisitingHourShouldNotBeFound("startHour.in=" + UPDATED_START_HOUR);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where startHour is not null
        defaultWeeklyVisitingHourShouldBeFound("startHour.specified=true");

        // Get all the weeklyVisitingHourList where startHour is null
        defaultWeeklyVisitingHourShouldNotBeFound("startHour.specified=false");
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where startHour greater than or equals to DEFAULT_START_HOUR
        defaultWeeklyVisitingHourShouldBeFound("startHour.greaterOrEqualThan=" + DEFAULT_START_HOUR);

        // Get all the weeklyVisitingHourList where startHour greater than or equals to (DEFAULT_START_HOUR + 1)
        defaultWeeklyVisitingHourShouldNotBeFound("startHour.greaterOrEqualThan=" + (DEFAULT_START_HOUR + 1));
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartHourIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where startHour less than or equals to DEFAULT_START_HOUR
        defaultWeeklyVisitingHourShouldNotBeFound("startHour.lessThan=" + DEFAULT_START_HOUR);

        // Get all the weeklyVisitingHourList where startHour less than or equals to (DEFAULT_START_HOUR + 1)
        defaultWeeklyVisitingHourShouldBeFound("startHour.lessThan=" + (DEFAULT_START_HOUR + 1));
    }


    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartMinuteIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where startMinute equals to DEFAULT_START_MINUTE
        defaultWeeklyVisitingHourShouldBeFound("startMinute.equals=" + DEFAULT_START_MINUTE);

        // Get all the weeklyVisitingHourList where startMinute equals to UPDATED_START_MINUTE
        defaultWeeklyVisitingHourShouldNotBeFound("startMinute.equals=" + UPDATED_START_MINUTE);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartMinuteIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where startMinute in DEFAULT_START_MINUTE or UPDATED_START_MINUTE
        defaultWeeklyVisitingHourShouldBeFound("startMinute.in=" + DEFAULT_START_MINUTE + "," + UPDATED_START_MINUTE);

        // Get all the weeklyVisitingHourList where startMinute equals to UPDATED_START_MINUTE
        defaultWeeklyVisitingHourShouldNotBeFound("startMinute.in=" + UPDATED_START_MINUTE);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartMinuteIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where startMinute is not null
        defaultWeeklyVisitingHourShouldBeFound("startMinute.specified=true");

        // Get all the weeklyVisitingHourList where startMinute is null
        defaultWeeklyVisitingHourShouldNotBeFound("startMinute.specified=false");
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartMinuteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where startMinute greater than or equals to DEFAULT_START_MINUTE
        defaultWeeklyVisitingHourShouldBeFound("startMinute.greaterOrEqualThan=" + DEFAULT_START_MINUTE);

        // Get all the weeklyVisitingHourList where startMinute greater than or equals to (DEFAULT_START_MINUTE + 1)
        defaultWeeklyVisitingHourShouldNotBeFound("startMinute.greaterOrEqualThan=" + (DEFAULT_START_MINUTE + 1));
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartMinuteIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where startMinute less than or equals to DEFAULT_START_MINUTE
        defaultWeeklyVisitingHourShouldNotBeFound("startMinute.lessThan=" + DEFAULT_START_MINUTE);

        // Get all the weeklyVisitingHourList where startMinute less than or equals to (DEFAULT_START_MINUTE + 1)
        defaultWeeklyVisitingHourShouldBeFound("startMinute.lessThan=" + (DEFAULT_START_MINUTE + 1));
    }


    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndHourIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where endHour equals to DEFAULT_END_HOUR
        defaultWeeklyVisitingHourShouldBeFound("endHour.equals=" + DEFAULT_END_HOUR);

        // Get all the weeklyVisitingHourList where endHour equals to UPDATED_END_HOUR
        defaultWeeklyVisitingHourShouldNotBeFound("endHour.equals=" + UPDATED_END_HOUR);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndHourIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where endHour in DEFAULT_END_HOUR or UPDATED_END_HOUR
        defaultWeeklyVisitingHourShouldBeFound("endHour.in=" + DEFAULT_END_HOUR + "," + UPDATED_END_HOUR);

        // Get all the weeklyVisitingHourList where endHour equals to UPDATED_END_HOUR
        defaultWeeklyVisitingHourShouldNotBeFound("endHour.in=" + UPDATED_END_HOUR);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where endHour is not null
        defaultWeeklyVisitingHourShouldBeFound("endHour.specified=true");

        // Get all the weeklyVisitingHourList where endHour is null
        defaultWeeklyVisitingHourShouldNotBeFound("endHour.specified=false");
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where endHour greater than or equals to DEFAULT_END_HOUR
        defaultWeeklyVisitingHourShouldBeFound("endHour.greaterOrEqualThan=" + DEFAULT_END_HOUR);

        // Get all the weeklyVisitingHourList where endHour greater than or equals to (DEFAULT_END_HOUR + 1)
        defaultWeeklyVisitingHourShouldNotBeFound("endHour.greaterOrEqualThan=" + (DEFAULT_END_HOUR + 1));
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndHourIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where endHour less than or equals to DEFAULT_END_HOUR
        defaultWeeklyVisitingHourShouldNotBeFound("endHour.lessThan=" + DEFAULT_END_HOUR);

        // Get all the weeklyVisitingHourList where endHour less than or equals to (DEFAULT_END_HOUR + 1)
        defaultWeeklyVisitingHourShouldBeFound("endHour.lessThan=" + (DEFAULT_END_HOUR + 1));
    }


    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndMinuteIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where endMinute equals to DEFAULT_END_MINUTE
        defaultWeeklyVisitingHourShouldBeFound("endMinute.equals=" + DEFAULT_END_MINUTE);

        // Get all the weeklyVisitingHourList where endMinute equals to UPDATED_END_MINUTE
        defaultWeeklyVisitingHourShouldNotBeFound("endMinute.equals=" + UPDATED_END_MINUTE);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndMinuteIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where endMinute in DEFAULT_END_MINUTE or UPDATED_END_MINUTE
        defaultWeeklyVisitingHourShouldBeFound("endMinute.in=" + DEFAULT_END_MINUTE + "," + UPDATED_END_MINUTE);

        // Get all the weeklyVisitingHourList where endMinute equals to UPDATED_END_MINUTE
        defaultWeeklyVisitingHourShouldNotBeFound("endMinute.in=" + UPDATED_END_MINUTE);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndMinuteIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where endMinute is not null
        defaultWeeklyVisitingHourShouldBeFound("endMinute.specified=true");

        // Get all the weeklyVisitingHourList where endMinute is null
        defaultWeeklyVisitingHourShouldNotBeFound("endMinute.specified=false");
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndMinuteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where endMinute greater than or equals to DEFAULT_END_MINUTE
        defaultWeeklyVisitingHourShouldBeFound("endMinute.greaterOrEqualThan=" + DEFAULT_END_MINUTE);

        // Get all the weeklyVisitingHourList where endMinute greater than or equals to (DEFAULT_END_MINUTE + 1)
        defaultWeeklyVisitingHourShouldNotBeFound("endMinute.greaterOrEqualThan=" + (DEFAULT_END_MINUTE + 1));
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndMinuteIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        // Get all the weeklyVisitingHourList where endMinute less than or equals to DEFAULT_END_MINUTE
        defaultWeeklyVisitingHourShouldNotBeFound("endMinute.lessThan=" + DEFAULT_END_MINUTE);

        // Get all the weeklyVisitingHourList where endMinute less than or equals to (DEFAULT_END_MINUTE + 1)
        defaultWeeklyVisitingHourShouldBeFound("endMinute.lessThan=" + (DEFAULT_END_MINUTE + 1));
    }


    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByChamberIsEqualToSomething() throws Exception {
        // Initialize the database
        Chamber chamber = ChamberResourceIntTest.createEntity(em);
        em.persist(chamber);
        em.flush();
        weeklyVisitingHour.setChamber(chamber);
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);
        Long chamberId = chamber.getId();

        // Get all the weeklyVisitingHourList where chamber equals to chamberId
        defaultWeeklyVisitingHourShouldBeFound("chamberId.equals=" + chamberId);

        // Get all the weeklyVisitingHourList where chamber equals to chamberId + 1
        defaultWeeklyVisitingHourShouldNotBeFound("chamberId.equals=" + (chamberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWeeklyVisitingHourShouldBeFound(String filter) throws Exception {
        restWeeklyVisitingHourMockMvc.perform(get("/api/weekly-visiting-hours?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyVisitingHour.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekDay").value(hasItem(DEFAULT_WEEK_DAY.toString())))
            .andExpect(jsonPath("$.[*].startHour").value(hasItem(DEFAULT_START_HOUR)))
            .andExpect(jsonPath("$.[*].startMinute").value(hasItem(DEFAULT_START_MINUTE)))
            .andExpect(jsonPath("$.[*].endHour").value(hasItem(DEFAULT_END_HOUR)))
            .andExpect(jsonPath("$.[*].endMinute").value(hasItem(DEFAULT_END_MINUTE)));

        // Check, that the count call also returns 1
        restWeeklyVisitingHourMockMvc.perform(get("/api/weekly-visiting-hours/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWeeklyVisitingHourShouldNotBeFound(String filter) throws Exception {
        restWeeklyVisitingHourMockMvc.perform(get("/api/weekly-visiting-hours?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWeeklyVisitingHourMockMvc.perform(get("/api/weekly-visiting-hours/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingWeeklyVisitingHour() throws Exception {
        // Get the weeklyVisitingHour
        restWeeklyVisitingHourMockMvc.perform(get("/api/weekly-visiting-hours/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeeklyVisitingHour() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        int databaseSizeBeforeUpdate = weeklyVisitingHourRepository.findAll().size();

        // Update the weeklyVisitingHour
        WeeklyVisitingHour updatedWeeklyVisitingHour = weeklyVisitingHourRepository.findById(weeklyVisitingHour.getId()).get();
        // Disconnect from session so that the updates on updatedWeeklyVisitingHour are not directly saved in db
        em.detach(updatedWeeklyVisitingHour);
        updatedWeeklyVisitingHour
            .weekDay(UPDATED_WEEK_DAY)
            .startHour(UPDATED_START_HOUR)
            .startMinute(UPDATED_START_MINUTE)
            .endHour(UPDATED_END_HOUR)
            .endMinute(UPDATED_END_MINUTE);
        WeeklyVisitingHourDTO weeklyVisitingHourDTO = weeklyVisitingHourMapper.toDto(updatedWeeklyVisitingHour);

        restWeeklyVisitingHourMockMvc.perform(put("/api/weekly-visiting-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weeklyVisitingHourDTO)))
            .andExpect(status().isOk());

        // Validate the WeeklyVisitingHour in the database
        List<WeeklyVisitingHour> weeklyVisitingHourList = weeklyVisitingHourRepository.findAll();
        assertThat(weeklyVisitingHourList).hasSize(databaseSizeBeforeUpdate);
        WeeklyVisitingHour testWeeklyVisitingHour = weeklyVisitingHourList.get(weeklyVisitingHourList.size() - 1);
        assertThat(testWeeklyVisitingHour.getWeekDay()).isEqualTo(UPDATED_WEEK_DAY);
        assertThat(testWeeklyVisitingHour.getStartHour()).isEqualTo(UPDATED_START_HOUR);
        assertThat(testWeeklyVisitingHour.getStartMinute()).isEqualTo(UPDATED_START_MINUTE);
        assertThat(testWeeklyVisitingHour.getEndHour()).isEqualTo(UPDATED_END_HOUR);
        assertThat(testWeeklyVisitingHour.getEndMinute()).isEqualTo(UPDATED_END_MINUTE);

        // Validate the WeeklyVisitingHour in Elasticsearch
        verify(mockWeeklyVisitingHourSearchRepository, times(1)).save(testWeeklyVisitingHour);
    }

    @Test
    @Transactional
    public void updateNonExistingWeeklyVisitingHour() throws Exception {
        int databaseSizeBeforeUpdate = weeklyVisitingHourRepository.findAll().size();

        // Create the WeeklyVisitingHour
        WeeklyVisitingHourDTO weeklyVisitingHourDTO = weeklyVisitingHourMapper.toDto(weeklyVisitingHour);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeeklyVisitingHourMockMvc.perform(put("/api/weekly-visiting-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weeklyVisitingHourDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WeeklyVisitingHour in the database
        List<WeeklyVisitingHour> weeklyVisitingHourList = weeklyVisitingHourRepository.findAll();
        assertThat(weeklyVisitingHourList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyVisitingHour in Elasticsearch
        verify(mockWeeklyVisitingHourSearchRepository, times(0)).save(weeklyVisitingHour);
    }

    @Test
    @Transactional
    public void deleteWeeklyVisitingHour() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);

        int databaseSizeBeforeDelete = weeklyVisitingHourRepository.findAll().size();

        // Get the weeklyVisitingHour
        restWeeklyVisitingHourMockMvc.perform(delete("/api/weekly-visiting-hours/{id}", weeklyVisitingHour.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WeeklyVisitingHour> weeklyVisitingHourList = weeklyVisitingHourRepository.findAll();
        assertThat(weeklyVisitingHourList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WeeklyVisitingHour in Elasticsearch
        verify(mockWeeklyVisitingHourSearchRepository, times(1)).deleteById(weeklyVisitingHour.getId());
    }

    @Test
    @Transactional
    public void searchWeeklyVisitingHour() throws Exception {
        // Initialize the database
        weeklyVisitingHourRepository.saveAndFlush(weeklyVisitingHour);
        when(mockWeeklyVisitingHourSearchRepository.search(queryStringQuery("id:" + weeklyVisitingHour.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(weeklyVisitingHour), PageRequest.of(0, 1), 1));
        // Search the weeklyVisitingHour
        restWeeklyVisitingHourMockMvc.perform(get("/api/_search/weekly-visiting-hours?query=id:" + weeklyVisitingHour.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyVisitingHour.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekDay").value(hasItem(DEFAULT_WEEK_DAY.toString())))
            .andExpect(jsonPath("$.[*].startHour").value(hasItem(DEFAULT_START_HOUR)))
            .andExpect(jsonPath("$.[*].startMinute").value(hasItem(DEFAULT_START_MINUTE)))
            .andExpect(jsonPath("$.[*].endHour").value(hasItem(DEFAULT_END_HOUR)))
            .andExpect(jsonPath("$.[*].endMinute").value(hasItem(DEFAULT_END_MINUTE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeeklyVisitingHour.class);
        WeeklyVisitingHour weeklyVisitingHour1 = new WeeklyVisitingHour();
        weeklyVisitingHour1.setId(1L);
        WeeklyVisitingHour weeklyVisitingHour2 = new WeeklyVisitingHour();
        weeklyVisitingHour2.setId(weeklyVisitingHour1.getId());
        assertThat(weeklyVisitingHour1).isEqualTo(weeklyVisitingHour2);
        weeklyVisitingHour2.setId(2L);
        assertThat(weeklyVisitingHour1).isNotEqualTo(weeklyVisitingHour2);
        weeklyVisitingHour1.setId(null);
        assertThat(weeklyVisitingHour1).isNotEqualTo(weeklyVisitingHour2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeeklyVisitingHourDTO.class);
        WeeklyVisitingHourDTO weeklyVisitingHourDTO1 = new WeeklyVisitingHourDTO();
        weeklyVisitingHourDTO1.setId(1L);
        WeeklyVisitingHourDTO weeklyVisitingHourDTO2 = new WeeklyVisitingHourDTO();
        assertThat(weeklyVisitingHourDTO1).isNotEqualTo(weeklyVisitingHourDTO2);
        weeklyVisitingHourDTO2.setId(weeklyVisitingHourDTO1.getId());
        assertThat(weeklyVisitingHourDTO1).isEqualTo(weeklyVisitingHourDTO2);
        weeklyVisitingHourDTO2.setId(2L);
        assertThat(weeklyVisitingHourDTO1).isNotEqualTo(weeklyVisitingHourDTO2);
        weeklyVisitingHourDTO1.setId(null);
        assertThat(weeklyVisitingHourDTO1).isNotEqualTo(weeklyVisitingHourDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(weeklyVisitingHourMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(weeklyVisitingHourMapper.fromId(null)).isNull();
    }
}
