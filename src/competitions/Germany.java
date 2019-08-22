package competitions;

import players.Glory;
import teams.*;

public class Germany {
    private static final String LEAGUE = "Bundesliga";
    private static final Stadium STADIUM = new Stadium(1, "", 1900, "", 50000, 100);
    private static final Owner OWNER = new Owner(1, "", "", 100, 100);
    private static final Coach COACH = new Coach(1, "", 80, Formation.F5, 50, 50, 50);
    
    private static final Club BAYERN_MUNICH = new Club(1, "FC Bayern München", 1899, STADIUM, "Munich", LEAGUE,
            new Glory(5, 29, 19, 0, 0, 0, 0),
            100, 100, 80, OWNER, COACH);

    private static final Club BORUSSIA_DORTMUND = new Club(2, "Borussia Dortmund", 1902, STADIUM, "Dortmund", LEAGUE,
            new Glory(1, 8, 4, 0, 0, 0, 0),
            100, 100, 50, OWNER, COACH);

    private static final Club RB_LEIPZIG = new Club(3, "RB Leipzig", 1892, STADIUM, "Leipzig", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 45, OWNER, COACH);

    private static final Club BAYER_LEVERKUSEN = new Club(4, "Bayer 04 Leverkusen", 1878, STADIUM, "Leverkusen", LEAGUE,
            new Glory(0, 0, 1, 0, 0, 0, 0),
            100, 100, 35, OWNER, COACH);

    private static final Club WERDER_BREMEN = new Club(5, "SV Werder Bremen", 1886, STADIUM, "Bremen", LEAGUE,
            new Glory(0, 4, 6, 0, 0, 0, 0),
            100, 100, 28, OWNER, COACH);

    private static final Club BORUSSIA_MONCHENGLADBACH = new Club(6, "Borussia Mönchengladbach", 1886, STADIUM, "Monchengladbach", LEAGUE,
            new Glory(0, 5, 3, 0, 0, 0, 0),
            100, 100, 34, OWNER, COACH);

    private static final Club WOLFSBURG = new Club(7, "VfL Wolfsburg", 1878, STADIUM, "Wolfsburg", LEAGUE,
            new Glory(0, 1, 1, 0, 0, 0, 0),
            100, 100, 24, OWNER, COACH);

    private static final Club EINTRACHT_FRANKFURT = new Club(8, "Eintracht Frankfurt", 1886, STADIUM, "Frankfurt", LEAGUE,
            new Glory(0, 1, 5, 0, 0, 0, 0),
            100, 100, 22, OWNER, COACH);

    private static final Club HOFFENHEIM = new Club(9, "TSG 1899 Hoffenheim", 1886, STADIUM, "Sinsheim", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 20, OWNER, COACH);

    private static final Club FORTUNA_DUSSELDORF = new Club(10, "Fortuna Düsseldorf", 1886, STADIUM, "Dusseldorf", LEAGUE,
            new Glory(0, 1, 2, 0, 0, 0, 0),
            100, 100, 12, OWNER, COACH);

    private static final Club HERTHA = new Club(11, "Hertha BSC", 1886, STADIUM, "Berlin", LEAGUE,
            new Glory(0, 2, 0, 0, 0, 0, 0),
            100, 100, 17, OWNER, COACH);

    private static final Club MAINZ = new Club(12, "1. FSV Mainz 05", 1886, STADIUM, "Mainz", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 14, OWNER, COACH);

    private static final Club FREIBURG = new Club(13, "SC Freiburg", 1886, STADIUM, "Freiburg im Breisgau", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 15, OWNER, COACH);

    private static final Club SCHALKE = new Club(14, "FC Schalke 04", 1886, STADIUM, "Gelsenkirchen", LEAGUE,
            new Glory(0, 7, 5, 0, 0, 0, 0),
            100, 100, 25, OWNER, COACH);

    private static final Club AUGSBURG = new Club(15, "FC Augsburg", 1886, STADIUM, "Augsburg", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 16, OWNER, COACH);

    private static final Club KOLN = new Club(16, "1. FC Köln", 1886, STADIUM, "Cologne", LEAGUE,
            new Glory(0, 2, 4, 0, 0, 0, 0),
            100, 100, 10, OWNER, COACH);

    private static final Club PADERBORN = new Club(17, "SC Paderborn 07", 1886, STADIUM, "Paderborn", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 8, OWNER, COACH);

    private static final Club UNION_BERLIN = new Club(18, "1. FC Union Berlin", 1886, STADIUM, "Berlin", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 9, OWNER, COACH);

    public static final Club[] CLUBS = {BAYERN_MUNICH, BORUSSIA_DORTMUND, RB_LEIPZIG, BAYER_LEVERKUSEN, WERDER_BREMEN, BORUSSIA_MONCHENGLADBACH, WOLFSBURG, EINTRACHT_FRANKFURT,
            HOFFENHEIM, FORTUNA_DUSSELDORF, HERTHA, MAINZ, FREIBURG, SCHALKE, AUGSBURG, KOLN, PADERBORN,
            UNION_BERLIN};
}
