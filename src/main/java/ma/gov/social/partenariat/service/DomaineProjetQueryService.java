package ma.gov.social.partenariat.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.gov.social.partenariat.domain.*; // for static metamodels
import ma.gov.social.partenariat.domain.DomaineProjet;
import ma.gov.social.partenariat.repository.DomaineProjetRepository;
import ma.gov.social.partenariat.service.criteria.DomaineProjetCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DomaineProjet} entities in the database.
 * The main input is a {@link DomaineProjetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DomaineProjet} or a {@link Page} of {@link DomaineProjet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DomaineProjetQueryService extends QueryService<DomaineProjet> {

    private final Logger log = LoggerFactory.getLogger(DomaineProjetQueryService.class);

    private final DomaineProjetRepository domaineProjetRepository;

    public DomaineProjetQueryService(DomaineProjetRepository domaineProjetRepository) {
        this.domaineProjetRepository = domaineProjetRepository;
    }

    /**
     * Return a {@link List} of {@link DomaineProjet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DomaineProjet> findByCriteria(DomaineProjetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DomaineProjet> specification = createSpecification(criteria);
        return domaineProjetRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DomaineProjet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DomaineProjet> findByCriteria(DomaineProjetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DomaineProjet> specification = createSpecification(criteria);
        return domaineProjetRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DomaineProjetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DomaineProjet> specification = createSpecification(criteria);
        return domaineProjetRepository.count(specification);
    }

    /**
     * Function to convert {@link DomaineProjetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DomaineProjet> createSpecification(DomaineProjetCriteria criteria) {
        Specification<DomaineProjet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DomaineProjet_.id));
            }
            if (criteria.getDesignationAr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesignationAr(), DomaineProjet_.designationAr));
            }
            if (criteria.getDesignationFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesignationFr(), DomaineProjet_.designationFr));
            }
            if (criteria.getProjetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjetId(), root -> root.join(DomaineProjet_.projets, JoinType.LEFT).get(Projet_.id))
                    );
            }
        }
        return specification;
    }
}
