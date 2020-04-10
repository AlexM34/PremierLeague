package simulation.dynamics;

import player.Footballer;
import team.Club;

public class Finance {

    public static void leaguePrizes(final Club[] league) {
        for (int team = 0; team < league.length; team++) {
            if (team == 0) league[team].changeBudget(50f);
            else if (team < 4) league[team].changeBudget(40f - (team - 1) * 5f);
            else if (team < 7) league[team].changeBudget(26f - (team - 4) * 3f);
            else league[team].changeBudget(25f - team);
        }
    }

    public static void knockoutPrizes(final Club[] clubs, final boolean isContinental) {
        final float base = isContinental ? 10f : 1f;

        for (int team = 0; team < clubs.length; team++) {
            if (team == 0) clubs[team].changeBudget(10 * base);
            else if (team == 1) clubs[team].changeBudget(7 * base);
            else if (team < 4) clubs[team].changeBudget(4 * base);
            else if (team < 8) clubs[team].changeBudget(2 * base);
            else clubs[team].changeBudget(base);
        }
    }

    public static void merchandise(final Club[] league) {
        for (final Club club : league) club.changeBudget((float) club.getReputation() * club.getReputation() / 500);
    }

    public static void salaries(final Club[] league) {
        for (final Club club : league) {
            System.out.println(club.getName() + " has before " + club.getBudget());
            for (final Footballer footballer : club.getFootballers()) club.changeBudget(-footballer.getWage() / 80000);
            System.out.println(club.getName() + " has after " + club.getBudget());
        }
    }
}
