package ma.gov.social.partenariat.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.gov.social.partenariat.domain.Convention;
import ma.gov.social.partenariat.repository.ConventionRepository;
import ma.gov.social.partenariat.service.ConventionQueryService;
import ma.gov.social.partenariat.service.ConventionService;
import ma.gov.social.partenariat.service.criteria.ConventionCriteria;
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
 * REST controller for managing {@link ma.gov.social.partenariat.domain.Convention}.
 */
@RestController
@RequestMapping("/api")
public class ConventionResource {

    private final Logger log = LoggerFactory.getLogger(ConventionResource.class);

    private static final String ENTITY_NAME = "convention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConventionService conventionService;

    private final ConventionRepository conventionRepository;

    private final ConventionQueryService conventionQueryService;

    public ConventionResource(
        ConventionService conventionService,
        ConventionRepository conventionRepository,
        ConventionQueryService conventionQueryService
    ) {
        this.conventionService = conventionService;
        this.conventionRepository = conventionRepository;
        this.conventionQueryService = conventionQueryService;
    }

    /**
     * {@code POST  /conventions} : Create a new convention.
     *
     * @param convention the convention to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new convention, or with status {@code 400 (Bad Request)} if the convention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conventions")
    public ResponseEntity<Convention> createConvention(@Valid @RequestBody Convention convention) throws URISyntaxException {
        log.debug("REST request to save Convention : {}", convention);
        if (convention.getId() != null) {
            throw new BadRequestAlertException("A new convention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Convention result = conventionService.save(convention);
        return ResponseEntity
            .created(new URI("/api/conventions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conventions/:id} : Updates an existing convention.
     *
     * @param id the id of the convention to save.
     * @param convention the convention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated convention,
     * or with status {@code 400 (Bad Request)} if the convention is not valid,
     * or with status {@code 500 (Internal Server Error)} if the convention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conventions/{id}")
    public ResponseEntity<Convention> updateConvention(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Convention convention
    ) throws URISyntaxException {
        log.debug("REST request to update Convention : {}, {}", id, convention);
        if (convention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, convention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Convention result = conventionService.update(convention);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, convention.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /conventions/:id} : Partial updates given fields of an existing convention, field will ignore if it is null
     *
     * @param id the id of the convention to save.
     * @param convention the convention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated convention,
     * or with status {@code 400 (Bad Request)} if the convention is not valid,
     * or with status {@code 404 (Not Found)} if the convention is not found,
     * or with status {@code 500 (Internal Server Error)} if the convention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/conventions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Convention> partialUpdateConvention(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Convention convention
    ) throws URISyntaxException {
        log.debug("REST request to partial update Convention partially : {}, {}", id, convention);
        if (convention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, convention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Convention> result = conventionService.partialUpdate(convention);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, convention.getId().toString())
        );
    }

    /**
     * {@code GET  /conventions} : get all the conventions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conventions in body.
     */
    @GetMapping("/conventions")
    public ResponseEntity<List<Convention>> getAllConventions(
        ConventionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Conventions by criteria: {}", criteria);
        Page<Convention> page = conventionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conventions/count} : count all the conventions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/conventions/count")
    public ResponseEntity<Long> countConventions(ConventionCriteria criteria) {
        log.debug("REST request to count Conventions by criteria: {}", criteria);
        return ResponseEntity.ok().body(conventionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /conventions/:id} : get the "id" convention.
     *
     * @param id the id of the convention to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the convention, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conventions/{id}")
    public ResponseEntity<Convention> getConvention(@PathVariable Long id) {
        log.debug("REST request to get Convention : {}", id);
        Optional<Convention> convention = conventionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(convention);
    }

    /**
     * {@code DELETE  /conventions/:id} : delete the "id" convention.
     *
     * @param id the id of the convention to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conventions/{id}")
    public ResponseEntity<Void> deleteConvention(@PathVariable Long id) {
        log.debug("REST request to delete Convention : {}", id);
        conventionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
