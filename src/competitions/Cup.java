package competitions;

import java.util.Objects;

public class Cup {
    private boolean alive;

    public Cup() {
        this.alive = false;
    }

    boolean isAlive() {
        return alive;
    }

    void setAlive(final boolean alive) {
        this.alive = alive;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Cup cup = (Cup) o;
        return alive == cup.alive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alive);
    }

    @Override
    public String toString() {
        return "Cup{" +
                "alive=" + alive +
                '}';
    }
}
