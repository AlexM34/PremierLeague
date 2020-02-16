package simulation.match;

import player.Competition;
import player.Footballer;
import player.MatchStats;
import player.Position;
import team.Club;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static simulation.Data.GOALKEEPER_1;
import static simulation.Helper.getCompetition;
import static simulation.match.Performance.goal;
import static simulation.match.Tactics.substitute;

public class Match {
    private static final Random random = new Random();
    public static int FANS = 3;

    public static final Map<String, Integer> leagueAssists = new HashMap<>();
    public static final Map<String, Integer> leagueYellowCards = new HashMap<>();
    public static final Map<String, Integer> leagueRedCards = new HashMap<>();
    public static final Map<String, Float> leagueAverageRatings = new HashMap<>();
    private static MatchStats motmPlayer;
    private static float motmRating;

    static int competition = 0;
    static String leagueName;
    static int minute = 1;
    static int stoppage = 0;
    static int homeSubs;
    static int awaySubs;
    static Report report;

    public static void simulate(final Report report) {
        competition = report.getCompetition().getType();
        Match.report = report;

        if (competition == 0) leagueName = report.getHome().getLeague();
        else leagueName = null;

        simulateGame();
        FANS = 3;
        finalWhistle();
    }

    private static void simulateGame() {
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
            report.append("It's time for the penalty shootout!<br/>");
            final boolean homeWin = penaltyShootout();
            if (homeWin) report.addHomeGoal();
            else report.addAwayGoal();
        }
    }

    static void kickoff() {
        for (final Footballer f : report.getHome().getFootballers()) {
            if (f.getPosition() == Position.GK) f.changeCondition(13 + random.nextInt(3));
            else f.changeCondition(12 + random.nextInt(3));
        }

        for (final Footballer f : report.getAway().getFootballers()) {
            if (f.getPosition() == Position.GK) f.changeCondition(13 + random.nextInt(3));
            else f.changeCondition(12 + random.nextInt(3));
        }
    }

    private static void event() {
        final int r = random.nextInt(1000);
        if (r < 10 * report.getMomentum()) {
            if (r < report.getMomentum() + report.getStyle() - 41) {
                goal(true);
                updateRatings(3);
            } else if (r < 5 * report.getMomentum()) {
                report.changeMomentum(1);
                updateRatings(1);
            }
        } else {
            if (r > 940 + report.getMomentum() - report.getStyle()) {
                goal(false);
                updateRatings(-3);
            } else if (r > 999 - 5 * report.getMomentum()) {
                report.changeMomentum(-1);
                updateRatings(-1);
            }
        }

        if (random.nextInt(40) == 0) yellowCardEvent();
        else if (random.nextInt(300) == 0) redCardEvent();

        if (minute > 60) {
            if (homeSubs > 0 && random.nextInt(10) == 0) {
                substitute(report.getHomeSquad(), report.getHomeBench());
                homeSubs--;
            }

            if (awaySubs > 0 && random.nextInt(10) == 0) {
                substitute(report.getAwaySquad(), report.getAwayBench());
                awaySubs--;
            }
        }

        stoppage++;
    }

    private static void yellowCardEvent() {
        final boolean t = random.nextBoolean();
        final int p = random.nextInt(11);
        final MatchStats footballer = (t ? report.getHomeSquad() : report.getAwaySquad()).get(p);

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

    private static void redCardEvent() {
        final boolean t = random.nextBoolean();
        final int p = random.nextInt(11);
        final MatchStats footballer = (t ? report.getHomeSquad() : report.getAwaySquad()).get(p);

        if (!footballer.isRedCarded()) {
            footballer.addRedCard();
            report.changeBalance(t ? -10 : 10);
            footballer.getFootballer().changeBan(random.nextInt(5) == 0 ? 2 : 1);
            report.append(getMinute()).append(footballer.getFootballer().getName()).append(" gets a red card<br/>");

            if (p == 0) goalkeeperEjected(t);
        }
    }

    private static void goalkeeperEjected(final boolean isHome) {
        final List<MatchStats> squad = isHome ? report.getHomeSquad() : report.getAwaySquad();
        final List<Footballer> bench = isHome ? report.getHomeBench() : report.getAwayBench();
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

        report.append(getMinute()).append(subbedIn.getName()).append(" replaces ")
                .append(subbedOut.getName()).append("<br/>");

        updateStats(squad.get(flop));
        squad.set(flop, new MatchStats(subbedIn, minute));
        bench.set(0, null);
    }

    private static boolean penaltyShootout() {
        final List<MatchStats> homeSquad = report.getHomeSquad();
        final List<MatchStats> awaySquad = report.getAwaySquad();
        int homeGoals = 0;
        int awayGoals = 0;
        int currentHome = 10;
        int currentAway = 10;
        int taken = 0;

        while(true) {
            while (homeSquad.get(currentHome) == null) currentHome = (currentHome + 10) % 11;
            homeGoals += penalty(homeSquad.get(currentHome), awaySquad.get(0));
            report.append(String.valueOf(homeGoals)).append("-").append(awayGoals).append("<br/>");
            if (taken < 5 && (homeGoals > awayGoals + 5 - taken || homeGoals + 4 - taken < awayGoals)) break;
            currentHome = (currentHome + 10) % 11;

            while (awaySquad.get(currentAway) == null) currentAway = (currentAway + 10) % 11;
            awayGoals += penalty(awaySquad.get(currentAway), homeSquad.get(0));
            report.append(String.valueOf(homeGoals)).append("-").append(awayGoals).append("<br/>");
            if (taken < 4 && (awayGoals > homeGoals + 4 - taken || awayGoals + 4 - taken < homeGoals)
                    || taken >= 4 && homeGoals != awayGoals) break;
            currentAway = (currentAway + 10) % 11;

            taken++;
        }

        return homeGoals > awayGoals;
    }

    private static int penalty(final MatchStats striker, final MatchStats goalkeeper) {
        report.append(striker.getFootballer().getName()).append(" steps up to take the penalty vs ")
                .append(goalkeeper.getFootballer().getName()).append("<br/>");
        if (random.nextInt(100) < 70 + striker.getFootballer().getOverall() - goalkeeper.getFootballer().getOverall()) {
            report.append("He scores with a great shot! ");
            return 1;
        } else {
            report.append("The goalkeeper makes a wonderful save! ");
            return 0;
        }
    }

    static void finalWhistle() {
        final Club home = report.getHome();
        final Club away = report.getAway();
        final int homeGoals = report.getHomeGoals();
        final int awayGoals = report.getAwayGoals();

        if (report.getHomeSquad().get(0) != null && awayGoals == 0) getCompetition(report.getHomeSquad().get(0)
                .getFootballer().getResume().getSeason(), competition).addCleanSheets(1);
        if (report.getAwaySquad().get(0) != null && homeGoals == 0) getCompetition(report.getAwaySquad().get(0)
                .getFootballer().getResume().getSeason(), competition).addCleanSheets(1);

        motmPlayer = report.getHomeSquad().get(0);
        motmRating = 0;

        for (int player = 0; player < 11; player++) {
            updateStats(report.getHomeSquad().get(player));
            updateStats(report.getAwaySquad().get(player));
        }

        getCompetition(motmPlayer.getFootballer().getResume().getSeason(), competition).addMotmAwards(1);
        if (competition == 0) updateLeague(home, away, homeGoals, awayGoals);
        updateForm();

        System.out.println(String.format("%s - %s %d:%d   --- %s %.2f", home.getName(),
                away.getName(), homeGoals, awayGoals, motmPlayer.getFootballer().getName(), motmRating));
    }

    static void updateStats(final MatchStats matchStats) {
        if (matchStats == null) return;

        if (matchStats.getRating() > motmRating) {
            motmPlayer = matchStats;
            motmRating = matchStats.getRating();
        }

        final Competition stats = getCompetition(matchStats.getFootballer().getResume().getSeason(), competition);

        stats.addRating((int) matchStats.getRating() * 100, 1);
        if (competition == 0) leagueAverageRatings.merge(leagueName, matchStats.getRating(), Float::sum);
        stats.addMatches(1);
        stats.addGoals(matchStats.getGoals());
        stats.addAssists(matchStats.getAssists());
        if (competition == 0) leagueAssists.merge(leagueName, matchStats.getAssists(), Integer::sum);
        if (matchStats.isYellowCarded()) {
            stats.addYellowCards();
            if (competition == 0) leagueYellowCards.merge(leagueName, 1, Integer::sum);
        }

        if (matchStats.isRedCarded()) {
            stats.addRedCards();
            if (competition == 0) leagueRedCards.merge(leagueName, 1, Integer::sum);
        }

        matchStats.getFootballer().changeCondition(-15);
    }

    private static void updateLeague(final Club home, final Club away, final int homeGoals, final int awayGoals) {
        home.getSeason().getLeague().addFixture(away, true, homeGoals, awayGoals);
        away.getSeason().getLeague().addFixture(home, false, awayGoals, homeGoals);
    }

    private static void updateForm() {
        final int homeGoals = report.getHomeGoals();
        final int awayGoals = report.getAwayGoals();
        final int homeForm = report.getHome().getSeason().getForm();
        final int awayForm = report.getAway().getSeason().getForm();

        if (homeGoals > awayGoals) {
            if (homeGoals - awayGoals > 2) modifyForm(1);

            if (homeForm < awayForm + 10) {
                if (homeForm < awayForm) modifyForm(3);
                else modifyForm(1);
            }

        } else if (homeGoals < awayGoals) {
            if (awayGoals - homeGoals > 2) modifyForm(-1);

            if (awayForm < homeForm + 10) {
                if (awayForm < homeForm) modifyForm(-3);
                else modifyForm(-1);
            }

        } else {
            if (homeForm < awayForm) modifyForm(2);
            else if (awayForm < homeForm) modifyForm(-2);
        }
    }

    private static void modifyForm(final int homeChange) {
        report.getHome().getSeason().changeForm(homeChange);
        report.getAway().getSeason().changeForm(-homeChange);
    }

    static void updateRatings(final int scale) {
        final int home = random.nextInt(5) + scale * 3 + 1;
        final int away = random.nextInt(5) - scale * 3 + 1;

        for (int i = 0; i < home; i++) updateRating(report.getHomeSquad(), 0.1f);
        for (int i = 0; i > home; i--) updateRating(report.getHomeSquad(), -0.1f);
        for (int i = 0; i < away; i++) updateRating(report.getAwaySquad(), 0.1f);
        for (int i = 0; i > away; i--) updateRating(report.getAwaySquad(), -0.1f);
    }

    private static void updateRating(final List<MatchStats> squad, final float change) {
        final int player = random.nextInt(11);
        if (squad.get(player) == null) return;
        float overall = (float) squad.get(player).getFootballer().getOverall() *
                squad.get(player).getFootballer().getOverall() / 6000;

        if (change < 0) overall = 1 / overall;
        squad.get(player).changeRating(change * overall);
    }

    private static String getMinute() {
        return minute + (stoppage != 0 ? "+" + stoppage : "") + "' ";
    }
}
