package player;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class GloryTest {

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Glory.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void toStringVerifier() {
        ToStringVerifier.forClass(Glory.class).verify();
    }

}