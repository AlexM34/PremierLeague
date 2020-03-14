package simulation.dynamics;

import helpers.FootballerBuilder;
import main.player.Footballer;
import main.team.Club;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static main.simulation.Data.LEAGUES;
import static main.simulation.dynamics.Preseason.progression;
import static main.simulation.dynamics.Preseason.retired;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PreseasonTest {

    private final FootballerBuilder footballerBuilder = new FootballerBuilder();

    @BeforeAll
    static void setup() {

    }

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

        progression();

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
    }
}
