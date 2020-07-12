package team;

import simulation.match.Fixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cup {

    private final List<Fixture> fixtures;
    private String stage;

    public Cup() {
        this.fixtures = new ArrayList<>();
        this.stage = "N/A";
    }

    public List<Fixture> getFixtures() {
        return fixtures;
    }

    public void setStage(final String stage) {
        this.stage = stage;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Cup)) return false;
        final Cup cup = (Cup) o;
        return Objects.equals(fixtures, cup.fixtures) &&
                Objects.equals(stage, cup.stage);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(fixtures, stage);
    }

    @Override
    public String toString() {
        return "Cup{" +
                "rounds=" + fixtures +
                ", stage='" + stage + '\'' +
                '}';
    }
}
