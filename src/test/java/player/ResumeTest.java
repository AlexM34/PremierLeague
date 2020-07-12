package player;

import builders.ClubBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import team.Club;

class ResumeTest {

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Resume.class)
                .withPrefabValues(Club.class, new ClubBuilder().build(), new ClubBuilder().withId(2).build())
                .verify();
    }

}