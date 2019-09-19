package competitions;

import players.Glory;
import teams.Club;
import teams.Coach;
import teams.Formation;
import teams.Owner;
import teams.Stadium;

public class England {
    public static final String LEAGUE = "Premier League";
    private static final Stadium STADIUM = new Stadium(1, "", 1900, "", 50000, 100);
    private static final Owner OWNER = new Owner(1, "", "", 100, 100);
    private static final Coach COACH = new Coach(1, "", 80, Formation.F5, 50, 50, 50);
    
    private static final Club ARSENAL = new Club(101, "Arsenal", 1886, STADIUM, "London", LEAGUE,
            new Glory(0, 13, 13, 2, 0, 0, 0),
            100, 100, 60, OWNER, COACH);

    private static final Club MANCHESTER_CITY = new Club(102, "Manchester City", 1886, STADIUM, "Manchester", LEAGUE,
            new Glory(0, 6, 6, 6, 0, 0, 0),
            100, 100, 130, OWNER, COACH);

    private static final Club LIVERPOOL = new Club(103, "Liverpool", 1892, STADIUM, "Liverpool", LEAGUE,
            new Glory(6, 18, 7, 8, 0, 0, 0),
            100, 100, 70, OWNER, COACH);

    private static final Club MANCHESTER_UNITED = new Club(104, "Manchester United", 1878, STADIUM, "Manchester", LEAGUE,
            new Glory(3, 20, 12, 5, 0, 0, 0),
            100, 100, 160, OWNER, COACH);

    private static final Club CHELSEA = new Club(105, "Chelsea", 1886, STADIUM, "London", LEAGUE,
            new Glory(1, 6, 8, 5, 0, 0, 0),
            100, 100, 80, OWNER, COACH);

    private static final Club TOTTENHAM = new Club(106, "Tottenham Hotspur", 1886, STADIUM, "London", LEAGUE,
            new Glory(0, 2, 8, 4, 0, 0, 0),
            100, 100, 50, OWNER, COACH);

    private static final Club EVERTON = new Club(107, "Everton", 1878, STADIUM, "Liverpool", LEAGUE,
            new Glory(0, 9, 5, 0, 0, 0, 0),
            100, 100, 35, OWNER, COACH);

    private static final Club LEICESTER = new Club(108, "Leicester City", 1886, STADIUM, "Leicester", LEAGUE,
            new Glory(0, 1, 0, 3, 0, 0, 0),
            100, 100, 30, OWNER, COACH);

    private static final Club WOLVERHAMPTON = new Club(109, "Wolverhampton Wanderers", 1886, STADIUM, "Wolverhampton", LEAGUE,
            new Glory(0, 3, 4, 2, 0, 0, 0),
            100, 100, 50, OWNER, COACH);

    private static final Club WATFORD = new Club(110, "Watford", 1886, STADIUM, "Watford", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 23, OWNER, COACH);

    private static final Club WEST_HAM = new Club(111, "West Ham United", 1886, STADIUM, "London", LEAGUE,
            new Glory(0, 0, 3, 0, 0, 0, 0),
            100, 100, 17, OWNER, COACH);

    private static final Club BOURNEMOUTH = new Club(112, "Bournemouth", 1886, STADIUM, "Bournemouth", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 14, OWNER, COACH);

    private static final Club CRYSTAL_PALACE = new Club(113, "Crystal Palace", 1886, STADIUM, "London", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 16, OWNER, COACH);

    private static final Club BURNLEY = new Club(114, "Burnley", 1886, STADIUM, "Burnley", LEAGUE,
            new Glory(0, 2, 1, 0, 0, 0, 0),
            100, 100, 9, OWNER, COACH);

    private static final Club NEWCASTLE = new Club(115, "Newcastle United", 1886, STADIUM, "Newcastle upon Tyne", LEAGUE,
            new Glory(0, 4, 6, 0, 0, 0, 0),
            100, 100, 23, OWNER, COACH);

    private static final Club SOUTHAMPTON = new Club(116, "Southampton", 1886, STADIUM, "Southampton", LEAGUE,
            new Glory(0, 0, 1, 0, 0, 0, 0),
            100, 100, 15, OWNER, COACH);

    private static final Club BRIGHTON = new Club(117, "Brighton & Hove Albion", 1886, STADIUM, "Brighton", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 11, OWNER, COACH);

    private static final Club ASTON_VILLA = new Club(118, "Aston Villa", 1886, STADIUM, "Birmingham", LEAGUE,
            new Glory(1, 7, 7, 5, 0, 0, 0),
            100, 100, 18, OWNER, COACH);

    private static final Club NORWICH = new Club(119, "Norwich City", 1886, STADIUM, "Norwich", LEAGUE,
            new Glory(0, 0, 2, 0, 0, 0, 0),
            100, 100, 8, OWNER, COACH);

    private static final Club SHEFFIELD_UNITED = new Club(120, "Sheffield United", 1886, STADIUM, "Sheffield", LEAGUE,
            new Glory(0, 1, 4, 0, 0, 0, 0),
            100, 100, 7, OWNER, COACH);

    public static final Club[] CLUBS = {ARSENAL, MANCHESTER_CITY, LIVERPOOL, MANCHESTER_UNITED, CHELSEA, TOTTENHAM, EVERTON, LEICESTER,
    WOLVERHAMPTON, WATFORD, WEST_HAM, BOURNEMOUTH, CRYSTAL_PALACE, BURNLEY, NEWCASTLE, SOUTHAMPTON, BRIGHTON,
            ASTON_VILLA, NORWICH, SHEFFIELD_UNITED};
}
