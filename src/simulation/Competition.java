package simulation;

public enum Competition {
    LEAGUE(0), NATIONAL_CUP(1), LEAGUE_CUP(2), CHAMPIONS_LEAGUE(3), EUROPA_LEAGUE(4);

    Competition(final int type) {
        this.type = type;
    }

    private int type;

    int getType() {
        return type;
    }
}
