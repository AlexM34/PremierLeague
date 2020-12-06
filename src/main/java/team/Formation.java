package team;

public enum Formation {
    F1(541, Type.PARK_THE_BUS),
    F2(532, Type.DEFENSIVE),
    F3(523, Type.DEFENSIVE),
    F4(451, Type.DEFENSIVE),
    F5(442, Type.BALANCED),
    F6(433, Type.OFFENSIVE),
    F7(424, Type.ALL_OUT_ATTACK),
    F8(352, Type.BALANCED),
    F9(343, Type.OFFENSIVE),
    F10(334, Type.ALL_OUT_ATTACK);

    enum Type {
        PARK_THE_BUS(-3),
        DEFENSIVE(-1),
        BALANCED(0),
        OFFENSIVE(1),
        ALL_OUT_ATTACK(3);

        private final int style;

        Type(final int style) {
            this.style = style;
        }
    }

    private final int scheme;
    private final Type type;

    Formation(final int scheme, final Type type) {
        this.scheme = scheme;
        this.type = type;
    }

    public int getDefenders() {
        return this.scheme / 100;
    }

    public int getMidfielders() {
        return this.scheme / 10 % 10;
    }

    public int getForwards() {
        return this.scheme % 10;
    }
}
