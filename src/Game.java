import java.util.Objects;

public class Game {
    private Footballer footballer;
    private float rating;
    private int goals;
    private int assists;
    private boolean yellowCarded;
    private boolean redCarded;

    Game(final Footballer footballer) {
        this.footballer = footballer;
        this.rating = 6;
        this.goals = 0;
        this.assists = 0;
        this.yellowCarded = false;
        this.redCarded = false;
    }

    public Footballer getFootballer() {
        return footballer;
    }

    public float getRating() {
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
    }

    int getAssists() {
        return assists;
    }

    void addAssists() {
        this.assists += 1;
    }

    boolean isYellowCarded() {
        return yellowCarded;
    }

    void addYellowCard() {
        this.yellowCarded = true;
    }

    boolean isRedCarded() {
        return redCarded;
    }

    void addtRedCard() {
        this.redCarded = true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Game game = (Game) o;
        return Float.compare(game.rating, rating) == 0 &&
                goals == game.goals &&
                assists == game.assists &&
                yellowCarded == game.yellowCarded &&
                redCarded == game.redCarded &&
                footballer.equals(game.footballer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(footballer, rating, goals, assists, yellowCarded, redCarded);
    }

    @Override
    public String toString() {
        return "Game{" +
                "footballer=" + footballer +
                ", rating=" + rating +
                ", goals=" + goals +
                ", assists=" + assists +
                ", yellowCarded=" + yellowCarded +
                ", redCarded=" + redCarded +
                '}';
    }
}
