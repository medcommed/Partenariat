package ma.gov.social.partenariat.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.gov.social.partenariat.domain.Commune;
import ma.gov.social.partenariat.repository.CommuneRepository;
import ma.gov.social.partenariat.service.CommuneQueryService;
import ma.gov.social.partenariat.service.CommuneService;
import ma.gov.social.partenariat.service.criteria.CommuneCriteria;
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
 * REST controller for managing {@link ma.gov.social.partenariat.domain.Commune}.
 */
@RestController
@RequestMapping("/api")
public class CommuneResource {

    private final Logger log = LoggerFactory.getLogger(CommuneResource.class);

    private static final String ENTITY_NAME = "commune";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommuneService communeService;

    private final CommuneRepository communeRepository;

    private final CommuneQueryService communeQueryService;

    public CommuneResource(CommuneService communeService, CommuneRepository communeRepository, CommuneQueryService communeQueryService) {
        this.communeService = communeService;
        this.communeRepository = communeRepository;
        this.communeQueryService = communeQueryService;
    }

    /**
     * {@code POST  /communes} : Create a new commune.
     *
     * @param commune the commune to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commune, or with status {@code 400 (Bad Request)} if the commune has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/communes")
    public ResponseEntity<Commune> createCommune(@Valid @RequestBody Commune commune) throws URISyntaxException {
        log.debug("REST request to save Commune : {}", commune);
        if (commune.getId() != null) {
            throw new BadRequestAlertException("A new commune cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Commune result = communeService.save(commune);
        return ResponseEntity
            .created(new URI("/api/communes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /communes/:id} : Updates an existing commune.
     *
     * @param id the id of the commune to save.
     * @param commune the commune to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commune,
     * or with status {@code 400 (Bad Request)} if the commune is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commune couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/communes/{id}")
    public ResponseEntity<Commune> updateCommune(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Commune commune
    ) throws URISyntaxException {
        log.debug("REST request to update Commune : {}, {}", id, commune);
        if (commune.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commune.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Commune result = communeService.update(commune);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commune.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /communes/:id} : Partial updates given fields of an existing commune, field will ignore if it is null
     *
     * @param id the id of the commune to save.
     * @param commune the commune to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commune,
     * or with status {@code 400 (Bad Request)} if the commune is not valid,
     * or with status {@code 404 (Not Found)} if the commune is not found,
     * or with status {@code 500 (Internal Server Error)} if the commune couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/communes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Commune> partialUpdateCommune(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Commune commune
    ) throws URISyntaxException {
        log.debug("REST request to partial update Commune partially : {}, {}", id, commune);
        if (commune.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commune.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Commune> result = communeService.partialUpdate(commune);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commune.getId().toString())
        );
    }

    /**
     * {@code GET  /communes} : get all the communes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communes in body.
     */
    @GetMapping("/communes")
    public ResponseEntity<List<Commune>> getAllCommunes(
        CommuneCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Communes by criteria: {}", criteria);
        Page<Commune> page = communeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /communes/count} : count all the communes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/communes/count")
    public ResponseEntity<Long> countCommunes(CommuneCriteria criteria) {
        log.debug("REST request to count Communes by criteria: {}", criteria);
        return ResponseEntity.ok().body(communeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /communes/:id} : get the "id" commune.
     *
     * @param id the id of the commune to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commune, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/communes/{id}")
    public ResponseEntity<Commune> getCommune(@PathVariable Long id) {
        log.debug("REST request to get Commune : {}", id);
        Optional<Commune> commune = communeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commune);
    }

    /**
     * {@code DELETE  /communes/:id} : delete the "id" commune.
     *
     * @param id the id of the commune to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/communes/{id}")
    public ResponseEntity<Void> deleteCommune(@PathVariable Long id) {
        log.debug("REST request to delete Commune : {}", id);
        communeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
