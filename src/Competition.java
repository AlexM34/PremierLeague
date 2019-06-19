import java.util.Objects;

class Competition {
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

    int getMatches() {
        return matches;
    }

    void clearMatches() {
        this.matches = 0;
    }

    void addMatches(int matches) {
        this.matches += matches;
    }

    int getGoals() {
        return goals;
    }

    void clearGoals() {
        this.goals = 0;
    }

    // TODO: Remove parameter for goals and assists
    void addGoals(int goals) {
        this.goals += goals;
    }

    int getAssists() {
        return assists;
    }

    void clearAssists() {
        this.assists = 0;
    }

    void addAssists(int assists) {
        this.assists += assists;
    }

    int getCleanSheets() {
        return cleanSheets;
    }

    void clearCleanSheets() {
        this.cleanSheets = 0;
    }

    void addCleanSheets(int cleanSheets) {
        this.cleanSheets += cleanSheets;
    }

    int getRating() {
        return rating;
    }

    void clearRating() {
        this.rating = 0;
    }

    void addRating(int rating, int matches) {
        if (matches > 0) {
            this.rating = (this.rating * this.matches + rating * matches) / (this.matches + matches);
        }
    }

    int getMotmAwards() {
        return motmAwards;
    }

    void clearMotmAwards() {
        this.motmAwards = 0;
    }

    void addMotmAwards(int motmAwards) {
        this.motmAwards += motmAwards;
    }

    int getYellowCards() {
        return yellowCards;
    }

    void clearYellowCards() {
        this.yellowCards = 0;
    }

    void addYellowCards(int yellows) {
        this.yellowCards += yellows;
    }

    int getRedCards() {
        return redCards;
    }

    void clearRedCards() {
        this.redCards = 0;
    }

    void addRedCards(int reds) {
        this.redCards += reds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Competition statistics = (Competition) o;
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
