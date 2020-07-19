package team;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class StadiumTest {

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Stadium.class).verify();
    }

    @Test
    void toStringVerifier() {
        ToStringVerifier.forClass(Stadium.class).verify();
    }

}