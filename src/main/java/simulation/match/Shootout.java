package simulation.match;

import player.Footballer;
import player.MatchStats;

import java.util.List;

public class Shootout {

    private final List<MatchStats> homeSquad;
    private final List<MatchStats> awaySquad;
    private Integer homeGoals;
    private Integer awayGoals;
    private Integer currentHome;
    private Integer currentAway;
    private int taken;
    private boolean homeTurn;

    Shootout(final Lineup homeSquad, final Lineup awaySquad) {
        this.homeSquad = homeSquad.getSquad();
        this.awaySquad = awaySquad.getSquad();
        this.homeGoals = 0;
        this.awayGoals = 0;
        this.currentHome = 0;
        this.currentAway = 0;
        this.taken = 0;
        this.homeTurn = true;
    }

    boolean isHomeWin(final StringBuilder report) {
        report.append("It's time for the penalty shootout!<br/>");

        do {
            takePenalty(report);
        } while (!isOver());

        return homeGoals > awayGoals;
    }

    private void takePenalty(final StringBuilder report) {
        if (homeTurn) homePenalty(report);
        else awayPenalty(report);

        report.append(homeGoals).append("-").append(awayGoals).append("<br/>");
        taken++;
        homeTurn = !homeTurn;
    }

    private void homePenalty(final StringBuilder report) {
        do {
            currentHome = (currentHome + 10) % 11;

        } while(homeSquad.get(currentHome).isRedCarded());

        homeGoals += Penalty.take(getNextTaker(homeSquad, currentHome), awaySquad.get(0).getFootballer(), report);
    }

    private void awayPenalty(final StringBuilder report) {
        do {
            currentAway = (currentAway + 10) % 11;

        } while(awaySquad.get(currentAway).isRedCarded());

        awayGoals += Penalty.take(getNextTaker(awaySquad, currentAway), homeSquad.get(0).getFootballer(), report);
    }

    private Footballer getNextTaker(final List<MatchStats> squad, Integer current) {
        do {
            current = (current + 10) % 11;

        } while(squad.get(current).isRedCarded());

        return squad.get(current).getFootballer();
    }

    private boolean isOver() {
        final int difference = homeGoals - awayGoals;
        if (difference == 0) return false;
        if (taken >= 10) return taken % 2 == 0;

        final int remaining = difference > 0 ? (11 - taken) / 2 : (10 - taken) / 2;
        return remaining < Math.abs(difference);
    }
}
