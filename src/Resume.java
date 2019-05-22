import java.util.Objects;

class Resume {
    private Glory glory;
    private Statistics total;
    private Statistics season;

    public Resume(Glory glory, Statistics total, Statistics season) {
        this.glory = glory;
        this.total = total;
        this.season = season;
    }

    public Glory getGlory() {
        return glory;
    }

    public void setGlory(Glory glory) {
        this.glory = glory;
    }

    public Statistics getTotal() {
        return total;
    }

    public void setTotal(Statistics total) {
        this.total = total;
    }

    public Statistics getSeason() {
        return season;
    }

    public void setSeason(Statistics season) {
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
