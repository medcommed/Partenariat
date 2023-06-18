package ma.gov.social.partenariat.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.gov.social.partenariat.domain.*; // for static metamodels
import ma.gov.social.partenariat.domain.TypeConvention;
import ma.gov.social.partenariat.repository.TypeConventionRepository;
import ma.gov.social.partenariat.service.criteria.TypeConventionCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TypeConvention} entities in the database.
 * The main input is a {@link TypeConventionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TypeConvention} or a {@link Page} of {@link TypeConvention} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TypeConventionQueryService extends QueryService<TypeConvention> {

    private final Logger log = LoggerFactory.getLogger(TypeConventionQueryService.class);

    private final TypeConventionRepository typeConventionRepository;

    public TypeConventionQueryService(TypeConventionRepository typeConventionRepository) {
        this.typeConventionRepository = typeConventionRepository;
    }

    /**
     * Return a {@link List} of {@link TypeConvention} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TypeConvention> findByCriteria(TypeConventionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TypeConvention> specification = createSpecification(criteria);
        return typeConventionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TypeConvention} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeConvention> findByCriteria(TypeConventionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TypeConvention> specification = createSpecification(criteria);
        return typeConventionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TypeConventionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TypeConvention> specification = createSpecification(criteria);
        return typeConventionRepository.count(specification);
    }

    /**
     * Function to convert {@link TypeConventionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TypeConvention> createSpecification(TypeConventionCriteria criteria) {
        Specification<TypeConvention> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TypeConvention_.id));
            }
            if (criteria.getNomTypeAr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomTypeAr(), TypeConvention_.nomTypeAr));
            }
            if (criteria.getNomTypeFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomTypeFr(), TypeConvention_.nomTypeFr));
            }
            if (criteria.getConventionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConventionId(),
                            root -> root.join(TypeConvention_.conventions, JoinType.LEFT).get(Convention_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
