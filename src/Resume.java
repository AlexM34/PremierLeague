import java.util.Objects;

class Resume {
    private Glory glory;
    private Statistics total;
    private Statistics season;

    Resume() {
        this.glory = new Glory(0, 0, 0, 0, 0, 0, 0);
        this.total = new Statistics();
        this.season = new Statistics();
    }

    Glory getGlory() {
        return glory;
    }

    Statistics getTotal() {
        return total;
    }

    Statistics getSeason() {
        return season;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return glory.equals(resume.glory) &&
                total.equals(resume.total) &&
                season.equals(resume.season);
    }

    @Override
    public int hashCode() {
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
