class England {
    static String LEAGUE = "Premier League";
    private static Stadium STADIUM = new Stadium(1, "", 1900, "", 50000, 100);
    private static Owner OWNER = new Owner(1, "", "", 100, 100);
    private static Coach COACH = new Coach(1, "", 80, Formation.F5, 50, 50, 50);
    
    private static Club ARSENAL = new Club(1, "Arsenal", 1886, STADIUM, "London", LEAGUE,
            new Glory(0, 13, 13, 2, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club MANCHESTER_CITY = new Club(2, "Manchester City", 1886, STADIUM, "Manchester", LEAGUE,
            new Glory(0, 6, 6, 6, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club LIVERPOOL = new Club(3, "Liverpool", 1892, STADIUM, "Liverpool", LEAGUE,
            new Glory(6, 18, 7, 8, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club MANCHESTER_UNITED = new Club(4, "Manchester United", 1878, STADIUM, "Manchester", LEAGUE,
            new Glory(3, 20, 12, 5, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club CHELSEA = new Club(5, "Chelsea", 1886, STADIUM, "London", LEAGUE,
            new Glory(1, 6, 8, 5, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club TOTTENHAM = new Club(6, "Tottenham Hotspur", 1886, STADIUM, "London", LEAGUE,
            new Glory(0, 2, 8, 4, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club EVERTON = new Club(7, "Everton", 1878, STADIUM, "Liverpool", LEAGUE,
            new Glory(0, 9, 5, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club LEICESTER = new Club(8, "Leicester City", 1886, STADIUM, "Leicester", LEAGUE,
            new Glory(0, 1, 0, 3, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club WOLVERHAMPTON = new Club(9, "Wolverhampton Wanderers", 1886, STADIUM, "Wolverhampton", LEAGUE,
            new Glory(0, 3, 4, 2, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club WATFORD = new Club(10, "Watford", 1886, STADIUM, "Watford", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club WEST_HAM = new Club(11, "West Ham United", 1886, STADIUM, "London", LEAGUE,
            new Glory(0, 0, 3, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club BOURNEMOUTH = new Club(12, "Bournemouth", 1886, STADIUM, "Bournemouth", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club CRYSTAL_PALACE = new Club(13, "Crystal Palace", 1886, STADIUM, "London", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club BURNLEY = new Club(14, "Burnley", 1886, STADIUM, "Burnley", LEAGUE,
            new Glory(0, 2, 1, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club NEWCASTLE = new Club(15, "Newcastle United", 1886, STADIUM, "Newcastle upon Tyne", LEAGUE,
            new Glory(0, 4, 6, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club SOUTHAMPTON = new Club(16, "Southampton", 1886, STADIUM, "Southampton", LEAGUE,
            new Glory(0, 0, 1, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club BRIGHTON = new Club(17, "Brighton & Hove Albion", 1886, STADIUM, "Brighton", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club ASTON_VILLA = new Club(18, "Aston Villa", 1886, STADIUM, "Birmingham", LEAGUE,
            new Glory(1, 7, 7, 5, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club NORWICH = new Club(19, "Norwich City", 1886, STADIUM, "Norwich", LEAGUE,
            new Glory(0, 0, 2, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    private static Club SHEFFIELD_UNITED = new Club(20, "Sheffield United", 1886, STADIUM, "Sheffield", LEAGUE,
            new Glory(0, 1, 4, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH);

    static Club[] CLUBS = {ARSENAL, MANCHESTER_CITY, LIVERPOOL, MANCHESTER_UNITED, CHELSEA, TOTTENHAM, EVERTON, LEICESTER,
    WOLVERHAMPTON, WATFORD, WEST_HAM, BOURNEMOUTH, CRYSTAL_PALACE, BURNLEY, NEWCASTLE, SOUTHAMPTON, BRIGHTON,
            ASTON_VILLA, NORWICH, SHEFFIELD_UNITED};
}
