package simulation.dynamics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static simulation.Data.LEAGUES;
import static simulation.dynamics.Preseason.retired;

import builders.FootballerBuilder;
import league.LeagueManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import player.Footballer;
import player.Glory;
import team.Club;
import team.Coach;
import team.Formation;
import team.Owner;
import team.Stadium;

import java.util.HashMap;
import java.util.Map;

class PreseasonTest {

    private final FootballerBuilder footballerBuilder = new FootballerBuilder();

    @BeforeAll
    static void setup() {
        final Club club = new Club(101, "Arsenal", 1886, new Stadium(1, "", 1, "", 2, 2), "London", "",
                new Glory(0, 0, 13, 13, 2, 0, 0, 0),
                79, 100, 60, new Owner(1, "", "", 1, 1), new Coach(1, "", 1, Formation.F5, 2, 1, 1));
        final Club[][] cs = new Club[1][1];
        cs[0][0] = club;

//        PowerMockito.mockStatic(LeagueManager.class);
//        PowerMockito.when(LeagueManager.getAllLeagues()).thenReturn(cs);
    }

    @Disabled
    @Test
    void progressionAgeUpdated() {
        final Map<Footballer, Integer> ages = new HashMap<>();

        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
                for (int i = 0; i < 11; i++) {
                    final Footballer footballer = footballerBuilder.build();
                    ages.put(footballer, footballer.getAge());
                    club.addFootballer(footballer);
                }
            }
        }

        int retiredPlayers = 0;
        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
                for (final Footballer footballer : club.getFootballers()) {
                    assertEquals(ages.get(footballer) + 1, footballer.getAge());
                    if (footballer.getTeam().equals("")) {
                        retiredPlayers++;
                        assertTrue(retired.contains(footballer));
                    }
                }
            }
        }

        assertEquals(retiredPlayers, retired.size());

        verifyStatic(LeagueManager.class, times(1));
    }
}
