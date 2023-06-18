package ma.gov.social.partenariat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ma.gov.social.partenariat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeConventionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeConvention.class);
        TypeConvention typeConvention1 = new TypeConvention();
        typeConvention1.setId(1L);
        TypeConvention typeConvention2 = new TypeConvention();
        typeConvention2.setId(typeConvention1.getId());
        assertThat(typeConvention1).isEqualTo(typeConvention2);
        typeConvention2.setId(2L);
        assertThat(typeConvention1).isNotEqualTo(typeConvention2);
        typeConvention1.setId(null);
        assertThat(typeConvention1).isNotEqualTo(typeConvention2);
    }
}
