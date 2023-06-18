package ma.gov.social.partenariat.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.gov.social.partenariat.domain.*; // for static metamodels
import ma.gov.social.partenariat.domain.Partenaire;
import ma.gov.social.partenariat.repository.PartenaireRepository;
import ma.gov.social.partenariat.service.criteria.PartenaireCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Partenaire} entities in the database.
 * The main input is a {@link PartenaireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Partenaire} or a {@link Page} of {@link Partenaire} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartenaireQueryService extends QueryService<Partenaire> {

    private final Logger log = LoggerFactory.getLogger(PartenaireQueryService.class);

    private final PartenaireRepository partenaireRepository;

    public PartenaireQueryService(PartenaireRepository partenaireRepository) {
        this.partenaireRepository = partenaireRepository;
    }

    /**
     * Return a {@link List} of {@link Partenaire} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Partenaire> findByCriteria(PartenaireCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Partenaire> specification = createSpecification(criteria);
        return partenaireRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Partenaire} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Partenaire> findByCriteria(PartenaireCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Partenaire> specification = createSpecification(criteria);
        return partenaireRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartenaireCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Partenaire> specification = createSpecification(criteria);
        return partenaireRepository.count(specification);
    }

    /**
     * Function to convert {@link PartenaireCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Partenaire> createSpecification(PartenaireCriteria criteria) {
        Specification<Partenaire> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Partenaire_.id));
            }
            if (criteria.getNomPartenaire() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomPartenaire(), Partenaire_.nomPartenaire));
            }
            if (criteria.getTel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTel(), Partenaire_.tel));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Partenaire_.email));
            }
            if (criteria.getConventionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConventionId(),
                            root -> root.join(Partenaire_.conventions, JoinType.LEFT).get(Convention_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
