package ma.gov.social.partenariat.service;

import java.util.Optional;
import ma.gov.social.partenariat.domain.Commune;
import ma.gov.social.partenariat.repository.CommuneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Commune}.
 */
@Service
@Transactional
public class CommuneService {

    private final Logger log = LoggerFactory.getLogger(CommuneService.class);

    private final CommuneRepository communeRepository;

    public CommuneService(CommuneRepository communeRepository) {
        this.communeRepository = communeRepository;
    }

    /**
     * Save a commune.
     *
     * @param commune the entity to save.
     * @return the persisted entity.
     */
    public Commune save(Commune commune) {
        log.debug("Request to save Commune : {}", commune);
        return communeRepository.save(commune);
    }

    /**
     * Update a commune.
     *
     * @param commune the entity to save.
     * @return the persisted entity.
     */
    public Commune update(Commune commune) {
        log.debug("Request to update Commune : {}", commune);
        return communeRepository.save(commune);
    }

    /**
     * Partially update a commune.
     *
     * @param commune the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Commune> partialUpdate(Commune commune) {
        log.debug("Request to partially update Commune : {}", commune);

        return communeRepository
            .findById(commune.getId())
            .map(existingCommune -> {
                if (commune.getNomCommuneAr() != null) {
                    existingCommune.setNomCommuneAr(commune.getNomCommuneAr());
                }
                if (commune.getNomCommuneFr() != null) {
                    existingCommune.setNomCommuneFr(commune.getNomCommuneFr());
                }

                return existingCommune;
            })
            .map(communeRepository::save);
    }

    /**
     * Get all the communes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Commune> findAll(Pageable pageable) {
        log.debug("Request to get all Communes");
        return communeRepository.findAll(pageable);
    }

    /**
     * Get all the communes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Commune> findAllWithEagerRelationships(Pageable pageable) {
        return communeRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one commune by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Commune> findOne(Long id) {
        log.debug("Request to get Commune : {}", id);
        return communeRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the commune by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Commune : {}", id);
        communeRepository.deleteById(id);
    }
}
