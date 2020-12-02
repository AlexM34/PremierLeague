package simulation.match;

import static simulation.Data.GOALKEEPER_1;
import static simulation.competition.League.updateLeagueStats;
import static simulation.match.Performance.goal;
import static simulation.match.Tactics.substitute;

import player.Footballer;
import player.MatchStats;
import player.Position;
import simulation.Simulator;
import simulation.competition.Competition;
import team.Club;

import java.util.List;
import java.util.Objects;

public class Match {

    public static int fans = 3;
    private final Club home;
    private final Club away;
    private final Competition competition;
    private final boolean last;
    private final int aggregateHomeGoals;
    private final int aggregateAwayGoals;
    private final Report report;

    private int minute = 1;
    private int stoppage = 0;
    private int homeSubs;
    private int awaySubs;

    public Match(final Club home, final Club away, final Competition competition,
                 final int aggregateHomeGoals, final int aggregateAwayGoals, final boolean last) {
        this.home = home;
        this.away = away;
        this.competition = competition;
        this.last = last;
        this.aggregateHomeGoals = aggregateHomeGoals;
        this.aggregateAwayGoals = aggregateAwayGoals;
        fans = 3;

        report = new Report(home, away, competition, aggregateHomeGoals, aggregateAwayGoals, last);
    }

    public Report simulate() {

        simulateGame();
        report.finalWhistle();

        if (report.getCompetition() == Competition.LEAGUE) {
            final String leagueName = report.getHome().getLeague();
            updateLeagueStats(leagueName, report.getHomeLineup().getSquad());
            updateLeagueStats(leagueName, report.getAwayLineup().getSquad());
        }

        return report;
    }

    private void simulateGame() {
        kickoff();
        minute = 0;
        int extra = 0;
        int added;

        homeSubs = 3;
        awaySubs = 3;

        while (++minute <= 90 + extra) {
            added = (minute % 45 == 0) ? 2 * minute / 45 : 0;
            stoppage = 0;

            while (stoppage <= added) event();

            if (minute == 90 && report.isStillTied()) {
                report.append("Extra time begins!<br/>");
                extra = 30;
            }
        }

        if (report.isStillTied()) {
            final boolean homeWin = new Shootout(report).isHomeWin();
            if (homeWin) report.addHomeGoal();
            else report.addAwayGoal();
        }
    }

    void kickoff() {
        for (final Footballer f : report.getHome().getFootballers()) {
            System.out.println(f.getPosition().getAttackingDuty() + " - " + f.getCondition() + ", " + f.getBan());
            if (f.getPosition() == Position.GK) f.changeCondition(13 + Simulator.getInt(3));
            else f.changeCondition(12 + Simulator.getInt(3));
        }

        System.out.println();
        for (final Footballer f : report.getAway().getFootballers()) {
            System.out.println(f.getPosition().getAttackingDuty() + " - " + f.getCondition() + ", " + f.getBan());
            if (f.getPosition() == Position.GK) f.changeCondition(13 + Simulator.getInt(3));
            else f.changeCondition(12 + Simulator.getInt(3));
        }

        report.getHomeLineup().getSquad().forEach(stats -> stats.setStarted(0));
        report.getAwayLineup().getSquad().forEach(stats -> stats.setStarted(0));
    }

    private void event() {
        final int r = Simulator.getInt(1000);
        if (r < 10 * report.getMomentum()) {
            if (r < report.getMomentum() + report.getStyle() - 41) {
                goal(report, minute, stoppage, true);
                updateRatings(3);
            } else if (r < 5 * report.getMomentum()) {
                report.changeMomentum(1);
                updateRatings(1);
            }
        } else {
            if (r > 940 + report.getMomentum() - report.getStyle()) {
                goal(report, minute, stoppage, false);
                updateRatings(-3);
            } else if (r > 999 - 5 * report.getMomentum()) {
                report.changeMomentum(-1);
                updateRatings(-1);
            }
        }

        if (Simulator.isSatisfied(1, 40)) yellowCardEvent();
        else if (Simulator.isSatisfied(1, 300)) redCardEvent();

        if (minute > 60) {
            if (homeSubs > 0 && Simulator.getInt(10) == 0) {
                substitute(report, minute, stoppage, true);
                homeSubs--;
            }

            if (awaySubs > 0 && Simulator.isSatisfied(10)) {
                substitute(report, minute, stoppage, false);
                awaySubs--;
            }
        }

        stoppage++;
    }

