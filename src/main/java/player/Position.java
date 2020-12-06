package player;

public enum Position {
    GK(Role.GOALKEEPER, 0),
    LWB(Role.DEFENDER, 2),
    LB(Role.DEFENDER, 2),
    LCB(Role.DEFENDER, 1),
    CB(Role.DEFENDER, 1),
    RCB(Role.DEFENDER, 1),
    RB(Role.DEFENDER, 2),
    RWB(Role.DEFENDER, 2),
    LAM(Role.MIDFIELDER, 4),
    LM(Role.MIDFIELDER, 3),
    LDM(Role.MIDFIELDER, 2),
    LCM(Role.MIDFIELDER, 3),
    CDM(Role.MIDFIELDER, 2),
    CM(Role.MIDFIELDER, 3),
    CAM(Role.MIDFIELDER, 4),
    RDM(Role.MIDFIELDER, 2),
    RCM(Role.MIDFIELDER, 3),
    RM(Role.MIDFIELDER, 3),
    RAM(Role.MIDFIELDER, 4),
    LS(Role.FORWARD, 5),
    LF(Role.FORWARD, 5),
    LW(Role.FORWARD, 5),
    CF(Role.FORWARD, 5),
    ST(Role.FORWARD, 5),
    RW(Role.FORWARD, 5),
    RF(Role.FORWARD, 5),
    RS(Role.FORWARD, 5);

    public enum Role {
        GOALKEEPER,
        DEFENDER,
        MIDFIELDER,
        FORWARD
    }

    private final Role role;
    private final int attackingDuty;

    Position(final Role role, final int attackingDuty) {
        this.role = role;
        this.attackingDuty = attackingDuty;
    }

    public Role getRole() {
        return this.role;
    }

    public int getAttackingDuty() {
        return this.attackingDuty;
    }
}
