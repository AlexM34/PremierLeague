package simulation.match;

import static simulation.Data.GOALKEEPER_1;
import static simulation.Data.USER;
import static simulation.Data.userStyle;
import static simulation.competition.League.updateLeagueStats;

import player.Footballer;
import player.MatchStats;
import player.Position;
import player.Statistics;
import simulation.Simulator;
import simulation.competition.Competition;
import team.Club;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

public class Match {

    private static final PrintStream STREAM = new PrintStream(new FileOutputStream(FileDescriptor.out));

    public static int fans = 3;
    private final Club home;
    private final Club away;
    private final Competition competition;
    private final boolean last;
    private final int aggregateHomeGoals;
    private final int aggregateAwayGoals;
    private final StringBuilder report;

    private int minute = 0;
    private int stoppage = 0;
    private int homeGoals = 0;
    private int awayGoals = 0;
    private int homeSubs = 3;
    private int awaySubs = 3;
    private final Tactics homeTactics;
    private final Lineup homeLineup;
    private final Tactics awayTactics;
    private final Lineup awayLineup;
    private MatchStats motmPlayer;
    private float motmRating;
    private int balance;
    private int momentum;
    private final int style;

    public Match(final Club home, final Club away, final Competition competition,
                 final int aggregateHomeGoals, final int aggregateAwayGoals, final boolean last) {
        this.home = home;
        this.away = away;
        this.competition = competition;
        this.last = last;
        this.aggregateHomeGoals = aggregateHomeGoals;
        this.aggregateAwayGoals = aggregateAwayGoals;
        this.homeTactics = new Tactics(home);
        this.homeLineup = homeTactics.pickSquad();
        this.awayTactics = new Tactics(away);
        this.awayLineup = awayTactics.pickSquad();
        this.motmPlayer = new MatchStats(null);
        this.motmRating = 0;

        final int homeAttack = getAttack(true);
        final int awayAttack = getAttack(false);

        balance = fans + 6 * (homeAttack - awayAttack) / 7  +
                (home.getSeason().getForm() - away.getSeason().getForm()) / 4 + 50;
        momentum = balance;

        if (home.getId() == USER || away.getId() == USER) style = (homeAttack + awayAttack) / 10 + userStyle / 2 - 11;
        else style = (homeAttack + awayAttack) / 10 - 6;

        report = new StringBuilder();
    }

    public void simulate() {
        simulateGame();
        finalWhistle();

        if (competition == Competition.LEAGUE) {
            final String leagueName = home.getLeague();
            updateLeagueStats(leagueName, homeLineup.getSquad());
            updateLeagueStats(leagueName, awayLineup.getSquad());
        }
    }

    private void simulateGame() {
        kickoff();
        int extra = 0;
        int added;

        while (++minute <= 90 + extra) {
            added = (minute % 45 == 0) ? 2 * minute / 45 : 0;
            stoppage = 0;

            while (stoppage <= added) event();

            if (minute == 90 && isStillTied()) {
                report.append("Extra time begins!<br/>");
                extra = 30;
            }
        }

        if (isStillTied()) {
            final boolean homeWin = new Shootout(homeLineup, awayLineup).isHomeWin(report);
            if (homeWin) homeGoals++;
            else awayGoals++;
        }
    }

    void kickoff() {
        for (final Footballer f : home.getFootballers()) {
            STREAM.println(f.getPosition().getAttackingDuty() + " - " + f.getCondition() + ", " + f.getBan());
            if (f.getPosition() == Position.GK) f.changeCondition(13 + Simulator.getInt(3));
            else f.changeCondition(12 + Simulator.getInt(3));
        }

        STREAM.println();
        for (final Footballer f : away.getFootballers()) {
            STREAM.println(f.getPosition().getAttackingDuty() + " - " + f.getCondition() + ", " + f.getBan());
            if (f.getPosition() == Position.GK) f.changeCondition(13 + Simulator.getInt(3));
            else f.changeCondition(12 + Simulator.getInt(3));
        }

        homeLineup.getSquad().forEach(stats -> stats.setStarted(0));
        awayLineup.getSquad().forEach(stats -> stats.setStarted(0));
    }

    private void event() {
        final int r = Simulator.getInt(1000);
        if (r < 10 * momentum) {
            if (r < momentum + style - 41) {
                goal(true);
            } else if (r < 5 * momentum) {
                momentum++;
                updateRatings(1);
            }

        } else {
            if (r > 940 + momentum - style) {
                goal(false);
            } else if (r > 999 - 5 * momentum) {
                momentum--;
                updateRatings(-1);
            }
        }

        if (Simulator.isSatisfied(1, 40)) yellowCardEvent();
        else if (Simulator.isSatisfied(1, 300)) redCardEvent();

        if (minute > 60) {
            if (homeSubs > 0 && Simulator.isSatisfied(10)) {
                final MatchStats subbedOut = homeTactics.substitute(minute, stoppage, report);
                updateStats(subbedOut);
                homeSubs--;
            }

            if (awaySubs > 0 && Simulator.isSatisfied(10)) {
                final MatchStats subbedOut = awayTactics.substitute(minute, stoppage, report);
                updateStats(subbedOut);
                awaySubs--;
            }
        }

        stoppage++;
    }

