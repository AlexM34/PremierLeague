package players;

import java.util.Objects;

public class Glory {
    private int continental;
    private int league;
    private int nationalCup;
    private int leagueCup;
    private int nationalSuperCup;
    private int continentalSuperCup;
    private int worldChampionship;

    public Glory(final int continental, final int league, final int nationalCup, final int leagueCup,
                 final int nationalSuperCup, final int continentalSuperCup, final int worldChampionship) {
        this.continental = continental;
        this.league = league;
        this.nationalCup = nationalCup;
        this.leagueCup = leagueCup;
        this.nationalSuperCup = nationalSuperCup;
        this.continentalSuperCup = continentalSuperCup;
        this.worldChampionship = worldChampionship;
    }

    public int getContinental() {
        return continental;
    }

    public void addContinental() {
        this.continental += 1;
    }

    public int getLeague() {
        return league;
    }

    public void addLeague() {
        this.league += 1;
    }

    public int getNationalCup() {
        return nationalCup;
    }

    public void addNationalCup() {
        this.nationalCup += 1;
    }

    public int getLeagueCup() {
        return leagueCup;
    }

    public void addLeagueCup() {
        this.leagueCup += 1;
    }

    int getNationalSuperCup() {
        return nationalSuperCup;
    }

    void addNationalSupCup() {
        this.nationalSuperCup += 1;
    }

    int getContinentalSuperCup() {
        return continentalSuperCup;
    }

    void addContinentalSuperCup() {
        this.continentalSuperCup += 1;
    }

    int getWorldChampionship() {
        return worldChampionship;
    }

    void addWorldChampionship() {
        this.worldChampionship += 1;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Glory glory = (Glory) o;
        return continental == glory.continental &&
                league == glory.league &&
                nationalCup == glory.nationalCup &&
                leagueCup == glory.leagueCup &&
                nationalSuperCup == glory.nationalSuperCup &&
                continentalSuperCup == glory.continentalSuperCup &&
                worldChampionship == glory.worldChampionship;
    }

    @Override
    public int hashCode() {
        return Objects.hash(continental, league, nationalCup, leagueCup, nationalSuperCup, continentalSuperCup, worldChampionship);
    }

    @Override
    public String toString() {
        return "players.Glory{" +
                "continental=" + continental +
                ", league=" + league +
                ", nationalCup=" + nationalCup +
                ", leagueCup=" + leagueCup +
                ", nationalSuperCup=" + nationalSuperCup +
                ", continentalSuperCup=" + continentalSuperCup +
                ", worldChampionship=" + worldChampionship +
                '}';
    }
}
