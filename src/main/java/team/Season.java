package team;

import simulation.competition.Continental;

import java.util.Objects;

public class Season {

    private final League league;
    private final Cup nationalCup;
    private final Cup leagueCup;
    private final Continental continental;
    private int form;
    private final int morale;

    public Season(final String leagueName) {
        this.league = new League(leagueName);
        this.nationalCup = new Cup();
        this.leagueCup = new Cup();
        this.continental = new Continental();
        this.form = 10;
        this.morale = 50;
    }

    public League getLeague() {
        return league;
    }

    public Cup getNationalCup() {
        return nationalCup;
    }

    public Cup getLeagueCup() {
        return leagueCup;
    }

    public Continental getContinental() {
        return continental;
    }

    public int getForm() {
        return form;
    }

    public void changeForm(final int change) {
        this.form = Math.min(20, Math.max(1, this.form + change));
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Season)) return false;
        final Season season = (Season) o;
        return form == season.form &&
                morale == season.morale &&
                Objects.equals(league, season.league) &&
                Objects.equals(nationalCup, season.nationalCup) &&
                Objects.equals(leagueCup, season.leagueCup) &&
                Objects.equals(continental, season.continental);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(league, nationalCup, leagueCup, continental, form, morale);
    }

    @Override
    public String toString() {
        return "Season{" +
                "main.league=" + league +
                ", nationalCup=" + nationalCup +
                ", leagueCup=" + leagueCup +
                ", continental=" + continental +
                ", form=" + form +
                ", morale=" + morale +
                '}';
    }

}
