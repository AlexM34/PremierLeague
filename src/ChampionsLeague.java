import java.util.Objects;

class ChampionsLeague {
    private boolean alive;
    private League group;

    ChampionsLeague() {
        this.alive = false;
        this.group = new League();
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public League getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChampionsLeague that = (ChampionsLeague) o;
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
