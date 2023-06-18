package ma.gov.social.partenariat.web.rest;

import static ma.gov.social.partenariat.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ma.gov.social.partenariat.IntegrationTest;
import ma.gov.social.partenariat.domain.Commune;
import ma.gov.social.partenariat.domain.Convention;
import ma.gov.social.partenariat.domain.DomaineProjet;
import ma.gov.social.partenariat.domain.Projet;
import ma.gov.social.partenariat.domain.SituationProjet;
import ma.gov.social.partenariat.domain.Tranche;
import ma.gov.social.partenariat.repository.ProjetRepository;
import ma.gov.social.partenariat.service.ProjetService;
import ma.gov.social.partenariat.service.criteria.ProjetCriteria;
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
 * Integration tests for the {@link ProjetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProjetResourceIT {

    private static final String DEFAULT_NOM_PROJET = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PROJET = "BBBBBBBBBB";

    private static final String DEFAULT_ANNEE_PROJET = "AAAAAAAAAA";
    private static final String UPDATED_ANNEE_PROJET = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DUREE_PROJET = "AAAAAAAAAA";
    private static final String UPDATED_DUREE_PROJET = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MONTANT_PROJET = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_PROJET = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTANT_PROJET = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/projets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjetRepository projetRepository;

    @Mock
    private ProjetRepository projetRepositoryMock;

    @Mock
    private ProjetService projetServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjetMockMvc;

    private Projet projet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projet createEntity(EntityManager em) {
        Projet projet = new Projet()
            .nomProjet(DEFAULT_NOM_PROJET)
            .anneeProjet(DEFAULT_ANNEE_PROJET)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dureeProjet(DEFAULT_DUREE_PROJET)
            .montantProjet(DEFAULT_MONTANT_PROJET);
        return projet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projet createUpdatedEntity(EntityManager em) {
        Projet projet = new Projet()
            .nomProjet(UPDATED_NOM_PROJET)
            .anneeProjet(UPDATED_ANNEE_PROJET)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dureeProjet(UPDATED_DUREE_PROJET)
            .montantProjet(UPDATED_MONTANT_PROJET);
        return projet;
    }

    @BeforeEach
    public void initTest() {
        projet = createEntity(em);
    }

    @Test
    @Transactional
    void createProjet() throws Exception {
        int databaseSizeBeforeCreate = projetRepository.findAll().size();
        // Create the Projet
        restProjetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isCreated());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeCreate + 1);
        Projet testProjet = projetList.get(projetList.size() - 1);
        assertThat(testProjet.getNomProjet()).isEqualTo(DEFAULT_NOM_PROJET);
        assertThat(testProjet.getAnneeProjet()).isEqualTo(DEFAULT_ANNEE_PROJET);
        assertThat(testProjet.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testProjet.getDureeProjet()).isEqualTo(DEFAULT_DUREE_PROJET);
        assertThat(testProjet.getMontantProjet()).isEqualByComparingTo(DEFAULT_MONTANT_PROJET);
    }

    @Test
    @Transactional
    void createProjetWithExistingId() throws Exception {
        // Create the Projet with an existing ID
        projet.setId(1L);

        int databaseSizeBeforeCreate = projetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isBadRequest());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomProjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetRepository.findAll().size();
        // set the field null
        projet.setNomProjet(null);

        // Create the Projet, which fails.

        restProjetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isBadRequest());

        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnneeProjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetRepository.findAll().size();
        // set the field null
        projet.setAnneeProjet(null);

        // Create the Projet, which fails.

        restProjetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isBadRequest());

        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetRepository.findAll().size();
        // set the field null
        projet.setDateDebut(null);

        // Create the Projet, which fails.

        restProjetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isBadRequest());

        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDureeProjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetRepository.findAll().size();
        // set the field null
        projet.setDureeProjet(null);

        // Create the Projet, which fails.

        restProjetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isBadRequest());

        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantProjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetRepository.findAll().size();
        // set the field null
        projet.setMontantProjet(null);

        // Create the Projet, which fails.

        restProjetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isBadRequest());

        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjets() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList
        restProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projet.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomProjet").value(hasItem(DEFAULT_NOM_PROJET)))
            .andExpect(jsonPath("$.[*].anneeProjet").value(hasItem(DEFAULT_ANNEE_PROJET)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dureeProjet").value(hasItem(DEFAULT_DUREE_PROJET)))
            .andExpect(jsonPath("$.[*].montantProjet").value(hasItem(sameNumber(DEFAULT_MONTANT_PROJET))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjetsWithEagerRelationshipsIsEnabled() throws Exception {
        when(projetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(projetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(projetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(projetRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get the projet
        restProjetMockMvc
            .perform(get(ENTITY_API_URL_ID, projet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projet.getId().intValue()))
            .andExpect(jsonPath("$.nomProjet").value(DEFAULT_NOM_PROJET))
            .andExpect(jsonPath("$.anneeProjet").value(DEFAULT_ANNEE_PROJET))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dureeProjet").value(DEFAULT_DUREE_PROJET))
            .andExpect(jsonPath("$.montantProjet").value(sameNumber(DEFAULT_MONTANT_PROJET)));
    }

    @Test
    @Transactional
    void getProjetsByIdFiltering() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        Long id = projet.getId();

        defaultProjetShouldBeFound("id.equals=" + id);
        defaultProjetShouldNotBeFound("id.notEquals=" + id);

        defaultProjetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProjetShouldNotBeFound("id.greaterThan=" + id);

        defaultProjetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProjetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProjetsByNomProjetIsEqualToSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where nomProjet equals to DEFAULT_NOM_PROJET
        defaultProjetShouldBeFound("nomProjet.equals=" + DEFAULT_NOM_PROJET);

        // Get all the projetList where nomProjet equals to UPDATED_NOM_PROJET
        defaultProjetShouldNotBeFound("nomProjet.equals=" + UPDATED_NOM_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByNomProjetIsInShouldWork() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where nomProjet in DEFAULT_NOM_PROJET or UPDATED_NOM_PROJET
        defaultProjetShouldBeFound("nomProjet.in=" + DEFAULT_NOM_PROJET + "," + UPDATED_NOM_PROJET);

        // Get all the projetList where nomProjet equals to UPDATED_NOM_PROJET
        defaultProjetShouldNotBeFound("nomProjet.in=" + UPDATED_NOM_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByNomProjetIsNullOrNotNull() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where nomProjet is not null
        defaultProjetShouldBeFound("nomProjet.specified=true");

        // Get all the projetList where nomProjet is null
        defaultProjetShouldNotBeFound("nomProjet.specified=false");
    }

    @Test
    @Transactional
    void getAllProjetsByNomProjetContainsSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where nomProjet contains DEFAULT_NOM_PROJET
        defaultProjetShouldBeFound("nomProjet.contains=" + DEFAULT_NOM_PROJET);

        // Get all the projetList where nomProjet contains UPDATED_NOM_PROJET
        defaultProjetShouldNotBeFound("nomProjet.contains=" + UPDATED_NOM_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByNomProjetNotContainsSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where nomProjet does not contain DEFAULT_NOM_PROJET
        defaultProjetShouldNotBeFound("nomProjet.doesNotContain=" + DEFAULT_NOM_PROJET);

        // Get all the projetList where nomProjet does not contain UPDATED_NOM_PROJET
        defaultProjetShouldBeFound("nomProjet.doesNotContain=" + UPDATED_NOM_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByAnneeProjetIsEqualToSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where anneeProjet equals to DEFAULT_ANNEE_PROJET
        defaultProjetShouldBeFound("anneeProjet.equals=" + DEFAULT_ANNEE_PROJET);

        // Get all the projetList where anneeProjet equals to UPDATED_ANNEE_PROJET
        defaultProjetShouldNotBeFound("anneeProjet.equals=" + UPDATED_ANNEE_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByAnneeProjetIsInShouldWork() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where anneeProjet in DEFAULT_ANNEE_PROJET or UPDATED_ANNEE_PROJET
        defaultProjetShouldBeFound("anneeProjet.in=" + DEFAULT_ANNEE_PROJET + "," + UPDATED_ANNEE_PROJET);

        // Get all the projetList where anneeProjet equals to UPDATED_ANNEE_PROJET
        defaultProjetShouldNotBeFound("anneeProjet.in=" + UPDATED_ANNEE_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByAnneeProjetIsNullOrNotNull() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where anneeProjet is not null
        defaultProjetShouldBeFound("anneeProjet.specified=true");

        // Get all the projetList where anneeProjet is null
        defaultProjetShouldNotBeFound("anneeProjet.specified=false");
    }

    @Test
    @Transactional
    void getAllProjetsByAnneeProjetContainsSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where anneeProjet contains DEFAULT_ANNEE_PROJET
        defaultProjetShouldBeFound("anneeProjet.contains=" + DEFAULT_ANNEE_PROJET);

        // Get all the projetList where anneeProjet contains UPDATED_ANNEE_PROJET
        defaultProjetShouldNotBeFound("anneeProjet.contains=" + UPDATED_ANNEE_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByAnneeProjetNotContainsSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where anneeProjet does not contain DEFAULT_ANNEE_PROJET
        defaultProjetShouldNotBeFound("anneeProjet.doesNotContain=" + DEFAULT_ANNEE_PROJET);

        // Get all the projetList where anneeProjet does not contain UPDATED_ANNEE_PROJET
        defaultProjetShouldBeFound("anneeProjet.doesNotContain=" + UPDATED_ANNEE_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultProjetShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the projetList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultProjetShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllProjetsByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultProjetShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the projetList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultProjetShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllProjetsByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where dateDebut is not null
        defaultProjetShouldBeFound("dateDebut.specified=true");

        // Get all the projetList where dateDebut is null
        defaultProjetShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllProjetsByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where dateDebut is greater than or equal to DEFAULT_DATE_DEBUT
        defaultProjetShouldBeFound("dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the projetList where dateDebut is greater than or equal to UPDATED_DATE_DEBUT
        defaultProjetShouldNotBeFound("dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllProjetsByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where dateDebut is less than or equal to DEFAULT_DATE_DEBUT
        defaultProjetShouldBeFound("dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the projetList where dateDebut is less than or equal to SMALLER_DATE_DEBUT
        defaultProjetShouldNotBeFound("dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllProjetsByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where dateDebut is less than DEFAULT_DATE_DEBUT
        defaultProjetShouldNotBeFound("dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);

        // Get all the projetList where dateDebut is less than UPDATED_DATE_DEBUT
        defaultProjetShouldBeFound("dateDebut.lessThan=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllProjetsByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where dateDebut is greater than DEFAULT_DATE_DEBUT
        defaultProjetShouldNotBeFound("dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);

        // Get all the projetList where dateDebut is greater than SMALLER_DATE_DEBUT
        defaultProjetShouldBeFound("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllProjetsByDureeProjetIsEqualToSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where dureeProjet equals to DEFAULT_DUREE_PROJET
        defaultProjetShouldBeFound("dureeProjet.equals=" + DEFAULT_DUREE_PROJET);

        // Get all the projetList where dureeProjet equals to UPDATED_DUREE_PROJET
        defaultProjetShouldNotBeFound("dureeProjet.equals=" + UPDATED_DUREE_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByDureeProjetIsInShouldWork() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where dureeProjet in DEFAULT_DUREE_PROJET or UPDATED_DUREE_PROJET
        defaultProjetShouldBeFound("dureeProjet.in=" + DEFAULT_DUREE_PROJET + "," + UPDATED_DUREE_PROJET);

        // Get all the projetList where dureeProjet equals to UPDATED_DUREE_PROJET
        defaultProjetShouldNotBeFound("dureeProjet.in=" + UPDATED_DUREE_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByDureeProjetIsNullOrNotNull() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where dureeProjet is not null
        defaultProjetShouldBeFound("dureeProjet.specified=true");

        // Get all the projetList where dureeProjet is null
        defaultProjetShouldNotBeFound("dureeProjet.specified=false");
    }

    @Test
    @Transactional
    void getAllProjetsByDureeProjetContainsSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where dureeProjet contains DEFAULT_DUREE_PROJET
        defaultProjetShouldBeFound("dureeProjet.contains=" + DEFAULT_DUREE_PROJET);

        // Get all the projetList where dureeProjet contains UPDATED_DUREE_PROJET
        defaultProjetShouldNotBeFound("dureeProjet.contains=" + UPDATED_DUREE_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByDureeProjetNotContainsSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where dureeProjet does not contain DEFAULT_DUREE_PROJET
        defaultProjetShouldNotBeFound("dureeProjet.doesNotContain=" + DEFAULT_DUREE_PROJET);

        // Get all the projetList where dureeProjet does not contain UPDATED_DUREE_PROJET
        defaultProjetShouldBeFound("dureeProjet.doesNotContain=" + UPDATED_DUREE_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByMontantProjetIsEqualToSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where montantProjet equals to DEFAULT_MONTANT_PROJET
        defaultProjetShouldBeFound("montantProjet.equals=" + DEFAULT_MONTANT_PROJET);

        // Get all the projetList where montantProjet equals to UPDATED_MONTANT_PROJET
        defaultProjetShouldNotBeFound("montantProjet.equals=" + UPDATED_MONTANT_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByMontantProjetIsInShouldWork() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where montantProjet in DEFAULT_MONTANT_PROJET or UPDATED_MONTANT_PROJET
        defaultProjetShouldBeFound("montantProjet.in=" + DEFAULT_MONTANT_PROJET + "," + UPDATED_MONTANT_PROJET);

        // Get all the projetList where montantProjet equals to UPDATED_MONTANT_PROJET
        defaultProjetShouldNotBeFound("montantProjet.in=" + UPDATED_MONTANT_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByMontantProjetIsNullOrNotNull() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where montantProjet is not null
        defaultProjetShouldBeFound("montantProjet.specified=true");

        // Get all the projetList where montantProjet is null
        defaultProjetShouldNotBeFound("montantProjet.specified=false");
    }

    @Test
    @Transactional
    void getAllProjetsByMontantProjetIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where montantProjet is greater than or equal to DEFAULT_MONTANT_PROJET
        defaultProjetShouldBeFound("montantProjet.greaterThanOrEqual=" + DEFAULT_MONTANT_PROJET);

        // Get all the projetList where montantProjet is greater than or equal to UPDATED_MONTANT_PROJET
        defaultProjetShouldNotBeFound("montantProjet.greaterThanOrEqual=" + UPDATED_MONTANT_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByMontantProjetIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where montantProjet is less than or equal to DEFAULT_MONTANT_PROJET
        defaultProjetShouldBeFound("montantProjet.lessThanOrEqual=" + DEFAULT_MONTANT_PROJET);

        // Get all the projetList where montantProjet is less than or equal to SMALLER_MONTANT_PROJET
        defaultProjetShouldNotBeFound("montantProjet.lessThanOrEqual=" + SMALLER_MONTANT_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByMontantProjetIsLessThanSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where montantProjet is less than DEFAULT_MONTANT_PROJET
        defaultProjetShouldNotBeFound("montantProjet.lessThan=" + DEFAULT_MONTANT_PROJET);

        // Get all the projetList where montantProjet is less than UPDATED_MONTANT_PROJET
        defaultProjetShouldBeFound("montantProjet.lessThan=" + UPDATED_MONTANT_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByMontantProjetIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList where montantProjet is greater than DEFAULT_MONTANT_PROJET
        defaultProjetShouldNotBeFound("montantProjet.greaterThan=" + DEFAULT_MONTANT_PROJET);

        // Get all the projetList where montantProjet is greater than SMALLER_MONTANT_PROJET
        defaultProjetShouldBeFound("montantProjet.greaterThan=" + SMALLER_MONTANT_PROJET);
    }

    @Test
    @Transactional
    void getAllProjetsByComuneIsEqualToSomething() throws Exception {
        Commune comune;
        if (TestUtil.findAll(em, Commune.class).isEmpty()) {
            projetRepository.saveAndFlush(projet);
            comune = CommuneResourceIT.createEntity(em);
        } else {
            comune = TestUtil.findAll(em, Commune.class).get(0);
        }
        em.persist(comune);
        em.flush();
        projet.setComune(comune);
        projetRepository.saveAndFlush(projet);
        Long comuneId = comune.getId();

        // Get all the projetList where comune equals to comuneId
        defaultProjetShouldBeFound("comuneId.equals=" + comuneId);

        // Get all the projetList where comune equals to (comuneId + 1)
        defaultProjetShouldNotBeFound("comuneId.equals=" + (comuneId + 1));
    }

    @Test
    @Transactional
    void getAllProjetsByDomaineProjetIsEqualToSomething() throws Exception {
        DomaineProjet domaineProjet;
        if (TestUtil.findAll(em, DomaineProjet.class).isEmpty()) {
            projetRepository.saveAndFlush(projet);
            domaineProjet = DomaineProjetResourceIT.createEntity(em);
        } else {
            domaineProjet = TestUtil.findAll(em, DomaineProjet.class).get(0);
        }
        em.persist(domaineProjet);
        em.flush();
        projet.setDomaineProjet(domaineProjet);
        projetRepository.saveAndFlush(projet);
        Long domaineProjetId = domaineProjet.getId();

        // Get all the projetList where domaineProjet equals to domaineProjetId
        defaultProjetShouldBeFound("domaineProjetId.equals=" + domaineProjetId);

        // Get all the projetList where domaineProjet equals to (domaineProjetId + 1)
        defaultProjetShouldNotBeFound("domaineProjetId.equals=" + (domaineProjetId + 1));
    }

    @Test
    @Transactional
    void getAllProjetsByConventionIsEqualToSomething() throws Exception {
        Convention convention;
        if (TestUtil.findAll(em, Convention.class).isEmpty()) {
            projetRepository.saveAndFlush(projet);
            convention = ConventionResourceIT.createEntity(em);
        } else {
            convention = TestUtil.findAll(em, Convention.class).get(0);
        }
        em.persist(convention);
        em.flush();
        projet.addConvention(convention);
        projetRepository.saveAndFlush(projet);
        Long conventionId = convention.getId();

        // Get all the projetList where convention equals to conventionId
        defaultProjetShouldBeFound("conventionId.equals=" + conventionId);

        // Get all the projetList where convention equals to (conventionId + 1)
        defaultProjetShouldNotBeFound("conventionId.equals=" + (conventionId + 1));
    }

    @Test
    @Transactional
    void getAllProjetsBySituationProjetIsEqualToSomething() throws Exception {
        SituationProjet situationProjet;
        if (TestUtil.findAll(em, SituationProjet.class).isEmpty()) {
            projetRepository.saveAndFlush(projet);
            situationProjet = SituationProjetResourceIT.createEntity(em);
        } else {
            situationProjet = TestUtil.findAll(em, SituationProjet.class).get(0);
        }
        em.persist(situationProjet);
        em.flush();
        projet.addSituationProjet(situationProjet);
        projetRepository.saveAndFlush(projet);
        Long situationProjetId = situationProjet.getId();

        // Get all the projetList where situationProjet equals to situationProjetId
        defaultProjetShouldBeFound("situationProjetId.equals=" + situationProjetId);

        // Get all the projetList where situationProjet equals to (situationProjetId + 1)
        defaultProjetShouldNotBeFound("situationProjetId.equals=" + (situationProjetId + 1));
    }

    @Test
    @Transactional
    void getAllProjetsByTrancheIsEqualToSomething() throws Exception {
        Tranche tranche;
        if (TestUtil.findAll(em, Tranche.class).isEmpty()) {
            projetRepository.saveAndFlush(projet);
            tranche = TrancheResourceIT.createEntity(em);
        } else {
            tranche = TestUtil.findAll(em, Tranche.class).get(0);
        }
        em.persist(tranche);
        em.flush();
        projet.addTranche(tranche);
        projetRepository.saveAndFlush(projet);
        Long trancheId = tranche.getId();

        // Get all the projetList where tranche equals to trancheId
        defaultProjetShouldBeFound("trancheId.equals=" + trancheId);

        // Get all the projetList where tranche equals to (trancheId + 1)
        defaultProjetShouldNotBeFound("trancheId.equals=" + (trancheId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProjetShouldBeFound(String filter) throws Exception {
        restProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projet.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomProjet").value(hasItem(DEFAULT_NOM_PROJET)))
            .andExpect(jsonPath("$.[*].anneeProjet").value(hasItem(DEFAULT_ANNEE_PROJET)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dureeProjet").value(hasItem(DEFAULT_DUREE_PROJET)))
            .andExpect(jsonPath("$.[*].montantProjet").value(hasItem(sameNumber(DEFAULT_MONTANT_PROJET))));

        // Check, that the count call also returns 1
        restProjetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProjetShouldNotBeFound(String filter) throws Exception {
        restProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProjetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProjet() throws Exception {
        // Get the projet
        restProjetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        int databaseSizeBeforeUpdate = projetRepository.findAll().size();

        // Update the projet
        Projet updatedProjet = projetRepository.findById(projet.getId()).get();
        // Disconnect from session so that the updates on updatedProjet are not directly saved in db
        em.detach(updatedProjet);
        updatedProjet
            .nomProjet(UPDATED_NOM_PROJET)
            .anneeProjet(UPDATED_ANNEE_PROJET)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dureeProjet(UPDATED_DUREE_PROJET)
            .montantProjet(UPDATED_MONTANT_PROJET);

        restProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProjet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProjet))
            )
            .andExpect(status().isOk());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
        Projet testProjet = projetList.get(projetList.size() - 1);
        assertThat(testProjet.getNomProjet()).isEqualTo(UPDATED_NOM_PROJET);
        assertThat(testProjet.getAnneeProjet()).isEqualTo(UPDATED_ANNEE_PROJET);
        assertThat(testProjet.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProjet.getDureeProjet()).isEqualTo(UPDATED_DUREE_PROJET);
        assertThat(testProjet.getMontantProjet()).isEqualByComparingTo(UPDATED_MONTANT_PROJET);
    }

    @Test
    @Transactional
    void putNonExistingProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();
        projet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();
        projet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();
        projet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjetWithPatch() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        int databaseSizeBeforeUpdate = projetRepository.findAll().size();

        // Update the projet using partial update
        Projet partialUpdatedProjet = new Projet();
        partialUpdatedProjet.setId(projet.getId());

        partialUpdatedProjet.dateDebut(UPDATED_DATE_DEBUT);

        restProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjet))
            )
            .andExpect(status().isOk());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
        Projet testProjet = projetList.get(projetList.size() - 1);
        assertThat(testProjet.getNomProjet()).isEqualTo(DEFAULT_NOM_PROJET);
        assertThat(testProjet.getAnneeProjet()).isEqualTo(DEFAULT_ANNEE_PROJET);
        assertThat(testProjet.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProjet.getDureeProjet()).isEqualTo(DEFAULT_DUREE_PROJET);
        assertThat(testProjet.getMontantProjet()).isEqualByComparingTo(DEFAULT_MONTANT_PROJET);
    }

    @Test
    @Transactional
    void fullUpdateProjetWithPatch() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        int databaseSizeBeforeUpdate = projetRepository.findAll().size();

        // Update the projet using partial update
        Projet partialUpdatedProjet = new Projet();
        partialUpdatedProjet.setId(projet.getId());

        partialUpdatedProjet
            .nomProjet(UPDATED_NOM_PROJET)
            .anneeProjet(UPDATED_ANNEE_PROJET)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dureeProjet(UPDATED_DUREE_PROJET)
            .montantProjet(UPDATED_MONTANT_PROJET);

        restProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjet))
            )
            .andExpect(status().isOk());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
        Projet testProjet = projetList.get(projetList.size() - 1);
        assertThat(testProjet.getNomProjet()).isEqualTo(UPDATED_NOM_PROJET);
        assertThat(testProjet.getAnneeProjet()).isEqualTo(UPDATED_ANNEE_PROJET);
        assertThat(testProjet.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProjet.getDureeProjet()).isEqualTo(UPDATED_DUREE_PROJET);
        assertThat(testProjet.getMontantProjet()).isEqualByComparingTo(UPDATED_MONTANT_PROJET);
    }

    @Test
    @Transactional
    void patchNonExistingProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();
        projet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();
        projet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();
        projet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        int databaseSizeBeforeDelete = projetRepository.findAll().size();

        // Delete the projet
        restProjetMockMvc
            .perform(delete(ENTITY_API_URL_ID, projet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
