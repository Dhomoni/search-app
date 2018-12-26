package com.dhomoni.search.web.rest;

import com.dhomoni.search.SearchApp;

import com.dhomoni.search.config.SecurityBeanOverrideConfiguration;

import com.dhomoni.search.domain.WeeklyVisitingHours;
import com.dhomoni.search.domain.Chamber;
import com.dhomoni.search.repository.WeeklyVisitingHoursRepository;
import com.dhomoni.search.repository.search.WeeklyVisitingHoursSearchRepository;
import com.dhomoni.search.service.WeeklyVisitingHoursService;
import com.dhomoni.search.service.dto.WeeklyVisitingHoursDTO;
import com.dhomoni.search.service.mapper.WeeklyVisitingHoursMapper;
import com.dhomoni.search.web.rest.errors.ExceptionTranslator;
import com.dhomoni.search.service.dto.WeeklyVisitingHoursCriteria;
import com.dhomoni.search.service.WeeklyVisitingHoursQueryService;

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
 * Test class for the WeeklyVisitingHoursResource REST controller.
 *
 * @see WeeklyVisitingHoursResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SearchApp.class})
public class WeeklyVisitingHoursResourceIntTest {

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
    private WeeklyVisitingHoursRepository weeklyVisitingHoursRepository;

    @Autowired
    private WeeklyVisitingHoursMapper weeklyVisitingHoursMapper;

    @Autowired
    private WeeklyVisitingHoursService weeklyVisitingHoursService;

    /**
     * This repository is mocked in the com.dhomoni.search.repository.search test package.
     *
     * @see com.dhomoni.search.repository.search.WeeklyVisitingHoursSearchRepositoryMockConfiguration
     */
    @Autowired
    private WeeklyVisitingHoursSearchRepository mockWeeklyVisitingHoursSearchRepository;

    @Autowired
    private WeeklyVisitingHoursQueryService weeklyVisitingHoursQueryService;

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

    private MockMvc restWeeklyVisitingHoursMockMvc;

    private WeeklyVisitingHours weeklyVisitingHours;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WeeklyVisitingHoursResource weeklyVisitingHoursResource = new WeeklyVisitingHoursResource(weeklyVisitingHoursService, weeklyVisitingHoursQueryService);
        this.restWeeklyVisitingHoursMockMvc = MockMvcBuilders.standaloneSetup(weeklyVisitingHoursResource)
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
    public static WeeklyVisitingHours createEntity(EntityManager em) {
        WeeklyVisitingHours weeklyVisitingHours = new WeeklyVisitingHours()
            .weekDay(DEFAULT_WEEK_DAY)
            .startHour(DEFAULT_START_HOUR)
            .startMinute(DEFAULT_START_MINUTE)
            .endHour(DEFAULT_END_HOUR)
            .endMinute(DEFAULT_END_MINUTE);
        return weeklyVisitingHours;
    }

    @Before
    public void initTest() {
        weeklyVisitingHours = createEntity(em);
    }

