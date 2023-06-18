package ma.gov.social.partenariat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ma.gov.social.partenariat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartenaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Partenaire.class);
        Partenaire partenaire1 = new Partenaire();
        partenaire1.setId(1L);
        Partenaire partenaire2 = new Partenaire();
        partenaire2.setId(partenaire1.getId());
        assertThat(partenaire1).isEqualTo(partenaire2);
        partenaire2.setId(2L);
        assertThat(partenaire1).isNotEqualTo(partenaire2);
        partenaire1.setId(null);
        assertThat(partenaire1).isNotEqualTo(partenaire2);
    }
}
