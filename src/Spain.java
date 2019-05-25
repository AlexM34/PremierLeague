import java.util.HashSet;

class Spain {
    // TODO: Enter details
    // TODO: Update cup final
    private static String LA_LIGA = "La Liga";
    private static Stadium STADIUM = new Stadium(1, "", 1900, "", 50000, 100);
    private static Owner OWNER = new Owner(1, "", "", 100, 100);
    private static Coach COACH = new Coach(1, "", 80, Formation.F5, 50, 50, 50);
    
    private static Club BARCELONA = new Club(1, "Barcelona", 1899, STADIUM, "Barcelona", LA_LIGA,
            new Glory(0, 26, 30, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club REAL_MADRID = new Club(2, "Real Madrid", 1902, STADIUM, "Madrid", LA_LIGA,
            new Glory(0, 33, 19, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club ATLETICO_MADRID = new Club(3, "Atletico Madrid", 1892, STADIUM, "Madrid", LA_LIGA,
            new Glory(0, 10, 10, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club VALENCIA = new Club(4, "Valencia", 1878, STADIUM, "Valencia", LA_LIGA,
            new Glory(0, 6, 7, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club SEVILLA = new Club(5, "Sevilla", 1886, STADIUM, "Seville", LA_LIGA,
            new Glory(0, 1, 5, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club ATHLETIC_BILBAO = new Club(6, "Athletic Bilbao", 1886, STADIUM, "Bilbao", LA_LIGA,
            new Glory(0, 8, 23, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club GETAFE = new Club(7, "Getafe", 1878, STADIUM, "Madrid", LA_LIGA,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club ESPANYOL = new Club(8, "Espanyol", 1886, STADIUM, "Cornella de Llobregat", LA_LIGA,
            new Glory(0, 0, 4, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club REAL_SOCIEDAD = new Club(9, "Real Sociedad", 1886, STADIUM, "Bilbao", LA_LIGA,
            new Glory(0, 2, 2, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club REAL_BETIS = new Club(10, "Real Betis", 1886, STADIUM, "Seville", LA_LIGA,
            new Glory(0, 1, 2, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club ALAVES = new Club(11, "Alaves", 1886, STADIUM, "Vitoria-Gasteiz", LA_LIGA,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club EIBAR = new Club(12, "Eibar", 1886, STADIUM, "Eibar", LA_LIGA,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club LEGANES = new Club(13, "Leganes", 1886, STADIUM, "Leganes", LA_LIGA,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club VILLARREAL = new Club(14, "Villarreal", 1886, STADIUM, "Villarreal", LA_LIGA,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club LEVANTE = new Club(15, "Levante", 1886, STADIUM, "Valencia", LA_LIGA,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club VALLADOLID = new Club(16, "Valladolid", 1886, STADIUM, "Valladolid", LA_LIGA,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club CELTA_VIGO = new Club(17, "Celta Vigo", 1886, STADIUM, "Vigo", LA_LIGA,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club GIRONA = new Club(18, "Girona", 1886, STADIUM, "Girona", LA_LIGA,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club HUESCA = new Club(19, "Huesca", 1886, STADIUM, "Huesca", LA_LIGA,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club RAYO_VALLECANO = new Club(20, "Rayo Vallecano", 1886, STADIUM, "Madrid", LA_LIGA,
            new Glory(0, 3, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    static Club[] CLUBS = {BARCELONA, REAL_MADRID, ATLETICO_MADRID, VALENCIA, SEVILLA, ATHLETIC_BILBAO, GETAFE, ESPANYOL,
            REAL_SOCIEDAD, REAL_BETIS, ALAVES, EIBAR, LEGANES, VILLARREAL, LEVANTE, VALLADOLID, CELTA_VIGO,
            GIRONA, HUESCA, RAYO_VALLECANO};
}
