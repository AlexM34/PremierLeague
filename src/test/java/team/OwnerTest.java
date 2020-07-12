package team;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class OwnerTest {

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Owner.class).verify();
    }

}