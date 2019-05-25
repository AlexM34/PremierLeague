import java.util.Objects;

class Resume {
    private Glory glory;
    private Statistics total;
    private Statistics season;

    Resume(Glory glory, Statistics total, Statistics season) {
        this.glory = glory;
        this.total = total;
        this.season = season;
    }

    Glory getGlory() {
        return glory;
    }

    void setGlory(Glory glory) {
        this.glory = glory;
    }

    Statistics getTotal() {
        return total;
    }

    void setTotal(Statistics total) {
        this.total = total;
    }

    Statistics getSeason() {
        return season;
    }

    void setSeason(Statistics season) {
        this.season = season;
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
