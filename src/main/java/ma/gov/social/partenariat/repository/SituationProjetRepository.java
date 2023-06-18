package ma.gov.social.partenariat.repository;

import ma.gov.social.partenariat.domain.SituationProjet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SituationProjet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SituationProjetRepository extends JpaRepository<SituationProjet, Long>, JpaSpecificationExecutor<SituationProjet> {}
