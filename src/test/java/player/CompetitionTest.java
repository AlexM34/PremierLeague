package player;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static player.Helper.randomiseCompetition;

class CompetitionTest {

    private final Competition competition = new Competition();

    @Test
    void clear() {
        randomiseCompetition(competition);
        competition.clear();
        assertEquals(new Competition(), competition);
    }

    @Test
    void addRatingZeroMatches() {
        competition.addRating(700, 0);
        assertEquals(0, competition.getRating());
    }

    @Test
    void addRatingMultiple() {
        competition.addRating(700, 10);
        assertEquals(10, competition.getMatches());
        assertEquals(700, competition.getRating());

        competition.addRating(580, 2);
        assertEquals(12, competition.getMatches());
        assertEquals(680, competition.getRating());

        competition.addRating(800, 24);
        assertEquals(36, competition.getMatches());
        assertEquals(760, competition.getRating());
    }

    @Test
    void update() {
        final Competition season = new Competition();
        randomiseCompetition(season);

        competition.update(season);
        assertEquals(season, competition);
    }

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Competition.class)
                .suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    void toStringVerifier() {
        ToStringVerifier.forClass(Competition.class).verify();
    }

}