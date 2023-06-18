package ma.gov.social.partenariat.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.gov.social.partenariat.domain.Partenaire;
import ma.gov.social.partenariat.repository.PartenaireRepository;
import ma.gov.social.partenariat.service.PartenaireQueryService;
import ma.gov.social.partenariat.service.PartenaireService;
import ma.gov.social.partenariat.service.criteria.PartenaireCriteria;
import ma.gov.social.partenariat.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ma.gov.social.partenariat.domain.Partenaire}.
 */
@RestController
@RequestMapping("/api")
public class PartenaireResource {

    private final Logger log = LoggerFactory.getLogger(PartenaireResource.class);

    private static final String ENTITY_NAME = "partenaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartenaireService partenaireService;

    private final PartenaireRepository partenaireRepository;

    private final PartenaireQueryService partenaireQueryService;

    public PartenaireResource(
        PartenaireService partenaireService,
        PartenaireRepository partenaireRepository,
        PartenaireQueryService partenaireQueryService
    ) {
        this.partenaireService = partenaireService;
        this.partenaireRepository = partenaireRepository;
        this.partenaireQueryService = partenaireQueryService;
    }

    /**
     * {@code POST  /partenaires} : Create a new partenaire.
     *
     * @param partenaire the partenaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partenaire, or with status {@code 400 (Bad Request)} if the partenaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/partenaires")
    public ResponseEntity<Partenaire> createPartenaire(@Valid @RequestBody Partenaire partenaire) throws URISyntaxException {
        log.debug("REST request to save Partenaire : {}", partenaire);
        if (partenaire.getId() != null) {
            throw new BadRequestAlertException("A new partenaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Partenaire result = partenaireService.save(partenaire);
        return ResponseEntity
            .created(new URI("/api/partenaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /partenaires/:id} : Updates an existing partenaire.
     *
     * @param id the id of the partenaire to save.
     * @param partenaire the partenaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partenaire,
     * or with status {@code 400 (Bad Request)} if the partenaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partenaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/partenaires/{id}")
    public ResponseEntity<Partenaire> updatePartenaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Partenaire partenaire
    ) throws URISyntaxException {
        log.debug("REST request to update Partenaire : {}, {}", id, partenaire);
        if (partenaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partenaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partenaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Partenaire result = partenaireService.update(partenaire);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partenaire.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /partenaires/:id} : Partial updates given fields of an existing partenaire, field will ignore if it is null
     *
     * @param id the id of the partenaire to save.
     * @param partenaire the partenaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partenaire,
     * or with status {@code 400 (Bad Request)} if the partenaire is not valid,
     * or with status {@code 404 (Not Found)} if the partenaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the partenaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/partenaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Partenaire> partialUpdatePartenaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Partenaire partenaire
    ) throws URISyntaxException {
        log.debug("REST request to partial update Partenaire partially : {}, {}", id, partenaire);
        if (partenaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partenaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partenaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Partenaire> result = partenaireService.partialUpdate(partenaire);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partenaire.getId().toString())
        );
    }

    /**
     * {@code GET  /partenaires} : get all the partenaires.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of partenaires in body.
     */
    @GetMapping("/partenaires")
    public ResponseEntity<List<Partenaire>> getAllPartenaires(
        PartenaireCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Partenaires by criteria: {}", criteria);
        Page<Partenaire> page = partenaireQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /partenaires/count} : count all the partenaires.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/partenaires/count")
    public ResponseEntity<Long> countPartenaires(PartenaireCriteria criteria) {
        log.debug("REST request to count Partenaires by criteria: {}", criteria);
        return ResponseEntity.ok().body(partenaireQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /partenaires/:id} : get the "id" partenaire.
     *
     * @param id the id of the partenaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partenaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/partenaires/{id}")
    public ResponseEntity<Partenaire> getPartenaire(@PathVariable Long id) {
        log.debug("REST request to get Partenaire : {}", id);
        Optional<Partenaire> partenaire = partenaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partenaire);
    }

    /**
     * {@code DELETE  /partenaires/:id} : delete the "id" partenaire.
     *
     * @param id the id of the partenaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/partenaires/{id}")
    public ResponseEntity<Void> deletePartenaire(@PathVariable Long id) {
        log.debug("REST request to delete Partenaire : {}", id);
        partenaireService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
