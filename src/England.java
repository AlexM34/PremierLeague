import java.util.HashSet;

class England {
    // TODO: Enter details
    private static String PREMIER_LEAGUE = "Premier League";
    private static Stadium STADIUM = new Stadium(1, "", 1900, "", 50000, 100);
    private static Owner OWNER = new Owner(1, "", "", 100, 100);
    private static Coach COACH = new Coach(1, "", 80, Formation.F5, 50, 50, 50);
    
    private static Club ARSENAL = new Club(1, "Arsenal", 1886, STADIUM, "London", PREMIER_LEAGUE,
            new Glory(0, 13, 13, 2, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club MANCHESTER_CITY = new Club(2, "Manchester City", 1886, STADIUM, "Manchester", PREMIER_LEAGUE,
            new Glory(0, 6, 6, 6, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club LIVERPOOL = new Club(3, "Liverpool", 1892, STADIUM, "Liverpool", PREMIER_LEAGUE,
            new Glory(0, 18, 7, 8, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club MANCHESTER_UNITED = new Club(4, "Manchester United", 1878, STADIUM, "Manchester", PREMIER_LEAGUE,
            new Glory(0, 20, 12, 5, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club CHELSEA = new Club(5, "Chelsea", 1886, STADIUM, "London", PREMIER_LEAGUE,
            new Glory(0, 6, 8, 5, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club TOTTENHAM = new Club(6, "Tottenham Hotspur", 1886, STADIUM, "London", PREMIER_LEAGUE,
            new Glory(0, 2, 8, 4, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club EVERTON = new Club(7, "Everton", 1878, STADIUM, "Liverpool", PREMIER_LEAGUE,
            new Glory(0, 9, 5, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club LEICESTER = new Club(8, "Leicester City", 1886, STADIUM, "Leicester", PREMIER_LEAGUE,
            new Glory(0, 1, 0, 3, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club WOLVERHAMPTON = new Club(9, "Wolverhampton Wanderers", 1886, STADIUM, "Wolverhampton", PREMIER_LEAGUE,
            new Glory(0, 3, 4, 2, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club WATFORD = new Club(10, "Watford", 1886, STADIUM, "Watford", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club WEST_HAM = new Club(11, "West Ham United", 1886, STADIUM, "London", PREMIER_LEAGUE,
            new Glory(0, 0, 3, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club BOURNEMOUTH = new Club(12, "Bournemouth", 1886, STADIUM, "Bournemouth", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club CRYSTAL_PALACE = new Club(13, "Crystal Palace", 1886, STADIUM, "London", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club BURNLEY = new Club(14, "Burnley", 1886, STADIUM, "Burnley", PREMIER_LEAGUE,
            new Glory(0, 2, 1, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club NEWCASTLE = new Club(15, "Newcastle United", 1886, STADIUM, "Newcastle upon Tyne", PREMIER_LEAGUE,
            new Glory(0, 4, 6, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club SOUTHAMPTON = new Club(16, "Southampton", 1886, STADIUM, "Southampton", PREMIER_LEAGUE,
            new Glory(0, 0, 1, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club BRIGHTON = new Club(17, "Brighton & Hove Albion", 1886, STADIUM, "Brighton", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club CARDIFF = new Club(18, "Cardiff City", 1886, STADIUM, "Cardiff", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club FULHAM = new Club(19, "Fulham", 1886, STADIUM, "London", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club HUDDERSFIELD = new Club(20, "Huddersfield Town", 1886, STADIUM, "Huddersfield", PREMIER_LEAGUE,
            new Glory(0, 3, 1, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    static Club[] CLUBS = {ARSENAL, MANCHESTER_CITY, LIVERPOOL, MANCHESTER_UNITED, CHELSEA, TOTTENHAM, EVERTON, LEICESTER,
    WOLVERHAMPTON, WATFORD, WEST_HAM, BOURNEMOUTH, CRYSTAL_PALACE, BURNLEY, NEWCASTLE, SOUTHAMPTON, BRIGHTON,
    CARDIFF, FULHAM, HUDDERSFIELD};
}
