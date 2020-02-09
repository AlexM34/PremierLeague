package simulation;

import team.Club;

import java.util.Arrays;
import java.util.Objects;

public class Fixture {
    private final Club opponent;
    private final int[] score;

    public Fixture(final Club opponent, final int[] score) {
        this.opponent = opponent;
        this.score = score;
    }

    public Club getOpponent() {
        return opponent;
    }

    public int[] getScore() {
        return score;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Fixture fixture = (Fixture) o;
        return opponent.equals(fixture.opponent) &&
                Arrays.equals(score, fixture.score);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(opponent);
        result = 31 * result + Arrays.hashCode(score);
        return result;
    }

    @Override
    public String toString() {
        return "Fixture{" +
                "opponent=" + opponent +
                ", score=" + Arrays.toString(score) +
                '}';
    }
}
