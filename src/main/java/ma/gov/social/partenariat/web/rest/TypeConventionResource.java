package ma.gov.social.partenariat.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.gov.social.partenariat.domain.TypeConvention;
import ma.gov.social.partenariat.repository.TypeConventionRepository;
import ma.gov.social.partenariat.service.TypeConventionQueryService;
import ma.gov.social.partenariat.service.TypeConventionService;
import ma.gov.social.partenariat.service.criteria.TypeConventionCriteria;
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
 * REST controller for managing {@link ma.gov.social.partenariat.domain.TypeConvention}.
 */
@RestController
@RequestMapping("/api")
public class TypeConventionResource {

    private final Logger log = LoggerFactory.getLogger(TypeConventionResource.class);

    private static final String ENTITY_NAME = "typeConvention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeConventionService typeConventionService;

    private final TypeConventionRepository typeConventionRepository;

    private final TypeConventionQueryService typeConventionQueryService;

    public TypeConventionResource(
        TypeConventionService typeConventionService,
        TypeConventionRepository typeConventionRepository,
        TypeConventionQueryService typeConventionQueryService
    ) {
        this.typeConventionService = typeConventionService;
        this.typeConventionRepository = typeConventionRepository;
        this.typeConventionQueryService = typeConventionQueryService;
    }

    /**
     * {@code POST  /type-conventions} : Create a new typeConvention.
     *
     * @param typeConvention the typeConvention to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeConvention, or with status {@code 400 (Bad Request)} if the typeConvention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-conventions")
    public ResponseEntity<TypeConvention> createTypeConvention(@Valid @RequestBody TypeConvention typeConvention)
        throws URISyntaxException {
        log.debug("REST request to save TypeConvention : {}", typeConvention);
        if (typeConvention.getId() != null) {
            throw new BadRequestAlertException("A new typeConvention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeConvention result = typeConventionService.save(typeConvention);
        return ResponseEntity
            .created(new URI("/api/type-conventions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-conventions/:id} : Updates an existing typeConvention.
     *
     * @param id the id of the typeConvention to save.
     * @param typeConvention the typeConvention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeConvention,
     * or with status {@code 400 (Bad Request)} if the typeConvention is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeConvention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-conventions/{id}")
    public ResponseEntity<TypeConvention> updateTypeConvention(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeConvention typeConvention
    ) throws URISyntaxException {
        log.debug("REST request to update TypeConvention : {}, {}", id, typeConvention);
        if (typeConvention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeConvention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeConventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeConvention result = typeConventionService.update(typeConvention);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeConvention.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-conventions/:id} : Partial updates given fields of an existing typeConvention, field will ignore if it is null
     *
     * @param id the id of the typeConvention to save.
     * @param typeConvention the typeConvention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeConvention,
     * or with status {@code 400 (Bad Request)} if the typeConvention is not valid,
     * or with status {@code 404 (Not Found)} if the typeConvention is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeConvention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-conventions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeConvention> partialUpdateTypeConvention(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeConvention typeConvention
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeConvention partially : {}, {}", id, typeConvention);
        if (typeConvention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeConvention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeConventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeConvention> result = typeConventionService.partialUpdate(typeConvention);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeConvention.getId().toString())
        );
    }

    /**
     * {@code GET  /type-conventions} : get all the typeConventions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeConventions in body.
     */
    @GetMapping("/type-conventions")
    public ResponseEntity<List<TypeConvention>> getAllTypeConventions(
        TypeConventionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TypeConventions by criteria: {}", criteria);
        Page<TypeConvention> page = typeConventionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-conventions/count} : count all the typeConventions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/type-conventions/count")
    public ResponseEntity<Long> countTypeConventions(TypeConventionCriteria criteria) {
        log.debug("REST request to count TypeConventions by criteria: {}", criteria);
        return ResponseEntity.ok().body(typeConventionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /type-conventions/:id} : get the "id" typeConvention.
     *
     * @param id the id of the typeConvention to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeConvention, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-conventions/{id}")
    public ResponseEntity<TypeConvention> getTypeConvention(@PathVariable Long id) {
        log.debug("REST request to get TypeConvention : {}", id);
        Optional<TypeConvention> typeConvention = typeConventionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeConvention);
    }

    /**
     * {@code DELETE  /type-conventions/:id} : delete the "id" typeConvention.
     *
     * @param id the id of the typeConvention to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-conventions/{id}")
    public ResponseEntity<Void> deleteTypeConvention(@PathVariable Long id) {
        log.debug("REST request to delete TypeConvention : {}", id);
        typeConventionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
