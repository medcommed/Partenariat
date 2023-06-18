package ma.gov.social.partenariat.web.rest;

import static ma.gov.social.partenariat.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ma.gov.social.partenariat.IntegrationTest;
import ma.gov.social.partenariat.domain.Projet;
import ma.gov.social.partenariat.domain.Tranche;
import ma.gov.social.partenariat.repository.TrancheRepository;
import ma.gov.social.partenariat.service.criteria.TrancheCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TrancheResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrancheResourceIT {

    private static final String DEFAULT_NOM_TRANCHE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_TRANCHE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEFFET = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEFFET = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEFFET = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_MONTANT_TRANCHE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_TRANCHE = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTANT_TRANCHE = new BigDecimal(1 - 1);

    private static final String DEFAULT_RAPPORT_TRANCHE = "AAAAAAAAAA";
    private static final String UPDATED_RAPPORT_TRANCHE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tranches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrancheRepository trancheRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrancheMockMvc;

    private Tranche tranche;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tranche createEntity(EntityManager em) {
        Tranche tranche = new Tranche()
            .nomTranche(DEFAULT_NOM_TRANCHE)
            .dateDeffet(DEFAULT_DATE_DEFFET)
            .montantTranche(DEFAULT_MONTANT_TRANCHE)
            .rapportTranche(DEFAULT_RAPPORT_TRANCHE);
        // Add required entity
        Projet projet;
        if (TestUtil.findAll(em, Projet.class).isEmpty()) {
            projet = ProjetResourceIT.createEntity(em);
            em.persist(projet);
            em.flush();
        } else {
            projet = TestUtil.findAll(em, Projet.class).get(0);
        }
        tranche.setProjet(projet);
        return tranche;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tranche createUpdatedEntity(EntityManager em) {
        Tranche tranche = new Tranche()
            .nomTranche(UPDATED_NOM_TRANCHE)
            .dateDeffet(UPDATED_DATE_DEFFET)
            .montantTranche(UPDATED_MONTANT_TRANCHE)
            .rapportTranche(UPDATED_RAPPORT_TRANCHE);
        // Add required entity
        Projet projet;
        if (TestUtil.findAll(em, Projet.class).isEmpty()) {
            projet = ProjetResourceIT.createUpdatedEntity(em);
            em.persist(projet);
            em.flush();
        } else {
            projet = TestUtil.findAll(em, Projet.class).get(0);
        }
        tranche.setProjet(projet);
        return tranche;
    }

    @BeforeEach
    public void initTest() {
        tranche = createEntity(em);
    }

    @Test
    @Transactional
    void createTranche() throws Exception {
        int databaseSizeBeforeCreate = trancheRepository.findAll().size();
        // Create the Tranche
        restTrancheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tranche)))
            .andExpect(status().isCreated());

        // Validate the Tranche in the database
        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeCreate + 1);
        Tranche testTranche = trancheList.get(trancheList.size() - 1);
        assertThat(testTranche.getNomTranche()).isEqualTo(DEFAULT_NOM_TRANCHE);
        assertThat(testTranche.getDateDeffet()).isEqualTo(DEFAULT_DATE_DEFFET);
        assertThat(testTranche.getMontantTranche()).isEqualByComparingTo(DEFAULT_MONTANT_TRANCHE);
        assertThat(testTranche.getRapportTranche()).isEqualTo(DEFAULT_RAPPORT_TRANCHE);
    }

    @Test
    @Transactional
    void createTrancheWithExistingId() throws Exception {
        // Create the Tranche with an existing ID
        tranche.setId(1L);

        int databaseSizeBeforeCreate = trancheRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrancheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tranche)))
            .andExpect(status().isBadRequest());

        // Validate the Tranche in the database
        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomTrancheIsRequired() throws Exception {
        int databaseSizeBeforeTest = trancheRepository.findAll().size();
        // set the field null
        tranche.setNomTranche(null);

        // Create the Tranche, which fails.

        restTrancheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tranche)))
            .andExpect(status().isBadRequest());

        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDeffetIsRequired() throws Exception {
        int databaseSizeBeforeTest = trancheRepository.findAll().size();
        // set the field null
        tranche.setDateDeffet(null);

        // Create the Tranche, which fails.

        restTrancheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tranche)))
            .andExpect(status().isBadRequest());

        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantTrancheIsRequired() throws Exception {
        int databaseSizeBeforeTest = trancheRepository.findAll().size();
        // set the field null
        tranche.setMontantTranche(null);

        // Create the Tranche, which fails.

        restTrancheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tranche)))
            .andExpect(status().isBadRequest());

        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRapportTrancheIsRequired() throws Exception {
        int databaseSizeBeforeTest = trancheRepository.findAll().size();
        // set the field null
        tranche.setRapportTranche(null);

        // Create the Tranche, which fails.

        restTrancheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tranche)))
            .andExpect(status().isBadRequest());

        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTranches() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList
        restTrancheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tranche.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomTranche").value(hasItem(DEFAULT_NOM_TRANCHE)))
            .andExpect(jsonPath("$.[*].dateDeffet").value(hasItem(DEFAULT_DATE_DEFFET.toString())))
            .andExpect(jsonPath("$.[*].montantTranche").value(hasItem(sameNumber(DEFAULT_MONTANT_TRANCHE))))
            .andExpect(jsonPath("$.[*].rapportTranche").value(hasItem(DEFAULT_RAPPORT_TRANCHE)));
    }

    @Test
    @Transactional
    void getTranche() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get the tranche
        restTrancheMockMvc
            .perform(get(ENTITY_API_URL_ID, tranche.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tranche.getId().intValue()))
            .andExpect(jsonPath("$.nomTranche").value(DEFAULT_NOM_TRANCHE))
            .andExpect(jsonPath("$.dateDeffet").value(DEFAULT_DATE_DEFFET.toString()))
            .andExpect(jsonPath("$.montantTranche").value(sameNumber(DEFAULT_MONTANT_TRANCHE)))
            .andExpect(jsonPath("$.rapportTranche").value(DEFAULT_RAPPORT_TRANCHE));
    }

    @Test
    @Transactional
    void getTranchesByIdFiltering() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        Long id = tranche.getId();

        defaultTrancheShouldBeFound("id.equals=" + id);
        defaultTrancheShouldNotBeFound("id.notEquals=" + id);

        defaultTrancheShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTrancheShouldNotBeFound("id.greaterThan=" + id);

        defaultTrancheShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTrancheShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTranchesByNomTrancheIsEqualToSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where nomTranche equals to DEFAULT_NOM_TRANCHE
        defaultTrancheShouldBeFound("nomTranche.equals=" + DEFAULT_NOM_TRANCHE);

        // Get all the trancheList where nomTranche equals to UPDATED_NOM_TRANCHE
        defaultTrancheShouldNotBeFound("nomTranche.equals=" + UPDATED_NOM_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByNomTrancheIsInShouldWork() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where nomTranche in DEFAULT_NOM_TRANCHE or UPDATED_NOM_TRANCHE
        defaultTrancheShouldBeFound("nomTranche.in=" + DEFAULT_NOM_TRANCHE + "," + UPDATED_NOM_TRANCHE);

        // Get all the trancheList where nomTranche equals to UPDATED_NOM_TRANCHE
        defaultTrancheShouldNotBeFound("nomTranche.in=" + UPDATED_NOM_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByNomTrancheIsNullOrNotNull() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where nomTranche is not null
        defaultTrancheShouldBeFound("nomTranche.specified=true");

        // Get all the trancheList where nomTranche is null
        defaultTrancheShouldNotBeFound("nomTranche.specified=false");
    }

    @Test
    @Transactional
    void getAllTranchesByNomTrancheContainsSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where nomTranche contains DEFAULT_NOM_TRANCHE
        defaultTrancheShouldBeFound("nomTranche.contains=" + DEFAULT_NOM_TRANCHE);

        // Get all the trancheList where nomTranche contains UPDATED_NOM_TRANCHE
        defaultTrancheShouldNotBeFound("nomTranche.contains=" + UPDATED_NOM_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByNomTrancheNotContainsSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where nomTranche does not contain DEFAULT_NOM_TRANCHE
        defaultTrancheShouldNotBeFound("nomTranche.doesNotContain=" + DEFAULT_NOM_TRANCHE);

        // Get all the trancheList where nomTranche does not contain UPDATED_NOM_TRANCHE
        defaultTrancheShouldBeFound("nomTranche.doesNotContain=" + UPDATED_NOM_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByDateDeffetIsEqualToSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where dateDeffet equals to DEFAULT_DATE_DEFFET
        defaultTrancheShouldBeFound("dateDeffet.equals=" + DEFAULT_DATE_DEFFET);

        // Get all the trancheList where dateDeffet equals to UPDATED_DATE_DEFFET
        defaultTrancheShouldNotBeFound("dateDeffet.equals=" + UPDATED_DATE_DEFFET);
    }

    @Test
    @Transactional
    void getAllTranchesByDateDeffetIsInShouldWork() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where dateDeffet in DEFAULT_DATE_DEFFET or UPDATED_DATE_DEFFET
        defaultTrancheShouldBeFound("dateDeffet.in=" + DEFAULT_DATE_DEFFET + "," + UPDATED_DATE_DEFFET);

        // Get all the trancheList where dateDeffet equals to UPDATED_DATE_DEFFET
        defaultTrancheShouldNotBeFound("dateDeffet.in=" + UPDATED_DATE_DEFFET);
    }

    @Test
    @Transactional
    void getAllTranchesByDateDeffetIsNullOrNotNull() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where dateDeffet is not null
        defaultTrancheShouldBeFound("dateDeffet.specified=true");

        // Get all the trancheList where dateDeffet is null
        defaultTrancheShouldNotBeFound("dateDeffet.specified=false");
    }

    @Test
    @Transactional
    void getAllTranchesByDateDeffetIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where dateDeffet is greater than or equal to DEFAULT_DATE_DEFFET
        defaultTrancheShouldBeFound("dateDeffet.greaterThanOrEqual=" + DEFAULT_DATE_DEFFET);

        // Get all the trancheList where dateDeffet is greater than or equal to UPDATED_DATE_DEFFET
        defaultTrancheShouldNotBeFound("dateDeffet.greaterThanOrEqual=" + UPDATED_DATE_DEFFET);
    }

    @Test
    @Transactional
    void getAllTranchesByDateDeffetIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where dateDeffet is less than or equal to DEFAULT_DATE_DEFFET
        defaultTrancheShouldBeFound("dateDeffet.lessThanOrEqual=" + DEFAULT_DATE_DEFFET);

        // Get all the trancheList where dateDeffet is less than or equal to SMALLER_DATE_DEFFET
        defaultTrancheShouldNotBeFound("dateDeffet.lessThanOrEqual=" + SMALLER_DATE_DEFFET);
    }

    @Test
    @Transactional
    void getAllTranchesByDateDeffetIsLessThanSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where dateDeffet is less than DEFAULT_DATE_DEFFET
        defaultTrancheShouldNotBeFound("dateDeffet.lessThan=" + DEFAULT_DATE_DEFFET);

        // Get all the trancheList where dateDeffet is less than UPDATED_DATE_DEFFET
        defaultTrancheShouldBeFound("dateDeffet.lessThan=" + UPDATED_DATE_DEFFET);
    }

    @Test
    @Transactional
    void getAllTranchesByDateDeffetIsGreaterThanSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where dateDeffet is greater than DEFAULT_DATE_DEFFET
        defaultTrancheShouldNotBeFound("dateDeffet.greaterThan=" + DEFAULT_DATE_DEFFET);

        // Get all the trancheList where dateDeffet is greater than SMALLER_DATE_DEFFET
        defaultTrancheShouldBeFound("dateDeffet.greaterThan=" + SMALLER_DATE_DEFFET);
    }

    @Test
    @Transactional
    void getAllTranchesByMontantTrancheIsEqualToSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where montantTranche equals to DEFAULT_MONTANT_TRANCHE
        defaultTrancheShouldBeFound("montantTranche.equals=" + DEFAULT_MONTANT_TRANCHE);

        // Get all the trancheList where montantTranche equals to UPDATED_MONTANT_TRANCHE
        defaultTrancheShouldNotBeFound("montantTranche.equals=" + UPDATED_MONTANT_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByMontantTrancheIsInShouldWork() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where montantTranche in DEFAULT_MONTANT_TRANCHE or UPDATED_MONTANT_TRANCHE
        defaultTrancheShouldBeFound("montantTranche.in=" + DEFAULT_MONTANT_TRANCHE + "," + UPDATED_MONTANT_TRANCHE);

        // Get all the trancheList where montantTranche equals to UPDATED_MONTANT_TRANCHE
        defaultTrancheShouldNotBeFound("montantTranche.in=" + UPDATED_MONTANT_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByMontantTrancheIsNullOrNotNull() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where montantTranche is not null
        defaultTrancheShouldBeFound("montantTranche.specified=true");

        // Get all the trancheList where montantTranche is null
        defaultTrancheShouldNotBeFound("montantTranche.specified=false");
    }

    @Test
    @Transactional
    void getAllTranchesByMontantTrancheIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where montantTranche is greater than or equal to DEFAULT_MONTANT_TRANCHE
        defaultTrancheShouldBeFound("montantTranche.greaterThanOrEqual=" + DEFAULT_MONTANT_TRANCHE);

        // Get all the trancheList where montantTranche is greater than or equal to UPDATED_MONTANT_TRANCHE
        defaultTrancheShouldNotBeFound("montantTranche.greaterThanOrEqual=" + UPDATED_MONTANT_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByMontantTrancheIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where montantTranche is less than or equal to DEFAULT_MONTANT_TRANCHE
        defaultTrancheShouldBeFound("montantTranche.lessThanOrEqual=" + DEFAULT_MONTANT_TRANCHE);

        // Get all the trancheList where montantTranche is less than or equal to SMALLER_MONTANT_TRANCHE
        defaultTrancheShouldNotBeFound("montantTranche.lessThanOrEqual=" + SMALLER_MONTANT_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByMontantTrancheIsLessThanSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where montantTranche is less than DEFAULT_MONTANT_TRANCHE
        defaultTrancheShouldNotBeFound("montantTranche.lessThan=" + DEFAULT_MONTANT_TRANCHE);

        // Get all the trancheList where montantTranche is less than UPDATED_MONTANT_TRANCHE
        defaultTrancheShouldBeFound("montantTranche.lessThan=" + UPDATED_MONTANT_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByMontantTrancheIsGreaterThanSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where montantTranche is greater than DEFAULT_MONTANT_TRANCHE
        defaultTrancheShouldNotBeFound("montantTranche.greaterThan=" + DEFAULT_MONTANT_TRANCHE);

        // Get all the trancheList where montantTranche is greater than SMALLER_MONTANT_TRANCHE
        defaultTrancheShouldBeFound("montantTranche.greaterThan=" + SMALLER_MONTANT_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByRapportTrancheIsEqualToSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where rapportTranche equals to DEFAULT_RAPPORT_TRANCHE
        defaultTrancheShouldBeFound("rapportTranche.equals=" + DEFAULT_RAPPORT_TRANCHE);

        // Get all the trancheList where rapportTranche equals to UPDATED_RAPPORT_TRANCHE
        defaultTrancheShouldNotBeFound("rapportTranche.equals=" + UPDATED_RAPPORT_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByRapportTrancheIsInShouldWork() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where rapportTranche in DEFAULT_RAPPORT_TRANCHE or UPDATED_RAPPORT_TRANCHE
        defaultTrancheShouldBeFound("rapportTranche.in=" + DEFAULT_RAPPORT_TRANCHE + "," + UPDATED_RAPPORT_TRANCHE);

        // Get all the trancheList where rapportTranche equals to UPDATED_RAPPORT_TRANCHE
        defaultTrancheShouldNotBeFound("rapportTranche.in=" + UPDATED_RAPPORT_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByRapportTrancheIsNullOrNotNull() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where rapportTranche is not null
        defaultTrancheShouldBeFound("rapportTranche.specified=true");

        // Get all the trancheList where rapportTranche is null
        defaultTrancheShouldNotBeFound("rapportTranche.specified=false");
    }

    @Test
    @Transactional
    void getAllTranchesByRapportTrancheContainsSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where rapportTranche contains DEFAULT_RAPPORT_TRANCHE
        defaultTrancheShouldBeFound("rapportTranche.contains=" + DEFAULT_RAPPORT_TRANCHE);

        // Get all the trancheList where rapportTranche contains UPDATED_RAPPORT_TRANCHE
        defaultTrancheShouldNotBeFound("rapportTranche.contains=" + UPDATED_RAPPORT_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByRapportTrancheNotContainsSomething() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        // Get all the trancheList where rapportTranche does not contain DEFAULT_RAPPORT_TRANCHE
        defaultTrancheShouldNotBeFound("rapportTranche.doesNotContain=" + DEFAULT_RAPPORT_TRANCHE);

        // Get all the trancheList where rapportTranche does not contain UPDATED_RAPPORT_TRANCHE
        defaultTrancheShouldBeFound("rapportTranche.doesNotContain=" + UPDATED_RAPPORT_TRANCHE);
    }

    @Test
    @Transactional
    void getAllTranchesByProjetIsEqualToSomething() throws Exception {
        Projet projet;
        if (TestUtil.findAll(em, Projet.class).isEmpty()) {
            trancheRepository.saveAndFlush(tranche);
            projet = ProjetResourceIT.createEntity(em);
        } else {
            projet = TestUtil.findAll(em, Projet.class).get(0);
        }
        em.persist(projet);
        em.flush();
        tranche.setProjet(projet);
        trancheRepository.saveAndFlush(tranche);
        Long projetId = projet.getId();

        // Get all the trancheList where projet equals to projetId
        defaultTrancheShouldBeFound("projetId.equals=" + projetId);

        // Get all the trancheList where projet equals to (projetId + 1)
        defaultTrancheShouldNotBeFound("projetId.equals=" + (projetId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrancheShouldBeFound(String filter) throws Exception {
        restTrancheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tranche.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomTranche").value(hasItem(DEFAULT_NOM_TRANCHE)))
            .andExpect(jsonPath("$.[*].dateDeffet").value(hasItem(DEFAULT_DATE_DEFFET.toString())))
            .andExpect(jsonPath("$.[*].montantTranche").value(hasItem(sameNumber(DEFAULT_MONTANT_TRANCHE))))
            .andExpect(jsonPath("$.[*].rapportTranche").value(hasItem(DEFAULT_RAPPORT_TRANCHE)));

        // Check, that the count call also returns 1
        restTrancheMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrancheShouldNotBeFound(String filter) throws Exception {
        restTrancheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrancheMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTranche() throws Exception {
        // Get the tranche
        restTrancheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTranche() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        int databaseSizeBeforeUpdate = trancheRepository.findAll().size();

        // Update the tranche
        Tranche updatedTranche = trancheRepository.findById(tranche.getId()).get();
        // Disconnect from session so that the updates on updatedTranche are not directly saved in db
        em.detach(updatedTranche);
        updatedTranche
            .nomTranche(UPDATED_NOM_TRANCHE)
            .dateDeffet(UPDATED_DATE_DEFFET)
            .montantTranche(UPDATED_MONTANT_TRANCHE)
            .rapportTranche(UPDATED_RAPPORT_TRANCHE);

        restTrancheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTranche.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTranche))
            )
            .andExpect(status().isOk());

        // Validate the Tranche in the database
        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeUpdate);
        Tranche testTranche = trancheList.get(trancheList.size() - 1);
        assertThat(testTranche.getNomTranche()).isEqualTo(UPDATED_NOM_TRANCHE);
        assertThat(testTranche.getDateDeffet()).isEqualTo(UPDATED_DATE_DEFFET);
        assertThat(testTranche.getMontantTranche()).isEqualByComparingTo(UPDATED_MONTANT_TRANCHE);
        assertThat(testTranche.getRapportTranche()).isEqualTo(UPDATED_RAPPORT_TRANCHE);
    }

    @Test
    @Transactional
    void putNonExistingTranche() throws Exception {
        int databaseSizeBeforeUpdate = trancheRepository.findAll().size();
        tranche.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrancheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tranche.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tranche))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tranche in the database
        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTranche() throws Exception {
        int databaseSizeBeforeUpdate = trancheRepository.findAll().size();
        tranche.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrancheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tranche))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tranche in the database
        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTranche() throws Exception {
        int databaseSizeBeforeUpdate = trancheRepository.findAll().size();
        tranche.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrancheMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tranche)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tranche in the database
        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrancheWithPatch() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        int databaseSizeBeforeUpdate = trancheRepository.findAll().size();

        // Update the tranche using partial update
        Tranche partialUpdatedTranche = new Tranche();
        partialUpdatedTranche.setId(tranche.getId());

        partialUpdatedTranche
            .nomTranche(UPDATED_NOM_TRANCHE)
            .dateDeffet(UPDATED_DATE_DEFFET)
            .montantTranche(UPDATED_MONTANT_TRANCHE)
            .rapportTranche(UPDATED_RAPPORT_TRANCHE);

        restTrancheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTranche.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTranche))
            )
            .andExpect(status().isOk());

        // Validate the Tranche in the database
        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeUpdate);
        Tranche testTranche = trancheList.get(trancheList.size() - 1);
        assertThat(testTranche.getNomTranche()).isEqualTo(UPDATED_NOM_TRANCHE);
        assertThat(testTranche.getDateDeffet()).isEqualTo(UPDATED_DATE_DEFFET);
        assertThat(testTranche.getMontantTranche()).isEqualByComparingTo(UPDATED_MONTANT_TRANCHE);
        assertThat(testTranche.getRapportTranche()).isEqualTo(UPDATED_RAPPORT_TRANCHE);
    }

    @Test
    @Transactional
    void fullUpdateTrancheWithPatch() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        int databaseSizeBeforeUpdate = trancheRepository.findAll().size();

        // Update the tranche using partial update
        Tranche partialUpdatedTranche = new Tranche();
        partialUpdatedTranche.setId(tranche.getId());

        partialUpdatedTranche
            .nomTranche(UPDATED_NOM_TRANCHE)
            .dateDeffet(UPDATED_DATE_DEFFET)
            .montantTranche(UPDATED_MONTANT_TRANCHE)
            .rapportTranche(UPDATED_RAPPORT_TRANCHE);

        restTrancheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTranche.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTranche))
            )
            .andExpect(status().isOk());

        // Validate the Tranche in the database
        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeUpdate);
        Tranche testTranche = trancheList.get(trancheList.size() - 1);
        assertThat(testTranche.getNomTranche()).isEqualTo(UPDATED_NOM_TRANCHE);
        assertThat(testTranche.getDateDeffet()).isEqualTo(UPDATED_DATE_DEFFET);
        assertThat(testTranche.getMontantTranche()).isEqualByComparingTo(UPDATED_MONTANT_TRANCHE);
        assertThat(testTranche.getRapportTranche()).isEqualTo(UPDATED_RAPPORT_TRANCHE);
    }

    @Test
    @Transactional
    void patchNonExistingTranche() throws Exception {
        int databaseSizeBeforeUpdate = trancheRepository.findAll().size();
        tranche.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrancheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tranche.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tranche))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tranche in the database
        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTranche() throws Exception {
        int databaseSizeBeforeUpdate = trancheRepository.findAll().size();
        tranche.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrancheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tranche))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tranche in the database
        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTranche() throws Exception {
        int databaseSizeBeforeUpdate = trancheRepository.findAll().size();
        tranche.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrancheMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tranche)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tranche in the database
        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTranche() throws Exception {
        // Initialize the database
        trancheRepository.saveAndFlush(tranche);

        int databaseSizeBeforeDelete = trancheRepository.findAll().size();

        // Delete the tranche
        restTrancheMockMvc
            .perform(delete(ENTITY_API_URL_ID, tranche.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tranche> trancheList = trancheRepository.findAll();
        assertThat(trancheList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
