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
import ma.gov.social.partenariat.domain.Province;
import ma.gov.social.partenariat.domain.Region;
import ma.gov.social.partenariat.repository.ProvinceRepository;
import ma.gov.social.partenariat.service.ProvinceService;
import ma.gov.social.partenariat.service.criteria.ProvinceCriteria;
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
 * Integration tests for the {@link ProvinceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProvinceResourceIT {

    private static final String DEFAULT_LIBELLE_PROVINCE_AR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_PROVINCE_AR = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_PROVINCE_FR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_PROVINCE_FR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/provinces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProvinceRepository provinceRepository;

    @Mock
    private ProvinceRepository provinceRepositoryMock;

    @Mock
    private ProvinceService provinceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProvinceMockMvc;

    private Province province;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createEntity(EntityManager em) {
        Province province = new Province().libelleProvinceAr(DEFAULT_LIBELLE_PROVINCE_AR).libelleProvinceFr(DEFAULT_LIBELLE_PROVINCE_FR);
        return province;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createUpdatedEntity(EntityManager em) {
        Province province = new Province().libelleProvinceAr(UPDATED_LIBELLE_PROVINCE_AR).libelleProvinceFr(UPDATED_LIBELLE_PROVINCE_FR);
        return province;
    }

    @BeforeEach
    public void initTest() {
        province = createEntity(em);
    }

    @Test
    @Transactional
    void createProvince() throws Exception {
        int databaseSizeBeforeCreate = provinceRepository.findAll().size();
        // Create the Province
        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(province)))
            .andExpect(status().isCreated());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate + 1);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getLibelleProvinceAr()).isEqualTo(DEFAULT_LIBELLE_PROVINCE_AR);
        assertThat(testProvince.getLibelleProvinceFr()).isEqualTo(DEFAULT_LIBELLE_PROVINCE_FR);
    }

    @Test
    @Transactional
    void createProvinceWithExistingId() throws Exception {
        // Create the Province with an existing ID
        province.setId(1L);

        int databaseSizeBeforeCreate = provinceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(province)))
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleProvinceArIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinceRepository.findAll().size();
        // set the field null
        province.setLibelleProvinceAr(null);

        // Create the Province, which fails.

        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(province)))
            .andExpect(status().isBadRequest());

        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProvinces() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleProvinceAr").value(hasItem(DEFAULT_LIBELLE_PROVINCE_AR)))
            .andExpect(jsonPath("$.[*].libelleProvinceFr").value(hasItem(DEFAULT_LIBELLE_PROVINCE_FR)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProvincesWithEagerRelationshipsIsEnabled() throws Exception {
        when(provinceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProvinceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(provinceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProvincesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(provinceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProvinceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(provinceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get the province
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL_ID, province.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(province.getId().intValue()))
            .andExpect(jsonPath("$.libelleProvinceAr").value(DEFAULT_LIBELLE_PROVINCE_AR))
            .andExpect(jsonPath("$.libelleProvinceFr").value(DEFAULT_LIBELLE_PROVINCE_FR));
    }

    @Test
    @Transactional
    void getProvincesByIdFiltering() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        Long id = province.getId();

        defaultProvinceShouldBeFound("id.equals=" + id);
        defaultProvinceShouldNotBeFound("id.notEquals=" + id);

        defaultProvinceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProvinceShouldNotBeFound("id.greaterThan=" + id);

        defaultProvinceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProvinceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProvincesByLibelleProvinceArIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libelleProvinceAr equals to DEFAULT_LIBELLE_PROVINCE_AR
        defaultProvinceShouldBeFound("libelleProvinceAr.equals=" + DEFAULT_LIBELLE_PROVINCE_AR);

        // Get all the provinceList where libelleProvinceAr equals to UPDATED_LIBELLE_PROVINCE_AR
        defaultProvinceShouldNotBeFound("libelleProvinceAr.equals=" + UPDATED_LIBELLE_PROVINCE_AR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibelleProvinceArIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libelleProvinceAr in DEFAULT_LIBELLE_PROVINCE_AR or UPDATED_LIBELLE_PROVINCE_AR
        defaultProvinceShouldBeFound("libelleProvinceAr.in=" + DEFAULT_LIBELLE_PROVINCE_AR + "," + UPDATED_LIBELLE_PROVINCE_AR);

        // Get all the provinceList where libelleProvinceAr equals to UPDATED_LIBELLE_PROVINCE_AR
        defaultProvinceShouldNotBeFound("libelleProvinceAr.in=" + UPDATED_LIBELLE_PROVINCE_AR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibelleProvinceArIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libelleProvinceAr is not null
        defaultProvinceShouldBeFound("libelleProvinceAr.specified=true");

        // Get all the provinceList where libelleProvinceAr is null
        defaultProvinceShouldNotBeFound("libelleProvinceAr.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByLibelleProvinceArContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libelleProvinceAr contains DEFAULT_LIBELLE_PROVINCE_AR
        defaultProvinceShouldBeFound("libelleProvinceAr.contains=" + DEFAULT_LIBELLE_PROVINCE_AR);

        // Get all the provinceList where libelleProvinceAr contains UPDATED_LIBELLE_PROVINCE_AR
        defaultProvinceShouldNotBeFound("libelleProvinceAr.contains=" + UPDATED_LIBELLE_PROVINCE_AR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibelleProvinceArNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libelleProvinceAr does not contain DEFAULT_LIBELLE_PROVINCE_AR
        defaultProvinceShouldNotBeFound("libelleProvinceAr.doesNotContain=" + DEFAULT_LIBELLE_PROVINCE_AR);

        // Get all the provinceList where libelleProvinceAr does not contain UPDATED_LIBELLE_PROVINCE_AR
        defaultProvinceShouldBeFound("libelleProvinceAr.doesNotContain=" + UPDATED_LIBELLE_PROVINCE_AR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibelleProvinceFrIsEqualToSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libelleProvinceFr equals to DEFAULT_LIBELLE_PROVINCE_FR
        defaultProvinceShouldBeFound("libelleProvinceFr.equals=" + DEFAULT_LIBELLE_PROVINCE_FR);

        // Get all the provinceList where libelleProvinceFr equals to UPDATED_LIBELLE_PROVINCE_FR
        defaultProvinceShouldNotBeFound("libelleProvinceFr.equals=" + UPDATED_LIBELLE_PROVINCE_FR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibelleProvinceFrIsInShouldWork() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libelleProvinceFr in DEFAULT_LIBELLE_PROVINCE_FR or UPDATED_LIBELLE_PROVINCE_FR
        defaultProvinceShouldBeFound("libelleProvinceFr.in=" + DEFAULT_LIBELLE_PROVINCE_FR + "," + UPDATED_LIBELLE_PROVINCE_FR);

        // Get all the provinceList where libelleProvinceFr equals to UPDATED_LIBELLE_PROVINCE_FR
        defaultProvinceShouldNotBeFound("libelleProvinceFr.in=" + UPDATED_LIBELLE_PROVINCE_FR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibelleProvinceFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libelleProvinceFr is not null
        defaultProvinceShouldBeFound("libelleProvinceFr.specified=true");

        // Get all the provinceList where libelleProvinceFr is null
        defaultProvinceShouldNotBeFound("libelleProvinceFr.specified=false");
    }

    @Test
    @Transactional
    void getAllProvincesByLibelleProvinceFrContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libelleProvinceFr contains DEFAULT_LIBELLE_PROVINCE_FR
        defaultProvinceShouldBeFound("libelleProvinceFr.contains=" + DEFAULT_LIBELLE_PROVINCE_FR);

        // Get all the provinceList where libelleProvinceFr contains UPDATED_LIBELLE_PROVINCE_FR
        defaultProvinceShouldNotBeFound("libelleProvinceFr.contains=" + UPDATED_LIBELLE_PROVINCE_FR);
    }

    @Test
    @Transactional
    void getAllProvincesByLibelleProvinceFrNotContainsSomething() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinceList where libelleProvinceFr does not contain DEFAULT_LIBELLE_PROVINCE_FR
        defaultProvinceShouldNotBeFound("libelleProvinceFr.doesNotContain=" + DEFAULT_LIBELLE_PROVINCE_FR);

        // Get all the provinceList where libelleProvinceFr does not contain UPDATED_LIBELLE_PROVINCE_FR
        defaultProvinceShouldBeFound("libelleProvinceFr.doesNotContain=" + UPDATED_LIBELLE_PROVINCE_FR);
    }

    @Test
    @Transactional
    void getAllProvincesByRegionIsEqualToSomething() throws Exception {
        Region region;
        if (TestUtil.findAll(em, Region.class).isEmpty()) {
            provinceRepository.saveAndFlush(province);
            region = RegionResourceIT.createEntity(em);
        } else {
            region = TestUtil.findAll(em, Region.class).get(0);
        }
        em.persist(region);
        em.flush();
        province.setRegion(region);
        provinceRepository.saveAndFlush(province);
        Long regionId = region.getId();

        // Get all the provinceList where region equals to regionId
        defaultProvinceShouldBeFound("regionId.equals=" + regionId);

        // Get all the provinceList where region equals to (regionId + 1)
        defaultProvinceShouldNotBeFound("regionId.equals=" + (regionId + 1));
    }

    @Test
    @Transactional
    void getAllProvincesByCommuneIsEqualToSomething() throws Exception {
        Commune commune;
        if (TestUtil.findAll(em, Commune.class).isEmpty()) {
            provinceRepository.saveAndFlush(province);
            commune = CommuneResourceIT.createEntity(em);
        } else {
            commune = TestUtil.findAll(em, Commune.class).get(0);
        }
        em.persist(commune);
        em.flush();
        province.addCommune(commune);
        provinceRepository.saveAndFlush(province);
        Long communeId = commune.getId();

        // Get all the provinceList where commune equals to communeId
        defaultProvinceShouldBeFound("communeId.equals=" + communeId);

        // Get all the provinceList where commune equals to (communeId + 1)
        defaultProvinceShouldNotBeFound("communeId.equals=" + (communeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProvinceShouldBeFound(String filter) throws Exception {
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleProvinceAr").value(hasItem(DEFAULT_LIBELLE_PROVINCE_AR)))
            .andExpect(jsonPath("$.[*].libelleProvinceFr").value(hasItem(DEFAULT_LIBELLE_PROVINCE_FR)));

        // Check, that the count call also returns 1
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProvinceShouldNotBeFound(String filter) throws Exception {
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProvince() throws Exception {
        // Get the province
        restProvinceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province
        Province updatedProvince = provinceRepository.findById(province.getId()).get();
        // Disconnect from session so that the updates on updatedProvince are not directly saved in db
        em.detach(updatedProvince);
        updatedProvince.libelleProvinceAr(UPDATED_LIBELLE_PROVINCE_AR).libelleProvinceFr(UPDATED_LIBELLE_PROVINCE_FR);

        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProvince.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getLibelleProvinceAr()).isEqualTo(UPDATED_LIBELLE_PROVINCE_AR);
        assertThat(testProvince.getLibelleProvinceFr()).isEqualTo(UPDATED_LIBELLE_PROVINCE_FR);
    }

    @Test
    @Transactional
    void putNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, province.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(province))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(province))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(province)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.libelleProvinceAr(UPDATED_LIBELLE_PROVINCE_AR);

        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getLibelleProvinceAr()).isEqualTo(UPDATED_LIBELLE_PROVINCE_AR);
        assertThat(testProvince.getLibelleProvinceFr()).isEqualTo(DEFAULT_LIBELLE_PROVINCE_FR);
    }

    @Test
    @Transactional
    void fullUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.libelleProvinceAr(UPDATED_LIBELLE_PROVINCE_AR).libelleProvinceFr(UPDATED_LIBELLE_PROVINCE_FR);

        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getLibelleProvinceAr()).isEqualTo(UPDATED_LIBELLE_PROVINCE_AR);
        assertThat(testProvince.getLibelleProvinceFr()).isEqualTo(UPDATED_LIBELLE_PROVINCE_FR);
    }

    @Test
    @Transactional
    void patchNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, province.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(province))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(province))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().size();
        province.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(province)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        int databaseSizeBeforeDelete = provinceRepository.findAll().size();

        // Delete the province
        restProvinceMockMvc
            .perform(delete(ENTITY_API_URL_ID, province.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Province> provinceList = provinceRepository.findAll();
        assertThat(provinceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
