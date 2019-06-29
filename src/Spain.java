class Spain {
    static final String LEAGUE = "La Liga";
    private static final Stadium STADIUM = new Stadium(1, "", 1900, "", 50000, 100);
    private static final Owner OWNER = new Owner(1, "", "", 100, 100);
    private static final Coach COACH = new Coach(1, "", 80, Formation.F5, 50, 50, 50);
    
    private static final Club BARCELONA = new Club(1, "FC Barcelona", 1899, STADIUM, "Barcelona", LEAGUE,
            new Glory(5, 26, 30, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club REAL_MADRID = new Club(2, "Real Madrid", 1902, STADIUM, "Madrid", LEAGUE,
            new Glory(13, 33, 19, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club ATLETICO_MADRID = new Club(3, "Atlético Madrid", 1892, STADIUM, "Madrid", LEAGUE,
            new Glory(0, 10, 10, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club VALENCIA = new Club(4, "Valencia CF", 1878, STADIUM, "Valencia", LEAGUE,
            new Glory(0, 6, 8, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club SEVILLA = new Club(5, "Sevilla FC", 1886, STADIUM, "Seville", LEAGUE,
            new Glory(0, 1, 5, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club ATHLETIC_BILBAO = new Club(6, "Athletic Club de Bilbao", 1886, STADIUM, "Bilbao", LEAGUE,
            new Glory(0, 8, 23, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club GETAFE = new Club(7, "Getafe CF", 1878, STADIUM, "Madrid", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club ESPANYOL = new Club(8, "RCD Espanyol", 1886, STADIUM, "Cornella de Llobregat", LEAGUE,
            new Glory(0, 0, 4, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club REAL_SOCIEDAD = new Club(9, "Real Sociedad", 1886, STADIUM, "Bilbao", LEAGUE,
            new Glory(0, 2, 2, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club REAL_BETIS = new Club(10, "Real Betis", 1886, STADIUM, "Seville", LEAGUE,
            new Glory(0, 1, 2, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club ALAVES = new Club(11, "Deportivo Alavés", 1886, STADIUM, "Vitoria-Gasteiz", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club EIBAR = new Club(12, "SD Eibar", 1886, STADIUM, "Eibar", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club LEGANES = new Club(13, "CD Leganés", 1886, STADIUM, "Leganes", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club VILLARREAL = new Club(14, "Villarreal CF", 1886, STADIUM, "Villarreal", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club LEVANTE = new Club(15, "Levante UD", 1886, STADIUM, "Valencia", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club VALLADOLID = new Club(16, "Real Valladolid CF", 1886, STADIUM, "Valladolid", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club CELTA_VIGO = new Club(17, "RC Celta", 1886, STADIUM, "Vigo", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club OSASUNA = new Club(18, "CA Osasuna", 1886, STADIUM, "Pamplona", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club GRANADA = new Club(19, "Granada CF", 1886, STADIUM, "Granada", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static final Club MALLORCA = new Club(20, "RCD Mallorca", 1886, STADIUM, "Palma", LEAGUE,
            new Glory(0, 0, 1, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    static final Club[] CLUBS = {BARCELONA, REAL_MADRID, ATLETICO_MADRID, VALENCIA, SEVILLA, ATHLETIC_BILBAO, GETAFE, ESPANYOL,
            REAL_SOCIEDAD, REAL_BETIS, ALAVES, EIBAR, LEGANES, VILLARREAL, LEVANTE, VALLADOLID, CELTA_VIGO,
            OSASUNA, GRANADA, MALLORCA};
}
