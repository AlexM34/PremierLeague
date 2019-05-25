import java.io.*;
import java.util.*;

class Data {
    // TODO: Put spaces for players
    // TODO: Add logos
    // TODO: Remove public tags
    // TODO: Remove setters
    static Club[][] LEAGUES = {England.CLUBS};
    static int[][] HOME = new int[38][10];
    static int[][] AWAY = new int[38][10];
    static Map<Integer, Club> DRAW = new HashMap<>();
    static int FANS = 5;
    static int HOME_WINS;
    static int AWAY_WINS;
    static float RATINGS;
    static Integer USER = -1;
    static int USER_STYLE;

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
                                    Float.parseFloat(values[11].substring(1, values[11].length() - 1)),
                                    Float.parseFloat(values[12].substring(1, values[12].length() - 1)),
                                    Position.valueOf(values[21]), Integer.parseInt(values[22]),
                                    Integer.parseInt(values[56]), Integer.parseInt(values[78]),
                                    new Resume(new Glory(0, 0, 0, 0, 0, 0, 0),
                                            new Statistics(0, 0, 0, 0, 0, 0, 0, 0),
                                            new Statistics(0, 0, 0, 0, 0, 0, 0, 0)), 100));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Exception thrown while building squads!");
        }
    }

    static void prepare(int year) {
        PremierLeague.pause();

        HOME_WINS = 0;
        AWAY_WINS = 0;
        HOME = new int[38][10];
        AWAY = new int[38][10];
        RATINGS = 0;

        // TODO: Title odds
        System.out.println(String.format("The Premier League %d-%d begins!", 2018 + year, 2019 + year));
        int team = 0;
        for (Club[] league : LEAGUES) {
            for (Club club : league) {
                DRAW.put(team++, club);
                club.getSeason().setForm(10);
                club.getSeason().setMorale(100);

                for (Footballer f : club.getFootballers()) {
                    f.getResume().getTotal().addGoals(f.getResume().getSeason().getGoals());
                    f.getResume().getTotal().addAssists(f.getResume().getSeason().getAssists());
                    f.getResume().getTotal().addCleanSheets(f.getResume().getSeason().getCleanSheets());
                    f.getResume().getTotal().addRating(f.getResume().getSeason().getRating(), f.getResume().getSeason().getMatches());
                    f.getResume().getTotal().addMatches(f.getResume().getSeason().getMatches());
                    f.getResume().getTotal().addMotmAwards(f.getResume().getSeason().getMotmAwards());
                    f.getResume().getTotal().addYellowCards(f.getResume().getSeason().getYellowCards());
                    f.getResume().getTotal().addRedCards(f.getResume().getSeason().getRedCards());

                    f.getResume().getSeason().setMatches(0);
                    f.getResume().getSeason().setGoals(0);
                    f.getResume().getSeason().setAssists(0);
                    f.getResume().getSeason().setCleanSheets(0);
                    f.getResume().getSeason().setRating(0);
                    f.getResume().getSeason().setMotmAwards(0);
                    f.getResume().getSeason().setYellowCards(0);
                    f.getResume().getSeason().setRedCards(0);
                }
            }
        }

        System.out.println();
    }
}
