import java.util.Objects;

class MatchStats {
    private final Footballer footballer;
    private int started;
    private float rating;
    private int goals;
    private int assists;
    private boolean yellowCarded;
    private boolean redCarded;

    MatchStats(final Footballer footballer, final int started) {
        this.footballer = footballer;
        this.started = started;
        this.rating = 6;
        this.goals = 0;
        this.assists = 0;
        this.yellowCarded = false;
        this.redCarded = false;
    }

    Footballer getFootballer() {
        return footballer;
    }

    int getStarted() {
        return started;
    }

    float getRating() {
        return rating;
    }

    void changeRating(final float rating) {
        this.rating += rating;
        this.rating = Math.max(this.rating, 0);
        this.rating = Math.min(this.rating, 10);
    }

    int getGoals() {
        return goals;
    }

    void addGoals() {
        this.goals += 1;
        this.rating += 1.25;
        if (this.getFootballer().getPosition().getAttackingDuty() < 5) {
            this.rating += 0.25;
            if (this.getFootballer().getPosition().getAttackingDuty() < 3) {
                this.rating += 0.5;
            }
        }
    }

    int getAssists() {
        return assists;
    }

    void addAssists() {
        this.assists += 1;
        this.rating += 1;
        if (this.getFootballer().getPosition().getAttackingDuty() < 5) {
            this.rating += 0.25;
            if (this.getFootballer().getPosition().getAttackingDuty() < 3) {
                this.rating += 0.5;
            }
        }
    }

    boolean isYellowCarded() {
        return yellowCarded;
    }

    void addYellowCard() {
        this.yellowCarded = true;
        this.rating -= 0.5;
    }

    boolean isRedCarded() {
        return redCarded;
    }

    void addRedCard() {
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
