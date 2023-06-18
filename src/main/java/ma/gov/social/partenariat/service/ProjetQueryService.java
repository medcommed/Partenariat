package ma.gov.social.partenariat.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import ma.gov.social.partenariat.domain.*; // for static metamodels
import ma.gov.social.partenariat.domain.Projet;
import ma.gov.social.partenariat.repository.ProjetRepository;
import ma.gov.social.partenariat.service.criteria.ProjetCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Projet} entities in the database.
 * The main input is a {@link ProjetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Projet} or a {@link Page} of {@link Projet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProjetQueryService extends QueryService<Projet> {

    private final Logger log = LoggerFactory.getLogger(ProjetQueryService.class);

    private final ProjetRepository projetRepository;

    public ProjetQueryService(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    /**
     * Return a {@link List} of {@link Projet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Projet> findByCriteria(ProjetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Projet> specification = createSpecification(criteria);
        return projetRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Projet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Projet> findByCriteria(ProjetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Projet> specification = createSpecification(criteria);
        return projetRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProjetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Projet> specification = createSpecification(criteria);
        return projetRepository.count(specification);
    }

    /**
     * Function to convert {@link ProjetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Projet> createSpecification(ProjetCriteria criteria) {
        Specification<Projet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Projet_.id));
            }
            if (criteria.getNomProjet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomProjet(), Projet_.nomProjet));
            }
            if (criteria.getAnneeProjet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnneeProjet(), Projet_.anneeProjet));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), Projet_.dateDebut));
            }
            if (criteria.getDureeProjet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDureeProjet(), Projet_.dureeProjet));
            }
            if (criteria.getMontantProjet() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontantProjet(), Projet_.montantProjet));
            }
            if (criteria.getComuneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getComuneId(), root -> root.join(Projet_.comune, JoinType.LEFT).get(Commune_.id))
                    );
            }
            if (criteria.getDomaineProjetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDomaineProjetId(),
                            root -> root.join(Projet_.domaineProjet, JoinType.LEFT).get(DomaineProjet_.id)
                        )
                    );
            }
            if (criteria.getConventionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConventionId(),
                            root -> root.join(Projet_.conventions, JoinType.LEFT).get(Convention_.id)
                        )
                    );
            }
            if (criteria.getSituationProjetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSituationProjetId(),
                            root -> root.join(Projet_.situationProjets, JoinType.LEFT).get(SituationProjet_.id)
                        )
                    );
            }
            if (criteria.getTrancheId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTrancheId(), root -> root.join(Projet_.tranches, JoinType.LEFT).get(Tranche_.id))
                    );
            }
        }
        return specification;
    }
}
