package ma.gov.social.partenariat.service;

import java.util.Optional;
import ma.gov.social.partenariat.domain.Province;
import ma.gov.social.partenariat.repository.ProvinceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Province}.
 */
@Service
@Transactional
public class ProvinceService {

    private final Logger log = LoggerFactory.getLogger(ProvinceService.class);

    private final ProvinceRepository provinceRepository;

    public ProvinceService(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    /**
     * Save a province.
     *
     * @param province the entity to save.
     * @return the persisted entity.
     */
    public Province save(Province province) {
        log.debug("Request to save Province : {}", province);
        return provinceRepository.save(province);
    }

    /**
     * Update a province.
     *
     * @param province the entity to save.
     * @return the persisted entity.
     */
    public Province update(Province province) {
        log.debug("Request to update Province : {}", province);
        return provinceRepository.save(province);
    }

    /**
     * Partially update a province.
     *
     * @param province the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Province> partialUpdate(Province province) {
        log.debug("Request to partially update Province : {}", province);

        return provinceRepository
            .findById(province.getId())
            .map(existingProvince -> {
                if (province.getLibelleProvinceAr() != null) {
                    existingProvince.setLibelleProvinceAr(province.getLibelleProvinceAr());
                }
                if (province.getLibelleProvinceFr() != null) {
                    existingProvince.setLibelleProvinceFr(province.getLibelleProvinceFr());
                }

                return existingProvince;
            })
            .map(provinceRepository::save);
    }

    /**
     * Get all the provinces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Province> findAll(Pageable pageable) {
        log.debug("Request to get all Provinces");
        return provinceRepository.findAll(pageable);
    }

    /**
     * Get all the provinces with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Province> findAllWithEagerRelationships(Pageable pageable) {
        return provinceRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one province by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Province> findOne(Long id) {
        log.debug("Request to get Province : {}", id);
        return provinceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the province by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Province : {}", id);
        provinceRepository.deleteById(id);
    }
}
