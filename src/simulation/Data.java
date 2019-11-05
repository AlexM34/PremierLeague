package simulation;

import competitions.ChampionsLeague;
import competitions.Cup;
import competitions.England;
import competitions.France;
import competitions.Germany;
import competitions.Italy;
import competitions.Spain;
import players.Competition;
import players.Footballer;
import players.Position;
import players.Resume;
import teams.Club;
import teams.League;
import teams.Season;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Data {
    static final Club[][] LEAGUES = {England.CLUBS, Spain.CLUBS, Germany.CLUBS, Italy.CLUBS, France.CLUBS};
    public static Map<String, Integer> homeWins = new HashMap<>();
    public static Map<String, Integer> draws = new HashMap<>();
    public static Map<String, Integer> awayWins = new HashMap<>();
    public static Map<String, Integer> scoredHome = new HashMap<>();
    public static Map<String, Integer> scoredAway = new HashMap<>();
    public static Map<String, Integer> assists = new HashMap<>();
    public static Map<String, Integer> yellowCards = new HashMap<>();
    public static Map<String, Integer> redCards = new HashMap<>();
    public static Map<String, Float> averageRatings = new HashMap<>();
    public static Map<String, Integer> cleanSheets = new HashMap<>();
    static int FANS = 3;
    static Integer USER = -1;
    static int USER_STYLE;

    static final String[] SURNAMES = {"Johnson", "Smith", "Rodriguez", "Traore", "Taylor",  "Pelle",  "Dembele",
            "Alba",  "Benitez", "Sterling",  "Martinez",  "Suarez",  "Jimenez",  "Muller",  "Gross",  "Cole"};

    static final String[] NATIONS = {"France", "England", "Italy", "Spain", "Germany",  "Argentina",  "Brazil",
            "Portugal",  "Netherlands", "Belgium",  "Colombia",  "Uruguay",  "Russia",  "Nigeria",  "Ukraine",  "Austria"};

    static final Footballer GOALKEEPER_1 = new Footballer(123455, "Goalkeeper 1", 18, "", 60, 60,
            0, 0, Position.GK, 101, 20, 20, new Resume());

    static final Footballer DEFENDER_1 = new Footballer(123456, "Defender 1", 18, "", 60, 60,
            0, 0, Position.CB, 101, 20, 20, new Resume());

    static final Footballer MIDFIELDER_1 = new Footballer(123457, "Midfielder 1", 18, "", 60, 60,
            0, 0, Position.CM, 102, 20, 20, new Resume());

    static final Footballer FORWARD_1 = new Footballer(123458, "Forward 1", 18, "", 60, 60,
            0, 0, Position.ST, 103, 20, 20, new Resume());

    static final Footballer DEFENDER_2 = new Footballer(123466, "Defender 2", 18, "", 60, 60,
            0, 0, Position.CB, 104, 20, 20, new Resume());

    static final Footballer MIDFIELDER_2 = new Footballer(123467, "Midfielder 2", 18, "", 60, 60,
            0, 0, Position.CM, 105, 20, 20, new Resume());

    static final Footballer FORWARD_2 = new Footballer(123468, "Forward 2", 18, "", 60, 60,
            0, 0, Position.ST, 106, 20, 20, new Resume());

    static void extractData() {
        final String fileName = "data/data.csv";
        final File data = new File(fileName);
        try {
            for (final Club[] league : LEAGUES) {
                for (final Club club : league) {
                    final File file = new File("data/" + club.getName() + ".csv");
                    file.delete();
                    file.createNewFile();
                    final FileWriter write = new FileWriter(file, true);
                    final PrintWriter line = new PrintWriter(write);
                    final Scanner inputStream = new Scanner(data);
                    inputStream.nextLine();

                    while (inputStream.hasNext()) {
                        final String footballer = inputStream.nextLine();
                        final String[] values = footballer.replaceAll("\\s","").split(",");
                        if (club.getName().replaceAll("\\s","").equals(values[9]) &&
                                values.length > 78 && !values[21].isEmpty() && !values[22].isEmpty() &&
                                !values[56].isEmpty() && !values[78].isEmpty() ) {
                            line.printf(footballer + "\n");
                        }
                    }
                    inputStream.close();
                    line.close();
                }
            }
        } catch (final IOException e) {
            System.out.println("Exception thrown while extracting data!");
        }
    }

    static void buildSquads() {
        try {
            for (final Club[] league : LEAGUES) {
                for (final Club club : league) {
                    final File data = new File("data/" + club.getName() + ".csv");

                    final Scanner inputStream = new Scanner(data);

                    while (inputStream.hasNextLine()) {
                        final String footballer = inputStream.nextLine();
//                        System.out.println(footballer);
                        final String[] v = footballer.split("\"");
//                        System.out.println(v[1]);
//                        System.out.println(v[3]);
//                        System.out.println(v[5]);
                        for (int i = 1; i < v.length; i += 2) {
                            v[i] = v[i].replace(",", "|");
                        }
//                        System.out.println(v[1]);
//                        System.out.println(v[3]);
//                        System.out.println(v[5]);

                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < v.length; i++) {
//                            System.out.println(v[i]);
                            sb.append(v[i]);
                        }

                        final String ff = sb.toString();
//                        System.out.println(ff);
                        final String[] values = ff.split(",");

//                        System.out.println("values");
                        for (int i = 0; i < values.length; i++) {
//                            System.out.println(values[i]);
                        }

                        if (club.getName().equals(values[9])) {
//                            System.out.println("position " + values[14].split("\\|")[0]);
//                            System.out.println(values[23]);
//                            System.out.println(values[24]);
//                            System.out.println(values[25]);
                            final Footballer f = new Footballer(Integer.parseInt(values[0]), values[2],
                                    Integer.parseInt(values[4]), values[8],
                                    Integer.parseInt(values[10]), Integer.parseInt(values[11]),
                                    Long.parseLong(values[12]), Long.parseLong(values[13]),
                                    Position.valueOf(values[14].split("\\|")[0]), Integer.parseInt(values[25]),
                                    Integer.parseInt(values[45]), Integer.parseInt(values[67]),
                                    new Resume());

//                            System.out.println(f);
                            club.addFootballer(f);
                        }
                    }
                }
            }
        } catch (final Exception e) {
            System.out.println("Exception thrown while building squads! " + e);
        }
    }

    static void addDummies() {
        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
                club.addFootballer(GOALKEEPER_1);
                club.addFootballer(DEFENDER_1);
                club.addFootballer(MIDFIELDER_1);
                club.addFootballer(FORWARD_1);
                club.addFootballer(DEFENDER_2);
                club.addFootballer(MIDFIELDER_2);
                club.addFootballer(FORWARD_2);
            }
        }
    }

    static void prepare(final int year) {
        homeWins.clear();
        draws.clear();
        awayWins.clear();
        scoredHome.clear();
        scoredAway.clear();
        assists.clear();
        yellowCards.clear();
        redCards.clear();
        averageRatings.clear();
        cleanSheets.clear();

        if (year < 10) System.out.println(String.format("Season %d-%d begins!", 2019 + year, 2020 + year));
        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
                club.setSeason(new Season(new League(), new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

                for (final Footballer f : club.getFootballers()) {
                    updateCareerStats(f);
                    clearSeasonStats(f);
                }
            }
        }

        System.out.println();
    }

    private static void updateCareerStats(final Footballer footballer) {
        updateCompetition(footballer.getResume().getTotal().getLeague(), footballer.getResume().getSeason().getLeague());
        updateCompetition(footballer.getResume().getTotal().getCup(), footballer.getResume().getSeason().getCup());
        updateCompetition(footballer.getResume().getTotal().getContinental(), footballer.getResume().getSeason().getContinental());
    }

    private static void clearSeasonStats(final Footballer footballer) {
        clearCompetition(footballer.getResume().getSeason().getLeague());
        clearCompetition(footballer.getResume().getSeason().getCup());
        clearCompetition(footballer.getResume().getSeason().getContinental());
    }

    private static void updateCompetition(final Competition total, final Competition season) {
        total.addMatches(season.getMatches());
        total.addGoals(season.getGoals());
        total.addAssists(season.getAssists());
        total.addCleanSheets(season.getCleanSheets());
        total.addRating(season.getRating(), season.getMatches());
        total.addMotmAwards(season.getMotmAwards());
        total.addYellowCards();
        total.addRedCards();
    }

    private static void clearCompetition(final Competition stats) {
        stats.clearMatches();
        stats.clearGoals();
        stats.clearAssists();
        stats.clearCleanSheets();
        stats.clearRating();
        stats.clearMotmAwards();
        stats.clearYellowCards();
        stats.clearRedCards();
    }
}
