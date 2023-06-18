package ma.gov.social.partenariat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ma.gov.social.partenariat.IntegrationTest;
import ma.gov.social.partenariat.domain.Convention;
import ma.gov.social.partenariat.domain.Partenaire;
import ma.gov.social.partenariat.repository.PartenaireRepository;
import ma.gov.social.partenariat.service.criteria.PartenaireCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PartenaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartenaireResourceIT {

    private static final String DEFAULT_NOM_PARTENAIRE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PARTENAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/partenaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartenaireRepository partenaireRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartenaireMockMvc;

    private Partenaire partenaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partenaire createEntity(EntityManager em) {
        Partenaire partenaire = new Partenaire().nomPartenaire(DEFAULT_NOM_PARTENAIRE).tel(DEFAULT_TEL).email(DEFAULT_EMAIL);
        return partenaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partenaire createUpdatedEntity(EntityManager em) {
        Partenaire partenaire = new Partenaire().nomPartenaire(UPDATED_NOM_PARTENAIRE).tel(UPDATED_TEL).email(UPDATED_EMAIL);
        return partenaire;
    }

    @BeforeEach
    public void initTest() {
        partenaire = createEntity(em);
    }

    @Test
    @Transactional
    void createPartenaire() throws Exception {
        int databaseSizeBeforeCreate = partenaireRepository.findAll().size();
        // Create the Partenaire
        restPartenaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partenaire)))
            .andExpect(status().isCreated());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeCreate + 1);
        Partenaire testPartenaire = partenaireList.get(partenaireList.size() - 1);
        assertThat(testPartenaire.getNomPartenaire()).isEqualTo(DEFAULT_NOM_PARTENAIRE);
        assertThat(testPartenaire.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testPartenaire.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createPartenaireWithExistingId() throws Exception {
        // Create the Partenaire with an existing ID
        partenaire.setId(1L);

        int databaseSizeBeforeCreate = partenaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartenaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partenaire)))
            .andExpect(status().isBadRequest());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPartenaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = partenaireRepository.findAll().size();
        // set the field null
        partenaire.setNomPartenaire(null);

        // Create the Partenaire, which fails.

        restPartenaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partenaire)))
            .andExpect(status().isBadRequest());

        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelIsRequired() throws Exception {
        int databaseSizeBeforeTest = partenaireRepository.findAll().size();
        // set the field null
        partenaire.setTel(null);

        // Create the Partenaire, which fails.

        restPartenaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partenaire)))
            .andExpect(status().isBadRequest());

        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = partenaireRepository.findAll().size();
        // set the field null
        partenaire.setEmail(null);

        // Create the Partenaire, which fails.

        restPartenaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partenaire)))
            .andExpect(status().isBadRequest());

        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartenaires() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList
        restPartenaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partenaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPartenaire").value(hasItem(DEFAULT_NOM_PARTENAIRE)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getPartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get the partenaire
        restPartenaireMockMvc
            .perform(get(ENTITY_API_URL_ID, partenaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partenaire.getId().intValue()))
            .andExpect(jsonPath("$.nomPartenaire").value(DEFAULT_NOM_PARTENAIRE))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getPartenairesByIdFiltering() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        Long id = partenaire.getId();

        defaultPartenaireShouldBeFound("id.equals=" + id);
        defaultPartenaireShouldNotBeFound("id.notEquals=" + id);

        defaultPartenaireShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPartenaireShouldNotBeFound("id.greaterThan=" + id);

        defaultPartenaireShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPartenaireShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartenairesByNomPartenaireIsEqualToSomething() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where nomPartenaire equals to DEFAULT_NOM_PARTENAIRE
        defaultPartenaireShouldBeFound("nomPartenaire.equals=" + DEFAULT_NOM_PARTENAIRE);

        // Get all the partenaireList where nomPartenaire equals to UPDATED_NOM_PARTENAIRE
        defaultPartenaireShouldNotBeFound("nomPartenaire.equals=" + UPDATED_NOM_PARTENAIRE);
    }

    @Test
    @Transactional
    void getAllPartenairesByNomPartenaireIsInShouldWork() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where nomPartenaire in DEFAULT_NOM_PARTENAIRE or UPDATED_NOM_PARTENAIRE
        defaultPartenaireShouldBeFound("nomPartenaire.in=" + DEFAULT_NOM_PARTENAIRE + "," + UPDATED_NOM_PARTENAIRE);

        // Get all the partenaireList where nomPartenaire equals to UPDATED_NOM_PARTENAIRE
        defaultPartenaireShouldNotBeFound("nomPartenaire.in=" + UPDATED_NOM_PARTENAIRE);
    }

    @Test
    @Transactional
    void getAllPartenairesByNomPartenaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where nomPartenaire is not null
        defaultPartenaireShouldBeFound("nomPartenaire.specified=true");

        // Get all the partenaireList where nomPartenaire is null
        defaultPartenaireShouldNotBeFound("nomPartenaire.specified=false");
    }

    @Test
    @Transactional
    void getAllPartenairesByNomPartenaireContainsSomething() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where nomPartenaire contains DEFAULT_NOM_PARTENAIRE
        defaultPartenaireShouldBeFound("nomPartenaire.contains=" + DEFAULT_NOM_PARTENAIRE);

        // Get all the partenaireList where nomPartenaire contains UPDATED_NOM_PARTENAIRE
        defaultPartenaireShouldNotBeFound("nomPartenaire.contains=" + UPDATED_NOM_PARTENAIRE);
    }

    @Test
    @Transactional
    void getAllPartenairesByNomPartenaireNotContainsSomething() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where nomPartenaire does not contain DEFAULT_NOM_PARTENAIRE
        defaultPartenaireShouldNotBeFound("nomPartenaire.doesNotContain=" + DEFAULT_NOM_PARTENAIRE);

        // Get all the partenaireList where nomPartenaire does not contain UPDATED_NOM_PARTENAIRE
        defaultPartenaireShouldBeFound("nomPartenaire.doesNotContain=" + UPDATED_NOM_PARTENAIRE);
    }

    @Test
    @Transactional
    void getAllPartenairesByTelIsEqualToSomething() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where tel equals to DEFAULT_TEL
        defaultPartenaireShouldBeFound("tel.equals=" + DEFAULT_TEL);

        // Get all the partenaireList where tel equals to UPDATED_TEL
        defaultPartenaireShouldNotBeFound("tel.equals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllPartenairesByTelIsInShouldWork() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where tel in DEFAULT_TEL or UPDATED_TEL
        defaultPartenaireShouldBeFound("tel.in=" + DEFAULT_TEL + "," + UPDATED_TEL);

        // Get all the partenaireList where tel equals to UPDATED_TEL
        defaultPartenaireShouldNotBeFound("tel.in=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllPartenairesByTelIsNullOrNotNull() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where tel is not null
        defaultPartenaireShouldBeFound("tel.specified=true");

        // Get all the partenaireList where tel is null
        defaultPartenaireShouldNotBeFound("tel.specified=false");
    }

    @Test
    @Transactional
    void getAllPartenairesByTelContainsSomething() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where tel contains DEFAULT_TEL
        defaultPartenaireShouldBeFound("tel.contains=" + DEFAULT_TEL);

        // Get all the partenaireList where tel contains UPDATED_TEL
        defaultPartenaireShouldNotBeFound("tel.contains=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllPartenairesByTelNotContainsSomething() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where tel does not contain DEFAULT_TEL
        defaultPartenaireShouldNotBeFound("tel.doesNotContain=" + DEFAULT_TEL);

        // Get all the partenaireList where tel does not contain UPDATED_TEL
        defaultPartenaireShouldBeFound("tel.doesNotContain=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllPartenairesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where email equals to DEFAULT_EMAIL
        defaultPartenaireShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the partenaireList where email equals to UPDATED_EMAIL
        defaultPartenaireShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPartenairesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPartenaireShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the partenaireList where email equals to UPDATED_EMAIL
        defaultPartenaireShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPartenairesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where email is not null
        defaultPartenaireShouldBeFound("email.specified=true");

        // Get all the partenaireList where email is null
        defaultPartenaireShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllPartenairesByEmailContainsSomething() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where email contains DEFAULT_EMAIL
        defaultPartenaireShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the partenaireList where email contains UPDATED_EMAIL
        defaultPartenaireShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPartenairesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList where email does not contain DEFAULT_EMAIL
        defaultPartenaireShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the partenaireList where email does not contain UPDATED_EMAIL
        defaultPartenaireShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPartenairesByConventionIsEqualToSomething() throws Exception {
        Convention convention;
        if (TestUtil.findAll(em, Convention.class).isEmpty()) {
            partenaireRepository.saveAndFlush(partenaire);
            convention = ConventionResourceIT.createEntity(em);
        } else {
            convention = TestUtil.findAll(em, Convention.class).get(0);
        }
        em.persist(convention);
        em.flush();
        partenaire.addConvention(convention);
        partenaireRepository.saveAndFlush(partenaire);
        Long conventionId = convention.getId();

        // Get all the partenaireList where convention equals to conventionId
        defaultPartenaireShouldBeFound("conventionId.equals=" + conventionId);

        // Get all the partenaireList where convention equals to (conventionId + 1)
        defaultPartenaireShouldNotBeFound("conventionId.equals=" + (conventionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartenaireShouldBeFound(String filter) throws Exception {
        restPartenaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partenaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPartenaire").value(hasItem(DEFAULT_NOM_PARTENAIRE)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restPartenaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartenaireShouldNotBeFound(String filter) throws Exception {
        restPartenaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartenaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartenaire() throws Exception {
        // Get the partenaire
        restPartenaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();

        // Update the partenaire
        Partenaire updatedPartenaire = partenaireRepository.findById(partenaire.getId()).get();
        // Disconnect from session so that the updates on updatedPartenaire are not directly saved in db
        em.detach(updatedPartenaire);
        updatedPartenaire.nomPartenaire(UPDATED_NOM_PARTENAIRE).tel(UPDATED_TEL).email(UPDATED_EMAIL);

        restPartenaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartenaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPartenaire))
            )
            .andExpect(status().isOk());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeUpdate);
        Partenaire testPartenaire = partenaireList.get(partenaireList.size() - 1);
        assertThat(testPartenaire.getNomPartenaire()).isEqualTo(UPDATED_NOM_PARTENAIRE);
        assertThat(testPartenaire.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testPartenaire.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingPartenaire() throws Exception {
        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();
        partenaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartenaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partenaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partenaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartenaire() throws Exception {
        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();
        partenaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partenaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartenaire() throws Exception {
        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();
        partenaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partenaire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartenaireWithPatch() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();

        // Update the partenaire using partial update
        Partenaire partialUpdatedPartenaire = new Partenaire();
        partialUpdatedPartenaire.setId(partenaire.getId());

        partialUpdatedPartenaire.tel(UPDATED_TEL);

        restPartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartenaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartenaire))
            )
            .andExpect(status().isOk());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeUpdate);
        Partenaire testPartenaire = partenaireList.get(partenaireList.size() - 1);
        assertThat(testPartenaire.getNomPartenaire()).isEqualTo(DEFAULT_NOM_PARTENAIRE);
        assertThat(testPartenaire.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testPartenaire.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdatePartenaireWithPatch() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();

        // Update the partenaire using partial update
        Partenaire partialUpdatedPartenaire = new Partenaire();
        partialUpdatedPartenaire.setId(partenaire.getId());

        partialUpdatedPartenaire.nomPartenaire(UPDATED_NOM_PARTENAIRE).tel(UPDATED_TEL).email(UPDATED_EMAIL);

        restPartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartenaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartenaire))
            )
            .andExpect(status().isOk());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeUpdate);
        Partenaire testPartenaire = partenaireList.get(partenaireList.size() - 1);
        assertThat(testPartenaire.getNomPartenaire()).isEqualTo(UPDATED_NOM_PARTENAIRE);
        assertThat(testPartenaire.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testPartenaire.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingPartenaire() throws Exception {
        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();
        partenaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partenaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partenaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartenaire() throws Exception {
        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();
        partenaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partenaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartenaire() throws Exception {
        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();
        partenaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(partenaire))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        int databaseSizeBeforeDelete = partenaireRepository.findAll().size();

        // Delete the partenaire
        restPartenaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, partenaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
