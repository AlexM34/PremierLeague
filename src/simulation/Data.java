package simulation;

import competition.Continental;
import competition.Cup;
import competition.England;
import competition.France;
import competition.Germany;
import competition.Italy;
import competition.Spain;
import players.Footballer;
import players.Position;
import players.Resume;
import players.Statistics;
import team.Club;
import team.League;
import team.Season;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Data {
    static final Club[][] LEAGUES = {England.CLUBS, Spain.CLUBS, Germany.CLUBS, Italy.CLUBS, France.CLUBS};
    public static final Map<String, Integer> homeWins = new HashMap<>();
    public static final Map<String, Integer> draws = new HashMap<>();
    public static final Map<String, Integer> awayWins = new HashMap<>();
    public static final Map<String, Integer> scoredHome = new HashMap<>();
    public static final Map<String, Integer> scoredAway = new HashMap<>();
    public static final Map<String, Integer> assists = new HashMap<>();
    public static final Map<String, Integer> yellowCards = new HashMap<>();
    public static final Map<String, Integer> redCards = new HashMap<>();
    public static final Map<String, Float> averageRatings = new HashMap<>();
    public static final Map<String, Integer> cleanSheets = new HashMap<>();
    static int FANS = 3;
    static Integer USER = -1;
    static int USER_STYLE;

    static final String[] SURNAMES = {"Johnson", "Smith", "Rodriguez", "Traore", "Taylor",  "Pelle",  "Dembele",
            "Alba",  "Benitez", "Sterling",  "Martinez",  "Suarez",  "Jimenez",  "Muller",  "Gross",  "Cole"};

    static final String[] NATIONS = {"France", "England", "Italy", "Spain", "Germany",  "Argentina",  "Brazil",
            "Portugal",  "Netherlands", "Belgium",  "Colombia",  "Uruguay",  "Russia",  "Nigeria",  "Ukraine",  "Austria"};

    static final Footballer GOALKEEPER_1 = new Footballer(123455, "Goalkeeper 1", 18, "", 60, 60,
            "Dummy", 0, 0, Position.GK, 101, 20, 20, new Resume());

    static final Footballer DEFENDER_1 = new Footballer(123456, "Defender 1", 18, "", 60, 60,
            "Dummy", 0, 0, Position.CB, 101, 20, 20, new Resume());

    static final Footballer MIDFIELDER_1 = new Footballer(123457, "Midfielder 1", 18, "", 60, 60,
            "Dummy", 0, 0, Position.CM, 102, 20, 20, new Resume());

    static final Footballer FORWARD_1 = new Footballer(123458, "Forward 1", 18, "", 60, 60,
            "Dummy", 0, 0, Position.ST, 103, 20, 20, new Resume());

    static final Footballer DEFENDER_2 = new Footballer(123466, "Defender 2", 18, "", 60, 60,
            "Dummy", 0, 0, Position.CB, 104, 20, 20, new Resume());

    static final Footballer MIDFIELDER_2 = new Footballer(123467, "Midfielder 2", 18, "", 60, 60,
            "Dummy", 0, 0, Position.CM, 105, 20, 20, new Resume());

    static final Footballer FORWARD_2 = new Footballer(123468, "Forward 2", 18, "", 60, 60,
            "Dummy", 0, 0, Position.ST, 106, 20, 20, new Resume());

    static void extractData() {
        final String fileName = "data/data.csv";
        final File data = new File(fileName);
        try {
            for (final Club[] league : LEAGUES) {
                for (final Club club : league) {
                    final File file = new File("data/" + club.getName() + ".csv");
                    if (!file.delete() || !file.createNewFile()) throw new Exception("Unable to recreate file!");

                    final FileWriter write = new FileWriter(file, true);
                    final PrintWriter line = new PrintWriter(write);
                    final Scanner inputStream = new Scanner(data);
                    inputStream.nextLine();

                    while (inputStream.hasNext()) {
                        final String footballer = inputStream.nextLine();
                        final String[] values = footballer.replaceAll("\\s","").split(",");
                        if (club.getName().replaceAll("\\s","").equals(values[9])) {
                            line.printf(footballer + "\n");
                        }
                    }
                    inputStream.close();
                    line.close();
                }
            }
        } catch (final Exception e) {
            System.out.println("Exception thrown while extracting data! " + e);
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
                        final String[] v = footballer.split("\"");
                        for (int i = 1; i < v.length; i += 2) {
                            v[i] = v[i].replace(",", "|");
                        }

                        final String[] values = String.join("", v).split(",");

                        if (club.getName().equals(values[9])) {
                            final int id = Integer.parseInt(values[0]);
                            final String name = values[2];
                            final int age = Integer.parseInt(values[4]);
                            final String nationality = values[8];
                            final int overall = Integer.parseInt(values[10]);
                            final int potential = Integer.parseInt(values[11]);
                            final long value = Long.parseLong(values[12]);
                            final long wage = Long.parseLong(values[13]);
                            final Position position = Position.valueOf(values[14].split("\\|")[0]);
                            final int number = Integer.parseInt(values[25]);
                            final int finishing = Integer.parseInt(values[45]);
                            final int vision = Integer.parseInt(values[67]);

                            final Footballer f = new Footballer(id, name, age, nationality, overall, potential,
                                    club.getName(), value, wage, position, number, finishing, vision, new Resume());

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

        System.out.println(String.format("Season %d-%d begins!", 2019 + year, 2020 + year));
        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
                club.setSeason(new Season(new League(), new Cup(), new Cup(), new Continental(), 100, 100));

                for (final Footballer f : club.getFootballers()) {
                    final Statistics career = f.getResume().getTotal();
                    final Statistics season = f.getResume().getSeason();

                    career.update(season);
                    season.clear();
                }
            }
        }

        System.out.println();
    }
}
