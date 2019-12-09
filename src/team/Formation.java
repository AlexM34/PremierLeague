package team;

public enum Formation {
    F1(541, Type.ParkTheBus),
    F2(532, Type.Defensive),
    F3(523, Type.Defensive),
    F4(451, Type.Defensive),
    F5(442, Type.Balanced),
    F6(433, Type.Offensive),
    F7(424, Type.AllOutAttack),
    F8(352, Type.Balanced),
    F9(343, Type.Offensive),
    F10(334, Type.AllOutAttack);

    enum Type {
        ParkTheBus(-3),
        Defensive(-1),
        Balanced(0),
        Offensive(1),
        AllOutAttack(3);

        private final int style;

        Type(final int style) {
            this.style = style;
        }

        int getStyle() {
            return this.style;
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

    Type getType() {
        return this.type;
    }
}
