package players;

import java.util.Objects;

public class MatchStats {
    private final Footballer footballer;
    private int started;
    private float rating;
    private int goals;
    private int assists;
    private boolean yellowCarded;
    private boolean redCarded;

    public MatchStats(final Footballer footballer, final int started) {
        this.footballer = footballer;
        this.started = started;
        this.rating = 6;
        this.goals = 0;
        this.assists = 0;
        this.yellowCarded = false;
        this.redCarded = false;
    }

    public Footballer getFootballer() {
        return footballer;
    }

    public int getStarted() {
        return started;
    }

    public float getRating() {
        return rating;
    }

    public void changeRating(final float rating) {
        this.rating += rating;
        this.rating = Math.max(this.rating, 0);
        this.rating = Math.min(this.rating, 10);
    }

    public int getGoals() {
        return goals;
    }

    public void addGoals() {
        this.goals += 1;
        this.rating += 1.25;
        if (this.getFootballer().getPosition().getAttackingDuty() < 5) {
            this.rating += 0.25;
            if (this.getFootballer().getPosition().getAttackingDuty() < 3) {
                this.rating += 0.5;
            }
        }
    }

    public int getAssists() {
        return assists;
    }

    public void addAssists() {
        this.assists += 1;
        this.rating += 1;
        if (this.getFootballer().getPosition().getAttackingDuty() < 5) {
            this.rating += 0.25;
            if (this.getFootballer().getPosition().getAttackingDuty() < 3) {
                this.rating += 0.5;
            }
        }
    }

    public boolean isYellowCarded() {
        return yellowCarded;
    }

    public void addYellowCard() {
        this.yellowCarded = true;
        this.rating -= 0.5;
    }

    public boolean isRedCarded() {
        return redCarded;
    }

    public void addRedCard() {
        this.redCarded = true;
        this.rating -= 2;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MatchStats that = (MatchStats) o;
        return started == that.started &&
                Float.compare(that.rating, rating) == 0 &&
                goals == that.goals &&
                assists == that.assists &&
                yellowCarded == that.yellowCarded &&
                redCarded == that.redCarded &&
                footballer.equals(that.footballer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(footballer, started, rating, goals, assists, yellowCarded, redCarded);
    }

    @Override
    public String toString() {
        return "MatchStats{" +
                "footballer=" + footballer +
                ", started=" + started +
                ", rating=" + rating +
                ", goals=" + goals +
                ", assists=" + assists +
                ", yellowCarded=" + yellowCarded +
                ", redCarded=" + redCarded +
                '}';
    }
}
