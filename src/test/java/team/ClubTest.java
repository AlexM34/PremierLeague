package team;

import builders.ClubBuilder;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class ClubTest {

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Club.class)
                .withPrefabValues(Club.class, new ClubBuilder().build(), new ClubBuilder().withId(2).build())
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void toStringVerifier() {
        ToStringVerifier.forClass(Club.class).verify();
    }

}