package simulation;

import team.Club;

import java.util.Arrays;
import java.util.Objects;

public class Fixture {
    private final Club opponent;
    private final boolean isHome;
    private final int[] score;

    public Fixture(final Club opponent, final boolean isHome, final int[] score) {
        this.opponent = opponent;
        this.isHome = isHome;
        this.score = score;
    }

    public Club getOpponent() {
        return opponent;
    }

    boolean isHome() {
        return isHome;
    }

    public int[] getScore() {
        return score;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Fixture fixture = (Fixture) o;
        return isHome == fixture.isHome &&
                opponent.equals(fixture.opponent) &&
                Arrays.equals(score, fixture.score);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(opponent, isHome);
        result = 31 * result + Arrays.hashCode(score);
        return result;
    }

    @Override
    public String toString() {
        return "Fixture{" +
                "opponent=" + opponent +
                ", isHome=" + isHome +
                ", score=" + Arrays.toString(score) +
                '}';
    }
}
