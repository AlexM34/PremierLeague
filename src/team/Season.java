package team;

import competition.Continental;
import competition.Cup;

import java.util.Objects;

public class Season {
    private League league;
    private Cup nationalCup;
    private Cup leagueCup;
    private Continental continental;
    private int form;
    private int morale;

    public Season(final League league, final Cup nationalCup, final Cup leagueCup, final Continental continental, final int form, final int morale) {
        this.league = league;
        this.nationalCup = nationalCup;
        this.leagueCup = leagueCup;
        this.continental = continental;
        this.form = form;
        this.morale = morale;
    }

    public League getLeague() {
        return league;
    }

    void setLeague(final League league) {
        this.league = league;
    }

    public Cup getNationalCup() {
        return nationalCup;
    }

    void setNationalCup(final Cup nationalCup) {
        this.nationalCup = nationalCup;
    }

    public Cup getLeagueCup() {
        return leagueCup;
    }

    void setLeagueCup(final Cup leagueCup) {
        this.leagueCup = leagueCup;
    }

    public Continental getContinental() {
        return continental;
    }

    void setContinental(final Continental continental) {
        this.continental = continental;
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
                Objects.equals(continental, season.continental);
    }

    @Override
    public int hashCode() {
        return Objects.hash(league, nationalCup, leagueCup, continental, form, morale);
    }

    @Override
    public String toString() {
        return "Season{" +
                "league=" + league +
                ", nationalCup=" + nationalCup +
                ", leagueCup=" + leagueCup +
                ", championsLeague=" + continental +
                ", form=" + form +
                ", morale=" + morale +
                '}';
    }
}
