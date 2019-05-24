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

    public League(int matches, int points, int wins, int draws, int losses, int scored, int conceded, int cleanSheets) {
        this.matches = matches;
        this.points = points;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.scored = scored;
        this.conceded = conceded;
        this.cleanSheets = cleanSheets;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getScored() {
        return scored;
    }

    public void setScored(int scored) {
        this.scored = scored;
    }

    public int getConceded() {
        return conceded;
    }

    public void setConceded(int conceded) {
        this.conceded = conceded;
    }

    public int getCleanSheets() {
        return cleanSheets;
    }

    public void setCleanSheets(int cleanSheets) {
        this.cleanSheets = cleanSheets;
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
