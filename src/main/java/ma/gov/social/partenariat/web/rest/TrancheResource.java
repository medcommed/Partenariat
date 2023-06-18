package ma.gov.social.partenariat.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.gov.social.partenariat.domain.Tranche;
import ma.gov.social.partenariat.repository.TrancheRepository;
import ma.gov.social.partenariat.service.TrancheQueryService;
import ma.gov.social.partenariat.service.TrancheService;
import ma.gov.social.partenariat.service.criteria.TrancheCriteria;
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
 * REST controller for managing {@link ma.gov.social.partenariat.domain.Tranche}.
 */
@RestController
@RequestMapping("/api")
public class TrancheResource {

    private final Logger log = LoggerFactory.getLogger(TrancheResource.class);

    private static final String ENTITY_NAME = "tranche";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrancheService trancheService;

    private final TrancheRepository trancheRepository;

    private final TrancheQueryService trancheQueryService;

    public TrancheResource(TrancheService trancheService, TrancheRepository trancheRepository, TrancheQueryService trancheQueryService) {
        this.trancheService = trancheService;
        this.trancheRepository = trancheRepository;
        this.trancheQueryService = trancheQueryService;
    }

    /**
     * {@code POST  /tranches} : Create a new tranche.
     *
     * @param tranche the tranche to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tranche, or with status {@code 400 (Bad Request)} if the tranche has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tranches")
    public ResponseEntity<Tranche> createTranche(@Valid @RequestBody Tranche tranche) throws URISyntaxException {
        log.debug("REST request to save Tranche : {}", tranche);
        if (tranche.getId() != null) {
            throw new BadRequestAlertException("A new tranche cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tranche result = trancheService.save(tranche);
        return ResponseEntity
            .created(new URI("/api/tranches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tranches/:id} : Updates an existing tranche.
     *
     * @param id the id of the tranche to save.
     * @param tranche the tranche to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tranche,
     * or with status {@code 400 (Bad Request)} if the tranche is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tranche couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tranches/{id}")
    public ResponseEntity<Tranche> updateTranche(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Tranche tranche
    ) throws URISyntaxException {
        log.debug("REST request to update Tranche : {}, {}", id, tranche);
        if (tranche.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tranche.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trancheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tranche result = trancheService.update(tranche);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tranche.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tranches/:id} : Partial updates given fields of an existing tranche, field will ignore if it is null
     *
     * @param id the id of the tranche to save.
     * @param tranche the tranche to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tranche,
     * or with status {@code 400 (Bad Request)} if the tranche is not valid,
     * or with status {@code 404 (Not Found)} if the tranche is not found,
     * or with status {@code 500 (Internal Server Error)} if the tranche couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tranches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tranche> partialUpdateTranche(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Tranche tranche
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tranche partially : {}, {}", id, tranche);
        if (tranche.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tranche.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trancheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tranche> result = trancheService.partialUpdate(tranche);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tranche.getId().toString())
        );
    }

    /**
     * {@code GET  /tranches} : get all the tranches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tranches in body.
     */
    @GetMapping("/tranches")
    public ResponseEntity<List<Tranche>> getAllTranches(
        TrancheCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Tranches by criteria: {}", criteria);
        Page<Tranche> page = trancheQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tranches/count} : count all the tranches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tranches/count")
    public ResponseEntity<Long> countTranches(TrancheCriteria criteria) {
        log.debug("REST request to count Tranches by criteria: {}", criteria);
        return ResponseEntity.ok().body(trancheQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tranches/:id} : get the "id" tranche.
     *
     * @param id the id of the tranche to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tranche, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tranches/{id}")
    public ResponseEntity<Tranche> getTranche(@PathVariable Long id) {
        log.debug("REST request to get Tranche : {}", id);
        Optional<Tranche> tranche = trancheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tranche);
    }

    /**
     * {@code DELETE  /tranches/:id} : delete the "id" tranche.
     *
     * @param id the id of the tranche to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tranches/{id}")
    public ResponseEntity<Void> deleteTranche(@PathVariable Long id) {
        log.debug("REST request to delete Tranche : {}", id);
        trancheService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
