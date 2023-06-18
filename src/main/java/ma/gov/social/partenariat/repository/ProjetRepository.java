package ma.gov.social.partenariat.repository;

import java.util.List;
import java.util.Optional;
import ma.gov.social.partenariat.domain.Projet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Projet entity.
 */
@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long>, JpaSpecificationExecutor<Projet> {
    default Optional<Projet> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Projet> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Projet> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct projet from Projet projet left join fetch projet.comune left join fetch projet.domaineProjet",
        countQuery = "select count(distinct projet) from Projet projet"
    )
    Page<Projet> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct projet from Projet projet left join fetch projet.comune left join fetch projet.domaineProjet")
    List<Projet> findAllWithToOneRelationships();

    @Query("select projet from Projet projet left join fetch projet.comune left join fetch projet.domaineProjet where projet.id =:id")
    Optional<Projet> findOneWithToOneRelationships(@Param("id") Long id);
}
