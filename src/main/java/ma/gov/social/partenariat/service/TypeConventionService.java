package ma.gov.social.partenariat.service;

import java.util.Optional;
import ma.gov.social.partenariat.domain.TypeConvention;
import ma.gov.social.partenariat.repository.TypeConventionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypeConvention}.
 */
@Service
@Transactional
public class TypeConventionService {

    private final Logger log = LoggerFactory.getLogger(TypeConventionService.class);

    private final TypeConventionRepository typeConventionRepository;

    public TypeConventionService(TypeConventionRepository typeConventionRepository) {
        this.typeConventionRepository = typeConventionRepository;
    }

    /**
     * Save a typeConvention.
     *
     * @param typeConvention the entity to save.
     * @return the persisted entity.
     */
    public TypeConvention save(TypeConvention typeConvention) {
        log.debug("Request to save TypeConvention : {}", typeConvention);
        return typeConventionRepository.save(typeConvention);
    }

    /**
     * Update a typeConvention.
     *
     * @param typeConvention the entity to save.
     * @return the persisted entity.
     */
    public TypeConvention update(TypeConvention typeConvention) {
        log.debug("Request to update TypeConvention : {}", typeConvention);
        return typeConventionRepository.save(typeConvention);
    }

    /**
     * Partially update a typeConvention.
     *
     * @param typeConvention the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeConvention> partialUpdate(TypeConvention typeConvention) {
        log.debug("Request to partially update TypeConvention : {}", typeConvention);

        return typeConventionRepository
            .findById(typeConvention.getId())
            .map(existingTypeConvention -> {
                if (typeConvention.getNomTypeAr() != null) {
                    existingTypeConvention.setNomTypeAr(typeConvention.getNomTypeAr());
                }
                if (typeConvention.getNomTypeFr() != null) {
                    existingTypeConvention.setNomTypeFr(typeConvention.getNomTypeFr());
                }

                return existingTypeConvention;
            })
            .map(typeConventionRepository::save);
    }

    /**
     * Get all the typeConventions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeConvention> findAll(Pageable pageable) {
        log.debug("Request to get all TypeConventions");
        return typeConventionRepository.findAll(pageable);
    }

    /**
     * Get one typeConvention by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeConvention> findOne(Long id) {
        log.debug("Request to get TypeConvention : {}", id);
        return typeConventionRepository.findById(id);
    }

    /**
     * Delete the typeConvention by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeConvention : {}", id);
        typeConventionRepository.deleteById(id);
    }
}
