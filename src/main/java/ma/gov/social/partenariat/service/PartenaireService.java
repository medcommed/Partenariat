package ma.gov.social.partenariat.service;

import java.util.Optional;
import ma.gov.social.partenariat.domain.Partenaire;
import ma.gov.social.partenariat.repository.PartenaireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Partenaire}.
 */
@Service
@Transactional
public class PartenaireService {

    private final Logger log = LoggerFactory.getLogger(PartenaireService.class);

    private final PartenaireRepository partenaireRepository;

    public PartenaireService(PartenaireRepository partenaireRepository) {
        this.partenaireRepository = partenaireRepository;
    }

    /**
     * Save a partenaire.
     *
     * @param partenaire the entity to save.
     * @return the persisted entity.
     */
    public Partenaire save(Partenaire partenaire) {
        log.debug("Request to save Partenaire : {}", partenaire);
        return partenaireRepository.save(partenaire);
    }

    /**
     * Update a partenaire.
     *
     * @param partenaire the entity to save.
     * @return the persisted entity.
     */
    public Partenaire update(Partenaire partenaire) {
        log.debug("Request to update Partenaire : {}", partenaire);
        return partenaireRepository.save(partenaire);
    }

    /**
     * Partially update a partenaire.
     *
     * @param partenaire the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Partenaire> partialUpdate(Partenaire partenaire) {
        log.debug("Request to partially update Partenaire : {}", partenaire);

        return partenaireRepository
            .findById(partenaire.getId())
            .map(existingPartenaire -> {
                if (partenaire.getNomPartenaire() != null) {
                    existingPartenaire.setNomPartenaire(partenaire.getNomPartenaire());
                }
                if (partenaire.getTel() != null) {
                    existingPartenaire.setTel(partenaire.getTel());
                }
                if (partenaire.getEmail() != null) {
                    existingPartenaire.setEmail(partenaire.getEmail());
                }

                return existingPartenaire;
            })
            .map(partenaireRepository::save);
    }

    /**
     * Get all the partenaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Partenaire> findAll(Pageable pageable) {
        log.debug("Request to get all Partenaires");
        return partenaireRepository.findAll(pageable);
    }

    /**
     * Get one partenaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Partenaire> findOne(Long id) {
        log.debug("Request to get Partenaire : {}", id);
        return partenaireRepository.findById(id);
    }

    /**
     * Delete the partenaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Partenaire : {}", id);
        partenaireRepository.deleteById(id);
    }
}
