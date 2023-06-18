package ma.gov.social.partenariat.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.gov.social.partenariat.domain.SituationProjet;
import ma.gov.social.partenariat.repository.SituationProjetRepository;
import ma.gov.social.partenariat.service.SituationProjetQueryService;
import ma.gov.social.partenariat.service.SituationProjetService;
import ma.gov.social.partenariat.service.criteria.SituationProjetCriteria;
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
 * REST controller for managing {@link ma.gov.social.partenariat.domain.SituationProjet}.
 */
@RestController
@RequestMapping("/api")
public class SituationProjetResource {

    private final Logger log = LoggerFactory.getLogger(SituationProjetResource.class);

    private static final String ENTITY_NAME = "situationProjet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SituationProjetService situationProjetService;

    private final SituationProjetRepository situationProjetRepository;

    private final SituationProjetQueryService situationProjetQueryService;

    public SituationProjetResource(
        SituationProjetService situationProjetService,
        SituationProjetRepository situationProjetRepository,
        SituationProjetQueryService situationProjetQueryService
    ) {
        this.situationProjetService = situationProjetService;
        this.situationProjetRepository = situationProjetRepository;
        this.situationProjetQueryService = situationProjetQueryService;
    }

    /**
     * {@code POST  /situation-projets} : Create a new situationProjet.
     *
     * @param situationProjet the situationProjet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new situationProjet, or with status {@code 400 (Bad Request)} if the situationProjet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/situation-projets")
    public ResponseEntity<SituationProjet> createSituationProjet(@Valid @RequestBody SituationProjet situationProjet)
        throws URISyntaxException {
        log.debug("REST request to save SituationProjet : {}", situationProjet);
        if (situationProjet.getId() != null) {
            throw new BadRequestAlertException("A new situationProjet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SituationProjet result = situationProjetService.save(situationProjet);
        return ResponseEntity
            .created(new URI("/api/situation-projets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /situation-projets/:id} : Updates an existing situationProjet.
     *
     * @param id the id of the situationProjet to save.
     * @param situationProjet the situationProjet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated situationProjet,
     * or with status {@code 400 (Bad Request)} if the situationProjet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the situationProjet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/situation-projets/{id}")
    public ResponseEntity<SituationProjet> updateSituationProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SituationProjet situationProjet
    ) throws URISyntaxException {
        log.debug("REST request to update SituationProjet : {}, {}", id, situationProjet);
        if (situationProjet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, situationProjet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!situationProjetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SituationProjet result = situationProjetService.update(situationProjet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, situationProjet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /situation-projets/:id} : Partial updates given fields of an existing situationProjet, field will ignore if it is null
     *
     * @param id the id of the situationProjet to save.
     * @param situationProjet the situationProjet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated situationProjet,
     * or with status {@code 400 (Bad Request)} if the situationProjet is not valid,
     * or with status {@code 404 (Not Found)} if the situationProjet is not found,
     * or with status {@code 500 (Internal Server Error)} if the situationProjet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/situation-projets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SituationProjet> partialUpdateSituationProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SituationProjet situationProjet
    ) throws URISyntaxException {
        log.debug("REST request to partial update SituationProjet partially : {}, {}", id, situationProjet);
        if (situationProjet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, situationProjet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!situationProjetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SituationProjet> result = situationProjetService.partialUpdate(situationProjet);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, situationProjet.getId().toString())
        );
    }

    /**
     * {@code GET  /situation-projets} : get all the situationProjets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of situationProjets in body.
     */
    @GetMapping("/situation-projets")
    public ResponseEntity<List<SituationProjet>> getAllSituationProjets(
        SituationProjetCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SituationProjets by criteria: {}", criteria);
        Page<SituationProjet> page = situationProjetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /situation-projets/count} : count all the situationProjets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/situation-projets/count")
    public ResponseEntity<Long> countSituationProjets(SituationProjetCriteria criteria) {
        log.debug("REST request to count SituationProjets by criteria: {}", criteria);
        return ResponseEntity.ok().body(situationProjetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /situation-projets/:id} : get the "id" situationProjet.
     *
     * @param id the id of the situationProjet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the situationProjet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/situation-projets/{id}")
    public ResponseEntity<SituationProjet> getSituationProjet(@PathVariable Long id) {
        log.debug("REST request to get SituationProjet : {}", id);
        Optional<SituationProjet> situationProjet = situationProjetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(situationProjet);
    }

    /**
     * {@code DELETE  /situation-projets/:id} : delete the "id" situationProjet.
     *
     * @param id the id of the situationProjet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/situation-projets/{id}")
    public ResponseEntity<Void> deleteSituationProjet(@PathVariable Long id) {
        log.debug("REST request to delete SituationProjet : {}", id);
        situationProjetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
