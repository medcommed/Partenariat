package ma.gov.social.partenariat.service;

import java.util.Optional;
import ma.gov.social.partenariat.domain.Convention;
import ma.gov.social.partenariat.repository.ConventionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Convention}.
 */
@Service
@Transactional
public class ConventionService {

    private final Logger log = LoggerFactory.getLogger(ConventionService.class);

    private final ConventionRepository conventionRepository;

    public ConventionService(ConventionRepository conventionRepository) {
        this.conventionRepository = conventionRepository;
    }

    /**
     * Save a convention.
     *
     * @param convention the entity to save.
     * @return the persisted entity.
     */
    public Convention save(Convention convention) {
        log.debug("Request to save Convention : {}", convention);
        return conventionRepository.save(convention);
    }

    /**
     * Update a convention.
     *
     * @param convention the entity to save.
     * @return the persisted entity.
     */
    public Convention update(Convention convention) {
        log.debug("Request to update Convention : {}", convention);
        return conventionRepository.save(convention);
    }

    /**
     * Partially update a convention.
     *
     * @param convention the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Convention> partialUpdate(Convention convention) {
        log.debug("Request to partially update Convention : {}", convention);

        return conventionRepository
            .findById(convention.getId())
            .map(existingConvention -> {
                if (convention.getAveanau() != null) {
                    existingConvention.setAveanau(convention.getAveanau());
                }
                if (convention.getDateDebutConv() != null) {
                    existingConvention.setDateDebutConv(convention.getDateDebutConv());
                }
                if (convention.getDureeConvention() != null) {
                    existingConvention.setDureeConvention(convention.getDureeConvention());
                }
                if (convention.getEtatConvention() != null) {
                    existingConvention.setEtatConvention(convention.getEtatConvention());
                }
                if (convention.getNbrTranche() != null) {
                    existingConvention.setNbrTranche(convention.getNbrTranche());
                }
                if (convention.getNomConvention() != null) {
                    existingConvention.setNomConvention(convention.getNomConvention());
                }
                if (convention.getObjectif() != null) {
                    existingConvention.setObjectif(convention.getObjectif());
                }

                return existingConvention;
            })
            .map(conventionRepository::save);
    }

    /**
     * Get all the conventions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Convention> findAll(Pageable pageable) {
        log.debug("Request to get all Conventions");
        return conventionRepository.findAll(pageable);
    }

    /**
     * Get all the conventions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Convention> findAllWithEagerRelationships(Pageable pageable) {
        return conventionRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one convention by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Convention> findOne(Long id) {
        log.debug("Request to get Convention : {}", id);
        return conventionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the convention by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Convention : {}", id);
        conventionRepository.deleteById(id);
    }
}
