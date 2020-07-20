package player;

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
        clear();
    }

    void clear() {
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

    public int getGoals() {
        return goals;
    }

    public void addGoals(final int goals) {
        this.goals += goals;
    }

    public int getAssists() {
        return assists;
    }

    public void addAssists(final int assists) {
        this.assists += assists;
    }

    public int getCleanSheets() {
        return cleanSheets;
    }

    public void addCleanSheets(final int cleanSheets) {
        this.cleanSheets += cleanSheets;
    }

    public int getRating() {
        return rating;
    }

    public void addRating(final int rating, final int matches) {
        if (matches > 0) {
            this.rating = (this.rating * this.matches + rating * matches) / (this.matches + matches);
            this.matches += matches;
        }
    }

    public int getMotmAwards() {
        return motmAwards;
    }

    public void addMotmAwards(final int motmAwards) {
        this.motmAwards += motmAwards;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public void addYellowCards(final int yellowCards) {
        this.yellowCards += yellowCards;
    }

    public int getRedCards() {
        return redCards;
    }

    public void addRedCards(final int redCards) {
        this.redCards += redCards;
    }

    void update(final Competition competition) {
        addGoals(competition.getGoals());
        addAssists(competition.getAssists());
        addCleanSheets(competition.getCleanSheets());
        addRating(competition.getRating(), competition.getMatches());
        addMotmAwards(competition.getMotmAwards());
        addYellowCards(competition.getYellowCards());
        addRedCards(competition.getRedCards());
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Competition)) return false;
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
    public final int hashCode() {
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
