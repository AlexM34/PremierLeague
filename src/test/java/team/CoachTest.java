package team;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class CoachTest {

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Coach.class).verify();
    }

}