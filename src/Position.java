enum Position {
    GK(Role.Goalkeeper),
    LWB(Role.Defender),
    LB(Role.Defender),
    LCB(Role.Defender),
    CB(Role.Defender),
    RCB(Role.Defender),
    RB(Role.Defender),
    RWB(Role.Defender),
    LAM(Role.Midfielder),
    LM(Role.Midfielder),
    LDM(Role.Midfielder),
    LCM(Role.Midfielder),
    CDM(Role.Midfielder),
    CM(Role.Midfielder),
    CAM(Role.Midfielder),
    RDM(Role.Midfielder),
    RCM(Role.Midfielder),
    RM(Role.Midfielder),
    RAM(Role.Midfielder),
    LS(Role.Forward),
    LF(Role.Forward),
    LW(Role.Forward),
    CF(Role.Forward),
    ST(Role.Forward),
    RW(Role.Forward),
    RF(Role.Forward),
    RS(Role.Forward);

    enum Role {
        Goalkeeper,
        Defender,
        Midfielder,
        Forward
    }

    private Role role;

    Position(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return this.role;
    }
}
