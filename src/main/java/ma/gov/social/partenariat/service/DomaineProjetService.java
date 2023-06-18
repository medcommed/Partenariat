package ma.gov.social.partenariat.service;

import java.util.Optional;
import ma.gov.social.partenariat.domain.DomaineProjet;
import ma.gov.social.partenariat.repository.DomaineProjetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DomaineProjet}.
 */
@Service
@Transactional
public class DomaineProjetService {

    private final Logger log = LoggerFactory.getLogger(DomaineProjetService.class);

    private final DomaineProjetRepository domaineProjetRepository;

    public DomaineProjetService(DomaineProjetRepository domaineProjetRepository) {
        this.domaineProjetRepository = domaineProjetRepository;
    }

    /**
     * Save a domaineProjet.
     *
     * @param domaineProjet the entity to save.
     * @return the persisted entity.
     */
    public DomaineProjet save(DomaineProjet domaineProjet) {
        log.debug("Request to save DomaineProjet : {}", domaineProjet);
        return domaineProjetRepository.save(domaineProjet);
    }

    /**
     * Update a domaineProjet.
     *
     * @param domaineProjet the entity to save.
     * @return the persisted entity.
     */
    public DomaineProjet update(DomaineProjet domaineProjet) {
        log.debug("Request to update DomaineProjet : {}", domaineProjet);
        return domaineProjetRepository.save(domaineProjet);
    }

    /**
     * Partially update a domaineProjet.
     *
     * @param domaineProjet the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DomaineProjet> partialUpdate(DomaineProjet domaineProjet) {
        log.debug("Request to partially update DomaineProjet : {}", domaineProjet);

        return domaineProjetRepository
            .findById(domaineProjet.getId())
            .map(existingDomaineProjet -> {
                if (domaineProjet.getDesignationAr() != null) {
                    existingDomaineProjet.setDesignationAr(domaineProjet.getDesignationAr());
                }
                if (domaineProjet.getDesignationFr() != null) {
                    existingDomaineProjet.setDesignationFr(domaineProjet.getDesignationFr());
                }

                return existingDomaineProjet;
            })
            .map(domaineProjetRepository::save);
    }

    /**
     * Get all the domaineProjets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DomaineProjet> findAll(Pageable pageable) {
        log.debug("Request to get all DomaineProjets");
        return domaineProjetRepository.findAll(pageable);
    }

    /**
     * Get one domaineProjet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DomaineProjet> findOne(Long id) {
        log.debug("Request to get DomaineProjet : {}", id);
        return domaineProjetRepository.findById(id);
    }

    /**
     * Delete the domaineProjet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DomaineProjet : {}", id);
        domaineProjetRepository.deleteById(id);
    }
}
