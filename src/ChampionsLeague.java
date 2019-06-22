import java.util.Objects;

class ChampionsLeague {
    private boolean alive;
    private final League group;

    ChampionsLeague() {
        this.alive = false;
        this.group = new League();
    }

    boolean isAlive() {
        return alive;
    }

    void setAlive(final boolean alive) {
        this.alive = alive;
    }

    League getGroup() {
        return group;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ChampionsLeague that = (ChampionsLeague) o;
        return alive == that.alive &&
                group.equals(that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alive, group);
    }

    @Override
    public String toString() {
        return "ChampionsLeague{" +
                "alive=" + alive +
                ", group=" + group +
                '}';
    }
}
