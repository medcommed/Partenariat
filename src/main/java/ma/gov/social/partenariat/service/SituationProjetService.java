package ma.gov.social.partenariat.service;

import java.util.Optional;
import ma.gov.social.partenariat.domain.SituationProjet;
import ma.gov.social.partenariat.repository.SituationProjetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SituationProjet}.
 */
@Service
@Transactional
public class SituationProjetService {

    private final Logger log = LoggerFactory.getLogger(SituationProjetService.class);

    private final SituationProjetRepository situationProjetRepository;

    public SituationProjetService(SituationProjetRepository situationProjetRepository) {
        this.situationProjetRepository = situationProjetRepository;
    }

    /**
     * Save a situationProjet.
     *
     * @param situationProjet the entity to save.
     * @return the persisted entity.
     */
    public SituationProjet save(SituationProjet situationProjet) {
        log.debug("Request to save SituationProjet : {}", situationProjet);
        return situationProjetRepository.save(situationProjet);
    }

    /**
     * Update a situationProjet.
     *
     * @param situationProjet the entity to save.
     * @return the persisted entity.
     */
    public SituationProjet update(SituationProjet situationProjet) {
        log.debug("Request to update SituationProjet : {}", situationProjet);
        return situationProjetRepository.save(situationProjet);
    }

    /**
     * Partially update a situationProjet.
     *
     * @param situationProjet the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SituationProjet> partialUpdate(SituationProjet situationProjet) {
        log.debug("Request to partially update SituationProjet : {}", situationProjet);

        return situationProjetRepository
            .findById(situationProjet.getId())
            .map(existingSituationProjet -> {
                if (situationProjet.getDateStatutValid() != null) {
                    existingSituationProjet.setDateStatutValid(situationProjet.getDateStatutValid());
                }
                if (situationProjet.getStatutProjet() != null) {
                    existingSituationProjet.setStatutProjet(situationProjet.getStatutProjet());
                }

                return existingSituationProjet;
            })
            .map(situationProjetRepository::save);
    }

    /**
     * Get all the situationProjets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SituationProjet> findAll(Pageable pageable) {
        log.debug("Request to get all SituationProjets");
        return situationProjetRepository.findAll(pageable);
    }

    /**
     * Get one situationProjet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SituationProjet> findOne(Long id) {
        log.debug("Request to get SituationProjet : {}", id);
        return situationProjetRepository.findById(id);
    }

    /**
     * Delete the situationProjet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SituationProjet : {}", id);
        situationProjetRepository.deleteById(id);
    }
}
