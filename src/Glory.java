import java.util.Objects;

class Glory {
    // TODO: Player stats
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

    void addContinental() {
        this.continental += 1;
    }

    int getLeague() {
        return league;
    }

    void addLeague() {
        this.league += 1;
    }

    int getNationalCup() {
        return nationalCup;
    }

    void addNationalCup() {
        this.nationalCup += 1;
    }

    int getLeagueCup() {
        return leagueCup;
    }

    void addLeagueCup() {
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