    private void yellowCardEvent() {
        final boolean t = Simulator.getBoolean();
        final int p = Simulator.getInt(11);
        final MatchStats footballer = (t ? report.getHomeLineup() : report.getAwayLineup())
                .getSquad().get(p);

        if (!footballer.isRedCarded()) {
            report.append(getMinute()).append(footballer.getFootballer().getName());
            if (!footballer.isYellowCarded()) {
                footballer.addYellowCard();
                report.changeBalance(t ? -1 : 1);
                report.append(" gets a yellow card<br/>");
            } else {
                footballer.addRedCard();
                report.changeBalance(t ? -10 : 10);
                footballer.getFootballer().changeBan(1);
                report.append(" gets a second yellow card and he is ejected<br/>");

                if (p == 0) goalkeeperEjected(t);
            }
        }
    }

    private void redCardEvent() {
        final boolean t = Simulator.getBoolean();
        final int p = Simulator.getInt(11);
        final MatchStats footballer = (t ? report.getHomeLineup() : report.getAwayLineup())
                .getSquad().get(p);

        if (!footballer.isRedCarded()) {
            footballer.addRedCard();
            report.changeBalance(t ? -10 : 10);
            footballer.getFootballer().changeBan(Simulator.getInt(5) == 0 ? 2 : 1);
            report.append(getMinute()).append(footballer.getFootballer().getName()).append(" gets a red card<br/>");

            if (p == 0) goalkeeperEjected(t);
        }
    }

    private void goalkeeperEjected(final boolean isHome) {
        final Lineup lineup = isHome ? report.getHomeLineup() : report.getAwayLineup();
        final List<MatchStats> squad = lineup.getSquad();
        final List<MatchStats> bench = lineup.getBench();
        float worst = 10;
        int flop = 5;

        for (int player = 6; player < 11; player++) {
            if (squad.get(player).isRedCarded() || squad.get(player).getStarted() != 1) continue;
            final float rating = squad.get(player).getRating();
            if (rating < worst) {
                worst = rating;
                flop = player;
            }
        }

        final Footballer subbedOut = squad.get(flop).getFootballer();
        final MatchStats subbedIn = bench.get(0) != null ? bench.get(0) :
                bench.stream().filter(Objects::nonNull).findFirst().orElse(new MatchStats(GOALKEEPER_1));

        report.append(getMinute()).append(subbedIn.getFootballer().getName()).append(" replaces ")
                .append(subbedOut.getName()).append("<br/>");

        report.updateStats(squad.get(flop));
        subbedIn.setStarted(minute);
        squad.set(flop, subbedIn);
        bench.set(0, null);
    }

    void updateRatings(final int scale) {
        final float homeScale = Simulator.getInt(5) + scale * 3 + 1;
        updateRating(report.getHomeLineup().getSquad(), homeScale);

        final float awayScale = Simulator.getInt(5) - scale * 3 + 1;
        updateRating(report.getAwayLineup().getSquad(), awayScale);
    }

    private void updateRating(final List<MatchStats> squad, final float change) {
        final int sign = change > 0 ? 1 : -1;

        for (int i = 0; i < Math.abs(change); i++) {
            final int player = Simulator.getInt(11);
            if (squad.get(player) == null) continue;

            float overall = (float) squad.get(player).getFootballer().getOverall() *
                    squad.get(player).getFootballer().getOverall() / 6000;

            if (sign < 0) overall = 1 / overall;
            squad.get(player).changeRating(0.1f * sign * overall);
        }
    }

    private String getMinute() {
        return minute + (stoppage != 0 ? "+" + stoppage : "") + "' ";
    }
}
