package ma.gov.social.partenariat.service;

import java.util.Optional;
import ma.gov.social.partenariat.domain.Projet;
import ma.gov.social.partenariat.repository.ProjetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Projet}.
 */
@Service
@Transactional
public class ProjetService {

    private final Logger log = LoggerFactory.getLogger(ProjetService.class);

    private final ProjetRepository projetRepository;

    public ProjetService(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    /**
     * Save a projet.
     *
     * @param projet the entity to save.
     * @return the persisted entity.
     */
    public Projet save(Projet projet) {
        log.debug("Request to save Projet : {}", projet);
        return projetRepository.save(projet);
    }

    /**
     * Update a projet.
     *
     * @param projet the entity to save.
     * @return the persisted entity.
     */
    public Projet update(Projet projet) {
        log.debug("Request to update Projet : {}", projet);
        return projetRepository.save(projet);
    }

    /**
     * Partially update a projet.
     *
     * @param projet the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Projet> partialUpdate(Projet projet) {
        log.debug("Request to partially update Projet : {}", projet);

        return projetRepository
            .findById(projet.getId())
            .map(existingProjet -> {
                if (projet.getNomProjet() != null) {
                    existingProjet.setNomProjet(projet.getNomProjet());
                }
                if (projet.getAnneeProjet() != null) {
                    existingProjet.setAnneeProjet(projet.getAnneeProjet());
                }
                if (projet.getDateDebut() != null) {
                    existingProjet.setDateDebut(projet.getDateDebut());
                }
                if (projet.getDureeProjet() != null) {
                    existingProjet.setDureeProjet(projet.getDureeProjet());
                }
                if (projet.getMontantProjet() != null) {
                    existingProjet.setMontantProjet(projet.getMontantProjet());
                }

                return existingProjet;
            })
            .map(projetRepository::save);
    }

    /**
     * Get all the projets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Projet> findAll(Pageable pageable) {
        log.debug("Request to get all Projets");
        return projetRepository.findAll(pageable);
    }

    /**
     * Get all the projets with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Projet> findAllWithEagerRelationships(Pageable pageable) {
        return projetRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one projet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Projet> findOne(Long id) {
        log.debug("Request to get Projet : {}", id);
        return projetRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the projet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Projet : {}", id);
        projetRepository.deleteById(id);
    }
}
