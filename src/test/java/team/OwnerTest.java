package team;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import player.Resume;

class OwnerTest {

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Owner.class).verify();
    }

    @Test
    void toStringVerifier() {
        ToStringVerifier.forClass(Owner.class).verify();
    }

}