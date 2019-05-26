import java.io.*;
import java.util.*;

class Data {
    // TODO: Put spaces for players
    // TODO: Add logos
    static Club[][] LEAGUES = {England.CLUBS};//{England.CLUBS, Spain.CLUBS, Italy.CLUBS, France.CLUBS, Germany.CLUBS};
    static int FANS = 5;
    static int HOME_WINS;
    static int AWAY_WINS;
    static float RATINGS;
    static Integer USER = -1;
    static int USER_STYLE;

    static Footballer DEFENDER_1 = new Footballer(123456, "Defender 1", 18, "", 60, 60,
            0, 0, Position.CB, 101, 20, 20, new Resume(new Glory(0, 0, 0, 0, 0, 0, 0),
            new Statistics(0, 0, 0, 0, 0, 0, 0, 0),
            new Statistics(0, 0, 0, 0, 0, 0, 0, 0)), 100);

    static Footballer MIDFIELDER_1 = new Footballer(123457, "Midfielder 1", 18, "", 60, 60,
            0, 0, Position.CM, 102, 20, 20, new Resume(new Glory(0, 0, 0, 0, 0, 0, 0),
            new Statistics(0, 0, 0, 0, 0, 0, 0, 0),
            new Statistics(0, 0, 0, 0, 0, 0, 0, 0)), 100);

    static Footballer FORWARD_1 = new Footballer(123458, "Forward 1", 18, "", 60, 60,
            0, 0, Position.ST, 103, 20, 20, new Resume(new Glory(0, 0, 0, 0, 0, 0, 0),
            new Statistics(0, 0, 0, 0, 0, 0, 0, 0),
            new Statistics(0, 0, 0, 0, 0, 0, 0, 0)), 100);

    static Footballer DEFENDER_2 = new Footballer(123466, "Defender 2", 18, "", 60, 60,
            0, 0, Position.CB, 104, 20, 20, new Resume(new Glory(0, 0, 0, 0, 0, 0, 0),
            new Statistics(0, 0, 0, 0, 0, 0, 0, 0),
            new Statistics(0, 0, 0, 0, 0, 0, 0, 0)), 100);

    static Footballer MIDFIELDER_2 = new Footballer(123467, "Midfielder 2", 18, "", 60, 60,
            0, 0, Position.CM, 105, 20, 20, new Resume(new Glory(0, 0, 0, 0, 0, 0, 0),
            new Statistics(0, 0, 0, 0, 0, 0, 0, 0),
            new Statistics(0, 0, 0, 0, 0, 0, 0, 0)), 100);

    static Footballer FORWARD_2 = new Footballer(123468, "Forward 2", 18, "", 60, 60,
            0, 0, Position.ST, 106, 20, 20, new Resume(new Glory(0, 0, 0, 0, 0, 0, 0),
            new Statistics(0, 0, 0, 0, 0, 0, 0, 0),
            new Statistics(0, 0, 0, 0, 0, 0, 0, 0)), 100);

