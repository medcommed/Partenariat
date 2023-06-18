package ma.gov.social.partenariat.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.gov.social.partenariat.domain.*; // for static metamodels
import ma.gov.social.partenariat.domain.SituationProjet;
import ma.gov.social.partenariat.repository.SituationProjetRepository;
import ma.gov.social.partenariat.service.criteria.SituationProjetCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SituationProjet} entities in the database.
 * The main input is a {@link SituationProjetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SituationProjet} or a {@link Page} of {@link SituationProjet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SituationProjetQueryService extends QueryService<SituationProjet> {

    private final Logger log = LoggerFactory.getLogger(SituationProjetQueryService.class);

    private final SituationProjetRepository situationProjetRepository;

    public SituationProjetQueryService(SituationProjetRepository situationProjetRepository) {
        this.situationProjetRepository = situationProjetRepository;
    }

    /**
     * Return a {@link List} of {@link SituationProjet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SituationProjet> findByCriteria(SituationProjetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SituationProjet> specification = createSpecification(criteria);
        return situationProjetRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SituationProjet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SituationProjet> findByCriteria(SituationProjetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SituationProjet> specification = createSpecification(criteria);
        return situationProjetRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SituationProjetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SituationProjet> specification = createSpecification(criteria);
        return situationProjetRepository.count(specification);
    }

    /**
     * Function to convert {@link SituationProjetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SituationProjet> createSpecification(SituationProjetCriteria criteria) {
        Specification<SituationProjet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SituationProjet_.id));
            }
            if (criteria.getDateStatutValid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateStatutValid(), SituationProjet_.dateStatutValid));
            }
            if (criteria.getStatutProjet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatutProjet(), SituationProjet_.statutProjet));
            }
            if (criteria.getProjetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProjetId(),
                            root -> root.join(SituationProjet_.projet, JoinType.LEFT).get(Projet_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
