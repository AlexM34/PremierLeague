package simulation.competition;

import builders.ClubBuilder;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import team.Club;

class ContinentalTest {

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Continental.class)
                .withPrefabValues(Club.class, new ClubBuilder().build(), new ClubBuilder().withId(2).build())
                .verify();
    }

    @Test
    void toStringVerifier() {
        ToStringVerifier.forClass(Continental.class).verify();
    }

}