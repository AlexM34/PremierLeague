package simulation;

import players.Footballer;
import players.MatchStats;
import team.Club;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static simulation.Data.FANS;
import static simulation.Data.GOALKEEPER_1;
import static simulation.Data.MIDFIELDER_1;
import static simulation.Data.USER;
import static simulation.Data.USER_STYLE;
import static simulation.Performance.goal;
import static simulation.Rater.finalWhistle;
import static simulation.Rater.kickoff;
import static simulation.Rater.updateRatings;
import static simulation.Rater.updateStats;
import static simulation.Tactics.pickSquad;
import static simulation.Tactics.substitute;

class Match {
    private static final Random random = new Random();

    static int competition = 0;
    static String leagueName;
    static int minute = 1;
    static int stoppage = 0;

    static List<MatchStats> homeSquad = new ArrayList<>();
    static List<MatchStats> awaySquad = new ArrayList<>();
    static List<Footballer> homeBench = new ArrayList<>();
    static List<Footballer> awayBench = new ArrayList<>();
    static int homeSubs;
    static int awaySubs;
    static StringBuilder gameReport;

    static int[] simulate(final Report report) {
        competition = report.getCompetition().getType();
        gameReport = report.getReport();
        
        if (competition == 0) leagueName = report.getHome().getLeague();
        final int[] result = simulateGame(report);
        FANS = 3;

        finalWhistle(report.getHome(), report.getAway(), result[0], result[1]);
        return result;
    }

    private static int[] simulateGame(final Report report) {
        final Club home = report.getHome();
        final Club away = report.getAway();
        final boolean last = report.isLast();
        final int aggregateHomeGoals = report.getAggregateHomeGoals();
        final int aggregateAwayGoals = report.getAggregateAwayGoals();

        kickoff(home, away);
        int homeGoals = 0;
        int awayGoals = 0;
        minute = 1;
        int extra = 0;
        int added;

        pickSquad(home, true);
        pickSquad(away, false);
        homeSubs = 3;
        awaySubs = 3;

        final int homeAttack = getAttack(homeSquad, awaySquad);
        final int awayAttack = getAttack(awaySquad, homeSquad);
        int balance = FANS + 6 * (homeAttack - awayAttack) / 7  +
                (home.getSeason().getForm() - away.getSeason().getForm()) / 4 + 50;

        int momentum = balance;
        int style = (homeAttack + awayAttack) / 10 - 6;

        if (home.getId() == USER || away.getId() == USER) style += USER_STYLE / 2 - 5;

        while (minute <= 90 + extra) {
            added = (minute % 45 == 0) ? 2 * minute / 45 : 0;
            stoppage = 0;

            while (stoppage <= added) {
                final int r = random.nextInt(1000);
                if (r < 10 * momentum) {
                    if (r < momentum + style - 41) {
                        homeGoals++;
                        goal(homeGoals, awayGoals, true);
                        momentum = balance;
                        updateRatings(3);
                    } else if (r < 5 * momentum) {
                        momentum++;
                        updateRatings(1);
                    }
                } else {
                    if (r > 940 + momentum - style) {
                        awayGoals++;
                        goal(homeGoals, awayGoals, false);
                        momentum = balance;
                        updateRatings(-3);
                    } else if (r > 999 - 5 * momentum) {
                        momentum--;
                        updateRatings(-1);
                    }
                }

                if (random.nextInt(40) == 0) {
                    final boolean t = random.nextBoolean();
                    final int p = random.nextInt(11);
                    final MatchStats footballer = (t ? homeSquad : awaySquad).get(p);

                    if (!footballer.isRedCarded()) {
                        report.append(getMinute()).append(footballer.getFootballer().getName());
                        if (!footballer.isYellowCarded()) {
                            footballer.addYellowCard();
                            balance += (t ? -1 : 1);
                            report.append(" gets a yellow card<br/>");
                        } else {
                            footballer.addRedCard();
                            balance += (t ? -10 : 10);
                            footballer.getFootballer().changeBan(1);
                            report.append(" gets a second yellow card and he is ejected<br/>");

                            if (p == 0) goalkeeperEjected(t);
                        }
                    }
                } else if (random.nextInt(300) == 0) {
                    final boolean t = random.nextBoolean();
                    final int p = random.nextInt(11);
                    final MatchStats footballer = (t ? homeSquad : awaySquad).get(p);

                    if (!footballer.isRedCarded()) {
                        footballer.addRedCard();
                        balance += (t ? -10 : 10);
                        footballer.getFootballer().changeBan(random.nextInt(5) == 0 ? 2 : 1);
                        report.append(getMinute()).append(footballer.getFootballer().getName()).append(" gets a red card<br/>");

                        if (p == 0) goalkeeperEjected(t);
                    }
                }

                if (minute > 60) {
                    if (homeSubs > 0 && random.nextInt(10) == 0) substitute(true);
                    if (awaySubs > 0 && random.nextInt(10) == 0) substitute(false);
                }

                stoppage++;
            }

            if (minute == 90 && last && (aggregateHomeGoals == -1 && homeGoals == awayGoals ||
                    homeGoals == aggregateAwayGoals && awayGoals == aggregateHomeGoals)) {
                report.append("Extra time begins!<br/>");
                extra = 30;
            }

            minute++;
        }

        if (last && (aggregateHomeGoals == -1 && homeGoals == awayGoals ||
                homeGoals == aggregateAwayGoals && awayGoals == aggregateHomeGoals)) {
            report.append("It's time for the penalty shootout!<br/>");
            final boolean homeWin = penaltyShootout(homeSquad, awaySquad);
            if (homeWin) homeGoals++;
            else awayGoals++;
        }

        return new int[]{homeGoals, awayGoals};
    }

