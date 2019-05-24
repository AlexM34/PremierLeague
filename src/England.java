class England {
    private String PREMIER_LEAGUE = "Premier League";
    private Stadium STADIUM = new Stadium(1, "", 1900, "", 50000, 100);
    private Owner OWNER = new Owner(1, "", "", 100, 100);
    private Coach COACH = new Coach(1, "", 100, Formation.F5, 100, 100, 100);
    
    Club ARSENAL = new Club(1, "Arsenal", 1886, STADIUM, "London", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club MANCHESTER_CITY = new Club(2, "Manchester City", 1886, STADIUM, "Manchester", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club LIVERPOOL = new Club(3, "Liverpool", 1892, STADIUM, "Liverpool", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club MANCHESTER_UNITED = new Club(4, "Manchester United", 1878, STADIUM, "Manchester", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club CHELSEA = new Club(5, "Chelsea", 1886, STADIUM, "London", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club TOTTENHAM = new Club(6, "Tottenham Hotspur", 1886, STADIUM, "London", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club EVERTON = new Club(7, "Everton", 1878, STADIUM, "Liverpool", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club LEICESTER = new Club(8, "Leicester City", 1886, STADIUM, "Leicester", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club WOLVERHAMPTON = new Club(9, "Wolverhampton Wanderers", 1886, STADIUM, "Wolverhampton", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club WATFORD = new Club(10, "Watford", 1886, STADIUM, "Watford", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club WEST_HAM = new Club(11, "West Ham United", 1886, STADIUM, "London", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club BOURNEMOUTH = new Club(12, "Bournemouth", 1886, STADIUM, "Bournemouth", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club CRYSTAL_PALACE = new Club(13, "Crystal Palace", 1886, STADIUM, "London", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club BURNLEY = new Club(14, "Burnley", 1886, STADIUM, "Burnley", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club NEWCASTLE = new Club(15, "Newcastle United", 1886, STADIUM, "Newcastle upon Tyne", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));
    
    Club SOUTHAMPTON = new Club(16, "Southampton", 1886, STADIUM, "Southampton", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    Club BRIGHTON = new Club(17, "Brighton & Hove Albion", 1886, STADIUM, "Brighton", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    Club CARDIFF = new Club(18, "Cardiff City", 1886, STADIUM, "Cardiff", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    Club FULHAM = new Club(19, "Fulham", 1886, STADIUM, "London", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    Club HUDDERSFIELD = new Club(20, "Huddersfield Town", 1886, STADIUM, "Huddersfield", PREMIER_LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new Footballer[34], new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    Club[] ENGLAND = {ARSENAL, MANCHESTER_CITY, LIVERPOOL, MANCHESTER_UNITED, CHELSEA, TOTTENHAM, EVERTON, LEICESTER,
    WOLVERHAMPTON, WATFORD, WEST_HAM, BOURNEMOUTH, CRYSTAL_PALACE, BURNLEY, NEWCASTLE, SOUTHAMPTON, BRIGHTON,
    CARDIFF, FULHAM, HUDDERSFIELD};
}
