package simulation;

import players.Footballer;
import players.MatchStats;
import team.Club;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static simulation.Data.FANS;
import static simulation.Data.MIDFIELDER_1;
import static simulation.Data.USER;
import static simulation.Data.USER_STYLE;
import static simulation.Tactics.pickSquad;

public class Report {
    private final Club home;
    private final Club away;
    private final Competition competition;
    private final boolean last;
    private int homeGoals;
    private int awayGoals;
    private int aggregateHomeGoals;
    private int aggregateAwayGoals;
    private List<MatchStats> homeSquad;
    private List<Footballer> homeBench;
    private List<MatchStats> awaySquad;
    private List<Footballer> awayBench;
    private int balance;
    private int momentum;
    private final int style;
    private final StringBuilder report;

    Report(final Club home, final Club away, final Competition competition,
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

        final int homeAttack = getAttack(homeSquad, awaySquad);
        final int awayAttack = getAttack(awaySquad, homeSquad);

        balance = FANS + 6 * (homeAttack - awayAttack) / 7  +
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

    Club getHome() {
        return home;
    }

    Club getAway() {
        return away;
    }

    Competition getCompetition() {
        return competition;
    }

    boolean isLast() {
        return last;
    }

    int getHomeGoals() {
        return homeGoals;
    }

    public void addHomeGoal() {
        homeGoals++;
    }

    int getAwayGoals() {
        return awayGoals;
    }

    public void addAwayGoal() {
        awayGoals++;
    }

    int getAggregateHomeGoals() {
        return aggregateHomeGoals;
    }

    int getAggregateAwayGoals() {
        return aggregateAwayGoals;
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

    int getBalance() {
        return balance;
    }

    void changeBalance(final int change) {
        this.balance += change;
    }

    int getMomentum() {
        return momentum;
    }

    void setMomentum(final int momentum) {
        this.momentum = momentum;
    }

    void changeMomentum(final int change) {
        this.momentum += change;
    }

    int getStyle() {
        return style;
    }

    StringBuilder getReport() {
        return report;
    }

    StringBuilder append(final String string) {
        return report.append(string);
    }
}