    private static void goalkeeperEjected(final boolean isHome) {
        final List<MatchStats> squad = isHome ? homeSquad : awaySquad;
        final List<Footballer> bench = isHome ? homeBench : awayBench;
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
        final Footballer subbedIn = bench.get(0) != null ? bench.get(0) :
                bench.stream().filter(Objects::nonNull).findFirst().orElse(GOALKEEPER_1);

        gameReport.append(getMinute()).append(subbedIn.getName()).append(" replaces ")
                .append(subbedOut.getName()).append("<br/>");

        updateStats(squad.get(flop));
        squad.set(flop, new MatchStats(subbedIn, minute));
        bench.set(0, null);

        if (isHome) homeSubs--;
        else awaySubs--;
    }

    private static int getAttack(final List<MatchStats> homeSquad, final List<MatchStats> awaySquad) {
        int attack = 0;
        int defence = 0;
        for (MatchStats footballer : homeSquad) {
            if (footballer == null) footballer = new MatchStats(MIDFIELDER_1, 0);
            attack += footballer.getFootballer().getOverall() * footballer.getFootballer().getPosition().getAttackingDuty();
        }

        for (MatchStats footballer : awaySquad) {
            if (footballer == null) footballer = new MatchStats(MIDFIELDER_1, 0);
            defence += footballer.getFootballer().getOverall() * (5 - footballer.getFootballer().getPosition().getAttackingDuty());
        }

        return 50 * attack / defence;
    }

    private static boolean penaltyShootout(final List<MatchStats> homeSquad, final List<MatchStats> awaySquad) {
        int homeGoals = 0;
        int awayGoals = 0;
        int currentHome = 10;
        int currentAway = 10;
        int taken = 0;

        while(true) {
            while (homeSquad.get(currentHome) == null) currentHome = (currentHome + 10) % 11;
            homeGoals += penalty(homeSquad.get(currentHome), awaySquad.get(0));
            gameReport.append(homeGoals).append("-").append(awayGoals).append("<br/>");
            if (taken < 5 && (homeGoals > awayGoals + 5 - taken || homeGoals + 4 - taken < awayGoals)) break;
            currentHome = (currentHome + 10) % 11;

            while (awaySquad.get(currentAway) == null) currentAway = (currentAway + 10) % 11;
            awayGoals += penalty(awaySquad.get(currentAway), homeSquad.get(0));
            gameReport.append(homeGoals).append("-").append(awayGoals).append("<br/>");
            if (taken < 4 && (awayGoals > homeGoals + 4 - taken || awayGoals + 4 - taken < homeGoals)
                    || taken >= 4 && homeGoals != awayGoals) break;
            currentAway = (currentAway + 10) % 11;

            taken++;
        }

        return homeGoals > awayGoals;
    }

    private static int penalty(final MatchStats striker, final MatchStats goalkeeper) {
        gameReport.append(striker.getFootballer().getName()).append(" steps up to take the penalty vs ")
                .append(goalkeeper.getFootballer().getName()).append("<br/>");
        if (random.nextInt(100) < 70 + striker.getFootballer().getOverall() - goalkeeper.getFootballer().getOverall()) {
            gameReport.append("He scores with a great shot! ");
            return 1;
        } else {
            gameReport.append("The goalkeeper makes a wonderful save! ");
            return 0;
        }
    }

    private static String getMinute() {
        return minute + (stoppage != 0 ? "+" + stoppage : "") + "' ";
    }
}
