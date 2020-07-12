package player;

import builders.FootballerBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatchStatsTest {

    private MatchStats matchStats;

    @Test
    void changeRating() {
        setup(new FootballerBuilder().build());
        matchStats.changeRating(1.77f);
        assertEquals(7.77f, matchStats.getRating());
    }

    @Test
    void changeRatingMax() {
        setup(new FootballerBuilder().build());
        matchStats.changeRating(4.29f);
        assertEquals(10, matchStats.getRating());
    }

    @Test
    void changeRatingMin() {
        setup(new FootballerBuilder().build());
        matchStats.changeRating(-2.34f);
        assertEquals(4, matchStats.getRating());
    }

    @Test
    void addGoalForward() {
        setup(new FootballerBuilder().withPosition(Position.ST).build());
        matchStats.addGoal();
        assertEquals(1, matchStats.getGoals());
        assertEquals(7.25f, matchStats.getRating());
    }

    @Test
    void addGoalMidfielder() {
        setup(new FootballerBuilder().withPosition(Position.CM).build());
        matchStats.addGoal();
        assertEquals(1, matchStats.getGoals());
        assertEquals(7.5f, matchStats.getRating());
    }

    @Test
    void addGoalDefender() {
        setup(new FootballerBuilder().withPosition(Position.CB).build());
        matchStats.addGoal();
        assertEquals(1, matchStats.getGoals());
        assertEquals(7.75f, matchStats.getRating());
    }

    @Test
    void addAssistForward() {
        setup(new FootballerBuilder().withPosition(Position.ST).build());
        matchStats.addAssist();
        assertEquals(1, matchStats.getAssists());
        assertEquals(7, matchStats.getRating());
    }

    @Test
    void addAssistMidfielder() {
        setup(new FootballerBuilder().withPosition(Position.CM).build());
        matchStats.addAssist();
        assertEquals(1, matchStats.getAssists());
        assertEquals(7.25f, matchStats.getRating());
    }

    @Test
    void addAssistDefender() {
        setup(new FootballerBuilder().withPosition(Position.CB).build());
        matchStats.addAssist();
        assertEquals(1, matchStats.getAssists());
        assertEquals(7.5f, matchStats.getRating());
    }

    @Test
    void addYellowCard() {
        setup(new FootballerBuilder().build());
        matchStats.addYellowCard();
        assertTrue(matchStats.isYellowCarded());
        assertEquals(5.5f, matchStats.getRating());
    }

    @Test
    void addRedCard() {
        setup(new FootballerBuilder().build());
        matchStats.addRedCard();
        assertTrue(matchStats.isRedCarded());
        assertEquals(4, matchStats.getRating());
    }

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(MatchStats.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    private void setup(final Footballer footballer) {
        matchStats = new MatchStats(footballer);
    }
}