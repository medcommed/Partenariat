package ma.gov.social.partenariat.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.gov.social.partenariat.domain.*; // for static metamodels
import ma.gov.social.partenariat.domain.Tranche;
import ma.gov.social.partenariat.repository.TrancheRepository;
import ma.gov.social.partenariat.service.criteria.TrancheCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Tranche} entities in the database.
 * The main input is a {@link TrancheCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Tranche} or a {@link Page} of {@link Tranche} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrancheQueryService extends QueryService<Tranche> {

    private final Logger log = LoggerFactory.getLogger(TrancheQueryService.class);

    private final TrancheRepository trancheRepository;

    public TrancheQueryService(TrancheRepository trancheRepository) {
        this.trancheRepository = trancheRepository;
    }

    /**
     * Return a {@link List} of {@link Tranche} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Tranche> findByCriteria(TrancheCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tranche> specification = createSpecification(criteria);
        return trancheRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Tranche} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Tranche> findByCriteria(TrancheCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tranche> specification = createSpecification(criteria);
        return trancheRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrancheCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tranche> specification = createSpecification(criteria);
        return trancheRepository.count(specification);
    }

    /**
     * Function to convert {@link TrancheCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tranche> createSpecification(TrancheCriteria criteria) {
        Specification<Tranche> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tranche_.id));
            }
            if (criteria.getNomTranche() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomTranche(), Tranche_.nomTranche));
            }
            if (criteria.getDateDeffet() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDeffet(), Tranche_.dateDeffet));
            }
            if (criteria.getMontantTranche() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontantTranche(), Tranche_.montantTranche));
            }
            if (criteria.getRapportTranche() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRapportTranche(), Tranche_.rapportTranche));
            }
            if (criteria.getProjetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjetId(), root -> root.join(Tranche_.projet, JoinType.LEFT).get(Projet_.id))
                    );
            }
        }
        return specification;
    }
}
