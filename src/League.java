import java.util.Objects;

class League {
    private int matches;
    private int points;
    private int wins;
    private int draws;
    private int losses;
    private int scored;
    private int conceded;
    private int cleanSheets;

    League(int matches, int points, int wins, int draws, int losses, int scored, int conceded, int cleanSheets) {
        this.matches = matches;
        this.points = points;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.scored = scored;
        this.conceded = conceded;
        this.cleanSheets = cleanSheets;
    }

    int getMatches() {
        return matches;
    }

    void addMatch() {
        this.matches += 1;
    }

    int getPoints() {
        return points;
    }

    void addPoints(int points) {
        this.points += points;
    }

    int getWins() {
        return wins;
    }

    void addWin() {
        this.wins += 1;
    }

    int getDraws() {
        return draws;
    }

    void addDraw() {
        this.draws += 1;
    }

    int getLosses() {
        return losses;
    }

    void addLoss() {
        this.losses += 1;
    }

    int getScored() {
        return scored;
    }

    void addScored(int goals) {
        this.scored += goals;
    }

    int getConceded() {
        return conceded;
    }

    void addConceded(int goals) {
        this.conceded += goals;
    }

    int getCleanSheets() {
        return cleanSheets;
    }

    void addCleanSheet() {
        this.cleanSheets += 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        League league = (League) o;
        return matches == league.matches &&
                points == league.points &&
                wins == league.wins &&
                draws == league.draws &&
                losses == league.losses &&
                scored == league.scored &&
                conceded == league.conceded &&
                cleanSheets == league.cleanSheets;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matches, points, wins, draws, losses, scored, conceded, cleanSheets);
    }

    @Override
    public String toString() {
        return "League{" +
                "matches=" + matches +
                ", points=" + points +
                ", wins=" + wins +
                ", draws=" + draws +
                ", losses=" + losses +
                ", scored=" + scored +
                ", conceded=" + conceded +
                ", cleanSheets=" + cleanSheets +
                '}';
    }
}
