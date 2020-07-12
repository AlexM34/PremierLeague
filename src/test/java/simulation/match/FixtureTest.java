package simulation.match;

import builders.ClubBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import team.Club;

class FixtureTest {

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Fixture.class)
                .withPrefabValues(Club.class, new ClubBuilder().build(), new ClubBuilder().withId(2).build())
                .verify();
    }

}