package team;

import competition.Fixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class League {
    private List<Fixture> fixtures;
    private int matches;
    private int points;
    private int wins;
    private int draws;
    private int losses;
    private int scored;
    private int conceded;
    private int cleanSheets;

    public League() {
        this.fixtures = new ArrayList<>();
        this.matches = 0;
        this.points = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.scored = 0;
        this.conceded = 0;
        this.cleanSheets = 0;
    }

    public List<Fixture> getFixtures() {
        return fixtures;
    }

    public void addFixture(final Club opponent, final int scored, final int conceded) {
        fixtures.add(new Fixture(opponent, new int[]{scored, conceded}));
    }

    public int getMatches() {
        return matches;
    }

    public void addMatch() {
        this.matches += 1;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(final int points) {
        this.points += points;
    }

    public int getWins() {
        return wins;
    }

    public void addWin() {
        this.wins += 1;
    }

    public int getDraws() {
        return draws;
    }

    public void addDraw() {
        this.draws += 1;
    }

    public int getLosses() {
        return losses;
    }

    public void addLoss() {
        this.losses += 1;
    }

    public int getScored() {
        return scored;
    }

    public void addScored(final int goals) {
        this.scored += goals;
    }

    public int getConceded() {
        return conceded;
    }

    public void addConceded(final int goals) {
        this.conceded += goals;
    }

    public void addCleanSheet() {
        this.cleanSheets += 1;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final League league = (League) o;
        return matches == league.matches &&
                points == league.points &&
                wins == league.wins &&
                draws == league.draws &&
                losses == league.losses &&
                scored == league.scored &&
                conceded == league.conceded &&
                cleanSheets == league.cleanSheets &&
                fixtures.equals(league.fixtures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fixtures, matches, points, wins, draws, losses, scored, conceded, cleanSheets);
    }

    @Override
    public String toString() {
        return "League{" +
                "fixtures=" + fixtures +
                ", matches=" + matches +
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
