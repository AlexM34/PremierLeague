package simulation.match;

import player.Footballer;
import player.MatchStats;
import player.Statistics;
import simulation.competition.Competition;
import team.Club;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static simulation.Data.MIDFIELDER_1;
import static simulation.Data.USER;
import static simulation.Data.USER_STYLE;
import static simulation.match.Match.fans;
import static simulation.match.Tactics.pickSquad;

public class Report {
    private final Club home;
    private final Club away;
    private final Competition competition;
    private final boolean last;
    private int homeGoals;
    private int awayGoals;
    private final int aggregateHomeGoals;
    private final int aggregateAwayGoals;
    private final List<MatchStats> homeSquad;
    private final List<Footballer> homeBench;
    private final List<MatchStats> awaySquad;
    private final List<Footballer> awayBench;
    private MatchStats motmPlayer;
    private float motmRating;
    private int balance;
    private int momentum;
    private final int style;
    private final StringBuilder report;

    public Report(final Club home, final Club away, final Competition competition,
                  final int aggregateHomeGoals, final int aggregateAwayGoals, final boolean last) {
        this.home = home;
        this.away = away;
        this.competition = competition;
        this.last = last;
        this.homeGoals = 0;
        this.awayGoals = 0;
        this.aggregateHomeGoals = aggregateHomeGoals;
        this.aggregateAwayGoals = aggregateAwayGoals;
        this.report = new StringBuilder();

        final List<Footballer[]> homeSelection = pickSquad(home);
        this.homeSquad = Arrays.stream(homeSelection.get(0))
                .map(f -> new MatchStats(f, 0)).collect(Collectors.toList());
        this.homeBench = Arrays.asList(homeSelection.get(1));

        final List<Footballer[]> awaySelection = pickSquad(away);
        this.awaySquad = Arrays.stream(awaySelection.get(0))
                .map(f -> new MatchStats(f, 0)).collect(Collectors.toList());
        this.awayBench = Arrays.asList(awaySelection.get(1));

        this.motmPlayer = new MatchStats(null, -1);
        this.motmRating = 0;

        final int homeAttack = getAttack(homeSquad, awaySquad);
        final int awayAttack = getAttack(awaySquad, homeSquad);

        balance = fans + 6 * (homeAttack - awayAttack) / 7  +
                (home.getSeason().getForm() - away.getSeason().getForm()) / 4 + 50;
        momentum = balance;

        if (home.getId() == USER || away.getId() == USER) style = (homeAttack + awayAttack) / 10 + USER_STYLE / 2 - 11;
        else style = (homeAttack + awayAttack) / 10 - 6;
    }

    private int getAttack(final List<MatchStats> homeSquad, final List<MatchStats> awaySquad) {
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

    public Club getHome() {
        return home;
    }

    public Club getAway() {
        return away;
    }

    public Competition getCompetition() {
        return competition;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void addHomeGoal() {
        homeGoals++;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public void addAwayGoal() {
        awayGoals++;
    }

    List<MatchStats> getHomeSquad() {
        return homeSquad;
    }

    List<Footballer> getHomeBench() {
        return homeBench;
    }

    List<MatchStats> getAwaySquad() {
        return awaySquad;
    }

    List<Footballer> getAwayBench() {
        return awayBench;
    }

    void changeBalance(final int change) {
        this.balance += change;
    }

    int getMomentum() {
        return momentum;
    }

    void clearMomentum() {
        this.momentum = balance;
    }

    void changeMomentum(final int change) {
        this.momentum += change;
    }

    int getStyle() {
        return style;
    }

    boolean isStillTied() {
        return last && (aggregateHomeGoals == -1 && homeGoals == awayGoals ||
                homeGoals == aggregateAwayGoals && awayGoals == aggregateHomeGoals);
    }

    StringBuilder append(final String string) {
        return report.append(string);
    }

    public void appendScore(final StringBuilder scores, final StringBuilder reports) {
        final String score = home.getName() + " - " + away.getName() + " " + homeGoals + ":" + awayGoals + "<br/>";
        scores.append(score);

        final String teams = home.getName() + " vs " + away.getName() + "<br/>";
        final String finalScore = "Final score: " + homeGoals + ":" + awayGoals + "<br/>" + "<br/>";
        reports.append(teams).append(report).append(finalScore);
    }

    public void finalWhistle() {
        if (homeSquad.get(0) != null && awayGoals == 0) getCompetition(homeSquad.get(0)
                .getFootballer().getResume().getSeason(), competition.getType()).addCleanSheets(1);

        if (awaySquad.get(0) != null && homeGoals == 0) getCompetition(awaySquad.get(0)
                .getFootballer().getResume().getSeason(), competition.getType()).addCleanSheets(1);

        for (int player = 0; player < 11; player++) {
            updateStats(homeSquad.get(player));
            updateStats(awaySquad.get(player));
        }

        getCompetition(motmPlayer.getFootballer().getResume().getSeason(),
                competition.getType()).addMotmAwards(1);

        if (competition == Competition.LEAGUE) updateLeague(home, away, homeGoals, awayGoals);
        updateForm();

        System.out.println(String.format("%s - %s %d:%d   --- %s %.2f", home.getName(),
                away.getName(), homeGoals, awayGoals, motmPlayer.getFootballer().getName(), motmRating));
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

        stats.addMatches(1);
        stats.addGoals(matchStats.getGoals());
        stats.addAssists(matchStats.getAssists());

        if (matchStats.isYellowCarded()) stats.addYellowCards();
        if (matchStats.isRedCarded()) stats.addRedCards();

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
}
