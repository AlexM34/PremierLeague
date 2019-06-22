import java.util.Objects;

class Statistics {
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

    Competition getCup() {
        return cup;
    }

    Competition getContinental() {
        return continental;
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
