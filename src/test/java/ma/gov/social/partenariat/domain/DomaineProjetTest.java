package ma.gov.social.partenariat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ma.gov.social.partenariat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DomaineProjetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DomaineProjet.class);
        DomaineProjet domaineProjet1 = new DomaineProjet();
        domaineProjet1.setId(1L);
        DomaineProjet domaineProjet2 = new DomaineProjet();
        domaineProjet2.setId(domaineProjet1.getId());
        assertThat(domaineProjet1).isEqualTo(domaineProjet2);
        domaineProjet2.setId(2L);
        assertThat(domaineProjet1).isNotEqualTo(domaineProjet2);
        domaineProjet1.setId(null);
        assertThat(domaineProjet1).isNotEqualTo(domaineProjet2);
    }
}
