import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

class Data {
    // TODO: Put spaces for teams and players
    static String[] TEAMS = {"Arsenal", "ManchesterCity", "Liverpool", "ManchesterUnited",
            "Chelsea", "TottenhamHotspur", "Everton", "LeicesterCity", "WolverhamptonWanderers", "Watford",
            "WestHamUnited", "Bournemouth", "CrystalPalace", "Burnley", "NewcastleUnited", "Southampton",
            "Brighton&HoveAlbion", "CardiffCity", "Fulham", "HuddersfieldTown"};

    // TODO: Add logos
    // TODO: Club stats
    // TODO: Coaches and formations
    // TODO: Coach decisions
    // TODO: Add fatigue
    static Map<String, List<Footballer>> SQUADS = new HashMap<>();
    static Integer USER = -1;
    static int OFFENSE;
    static int[] TITLES = {13, 5, 18, 20, 6, 2, 9, 1, 3, 0, 0, 0, 0, 2, 4, 0, 0, 0, 0, 3};
    static int[] POINTS = new int[20];
    static int[] GOALS_FOR = new int[20];
    static int[] GOALS_AGAINST = new int[20];
    static int[] GAMES = new int[20];
    static int[] WINS = new int[20];
    static int[] DRAWS = new int[20];
    static int[] LOSES = new int[20];
    static int[][] HOME = new int[38][10];
    static int[][] AWAY = new int[38][10];
    // TODO: Explore fan factor
    // TODO: Home and away records
    static int FANS = 5;
    static int[] FORM = new int[20];
    static float[][] RATINGS = new float[20][11];
    static int[][] MOTM = new int[20][11];
    static int[][] GOALS = new int[20][11];
    static int[][] ASSISTS = new int[20][11];
    static int[] CLEAN_SHEETS = new int[20];

    static void extractData() {
        String fileName = "data/data.csv";
        File data = new File(fileName);
        try {
            for (int team = 0; team < 20; team++) {
                // TODO: Make just one pass through the data
                File file = new File("data/" + Data.TEAMS[team] + ".csv");
                file.delete();
                file.createNewFile();
                FileWriter write = new FileWriter(file, true);
                PrintWriter line = new PrintWriter(write);

//                int count = 0;
                Scanner inputStream = new Scanner(data);
                inputStream.next();

                List<Footballer> footballers = new ArrayList<>();
                while (inputStream.hasNext()) {
                    String footballer = inputStream.next();
                    String[] values = footballer.split(",");
                    if (Data.TEAMS[team].equals(values[9])) {
                        line.printf(footballer + "\n");
                    }
                }
                inputStream.close();
                line.close();
            }
        } catch (IOException e) {
            System.out.println("Exception thrown with extracting!");
        }
    }

    static void buildSquads() {
        try {
            for (int team = 0; team < 20; team++) {
                File data = new File("data/" + Data.TEAMS[team] + ".csv");

                Scanner inputStream = new Scanner(data);

                List<Footballer> footballers = new ArrayList<>();
                while (inputStream.hasNext()) {
                    // TODO: Handle nulls
                    String footballer = inputStream.next();
                    String[] values = footballer.split(",");
                    if (Data.TEAMS[team].equals(values[9])) {
                        footballers.add(new Footballer(values[2], Integer.parseInt(values[3]), values[5],
                                Integer.parseInt(values[7]), Integer.parseInt(values[8]),
                                Double.parseDouble(values[11].substring(1, values[11].length() - 1)),
                                Double.parseDouble(values[12].substring(1, values[12].length() - 1)),
                                values.length > 21 && !values[21].isEmpty() ? Position.valueOf(values[21]) : null,
                                values.length > 22 && !values[22].isEmpty() ? Integer.parseInt(values[22]) : 0,
                                values.length > 56 && !values[56].isEmpty() ? Integer.parseInt(values[56]) : 0,
                                values.length > 78 && !values[78].isEmpty() ? Integer.parseInt(values[78]) : 0));
                    }
                }

                SQUADS.put(TEAMS[team], footballers);
            }
        } catch (IOException e) {
            System.out.println("Exception thrown while building squads!");
        }
    }

    static void prepare(int year) {
        POINTS = new int[20];
        GOALS_FOR = new int[20];
        GOALS_AGAINST = new int[20];
        GAMES = new int[20];
        WINS = new int[20];
        DRAWS = new int[20];
        LOSES = new int[20];
        HOME = new int[38][10];
        AWAY = new int[38][10];
        FORM = new int[20];
        RATINGS = new float[20][11];
        MOTM = new int[20][11];
        GOALS = new int[20][11];
        ASSISTS = new int[20][11];
        CLEAN_SHEETS = new int[20];

        // TODO: Title odds
        System.out.println(String.format("The Premier League %d-%d begins!", 2018 + year, 2019 + year));
        IntStream.range(0, 20).forEach(team -> FORM[team] = 10);

//        System.out.println();
//        for (int team = 0; team < 20; team++) {
//            for (int player = 0; player < 11; player++) {
//                System.out.println(String.format("%s %s", PLAYERS[team][player],
//                        OVERALL[team][player]));
//            }
//        }

        System.out.println();
        PremierLeague.pause();
    }
}
