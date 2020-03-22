package simulation.dynamics;

import league.England;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Footballer;
import team.Club;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static simulation.dynamics.Preseason.youngster;

class FinanceTest {

    private static Club[] clubs;

    @BeforeEach
    void setup() {
        clubs = England.getClubs();
        Arrays.stream(clubs).forEach(club -> {
            club.changeBudget(-club.getBudget());
            for (int i = 0; i < 11; i++) youngster(club, null);
        });
    }

    @Test
    void leaguePrizes() {
        Finance.leaguePrizes(clubs);

        for (int team = 0; team < clubs.length; team++) {
            if (team == 0) assertEquals(50f, clubs[team].getBudget());
            else if (team < 4) assertEquals(40f - (team - 1) * 5f, clubs[team].getBudget());
            else if (team < 7) assertEquals(26f - (team - 4) * 3f, clubs[team].getBudget());
            else assertEquals(25f - team, clubs[team].getBudget());
        }
    }

    @Test
    void knockoutPrizesContinental() {
        Finance.knockoutPrizes(clubs, true);

        for (int team = 0; team < clubs.length; team++) {
            if (team == 0) assertEquals(100, clubs[team].getBudget());
            else if (team == 1) assertEquals(70, clubs[team].getBudget());
            else if (team < 4) assertEquals(40, clubs[team].getBudget());
            else if (team < 8) assertEquals(20, clubs[team].getBudget());
            else assertEquals(10, clubs[team].getBudget());
        }
    }

    @Test
    void knockoutPrizesCup() {
        Finance.knockoutPrizes(clubs, false);

        for (int team = 0; team < clubs.length; team++) {
            if (team == 0) assertEquals(10, clubs[team].getBudget());
            else if (team == 1) assertEquals(7, clubs[team].getBudget());
            else if (team < 4) assertEquals(4, clubs[team].getBudget());
            else if (team < 8) assertEquals(2, clubs[team].getBudget());
            else assertEquals(1, clubs[team].getBudget());
        }
    }

    @Test
    void merchandise() {
        Finance.merchandise(clubs);

        for (final Club club : clubs) {
            assertEquals((float) club.getReputation() * club.getReputation() / 500, club.getBudget());
        }
    }

    @Test
    void salaries() {
        Finance.salaries(clubs);

        for (final Club club : clubs) {
            float expected = 0;
            for (final Footballer f : club.getFootballers()) expected -= f.getWage() / 80000;

            assertEquals(expected, club.getBudget());
        }
    }
}
