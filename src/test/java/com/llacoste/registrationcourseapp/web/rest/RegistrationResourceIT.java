package com.llacoste.registrationcourseapp.web.rest;

import com.llacoste.registrationcourseapp.RegistrationCourseApp;
import com.llacoste.registrationcourseapp.domain.Registration;
import com.llacoste.registrationcourseapp.repository.RegistrationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RegistrationResource} REST controller.
 */
@SpringBootTest(classes = RegistrationCourseApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class RegistrationResourceIT {

    private static final Boolean DEFAULT_IS_CERT_OK = false;
    private static final Boolean UPDATED_IS_CERT_OK = true;

    private static final Boolean DEFAULT_IS_PAYE = false;
    private static final Boolean UPDATED_IS_PAYE = true;

    private static final Long DEFAULT_DOSSARD = 1L;
    private static final Long UPDATED_DOSSARD = 2L;

    private static final Duration DEFAULT_TEMPS = Duration.ofHours(6);
    private static final Duration UPDATED_TEMPS = Duration.ofHours(12);

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegistrationMockMvc;

    private Registration registration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Registration createEntity(EntityManager em) {
        Registration registration = new Registration()
            .isCertOk(DEFAULT_IS_CERT_OK)
            .isPaye(DEFAULT_IS_PAYE)
            .dossard(DEFAULT_DOSSARD)
            .temps(DEFAULT_TEMPS)
            .date(DEFAULT_DATE);
        return registration;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Registration createUpdatedEntity(EntityManager em) {
        Registration registration = new Registration()
            .isCertOk(UPDATED_IS_CERT_OK)
            .isPaye(UPDATED_IS_PAYE)
            .dossard(UPDATED_DOSSARD)
            .temps(UPDATED_TEMPS)
            .date(UPDATED_DATE);
        return registration;
    }

    @BeforeEach
    public void initTest() {
        registration = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegistration() throws Exception {
        int databaseSizeBeforeCreate = registrationRepository.findAll().size();

        // Create the Registration
        restRegistrationMockMvc.perform(post("/api/registrations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(registration)))
            .andExpect(status().isCreated());

        // Validate the Registration in the database
        List<Registration> registrationList = registrationRepository.findAll();
        assertThat(registrationList).hasSize(databaseSizeBeforeCreate + 1);
        Registration testRegistration = registrationList.get(registrationList.size() - 1);
        assertThat(testRegistration.isIsCertOk()).isEqualTo(DEFAULT_IS_CERT_OK);
        assertThat(testRegistration.isIsPaye()).isEqualTo(DEFAULT_IS_PAYE);
        assertThat(testRegistration.getDossard()).isEqualTo(DEFAULT_DOSSARD);
        assertThat(testRegistration.getTemps()).isEqualTo(DEFAULT_TEMPS);
        assertThat(testRegistration.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createRegistrationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = registrationRepository.findAll().size();

        // Create the Registration with an existing ID
        registration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistrationMockMvc.perform(post("/api/registrations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(registration)))
            .andExpect(status().isBadRequest());

        // Validate the Registration in the database
        List<Registration> registrationList = registrationRepository.findAll();
        assertThat(registrationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRegistrations() throws Exception {
        // Initialize the database
        registrationRepository.saveAndFlush(registration);

        // Get all the registrationList
        restRegistrationMockMvc.perform(get("/api/registrations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registration.getId().intValue())))
            .andExpect(jsonPath("$.[*].isCertOk").value(hasItem(DEFAULT_IS_CERT_OK.booleanValue())))
            .andExpect(jsonPath("$.[*].isPaye").value(hasItem(DEFAULT_IS_PAYE.booleanValue())))
            .andExpect(jsonPath("$.[*].dossard").value(hasItem(DEFAULT_DOSSARD.intValue())))
            .andExpect(jsonPath("$.[*].temps").value(hasItem(DEFAULT_TEMPS.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getRegistration() throws Exception {
        // Initialize the database
        registrationRepository.saveAndFlush(registration);

        // Get the registration
        restRegistrationMockMvc.perform(get("/api/registrations/{id}", registration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(registration.getId().intValue()))
            .andExpect(jsonPath("$.isCertOk").value(DEFAULT_IS_CERT_OK.booleanValue()))
            .andExpect(jsonPath("$.isPaye").value(DEFAULT_IS_PAYE.booleanValue()))
            .andExpect(jsonPath("$.dossard").value(DEFAULT_DOSSARD.intValue()))
            .andExpect(jsonPath("$.temps").value(DEFAULT_TEMPS.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRegistration() throws Exception {
        // Get the registration
        restRegistrationMockMvc.perform(get("/api/registrations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegistration() throws Exception {
        // Initialize the database
        registrationRepository.saveAndFlush(registration);

        int databaseSizeBeforeUpdate = registrationRepository.findAll().size();

        // Update the registration
        Registration updatedRegistration = registrationRepository.findById(registration.getId()).get();
        // Disconnect from session so that the updates on updatedRegistration are not directly saved in db
        em.detach(updatedRegistration);
        updatedRegistration
            .isCertOk(UPDATED_IS_CERT_OK)
            .isPaye(UPDATED_IS_PAYE)
            .dossard(UPDATED_DOSSARD)
            .temps(UPDATED_TEMPS)
            .date(UPDATED_DATE);

        restRegistrationMockMvc.perform(put("/api/registrations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRegistration)))
            .andExpect(status().isOk());

        // Validate the Registration in the database
        List<Registration> registrationList = registrationRepository.findAll();
        assertThat(registrationList).hasSize(databaseSizeBeforeUpdate);
        Registration testRegistration = registrationList.get(registrationList.size() - 1);
        assertThat(testRegistration.isIsCertOk()).isEqualTo(UPDATED_IS_CERT_OK);
        assertThat(testRegistration.isIsPaye()).isEqualTo(UPDATED_IS_PAYE);
        assertThat(testRegistration.getDossard()).isEqualTo(UPDATED_DOSSARD);
        assertThat(testRegistration.getTemps()).isEqualTo(UPDATED_TEMPS);
        assertThat(testRegistration.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingRegistration() throws Exception {
        int databaseSizeBeforeUpdate = registrationRepository.findAll().size();

        // Create the Registration

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistrationMockMvc.perform(put("/api/registrations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(registration)))
            .andExpect(status().isBadRequest());

        // Validate the Registration in the database
        List<Registration> registrationList = registrationRepository.findAll();
        assertThat(registrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRegistration() throws Exception {
        // Initialize the database
        registrationRepository.saveAndFlush(registration);

        int databaseSizeBeforeDelete = registrationRepository.findAll().size();

        // Delete the registration
        restRegistrationMockMvc.perform(delete("/api/registrations/{id}", registration.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Registration> registrationList = registrationRepository.findAll();
        assertThat(registrationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
