package players;

import java.util.Objects;

public class Statistics {
    private final Competition league;
    private final Competition cup;
    private final Competition continental;

    Statistics() {
        this.league = new Competition();
        this.cup = new Competition();
        this.continental = new Competition();
    }

    public Competition getLeague() {
        return league;
    }

    public Competition getCup() {
        return cup;
    }

    public Competition getContinental() {
        return continental;
    }

    public void clear() {
        league.clear();
        cup.clear();
        continental.clear();
    }

    public void update(final Statistics statistics) {
        league.update(statistics.getLeague());
        cup.update(statistics.getCup());
        continental.update(statistics.getContinental());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Statistics that = (Statistics) o;
        return league.equals(that.league) &&
                cup.equals(that.cup) &&
                continental.equals(that.continental);
    }

    @Override
    public int hashCode() {
        return Objects.hash(league, cup, continental);
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "league=" + league +
                ", cup=" + cup +
                ", continental=" + continental +
                '}';
    }
}
