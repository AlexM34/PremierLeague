package teams;

import competitions.ChampionsLeague;
import competitions.Cup;

import java.util.Objects;

public class Season {
    private League league;
    private Cup nationalCup;
    private Cup leagueCup;
    private ChampionsLeague championsLeague;
    private int form;
    private int morale;

    public Season(final League league, final Cup nationalCup, final Cup leagueCup, final ChampionsLeague championsLeague, final int form, final int morale) {
        this.league = league;
        this.nationalCup = nationalCup;
        this.leagueCup = leagueCup;
        this.championsLeague = championsLeague;
        this.form = form;
        this.morale = morale;
    }

    public League getLeague() {
        return league;
    }

    void setLeague(final League league) {
        this.league = league;
    }

    Cup getNationalCup() {
        return nationalCup;
    }

    void setNationalCup(final Cup nationalCup) {
        this.nationalCup = nationalCup;
    }

    Cup getLeagueCup() {
        return leagueCup;
    }

    void setLeagueCup(final Cup leagueCup) {
        this.leagueCup = leagueCup;
    }

    public ChampionsLeague getChampionsLeague() {
        return championsLeague;
    }

    void setChampionsLeague(final ChampionsLeague championsLeague) {
        this.championsLeague = championsLeague;
    }

    public int getForm() {
        return form;
    }

    void setForm(final int form) {
        this.form = form;
    }

    public void changeForm(final int change) {
        this.form += change;
        if (this.form > 20) this.form = 20;
        else if (this.form < 0) this.form = 0;
    }

    int getMorale() {
        return morale;
    }

    void setMorale(final int morale) {
        this.morale = morale;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Season season = (Season) o;
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
        return "teams.Season{" +
                "league=" + league +
                ", nationalCup=" + nationalCup +
                ", leagueCup=" + leagueCup +
                ", championsLeague=" + championsLeague +
                ", form=" + form +
                ", morale=" + morale +
                '}';
    }
}
