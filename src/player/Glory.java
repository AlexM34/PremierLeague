package player;

import java.util.Objects;

public class Glory {
    private int league;
    private int nationalCup;
    private int leagueCup;
    private int championsLeague;
    private int europaLeague;
    private final int nationalSuperCup;
    private final int continentalSuperCup;
    private final int worldChampionship;

    public Glory(final int championsLeague, final int europaLeague, final int league, final int nationalCup, final int leagueCup,
                 final int nationalSuperCup, final int continentalSuperCup, final int worldChampionship) {
        this.league = league;
        this.nationalCup = nationalCup;
        this.leagueCup = leagueCup;
        this.championsLeague = championsLeague;
        this.europaLeague = europaLeague;
        this.nationalSuperCup = nationalSuperCup;
        this.continentalSuperCup = continentalSuperCup;
        this.worldChampionship = worldChampionship;
    }

    public int getLeague() {
        return league;
    }

    public void addLeague() {
        this.league++;
    }

    public int getNationalCup() {
        return nationalCup;
    }

    public void addNationalCup() {
        this.nationalCup++;
    }

    public int getLeagueCup() {
        return leagueCup;
    }

    public void addLeagueCup() {
        this.leagueCup++;
    }

    public int getChampionsLeague() {
        return championsLeague;
    }

    public void addChampionsLeague() {
        this.championsLeague++;
    }

    public int getEuropaLeague() {
        return europaLeague;
    }

    public void addEuropaLeague() {
        this.europaLeague++;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Glory glory = (Glory) o;
        return league == glory.league &&
                nationalCup == glory.nationalCup &&
                leagueCup == glory.leagueCup &&
                championsLeague == glory.championsLeague &&
                europaLeague == glory.europaLeague &&
                nationalSuperCup == glory.nationalSuperCup &&
                continentalSuperCup == glory.continentalSuperCup &&
                worldChampionship == glory.worldChampionship;
    }

    @Override
    public int hashCode() {
        return Objects.hash(league, nationalCup, leagueCup, championsLeague, europaLeague, nationalSuperCup, continentalSuperCup, worldChampionship);
    }

    @Override
    public String toString() {
        return "Glory{" +
                "league=" + league +
                ", nationalCup=" + nationalCup +
                ", leagueCup=" + leagueCup +
                ", championsLeague=" + championsLeague +
                ", europaLeague=" + europaLeague +
                ", nationalSuperCup=" + nationalSuperCup +
                ", continentalSuperCup=" + continentalSuperCup +
                ", worldChampionship=" + worldChampionship +
                '}';
    }
}
