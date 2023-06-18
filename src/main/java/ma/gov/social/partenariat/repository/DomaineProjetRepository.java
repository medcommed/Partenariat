package ma.gov.social.partenariat.repository;

import ma.gov.social.partenariat.domain.DomaineProjet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DomaineProjet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DomaineProjetRepository extends JpaRepository<DomaineProjet, Long>, JpaSpecificationExecutor<DomaineProjet> {}
