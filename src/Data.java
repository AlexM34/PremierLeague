import java.io.*;
import java.util.*;

class Data {
    // TODO: Put spaces for players
    static String[] TEAMS = {"Arsenal", "Manchester City", "Liverpool", "Manchester United",
            "Chelsea", "Tottenham Hotspur", "Everton", "Leicester City", "Wolverhampton Wanderers", "Watford",
            "West Ham United", "Bournemouth", "Crystal Palace", "Burnley", "Newcastle United", "Southampton",
            "Brighton & Hove Albion", "Cardiff City", "Fulham", "Huddersfield Town"};

    static Coach ATTACK = new Coach(1, "Attack", 80, Formation.F6, 80, 80, 50);
    static Coach BALANCE = new Coach(2, "Balance", 80, Formation.F5, 50, 65, 65);
    static Coach DEFENCE = new Coach(3, "Defence", 80, Formation.F4, 20, 50, 80);
    static Coach[] COACHES = {ATTACK, ATTACK, BALANCE, DEFENCE, BALANCE, BALANCE, BALANCE,
    DEFENCE, ATTACK, BALANCE, DEFENCE, BALANCE, DEFENCE, DEFENCE, ATTACK, DEFENCE, DEFENCE, DEFENCE,
    BALANCE, DEFENCE};

    // TODO: Add logos
    // TODO: Club stats
    // TODO: Add fatigue
    static Map<String, Set<Footballer>> SQUADS = new HashMap<>();
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
    static int HOME_WINS;
    static int AWAY_WINS;
    static int FANS = 5;
    static int[] FORM = new int[20];
    static float[][] RATINGS = new float[20][30];
    static int[][] MOTM = new int[20][30];
    static int[][] GOALS = new int[20][30];
    static int[][] ASSISTS = new int[20][30];
    static int[] CLEAN_SHEETS = new int[20];
    static Map<Integer, Map<String, Integer>> STATS = new HashMap<>();

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
                Scanner inputStream = new Scanner(data);
                inputStream.nextLine();

                while (inputStream.hasNext()) {
                    String footballer = inputStream.nextLine();
                    String[] values = footballer.replaceAll("\\s","").split(",");
                    if (Data.TEAMS[team].replaceAll("\\s","").equals(values[9])) {
                        line.printf(footballer + "\n");
                    }
                }
                inputStream.close();
                line.close();
            }
        } catch (IOException e) {
            System.out.println("Exception thrown while extracting data!");
        }
    }

    static void buildSquads() {
        try {
            for (int team = 0; team < 20; team++) {
                File data = new File("data/" + Data.TEAMS[team] + ".csv");

                Scanner inputStream = new Scanner(data);

                Set<Footballer> footballers = new HashSet<>();
                while (inputStream.hasNextLine()) {
                    // TODO: Handle nulls
                    String footballer = inputStream.nextLine();
                    String[] values = footballer.replaceAll("\\s","").split(",");

                    if (Data.TEAMS[team].replaceAll("\\s","").equals(values[9])) {
                        footballers.add(new Footballer(Integer.parseInt(values[0]), values[2], Integer.parseInt(values[3]), values[5],
                                Integer.parseInt(values[7]), Integer.parseInt(values[8]),
                                Float.parseFloat(values[11].substring(1, values[11].length() - 1)),
                                Float.parseFloat(values[12].substring(1, values[12].length() - 1)),
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
        PremierLeague.pause();

        // TODO: All-time player stats
        POINTS = new int[20];
        GOALS_FOR = new int[20];
        GOALS_AGAINST = new int[20];
        GAMES = new int[20];
        WINS = new int[20];
        DRAWS = new int[20];
        LOSES = new int[20];
        HOME_WINS = 0;
        AWAY_WINS = 0;
        HOME = new int[38][10];
        AWAY = new int[38][10];
        FORM = new int[20];
        RATINGS = new float[20][30];
        MOTM = new int[20][30];
        GOALS = new int[20][30];
        ASSISTS = new int[20][30];
        CLEAN_SHEETS = new int[20];

        // TODO: Title odds
        System.out.println(String.format("The Premier League %d-%d begins!", 2018 + year, 2019 + year));
        for (int team = 0; team < 20; team++) {
            FORM[team] = 10;

            for (Footballer f : SQUADS.get(TEAMS[team])) {
                Map<String, Integer> info = new HashMap<>();
                info.put("Team", team);
                info.put("Id", f.getId());
                info.put("Games", 0);
                info.put("Ratings", 0);
                info.put("MOTM", 0);
                info.put("Goals", 0);
                info.put("Assists", 0);
                info.put("Clean Sheets", 0);

                STATS.put(f.getId(), info);
            }
        }

//        System.out.println();
//        for (int team = 0; team < 20; team++) {
//            for (int player = 0; player < 11; player++) {
//                System.out.println(String.format("%s %s", PLAYERS[team][player],
//                        OVERALL[team][player]));
//            }
//        }

        System.out.println();
    }
}
