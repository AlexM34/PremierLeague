package team;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import player.Resume;

class CoachTest {

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Coach.class).verify();
    }

    @Test
    void toStringVerifier() {
        ToStringVerifier.forClass(Coach.class).verify();
    }

}