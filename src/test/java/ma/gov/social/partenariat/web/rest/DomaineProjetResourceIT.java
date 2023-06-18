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
import ma.gov.social.partenariat.domain.DomaineProjet;
import ma.gov.social.partenariat.domain.Projet;
import ma.gov.social.partenariat.repository.DomaineProjetRepository;
import ma.gov.social.partenariat.service.criteria.DomaineProjetCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DomaineProjetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DomaineProjetResourceIT {

    private static final String DEFAULT_DESIGNATION_AR = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION_AR = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION_FR = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION_FR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/domaine-projets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DomaineProjetRepository domaineProjetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDomaineProjetMockMvc;

    private DomaineProjet domaineProjet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DomaineProjet createEntity(EntityManager em) {
        DomaineProjet domaineProjet = new DomaineProjet().designationAr(DEFAULT_DESIGNATION_AR).designationFr(DEFAULT_DESIGNATION_FR);
        return domaineProjet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DomaineProjet createUpdatedEntity(EntityManager em) {
        DomaineProjet domaineProjet = new DomaineProjet().designationAr(UPDATED_DESIGNATION_AR).designationFr(UPDATED_DESIGNATION_FR);
        return domaineProjet;
    }

    @BeforeEach
    public void initTest() {
        domaineProjet = createEntity(em);
    }

    @Test
    @Transactional
    void createDomaineProjet() throws Exception {
        int databaseSizeBeforeCreate = domaineProjetRepository.findAll().size();
        // Create the DomaineProjet
        restDomaineProjetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domaineProjet)))
            .andExpect(status().isCreated());

        // Validate the DomaineProjet in the database
        List<DomaineProjet> domaineProjetList = domaineProjetRepository.findAll();
        assertThat(domaineProjetList).hasSize(databaseSizeBeforeCreate + 1);
        DomaineProjet testDomaineProjet = domaineProjetList.get(domaineProjetList.size() - 1);
        assertThat(testDomaineProjet.getDesignationAr()).isEqualTo(DEFAULT_DESIGNATION_AR);
        assertThat(testDomaineProjet.getDesignationFr()).isEqualTo(DEFAULT_DESIGNATION_FR);
    }

    @Test
    @Transactional
    void createDomaineProjetWithExistingId() throws Exception {
        // Create the DomaineProjet with an existing ID
        domaineProjet.setId(1L);

        int databaseSizeBeforeCreate = domaineProjetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomaineProjetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domaineProjet)))
            .andExpect(status().isBadRequest());

        // Validate the DomaineProjet in the database
        List<DomaineProjet> domaineProjetList = domaineProjetRepository.findAll();
        assertThat(domaineProjetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesignationArIsRequired() throws Exception {
        int databaseSizeBeforeTest = domaineProjetRepository.findAll().size();
        // set the field null
        domaineProjet.setDesignationAr(null);

        // Create the DomaineProjet, which fails.

        restDomaineProjetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domaineProjet)))
            .andExpect(status().isBadRequest());

        List<DomaineProjet> domaineProjetList = domaineProjetRepository.findAll();
        assertThat(domaineProjetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDomaineProjets() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        // Get all the domaineProjetList
        restDomaineProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domaineProjet.getId().intValue())))
            .andExpect(jsonPath("$.[*].designationAr").value(hasItem(DEFAULT_DESIGNATION_AR)))
            .andExpect(jsonPath("$.[*].designationFr").value(hasItem(DEFAULT_DESIGNATION_FR)));
    }

    @Test
    @Transactional
    void getDomaineProjet() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        // Get the domaineProjet
        restDomaineProjetMockMvc
            .perform(get(ENTITY_API_URL_ID, domaineProjet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(domaineProjet.getId().intValue()))
            .andExpect(jsonPath("$.designationAr").value(DEFAULT_DESIGNATION_AR))
            .andExpect(jsonPath("$.designationFr").value(DEFAULT_DESIGNATION_FR));
    }

    @Test
    @Transactional
    void getDomaineProjetsByIdFiltering() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        Long id = domaineProjet.getId();

        defaultDomaineProjetShouldBeFound("id.equals=" + id);
        defaultDomaineProjetShouldNotBeFound("id.notEquals=" + id);

        defaultDomaineProjetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDomaineProjetShouldNotBeFound("id.greaterThan=" + id);

        defaultDomaineProjetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDomaineProjetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDomaineProjetsByDesignationArIsEqualToSomething() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        // Get all the domaineProjetList where designationAr equals to DEFAULT_DESIGNATION_AR
        defaultDomaineProjetShouldBeFound("designationAr.equals=" + DEFAULT_DESIGNATION_AR);

        // Get all the domaineProjetList where designationAr equals to UPDATED_DESIGNATION_AR
        defaultDomaineProjetShouldNotBeFound("designationAr.equals=" + UPDATED_DESIGNATION_AR);
    }

    @Test
    @Transactional
    void getAllDomaineProjetsByDesignationArIsInShouldWork() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        // Get all the domaineProjetList where designationAr in DEFAULT_DESIGNATION_AR or UPDATED_DESIGNATION_AR
        defaultDomaineProjetShouldBeFound("designationAr.in=" + DEFAULT_DESIGNATION_AR + "," + UPDATED_DESIGNATION_AR);

        // Get all the domaineProjetList where designationAr equals to UPDATED_DESIGNATION_AR
        defaultDomaineProjetShouldNotBeFound("designationAr.in=" + UPDATED_DESIGNATION_AR);
    }

    @Test
    @Transactional
    void getAllDomaineProjetsByDesignationArIsNullOrNotNull() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        // Get all the domaineProjetList where designationAr is not null
        defaultDomaineProjetShouldBeFound("designationAr.specified=true");

        // Get all the domaineProjetList where designationAr is null
        defaultDomaineProjetShouldNotBeFound("designationAr.specified=false");
    }

    @Test
    @Transactional
    void getAllDomaineProjetsByDesignationArContainsSomething() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        // Get all the domaineProjetList where designationAr contains DEFAULT_DESIGNATION_AR
        defaultDomaineProjetShouldBeFound("designationAr.contains=" + DEFAULT_DESIGNATION_AR);

        // Get all the domaineProjetList where designationAr contains UPDATED_DESIGNATION_AR
        defaultDomaineProjetShouldNotBeFound("designationAr.contains=" + UPDATED_DESIGNATION_AR);
    }

    @Test
    @Transactional
    void getAllDomaineProjetsByDesignationArNotContainsSomething() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        // Get all the domaineProjetList where designationAr does not contain DEFAULT_DESIGNATION_AR
        defaultDomaineProjetShouldNotBeFound("designationAr.doesNotContain=" + DEFAULT_DESIGNATION_AR);

        // Get all the domaineProjetList where designationAr does not contain UPDATED_DESIGNATION_AR
        defaultDomaineProjetShouldBeFound("designationAr.doesNotContain=" + UPDATED_DESIGNATION_AR);
    }

    @Test
    @Transactional
    void getAllDomaineProjetsByDesignationFrIsEqualToSomething() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        // Get all the domaineProjetList where designationFr equals to DEFAULT_DESIGNATION_FR
        defaultDomaineProjetShouldBeFound("designationFr.equals=" + DEFAULT_DESIGNATION_FR);

        // Get all the domaineProjetList where designationFr equals to UPDATED_DESIGNATION_FR
        defaultDomaineProjetShouldNotBeFound("designationFr.equals=" + UPDATED_DESIGNATION_FR);
    }

    @Test
    @Transactional
    void getAllDomaineProjetsByDesignationFrIsInShouldWork() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        // Get all the domaineProjetList where designationFr in DEFAULT_DESIGNATION_FR or UPDATED_DESIGNATION_FR
        defaultDomaineProjetShouldBeFound("designationFr.in=" + DEFAULT_DESIGNATION_FR + "," + UPDATED_DESIGNATION_FR);

        // Get all the domaineProjetList where designationFr equals to UPDATED_DESIGNATION_FR
        defaultDomaineProjetShouldNotBeFound("designationFr.in=" + UPDATED_DESIGNATION_FR);
    }

    @Test
    @Transactional
    void getAllDomaineProjetsByDesignationFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        // Get all the domaineProjetList where designationFr is not null
        defaultDomaineProjetShouldBeFound("designationFr.specified=true");

        // Get all the domaineProjetList where designationFr is null
        defaultDomaineProjetShouldNotBeFound("designationFr.specified=false");
    }

    @Test
    @Transactional
    void getAllDomaineProjetsByDesignationFrContainsSomething() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        // Get all the domaineProjetList where designationFr contains DEFAULT_DESIGNATION_FR
        defaultDomaineProjetShouldBeFound("designationFr.contains=" + DEFAULT_DESIGNATION_FR);

        // Get all the domaineProjetList where designationFr contains UPDATED_DESIGNATION_FR
        defaultDomaineProjetShouldNotBeFound("designationFr.contains=" + UPDATED_DESIGNATION_FR);
    }

    @Test
    @Transactional
    void getAllDomaineProjetsByDesignationFrNotContainsSomething() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        // Get all the domaineProjetList where designationFr does not contain DEFAULT_DESIGNATION_FR
        defaultDomaineProjetShouldNotBeFound("designationFr.doesNotContain=" + DEFAULT_DESIGNATION_FR);

        // Get all the domaineProjetList where designationFr does not contain UPDATED_DESIGNATION_FR
        defaultDomaineProjetShouldBeFound("designationFr.doesNotContain=" + UPDATED_DESIGNATION_FR);
    }

    @Test
    @Transactional
    void getAllDomaineProjetsByProjetIsEqualToSomething() throws Exception {
        Projet projet;
        if (TestUtil.findAll(em, Projet.class).isEmpty()) {
            domaineProjetRepository.saveAndFlush(domaineProjet);
            projet = ProjetResourceIT.createEntity(em);
        } else {
            projet = TestUtil.findAll(em, Projet.class).get(0);
        }
        em.persist(projet);
        em.flush();
        domaineProjet.addProjet(projet);
        domaineProjetRepository.saveAndFlush(domaineProjet);
        Long projetId = projet.getId();

        // Get all the domaineProjetList where projet equals to projetId
        defaultDomaineProjetShouldBeFound("projetId.equals=" + projetId);

        // Get all the domaineProjetList where projet equals to (projetId + 1)
        defaultDomaineProjetShouldNotBeFound("projetId.equals=" + (projetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDomaineProjetShouldBeFound(String filter) throws Exception {
        restDomaineProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domaineProjet.getId().intValue())))
            .andExpect(jsonPath("$.[*].designationAr").value(hasItem(DEFAULT_DESIGNATION_AR)))
            .andExpect(jsonPath("$.[*].designationFr").value(hasItem(DEFAULT_DESIGNATION_FR)));

        // Check, that the count call also returns 1
        restDomaineProjetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDomaineProjetShouldNotBeFound(String filter) throws Exception {
        restDomaineProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDomaineProjetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDomaineProjet() throws Exception {
        // Get the domaineProjet
        restDomaineProjetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDomaineProjet() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        int databaseSizeBeforeUpdate = domaineProjetRepository.findAll().size();

        // Update the domaineProjet
        DomaineProjet updatedDomaineProjet = domaineProjetRepository.findById(domaineProjet.getId()).get();
        // Disconnect from session so that the updates on updatedDomaineProjet are not directly saved in db
        em.detach(updatedDomaineProjet);
        updatedDomaineProjet.designationAr(UPDATED_DESIGNATION_AR).designationFr(UPDATED_DESIGNATION_FR);

        restDomaineProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDomaineProjet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDomaineProjet))
            )
            .andExpect(status().isOk());

        // Validate the DomaineProjet in the database
        List<DomaineProjet> domaineProjetList = domaineProjetRepository.findAll();
        assertThat(domaineProjetList).hasSize(databaseSizeBeforeUpdate);
        DomaineProjet testDomaineProjet = domaineProjetList.get(domaineProjetList.size() - 1);
        assertThat(testDomaineProjet.getDesignationAr()).isEqualTo(UPDATED_DESIGNATION_AR);
        assertThat(testDomaineProjet.getDesignationFr()).isEqualTo(UPDATED_DESIGNATION_FR);
    }

    @Test
    @Transactional
    void putNonExistingDomaineProjet() throws Exception {
        int databaseSizeBeforeUpdate = domaineProjetRepository.findAll().size();
        domaineProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomaineProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, domaineProjet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domaineProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the DomaineProjet in the database
        List<DomaineProjet> domaineProjetList = domaineProjetRepository.findAll();
        assertThat(domaineProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDomaineProjet() throws Exception {
        int databaseSizeBeforeUpdate = domaineProjetRepository.findAll().size();
        domaineProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domaineProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the DomaineProjet in the database
        List<DomaineProjet> domaineProjetList = domaineProjetRepository.findAll();
        assertThat(domaineProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDomaineProjet() throws Exception {
        int databaseSizeBeforeUpdate = domaineProjetRepository.findAll().size();
        domaineProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineProjetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domaineProjet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DomaineProjet in the database
        List<DomaineProjet> domaineProjetList = domaineProjetRepository.findAll();
        assertThat(domaineProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDomaineProjetWithPatch() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        int databaseSizeBeforeUpdate = domaineProjetRepository.findAll().size();

        // Update the domaineProjet using partial update
        DomaineProjet partialUpdatedDomaineProjet = new DomaineProjet();
        partialUpdatedDomaineProjet.setId(domaineProjet.getId());

        partialUpdatedDomaineProjet.designationAr(UPDATED_DESIGNATION_AR);

        restDomaineProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomaineProjet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomaineProjet))
            )
            .andExpect(status().isOk());

        // Validate the DomaineProjet in the database
        List<DomaineProjet> domaineProjetList = domaineProjetRepository.findAll();
        assertThat(domaineProjetList).hasSize(databaseSizeBeforeUpdate);
        DomaineProjet testDomaineProjet = domaineProjetList.get(domaineProjetList.size() - 1);
        assertThat(testDomaineProjet.getDesignationAr()).isEqualTo(UPDATED_DESIGNATION_AR);
        assertThat(testDomaineProjet.getDesignationFr()).isEqualTo(DEFAULT_DESIGNATION_FR);
    }

    @Test
    @Transactional
    void fullUpdateDomaineProjetWithPatch() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        int databaseSizeBeforeUpdate = domaineProjetRepository.findAll().size();

        // Update the domaineProjet using partial update
        DomaineProjet partialUpdatedDomaineProjet = new DomaineProjet();
        partialUpdatedDomaineProjet.setId(domaineProjet.getId());

        partialUpdatedDomaineProjet.designationAr(UPDATED_DESIGNATION_AR).designationFr(UPDATED_DESIGNATION_FR);

        restDomaineProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomaineProjet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomaineProjet))
            )
            .andExpect(status().isOk());

        // Validate the DomaineProjet in the database
        List<DomaineProjet> domaineProjetList = domaineProjetRepository.findAll();
        assertThat(domaineProjetList).hasSize(databaseSizeBeforeUpdate);
        DomaineProjet testDomaineProjet = domaineProjetList.get(domaineProjetList.size() - 1);
        assertThat(testDomaineProjet.getDesignationAr()).isEqualTo(UPDATED_DESIGNATION_AR);
        assertThat(testDomaineProjet.getDesignationFr()).isEqualTo(UPDATED_DESIGNATION_FR);
    }

    @Test
    @Transactional
    void patchNonExistingDomaineProjet() throws Exception {
        int databaseSizeBeforeUpdate = domaineProjetRepository.findAll().size();
        domaineProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomaineProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, domaineProjet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domaineProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the DomaineProjet in the database
        List<DomaineProjet> domaineProjetList = domaineProjetRepository.findAll();
        assertThat(domaineProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDomaineProjet() throws Exception {
        int databaseSizeBeforeUpdate = domaineProjetRepository.findAll().size();
        domaineProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domaineProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the DomaineProjet in the database
        List<DomaineProjet> domaineProjetList = domaineProjetRepository.findAll();
        assertThat(domaineProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDomaineProjet() throws Exception {
        int databaseSizeBeforeUpdate = domaineProjetRepository.findAll().size();
        domaineProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineProjetMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(domaineProjet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DomaineProjet in the database
        List<DomaineProjet> domaineProjetList = domaineProjetRepository.findAll();
        assertThat(domaineProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDomaineProjet() throws Exception {
        // Initialize the database
        domaineProjetRepository.saveAndFlush(domaineProjet);

        int databaseSizeBeforeDelete = domaineProjetRepository.findAll().size();

        // Delete the domaineProjet
        restDomaineProjetMockMvc
            .perform(delete(ENTITY_API_URL_ID, domaineProjet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DomaineProjet> domaineProjetList = domaineProjetRepository.findAll();
        assertThat(domaineProjetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
