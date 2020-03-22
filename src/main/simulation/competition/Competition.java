package simulation.competition;

public enum Competition {
    LEAGUE(0), NATIONAL_CUP(1), LEAGUE_CUP(2), CHAMPIONS_LEAGUE(3), EUROPA_LEAGUE(4);

    Competition(final int type) {
        this.type = type;
    }

    private final int type;

    public int getType() {
        return type;
    }
}
