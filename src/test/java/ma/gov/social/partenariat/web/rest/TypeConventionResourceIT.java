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
import ma.gov.social.partenariat.domain.TypeConvention;
import ma.gov.social.partenariat.repository.TypeConventionRepository;
import ma.gov.social.partenariat.service.criteria.TypeConventionCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TypeConventionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeConventionResourceIT {

    private static final String DEFAULT_NOM_TYPE_AR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_TYPE_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_TYPE_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_TYPE_FR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-conventions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeConventionRepository typeConventionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeConventionMockMvc;

    private TypeConvention typeConvention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeConvention createEntity(EntityManager em) {
        TypeConvention typeConvention = new TypeConvention().nomTypeAr(DEFAULT_NOM_TYPE_AR).nomTypeFr(DEFAULT_NOM_TYPE_FR);
        return typeConvention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeConvention createUpdatedEntity(EntityManager em) {
        TypeConvention typeConvention = new TypeConvention().nomTypeAr(UPDATED_NOM_TYPE_AR).nomTypeFr(UPDATED_NOM_TYPE_FR);
        return typeConvention;
    }

    @BeforeEach
    public void initTest() {
        typeConvention = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeConvention() throws Exception {
        int databaseSizeBeforeCreate = typeConventionRepository.findAll().size();
        // Create the TypeConvention
        restTypeConventionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeConvention))
            )
            .andExpect(status().isCreated());

        // Validate the TypeConvention in the database
        List<TypeConvention> typeConventionList = typeConventionRepository.findAll();
        assertThat(typeConventionList).hasSize(databaseSizeBeforeCreate + 1);
        TypeConvention testTypeConvention = typeConventionList.get(typeConventionList.size() - 1);
        assertThat(testTypeConvention.getNomTypeAr()).isEqualTo(DEFAULT_NOM_TYPE_AR);
        assertThat(testTypeConvention.getNomTypeFr()).isEqualTo(DEFAULT_NOM_TYPE_FR);
    }

    @Test
    @Transactional
    void createTypeConventionWithExistingId() throws Exception {
        // Create the TypeConvention with an existing ID
        typeConvention.setId(1L);

        int databaseSizeBeforeCreate = typeConventionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeConventionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeConvention))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeConvention in the database
        List<TypeConvention> typeConventionList = typeConventionRepository.findAll();
        assertThat(typeConventionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomTypeArIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeConventionRepository.findAll().size();
        // set the field null
        typeConvention.setNomTypeAr(null);

        // Create the TypeConvention, which fails.

        restTypeConventionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeConvention))
            )
            .andExpect(status().isBadRequest());

        List<TypeConvention> typeConventionList = typeConventionRepository.findAll();
        assertThat(typeConventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeConventions() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        // Get all the typeConventionList
        restTypeConventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeConvention.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomTypeAr").value(hasItem(DEFAULT_NOM_TYPE_AR)))
            .andExpect(jsonPath("$.[*].nomTypeFr").value(hasItem(DEFAULT_NOM_TYPE_FR)));
    }

    @Test
    @Transactional
    void getTypeConvention() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        // Get the typeConvention
        restTypeConventionMockMvc
            .perform(get(ENTITY_API_URL_ID, typeConvention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeConvention.getId().intValue()))
            .andExpect(jsonPath("$.nomTypeAr").value(DEFAULT_NOM_TYPE_AR))
            .andExpect(jsonPath("$.nomTypeFr").value(DEFAULT_NOM_TYPE_FR));
    }

    @Test
    @Transactional
    void getTypeConventionsByIdFiltering() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        Long id = typeConvention.getId();

        defaultTypeConventionShouldBeFound("id.equals=" + id);
        defaultTypeConventionShouldNotBeFound("id.notEquals=" + id);

        defaultTypeConventionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTypeConventionShouldNotBeFound("id.greaterThan=" + id);

        defaultTypeConventionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTypeConventionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTypeConventionsByNomTypeArIsEqualToSomething() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        // Get all the typeConventionList where nomTypeAr equals to DEFAULT_NOM_TYPE_AR
        defaultTypeConventionShouldBeFound("nomTypeAr.equals=" + DEFAULT_NOM_TYPE_AR);

        // Get all the typeConventionList where nomTypeAr equals to UPDATED_NOM_TYPE_AR
        defaultTypeConventionShouldNotBeFound("nomTypeAr.equals=" + UPDATED_NOM_TYPE_AR);
    }

    @Test
    @Transactional
    void getAllTypeConventionsByNomTypeArIsInShouldWork() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        // Get all the typeConventionList where nomTypeAr in DEFAULT_NOM_TYPE_AR or UPDATED_NOM_TYPE_AR
        defaultTypeConventionShouldBeFound("nomTypeAr.in=" + DEFAULT_NOM_TYPE_AR + "," + UPDATED_NOM_TYPE_AR);

        // Get all the typeConventionList where nomTypeAr equals to UPDATED_NOM_TYPE_AR
        defaultTypeConventionShouldNotBeFound("nomTypeAr.in=" + UPDATED_NOM_TYPE_AR);
    }

    @Test
    @Transactional
    void getAllTypeConventionsByNomTypeArIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        // Get all the typeConventionList where nomTypeAr is not null
        defaultTypeConventionShouldBeFound("nomTypeAr.specified=true");

        // Get all the typeConventionList where nomTypeAr is null
        defaultTypeConventionShouldNotBeFound("nomTypeAr.specified=false");
    }

    @Test
    @Transactional
    void getAllTypeConventionsByNomTypeArContainsSomething() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        // Get all the typeConventionList where nomTypeAr contains DEFAULT_NOM_TYPE_AR
        defaultTypeConventionShouldBeFound("nomTypeAr.contains=" + DEFAULT_NOM_TYPE_AR);

        // Get all the typeConventionList where nomTypeAr contains UPDATED_NOM_TYPE_AR
        defaultTypeConventionShouldNotBeFound("nomTypeAr.contains=" + UPDATED_NOM_TYPE_AR);
    }

    @Test
    @Transactional
    void getAllTypeConventionsByNomTypeArNotContainsSomething() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        // Get all the typeConventionList where nomTypeAr does not contain DEFAULT_NOM_TYPE_AR
        defaultTypeConventionShouldNotBeFound("nomTypeAr.doesNotContain=" + DEFAULT_NOM_TYPE_AR);

        // Get all the typeConventionList where nomTypeAr does not contain UPDATED_NOM_TYPE_AR
        defaultTypeConventionShouldBeFound("nomTypeAr.doesNotContain=" + UPDATED_NOM_TYPE_AR);
    }

    @Test
    @Transactional
    void getAllTypeConventionsByNomTypeFrIsEqualToSomething() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        // Get all the typeConventionList where nomTypeFr equals to DEFAULT_NOM_TYPE_FR
        defaultTypeConventionShouldBeFound("nomTypeFr.equals=" + DEFAULT_NOM_TYPE_FR);

        // Get all the typeConventionList where nomTypeFr equals to UPDATED_NOM_TYPE_FR
        defaultTypeConventionShouldNotBeFound("nomTypeFr.equals=" + UPDATED_NOM_TYPE_FR);
    }

    @Test
    @Transactional
    void getAllTypeConventionsByNomTypeFrIsInShouldWork() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        // Get all the typeConventionList where nomTypeFr in DEFAULT_NOM_TYPE_FR or UPDATED_NOM_TYPE_FR
        defaultTypeConventionShouldBeFound("nomTypeFr.in=" + DEFAULT_NOM_TYPE_FR + "," + UPDATED_NOM_TYPE_FR);

        // Get all the typeConventionList where nomTypeFr equals to UPDATED_NOM_TYPE_FR
        defaultTypeConventionShouldNotBeFound("nomTypeFr.in=" + UPDATED_NOM_TYPE_FR);
    }

    @Test
    @Transactional
    void getAllTypeConventionsByNomTypeFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        // Get all the typeConventionList where nomTypeFr is not null
        defaultTypeConventionShouldBeFound("nomTypeFr.specified=true");

        // Get all the typeConventionList where nomTypeFr is null
        defaultTypeConventionShouldNotBeFound("nomTypeFr.specified=false");
    }

    @Test
    @Transactional
    void getAllTypeConventionsByNomTypeFrContainsSomething() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        // Get all the typeConventionList where nomTypeFr contains DEFAULT_NOM_TYPE_FR
        defaultTypeConventionShouldBeFound("nomTypeFr.contains=" + DEFAULT_NOM_TYPE_FR);

        // Get all the typeConventionList where nomTypeFr contains UPDATED_NOM_TYPE_FR
        defaultTypeConventionShouldNotBeFound("nomTypeFr.contains=" + UPDATED_NOM_TYPE_FR);
    }

    @Test
    @Transactional
    void getAllTypeConventionsByNomTypeFrNotContainsSomething() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        // Get all the typeConventionList where nomTypeFr does not contain DEFAULT_NOM_TYPE_FR
        defaultTypeConventionShouldNotBeFound("nomTypeFr.doesNotContain=" + DEFAULT_NOM_TYPE_FR);

        // Get all the typeConventionList where nomTypeFr does not contain UPDATED_NOM_TYPE_FR
        defaultTypeConventionShouldBeFound("nomTypeFr.doesNotContain=" + UPDATED_NOM_TYPE_FR);
    }

    @Test
    @Transactional
    void getAllTypeConventionsByConventionIsEqualToSomething() throws Exception {
        Convention convention;
        if (TestUtil.findAll(em, Convention.class).isEmpty()) {
            typeConventionRepository.saveAndFlush(typeConvention);
            convention = ConventionResourceIT.createEntity(em);
        } else {
            convention = TestUtil.findAll(em, Convention.class).get(0);
        }
        em.persist(convention);
        em.flush();
        typeConvention.addConvention(convention);
        typeConventionRepository.saveAndFlush(typeConvention);
        Long conventionId = convention.getId();

        // Get all the typeConventionList where convention equals to conventionId
        defaultTypeConventionShouldBeFound("conventionId.equals=" + conventionId);

        // Get all the typeConventionList where convention equals to (conventionId + 1)
        defaultTypeConventionShouldNotBeFound("conventionId.equals=" + (conventionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypeConventionShouldBeFound(String filter) throws Exception {
        restTypeConventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeConvention.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomTypeAr").value(hasItem(DEFAULT_NOM_TYPE_AR)))
            .andExpect(jsonPath("$.[*].nomTypeFr").value(hasItem(DEFAULT_NOM_TYPE_FR)));

        // Check, that the count call also returns 1
        restTypeConventionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypeConventionShouldNotBeFound(String filter) throws Exception {
        restTypeConventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypeConventionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTypeConvention() throws Exception {
        // Get the typeConvention
        restTypeConventionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeConvention() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        int databaseSizeBeforeUpdate = typeConventionRepository.findAll().size();

        // Update the typeConvention
        TypeConvention updatedTypeConvention = typeConventionRepository.findById(typeConvention.getId()).get();
        // Disconnect from session so that the updates on updatedTypeConvention are not directly saved in db
        em.detach(updatedTypeConvention);
        updatedTypeConvention.nomTypeAr(UPDATED_NOM_TYPE_AR).nomTypeFr(UPDATED_NOM_TYPE_FR);

        restTypeConventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypeConvention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypeConvention))
            )
            .andExpect(status().isOk());

        // Validate the TypeConvention in the database
        List<TypeConvention> typeConventionList = typeConventionRepository.findAll();
        assertThat(typeConventionList).hasSize(databaseSizeBeforeUpdate);
        TypeConvention testTypeConvention = typeConventionList.get(typeConventionList.size() - 1);
        assertThat(testTypeConvention.getNomTypeAr()).isEqualTo(UPDATED_NOM_TYPE_AR);
        assertThat(testTypeConvention.getNomTypeFr()).isEqualTo(UPDATED_NOM_TYPE_FR);
    }

    @Test
    @Transactional
    void putNonExistingTypeConvention() throws Exception {
        int databaseSizeBeforeUpdate = typeConventionRepository.findAll().size();
        typeConvention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeConventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeConvention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeConvention))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeConvention in the database
        List<TypeConvention> typeConventionList = typeConventionRepository.findAll();
        assertThat(typeConventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeConvention() throws Exception {
        int databaseSizeBeforeUpdate = typeConventionRepository.findAll().size();
        typeConvention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeConventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeConvention))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeConvention in the database
        List<TypeConvention> typeConventionList = typeConventionRepository.findAll();
        assertThat(typeConventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeConvention() throws Exception {
        int databaseSizeBeforeUpdate = typeConventionRepository.findAll().size();
        typeConvention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeConventionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeConvention)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeConvention in the database
        List<TypeConvention> typeConventionList = typeConventionRepository.findAll();
        assertThat(typeConventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeConventionWithPatch() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        int databaseSizeBeforeUpdate = typeConventionRepository.findAll().size();

        // Update the typeConvention using partial update
        TypeConvention partialUpdatedTypeConvention = new TypeConvention();
        partialUpdatedTypeConvention.setId(typeConvention.getId());

        partialUpdatedTypeConvention.nomTypeAr(UPDATED_NOM_TYPE_AR);

        restTypeConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeConvention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeConvention))
            )
            .andExpect(status().isOk());

        // Validate the TypeConvention in the database
        List<TypeConvention> typeConventionList = typeConventionRepository.findAll();
        assertThat(typeConventionList).hasSize(databaseSizeBeforeUpdate);
        TypeConvention testTypeConvention = typeConventionList.get(typeConventionList.size() - 1);
        assertThat(testTypeConvention.getNomTypeAr()).isEqualTo(UPDATED_NOM_TYPE_AR);
        assertThat(testTypeConvention.getNomTypeFr()).isEqualTo(DEFAULT_NOM_TYPE_FR);
    }

    @Test
    @Transactional
    void fullUpdateTypeConventionWithPatch() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        int databaseSizeBeforeUpdate = typeConventionRepository.findAll().size();

        // Update the typeConvention using partial update
        TypeConvention partialUpdatedTypeConvention = new TypeConvention();
        partialUpdatedTypeConvention.setId(typeConvention.getId());

        partialUpdatedTypeConvention.nomTypeAr(UPDATED_NOM_TYPE_AR).nomTypeFr(UPDATED_NOM_TYPE_FR);

        restTypeConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeConvention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeConvention))
            )
            .andExpect(status().isOk());

        // Validate the TypeConvention in the database
        List<TypeConvention> typeConventionList = typeConventionRepository.findAll();
        assertThat(typeConventionList).hasSize(databaseSizeBeforeUpdate);
        TypeConvention testTypeConvention = typeConventionList.get(typeConventionList.size() - 1);
        assertThat(testTypeConvention.getNomTypeAr()).isEqualTo(UPDATED_NOM_TYPE_AR);
        assertThat(testTypeConvention.getNomTypeFr()).isEqualTo(UPDATED_NOM_TYPE_FR);
    }

    @Test
    @Transactional
    void patchNonExistingTypeConvention() throws Exception {
        int databaseSizeBeforeUpdate = typeConventionRepository.findAll().size();
        typeConvention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeConvention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeConvention))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeConvention in the database
        List<TypeConvention> typeConventionList = typeConventionRepository.findAll();
        assertThat(typeConventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeConvention() throws Exception {
        int databaseSizeBeforeUpdate = typeConventionRepository.findAll().size();
        typeConvention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeConvention))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeConvention in the database
        List<TypeConvention> typeConventionList = typeConventionRepository.findAll();
        assertThat(typeConventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeConvention() throws Exception {
        int databaseSizeBeforeUpdate = typeConventionRepository.findAll().size();
        typeConvention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeConventionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typeConvention))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeConvention in the database
        List<TypeConvention> typeConventionList = typeConventionRepository.findAll();
        assertThat(typeConventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeConvention() throws Exception {
        // Initialize the database
        typeConventionRepository.saveAndFlush(typeConvention);

        int databaseSizeBeforeDelete = typeConventionRepository.findAll().size();

        // Delete the typeConvention
        restTypeConventionMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeConvention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeConvention> typeConventionList = typeConventionRepository.findAll();
        assertThat(typeConventionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
