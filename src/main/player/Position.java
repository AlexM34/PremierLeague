package main.player;

public enum Position {
    GK(Role.Goalkeeper, 0),
    LWB(Role.Defender, 2),
    LB(Role.Defender, 2),
    LCB(Role.Defender, 1),
    CB(Role.Defender, 1),
    RCB(Role.Defender, 1),
    RB(Role.Defender, 2),
    RWB(Role.Defender, 2),
    LAM(Role.Midfielder, 4),
    LM(Role.Midfielder, 3),
    LDM(Role.Midfielder, 2),
    LCM(Role.Midfielder, 3),
    CDM(Role.Midfielder, 2),
    CM(Role.Midfielder, 3),
    CAM(Role.Midfielder, 4),
    RDM(Role.Midfielder, 2),
    RCM(Role.Midfielder, 3),
    RM(Role.Midfielder, 3),
    RAM(Role.Midfielder, 4),
    LS(Role.Forward, 5),
    LF(Role.Forward, 5),
    LW(Role.Forward, 5),
    CF(Role.Forward, 5),
    ST(Role.Forward, 5),
    RW(Role.Forward, 5),
    RF(Role.Forward, 5),
    RS(Role.Forward, 5);

    public enum Role {
        Goalkeeper,
        Defender,
        Midfielder,
        Forward
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
