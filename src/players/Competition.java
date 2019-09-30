package players;

import java.util.Objects;

public class Competition {
    private int matches;
    private int goals;
    private int assists;
    private int cleanSheets;
    private int rating;
    private int motmAwards;
    private int yellowCards;
    private int redCards;

    Competition() {
        this.matches = 0;
        this.goals = 0;
        this.assists = 0;
        this.cleanSheets = 0;
        this.rating = 0;
        this.motmAwards = 0;
        this.yellowCards = 0;
        this.redCards = 0;
    }

    public int getMatches() {
        return matches;
    }

    public void clearMatches() {
        this.matches = 0;
    }

    public void addMatches(final int matches) {
        this.matches += matches;
    }

    public int getGoals() {
        return goals;
    }

    public void clearGoals() {
        this.goals = 0;
    }

    public void addGoals(final int goals) {
        this.goals += goals;
    }

    public int getAssists() {
        return assists;
    }

    public void clearAssists() {
        this.assists = 0;
    }

    public void addAssists(final int assists) {
        this.assists += assists;
    }

    public int getCleanSheets() {
        return cleanSheets;
    }

    public void clearCleanSheets() {
        this.cleanSheets = 0;
    }

    public void addCleanSheets(final int cleanSheets) {
        this.cleanSheets += cleanSheets;
    }

    public int getRating() {
        return rating;
    }

    public void clearRating() {
        this.rating = 0;
    }

    public void addRating(final int rating, final int matches) {
        if (matches > 0) {
            this.rating = (this.rating * this.matches + rating * matches) / (this.matches + matches);
        }
    }

    public int getMotmAwards() {
        return motmAwards;
    }

    public void clearMotmAwards() {
        this.motmAwards = 0;
    }

    public void addMotmAwards(final int motmAwards) {
        this.motmAwards += motmAwards;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public void clearYellowCards() {
        this.yellowCards = 0;
    }

    public void addYellowCards() {
        this.yellowCards += 1;
    }

    public int getRedCards() {
        return redCards;
    }

    public void clearRedCards() {
        this.redCards = 0;
    }

    public void addRedCards() {
        this.redCards += 1;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Competition statistics = (Competition) o;
        return matches == statistics.matches &&
                goals == statistics.goals &&
                assists == statistics.assists &&
                cleanSheets == statistics.cleanSheets &&
                rating == statistics.rating &&
                motmAwards == statistics.motmAwards &&
                yellowCards == statistics.yellowCards &&
                redCards == statistics.redCards;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matches, goals, assists, cleanSheets, rating, motmAwards, yellowCards, redCards);
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "matches=" + matches +
                ", goals=" + goals +
                ", assists=" + assists +
                ", cleanSheets=" + cleanSheets +
                ", rating=" + rating +
                ", motmAwards=" + motmAwards +
                ", yellowCards=" + yellowCards +
                ", redCards=" + redCards +
                '}';
    }
}
