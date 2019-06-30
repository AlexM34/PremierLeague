import java.util.Objects;

class MatchStats {
    private final Footballer footballer;
    private float rating;
    private int goals;
    private int assists;
    private boolean yellowCarded;
    private boolean redCarded;

    MatchStats(final Footballer footballer) {
        this.footballer = footballer;
        this.rating = 6;
        this.goals = 0;
        this.assists = 0;
        this.yellowCarded = false;
        this.redCarded = false;
    }

    Footballer getFootballer() {
        return footballer;
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
        final MatchStats matchStats = (MatchStats) o;
        return Float.compare(matchStats.rating, rating) == 0 &&
                goals == matchStats.goals &&
                assists == matchStats.assists &&
                yellowCarded == matchStats.yellowCarded &&
                redCarded == matchStats.redCarded &&
                footballer.equals(matchStats.footballer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(footballer, rating, goals, assists, yellowCarded, redCarded);
    }

    @Override
    public String toString() {
        return "MatchStats{" +
                "footballer=" + footballer +
                ", rating=" + rating +
                ", goals=" + goals +
                ", assists=" + assists +
                ", yellowCarded=" + yellowCarded +
                ", redCarded=" + redCarded +
                '}';
    }
}