    @Test
    @Transactional
    public void createWeeklyVisitingHours() throws Exception {
        int databaseSizeBeforeCreate = weeklyVisitingHoursRepository.findAll().size();

        // Create the WeeklyVisitingHours
        WeeklyVisitingHoursDTO weeklyVisitingHoursDTO = weeklyVisitingHoursMapper.toDto(weeklyVisitingHours);
        restWeeklyVisitingHoursMockMvc.perform(post("/api/weekly-visiting-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weeklyVisitingHoursDTO)))
            .andExpect(status().isCreated());

        // Validate the WeeklyVisitingHours in the database
        List<WeeklyVisitingHours> weeklyVisitingHoursList = weeklyVisitingHoursRepository.findAll();
        assertThat(weeklyVisitingHoursList).hasSize(databaseSizeBeforeCreate + 1);
        WeeklyVisitingHours testWeeklyVisitingHours = weeklyVisitingHoursList.get(weeklyVisitingHoursList.size() - 1);
        assertThat(testWeeklyVisitingHours.getWeekDay()).isEqualTo(DEFAULT_WEEK_DAY);
        assertThat(testWeeklyVisitingHours.getStartHour()).isEqualTo(DEFAULT_START_HOUR);
        assertThat(testWeeklyVisitingHours.getStartMinute()).isEqualTo(DEFAULT_START_MINUTE);
        assertThat(testWeeklyVisitingHours.getEndHour()).isEqualTo(DEFAULT_END_HOUR);
        assertThat(testWeeklyVisitingHours.getEndMinute()).isEqualTo(DEFAULT_END_MINUTE);

        // Validate the WeeklyVisitingHours in Elasticsearch
        verify(mockWeeklyVisitingHoursSearchRepository, times(1)).save(testWeeklyVisitingHours);
    }

    @Test
    @Transactional
    public void createWeeklyVisitingHoursWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = weeklyVisitingHoursRepository.findAll().size();

        // Create the WeeklyVisitingHours with an existing ID
        weeklyVisitingHours.setId(1L);
        WeeklyVisitingHoursDTO weeklyVisitingHoursDTO = weeklyVisitingHoursMapper.toDto(weeklyVisitingHours);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeeklyVisitingHoursMockMvc.perform(post("/api/weekly-visiting-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weeklyVisitingHoursDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WeeklyVisitingHours in the database
        List<WeeklyVisitingHours> weeklyVisitingHoursList = weeklyVisitingHoursRepository.findAll();
        assertThat(weeklyVisitingHoursList).hasSize(databaseSizeBeforeCreate);

        // Validate the WeeklyVisitingHours in Elasticsearch
        verify(mockWeeklyVisitingHoursSearchRepository, times(0)).save(weeklyVisitingHours);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHours() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList
        restWeeklyVisitingHoursMockMvc.perform(get("/api/weekly-visiting-hours?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyVisitingHours.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekDay").value(hasItem(DEFAULT_WEEK_DAY.toString())))
            .andExpect(jsonPath("$.[*].startHour").value(hasItem(DEFAULT_START_HOUR)))
            .andExpect(jsonPath("$.[*].startMinute").value(hasItem(DEFAULT_START_MINUTE)))
            .andExpect(jsonPath("$.[*].endHour").value(hasItem(DEFAULT_END_HOUR)))
            .andExpect(jsonPath("$.[*].endMinute").value(hasItem(DEFAULT_END_MINUTE)));
    }
    
    @Test
    @Transactional
    public void getWeeklyVisitingHours() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get the weeklyVisitingHours
        restWeeklyVisitingHoursMockMvc.perform(get("/api/weekly-visiting-hours/{id}", weeklyVisitingHours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(weeklyVisitingHours.getId().intValue()))
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
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where weekDay equals to DEFAULT_WEEK_DAY
        defaultWeeklyVisitingHoursShouldBeFound("weekDay.equals=" + DEFAULT_WEEK_DAY);

        // Get all the weeklyVisitingHoursList where weekDay equals to UPDATED_WEEK_DAY
        defaultWeeklyVisitingHoursShouldNotBeFound("weekDay.equals=" + UPDATED_WEEK_DAY);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByWeekDayIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where weekDay in DEFAULT_WEEK_DAY or UPDATED_WEEK_DAY
        defaultWeeklyVisitingHoursShouldBeFound("weekDay.in=" + DEFAULT_WEEK_DAY + "," + UPDATED_WEEK_DAY);

        // Get all the weeklyVisitingHoursList where weekDay equals to UPDATED_WEEK_DAY
        defaultWeeklyVisitingHoursShouldNotBeFound("weekDay.in=" + UPDATED_WEEK_DAY);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByWeekDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where weekDay is not null
        defaultWeeklyVisitingHoursShouldBeFound("weekDay.specified=true");

        // Get all the weeklyVisitingHoursList where weekDay is null
        defaultWeeklyVisitingHoursShouldNotBeFound("weekDay.specified=false");
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartHourIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where startHour equals to DEFAULT_START_HOUR
        defaultWeeklyVisitingHoursShouldBeFound("startHour.equals=" + DEFAULT_START_HOUR);

        // Get all the weeklyVisitingHoursList where startHour equals to UPDATED_START_HOUR
        defaultWeeklyVisitingHoursShouldNotBeFound("startHour.equals=" + UPDATED_START_HOUR);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartHourIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where startHour in DEFAULT_START_HOUR or UPDATED_START_HOUR
        defaultWeeklyVisitingHoursShouldBeFound("startHour.in=" + DEFAULT_START_HOUR + "," + UPDATED_START_HOUR);

        // Get all the weeklyVisitingHoursList where startHour equals to UPDATED_START_HOUR
        defaultWeeklyVisitingHoursShouldNotBeFound("startHour.in=" + UPDATED_START_HOUR);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where startHour is not null
        defaultWeeklyVisitingHoursShouldBeFound("startHour.specified=true");

        // Get all the weeklyVisitingHoursList where startHour is null
        defaultWeeklyVisitingHoursShouldNotBeFound("startHour.specified=false");
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where startHour greater than or equals to DEFAULT_START_HOUR
        defaultWeeklyVisitingHoursShouldBeFound("startHour.greaterOrEqualThan=" + DEFAULT_START_HOUR);

        // Get all the weeklyVisitingHoursList where startHour greater than or equals to (DEFAULT_START_HOUR + 1)
        defaultWeeklyVisitingHoursShouldNotBeFound("startHour.greaterOrEqualThan=" + (DEFAULT_START_HOUR + 1));
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartHourIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where startHour less than or equals to DEFAULT_START_HOUR
        defaultWeeklyVisitingHoursShouldNotBeFound("startHour.lessThan=" + DEFAULT_START_HOUR);

        // Get all the weeklyVisitingHoursList where startHour less than or equals to (DEFAULT_START_HOUR + 1)
        defaultWeeklyVisitingHoursShouldBeFound("startHour.lessThan=" + (DEFAULT_START_HOUR + 1));
    }


    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartMinuteIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where startMinute equals to DEFAULT_START_MINUTE
        defaultWeeklyVisitingHoursShouldBeFound("startMinute.equals=" + DEFAULT_START_MINUTE);

        // Get all the weeklyVisitingHoursList where startMinute equals to UPDATED_START_MINUTE
        defaultWeeklyVisitingHoursShouldNotBeFound("startMinute.equals=" + UPDATED_START_MINUTE);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartMinuteIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where startMinute in DEFAULT_START_MINUTE or UPDATED_START_MINUTE
        defaultWeeklyVisitingHoursShouldBeFound("startMinute.in=" + DEFAULT_START_MINUTE + "," + UPDATED_START_MINUTE);

        // Get all the weeklyVisitingHoursList where startMinute equals to UPDATED_START_MINUTE
        defaultWeeklyVisitingHoursShouldNotBeFound("startMinute.in=" + UPDATED_START_MINUTE);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartMinuteIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where startMinute is not null
        defaultWeeklyVisitingHoursShouldBeFound("startMinute.specified=true");

        // Get all the weeklyVisitingHoursList where startMinute is null
        defaultWeeklyVisitingHoursShouldNotBeFound("startMinute.specified=false");
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartMinuteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where startMinute greater than or equals to DEFAULT_START_MINUTE
        defaultWeeklyVisitingHoursShouldBeFound("startMinute.greaterOrEqualThan=" + DEFAULT_START_MINUTE);

        // Get all the weeklyVisitingHoursList where startMinute greater than or equals to (DEFAULT_START_MINUTE + 1)
        defaultWeeklyVisitingHoursShouldNotBeFound("startMinute.greaterOrEqualThan=" + (DEFAULT_START_MINUTE + 1));
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByStartMinuteIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where startMinute less than or equals to DEFAULT_START_MINUTE
        defaultWeeklyVisitingHoursShouldNotBeFound("startMinute.lessThan=" + DEFAULT_START_MINUTE);

        // Get all the weeklyVisitingHoursList where startMinute less than or equals to (DEFAULT_START_MINUTE + 1)
        defaultWeeklyVisitingHoursShouldBeFound("startMinute.lessThan=" + (DEFAULT_START_MINUTE + 1));
    }


    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndHourIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where endHour equals to DEFAULT_END_HOUR
        defaultWeeklyVisitingHoursShouldBeFound("endHour.equals=" + DEFAULT_END_HOUR);

        // Get all the weeklyVisitingHoursList where endHour equals to UPDATED_END_HOUR
        defaultWeeklyVisitingHoursShouldNotBeFound("endHour.equals=" + UPDATED_END_HOUR);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndHourIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where endHour in DEFAULT_END_HOUR or UPDATED_END_HOUR
        defaultWeeklyVisitingHoursShouldBeFound("endHour.in=" + DEFAULT_END_HOUR + "," + UPDATED_END_HOUR);

        // Get all the weeklyVisitingHoursList where endHour equals to UPDATED_END_HOUR
        defaultWeeklyVisitingHoursShouldNotBeFound("endHour.in=" + UPDATED_END_HOUR);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndHourIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where endHour is not null
        defaultWeeklyVisitingHoursShouldBeFound("endHour.specified=true");

        // Get all the weeklyVisitingHoursList where endHour is null
        defaultWeeklyVisitingHoursShouldNotBeFound("endHour.specified=false");
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndHourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where endHour greater than or equals to DEFAULT_END_HOUR
        defaultWeeklyVisitingHoursShouldBeFound("endHour.greaterOrEqualThan=" + DEFAULT_END_HOUR);

        // Get all the weeklyVisitingHoursList where endHour greater than or equals to (DEFAULT_END_HOUR + 1)
        defaultWeeklyVisitingHoursShouldNotBeFound("endHour.greaterOrEqualThan=" + (DEFAULT_END_HOUR + 1));
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndHourIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where endHour less than or equals to DEFAULT_END_HOUR
        defaultWeeklyVisitingHoursShouldNotBeFound("endHour.lessThan=" + DEFAULT_END_HOUR);

        // Get all the weeklyVisitingHoursList where endHour less than or equals to (DEFAULT_END_HOUR + 1)
        defaultWeeklyVisitingHoursShouldBeFound("endHour.lessThan=" + (DEFAULT_END_HOUR + 1));
    }


    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndMinuteIsEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where endMinute equals to DEFAULT_END_MINUTE
        defaultWeeklyVisitingHoursShouldBeFound("endMinute.equals=" + DEFAULT_END_MINUTE);

        // Get all the weeklyVisitingHoursList where endMinute equals to UPDATED_END_MINUTE
        defaultWeeklyVisitingHoursShouldNotBeFound("endMinute.equals=" + UPDATED_END_MINUTE);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndMinuteIsInShouldWork() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where endMinute in DEFAULT_END_MINUTE or UPDATED_END_MINUTE
        defaultWeeklyVisitingHoursShouldBeFound("endMinute.in=" + DEFAULT_END_MINUTE + "," + UPDATED_END_MINUTE);

        // Get all the weeklyVisitingHoursList where endMinute equals to UPDATED_END_MINUTE
        defaultWeeklyVisitingHoursShouldNotBeFound("endMinute.in=" + UPDATED_END_MINUTE);
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndMinuteIsNullOrNotNull() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where endMinute is not null
        defaultWeeklyVisitingHoursShouldBeFound("endMinute.specified=true");

        // Get all the weeklyVisitingHoursList where endMinute is null
        defaultWeeklyVisitingHoursShouldNotBeFound("endMinute.specified=false");
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndMinuteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where endMinute greater than or equals to DEFAULT_END_MINUTE
        defaultWeeklyVisitingHoursShouldBeFound("endMinute.greaterOrEqualThan=" + DEFAULT_END_MINUTE);

        // Get all the weeklyVisitingHoursList where endMinute greater than or equals to (DEFAULT_END_MINUTE + 1)
        defaultWeeklyVisitingHoursShouldNotBeFound("endMinute.greaterOrEqualThan=" + (DEFAULT_END_MINUTE + 1));
    }

    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByEndMinuteIsLessThanSomething() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        // Get all the weeklyVisitingHoursList where endMinute less than or equals to DEFAULT_END_MINUTE
        defaultWeeklyVisitingHoursShouldNotBeFound("endMinute.lessThan=" + DEFAULT_END_MINUTE);

