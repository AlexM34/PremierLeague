class Italy {
    private static String SERIE_A = "Serie A";
    private static Stadium STADIUM = new Stadium(1, "", 1900, "", 50000, 100);
    private static Owner OWNER = new Owner(1, "", "", 100, 100);
    private static Coach COACH = new Coach(1, "", 80, Formation.F5, 50, 50, 50);
    
    private static Club MILAN = new Club(1, "Milan", 1899, STADIUM, "Milan", SERIE_A,
            new Glory(7, 18, 5, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club JUVENTUS = new Club(2, "Juventus", 1902, STADIUM, "Turin", SERIE_A,
            new Glory(2, 35, 13, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club INTER = new Club(3, "Inter", 1892, STADIUM, "Milan", SERIE_A,
            new Glory(3, 18, 7, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club NAPOLI = new Club(4, "Napoli", 1878, STADIUM, "Naples", SERIE_A,
            new Glory(0, 2, 5, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club ROMA = new Club(5, "Roma", 1886, STADIUM, "Rome", SERIE_A,
            new Glory(0, 3, 9, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club LAZIO = new Club(6, "Lazio", 1886, STADIUM, "Rome", SERIE_A,
            new Glory(0, 2, 7, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club ATALANTA = new Club(7, "Atalanta", 1878, STADIUM, "Bergamo", SERIE_A,
            new Glory(0, 0, 1, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club TORINO = new Club(8, "Torino", 1886, STADIUM, "Turin", SERIE_A,
            new Glory(0, 7, 5, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club SAMPDORIA = new Club(9, "Sampdoria", 1886, STADIUM, "Genoa", SERIE_A,
            new Glory(0, 1, 4, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club SASSUOLO = new Club(10, "Sassuolo", 1886, STADIUM, "Sassuolo", SERIE_A,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club BOLOGNA = new Club(11, "Bologna", 1886, STADIUM, "Bologna", SERIE_A,
            new Glory(0, 7, 2, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club CAGLIARI = new Club(12, "Cagliari", 1886, STADIUM, "Cagliari", SERIE_A,
            new Glory(0, 1, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club PARMA = new Club(13, "Parma", 1886, STADIUM, "Parma", SERIE_A,
            new Glory(0, 0, 3, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club FIORENTINA = new Club(14, "Fiorentina", 1886, STADIUM, "Florence", SERIE_A,
            new Glory(0, 2, 6, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club UDINESE = new Club(15, "Udinese", 1886, STADIUM, "Udine", SERIE_A,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club BRESCIA = new Club(16, "Brescia", 1886, STADIUM, "Brescia", SERIE_A,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club SPAL = new Club(17, "SPAL", 1886, STADIUM, "Ferrara", SERIE_A,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club GENOA = new Club(18, "Genoa", 1886, STADIUM, "Genoa", SERIE_A,
            new Glory(0, 9, 1, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club LECCE = new Club(19, "Lecce", 1886, STADIUM, "Lecce", SERIE_A,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club VERONA = new Club(20, "Hellas Verona", 1886, STADIUM, "Verona", SERIE_A,
            new Glory(0, 1, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    static Club[] CLUBS = {MILAN, JUVENTUS, INTER, NAPOLI, ROMA, LAZIO, ATALANTA, TORINO,
            SAMPDORIA, SASSUOLO, BOLOGNA, CAGLIARI, PARMA, FIORENTINA, UDINESE, BRESCIA, SPAL,
            GENOA, LECCE, VERONA};
}
