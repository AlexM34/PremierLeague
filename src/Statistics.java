import java.util.Objects;

class Statistics {
    private Competition league;
    private Competition cup;
    private Competition continental;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistics that = (Statistics) o;
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
