import java.util.Objects;

class Season {
    private League league;
    private Cup nationalCup;
    private Cup leagueCup;
    private ChampionsLeague championsLeague;
    private int form;
    private int morale;

    Season(League league, Cup nationalCup, Cup leagueCup, ChampionsLeague championsLeague, int form, int morale) {
        this.league = league;
        this.nationalCup = nationalCup;
        this.leagueCup = leagueCup;
        this.championsLeague = championsLeague;
        this.form = form;
        this.morale = morale;
    }

    League getLeague() {
        return league;
    }

    void setLeague(League league) {
        this.league = league;
    }

    Cup getNationalCup() {
        return nationalCup;
    }

    void setNationalCup(Cup nationalCup) {
        this.nationalCup = nationalCup;
    }

    Cup getLeagueCup() {
        return leagueCup;
    }

    void setLeagueCup(Cup leagueCup) {
        this.leagueCup = leagueCup;
    }

    ChampionsLeague getChampionsLeague() {
        return championsLeague;
    }

    void setChampionsLeague(ChampionsLeague championsLeague) {
        this.championsLeague = championsLeague;
    }

    int getForm() {
        return form;
    }

    void setForm(int form) {
        this.form = form;
    }

    void changeForm(int change) {
        this.form += change;
        if (this.form > 20) this.form = 20;
        else if (this.form < 0) this.form = 0;
    }

    int getMorale() {
        return morale;
    }

    void setMorale(int morale) {
        this.morale = morale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Season season = (Season) o;
        return form == season.form &&
                morale == season.morale &&
                league.equals(season.league) &&
                Objects.equals(nationalCup, season.nationalCup) &&
                Objects.equals(leagueCup, season.leagueCup) &&
                Objects.equals(championsLeague, season.championsLeague);
    }

    @Override
    public int hashCode() {
        return Objects.hash(league, nationalCup, leagueCup, championsLeague, form, morale);
    }

    @Override
    public String toString() {
        return "Season{" +
                "league=" + league +
                ", nationalCup=" + nationalCup +
                ", leagueCup=" + leagueCup +
                ", championsLeague=" + championsLeague +
                ", form=" + form +
                ", morale=" + morale +
                '}';
    }
}
