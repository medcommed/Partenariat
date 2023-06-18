package ma.gov.social.partenariat.repository;

import java.util.List;
import java.util.Optional;
import ma.gov.social.partenariat.domain.Convention;
import org.springframework.data.domain.Page;

public interface ConventionRepositoryWithBagRelationships {
    Optional<Convention> fetchBagRelationships(Optional<Convention> convention);

    List<Convention> fetchBagRelationships(List<Convention> conventions);

    Page<Convention> fetchBagRelationships(Page<Convention> conventions);
}
