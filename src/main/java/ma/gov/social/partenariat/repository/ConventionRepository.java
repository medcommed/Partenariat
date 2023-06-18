package ma.gov.social.partenariat.repository;

import java.util.List;
import java.util.Optional;
import ma.gov.social.partenariat.domain.Convention;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Convention entity.
 *
 * When extending this class, extend ConventionRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ConventionRepository
    extends ConventionRepositoryWithBagRelationships, JpaRepository<Convention, Long>, JpaSpecificationExecutor<Convention> {
    default Optional<Convention> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Convention> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Convention> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
