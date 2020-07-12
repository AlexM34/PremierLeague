package team;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class StadiumTest {

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Stadium.class).verify();
    }

}