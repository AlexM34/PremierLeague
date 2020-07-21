package simulation.competition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static simulation.competition.Competition.CHAMPIONS_LEAGUE;
import static simulation.competition.Competition.EUROPA_LEAGUE;
import static simulation.competition.Competition.LEAGUE;
import static simulation.competition.Competition.LEAGUE_CUP;
import static simulation.competition.Competition.NATIONAL_CUP;

class CompetitionTest {

    @Test
    void getType() {
        final Competition[] values = Competition.values();
        assertEquals(5, values.length);
        assertEquals(LEAGUE, values[0]);
        assertEquals(NATIONAL_CUP, values[1]);
        assertEquals(LEAGUE_CUP, values[2]);
        assertEquals(CHAMPIONS_LEAGUE, values[3]);
        assertEquals(EUROPA_LEAGUE, values[4]);
    }

}
