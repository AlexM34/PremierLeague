import java.util.HashSet;

class France {
    // TODO: Enter details
    private static String LEAGUE_1 = "League 1";
    private static Stadium STADIUM = new Stadium(1, "", 1900, "", 50000, 100);
    private static Owner OWNER = new Owner(1, "", "", 100, 100);
    private static Coach COACH = new Coach(1, "", 80, Formation.F5, 50, 50, 50);
    
    private static Club LYON = new Club(1, "Olympique Lyonnais", 1899, STADIUM, "Decines-Charpieu", LEAGUE_1,
            new Glory(0, 7, 5, 1, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club PSG = new Club(2, "Paris Saint-Germain", 1902, STADIUM, "Paris", LEAGUE_1,
            new Glory(0, 8, 12, 8, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club MARSEILLE = new Club(3, "Olympique de Marseille", 1892, STADIUM, "Marseille", LEAGUE_1,
            new Glory(0, 9, 10, 3, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club SAINT_ETIENNE = new Club(4, "AS Saint-Étienne", 1878, STADIUM, "Saint-Etienne", LEAGUE_1,
            new Glory(0, 10, 6, 1, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club LILLE = new Club(5, "LOSC Lille", 1886, STADIUM, "Villeneuve-d'Ascq", LEAGUE_1,
            new Glory(0, 3, 6, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club MONTPELLIER = new Club(6, "Montpellier HSC", 1886, STADIUM, "Montpellier", LEAGUE_1,
            new Glory(0, 1, 2, 1, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club NICE = new Club(7, "OGC Nice", 1878, STADIUM, "Nice", LEAGUE_1,
            new Glory(0, 4, 3, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club REIMS = new Club(8, "Stade de Reims", 1886, STADIUM, "Reims", LEAGUE_1,
            new Glory(0, 6, 2, 1, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club NIMES = new Club(9, "Nîmes Olympique", 1886, STADIUM, "Nimes", LEAGUE_1,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club RENNES = new Club(10, "Stade Rennais FC", 1886, STADIUM, "Rennes", LEAGUE_1,
            new Glory(0, 0, 3, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club STRASBOURG = new Club(11, "RC Strasbourg Alsace", 1886, STADIUM, "Strasbourg", LEAGUE_1,
            new Glory(0, 1, 3, 4, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club NANTES = new Club(12, "FC Nantes", 1886, STADIUM, "Nantes", LEAGUE_1,
            new Glory(0, 8, 3, 1, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club BORDEAUX = new Club(13, "FC Girondins de Bordeaux", 1886, STADIUM, "Bordeaux", LEAGUE_1,
            new Glory(0, 6, 4, 3, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club ANGERS = new Club(14, "Angers SCO", 1886, STADIUM, "Angers", LEAGUE_1,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club AMIENS = new Club(15, "Amiens SC", 1886, STADIUM, "Amiens", LEAGUE_1,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club TOULOUSE = new Club(16, "Toulouse Football Club", 1886, STADIUM, "Toulouse", LEAGUE_1,
            new Glory(0, 0, 1, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club MONACO = new Club(17, "AS Monaco", 1886, STADIUM, "Monaco", LEAGUE_1,
            new Glory(0, 8, 5, 1, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club DIJON = new Club(18, "Dijon FCO", 1886, STADIUM, "Dijon", LEAGUE_1,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club CAEN = new Club(19, "Stade Malherbe Caen", 1886, STADIUM, "Caen", LEAGUE_1,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    private static Club GUINGAMP = new Club(20, "En Avant de Guingamp", 1886, STADIUM, "Guingamp", LEAGUE_1,
            new Glory(0, 0, 2, 0, 0, 0, 0),
            100, 100, 100, OWNER, COACH, new HashSet<>(), new Season(
            new League(0, 0, 0, 0, 0, 0, 0, 0),
            new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

    static Club[] CLUBS = {LYON, PSG, MARSEILLE, SAINT_ETIENNE, LILLE, MONTPELLIER, NICE, REIMS,
            NIMES, RENNES, STRASBOURG, NANTES, BORDEAUX, ANGERS, AMIENS, TOULOUSE, MONACO,
            DIJON, CAEN, GUINGAMP};
}
