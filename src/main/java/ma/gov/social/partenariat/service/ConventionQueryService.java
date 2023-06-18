package ma.gov.social.partenariat.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.gov.social.partenariat.domain.*; // for static metamodels
import ma.gov.social.partenariat.domain.Convention;
import ma.gov.social.partenariat.repository.ConventionRepository;
import ma.gov.social.partenariat.service.criteria.ConventionCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Convention} entities in the database.
 * The main input is a {@link ConventionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Convention} or a {@link Page} of {@link Convention} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConventionQueryService extends QueryService<Convention> {

    private final Logger log = LoggerFactory.getLogger(ConventionQueryService.class);

    private final ConventionRepository conventionRepository;

    public ConventionQueryService(ConventionRepository conventionRepository) {
        this.conventionRepository = conventionRepository;
    }

    /**
     * Return a {@link List} of {@link Convention} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Convention> findByCriteria(ConventionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Convention> specification = createSpecification(criteria);
        return conventionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Convention} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Convention> findByCriteria(ConventionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Convention> specification = createSpecification(criteria);
        return conventionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConventionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Convention> specification = createSpecification(criteria);
        return conventionRepository.count(specification);
    }

    /**
     * Function to convert {@link ConventionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Convention> createSpecification(ConventionCriteria criteria) {
        Specification<Convention> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Convention_.id));
            }
            if (criteria.getAveanau() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAveanau(), Convention_.aveanau));
            }
            if (criteria.getDateDebutConv() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebutConv(), Convention_.dateDebutConv));
            }
            if (criteria.getDureeConvention() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDureeConvention(), Convention_.dureeConvention));
            }
            if (criteria.getEtatConvention() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEtatConvention(), Convention_.etatConvention));
            }
            if (criteria.getNbrTranche() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbrTranche(), Convention_.nbrTranche));
            }
            if (criteria.getNomConvention() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomConvention(), Convention_.nomConvention));
            }
            if (criteria.getProjetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjetId(), root -> root.join(Convention_.projet, JoinType.LEFT).get(Projet_.id))
                    );
            }
            if (criteria.getTypeConventionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTypeConventionId(),
                            root -> root.join(Convention_.typeConvention, JoinType.LEFT).get(TypeConvention_.id)
                        )
                    );
            }
            if (criteria.getPartenaireId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPartenaireId(),
                            root -> root.join(Convention_.partenaires, JoinType.LEFT).get(Partenaire_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
