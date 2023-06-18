package ma.gov.social.partenariat.service;

import java.util.Optional;
import ma.gov.social.partenariat.domain.Tranche;
import ma.gov.social.partenariat.repository.TrancheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tranche}.
 */
@Service
@Transactional
public class TrancheService {

    private final Logger log = LoggerFactory.getLogger(TrancheService.class);

    private final TrancheRepository trancheRepository;

    public TrancheService(TrancheRepository trancheRepository) {
        this.trancheRepository = trancheRepository;
    }

    /**
     * Save a tranche.
     *
     * @param tranche the entity to save.
     * @return the persisted entity.
     */
    public Tranche save(Tranche tranche) {
        log.debug("Request to save Tranche : {}", tranche);
        return trancheRepository.save(tranche);
    }

    /**
     * Update a tranche.
     *
     * @param tranche the entity to save.
     * @return the persisted entity.
     */
    public Tranche update(Tranche tranche) {
        log.debug("Request to update Tranche : {}", tranche);
        return trancheRepository.save(tranche);
    }

    /**
     * Partially update a tranche.
     *
     * @param tranche the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Tranche> partialUpdate(Tranche tranche) {
        log.debug("Request to partially update Tranche : {}", tranche);

        return trancheRepository
            .findById(tranche.getId())
            .map(existingTranche -> {
                if (tranche.getNomTranche() != null) {
                    existingTranche.setNomTranche(tranche.getNomTranche());
                }
                if (tranche.getDateDeffet() != null) {
                    existingTranche.setDateDeffet(tranche.getDateDeffet());
                }
                if (tranche.getMontantTranche() != null) {
                    existingTranche.setMontantTranche(tranche.getMontantTranche());
                }
                if (tranche.getRapportTranche() != null) {
                    existingTranche.setRapportTranche(tranche.getRapportTranche());
                }

                return existingTranche;
            })
            .map(trancheRepository::save);
    }

    /**
     * Get all the tranches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Tranche> findAll(Pageable pageable) {
        log.debug("Request to get all Tranches");
        return trancheRepository.findAll(pageable);
    }

    /**
     * Get one tranche by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Tranche> findOne(Long id) {
        log.debug("Request to get Tranche : {}", id);
        return trancheRepository.findById(id);
    }

    /**
     * Delete the tranche by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tranche : {}", id);
        trancheRepository.deleteById(id);
    }
}
