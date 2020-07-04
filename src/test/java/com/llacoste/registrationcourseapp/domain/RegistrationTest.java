package com.llacoste.registrationcourseapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.llacoste.registrationcourseapp.web.rest.TestUtil;

public class RegistrationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Registration.class);
        Registration registration1 = new Registration();
        registration1.setId(1L);
        Registration registration2 = new Registration();
        registration2.setId(registration1.getId());
        assertThat(registration1).isEqualTo(registration2);
        registration2.setId(2L);
        assertThat(registration1).isNotEqualTo(registration2);
        registration1.setId(null);
        assertThat(registration1).isNotEqualTo(registration2);
    }
}
