package ma.gov.social.partenariat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ma.gov.social.partenariat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrancheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tranche.class);
        Tranche tranche1 = new Tranche();
        tranche1.setId(1L);
        Tranche tranche2 = new Tranche();
        tranche2.setId(tranche1.getId());
        assertThat(tranche1).isEqualTo(tranche2);
        tranche2.setId(2L);
        assertThat(tranche1).isNotEqualTo(tranche2);
        tranche1.setId(null);
        assertThat(tranche1).isNotEqualTo(tranche2);
    }
}
