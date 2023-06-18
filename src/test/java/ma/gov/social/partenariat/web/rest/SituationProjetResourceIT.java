package ma.gov.social.partenariat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ma.gov.social.partenariat.IntegrationTest;
import ma.gov.social.partenariat.domain.Projet;
import ma.gov.social.partenariat.domain.SituationProjet;
import ma.gov.social.partenariat.repository.SituationProjetRepository;
import ma.gov.social.partenariat.service.criteria.SituationProjetCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SituationProjetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SituationProjetResourceIT {

    private static final LocalDate DEFAULT_DATE_STATUT_VALID = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_STATUT_VALID = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_STATUT_VALID = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_STATUT_PROJET = "AAAAAAAAAA";
    private static final String UPDATED_STATUT_PROJET = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/situation-projets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SituationProjetRepository situationProjetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSituationProjetMockMvc;

    private SituationProjet situationProjet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SituationProjet createEntity(EntityManager em) {
        SituationProjet situationProjet = new SituationProjet()
            .dateStatutValid(DEFAULT_DATE_STATUT_VALID)
            .statutProjet(DEFAULT_STATUT_PROJET);
        // Add required entity
        Projet projet;
        if (TestUtil.findAll(em, Projet.class).isEmpty()) {
            projet = ProjetResourceIT.createEntity(em);
            em.persist(projet);
            em.flush();
        } else {
            projet = TestUtil.findAll(em, Projet.class).get(0);
        }
        situationProjet.setProjet(projet);
        return situationProjet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SituationProjet createUpdatedEntity(EntityManager em) {
        SituationProjet situationProjet = new SituationProjet()
            .dateStatutValid(UPDATED_DATE_STATUT_VALID)
            .statutProjet(UPDATED_STATUT_PROJET);
        // Add required entity
        Projet projet;
        if (TestUtil.findAll(em, Projet.class).isEmpty()) {
            projet = ProjetResourceIT.createUpdatedEntity(em);
            em.persist(projet);
            em.flush();
        } else {
            projet = TestUtil.findAll(em, Projet.class).get(0);
        }
        situationProjet.setProjet(projet);
        return situationProjet;
    }

    @BeforeEach
    public void initTest() {
        situationProjet = createEntity(em);
    }

    @Test
    @Transactional
    void createSituationProjet() throws Exception {
        int databaseSizeBeforeCreate = situationProjetRepository.findAll().size();
        // Create the SituationProjet
        restSituationProjetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(situationProjet))
            )
            .andExpect(status().isCreated());

        // Validate the SituationProjet in the database
        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeCreate + 1);
        SituationProjet testSituationProjet = situationProjetList.get(situationProjetList.size() - 1);
        assertThat(testSituationProjet.getDateStatutValid()).isEqualTo(DEFAULT_DATE_STATUT_VALID);
        assertThat(testSituationProjet.getStatutProjet()).isEqualTo(DEFAULT_STATUT_PROJET);
    }

    @Test
    @Transactional
    void createSituationProjetWithExistingId() throws Exception {
        // Create the SituationProjet with an existing ID
        situationProjet.setId(1L);

        int databaseSizeBeforeCreate = situationProjetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSituationProjetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(situationProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SituationProjet in the database
        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateStatutValidIsRequired() throws Exception {
        int databaseSizeBeforeTest = situationProjetRepository.findAll().size();
        // set the field null
        situationProjet.setDateStatutValid(null);

        // Create the SituationProjet, which fails.

        restSituationProjetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(situationProjet))
            )
            .andExpect(status().isBadRequest());

        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutProjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = situationProjetRepository.findAll().size();
        // set the field null
        situationProjet.setStatutProjet(null);

        // Create the SituationProjet, which fails.

        restSituationProjetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(situationProjet))
            )
            .andExpect(status().isBadRequest());

        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSituationProjets() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get all the situationProjetList
        restSituationProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(situationProjet.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateStatutValid").value(hasItem(DEFAULT_DATE_STATUT_VALID.toString())))
            .andExpect(jsonPath("$.[*].statutProjet").value(hasItem(DEFAULT_STATUT_PROJET)));
    }

    @Test
    @Transactional
    void getSituationProjet() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get the situationProjet
        restSituationProjetMockMvc
            .perform(get(ENTITY_API_URL_ID, situationProjet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(situationProjet.getId().intValue()))
            .andExpect(jsonPath("$.dateStatutValid").value(DEFAULT_DATE_STATUT_VALID.toString()))
            .andExpect(jsonPath("$.statutProjet").value(DEFAULT_STATUT_PROJET));
    }

    @Test
    @Transactional
    void getSituationProjetsByIdFiltering() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        Long id = situationProjet.getId();

        defaultSituationProjetShouldBeFound("id.equals=" + id);
        defaultSituationProjetShouldNotBeFound("id.notEquals=" + id);

        defaultSituationProjetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSituationProjetShouldNotBeFound("id.greaterThan=" + id);

        defaultSituationProjetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSituationProjetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSituationProjetsByDateStatutValidIsEqualToSomething() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get all the situationProjetList where dateStatutValid equals to DEFAULT_DATE_STATUT_VALID
        defaultSituationProjetShouldBeFound("dateStatutValid.equals=" + DEFAULT_DATE_STATUT_VALID);

        // Get all the situationProjetList where dateStatutValid equals to UPDATED_DATE_STATUT_VALID
        defaultSituationProjetShouldNotBeFound("dateStatutValid.equals=" + UPDATED_DATE_STATUT_VALID);
    }

    @Test
    @Transactional
    void getAllSituationProjetsByDateStatutValidIsInShouldWork() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get all the situationProjetList where dateStatutValid in DEFAULT_DATE_STATUT_VALID or UPDATED_DATE_STATUT_VALID
        defaultSituationProjetShouldBeFound("dateStatutValid.in=" + DEFAULT_DATE_STATUT_VALID + "," + UPDATED_DATE_STATUT_VALID);

        // Get all the situationProjetList where dateStatutValid equals to UPDATED_DATE_STATUT_VALID
        defaultSituationProjetShouldNotBeFound("dateStatutValid.in=" + UPDATED_DATE_STATUT_VALID);
    }

    @Test
    @Transactional
    void getAllSituationProjetsByDateStatutValidIsNullOrNotNull() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get all the situationProjetList where dateStatutValid is not null
        defaultSituationProjetShouldBeFound("dateStatutValid.specified=true");

        // Get all the situationProjetList where dateStatutValid is null
        defaultSituationProjetShouldNotBeFound("dateStatutValid.specified=false");
    }

    @Test
    @Transactional
    void getAllSituationProjetsByDateStatutValidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get all the situationProjetList where dateStatutValid is greater than or equal to DEFAULT_DATE_STATUT_VALID
        defaultSituationProjetShouldBeFound("dateStatutValid.greaterThanOrEqual=" + DEFAULT_DATE_STATUT_VALID);

        // Get all the situationProjetList where dateStatutValid is greater than or equal to UPDATED_DATE_STATUT_VALID
        defaultSituationProjetShouldNotBeFound("dateStatutValid.greaterThanOrEqual=" + UPDATED_DATE_STATUT_VALID);
    }

    @Test
    @Transactional
    void getAllSituationProjetsByDateStatutValidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get all the situationProjetList where dateStatutValid is less than or equal to DEFAULT_DATE_STATUT_VALID
        defaultSituationProjetShouldBeFound("dateStatutValid.lessThanOrEqual=" + DEFAULT_DATE_STATUT_VALID);

        // Get all the situationProjetList where dateStatutValid is less than or equal to SMALLER_DATE_STATUT_VALID
        defaultSituationProjetShouldNotBeFound("dateStatutValid.lessThanOrEqual=" + SMALLER_DATE_STATUT_VALID);
    }

    @Test
    @Transactional
    void getAllSituationProjetsByDateStatutValidIsLessThanSomething() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get all the situationProjetList where dateStatutValid is less than DEFAULT_DATE_STATUT_VALID
        defaultSituationProjetShouldNotBeFound("dateStatutValid.lessThan=" + DEFAULT_DATE_STATUT_VALID);

        // Get all the situationProjetList where dateStatutValid is less than UPDATED_DATE_STATUT_VALID
        defaultSituationProjetShouldBeFound("dateStatutValid.lessThan=" + UPDATED_DATE_STATUT_VALID);
    }

    @Test
    @Transactional
    void getAllSituationProjetsByDateStatutValidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get all the situationProjetList where dateStatutValid is greater than DEFAULT_DATE_STATUT_VALID
        defaultSituationProjetShouldNotBeFound("dateStatutValid.greaterThan=" + DEFAULT_DATE_STATUT_VALID);

        // Get all the situationProjetList where dateStatutValid is greater than SMALLER_DATE_STATUT_VALID
        defaultSituationProjetShouldBeFound("dateStatutValid.greaterThan=" + SMALLER_DATE_STATUT_VALID);
    }

    @Test
    @Transactional
    void getAllSituationProjetsByStatutProjetIsEqualToSomething() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get all the situationProjetList where statutProjet equals to DEFAULT_STATUT_PROJET
        defaultSituationProjetShouldBeFound("statutProjet.equals=" + DEFAULT_STATUT_PROJET);

        // Get all the situationProjetList where statutProjet equals to UPDATED_STATUT_PROJET
        defaultSituationProjetShouldNotBeFound("statutProjet.equals=" + UPDATED_STATUT_PROJET);
    }

    @Test
    @Transactional
    void getAllSituationProjetsByStatutProjetIsInShouldWork() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get all the situationProjetList where statutProjet in DEFAULT_STATUT_PROJET or UPDATED_STATUT_PROJET
        defaultSituationProjetShouldBeFound("statutProjet.in=" + DEFAULT_STATUT_PROJET + "," + UPDATED_STATUT_PROJET);

        // Get all the situationProjetList where statutProjet equals to UPDATED_STATUT_PROJET
        defaultSituationProjetShouldNotBeFound("statutProjet.in=" + UPDATED_STATUT_PROJET);
    }

    @Test
    @Transactional
    void getAllSituationProjetsByStatutProjetIsNullOrNotNull() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get all the situationProjetList where statutProjet is not null
        defaultSituationProjetShouldBeFound("statutProjet.specified=true");

        // Get all the situationProjetList where statutProjet is null
        defaultSituationProjetShouldNotBeFound("statutProjet.specified=false");
    }

    @Test
    @Transactional
    void getAllSituationProjetsByStatutProjetContainsSomething() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get all the situationProjetList where statutProjet contains DEFAULT_STATUT_PROJET
        defaultSituationProjetShouldBeFound("statutProjet.contains=" + DEFAULT_STATUT_PROJET);

        // Get all the situationProjetList where statutProjet contains UPDATED_STATUT_PROJET
        defaultSituationProjetShouldNotBeFound("statutProjet.contains=" + UPDATED_STATUT_PROJET);
    }

    @Test
    @Transactional
    void getAllSituationProjetsByStatutProjetNotContainsSomething() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        // Get all the situationProjetList where statutProjet does not contain DEFAULT_STATUT_PROJET
        defaultSituationProjetShouldNotBeFound("statutProjet.doesNotContain=" + DEFAULT_STATUT_PROJET);

        // Get all the situationProjetList where statutProjet does not contain UPDATED_STATUT_PROJET
        defaultSituationProjetShouldBeFound("statutProjet.doesNotContain=" + UPDATED_STATUT_PROJET);
    }

    @Test
    @Transactional
    void getAllSituationProjetsByProjetIsEqualToSomething() throws Exception {
        Projet projet;
        if (TestUtil.findAll(em, Projet.class).isEmpty()) {
            situationProjetRepository.saveAndFlush(situationProjet);
            projet = ProjetResourceIT.createEntity(em);
        } else {
            projet = TestUtil.findAll(em, Projet.class).get(0);
        }
        em.persist(projet);
        em.flush();
        situationProjet.setProjet(projet);
        situationProjetRepository.saveAndFlush(situationProjet);
        Long projetId = projet.getId();

        // Get all the situationProjetList where projet equals to projetId
        defaultSituationProjetShouldBeFound("projetId.equals=" + projetId);

        // Get all the situationProjetList where projet equals to (projetId + 1)
        defaultSituationProjetShouldNotBeFound("projetId.equals=" + (projetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSituationProjetShouldBeFound(String filter) throws Exception {
        restSituationProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(situationProjet.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateStatutValid").value(hasItem(DEFAULT_DATE_STATUT_VALID.toString())))
            .andExpect(jsonPath("$.[*].statutProjet").value(hasItem(DEFAULT_STATUT_PROJET)));

        // Check, that the count call also returns 1
        restSituationProjetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSituationProjetShouldNotBeFound(String filter) throws Exception {
        restSituationProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSituationProjetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSituationProjet() throws Exception {
        // Get the situationProjet
        restSituationProjetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSituationProjet() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        int databaseSizeBeforeUpdate = situationProjetRepository.findAll().size();

        // Update the situationProjet
        SituationProjet updatedSituationProjet = situationProjetRepository.findById(situationProjet.getId()).get();
        // Disconnect from session so that the updates on updatedSituationProjet are not directly saved in db
        em.detach(updatedSituationProjet);
        updatedSituationProjet.dateStatutValid(UPDATED_DATE_STATUT_VALID).statutProjet(UPDATED_STATUT_PROJET);

        restSituationProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSituationProjet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSituationProjet))
            )
            .andExpect(status().isOk());

        // Validate the SituationProjet in the database
        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeUpdate);
        SituationProjet testSituationProjet = situationProjetList.get(situationProjetList.size() - 1);
        assertThat(testSituationProjet.getDateStatutValid()).isEqualTo(UPDATED_DATE_STATUT_VALID);
        assertThat(testSituationProjet.getStatutProjet()).isEqualTo(UPDATED_STATUT_PROJET);
    }

    @Test
    @Transactional
    void putNonExistingSituationProjet() throws Exception {
        int databaseSizeBeforeUpdate = situationProjetRepository.findAll().size();
        situationProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSituationProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, situationProjet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(situationProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SituationProjet in the database
        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSituationProjet() throws Exception {
        int databaseSizeBeforeUpdate = situationProjetRepository.findAll().size();
        situationProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSituationProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(situationProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SituationProjet in the database
        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSituationProjet() throws Exception {
        int databaseSizeBeforeUpdate = situationProjetRepository.findAll().size();
        situationProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSituationProjetMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(situationProjet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SituationProjet in the database
        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSituationProjetWithPatch() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        int databaseSizeBeforeUpdate = situationProjetRepository.findAll().size();

        // Update the situationProjet using partial update
        SituationProjet partialUpdatedSituationProjet = new SituationProjet();
        partialUpdatedSituationProjet.setId(situationProjet.getId());

        partialUpdatedSituationProjet.statutProjet(UPDATED_STATUT_PROJET);

        restSituationProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSituationProjet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSituationProjet))
            )
            .andExpect(status().isOk());

        // Validate the SituationProjet in the database
        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeUpdate);
        SituationProjet testSituationProjet = situationProjetList.get(situationProjetList.size() - 1);
        assertThat(testSituationProjet.getDateStatutValid()).isEqualTo(DEFAULT_DATE_STATUT_VALID);
        assertThat(testSituationProjet.getStatutProjet()).isEqualTo(UPDATED_STATUT_PROJET);
    }

    @Test
    @Transactional
    void fullUpdateSituationProjetWithPatch() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        int databaseSizeBeforeUpdate = situationProjetRepository.findAll().size();

        // Update the situationProjet using partial update
        SituationProjet partialUpdatedSituationProjet = new SituationProjet();
        partialUpdatedSituationProjet.setId(situationProjet.getId());

        partialUpdatedSituationProjet.dateStatutValid(UPDATED_DATE_STATUT_VALID).statutProjet(UPDATED_STATUT_PROJET);

        restSituationProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSituationProjet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSituationProjet))
            )
            .andExpect(status().isOk());

        // Validate the SituationProjet in the database
        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeUpdate);
        SituationProjet testSituationProjet = situationProjetList.get(situationProjetList.size() - 1);
        assertThat(testSituationProjet.getDateStatutValid()).isEqualTo(UPDATED_DATE_STATUT_VALID);
        assertThat(testSituationProjet.getStatutProjet()).isEqualTo(UPDATED_STATUT_PROJET);
    }

    @Test
    @Transactional
    void patchNonExistingSituationProjet() throws Exception {
        int databaseSizeBeforeUpdate = situationProjetRepository.findAll().size();
        situationProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSituationProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, situationProjet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(situationProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SituationProjet in the database
        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSituationProjet() throws Exception {
        int databaseSizeBeforeUpdate = situationProjetRepository.findAll().size();
        situationProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSituationProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(situationProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SituationProjet in the database
        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSituationProjet() throws Exception {
        int databaseSizeBeforeUpdate = situationProjetRepository.findAll().size();
        situationProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSituationProjetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(situationProjet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SituationProjet in the database
        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSituationProjet() throws Exception {
        // Initialize the database
        situationProjetRepository.saveAndFlush(situationProjet);

        int databaseSizeBeforeDelete = situationProjetRepository.findAll().size();

        // Delete the situationProjet
        restSituationProjetMockMvc
            .perform(delete(ENTITY_API_URL_ID, situationProjet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SituationProjet> situationProjetList = situationProjetRepository.findAll();
        assertThat(situationProjetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
