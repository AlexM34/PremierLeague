import java.util.Objects;

class Glory {
    private int continental;
    private int league;
    private int nationalCup;
    private int leagueCup;
    private int nationalSuperCup;
    private int continentalSuperCup;
    private int worldChampionship;

    Glory(int continental, int league, int nationalCup, int leagueCup,
                 int nationalSuperCup, int continentalSuperCup, int worldChampionship) {
        this.continental = continental;
        this.league = league;
        this.nationalCup = nationalCup;
        this.leagueCup = leagueCup;
        this.nationalSuperCup = nationalSuperCup;
        this.continentalSuperCup = continentalSuperCup;
        this.worldChampionship = worldChampionship;
    }

    int getContinental() {
        return continental;
    }

    void setContinental(int continental) {
        this.continental = continental;
    }

    int getLeague() {
        return league;
    }

    void setLeague(int league) {
        this.league = league;
    }

    void addLeague(int league) {
        this.league += league;
    }

    int getNationalCup() {
        return nationalCup;
    }

    void setNationalCup(int nationalCup) {
        this.nationalCup = nationalCup;
    }

    int getLeagueCup() {
        return leagueCup;
    }

    void setLeagueCup(int leagueCup) {
        this.leagueCup = leagueCup;
    }

    int getNationalSuperCup() {
        return nationalSuperCup;
    }

    void setNationalSuperCup(int nationalSuperCup) {
        this.nationalSuperCup = nationalSuperCup;
    }

    int getContinentalSuperCup() {
        return continentalSuperCup;
    }

    void setContinentalSuperCup(int continentalSuperCup) {
        this.continentalSuperCup = continentalSuperCup;
    }

    int getWorldChampionship() {
        return worldChampionship;
    }

    void setWorldChampionship(int worldChampionship) {
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