        // Get all the weeklyVisitingHoursList where endMinute less than or equals to (DEFAULT_END_MINUTE + 1)
        defaultWeeklyVisitingHoursShouldBeFound("endMinute.lessThan=" + (DEFAULT_END_MINUTE + 1));
    }


    @Test
    @Transactional
    public void getAllWeeklyVisitingHoursByChamberIsEqualToSomething() throws Exception {
        // Initialize the database
        Chamber chamber = ChamberResourceIntTest.createEntity(em);
        em.persist(chamber);
        em.flush();
        weeklyVisitingHours.setChamber(chamber);
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);
        Long chamberId = chamber.getId();

        // Get all the weeklyVisitingHoursList where chamber equals to chamberId
        defaultWeeklyVisitingHoursShouldBeFound("chamberId.equals=" + chamberId);

        // Get all the weeklyVisitingHoursList where chamber equals to chamberId + 1
        defaultWeeklyVisitingHoursShouldNotBeFound("chamberId.equals=" + (chamberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWeeklyVisitingHoursShouldBeFound(String filter) throws Exception {
        restWeeklyVisitingHoursMockMvc.perform(get("/api/weekly-visiting-hours?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyVisitingHours.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekDay").value(hasItem(DEFAULT_WEEK_DAY.toString())))
            .andExpect(jsonPath("$.[*].startHour").value(hasItem(DEFAULT_START_HOUR)))
            .andExpect(jsonPath("$.[*].startMinute").value(hasItem(DEFAULT_START_MINUTE)))
            .andExpect(jsonPath("$.[*].endHour").value(hasItem(DEFAULT_END_HOUR)))
            .andExpect(jsonPath("$.[*].endMinute").value(hasItem(DEFAULT_END_MINUTE)));

        // Check, that the count call also returns 1
        restWeeklyVisitingHoursMockMvc.perform(get("/api/weekly-visiting-hours/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWeeklyVisitingHoursShouldNotBeFound(String filter) throws Exception {
        restWeeklyVisitingHoursMockMvc.perform(get("/api/weekly-visiting-hours?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWeeklyVisitingHoursMockMvc.perform(get("/api/weekly-visiting-hours/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingWeeklyVisitingHours() throws Exception {
        // Get the weeklyVisitingHours
        restWeeklyVisitingHoursMockMvc.perform(get("/api/weekly-visiting-hours/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeeklyVisitingHours() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        int databaseSizeBeforeUpdate = weeklyVisitingHoursRepository.findAll().size();

        // Update the weeklyVisitingHours
        WeeklyVisitingHours updatedWeeklyVisitingHours = weeklyVisitingHoursRepository.findById(weeklyVisitingHours.getId()).get();
        // Disconnect from session so that the updates on updatedWeeklyVisitingHours are not directly saved in db
        em.detach(updatedWeeklyVisitingHours);
        updatedWeeklyVisitingHours
            .weekDay(UPDATED_WEEK_DAY)
            .startHour(UPDATED_START_HOUR)
            .startMinute(UPDATED_START_MINUTE)
            .endHour(UPDATED_END_HOUR)
            .endMinute(UPDATED_END_MINUTE);
        WeeklyVisitingHoursDTO weeklyVisitingHoursDTO = weeklyVisitingHoursMapper.toDto(updatedWeeklyVisitingHours);

        restWeeklyVisitingHoursMockMvc.perform(put("/api/weekly-visiting-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weeklyVisitingHoursDTO)))
            .andExpect(status().isOk());

        // Validate the WeeklyVisitingHours in the database
        List<WeeklyVisitingHours> weeklyVisitingHoursList = weeklyVisitingHoursRepository.findAll();
        assertThat(weeklyVisitingHoursList).hasSize(databaseSizeBeforeUpdate);
        WeeklyVisitingHours testWeeklyVisitingHours = weeklyVisitingHoursList.get(weeklyVisitingHoursList.size() - 1);
        assertThat(testWeeklyVisitingHours.getWeekDay()).isEqualTo(UPDATED_WEEK_DAY);
        assertThat(testWeeklyVisitingHours.getStartHour()).isEqualTo(UPDATED_START_HOUR);
        assertThat(testWeeklyVisitingHours.getStartMinute()).isEqualTo(UPDATED_START_MINUTE);
        assertThat(testWeeklyVisitingHours.getEndHour()).isEqualTo(UPDATED_END_HOUR);
        assertThat(testWeeklyVisitingHours.getEndMinute()).isEqualTo(UPDATED_END_MINUTE);

        // Validate the WeeklyVisitingHours in Elasticsearch
        verify(mockWeeklyVisitingHoursSearchRepository, times(1)).save(testWeeklyVisitingHours);
    }

    @Test
    @Transactional
    public void updateNonExistingWeeklyVisitingHours() throws Exception {
        int databaseSizeBeforeUpdate = weeklyVisitingHoursRepository.findAll().size();

        // Create the WeeklyVisitingHours
        WeeklyVisitingHoursDTO weeklyVisitingHoursDTO = weeklyVisitingHoursMapper.toDto(weeklyVisitingHours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeeklyVisitingHoursMockMvc.perform(put("/api/weekly-visiting-hours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(weeklyVisitingHoursDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WeeklyVisitingHours in the database
        List<WeeklyVisitingHours> weeklyVisitingHoursList = weeklyVisitingHoursRepository.findAll();
        assertThat(weeklyVisitingHoursList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WeeklyVisitingHours in Elasticsearch
        verify(mockWeeklyVisitingHoursSearchRepository, times(0)).save(weeklyVisitingHours);
    }

    @Test
    @Transactional
    public void deleteWeeklyVisitingHours() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);

        int databaseSizeBeforeDelete = weeklyVisitingHoursRepository.findAll().size();

        // Get the weeklyVisitingHours
        restWeeklyVisitingHoursMockMvc.perform(delete("/api/weekly-visiting-hours/{id}", weeklyVisitingHours.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WeeklyVisitingHours> weeklyVisitingHoursList = weeklyVisitingHoursRepository.findAll();
        assertThat(weeklyVisitingHoursList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WeeklyVisitingHours in Elasticsearch
        verify(mockWeeklyVisitingHoursSearchRepository, times(1)).deleteById(weeklyVisitingHours.getId());
    }

    @Test
    @Transactional
    public void searchWeeklyVisitingHours() throws Exception {
        // Initialize the database
        weeklyVisitingHoursRepository.saveAndFlush(weeklyVisitingHours);
        when(mockWeeklyVisitingHoursSearchRepository.search(queryStringQuery("id:" + weeklyVisitingHours.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(weeklyVisitingHours), PageRequest.of(0, 1), 1));
        // Search the weeklyVisitingHours
        restWeeklyVisitingHoursMockMvc.perform(get("/api/_search/weekly-visiting-hours?query=id:" + weeklyVisitingHours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyVisitingHours.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekDay").value(hasItem(DEFAULT_WEEK_DAY.toString())))
            .andExpect(jsonPath("$.[*].startHour").value(hasItem(DEFAULT_START_HOUR)))
            .andExpect(jsonPath("$.[*].startMinute").value(hasItem(DEFAULT_START_MINUTE)))
            .andExpect(jsonPath("$.[*].endHour").value(hasItem(DEFAULT_END_HOUR)))
            .andExpect(jsonPath("$.[*].endMinute").value(hasItem(DEFAULT_END_MINUTE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeeklyVisitingHours.class);
        WeeklyVisitingHours weeklyVisitingHours1 = new WeeklyVisitingHours();
        weeklyVisitingHours1.setId(1L);
        WeeklyVisitingHours weeklyVisitingHours2 = new WeeklyVisitingHours();
        weeklyVisitingHours2.setId(weeklyVisitingHours1.getId());
        assertThat(weeklyVisitingHours1).isEqualTo(weeklyVisitingHours2);
        weeklyVisitingHours2.setId(2L);
        assertThat(weeklyVisitingHours1).isNotEqualTo(weeklyVisitingHours2);
        weeklyVisitingHours1.setId(null);
        assertThat(weeklyVisitingHours1).isNotEqualTo(weeklyVisitingHours2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeeklyVisitingHoursDTO.class);
        WeeklyVisitingHoursDTO weeklyVisitingHoursDTO1 = new WeeklyVisitingHoursDTO();
        weeklyVisitingHoursDTO1.setId(1L);
        WeeklyVisitingHoursDTO weeklyVisitingHoursDTO2 = new WeeklyVisitingHoursDTO();
        assertThat(weeklyVisitingHoursDTO1).isNotEqualTo(weeklyVisitingHoursDTO2);
        weeklyVisitingHoursDTO2.setId(weeklyVisitingHoursDTO1.getId());
        assertThat(weeklyVisitingHoursDTO1).isEqualTo(weeklyVisitingHoursDTO2);
        weeklyVisitingHoursDTO2.setId(2L);
        assertThat(weeklyVisitingHoursDTO1).isNotEqualTo(weeklyVisitingHoursDTO2);
        weeklyVisitingHoursDTO1.setId(null);
        assertThat(weeklyVisitingHoursDTO1).isNotEqualTo(weeklyVisitingHoursDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(weeklyVisitingHoursMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(weeklyVisitingHoursMapper.fromId(null)).isNull();
    }
}
