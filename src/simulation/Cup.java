package simulation;

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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Cup cup = (Cup) o;
        return fixtures.equals(cup.fixtures) &&
                stage.equals(cup.stage);
    }

    @Override
    public int hashCode() {
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
