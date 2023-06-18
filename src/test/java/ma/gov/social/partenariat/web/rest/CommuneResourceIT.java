package ma.gov.social.partenariat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ma.gov.social.partenariat.IntegrationTest;
import ma.gov.social.partenariat.domain.Commune;
import ma.gov.social.partenariat.domain.Projet;
import ma.gov.social.partenariat.domain.Province;
import ma.gov.social.partenariat.repository.CommuneRepository;
import ma.gov.social.partenariat.service.CommuneService;
import ma.gov.social.partenariat.service.criteria.CommuneCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CommuneResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommuneResourceIT {

    private static final String DEFAULT_NOM_COMMUNE_AR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_COMMUNE_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_COMMUNE_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_COMMUNE_FR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/communes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommuneRepository communeRepository;

    @Mock
    private CommuneRepository communeRepositoryMock;

    @Mock
    private CommuneService communeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommuneMockMvc;

    private Commune commune;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commune createEntity(EntityManager em) {
        Commune commune = new Commune().nomCommuneAr(DEFAULT_NOM_COMMUNE_AR).nomCommuneFr(DEFAULT_NOM_COMMUNE_FR);
        return commune;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commune createUpdatedEntity(EntityManager em) {
        Commune commune = new Commune().nomCommuneAr(UPDATED_NOM_COMMUNE_AR).nomCommuneFr(UPDATED_NOM_COMMUNE_FR);
        return commune;
    }

    @BeforeEach
    public void initTest() {
        commune = createEntity(em);
    }

    @Test
    @Transactional
    void createCommune() throws Exception {
        int databaseSizeBeforeCreate = communeRepository.findAll().size();
        // Create the Commune
        restCommuneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commune)))
            .andExpect(status().isCreated());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeCreate + 1);
        Commune testCommune = communeList.get(communeList.size() - 1);
        assertThat(testCommune.getNomCommuneAr()).isEqualTo(DEFAULT_NOM_COMMUNE_AR);
        assertThat(testCommune.getNomCommuneFr()).isEqualTo(DEFAULT_NOM_COMMUNE_FR);
    }

    @Test
    @Transactional
    void createCommuneWithExistingId() throws Exception {
        // Create the Commune with an existing ID
        commune.setId(1L);

        int databaseSizeBeforeCreate = communeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommuneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commune)))
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomCommuneArIsRequired() throws Exception {
        int databaseSizeBeforeTest = communeRepository.findAll().size();
        // set the field null
        commune.setNomCommuneAr(null);

        // Create the Commune, which fails.

        restCommuneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commune)))
            .andExpect(status().isBadRequest());

        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommunes() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communeList
        restCommuneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commune.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomCommuneAr").value(hasItem(DEFAULT_NOM_COMMUNE_AR)))
            .andExpect(jsonPath("$.[*].nomCommuneFr").value(hasItem(DEFAULT_NOM_COMMUNE_FR)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommunesWithEagerRelationshipsIsEnabled() throws Exception {
        when(communeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommuneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(communeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommunesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(communeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommuneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(communeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCommune() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get the commune
        restCommuneMockMvc
            .perform(get(ENTITY_API_URL_ID, commune.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commune.getId().intValue()))
            .andExpect(jsonPath("$.nomCommuneAr").value(DEFAULT_NOM_COMMUNE_AR))
            .andExpect(jsonPath("$.nomCommuneFr").value(DEFAULT_NOM_COMMUNE_FR));
    }

    @Test
    @Transactional
    void getCommunesByIdFiltering() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        Long id = commune.getId();

        defaultCommuneShouldBeFound("id.equals=" + id);
        defaultCommuneShouldNotBeFound("id.notEquals=" + id);

        defaultCommuneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommuneShouldNotBeFound("id.greaterThan=" + id);

        defaultCommuneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommuneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommunesByNomCommuneArIsEqualToSomething() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communeList where nomCommuneAr equals to DEFAULT_NOM_COMMUNE_AR
        defaultCommuneShouldBeFound("nomCommuneAr.equals=" + DEFAULT_NOM_COMMUNE_AR);

        // Get all the communeList where nomCommuneAr equals to UPDATED_NOM_COMMUNE_AR
        defaultCommuneShouldNotBeFound("nomCommuneAr.equals=" + UPDATED_NOM_COMMUNE_AR);
    }

    @Test
    @Transactional
    void getAllCommunesByNomCommuneArIsInShouldWork() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communeList where nomCommuneAr in DEFAULT_NOM_COMMUNE_AR or UPDATED_NOM_COMMUNE_AR
        defaultCommuneShouldBeFound("nomCommuneAr.in=" + DEFAULT_NOM_COMMUNE_AR + "," + UPDATED_NOM_COMMUNE_AR);

        // Get all the communeList where nomCommuneAr equals to UPDATED_NOM_COMMUNE_AR
        defaultCommuneShouldNotBeFound("nomCommuneAr.in=" + UPDATED_NOM_COMMUNE_AR);
    }

    @Test
    @Transactional
    void getAllCommunesByNomCommuneArIsNullOrNotNull() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communeList where nomCommuneAr is not null
        defaultCommuneShouldBeFound("nomCommuneAr.specified=true");

        // Get all the communeList where nomCommuneAr is null
        defaultCommuneShouldNotBeFound("nomCommuneAr.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunesByNomCommuneArContainsSomething() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communeList where nomCommuneAr contains DEFAULT_NOM_COMMUNE_AR
        defaultCommuneShouldBeFound("nomCommuneAr.contains=" + DEFAULT_NOM_COMMUNE_AR);

        // Get all the communeList where nomCommuneAr contains UPDATED_NOM_COMMUNE_AR
        defaultCommuneShouldNotBeFound("nomCommuneAr.contains=" + UPDATED_NOM_COMMUNE_AR);
    }

    @Test
    @Transactional
    void getAllCommunesByNomCommuneArNotContainsSomething() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communeList where nomCommuneAr does not contain DEFAULT_NOM_COMMUNE_AR
        defaultCommuneShouldNotBeFound("nomCommuneAr.doesNotContain=" + DEFAULT_NOM_COMMUNE_AR);

        // Get all the communeList where nomCommuneAr does not contain UPDATED_NOM_COMMUNE_AR
        defaultCommuneShouldBeFound("nomCommuneAr.doesNotContain=" + UPDATED_NOM_COMMUNE_AR);
    }

    @Test
    @Transactional
    void getAllCommunesByNomCommuneFrIsEqualToSomething() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communeList where nomCommuneFr equals to DEFAULT_NOM_COMMUNE_FR
        defaultCommuneShouldBeFound("nomCommuneFr.equals=" + DEFAULT_NOM_COMMUNE_FR);

        // Get all the communeList where nomCommuneFr equals to UPDATED_NOM_COMMUNE_FR
        defaultCommuneShouldNotBeFound("nomCommuneFr.equals=" + UPDATED_NOM_COMMUNE_FR);
    }

    @Test
    @Transactional
    void getAllCommunesByNomCommuneFrIsInShouldWork() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communeList where nomCommuneFr in DEFAULT_NOM_COMMUNE_FR or UPDATED_NOM_COMMUNE_FR
        defaultCommuneShouldBeFound("nomCommuneFr.in=" + DEFAULT_NOM_COMMUNE_FR + "," + UPDATED_NOM_COMMUNE_FR);

        // Get all the communeList where nomCommuneFr equals to UPDATED_NOM_COMMUNE_FR
        defaultCommuneShouldNotBeFound("nomCommuneFr.in=" + UPDATED_NOM_COMMUNE_FR);
    }

    @Test
    @Transactional
    void getAllCommunesByNomCommuneFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communeList where nomCommuneFr is not null
        defaultCommuneShouldBeFound("nomCommuneFr.specified=true");

        // Get all the communeList where nomCommuneFr is null
        defaultCommuneShouldNotBeFound("nomCommuneFr.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunesByNomCommuneFrContainsSomething() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communeList where nomCommuneFr contains DEFAULT_NOM_COMMUNE_FR
        defaultCommuneShouldBeFound("nomCommuneFr.contains=" + DEFAULT_NOM_COMMUNE_FR);

        // Get all the communeList where nomCommuneFr contains UPDATED_NOM_COMMUNE_FR
        defaultCommuneShouldNotBeFound("nomCommuneFr.contains=" + UPDATED_NOM_COMMUNE_FR);
    }

    @Test
    @Transactional
    void getAllCommunesByNomCommuneFrNotContainsSomething() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communeList where nomCommuneFr does not contain DEFAULT_NOM_COMMUNE_FR
        defaultCommuneShouldNotBeFound("nomCommuneFr.doesNotContain=" + DEFAULT_NOM_COMMUNE_FR);

        // Get all the communeList where nomCommuneFr does not contain UPDATED_NOM_COMMUNE_FR
        defaultCommuneShouldBeFound("nomCommuneFr.doesNotContain=" + UPDATED_NOM_COMMUNE_FR);
    }

    @Test
    @Transactional
    void getAllCommunesByProvincesIsEqualToSomething() throws Exception {
        Province provinces;
        if (TestUtil.findAll(em, Province.class).isEmpty()) {
            communeRepository.saveAndFlush(commune);
            provinces = ProvinceResourceIT.createEntity(em);
        } else {
            provinces = TestUtil.findAll(em, Province.class).get(0);
        }
        em.persist(provinces);
        em.flush();
        commune.setProvinces(provinces);
        communeRepository.saveAndFlush(commune);
        Long provincesId = provinces.getId();

        // Get all the communeList where provinces equals to provincesId
        defaultCommuneShouldBeFound("provincesId.equals=" + provincesId);

        // Get all the communeList where provinces equals to (provincesId + 1)
        defaultCommuneShouldNotBeFound("provincesId.equals=" + (provincesId + 1));
    }

    @Test
    @Transactional
    void getAllCommunesByProjetIsEqualToSomething() throws Exception {
        Projet projet;
        if (TestUtil.findAll(em, Projet.class).isEmpty()) {
            communeRepository.saveAndFlush(commune);
            projet = ProjetResourceIT.createEntity(em);
        } else {
            projet = TestUtil.findAll(em, Projet.class).get(0);
        }
        em.persist(projet);
        em.flush();
        commune.addProjet(projet);
        communeRepository.saveAndFlush(commune);
        Long projetId = projet.getId();

        // Get all the communeList where projet equals to projetId
        defaultCommuneShouldBeFound("projetId.equals=" + projetId);

        // Get all the communeList where projet equals to (projetId + 1)
        defaultCommuneShouldNotBeFound("projetId.equals=" + (projetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommuneShouldBeFound(String filter) throws Exception {
        restCommuneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commune.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomCommuneAr").value(hasItem(DEFAULT_NOM_COMMUNE_AR)))
            .andExpect(jsonPath("$.[*].nomCommuneFr").value(hasItem(DEFAULT_NOM_COMMUNE_FR)));

        // Check, that the count call also returns 1
        restCommuneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommuneShouldNotBeFound(String filter) throws Exception {
        restCommuneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommuneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommune() throws Exception {
        // Get the commune
        restCommuneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommune() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        int databaseSizeBeforeUpdate = communeRepository.findAll().size();

        // Update the commune
        Commune updatedCommune = communeRepository.findById(commune.getId()).get();
        // Disconnect from session so that the updates on updatedCommune are not directly saved in db
        em.detach(updatedCommune);
        updatedCommune.nomCommuneAr(UPDATED_NOM_COMMUNE_AR).nomCommuneFr(UPDATED_NOM_COMMUNE_FR);

        restCommuneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommune.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommune))
            )
            .andExpect(status().isOk());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
        Commune testCommune = communeList.get(communeList.size() - 1);
        assertThat(testCommune.getNomCommuneAr()).isEqualTo(UPDATED_NOM_COMMUNE_AR);
        assertThat(testCommune.getNomCommuneFr()).isEqualTo(UPDATED_NOM_COMMUNE_FR);
    }

    @Test
    @Transactional
    void putNonExistingCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commune.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commune)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommuneWithPatch() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        int databaseSizeBeforeUpdate = communeRepository.findAll().size();

        // Update the commune using partial update
        Commune partialUpdatedCommune = new Commune();
        partialUpdatedCommune.setId(commune.getId());

        partialUpdatedCommune.nomCommuneFr(UPDATED_NOM_COMMUNE_FR);

        restCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommune.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommune))
            )
            .andExpect(status().isOk());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
        Commune testCommune = communeList.get(communeList.size() - 1);
        assertThat(testCommune.getNomCommuneAr()).isEqualTo(DEFAULT_NOM_COMMUNE_AR);
        assertThat(testCommune.getNomCommuneFr()).isEqualTo(UPDATED_NOM_COMMUNE_FR);
    }

    @Test
    @Transactional
    void fullUpdateCommuneWithPatch() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        int databaseSizeBeforeUpdate = communeRepository.findAll().size();

        // Update the commune using partial update
        Commune partialUpdatedCommune = new Commune();
        partialUpdatedCommune.setId(commune.getId());

        partialUpdatedCommune.nomCommuneAr(UPDATED_NOM_COMMUNE_AR).nomCommuneFr(UPDATED_NOM_COMMUNE_FR);

        restCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommune.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommune))
            )
            .andExpect(status().isOk());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
        Commune testCommune = communeList.get(communeList.size() - 1);
        assertThat(testCommune.getNomCommuneAr()).isEqualTo(UPDATED_NOM_COMMUNE_AR);
        assertThat(testCommune.getNomCommuneFr()).isEqualTo(UPDATED_NOM_COMMUNE_FR);
    }

    @Test
    @Transactional
    void patchNonExistingCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commune.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commune))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommune() throws Exception {
        int databaseSizeBeforeUpdate = communeRepository.findAll().size();
        commune.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommuneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commune)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commune in the database
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommune() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        int databaseSizeBeforeDelete = communeRepository.findAll().size();

        // Delete the commune
        restCommuneMockMvc
            .perform(delete(ENTITY_API_URL_ID, commune.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commune> communeList = communeRepository.findAll();
        assertThat(communeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
