package ma.gov.social.partenariat.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ma.gov.social.partenariat.domain.Convention;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ConventionRepositoryWithBagRelationshipsImpl implements ConventionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Convention> fetchBagRelationships(Optional<Convention> convention) {
        return convention.map(this::fetchPartenaires);
    }

    @Override
    public Page<Convention> fetchBagRelationships(Page<Convention> conventions) {
        return new PageImpl<>(fetchBagRelationships(conventions.getContent()), conventions.getPageable(), conventions.getTotalElements());
    }

    @Override
    public List<Convention> fetchBagRelationships(List<Convention> conventions) {
        return Optional.of(conventions).map(this::fetchPartenaires).orElse(Collections.emptyList());
    }

    Convention fetchPartenaires(Convention result) {
        return entityManager
            .createQuery(
                "select convention from Convention convention left join fetch convention.partenaires where convention is :convention",
                Convention.class
            )
            .setParameter("convention", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Convention> fetchPartenaires(List<Convention> conventions) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, conventions.size()).forEach(index -> order.put(conventions.get(index).getId(), index));
        List<Convention> result = entityManager
            .createQuery(
                "select distinct convention from Convention convention left join fetch convention.partenaires where convention in :conventions",
                Convention.class
            )
            .setParameter("conventions", conventions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
