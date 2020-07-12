package simulation.match;

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

    public int[] getScore() {
        return score;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Fixture)) return false;
        final Fixture fixture = (Fixture) o;
        return isHome == fixture.isHome &&
                Objects.equals(opponent, fixture.opponent) &&
                Arrays.equals(score, fixture.score);
    }

    @Override
    public final int hashCode() {
        int result = Objects.hash(opponent, isHome);
        result = 31 * result + Arrays.hashCode(score);
        return result;
    }

    @Override
    public String toString() {
        return (score[0] > score[1] ? "Win" : score[0] < score[1] ? "Loss" : "Draw") +
                ": " + (isHome ? "H" : "A") +
                " vs " + opponent.getName() +
                " " + (isHome ? score[0] : score[1]) +
                ":" + (isHome ? score[1] : score[0]);
    }
}
