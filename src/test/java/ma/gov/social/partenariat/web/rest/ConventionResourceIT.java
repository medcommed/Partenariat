package ma.gov.social.partenariat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ma.gov.social.partenariat.IntegrationTest;
import ma.gov.social.partenariat.domain.Convention;
import ma.gov.social.partenariat.domain.Partenaire;
import ma.gov.social.partenariat.domain.Projet;
import ma.gov.social.partenariat.domain.TypeConvention;
import ma.gov.social.partenariat.repository.ConventionRepository;
import ma.gov.social.partenariat.service.ConventionService;
import ma.gov.social.partenariat.service.criteria.ConventionCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ConventionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ConventionResourceIT {

    private static final String DEFAULT_AVEANAU = "AAAAAAAAAA";
    private static final String UPDATED_AVEANAU = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT_CONV = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT_CONV = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT_CONV = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DUREE_CONVENTION = "AAAAAAAAAA";
    private static final String UPDATED_DUREE_CONVENTION = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT_CONVENTION = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_CONVENTION = "BBBBBBBBBB";

    private static final Long DEFAULT_NBR_TRANCHE = 1L;
    private static final Long UPDATED_NBR_TRANCHE = 2L;
    private static final Long SMALLER_NBR_TRANCHE = 1L - 1L;

    private static final String DEFAULT_NOM_CONVENTION = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CONVENTION = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECTIF = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/conventions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConventionRepository conventionRepository;

    @Mock
    private ConventionRepository conventionRepositoryMock;

    @Mock
    private ConventionService conventionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConventionMockMvc;

    private Convention convention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Convention createEntity(EntityManager em) {
        Convention convention = new Convention()
            .aveanau(DEFAULT_AVEANAU)
            .dateDebutConv(DEFAULT_DATE_DEBUT_CONV)
            .dureeConvention(DEFAULT_DUREE_CONVENTION)
            .etatConvention(DEFAULT_ETAT_CONVENTION)
            .nbrTranche(DEFAULT_NBR_TRANCHE)
            .nomConvention(DEFAULT_NOM_CONVENTION)
            .objectif(DEFAULT_OBJECTIF);
        // Add required entity
        Projet projet;
        if (TestUtil.findAll(em, Projet.class).isEmpty()) {
            projet = ProjetResourceIT.createEntity(em);
            em.persist(projet);
            em.flush();
        } else {
            projet = TestUtil.findAll(em, Projet.class).get(0);
        }
        convention.setProjet(projet);
        // Add required entity
        TypeConvention typeConvention;
        if (TestUtil.findAll(em, TypeConvention.class).isEmpty()) {
            typeConvention = TypeConventionResourceIT.createEntity(em);
            em.persist(typeConvention);
            em.flush();
        } else {
            typeConvention = TestUtil.findAll(em, TypeConvention.class).get(0);
        }
        convention.setTypeConvention(typeConvention);
        // Add required entity
        Partenaire partenaire;
        if (TestUtil.findAll(em, Partenaire.class).isEmpty()) {
            partenaire = PartenaireResourceIT.createEntity(em);
            em.persist(partenaire);
            em.flush();
        } else {
            partenaire = TestUtil.findAll(em, Partenaire.class).get(0);
        }
        convention.getPartenaires().add(partenaire);
        return convention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Convention createUpdatedEntity(EntityManager em) {
        Convention convention = new Convention()
            .aveanau(UPDATED_AVEANAU)
            .dateDebutConv(UPDATED_DATE_DEBUT_CONV)
            .dureeConvention(UPDATED_DUREE_CONVENTION)
            .etatConvention(UPDATED_ETAT_CONVENTION)
            .nbrTranche(UPDATED_NBR_TRANCHE)
            .nomConvention(UPDATED_NOM_CONVENTION)
            .objectif(UPDATED_OBJECTIF);
        // Add required entity
        Projet projet;
        if (TestUtil.findAll(em, Projet.class).isEmpty()) {
            projet = ProjetResourceIT.createUpdatedEntity(em);
            em.persist(projet);
            em.flush();
        } else {
            projet = TestUtil.findAll(em, Projet.class).get(0);
        }
        convention.setProjet(projet);
        // Add required entity
        TypeConvention typeConvention;
        if (TestUtil.findAll(em, TypeConvention.class).isEmpty()) {
            typeConvention = TypeConventionResourceIT.createUpdatedEntity(em);
            em.persist(typeConvention);
            em.flush();
        } else {
            typeConvention = TestUtil.findAll(em, TypeConvention.class).get(0);
        }
        convention.setTypeConvention(typeConvention);
        // Add required entity
        Partenaire partenaire;
        if (TestUtil.findAll(em, Partenaire.class).isEmpty()) {
            partenaire = PartenaireResourceIT.createUpdatedEntity(em);
            em.persist(partenaire);
            em.flush();
        } else {
            partenaire = TestUtil.findAll(em, Partenaire.class).get(0);
        }
        convention.getPartenaires().add(partenaire);
        return convention;
    }

    @BeforeEach
    public void initTest() {
        convention = createEntity(em);
    }

    @Test
    @Transactional
    void createConvention() throws Exception {
        int databaseSizeBeforeCreate = conventionRepository.findAll().size();
        // Create the Convention
        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(convention)))
            .andExpect(status().isCreated());

        // Validate the Convention in the database
        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeCreate + 1);
        Convention testConvention = conventionList.get(conventionList.size() - 1);
        assertThat(testConvention.getAveanau()).isEqualTo(DEFAULT_AVEANAU);
        assertThat(testConvention.getDateDebutConv()).isEqualTo(DEFAULT_DATE_DEBUT_CONV);
        assertThat(testConvention.getDureeConvention()).isEqualTo(DEFAULT_DUREE_CONVENTION);
        assertThat(testConvention.getEtatConvention()).isEqualTo(DEFAULT_ETAT_CONVENTION);
        assertThat(testConvention.getNbrTranche()).isEqualTo(DEFAULT_NBR_TRANCHE);
        assertThat(testConvention.getNomConvention()).isEqualTo(DEFAULT_NOM_CONVENTION);
        assertThat(testConvention.getObjectif()).isEqualTo(DEFAULT_OBJECTIF);
    }

    @Test
    @Transactional
    void createConventionWithExistingId() throws Exception {
        // Create the Convention with an existing ID
        convention.setId(1L);

        int databaseSizeBeforeCreate = conventionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(convention)))
            .andExpect(status().isBadRequest());

        // Validate the Convention in the database
        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAveanauIsRequired() throws Exception {
        int databaseSizeBeforeTest = conventionRepository.findAll().size();
        // set the field null
        convention.setAveanau(null);

        // Create the Convention, which fails.

        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(convention)))
            .andExpect(status().isBadRequest());

        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutConvIsRequired() throws Exception {
        int databaseSizeBeforeTest = conventionRepository.findAll().size();
        // set the field null
        convention.setDateDebutConv(null);

        // Create the Convention, which fails.

        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(convention)))
            .andExpect(status().isBadRequest());

        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDureeConventionIsRequired() throws Exception {
        int databaseSizeBeforeTest = conventionRepository.findAll().size();
        // set the field null
        convention.setDureeConvention(null);

        // Create the Convention, which fails.

        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(convention)))
            .andExpect(status().isBadRequest());

        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtatConventionIsRequired() throws Exception {
        int databaseSizeBeforeTest = conventionRepository.findAll().size();
        // set the field null
        convention.setEtatConvention(null);

        // Create the Convention, which fails.

        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(convention)))
            .andExpect(status().isBadRequest());

        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNbrTrancheIsRequired() throws Exception {
        int databaseSizeBeforeTest = conventionRepository.findAll().size();
        // set the field null
        convention.setNbrTranche(null);

        // Create the Convention, which fails.

        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(convention)))
            .andExpect(status().isBadRequest());

        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomConventionIsRequired() throws Exception {
        int databaseSizeBeforeTest = conventionRepository.findAll().size();
        // set the field null
        convention.setNomConvention(null);

        // Create the Convention, which fails.

        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(convention)))
            .andExpect(status().isBadRequest());

        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConventions() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList
        restConventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(convention.getId().intValue())))
            .andExpect(jsonPath("$.[*].aveanau").value(hasItem(DEFAULT_AVEANAU)))
            .andExpect(jsonPath("$.[*].dateDebutConv").value(hasItem(DEFAULT_DATE_DEBUT_CONV.toString())))
            .andExpect(jsonPath("$.[*].dureeConvention").value(hasItem(DEFAULT_DUREE_CONVENTION)))
            .andExpect(jsonPath("$.[*].etatConvention").value(hasItem(DEFAULT_ETAT_CONVENTION)))
            .andExpect(jsonPath("$.[*].nbrTranche").value(hasItem(DEFAULT_NBR_TRANCHE.intValue())))
            .andExpect(jsonPath("$.[*].nomConvention").value(hasItem(DEFAULT_NOM_CONVENTION)))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConventionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(conventionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConventionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(conventionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConventionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(conventionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConventionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(conventionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getConvention() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get the convention
        restConventionMockMvc
            .perform(get(ENTITY_API_URL_ID, convention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(convention.getId().intValue()))
            .andExpect(jsonPath("$.aveanau").value(DEFAULT_AVEANAU))
            .andExpect(jsonPath("$.dateDebutConv").value(DEFAULT_DATE_DEBUT_CONV.toString()))
            .andExpect(jsonPath("$.dureeConvention").value(DEFAULT_DUREE_CONVENTION))
            .andExpect(jsonPath("$.etatConvention").value(DEFAULT_ETAT_CONVENTION))
            .andExpect(jsonPath("$.nbrTranche").value(DEFAULT_NBR_TRANCHE.intValue()))
            .andExpect(jsonPath("$.nomConvention").value(DEFAULT_NOM_CONVENTION))
            .andExpect(jsonPath("$.objectif").value(DEFAULT_OBJECTIF.toString()));
    }

    @Test
    @Transactional
    void getConventionsByIdFiltering() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        Long id = convention.getId();

        defaultConventionShouldBeFound("id.equals=" + id);
        defaultConventionShouldNotBeFound("id.notEquals=" + id);

        defaultConventionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConventionShouldNotBeFound("id.greaterThan=" + id);

        defaultConventionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConventionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConventionsByAveanauIsEqualToSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where aveanau equals to DEFAULT_AVEANAU
        defaultConventionShouldBeFound("aveanau.equals=" + DEFAULT_AVEANAU);

        // Get all the conventionList where aveanau equals to UPDATED_AVEANAU
        defaultConventionShouldNotBeFound("aveanau.equals=" + UPDATED_AVEANAU);
    }

    @Test
    @Transactional
    void getAllConventionsByAveanauIsInShouldWork() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where aveanau in DEFAULT_AVEANAU or UPDATED_AVEANAU
        defaultConventionShouldBeFound("aveanau.in=" + DEFAULT_AVEANAU + "," + UPDATED_AVEANAU);

        // Get all the conventionList where aveanau equals to UPDATED_AVEANAU
        defaultConventionShouldNotBeFound("aveanau.in=" + UPDATED_AVEANAU);
    }

    @Test
    @Transactional
    void getAllConventionsByAveanauIsNullOrNotNull() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where aveanau is not null
        defaultConventionShouldBeFound("aveanau.specified=true");

        // Get all the conventionList where aveanau is null
        defaultConventionShouldNotBeFound("aveanau.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByAveanauContainsSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where aveanau contains DEFAULT_AVEANAU
        defaultConventionShouldBeFound("aveanau.contains=" + DEFAULT_AVEANAU);

        // Get all the conventionList where aveanau contains UPDATED_AVEANAU
        defaultConventionShouldNotBeFound("aveanau.contains=" + UPDATED_AVEANAU);
    }

    @Test
    @Transactional
    void getAllConventionsByAveanauNotContainsSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where aveanau does not contain DEFAULT_AVEANAU
        defaultConventionShouldNotBeFound("aveanau.doesNotContain=" + DEFAULT_AVEANAU);

        // Get all the conventionList where aveanau does not contain UPDATED_AVEANAU
        defaultConventionShouldBeFound("aveanau.doesNotContain=" + UPDATED_AVEANAU);
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsEqualToSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where dateDebutConv equals to DEFAULT_DATE_DEBUT_CONV
        defaultConventionShouldBeFound("dateDebutConv.equals=" + DEFAULT_DATE_DEBUT_CONV);

        // Get all the conventionList where dateDebutConv equals to UPDATED_DATE_DEBUT_CONV
        defaultConventionShouldNotBeFound("dateDebutConv.equals=" + UPDATED_DATE_DEBUT_CONV);
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsInShouldWork() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where dateDebutConv in DEFAULT_DATE_DEBUT_CONV or UPDATED_DATE_DEBUT_CONV
        defaultConventionShouldBeFound("dateDebutConv.in=" + DEFAULT_DATE_DEBUT_CONV + "," + UPDATED_DATE_DEBUT_CONV);

        // Get all the conventionList where dateDebutConv equals to UPDATED_DATE_DEBUT_CONV
        defaultConventionShouldNotBeFound("dateDebutConv.in=" + UPDATED_DATE_DEBUT_CONV);
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsNullOrNotNull() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where dateDebutConv is not null
        defaultConventionShouldBeFound("dateDebutConv.specified=true");

        // Get all the conventionList where dateDebutConv is null
        defaultConventionShouldNotBeFound("dateDebutConv.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where dateDebutConv is greater than or equal to DEFAULT_DATE_DEBUT_CONV
        defaultConventionShouldBeFound("dateDebutConv.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT_CONV);

        // Get all the conventionList where dateDebutConv is greater than or equal to UPDATED_DATE_DEBUT_CONV
        defaultConventionShouldNotBeFound("dateDebutConv.greaterThanOrEqual=" + UPDATED_DATE_DEBUT_CONV);
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where dateDebutConv is less than or equal to DEFAULT_DATE_DEBUT_CONV
        defaultConventionShouldBeFound("dateDebutConv.lessThanOrEqual=" + DEFAULT_DATE_DEBUT_CONV);

        // Get all the conventionList where dateDebutConv is less than or equal to SMALLER_DATE_DEBUT_CONV
        defaultConventionShouldNotBeFound("dateDebutConv.lessThanOrEqual=" + SMALLER_DATE_DEBUT_CONV);
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsLessThanSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where dateDebutConv is less than DEFAULT_DATE_DEBUT_CONV
        defaultConventionShouldNotBeFound("dateDebutConv.lessThan=" + DEFAULT_DATE_DEBUT_CONV);

        // Get all the conventionList where dateDebutConv is less than UPDATED_DATE_DEBUT_CONV
        defaultConventionShouldBeFound("dateDebutConv.lessThan=" + UPDATED_DATE_DEBUT_CONV);
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsGreaterThanSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where dateDebutConv is greater than DEFAULT_DATE_DEBUT_CONV
        defaultConventionShouldNotBeFound("dateDebutConv.greaterThan=" + DEFAULT_DATE_DEBUT_CONV);

        // Get all the conventionList where dateDebutConv is greater than SMALLER_DATE_DEBUT_CONV
        defaultConventionShouldBeFound("dateDebutConv.greaterThan=" + SMALLER_DATE_DEBUT_CONV);
    }

    @Test
    @Transactional
    void getAllConventionsByDureeConventionIsEqualToSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where dureeConvention equals to DEFAULT_DUREE_CONVENTION
        defaultConventionShouldBeFound("dureeConvention.equals=" + DEFAULT_DUREE_CONVENTION);

        // Get all the conventionList where dureeConvention equals to UPDATED_DUREE_CONVENTION
        defaultConventionShouldNotBeFound("dureeConvention.equals=" + UPDATED_DUREE_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByDureeConventionIsInShouldWork() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where dureeConvention in DEFAULT_DUREE_CONVENTION or UPDATED_DUREE_CONVENTION
        defaultConventionShouldBeFound("dureeConvention.in=" + DEFAULT_DUREE_CONVENTION + "," + UPDATED_DUREE_CONVENTION);

        // Get all the conventionList where dureeConvention equals to UPDATED_DUREE_CONVENTION
        defaultConventionShouldNotBeFound("dureeConvention.in=" + UPDATED_DUREE_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByDureeConventionIsNullOrNotNull() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where dureeConvention is not null
        defaultConventionShouldBeFound("dureeConvention.specified=true");

        // Get all the conventionList where dureeConvention is null
        defaultConventionShouldNotBeFound("dureeConvention.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByDureeConventionContainsSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where dureeConvention contains DEFAULT_DUREE_CONVENTION
        defaultConventionShouldBeFound("dureeConvention.contains=" + DEFAULT_DUREE_CONVENTION);

        // Get all the conventionList where dureeConvention contains UPDATED_DUREE_CONVENTION
        defaultConventionShouldNotBeFound("dureeConvention.contains=" + UPDATED_DUREE_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByDureeConventionNotContainsSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where dureeConvention does not contain DEFAULT_DUREE_CONVENTION
        defaultConventionShouldNotBeFound("dureeConvention.doesNotContain=" + DEFAULT_DUREE_CONVENTION);

        // Get all the conventionList where dureeConvention does not contain UPDATED_DUREE_CONVENTION
        defaultConventionShouldBeFound("dureeConvention.doesNotContain=" + UPDATED_DUREE_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByEtatConventionIsEqualToSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where etatConvention equals to DEFAULT_ETAT_CONVENTION
        defaultConventionShouldBeFound("etatConvention.equals=" + DEFAULT_ETAT_CONVENTION);

        // Get all the conventionList where etatConvention equals to UPDATED_ETAT_CONVENTION
        defaultConventionShouldNotBeFound("etatConvention.equals=" + UPDATED_ETAT_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByEtatConventionIsInShouldWork() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where etatConvention in DEFAULT_ETAT_CONVENTION or UPDATED_ETAT_CONVENTION
        defaultConventionShouldBeFound("etatConvention.in=" + DEFAULT_ETAT_CONVENTION + "," + UPDATED_ETAT_CONVENTION);

        // Get all the conventionList where etatConvention equals to UPDATED_ETAT_CONVENTION
        defaultConventionShouldNotBeFound("etatConvention.in=" + UPDATED_ETAT_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByEtatConventionIsNullOrNotNull() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where etatConvention is not null
        defaultConventionShouldBeFound("etatConvention.specified=true");

        // Get all the conventionList where etatConvention is null
        defaultConventionShouldNotBeFound("etatConvention.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByEtatConventionContainsSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where etatConvention contains DEFAULT_ETAT_CONVENTION
        defaultConventionShouldBeFound("etatConvention.contains=" + DEFAULT_ETAT_CONVENTION);

        // Get all the conventionList where etatConvention contains UPDATED_ETAT_CONVENTION
        defaultConventionShouldNotBeFound("etatConvention.contains=" + UPDATED_ETAT_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByEtatConventionNotContainsSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where etatConvention does not contain DEFAULT_ETAT_CONVENTION
        defaultConventionShouldNotBeFound("etatConvention.doesNotContain=" + DEFAULT_ETAT_CONVENTION);

        // Get all the conventionList where etatConvention does not contain UPDATED_ETAT_CONVENTION
        defaultConventionShouldBeFound("etatConvention.doesNotContain=" + UPDATED_ETAT_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByNbrTrancheIsEqualToSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where nbrTranche equals to DEFAULT_NBR_TRANCHE
        defaultConventionShouldBeFound("nbrTranche.equals=" + DEFAULT_NBR_TRANCHE);

        // Get all the conventionList where nbrTranche equals to UPDATED_NBR_TRANCHE
        defaultConventionShouldNotBeFound("nbrTranche.equals=" + UPDATED_NBR_TRANCHE);
    }

    @Test
    @Transactional
    void getAllConventionsByNbrTrancheIsInShouldWork() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where nbrTranche in DEFAULT_NBR_TRANCHE or UPDATED_NBR_TRANCHE
        defaultConventionShouldBeFound("nbrTranche.in=" + DEFAULT_NBR_TRANCHE + "," + UPDATED_NBR_TRANCHE);

        // Get all the conventionList where nbrTranche equals to UPDATED_NBR_TRANCHE
        defaultConventionShouldNotBeFound("nbrTranche.in=" + UPDATED_NBR_TRANCHE);
    }

    @Test
    @Transactional
    void getAllConventionsByNbrTrancheIsNullOrNotNull() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where nbrTranche is not null
        defaultConventionShouldBeFound("nbrTranche.specified=true");

        // Get all the conventionList where nbrTranche is null
        defaultConventionShouldNotBeFound("nbrTranche.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByNbrTrancheIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where nbrTranche is greater than or equal to DEFAULT_NBR_TRANCHE
        defaultConventionShouldBeFound("nbrTranche.greaterThanOrEqual=" + DEFAULT_NBR_TRANCHE);

        // Get all the conventionList where nbrTranche is greater than or equal to UPDATED_NBR_TRANCHE
        defaultConventionShouldNotBeFound("nbrTranche.greaterThanOrEqual=" + UPDATED_NBR_TRANCHE);
    }

    @Test
    @Transactional
    void getAllConventionsByNbrTrancheIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where nbrTranche is less than or equal to DEFAULT_NBR_TRANCHE
        defaultConventionShouldBeFound("nbrTranche.lessThanOrEqual=" + DEFAULT_NBR_TRANCHE);

        // Get all the conventionList where nbrTranche is less than or equal to SMALLER_NBR_TRANCHE
        defaultConventionShouldNotBeFound("nbrTranche.lessThanOrEqual=" + SMALLER_NBR_TRANCHE);
    }

    @Test
    @Transactional
    void getAllConventionsByNbrTrancheIsLessThanSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where nbrTranche is less than DEFAULT_NBR_TRANCHE
        defaultConventionShouldNotBeFound("nbrTranche.lessThan=" + DEFAULT_NBR_TRANCHE);

        // Get all the conventionList where nbrTranche is less than UPDATED_NBR_TRANCHE
        defaultConventionShouldBeFound("nbrTranche.lessThan=" + UPDATED_NBR_TRANCHE);
    }

    @Test
    @Transactional
    void getAllConventionsByNbrTrancheIsGreaterThanSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where nbrTranche is greater than DEFAULT_NBR_TRANCHE
        defaultConventionShouldNotBeFound("nbrTranche.greaterThan=" + DEFAULT_NBR_TRANCHE);

        // Get all the conventionList where nbrTranche is greater than SMALLER_NBR_TRANCHE
        defaultConventionShouldBeFound("nbrTranche.greaterThan=" + SMALLER_NBR_TRANCHE);
    }

    @Test
    @Transactional
    void getAllConventionsByNomConventionIsEqualToSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where nomConvention equals to DEFAULT_NOM_CONVENTION
        defaultConventionShouldBeFound("nomConvention.equals=" + DEFAULT_NOM_CONVENTION);

        // Get all the conventionList where nomConvention equals to UPDATED_NOM_CONVENTION
        defaultConventionShouldNotBeFound("nomConvention.equals=" + UPDATED_NOM_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByNomConventionIsInShouldWork() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where nomConvention in DEFAULT_NOM_CONVENTION or UPDATED_NOM_CONVENTION
        defaultConventionShouldBeFound("nomConvention.in=" + DEFAULT_NOM_CONVENTION + "," + UPDATED_NOM_CONVENTION);

        // Get all the conventionList where nomConvention equals to UPDATED_NOM_CONVENTION
        defaultConventionShouldNotBeFound("nomConvention.in=" + UPDATED_NOM_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByNomConventionIsNullOrNotNull() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where nomConvention is not null
        defaultConventionShouldBeFound("nomConvention.specified=true");

        // Get all the conventionList where nomConvention is null
        defaultConventionShouldNotBeFound("nomConvention.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByNomConventionContainsSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where nomConvention contains DEFAULT_NOM_CONVENTION
        defaultConventionShouldBeFound("nomConvention.contains=" + DEFAULT_NOM_CONVENTION);

        // Get all the conventionList where nomConvention contains UPDATED_NOM_CONVENTION
        defaultConventionShouldNotBeFound("nomConvention.contains=" + UPDATED_NOM_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByNomConventionNotContainsSomething() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        // Get all the conventionList where nomConvention does not contain DEFAULT_NOM_CONVENTION
        defaultConventionShouldNotBeFound("nomConvention.doesNotContain=" + DEFAULT_NOM_CONVENTION);

        // Get all the conventionList where nomConvention does not contain UPDATED_NOM_CONVENTION
        defaultConventionShouldBeFound("nomConvention.doesNotContain=" + UPDATED_NOM_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByProjetIsEqualToSomething() throws Exception {
        Projet projet;
        if (TestUtil.findAll(em, Projet.class).isEmpty()) {
            conventionRepository.saveAndFlush(convention);
            projet = ProjetResourceIT.createEntity(em);
        } else {
            projet = TestUtil.findAll(em, Projet.class).get(0);
        }
        em.persist(projet);
        em.flush();
        convention.setProjet(projet);
        conventionRepository.saveAndFlush(convention);
        Long projetId = projet.getId();

        // Get all the conventionList where projet equals to projetId
        defaultConventionShouldBeFound("projetId.equals=" + projetId);

        // Get all the conventionList where projet equals to (projetId + 1)
        defaultConventionShouldNotBeFound("projetId.equals=" + (projetId + 1));
    }

    @Test
    @Transactional
    void getAllConventionsByTypeConventionIsEqualToSomething() throws Exception {
        TypeConvention typeConvention;
        if (TestUtil.findAll(em, TypeConvention.class).isEmpty()) {
            conventionRepository.saveAndFlush(convention);
            typeConvention = TypeConventionResourceIT.createEntity(em);
        } else {
            typeConvention = TestUtil.findAll(em, TypeConvention.class).get(0);
        }
        em.persist(typeConvention);
        em.flush();
        convention.setTypeConvention(typeConvention);
        conventionRepository.saveAndFlush(convention);
        Long typeConventionId = typeConvention.getId();

        // Get all the conventionList where typeConvention equals to typeConventionId
        defaultConventionShouldBeFound("typeConventionId.equals=" + typeConventionId);

        // Get all the conventionList where typeConvention equals to (typeConventionId + 1)
        defaultConventionShouldNotBeFound("typeConventionId.equals=" + (typeConventionId + 1));
    }

    @Test
    @Transactional
    void getAllConventionsByPartenaireIsEqualToSomething() throws Exception {
        Partenaire partenaire;
        if (TestUtil.findAll(em, Partenaire.class).isEmpty()) {
            conventionRepository.saveAndFlush(convention);
            partenaire = PartenaireResourceIT.createEntity(em);
        } else {
            partenaire = TestUtil.findAll(em, Partenaire.class).get(0);
        }
        em.persist(partenaire);
        em.flush();
        convention.addPartenaire(partenaire);
        conventionRepository.saveAndFlush(convention);
        Long partenaireId = partenaire.getId();

        // Get all the conventionList where partenaire equals to partenaireId
        defaultConventionShouldBeFound("partenaireId.equals=" + partenaireId);

        // Get all the conventionList where partenaire equals to (partenaireId + 1)
        defaultConventionShouldNotBeFound("partenaireId.equals=" + (partenaireId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConventionShouldBeFound(String filter) throws Exception {
        restConventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(convention.getId().intValue())))
            .andExpect(jsonPath("$.[*].aveanau").value(hasItem(DEFAULT_AVEANAU)))
            .andExpect(jsonPath("$.[*].dateDebutConv").value(hasItem(DEFAULT_DATE_DEBUT_CONV.toString())))
            .andExpect(jsonPath("$.[*].dureeConvention").value(hasItem(DEFAULT_DUREE_CONVENTION)))
            .andExpect(jsonPath("$.[*].etatConvention").value(hasItem(DEFAULT_ETAT_CONVENTION)))
            .andExpect(jsonPath("$.[*].nbrTranche").value(hasItem(DEFAULT_NBR_TRANCHE.intValue())))
            .andExpect(jsonPath("$.[*].nomConvention").value(hasItem(DEFAULT_NOM_CONVENTION)))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.toString())));

        // Check, that the count call also returns 1
        restConventionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConventionShouldNotBeFound(String filter) throws Exception {
        restConventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConventionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConvention() throws Exception {
        // Get the convention
        restConventionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConvention() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        int databaseSizeBeforeUpdate = conventionRepository.findAll().size();

        // Update the convention
        Convention updatedConvention = conventionRepository.findById(convention.getId()).get();
        // Disconnect from session so that the updates on updatedConvention are not directly saved in db
        em.detach(updatedConvention);
        updatedConvention
            .aveanau(UPDATED_AVEANAU)
            .dateDebutConv(UPDATED_DATE_DEBUT_CONV)
            .dureeConvention(UPDATED_DUREE_CONVENTION)
            .etatConvention(UPDATED_ETAT_CONVENTION)
            .nbrTranche(UPDATED_NBR_TRANCHE)
            .nomConvention(UPDATED_NOM_CONVENTION)
            .objectif(UPDATED_OBJECTIF);

        restConventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConvention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConvention))
            )
            .andExpect(status().isOk());

        // Validate the Convention in the database
        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeUpdate);
        Convention testConvention = conventionList.get(conventionList.size() - 1);
        assertThat(testConvention.getAveanau()).isEqualTo(UPDATED_AVEANAU);
        assertThat(testConvention.getDateDebutConv()).isEqualTo(UPDATED_DATE_DEBUT_CONV);
        assertThat(testConvention.getDureeConvention()).isEqualTo(UPDATED_DUREE_CONVENTION);
        assertThat(testConvention.getEtatConvention()).isEqualTo(UPDATED_ETAT_CONVENTION);
        assertThat(testConvention.getNbrTranche()).isEqualTo(UPDATED_NBR_TRANCHE);
        assertThat(testConvention.getNomConvention()).isEqualTo(UPDATED_NOM_CONVENTION);
        assertThat(testConvention.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
    }

    @Test
    @Transactional
    void putNonExistingConvention() throws Exception {
        int databaseSizeBeforeUpdate = conventionRepository.findAll().size();
        convention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, convention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(convention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Convention in the database
        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConvention() throws Exception {
        int databaseSizeBeforeUpdate = conventionRepository.findAll().size();
        convention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(convention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Convention in the database
        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConvention() throws Exception {
        int databaseSizeBeforeUpdate = conventionRepository.findAll().size();
        convention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConventionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(convention)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Convention in the database
        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConventionWithPatch() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        int databaseSizeBeforeUpdate = conventionRepository.findAll().size();

        // Update the convention using partial update
        Convention partialUpdatedConvention = new Convention();
        partialUpdatedConvention.setId(convention.getId());

        partialUpdatedConvention
            .aveanau(UPDATED_AVEANAU)
            .dateDebutConv(UPDATED_DATE_DEBUT_CONV)
            .dureeConvention(UPDATED_DUREE_CONVENTION)
            .etatConvention(UPDATED_ETAT_CONVENTION)
            .nbrTranche(UPDATED_NBR_TRANCHE)
            .objectif(UPDATED_OBJECTIF);

        restConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConvention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConvention))
            )
            .andExpect(status().isOk());

        // Validate the Convention in the database
        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeUpdate);
        Convention testConvention = conventionList.get(conventionList.size() - 1);
        assertThat(testConvention.getAveanau()).isEqualTo(UPDATED_AVEANAU);
        assertThat(testConvention.getDateDebutConv()).isEqualTo(UPDATED_DATE_DEBUT_CONV);
        assertThat(testConvention.getDureeConvention()).isEqualTo(UPDATED_DUREE_CONVENTION);
        assertThat(testConvention.getEtatConvention()).isEqualTo(UPDATED_ETAT_CONVENTION);
        assertThat(testConvention.getNbrTranche()).isEqualTo(UPDATED_NBR_TRANCHE);
        assertThat(testConvention.getNomConvention()).isEqualTo(DEFAULT_NOM_CONVENTION);
        assertThat(testConvention.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
    }

    @Test
    @Transactional
    void fullUpdateConventionWithPatch() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        int databaseSizeBeforeUpdate = conventionRepository.findAll().size();

        // Update the convention using partial update
        Convention partialUpdatedConvention = new Convention();
        partialUpdatedConvention.setId(convention.getId());

        partialUpdatedConvention
            .aveanau(UPDATED_AVEANAU)
            .dateDebutConv(UPDATED_DATE_DEBUT_CONV)
            .dureeConvention(UPDATED_DUREE_CONVENTION)
            .etatConvention(UPDATED_ETAT_CONVENTION)
            .nbrTranche(UPDATED_NBR_TRANCHE)
            .nomConvention(UPDATED_NOM_CONVENTION)
            .objectif(UPDATED_OBJECTIF);

        restConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConvention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConvention))
            )
            .andExpect(status().isOk());

        // Validate the Convention in the database
        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeUpdate);
        Convention testConvention = conventionList.get(conventionList.size() - 1);
        assertThat(testConvention.getAveanau()).isEqualTo(UPDATED_AVEANAU);
        assertThat(testConvention.getDateDebutConv()).isEqualTo(UPDATED_DATE_DEBUT_CONV);
        assertThat(testConvention.getDureeConvention()).isEqualTo(UPDATED_DUREE_CONVENTION);
        assertThat(testConvention.getEtatConvention()).isEqualTo(UPDATED_ETAT_CONVENTION);
        assertThat(testConvention.getNbrTranche()).isEqualTo(UPDATED_NBR_TRANCHE);
        assertThat(testConvention.getNomConvention()).isEqualTo(UPDATED_NOM_CONVENTION);
        assertThat(testConvention.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
    }

    @Test
    @Transactional
    void patchNonExistingConvention() throws Exception {
        int databaseSizeBeforeUpdate = conventionRepository.findAll().size();
        convention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, convention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(convention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Convention in the database
        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConvention() throws Exception {
        int databaseSizeBeforeUpdate = conventionRepository.findAll().size();
        convention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(convention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Convention in the database
        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConvention() throws Exception {
        int databaseSizeBeforeUpdate = conventionRepository.findAll().size();
        convention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConventionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(convention))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Convention in the database
        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConvention() throws Exception {
        // Initialize the database
        conventionRepository.saveAndFlush(convention);

        int databaseSizeBeforeDelete = conventionRepository.findAll().size();

        // Delete the convention
        restConventionMockMvc
            .perform(delete(ENTITY_API_URL_ID, convention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Convention> conventionList = conventionRepository.findAll();
        assertThat(conventionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
