package simulation;

import competitions.*;
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
import java.util.Scanner;

import static simulation.PremierLeague.pause;

class Data {
    static final Club[][] LEAGUES = {England.CLUBS, Spain.CLUBS, Italy.CLUBS, France.CLUBS, Germany.CLUBS};
    static Club[] CHAMPIONS_LEAGUE = new Club[32];
    static int FANS = 3;
    static int HOME_WINS;
    static int AWAY_WINS;
    static float RATINGS;
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
        final String fileName = "data/csv";
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
                        final String[] values = footballer.split(",");

                        if (club.getName().equals(values[9])) {
                            club.addFootballer(new Footballer(Integer.parseInt(values[0]), values[2], Integer.parseInt(values[3]), values[5],
                                    Integer.parseInt(values[7]), Integer.parseInt(values[8]),
                                    values[11].substring(1, values[11].length() - 1).length() > 0 ? Float.parseFloat(values[11].substring(1, values[11].length() - 1)) : 0,
                                    values[11].substring(1, values[11].length() - 1).length() > 0 ? Float.parseFloat(values[12].substring(1, values[12].length() - 1)) : 0,
                                    Position.valueOf(values[21]), Integer.parseInt(values[22]),
                                    Integer.parseInt(values[56]), Integer.parseInt(values[78]),
                                    new Resume()));
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
        pause();

        HOME_WINS = 0;
        AWAY_WINS = 0;
        RATINGS = 0;

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
        total.addYellowCards(season.getYellowCards());
        total.addRedCards(season.getRedCards());
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
