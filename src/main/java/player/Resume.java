package player;

import java.util.Objects;

public class Resume {

    private final Glory glory;
    private final Statistics total;
    private final Statistics season;

    public Resume() {
        this.glory = new Glory(0, 0, 0, 0, 0, 0, 0, 0);
        this.total = new Statistics();
        this.season = new Statistics();
    }

    public Glory getGlory() {
        return glory;
    }

    public Statistics getTotal() {
        return total;
    }

    public Statistics getSeason() {
        return season;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Resume)) return false;
        final Resume resume = (Resume) o;
        return Objects.equals(glory, resume.glory) &&
                Objects.equals(total, resume.total) &&
                Objects.equals(season, resume.season);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(glory, total, season);
    }

    @Override
    public String toString() {
        return "Resume{" +
                "glory=" + glory +
                ", total=" + total +
                ", season=" + season +
                '}';
    }
}
