import java.util.Objects;

class Statistics {
    private int matches;
    private int goals;
    private int assists;
    private int cleanSheets;
    private int rating;
    private int motmAwards;
    private int yellowCards;
    private int redCards;

    Statistics(int matches, int goals, int assists, int cleanSheets, int rating, int motmAwards, int yellowCards, int redCards) {
        this.matches = matches;
        this.goals = goals;
        this.assists = assists;
        this.cleanSheets = cleanSheets;
        this.rating = rating;
        this.motmAwards = motmAwards;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
    }

    int getMatches() {
        return matches;
    }

    void setMatches(int matches) {
        this.matches = matches;
    }

    void addMatches(int matches) {
        this.matches += matches;
    }

    int getGoals() {
        return goals;
    }

    void setGoals(int goals) {
        this.goals = goals;
    }

    void addGoals(int goals) {
        this.goals += goals;
    }

    int getAssists() {
        return assists;
    }

    void setAssists(int assists) {
        this.assists = assists;
    }

    void addAssists(int assists) {
        this.assists += assists;
    }

    int getCleanSheets() {
        return cleanSheets;
    }

    void setCleanSheets(int cleanSheets) {
        this.cleanSheets = cleanSheets;
    }

    void addCleanSheets(int cleanSheets) {
        this.cleanSheets += cleanSheets;
    }

    int getRating() {
        return rating;
    }

    void setRating(int rating) {
        this.rating = rating;
    }

    void addRating(int rating, int matches) {
        if (matches > 0) {
            this.rating = (this.rating * this.matches + rating * matches) / (this.matches + matches);
        }
    }

    int getMotmAwards() {
        return motmAwards;
    }

    void setMotmAwards(int motmAwards) {
        this.motmAwards = motmAwards;
    }

    void addMotmAwards(int motmAwards) {
        this.motmAwards += motmAwards;
    }

    int getYellowCards() {
        return yellowCards;
    }

    void setYellowCards(int yellowCards) {
        this.yellowCards = yellowCards;
    }

    void addYellowCards(int yellows) {
        this.yellowCards += yellows;
    }

    int getRedCards() {
        return redCards;
    }

    void setRedCards(int redCards) {
        this.redCards = redCards;
    }

    void addRedCards(int reds) {
        this.redCards += reds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistics statistics = (Statistics) o;
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
