package player;

import java.util.Objects;

public class Statistics {
    private final Competition league;
    private final Competition nationalCup;
    private final Competition leagueCup;
    private final Competition championsLeague;
    private final Competition europaLeague;

    Statistics() {
        this.league = new Competition();
        this.nationalCup = new Competition();
        this.leagueCup = new Competition();
        this.championsLeague = new Competition();
        this.europaLeague = new Competition();
    }

    public Competition getLeague() {
        return league;
    }

    public Competition getNationalCup() {
        return nationalCup;
    }

    public Competition getLeagueCup() {
        return leagueCup;
    }

    public Competition getChampionsLeague() {
        return championsLeague;
    }

    public Competition getEuropaLeague() {
        return europaLeague;
    }

    public void clear() {
        league.clear();
        nationalCup.clear();
        leagueCup.clear();
        championsLeague.clear();
        europaLeague.clear();
    }

    public void update(final Statistics statistics) {
        league.update(statistics.getLeague());
        nationalCup.update(statistics.getNationalCup());
        leagueCup.update(statistics.getLeagueCup());
        championsLeague.update(statistics.getChampionsLeague());
        europaLeague.update(statistics.getEuropaLeague());
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Statistics)) return false;
        final Statistics that = (Statistics) o;
        return Objects.equals(league, that.league) &&
                Objects.equals(nationalCup, that.nationalCup) &&
                Objects.equals(leagueCup, that.leagueCup) &&
                Objects.equals(championsLeague, that.championsLeague) &&
                Objects.equals(europaLeague, that.europaLeague);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(league, nationalCup, leagueCup, championsLeague, europaLeague);
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "main.league=" + league +
                ", nationalCup=" + nationalCup +
                ", leagueCup=" + leagueCup +
                ", championsLeague=" + championsLeague +
                ", europaLeague=" + europaLeague +
                '}';
    }
}
