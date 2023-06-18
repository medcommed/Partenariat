package ma.gov.social.partenariat.repository;

import ma.gov.social.partenariat.domain.TypeConvention;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TypeConvention entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeConventionRepository extends JpaRepository<TypeConvention, Long>, JpaSpecificationExecutor<TypeConvention> {}
