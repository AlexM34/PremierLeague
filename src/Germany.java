class Germany {
    static String LEAGUE = "Bundesliga";
    private static Stadium STADIUM = new Stadium(1, "", 1900, "", 50000, 100);
    private static Owner OWNER = new Owner(1, "", "", 100, 100);
    private static Coach COACH = new Coach(1, "", 80, Formation.F5, 50, 50, 50);
    
    private static Club BAYERN_MUNICH = new Club(1, "FC Bayern München", 1899, STADIUM, "Munich", LEAGUE,
            new Glory(5, 29, 19, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club BORUSSIA_DORTMUND = new Club(2, "Borussia Dortmund", 1902, STADIUM, "Dortmund", LEAGUE,
            new Glory(1, 8, 4, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club RB_LEIPZIG = new Club(3, "RB Leipzig", 1892, STADIUM, "Leipzig", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club BAYER_LEVERKUSEN = new Club(4, "Bayer 04 Leverkusen", 1878, STADIUM, "Leverkusen", LEAGUE,
            new Glory(0, 0, 1, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club WERDER_BREMEN = new Club(5, "SV Werder Bremen", 1886, STADIUM, "Bremen", LEAGUE,
            new Glory(0, 4, 6, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club BORUSSIA_MONCHENGLADBACH = new Club(6, "Borussia Mönchengladbach", 1886, STADIUM, "Monchengladbach", LEAGUE,
            new Glory(0, 5, 3, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club WOLFSBURG = new Club(7, "VfL Wolfsburg", 1878, STADIUM, "Wolfsburg", LEAGUE,
            new Glory(0, 1, 1, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club EINTRACHT_FRANKFURT = new Club(8, "Eintracht Frankfurt", 1886, STADIUM, "Frankfurt", LEAGUE,
            new Glory(0, 1, 5, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club HOFFENHEIM = new Club(9, "TSG 1899 Hoffenheim", 1886, STADIUM, "Sinsheim", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club FORTUNA_DUSSELDORF = new Club(10, "Fortuna Düsseldorf", 1886, STADIUM, "Dusseldorf", LEAGUE,
            new Glory(0, 1, 2, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club HERTHA = new Club(11, "Hertha BSC", 1886, STADIUM, "Berlin", LEAGUE,
            new Glory(0, 2, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club MAINZ = new Club(12, "1. FSV Mainz 05", 1886, STADIUM, "Mainz", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club FREIBURG = new Club(13, "SC Freiburg", 1886, STADIUM, "Freiburg im Breisgau", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club SCHALKE = new Club(14, "FC Schalke 04", 1886, STADIUM, "Gelsenkirchen", LEAGUE,
            new Glory(0, 7, 5, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club AUGSBURG = new Club(15, "FC Augsburg", 1886, STADIUM, "Augsburg", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club KOLN = new Club(16, "1. FC Köln", 1886, STADIUM, "Cologne", LEAGUE,
            new Glory(0, 2, 4, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club PADERBORN = new Club(17, "SC Paderborn 07", 1886, STADIUM, "Paderborn", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club UNION_BERLIN = new Club(18, "1. FC Union Berlin", 1886, STADIUM, "Berlin", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    static Club[] CLUBS = {BAYERN_MUNICH, BORUSSIA_DORTMUND, RB_LEIPZIG, BAYER_LEVERKUSEN, WERDER_BREMEN, BORUSSIA_MONCHENGLADBACH, WOLFSBURG, EINTRACHT_FRANKFURT,
            HOFFENHEIM, FORTUNA_DUSSELDORF, HERTHA, MAINZ, FREIBURG, SCHALKE, AUGSBURG, KOLN, PADERBORN,
            UNION_BERLIN};
}
