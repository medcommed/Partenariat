package ma.gov.social.partenariat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ma.gov.social.partenariat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConventionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Convention.class);
        Convention convention1 = new Convention();
        convention1.setId(1L);
        Convention convention2 = new Convention();
        convention2.setId(convention1.getId());
        assertThat(convention1).isEqualTo(convention2);
        convention2.setId(2L);
        assertThat(convention1).isNotEqualTo(convention2);
        convention1.setId(null);
        assertThat(convention1).isNotEqualTo(convention2);
    }
}
