package ma.gov.social.partenariat.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.gov.social.partenariat.domain.*; // for static metamodels
import ma.gov.social.partenariat.domain.Commune;
import ma.gov.social.partenariat.repository.CommuneRepository;
import ma.gov.social.partenariat.service.criteria.CommuneCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Commune} entities in the database.
 * The main input is a {@link CommuneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Commune} or a {@link Page} of {@link Commune} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommuneQueryService extends QueryService<Commune> {

    private final Logger log = LoggerFactory.getLogger(CommuneQueryService.class);

    private final CommuneRepository communeRepository;

    public CommuneQueryService(CommuneRepository communeRepository) {
        this.communeRepository = communeRepository;
    }

    /**
     * Return a {@link List} of {@link Commune} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Commune> findByCriteria(CommuneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Commune> specification = createSpecification(criteria);
        return communeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Commune} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Commune> findByCriteria(CommuneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Commune> specification = createSpecification(criteria);
        return communeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommuneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Commune> specification = createSpecification(criteria);
        return communeRepository.count(specification);
    }

    /**
     * Function to convert {@link CommuneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Commune> createSpecification(CommuneCriteria criteria) {
        Specification<Commune> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Commune_.id));
            }
            if (criteria.getNomCommuneAr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomCommuneAr(), Commune_.nomCommuneAr));
            }
            if (criteria.getNomCommuneFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomCommuneFr(), Commune_.nomCommuneFr));
            }
            if (criteria.getProvincesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProvincesId(),
                            root -> root.join(Commune_.provinces, JoinType.LEFT).get(Province_.id)
                        )
                    );
            }
            if (criteria.getProjetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjetId(), root -> root.join(Commune_.projets, JoinType.LEFT).get(Projet_.id))
                    );
            }
        }
        return specification;
    }
}
