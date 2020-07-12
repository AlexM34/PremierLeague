package team;

import builders.ClubBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class CupTest {

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Cup.class)
                .withPrefabValues(Club.class, new ClubBuilder().build(), new ClubBuilder().withId(2).build())
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

}