    private void yellowCardEvent() {
        final boolean t = Simulator.getBoolean();
        final int p = Simulator.getInt(11);
        final MatchStats footballer = (t ? homeLineup : awayLineup)
                .getSquad().get(p);

        if (!footballer.isRedCarded()) {
            report.append(getMinute()).append(footballer.getFootballer().getName());
            if (!footballer.isYellowCarded()) {
                footballer.addYellowCard();
                balance += t ? -1 : 1;
                report.append(" gets a yellow card<br/>");
            } else {
                footballer.addRedCard();
                balance += t ? -10 : 10;
                footballer.getFootballer().changeBan(1);
                report.append(" gets a second yellow card and he is ejected<br/>");

                if (p == 0) goalkeeperEjected(t);
            }
        }
    }

    private void redCardEvent() {
        final boolean t = Simulator.getBoolean();
        final int p = Simulator.getInt(11);
        final MatchStats footballer = (t ? homeLineup : awayLineup)
                .getSquad().get(p);

        if (!footballer.isRedCarded()) {
            footballer.addRedCard();
            balance += t ? -10 : 10;
            footballer.getFootballer().changeBan(Simulator.getInt(5) == 0 ? 2 : 1);
            report.append(getMinute()).append(footballer.getFootballer().getName()).append(" gets a red card<br/>");

            if (p == 0) goalkeeperEjected(t);
        }
    }

    private void goalkeeperEjected(final boolean isHome) {
        final Lineup lineup = isHome ? homeLineup : awayLineup;
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

        updateStats(squad.get(flop));
        subbedIn.setStarted(minute);
        squad.set(flop, subbedIn);
        bench.set(0, null);
    }

