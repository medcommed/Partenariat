package ma.gov.social.partenariat.repository;

import ma.gov.social.partenariat.domain.Tranche;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tranche entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrancheRepository extends JpaRepository<Tranche, Long>, JpaSpecificationExecutor<Tranche> {}
