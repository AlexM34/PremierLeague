package team;

import builders.ClubBuilder;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeasonTest {

    private final Season season = new Season("test");

    @Test
    void changeForm() {
        season.changeForm(3);
        assertEquals(13, season.getForm());
    }

    @Test
    void changeFormMax() {
        season.changeForm(11);
        assertEquals(20, season.getForm());
    }

    @Test
    void changeFormMin() {
        season.changeForm(-10);
        assertEquals(1, season.getForm());
    }

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Season.class)
                .withPrefabValues(Club.class, new ClubBuilder().build(), new ClubBuilder().withId(2).build())
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void toStringVerifier() {
        ToStringVerifier.forClass(Season.class).verify();
    }

}