    void updateRatings(final int scale) {
        final float homeScale = Simulator.getInt(5) + scale * 3 + 1f;
        updateRating(homeLineup.getSquad(), homeScale);

        final float awayScale = Simulator.getInt(5) - scale * 3 + 1f;
        updateRating(awayLineup.getSquad(), awayScale);
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

    boolean isStillTied() {
        return last && (aggregateHomeGoals == -1 && homeGoals == awayGoals ||
                homeGoals == aggregateAwayGoals && awayGoals == aggregateHomeGoals);
    }

    public void finalWhistle() {
        if (homeLineup.getStats(0).getFootballer() != null && awayGoals == 0) {
            getCompetition(homeLineup.getStats(0).getFootballer()
                    .getResume().getSeason(), competition.getType()).addCleanSheets(1);
        }

        if (awayLineup.getStats(0).getFootballer() != null && homeGoals == 0) {
            getCompetition(awayLineup.getStats(0).getFootballer()
                    .getResume().getSeason(), competition.getType()).addCleanSheets(1);
        }

        for (int player = 0; player < 11; player++) {
            updateStats(homeLineup.getStats(player));
            updateStats(awayLineup.getStats(player));
        }

        getCompetition(motmPlayer.getFootballer().getResume().getSeason(),
                competition.getType()).addMotmAwards(1);

        if (competition == Competition.LEAGUE) updateLeague(home, away, homeGoals, awayGoals);
        updateForm();

        STREAM.printf("%s - %s %d:%d   --- %s %.2f%n", home.getName(),
                away.getName(), homeGoals, awayGoals, motmPlayer.getFootballer().getName(), motmRating);
    }

    private static player.Competition getCompetition(final Statistics season, final int type) {
        switch (type) {
            case 0: return season.getLeague();
            case 1: return season.getNationalCup();
            case 2: return season.getLeagueCup();
            case 3: return season.getChampionsLeague();
            default: return season.getEuropaLeague();
        }
    }

    public void updateStats(final MatchStats matchStats) {
        if (matchStats == null) return;

        if (matchStats.getRating() > motmRating) {
            motmPlayer = matchStats;
            motmRating = matchStats.getRating();
        }

        final player.Competition stats = getCompetition(
                matchStats.getFootballer().getResume().getSeason(), competition.getType());

        stats.addRating((int) matchStats.getRating() * 100, 1);
        stats.addGoals(matchStats.getGoals());
        stats.addAssists(matchStats.getAssists());

        if (matchStats.isYellowCarded()) stats.addYellowCards(1);
        if (matchStats.isRedCarded()) stats.addRedCards(1);

        matchStats.getFootballer().changeCondition(-15);
    }

    private static void updateLeague(final Club home, final Club away, final int homeGoals, final int awayGoals) {
        home.getSeason().getLeague().addFixture(away, true, homeGoals, awayGoals);
        away.getSeason().getLeague().addFixture(home, false, awayGoals, homeGoals);
    }

    private void updateForm() {
        final int homeForm = home.getSeason().getForm();
        final int awayForm = away.getSeason().getForm();

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

    private void modifyForm(final int homeChange) {
        home.getSeason().changeForm(homeChange);
        away.getSeason().changeForm(-homeChange);
    }

    private int getAttack(final boolean isHome) {
        int attack = 1;
        int defence = 1;
        final Lineup team = isHome ? homeLineup : awayLineup;
        final Lineup opponent = isHome ? awayLineup : homeLineup;

        for (final MatchStats footballer : team.getSquad()) {
            attack += footballer.getFootballer().getOverall() * footballer.getFootballer().getPosition().getAttackingDuty();
        }

        for (final MatchStats footballer : opponent.getSquad()) {
            defence += footballer.getFootballer().getOverall() * (5 - footballer.getFootballer().getPosition().getAttackingDuty());
        }

        return 50 * attack / defence;
    }

    public void appendScore(final Club home, final Club away, final StringBuilder scores, final StringBuilder reports) {
        final String score = home.getName() + " - " + away.getName() + " " + homeGoals + ":" + awayGoals + "<br/>";
        scores.append(score);

        final String teams = home.getName() + " vs " + away.getName() + "<br/>";
        final String finalScore = "Final score: " + homeGoals + ":" + awayGoals + "<br/>" + "<br/>";
        reports.append(teams).append(report).append(finalScore);
    }

    void goal(final boolean isHome) {
        if (isHome) homeGoals++;
        else awayGoals++;

        individualCredits(isHome);

        momentum = balance;
        updateRatings(isHome ? 3 : -3);
    }

    private void individualCredits(final boolean isHome) {
        final List<MatchStats> squad = (isHome ? homeLineup : awayLineup).getSquad();
        int scoring = 3000;
        int assisting = 10000;

        for (int player = 0; player < 11; player++) {
            if (squad.get(player) == null || squad.get(player).isRedCarded()) continue;
            scoring += squad.get(player).getFootballer().getScoringChance();
            assisting += squad.get(player).getFootballer().getAssistChance();
        }


        final Footballer goalscorer = pickGoalscorer(squad, scoring);

        if (goalscorer == null) {
            final List<MatchStats> opponent = (isHome ? awayLineup : homeLineup)
                    .getSquad();
            ownGoal(opponent);
            return;
        }

        Footballer assistmaker = pickAssistmaker(squad, assisting, goalscorer);

        report.append(minute).append(stoppage != 0 ? "+" + stoppage : "")
                .append("' ").append(goalscorer.getName()).append(assistmaker != null
                ? " scores after a pass from " + assistmaker.getName() : " scores after a solo run")
                .append(". ").append(homeGoals).append("-").append(awayGoals).append("<br/>");
    }

    private Footballer pickGoalscorer(final List<MatchStats> squad, final int scoring) {
        int r = Simulator.getInt(scoring);
        for (int player = 0; player < 11; player++) {
            if (squad.get(player) == null || squad.get(player).isRedCarded()) continue;
            r -= squad.get(player).getFootballer().getScoringChance();
            if (r < 0) {
                squad.get(player).addGoal();
                return squad.get(player).getFootballer();
            }
        }

        return null;
    }

    private Footballer pickAssistmaker(final List<MatchStats> squad, final int assisting, final Footballer goalscorer) {
        int r = Simulator.getInt(assisting);
        for (int player = 0; player < 11; player++) {
            if (squad.get(player) == null || squad.get(player).isRedCarded()) continue;
            r -= squad.get(player).getFootballer().getAssistChance();

            if (r < 0) {
                if (squad.get(player).getFootballer().equals(goalscorer)) {
                    squad.get(player).changeRating(0.25f);
                    return null;

                } else {
                    squad.get(player).addAssist();
                    return squad.get(player).getFootballer();
                }
            }
        }

        return null;
    }

    private void ownGoal(final List<MatchStats> opponent) {
        int index = pickOwnGoalScorer();

        if (opponent.get(index).isRedCarded()) index = 0;
        opponent.get(index).changeRating(-1.5f);
        report.append(minute).append(stoppage != 0 ? "+" + stoppage : "")
                .append("' ").append("Own goal scored by ").append(opponent.get(index).getFootballer().getName())
                .append(". ").append(homeGoals).append("-").append(awayGoals).append("<br/>");
    }

    private int pickOwnGoalScorer() {
        if (Simulator.isSatisfied(20)) return 0;
        if (Simulator.isSatisfied(60)) return 1 + Simulator.getInt(4);
        if (Simulator.isSatisfied(70)) return 5 + Simulator.getInt(3);
        return 8 + Simulator.getInt(3);
    }

    private String getMinute() {
        return minute + (stoppage != 0 ? "+" + stoppage : "") + "' ";
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }
}
