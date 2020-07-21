package league;

import org.junit.jupiter.api.Test;
import team.Club;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LeagueManagerTest {

    @Test
    void getLeagues() {
        final String[] expected = {"Premier League", "La Liga", "Bundesliga", "Serie A",
            "Ligue 1", "Champions League", "Europa League"};

        assertArrayEquals(expected, LeagueManager.getLeagues());
    }

    @Test
    void getClubs() {
        assertEquals(England.getClubs(), LeagueManager.getClubs("Premier League"));
        assertEquals(Spain.getClubs(), LeagueManager.getClubs("La Liga"));
        assertEquals(Germany.getClubs(), LeagueManager.getClubs("Bundesliga"));
        assertEquals(Italy.getClubs(), LeagueManager.getClubs("Serie A"));
        assertEquals(France.getClubs(), LeagueManager.getClubs("Ligue 1"));

        final Club[] allClubs = Arrays.stream(LeagueManager.getAllLeagues())
                .flatMap(Arrays::stream).toArray(Club[]::new);
        assertArrayEquals(allClubs, LeagueManager.getClubs("All"));
    }

    @Test
    void getAllLeagues() {
        final Club[][] expected = new Club[][]{England.getClubs(), Spain.getClubs(), Germany.getClubs(),
                Italy.getClubs(), France.getClubs()};

        assertArrayEquals(expected, LeagueManager.getAllLeagues());
    }

    @Test
    void getNationalCupGames() {
        assertEquals(2, LeagueManager.getNationalCupGames("La Liga"));
        assertEquals(1, LeagueManager.getNationalCupGames("Premier League"));
        assertEquals(1, LeagueManager.getNationalCupGames("Bundesliga"));
        assertEquals(1, LeagueManager.getNationalCupGames("Serie A"));
        assertEquals(1, LeagueManager.getNationalCupGames("Ligue 1"));
        assertEquals(1, LeagueManager.getNationalCupGames("All"));
    }

    @Test
    void getLeagueCupClubs() {
        assertEquals(Arrays.asList(England.getClubs(), France.getClubs()), LeagueManager.getLeagueCupClubs());
    }

    @Test
    void isReplayed() {
        assertTrue(LeagueManager.isReplayed("Premier League"));
        assertFalse(LeagueManager.isReplayed("La Liga"));
        assertFalse(LeagueManager.isReplayed("Bundesliga"));
        assertFalse(LeagueManager.isReplayed("Serie A"));
        assertFalse(LeagueManager.isReplayed("Ligue 1"));
        assertFalse(LeagueManager.isReplayed("All"));
    }

}