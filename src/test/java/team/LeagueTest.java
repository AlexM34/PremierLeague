package team;

import builders.ClubBuilder;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulation.match.Fixture;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeagueTest {

    private static final String LEAGUE = "Test";

    private final League league = new League(LEAGUE);

    @BeforeEach
    void setup() {
        League.clearLeagueStats();
    }

    @Test
    void addFixtures() {
        league.addFixture(new ClubBuilder().withId(1).build(), true, 4, 1);
        league.addFixture(new ClubBuilder().withId(2).build(), false, 2, 2);
        league.addFixture(new ClubBuilder().withId(3).build(), true, 5, 0);
        league.addFixture(new ClubBuilder().withId(4).build(), false, 0, 2);
        league.addFixture(new ClubBuilder().withId(5).build(), false, 3, 0);

        assertEquals(new Fixture(new ClubBuilder().withId(1).build(), true, new int[]{4, 1}),
                league.getFixtures().get(0));
        assertEquals(new Fixture(new ClubBuilder().withId(2).build(), false, new int[]{2, 2}),
                league.getFixtures().get(1));
        assertEquals(new Fixture(new ClubBuilder().withId(3).build(), true, new int[]{5, 0}),
                league.getFixtures().get(2));
        assertEquals(new Fixture(new ClubBuilder().withId(4).build(), false, new int[]{0, 2}),
                league.getFixtures().get(3));
        assertEquals(new Fixture(new ClubBuilder().withId(5).build(), false, new int[]{3, 0}),
                league.getFixtures().get(4));

        assertEquals(2, league.getCleanSheets());
        assertEquals(5, league.getMatches());
        assertEquals(3, league.getWins());
        assertEquals(1, league.getDraws());
        assertEquals(1, league.getLosses());
        assertEquals(10, league.getPoints());
        assertEquals(14, league.getScored());
        assertEquals(5, league.getConceded());
    }

    @Test
    void addWinHome() {
        league.addWin(true);
        assertEquals(1, league.getWins());
        assertEquals(3, league.getPoints());
        assertEquals("1", League.getLeagueHomeWins(LEAGUE));
        assertEquals("0", League.getLeagueAwayWins(LEAGUE));
    }

    @Test
    void addWinAway() {
        league.addWin(false);
        assertEquals(1, league.getWins());
        assertEquals(3, league.getPoints());
        assertEquals("0", League.getLeagueHomeWins(LEAGUE));
        assertEquals("1", League.getLeagueAwayWins(LEAGUE));
    }

    @Test
    void addDrawHome() {
        league.addDraw(true);
        assertEquals(1, league.getDraws());
        assertEquals(1, league.getPoints());
        assertEquals("1", League.getLeagueDraws(LEAGUE));
    }

    @Test
    void addDrawAway() {
        league.addDraw(false);
        assertEquals(1, league.getDraws());
        assertEquals(1, league.getPoints());
        assertEquals("0", League.getLeagueDraws(LEAGUE));
    }

    @Test
    void addScoredHome() {
        league.addScored(3, true);
        assertEquals(3, league.getScored());
        assertEquals("3", League.getLeagueScoredHome(LEAGUE));
        assertEquals("0", League.getLeagueScoredAway(LEAGUE));
    }

    @Test
    void addScoredAway() {
        league.addScored(2, false);
        assertEquals(2, league.getScored());
        assertEquals("0", League.getLeagueScoredHome(LEAGUE));
        assertEquals("2", League.getLeagueScoredAway(LEAGUE));
    }

    @Test
    void addCleanSheet() {
        league.addCleanSheet();
        assertEquals(1, league.getCleanSheets());
        assertEquals("1", League.getLeagueCleanSheets(LEAGUE));
    }

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(League.class)
                .withPrefabValues(Club.class, new ClubBuilder().build(), new ClubBuilder().withId(2).build())
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void toStringVerifier() {
        ToStringVerifier.forClass(League.class)
                .withPrefabValue(Fixture.class, new Fixture(new ClubBuilder().build(), true, new int[]{2, 1}))
                .verify();
    }

}
