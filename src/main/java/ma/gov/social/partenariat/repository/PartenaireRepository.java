package ma.gov.social.partenariat.repository;

import ma.gov.social.partenariat.domain.Partenaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Partenaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartenaireRepository extends JpaRepository<Partenaire, Long>, JpaSpecificationExecutor<Partenaire> {}
