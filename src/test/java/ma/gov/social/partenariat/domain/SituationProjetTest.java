package ma.gov.social.partenariat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ma.gov.social.partenariat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SituationProjetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SituationProjet.class);
        SituationProjet situationProjet1 = new SituationProjet();
        situationProjet1.setId(1L);
        SituationProjet situationProjet2 = new SituationProjet();
        situationProjet2.setId(situationProjet1.getId());
        assertThat(situationProjet1).isEqualTo(situationProjet2);
        situationProjet2.setId(2L);
        assertThat(situationProjet1).isNotEqualTo(situationProjet2);
        situationProjet1.setId(null);
        assertThat(situationProjet1).isNotEqualTo(situationProjet2);
    }
}
