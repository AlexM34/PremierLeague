package league;

import player.Glory;
import team.Club;
import team.Coach;
import team.Formation;
import team.Owner;
import team.Stadium;

public class Spain {
    public static final String LEAGUE = "La Liga";
    private static final Stadium STADIUM = new Stadium(1, "", 1900, "", 50000, 100);
    private static final Owner OWNER = new Owner(1, "", "", 100, 100);
    private static final Coach COACH = new Coach(1, "", 80, Formation.F5, 50, 50, 50);
    
    private static final Club BARCELONA = new Club(201, "FC Barcelona", 1899, STADIUM, "Barcelona", LEAGUE,
            new Glory(5, 0, 26, 30, 0, 0, 0, 0),
            94, 100, 150, OWNER, COACH);

    private static final Club REAL_MADRID = new Club(202, "Real Madrid", 1902, STADIUM, "Madrid", LEAGUE,
            new Glory(13, 2, 33, 19, 0, 0, 0, 0),
            93, 100, 200, OWNER, COACH);

    private static final Club ATLETICO_MADRID = new Club(203, "Atlético Madrid", 1892, STADIUM, "Madrid", LEAGUE,
            new Glory(0, 3, 10, 10, 0, 0, 0, 0),
            81, 100, 75, OWNER, COACH);

    private static final Club VALENCIA = new Club(204, "Valencia CF", 1878, STADIUM, "Valencia", LEAGUE,
            new Glory(0, 1, 6, 8, 0, 0, 0, 0),
            73, 100, 50, OWNER, COACH);

    private static final Club SEVILLA = new Club(205, "Sevilla FC", 1886, STADIUM, "Seville", LEAGUE,
            new Glory(0, 5, 1, 5, 0, 0, 0, 0),
            75, 100, 40, OWNER, COACH);

    private static final Club ATHLETIC_BILBAO = new Club(206, "Athletic Club de Bilbao", 1886, STADIUM, "Bilbao", LEAGUE,
            new Glory(0, 0, 8, 23, 0, 0, 0, 0),
            68, 100, 30, OWNER, COACH);

    private static final Club GETAFE = new Club(207, "Getafe CF", 1878, STADIUM, "Madrid", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            60, 100, 27, OWNER, COACH);

    private static final Club ESPANYOL = new Club(208, "RCD Espanyol", 1886, STADIUM, "Cornella de Llobregat", LEAGUE,
            new Glory(0, 0, 0, 4, 0, 0, 0, 0),
            62, 100, 22, OWNER, COACH);

    private static final Club REAL_SOCIEDAD = new Club(209, "Real Sociedad", 1886, STADIUM, "Bilbao", LEAGUE,
            new Glory(0, 0, 2, 2, 0, 0, 0, 0),
            65, 100, 26, OWNER, COACH);

    private static final Club REAL_BETIS = new Club(210, "Real Betis", 1886, STADIUM, "Seville", LEAGUE,
            new Glory(0, 0, 1, 2, 0, 0, 0, 0),
            59, 100, 17, OWNER, COACH);

    private static final Club ALAVES = new Club(211, "Deportivo Alavés", 1886, STADIUM, "Vitoria-Gasteiz", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            50, 100, 11, OWNER, COACH);

    private static final Club EIBAR = new Club(212, "SD Eibar", 1886, STADIUM, "Eibar", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            47, 100, 10, OWNER, COACH);

    private static final Club LEGANES = new Club(213, "CD Leganés", 1886, STADIUM, "Leganes", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            51, 100, 8, OWNER, COACH);

    private static final Club VILLARREAL = new Club(214, "Villarreal CF", 1886, STADIUM, "Villarreal", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            57, 100, 18, OWNER, COACH);

    private static final Club LEVANTE = new Club(215, "Levante UD", 1886, STADIUM, "Valencia", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            54, 100, 13, OWNER, COACH);

    private static final Club VALLADOLID = new Club(216, "Real Valladolid CF", 1886, STADIUM, "Valladolid", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            55, 100, 9, OWNER, COACH);

    private static final Club CELTA_VIGO = new Club(217, "RC Celta", 1886, STADIUM, "Vigo", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            53, 100, 16, OWNER, COACH);

    private static final Club OSASUNA = new Club(218, "CA Osasuna", 1886, STADIUM, "Pamplona", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            50, 100, 8, OWNER, COACH);

    private static final Club GRANADA = new Club(219, "Granada CF", 1886, STADIUM, "Granada", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0, 0),
            44, 100, 7, OWNER, COACH);

    private static final Club MALLORCA = new Club(220, "RCD Mallorca", 1886, STADIUM, "Palma", LEAGUE,
            new Glory(0, 0, 0, 1, 0, 0, 0, 0),
            49, 100, 6, OWNER, COACH);

    public static final Club[] CLUBS = {BARCELONA, REAL_MADRID, ATLETICO_MADRID, VALENCIA, SEVILLA, ATHLETIC_BILBAO,
            GETAFE, ESPANYOL, REAL_SOCIEDAD, REAL_BETIS, ALAVES, EIBAR, LEGANES, VILLARREAL, LEVANTE, VALLADOLID,
            CELTA_VIGO, OSASUNA, GRANADA, MALLORCA};

    public static String getLeague() {
        return LEAGUE;
    }

    public static Club[] getClubs() {
        return CLUBS;
    }
}
