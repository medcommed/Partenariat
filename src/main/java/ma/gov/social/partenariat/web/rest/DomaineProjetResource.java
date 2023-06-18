package ma.gov.social.partenariat.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.gov.social.partenariat.domain.DomaineProjet;
import ma.gov.social.partenariat.repository.DomaineProjetRepository;
import ma.gov.social.partenariat.service.DomaineProjetQueryService;
import ma.gov.social.partenariat.service.DomaineProjetService;
import ma.gov.social.partenariat.service.criteria.DomaineProjetCriteria;
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
 * REST controller for managing {@link ma.gov.social.partenariat.domain.DomaineProjet}.
 */
@RestController
@RequestMapping("/api")
public class DomaineProjetResource {

    private final Logger log = LoggerFactory.getLogger(DomaineProjetResource.class);

    private static final String ENTITY_NAME = "domaineProjet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DomaineProjetService domaineProjetService;

    private final DomaineProjetRepository domaineProjetRepository;

    private final DomaineProjetQueryService domaineProjetQueryService;

    public DomaineProjetResource(
        DomaineProjetService domaineProjetService,
        DomaineProjetRepository domaineProjetRepository,
        DomaineProjetQueryService domaineProjetQueryService
    ) {
        this.domaineProjetService = domaineProjetService;
        this.domaineProjetRepository = domaineProjetRepository;
        this.domaineProjetQueryService = domaineProjetQueryService;
    }

    /**
     * {@code POST  /domaine-projets} : Create a new domaineProjet.
     *
     * @param domaineProjet the domaineProjet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new domaineProjet, or with status {@code 400 (Bad Request)} if the domaineProjet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/domaine-projets")
    public ResponseEntity<DomaineProjet> createDomaineProjet(@Valid @RequestBody DomaineProjet domaineProjet) throws URISyntaxException {
        log.debug("REST request to save DomaineProjet : {}", domaineProjet);
        if (domaineProjet.getId() != null) {
            throw new BadRequestAlertException("A new domaineProjet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DomaineProjet result = domaineProjetService.save(domaineProjet);
        return ResponseEntity
            .created(new URI("/api/domaine-projets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /domaine-projets/:id} : Updates an existing domaineProjet.
     *
     * @param id the id of the domaineProjet to save.
     * @param domaineProjet the domaineProjet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domaineProjet,
     * or with status {@code 400 (Bad Request)} if the domaineProjet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the domaineProjet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/domaine-projets/{id}")
    public ResponseEntity<DomaineProjet> updateDomaineProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DomaineProjet domaineProjet
    ) throws URISyntaxException {
        log.debug("REST request to update DomaineProjet : {}, {}", id, domaineProjet);
        if (domaineProjet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domaineProjet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domaineProjetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DomaineProjet result = domaineProjetService.update(domaineProjet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, domaineProjet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /domaine-projets/:id} : Partial updates given fields of an existing domaineProjet, field will ignore if it is null
     *
     * @param id the id of the domaineProjet to save.
     * @param domaineProjet the domaineProjet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domaineProjet,
     * or with status {@code 400 (Bad Request)} if the domaineProjet is not valid,
     * or with status {@code 404 (Not Found)} if the domaineProjet is not found,
     * or with status {@code 500 (Internal Server Error)} if the domaineProjet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/domaine-projets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DomaineProjet> partialUpdateDomaineProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DomaineProjet domaineProjet
    ) throws URISyntaxException {
        log.debug("REST request to partial update DomaineProjet partially : {}, {}", id, domaineProjet);
        if (domaineProjet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domaineProjet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domaineProjetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DomaineProjet> result = domaineProjetService.partialUpdate(domaineProjet);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, domaineProjet.getId().toString())
        );
    }

    /**
     * {@code GET  /domaine-projets} : get all the domaineProjets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of domaineProjets in body.
     */
    @GetMapping("/domaine-projets")
    public ResponseEntity<List<DomaineProjet>> getAllDomaineProjets(
        DomaineProjetCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DomaineProjets by criteria: {}", criteria);
        Page<DomaineProjet> page = domaineProjetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /domaine-projets/count} : count all the domaineProjets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/domaine-projets/count")
    public ResponseEntity<Long> countDomaineProjets(DomaineProjetCriteria criteria) {
        log.debug("REST request to count DomaineProjets by criteria: {}", criteria);
        return ResponseEntity.ok().body(domaineProjetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /domaine-projets/:id} : get the "id" domaineProjet.
     *
     * @param id the id of the domaineProjet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the domaineProjet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/domaine-projets/{id}")
    public ResponseEntity<DomaineProjet> getDomaineProjet(@PathVariable Long id) {
        log.debug("REST request to get DomaineProjet : {}", id);
        Optional<DomaineProjet> domaineProjet = domaineProjetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(domaineProjet);
    }

    /**
     * {@code DELETE  /domaine-projets/:id} : delete the "id" domaineProjet.
     *
     * @param id the id of the domaineProjet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/domaine-projets/{id}")
    public ResponseEntity<Void> deleteDomaineProjet(@PathVariable Long id) {
        log.debug("REST request to delete DomaineProjet : {}", id);
        domaineProjetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
