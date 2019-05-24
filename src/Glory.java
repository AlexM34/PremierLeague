import java.util.Objects;

class Glory {
    private int continental;
    private int league;
    private int nationalCup;
    private int leagueCup;
    private int nationalSuperCup;
    private int continentalSuperCup;
    private int worldChampionship;

    public Glory(int continental, int league, int nationalCup, int leagueCup,
                 int nationalSuperCup, int continentalSuperCup, int worldChampionship) {
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

    public void setContinental(int continental) {
        this.continental = continental;
    }

    public int getLeague() {
        return league;
    }

    public void setLeague(int league) {
        this.league = league;
    }

    public void addLeague(int league) {
        this.league += league;
    }

    public int getNationalCup() {
        return nationalCup;
    }

    public void setNationalCup(int nationalCup) {
        this.nationalCup = nationalCup;
    }

    public int getLeagueCup() {
        return leagueCup;
    }

    public void setLeagueCup(int leagueCup) {
        this.leagueCup = leagueCup;
    }

    public int getNationalSuperCup() {
        return nationalSuperCup;
    }

    public void setNationalSuperCup(int nationalSuperCup) {
        this.nationalSuperCup = nationalSuperCup;
    }

    public int getContinentalSuperCup() {
        return continentalSuperCup;
    }

    public void setContinentalSuperCup(int continentalSuperCup) {
        this.continentalSuperCup = continentalSuperCup;
    }

    public int getWorldChampionship() {
        return worldChampionship;
    }

    public void setWorldChampionship(int worldChampionship) {
        this.worldChampionship = worldChampionship;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Glory glory = (Glory) o;
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
        return "Glory{" +
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
