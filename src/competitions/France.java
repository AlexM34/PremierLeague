package competitions;

import players.Glory;
import teams.Club;
import teams.Coach;
import teams.Formation;
import teams.Stadium;
import teams.Owner;

public class France {
    public static final String LEAGUE = "Ligue 1";
    private static final Stadium STADIUM = new Stadium(1, "", 1900, "", 50000, 100);
    private static final Owner OWNER = new Owner(1, "", "", 100, 100);
    private static final Coach COACH = new Coach(1, "", 80, Formation.F5, 50, 50, 50);
    
    private static final Club LYON = new Club(1, "Olympique Lyonnais", 1899, STADIUM, "Decines-Charpieu", LEAGUE,
            new Glory(0, 7, 5, 1, 0, 0, 0),
            100, 100, 60, OWNER, COACH);

    private static final Club PSG = new Club(2, "Paris Saint-Germain", 1902, STADIUM, "Paris", LEAGUE,
            new Glory(0, 8, 12, 8, 0, 0, 0),
            100, 100, 160, OWNER, COACH);

    private static final Club MARSEILLE = new Club(3, "Olympique de Marseille", 1892, STADIUM, "Marseille", LEAGUE,
            new Glory(1, 9, 10, 3, 0, 0, 0),
            100, 100, 50, OWNER, COACH);

    private static final Club SAINT_ETIENNE = new Club(4, "AS Saint-Étienne", 1878, STADIUM, "Saint-Etienne", LEAGUE,
            new Glory(0, 10, 6, 1, 0, 0, 0),
            100, 100, 30, OWNER, COACH);

    private static final Club LILLE = new Club(5, "LOSC Lille", 1886, STADIUM, "Villeneuve-d'Ascq", LEAGUE,
            new Glory(0, 3, 6, 0, 0, 0, 0),
            100, 100, 35, OWNER, COACH);

    private static final Club MONTPELLIER = new Club(6, "Montpellier HSC", 1886, STADIUM, "Montpellier", LEAGUE,
            new Glory(0, 1, 2, 1, 0, 0, 0),
            100, 100, 25, OWNER, COACH);

    private static final Club NICE = new Club(7, "OGC Nice", 1878, STADIUM, "Nice", LEAGUE,
            new Glory(0, 4, 3, 0, 0, 0, 0),
            100, 100, 28, OWNER, COACH);

    private static final Club REIMS = new Club(8, "Stade de Reims", 1886, STADIUM, "Reims", LEAGUE,
            new Glory(0, 6, 2, 1, 0, 0, 0),
            100, 100, 18, OWNER, COACH);

    private static final Club NIMES = new Club(9, "Nîmes Olympique", 1886, STADIUM, "Nimes", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 12, OWNER, COACH);

    private static final Club RENNES = new Club(10, "Stade Rennais FC", 1886, STADIUM, "Rennes", LEAGUE,
            new Glory(0, 0, 3, 0, 0, 0, 0),
            100, 100, 17, OWNER, COACH);

    private static final Club STRASBOURG = new Club(11, "RC Strasbourg Alsace", 1886, STADIUM, "Strasbourg", LEAGUE,
            new Glory(0, 1, 3, 4, 0, 0, 0),
            100, 100, 13, OWNER, COACH);

    private static final Club NANTES = new Club(12, "FC Nantes", 1886, STADIUM, "Nantes", LEAGUE,
            new Glory(0, 8, 3, 1, 0, 0, 0),
            100, 100, 20, OWNER, COACH);

    private static final Club BORDEAUX = new Club(13, "FC Girondins de Bordeaux", 1886, STADIUM, "Bordeaux", LEAGUE,
            new Glory(0, 6, 4, 3, 0, 0, 0),
            100, 100, 16, OWNER, COACH);

    private static final Club ANGERS = new Club(14, "Angers SCO", 1886, STADIUM, "Angers", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 11, OWNER, COACH);

    private static final Club AMIENS = new Club(15, "Amiens SC", 1886, STADIUM, "Amiens", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 9, OWNER, COACH);

    private static final Club TOULOUSE = new Club(16, "Toulouse Football Club", 1886, STADIUM, "Toulouse", LEAGUE,
            new Glory(0, 0, 1, 0, 0, 0, 0),
            100, 100, 15, OWNER, COACH);

    private static final Club MONACO = new Club(17, "AS Monaco", 1886, STADIUM, "Monaco", LEAGUE,
            new Glory(0, 8, 5, 1, 0, 0, 0),
            100, 100, 30, OWNER, COACH);

    private static final Club DIJON = new Club(18, "Dijon FCO", 1886, STADIUM, "Dijon", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 10, OWNER, COACH);

    private static final Club BREST = new Club(19, "Stade Brestois 29", 1886, STADIUM, "Brest", LEAGUE,
            new Glory(0, 0, 0, 0, 0, 0, 0),
            100, 100, 8, OWNER, COACH);

    private static final Club METZ = new Club(20, "FC Metz", 1886, STADIUM, "Metz", LEAGUE,
            new Glory(0, 0, 2, 2, 0, 0, 0),
            100, 100, 7, OWNER, COACH);

    public static final Club[] CLUBS = {LYON, PSG, MARSEILLE, SAINT_ETIENNE, LILLE, MONTPELLIER, NICE, REIMS,
            NIMES, RENNES, STRASBOURG, NANTES, BORDEAUX, ANGERS, AMIENS, TOULOUSE, MONACO,
            DIJON, BREST, METZ};
}
