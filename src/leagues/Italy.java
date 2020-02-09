package leagues;

import players.Glory;
import team.Club;
import team.Coach;
import team.Formation;
import team.Owner;
import team.Stadium;

public class Italy {
    public static final String LEAGUE = "Serie A";
    private static final Stadium STADIUM = new Stadium(1, "", 1900, "", 50000, 100);
    private static final Owner OWNER = new Owner(1, "", "", 100, 100);
    private static final Coach COACH = new Coach(1, "", 80, Formation.F5, 50, 50, 50);
    
    private static final Club MILAN = new Club(401, "Milan", 1899, STADIUM, "Milan", LEAGUE,
            new Glory(7, 0, 18, 5, 0, 0, 0, 0),
            73, 100, 45, OWNER, COACH);

    private static final Club JUVENTUS = new Club(402, "Juventus", 1902, STADIUM, "Turin", LEAGUE,
            new Glory(2, 3, 35, 13, 0, 0, 0, 0),
            86, 100, 130, OWNER, COACH);

    private static final Club INTER = new Club(403, "Inter", 1892, STADIUM, "Milan", LEAGUE,
            new Glory(3, 3, 18, 7, 0, 0, 0, 0),
            77, 100, 60, OWNER, COACH);

    private static final Club NAPOLI = new Club(404, "Napoli", 1878, STADIUM, "Naples", LEAGUE,
            new Glory(0, 1, 2, 5, 0, 0, 0, 0),
            75, 100, 80, OWNER, COACH);

    private static final Club ROMA = new Club(405, "Roma", 1886, STADIUM, "Rome", LEAGUE,
            new Glory(0, 0, 3, 9, 0, 0, 0, 0),
            75, 100, 40, OWNER, COACH);

    private static final Club LAZIO = new Club(406, "Lazio", 1886, STADIUM, "Rome", LEAGUE,
            new Glory(0, 0, 2, 7, 0, 0, 0, 0),
            71, 100, 35, OWNER, COACH);

    private static final Club ATALANTA = new Club(407, "Atalanta", 1878, STADIUM, "Bergamo", LEAGUE,
            new Glory(0, 0, 0, 1, 0, 0, 0, 0),
            66, 100, 23, OWNER, COACH);

    private static final Club TORINO = new Club(408, "Torino", 1886, STADIUM, "Turin", LEAGUE,
            new Glory(0, 0, 7, 5, 0, 0, 0, 0),
            64, 100, 25, OWNER, COACH);

    private static final Club SAMPDORIA = new Club(409, "Sampdoria", 1886, STADIUM, "Genoa", LEAGUE,
            new Glory(0, 0, 1, 4, 0, 0, 0, 0),
            57, 100, 19, OWNER, COACH);

    private static final Club SASSUOLO = new Club(410, "Sassuolo", 1886, STADIUM, "Sassuolo", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            52, 100, 14, OWNER, COACH);

    private static final Club BOLOGNA = new Club(411, "Bologna", 1886, STADIUM, "Bologna", LEAGUE,
            new Glory(0, 0, 7, 2, 0, 0, 0, 0),
            53, 100, 17, OWNER, COACH);

    private static final Club CAGLIARI = new Club(412, "Cagliari", 1886, STADIUM, "Cagliari", LEAGUE,
            new Glory(0, 0, 1, 0, 0, 0, 0, 0),
            50, 100, 15, OWNER, COACH);

    private static final Club PARMA = new Club(413, "Parma", 1886, STADIUM, "Parma", LEAGUE,
            new Glory(0, 2, 0, 3, 0, 0, 0, 0),
            51, 100, 11, OWNER, COACH);

    private static final Club FIORENTINA = new Club(414, "Fiorentina", 1886, STADIUM, "Florence", LEAGUE,
            new Glory(0, 0, 2, 6, 0, 0, 0, 0),
            59, 100, 20, OWNER, COACH);

    private static final Club UDINESE = new Club(415, "Udinese", 1886, STADIUM, "Udine", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            58, 100, 14, OWNER, COACH);

    private static final Club BRESCIA = new Club(416, "Brescia", 1886, STADIUM, "Brescia", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            40, 100, 8, OWNER, COACH);

    private static final Club SPAL = new Club(417, "SPAL", 1886, STADIUM, "Ferrara", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            42, 100, 12, OWNER, COACH);

    private static final Club GENOA = new Club(418, "Genoa", 1886, STADIUM, "Genoa", LEAGUE,
            new Glory(0, 0, 9, 1, 0, 0, 0, 0),
            49, 100, 16, OWNER, COACH);

    private static final Club LECCE = new Club(419, "Lecce", 1886, STADIUM, "Lecce", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            37, 100, 9, OWNER, COACH);

    private static final Club VERONA = new Club(420, "Hellas Verona", 1886, STADIUM, "Verona", LEAGUE,
            new Glory(0, 0, 1, 0, 0, 0, 0, 0),
            39, 100, 8, OWNER, COACH);

    public static final Club[] CLUBS = {MILAN, JUVENTUS, INTER, NAPOLI, ROMA, LAZIO, ATALANTA, TORINO,
            SAMPDORIA, SASSUOLO, BOLOGNA, CAGLIARI, PARMA, FIORENTINA, UDINESE, BRESCIA, SPAL,
            GENOA, LECCE, VERONA};
}
