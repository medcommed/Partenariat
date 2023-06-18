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
import ma.gov.social.partenariat.domain.Province;
import ma.gov.social.partenariat.domain.Region;
import ma.gov.social.partenariat.repository.RegionRepository;
import ma.gov.social.partenariat.service.criteria.RegionCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RegionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RegionResourceIT {

    private static final String DEFAULT_LIBELLE_REGION_AR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_REGION_AR = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_REGION_FR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_REGION_FR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/regions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegionMockMvc;

    private Region region;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Region createEntity(EntityManager em) {
        Region region = new Region().libelleRegionAr(DEFAULT_LIBELLE_REGION_AR).libelleRegionFr(DEFAULT_LIBELLE_REGION_FR);
        return region;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Region createUpdatedEntity(EntityManager em) {
        Region region = new Region().libelleRegionAr(UPDATED_LIBELLE_REGION_AR).libelleRegionFr(UPDATED_LIBELLE_REGION_FR);
        return region;
    }

    @BeforeEach
    public void initTest() {
        region = createEntity(em);
    }

    @Test
    @Transactional
    void createRegion() throws Exception {
        int databaseSizeBeforeCreate = regionRepository.findAll().size();
        // Create the Region
        restRegionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(region)))
            .andExpect(status().isCreated());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeCreate + 1);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getLibelleRegionAr()).isEqualTo(DEFAULT_LIBELLE_REGION_AR);
        assertThat(testRegion.getLibelleRegionFr()).isEqualTo(DEFAULT_LIBELLE_REGION_FR);
    }

    @Test
    @Transactional
    void createRegionWithExistingId() throws Exception {
        // Create the Region with an existing ID
        region.setId(1L);

        int databaseSizeBeforeCreate = regionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(region)))
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleRegionArIsRequired() throws Exception {
        int databaseSizeBeforeTest = regionRepository.findAll().size();
        // set the field null
        region.setLibelleRegionAr(null);

        // Create the Region, which fails.

        restRegionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(region)))
            .andExpect(status().isBadRequest());

        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRegions() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(region.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleRegionAr").value(hasItem(DEFAULT_LIBELLE_REGION_AR)))
            .andExpect(jsonPath("$.[*].libelleRegionFr").value(hasItem(DEFAULT_LIBELLE_REGION_FR)));
    }

    @Test
    @Transactional
    void getRegion() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get the region
        restRegionMockMvc
            .perform(get(ENTITY_API_URL_ID, region.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(region.getId().intValue()))
            .andExpect(jsonPath("$.libelleRegionAr").value(DEFAULT_LIBELLE_REGION_AR))
            .andExpect(jsonPath("$.libelleRegionFr").value(DEFAULT_LIBELLE_REGION_FR));
    }

    @Test
    @Transactional
    void getRegionsByIdFiltering() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        Long id = region.getId();

        defaultRegionShouldBeFound("id.equals=" + id);
        defaultRegionShouldNotBeFound("id.notEquals=" + id);

        defaultRegionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRegionShouldNotBeFound("id.greaterThan=" + id);

        defaultRegionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRegionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRegionsByLibelleRegionArIsEqualToSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libelleRegionAr equals to DEFAULT_LIBELLE_REGION_AR
        defaultRegionShouldBeFound("libelleRegionAr.equals=" + DEFAULT_LIBELLE_REGION_AR);

        // Get all the regionList where libelleRegionAr equals to UPDATED_LIBELLE_REGION_AR
        defaultRegionShouldNotBeFound("libelleRegionAr.equals=" + UPDATED_LIBELLE_REGION_AR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibelleRegionArIsInShouldWork() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libelleRegionAr in DEFAULT_LIBELLE_REGION_AR or UPDATED_LIBELLE_REGION_AR
        defaultRegionShouldBeFound("libelleRegionAr.in=" + DEFAULT_LIBELLE_REGION_AR + "," + UPDATED_LIBELLE_REGION_AR);

        // Get all the regionList where libelleRegionAr equals to UPDATED_LIBELLE_REGION_AR
        defaultRegionShouldNotBeFound("libelleRegionAr.in=" + UPDATED_LIBELLE_REGION_AR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibelleRegionArIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libelleRegionAr is not null
        defaultRegionShouldBeFound("libelleRegionAr.specified=true");

        // Get all the regionList where libelleRegionAr is null
        defaultRegionShouldNotBeFound("libelleRegionAr.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionsByLibelleRegionArContainsSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libelleRegionAr contains DEFAULT_LIBELLE_REGION_AR
        defaultRegionShouldBeFound("libelleRegionAr.contains=" + DEFAULT_LIBELLE_REGION_AR);

        // Get all the regionList where libelleRegionAr contains UPDATED_LIBELLE_REGION_AR
        defaultRegionShouldNotBeFound("libelleRegionAr.contains=" + UPDATED_LIBELLE_REGION_AR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibelleRegionArNotContainsSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libelleRegionAr does not contain DEFAULT_LIBELLE_REGION_AR
        defaultRegionShouldNotBeFound("libelleRegionAr.doesNotContain=" + DEFAULT_LIBELLE_REGION_AR);

        // Get all the regionList where libelleRegionAr does not contain UPDATED_LIBELLE_REGION_AR
        defaultRegionShouldBeFound("libelleRegionAr.doesNotContain=" + UPDATED_LIBELLE_REGION_AR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibelleRegionFrIsEqualToSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libelleRegionFr equals to DEFAULT_LIBELLE_REGION_FR
        defaultRegionShouldBeFound("libelleRegionFr.equals=" + DEFAULT_LIBELLE_REGION_FR);

        // Get all the regionList where libelleRegionFr equals to UPDATED_LIBELLE_REGION_FR
        defaultRegionShouldNotBeFound("libelleRegionFr.equals=" + UPDATED_LIBELLE_REGION_FR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibelleRegionFrIsInShouldWork() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libelleRegionFr in DEFAULT_LIBELLE_REGION_FR or UPDATED_LIBELLE_REGION_FR
        defaultRegionShouldBeFound("libelleRegionFr.in=" + DEFAULT_LIBELLE_REGION_FR + "," + UPDATED_LIBELLE_REGION_FR);

        // Get all the regionList where libelleRegionFr equals to UPDATED_LIBELLE_REGION_FR
        defaultRegionShouldNotBeFound("libelleRegionFr.in=" + UPDATED_LIBELLE_REGION_FR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibelleRegionFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libelleRegionFr is not null
        defaultRegionShouldBeFound("libelleRegionFr.specified=true");

        // Get all the regionList where libelleRegionFr is null
        defaultRegionShouldNotBeFound("libelleRegionFr.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionsByLibelleRegionFrContainsSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libelleRegionFr contains DEFAULT_LIBELLE_REGION_FR
        defaultRegionShouldBeFound("libelleRegionFr.contains=" + DEFAULT_LIBELLE_REGION_FR);

        // Get all the regionList where libelleRegionFr contains UPDATED_LIBELLE_REGION_FR
        defaultRegionShouldNotBeFound("libelleRegionFr.contains=" + UPDATED_LIBELLE_REGION_FR);
    }

    @Test
    @Transactional
    void getAllRegionsByLibelleRegionFrNotContainsSomething() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regionList where libelleRegionFr does not contain DEFAULT_LIBELLE_REGION_FR
        defaultRegionShouldNotBeFound("libelleRegionFr.doesNotContain=" + DEFAULT_LIBELLE_REGION_FR);

        // Get all the regionList where libelleRegionFr does not contain UPDATED_LIBELLE_REGION_FR
        defaultRegionShouldBeFound("libelleRegionFr.doesNotContain=" + UPDATED_LIBELLE_REGION_FR);
    }

    @Test
    @Transactional
    void getAllRegionsByProvinceIsEqualToSomething() throws Exception {
        Province province;
        if (TestUtil.findAll(em, Province.class).isEmpty()) {
            regionRepository.saveAndFlush(region);
            province = ProvinceResourceIT.createEntity(em);
        } else {
            province = TestUtil.findAll(em, Province.class).get(0);
        }
        em.persist(province);
        em.flush();
        region.addProvince(province);
        regionRepository.saveAndFlush(region);
        Long provinceId = province.getId();

        // Get all the regionList where province equals to provinceId
        defaultRegionShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the regionList where province equals to (provinceId + 1)
        defaultRegionShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRegionShouldBeFound(String filter) throws Exception {
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(region.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleRegionAr").value(hasItem(DEFAULT_LIBELLE_REGION_AR)))
            .andExpect(jsonPath("$.[*].libelleRegionFr").value(hasItem(DEFAULT_LIBELLE_REGION_FR)));

        // Check, that the count call also returns 1
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRegionShouldNotBeFound(String filter) throws Exception {
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRegion() throws Exception {
        // Get the region
        restRegionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRegion() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // Update the region
        Region updatedRegion = regionRepository.findById(region.getId()).get();
        // Disconnect from session so that the updates on updatedRegion are not directly saved in db
        em.detach(updatedRegion);
        updatedRegion.libelleRegionAr(UPDATED_LIBELLE_REGION_AR).libelleRegionFr(UPDATED_LIBELLE_REGION_FR);

        restRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRegion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRegion))
            )
            .andExpect(status().isOk());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getLibelleRegionAr()).isEqualTo(UPDATED_LIBELLE_REGION_AR);
        assertThat(testRegion.getLibelleRegionFr()).isEqualTo(UPDATED_LIBELLE_REGION_FR);
    }

    @Test
    @Transactional
    void putNonExistingRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();
        region.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, region.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(region))
            )
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();
        region.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(region))
            )
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();
        region.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(region)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegionWithPatch() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // Update the region using partial update
        Region partialUpdatedRegion = new Region();
        partialUpdatedRegion.setId(region.getId());

        partialUpdatedRegion.libelleRegionAr(UPDATED_LIBELLE_REGION_AR);

        restRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegion))
            )
            .andExpect(status().isOk());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getLibelleRegionAr()).isEqualTo(UPDATED_LIBELLE_REGION_AR);
        assertThat(testRegion.getLibelleRegionFr()).isEqualTo(DEFAULT_LIBELLE_REGION_FR);
    }

    @Test
    @Transactional
    void fullUpdateRegionWithPatch() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // Update the region using partial update
        Region partialUpdatedRegion = new Region();
        partialUpdatedRegion.setId(region.getId());

        partialUpdatedRegion.libelleRegionAr(UPDATED_LIBELLE_REGION_AR).libelleRegionFr(UPDATED_LIBELLE_REGION_FR);

        restRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegion))
            )
            .andExpect(status().isOk());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getLibelleRegionAr()).isEqualTo(UPDATED_LIBELLE_REGION_AR);
        assertThat(testRegion.getLibelleRegionFr()).isEqualTo(UPDATED_LIBELLE_REGION_FR);
    }

    @Test
    @Transactional
    void patchNonExistingRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();
        region.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, region.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(region))
            )
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();
        region.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(region))
            )
            .andExpect(status().isBadRequest());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();
        region.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(region)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegion() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        int databaseSizeBeforeDelete = regionRepository.findAll().size();

        // Delete the region
        restRegionMockMvc
            .perform(delete(ENTITY_API_URL_ID, region.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
