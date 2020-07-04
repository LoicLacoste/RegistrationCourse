package com.llacoste.registrationcourseapp.web.rest;

import com.llacoste.registrationcourseapp.RegistrationCourseApp;
import com.llacoste.registrationcourseapp.domain.ExtraUser;
import com.llacoste.registrationcourseapp.repository.ExtraUserRepository;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ExtraUserResource} REST controller.
 */
@SpringBootTest(classes = RegistrationCourseApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ExtraUserResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Long DEFAULT_PRENOM = 1L;
    private static final Long UPDATED_PRENOM = 2L;

    private static final Instant DEFAULT_DATE_NAISSANCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_NAISSANCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ExtraUserRepository extraUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtraUserMockMvc;

    private ExtraUser extraUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtraUser createEntity(EntityManager em) {
        ExtraUser extraUser = new ExtraUser()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .dateNaissance(DEFAULT_DATE_NAISSANCE);
        return extraUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtraUser createUpdatedEntity(EntityManager em) {
        ExtraUser extraUser = new ExtraUser()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE);
        return extraUser;
    }

    @BeforeEach
    public void initTest() {
        extraUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtraUser() throws Exception {
        int databaseSizeBeforeCreate = extraUserRepository.findAll().size();

        // Create the ExtraUser
        restExtraUserMockMvc.perform(post("/api/extra-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(extraUser)))
            .andExpect(status().isCreated());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeCreate + 1);
        ExtraUser testExtraUser = extraUserList.get(extraUserList.size() - 1);
        assertThat(testExtraUser.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testExtraUser.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testExtraUser.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void createExtraUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extraUserRepository.findAll().size();

        // Create the ExtraUser with an existing ID
        extraUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtraUserMockMvc.perform(post("/api/extra-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(extraUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = extraUserRepository.findAll().size();
        // set the field null
        extraUser.setNom(null);

        // Create the ExtraUser, which fails.

        restExtraUserMockMvc.perform(post("/api/extra-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(extraUser)))
            .andExpect(status().isBadRequest());

        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = extraUserRepository.findAll().size();
        // set the field null
        extraUser.setPrenom(null);

        // Create the ExtraUser, which fails.

        restExtraUserMockMvc.perform(post("/api/extra-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(extraUser)))
            .andExpect(status().isBadRequest());

        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateNaissanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = extraUserRepository.findAll().size();
        // set the field null
        extraUser.setDateNaissance(null);

        // Create the ExtraUser, which fails.

        restExtraUserMockMvc.perform(post("/api/extra-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(extraUser)))
            .andExpect(status().isBadRequest());

        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExtraUsers() throws Exception {
        // Initialize the database
        extraUserRepository.saveAndFlush(extraUser);

        // Get all the extraUserList
        restExtraUserMockMvc.perform(get("/api/extra-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extraUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.intValue())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())));
    }
    
    @Test
    @Transactional
    public void getExtraUser() throws Exception {
        // Initialize the database
        extraUserRepository.saveAndFlush(extraUser);

        // Get the extraUser
        restExtraUserMockMvc.perform(get("/api/extra-users/{id}", extraUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extraUser.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.intValue()))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExtraUser() throws Exception {
        // Get the extraUser
        restExtraUserMockMvc.perform(get("/api/extra-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtraUser() throws Exception {
        // Initialize the database
        extraUserRepository.saveAndFlush(extraUser);

        int databaseSizeBeforeUpdate = extraUserRepository.findAll().size();

        // Update the extraUser
        ExtraUser updatedExtraUser = extraUserRepository.findById(extraUser.getId()).get();
        // Disconnect from session so that the updates on updatedExtraUser are not directly saved in db
        em.detach(updatedExtraUser);
        updatedExtraUser
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE);

        restExtraUserMockMvc.perform(put("/api/extra-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedExtraUser)))
            .andExpect(status().isOk());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeUpdate);
        ExtraUser testExtraUser = extraUserList.get(extraUserList.size() - 1);
        assertThat(testExtraUser.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testExtraUser.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testExtraUser.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingExtraUser() throws Exception {
        int databaseSizeBeforeUpdate = extraUserRepository.findAll().size();

        // Create the ExtraUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtraUserMockMvc.perform(put("/api/extra-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(extraUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExtraUser in the database
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExtraUser() throws Exception {
        // Initialize the database
        extraUserRepository.saveAndFlush(extraUser);

        int databaseSizeBeforeDelete = extraUserRepository.findAll().size();

        // Delete the extraUser
        restExtraUserMockMvc.perform(delete("/api/extra-users/{id}", extraUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExtraUser> extraUserList = extraUserRepository.findAll();
        assertThat(extraUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