    static void extractData() {
        String fileName = "data/data.csv";
        File data = new File(fileName);
        try {
            for (Club[] league : LEAGUES) {
                for (Club club : league) {
                    // TODO: Make just one pass through the data
                    File file = new File("data/" + club.getName() + ".csv");
                    file.delete();
                    file.createNewFile();
                    FileWriter write = new FileWriter(file, true);
                    PrintWriter line = new PrintWriter(write);
                    Scanner inputStream = new Scanner(data);
                    inputStream.nextLine();

                    while (inputStream.hasNext()) {
                        String footballer = inputStream.nextLine();
                        String[] values = footballer.replaceAll("\\s","").split(",");
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
        } catch (IOException e) {
            System.out.println("Exception thrown while extracting data!");
        }
    }

    static void buildSquads() {
        try {
            for (Club[] league : LEAGUES) {
                for (Club club : league) {
                    File data = new File("data/" + club.getName() + ".csv");

                    Scanner inputStream = new Scanner(data);

                    while (inputStream.hasNextLine()) {
                        // TODO: Handle nulls
                        String footballer = inputStream.nextLine();
                        String[] values = footballer.replaceAll("\\s", "").split(",");

                        if (club.getName().replaceAll("\\s", "").equals(values[9])) {
                            club.addFootballer(new Footballer(Integer.parseInt(values[0]), values[2], Integer.parseInt(values[3]), values[5],
                                    Integer.parseInt(values[7]), Integer.parseInt(values[8]),
                                    values[11].substring(1, values[11].length() - 1).length() > 0 ? Float.parseFloat(values[11].substring(1, values[11].length() - 1)) : 0,
                                    values[11].substring(1, values[11].length() - 1).length() > 0 ? Float.parseFloat(values[12].substring(1, values[12].length() - 1)) : 0,
                                    Position.valueOf(values[21]), Integer.parseInt(values[22]),
                                    Integer.parseInt(values[56]), Integer.parseInt(values[78]),
                                    new Resume(new Glory(0, 0, 0, 0, 0, 0, 0),
                                            new Statistics(0, 0, 0, 0, 0, 0, 0, 0),
                                            new Statistics(0, 0, 0, 0, 0, 0, 0, 0)), 100));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception thrown while building squads! " + e);
        }
    }

    static void addDummies() {
        for (Club[] league : LEAGUES) {
            for (Club club : league) {
                club.addFootballer(DEFENDER_1);
                club.addFootballer(MIDFIELDER_1);
                club.addFootballer(FORWARD_1);
                club.addFootballer(DEFENDER_2);
                club.addFootballer(MIDFIELDER_2);
                club.addFootballer(FORWARD_2);
            }
        }
    }

    static void prepare(int year) {
        PremierLeague.pause();

        HOME_WINS = 0;
        AWAY_WINS = 0;
        RATINGS = 0;

        // TODO: Title odds
        if (year < 10) System.out.println(String.format("Season %d-%d begins!", 2019 + year, 2020 + year));
        for (Club[] league : LEAGUES) {
            for (Club club : league) {
                club.setSeason(new Season(new League(0, 0, 0, 0, 0, 0, 0,0),
                        new Cup(), new Cup(), new ChampionsLeague(), 100, 100));

                for (Footballer f : club.getFootballers()) {
                    updateCareerStats(f);
                    clearSeasonStats(f);
                }
            }
        }

        System.out.println();
    }

    private static void updateCareerStats(Footballer footballer) {
        footballer.getResume().getTotal().addGoals(footballer.getResume().getSeason().getGoals());
        footballer.getResume().getTotal().addAssists(footballer.getResume().getSeason().getAssists());
        footballer.getResume().getTotal().addCleanSheets(footballer.getResume().getSeason().getCleanSheets());
        footballer.getResume().getTotal().addRating(footballer.getResume().getSeason().getRating(), footballer.getResume().getSeason().getMatches());
        footballer.getResume().getTotal().addMatches(footballer.getResume().getSeason().getMatches());
        footballer.getResume().getTotal().addMotmAwards(footballer.getResume().getSeason().getMotmAwards());
        footballer.getResume().getTotal().addYellowCards(footballer.getResume().getSeason().getYellowCards());
        footballer.getResume().getTotal().addRedCards(footballer.getResume().getSeason().getRedCards());
    }

    private static void clearSeasonStats(Footballer footballer) {
        footballer.getResume().getSeason().setMatches(0);
        footballer.getResume().getSeason().setGoals(0);
        footballer.getResume().getSeason().setAssists(0);
        footballer.getResume().getSeason().setCleanSheets(0);
        footballer.getResume().getSeason().setRating(0);
        footballer.getResume().getSeason().setMotmAwards(0);
        footballer.getResume().getSeason().setYellowCards(0);
        footballer.getResume().getSeason().setRedCards(0);
    }
}
