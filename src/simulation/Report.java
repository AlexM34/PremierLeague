package simulation;

import team.Club;

public class Report {
    private final Club home;
    private final Club away;
    private final Competition competition;
    private int homeGoals;
    private int awayGoals;
    private int aggregateHomeGoals;
    private int aggregateAwayGoals;
    private final boolean last;
    private final StringBuilder report;

    Report(final Club home, final Club away, final Competition competition,
           final int aggregateHomeGoals, final int aggregateAwayGoals, final boolean last) {
        this.home = home;
        this.away = away;
        this.competition = competition;
        this.homeGoals = 0;
        this.awayGoals = 0;
        this.aggregateHomeGoals = aggregateHomeGoals;
        this.aggregateAwayGoals = aggregateAwayGoals;
        this.last = last;
        this.report = new StringBuilder();
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

    int getHomeGoals() {
        return aggregateHomeGoals;
    }

    public void addHomeGoal() {
        aggregateHomeGoals++;
    }

    int getAwayGoals() {
        return aggregateAwayGoals;
    }

    public void addAwayGoal() {
        aggregateAwayGoals++;
    }

    int getAggregateHomeGoals() {
        return aggregateHomeGoals;
    }

    int getAggregateAwayGoals() {
        return aggregateAwayGoals;
    }

    boolean isLast() {
        return last;
    }

    StringBuilder getReport() {
        return report;
    }

    StringBuilder append(final String string) {
        return report.append(string);
    }
}
