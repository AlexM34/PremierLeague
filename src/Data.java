import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

class Data {
    // TODO: Change length for each team from 15 to 20
    // TODO: Put space for teams and players
    static String[] TEAMS = {"Arsenal", "ManchesterCity", "Liverpool", "ManchesterUnited",
            "Chelsea", "TottenhamHotspur", "Everton", "LeicesterCity", "WolverhamptonWanderers", "Watford",
            "WestHamUnited", "Bournemouth", "CrystalPalace", "Burnley", "NewcastleUnited", "Southampton",
            "Brighton&HoveAlbion", "CardiffCity", "Fulham", "HuddersfieldTown"};

    static String[][] PLAYERS = {
            {"Leno", "Bellerin", "Sokratis", "Koscielny", "Kolasinac",
                    "Torreira", "Ramsey", "Xhaka", "Mkhitaryan", "Lacazette", "Aubameyang"},
            {"Ederson", "Walker", "Kompany", "Laporte", "Mendy",
                    "Fernandinho", "David Silva", "De Bruyne", "Sane", "Sterling", "Aguero"},
            {"Alisson", "Alexander-Arnold", "Lovren", "van Dijk", "Robertson",
                    "Fabinho", "Milner", "Keita", "Mane", "Salah", "Firmino"},
            {"De Gea", "Young", "Bailly", "Lindelof", "Shaw",
                    "Herrera", "Matic", "Pogba", "Lingard", "Rashford", "Lukaku"},
            {"Kepa", "Azpilicueta", "Rudiger", "Luiz", "Alonso",
                    "Kante", "Jorginho", "Barkley", "Willian", "Hazard", "Higuain"},
            {"Lloris", "Trippier", "Alderweireld", "Vertonghen", "Rose",
                    "Dier", "Sissoko", "Eriksen", "Alli", "Son", "Kane"},
            {"Pickford", "Coleman", "Keane", "Zouma", "Digne",
                    "Gueye", "Gomes", "Richarlison", "Bernard", "Sigurdsson", "Calvert-Lewin"},
            {"Schmeichel", "Pereira", "Evans", "Maguire", "Chillwell",
                    "Ndidi", "Tielemans", "Maddison", "Gray", "Barnes", "Vardy"},
            {"Rui Patricio", "Doherty", "Bennett", "Coady", "Boly",
                    "Jonny", "Dendoncker", "Neves", "Moutinho", "Jota", "Jimenez"},
            {"Foster", "Janmaat", "Cathcart", "Kabasele", "Holebas",
                    "Doucoure", "Capoue", "Hughes", "Pereyra", "Deulofeu", "Deeney"},
            {"Fabianski", "Zabaleta", "Diop", "Ogbonna", "Cresswell",
                    "Rice", "Snodgrass", "Noble", "Lanzini", "Felipe Anderson", "Arnautovic"},
            {"Boruc", "Clyne", "Mepham", "Ake", "Smith",
                    "Brooks", "Surman", "Lerma", "Fraser", "King", "Wilson"},
            {"Guaita", "Wan-Bissaka", "Tomkins", "Dann", "van Aanholt",
                    "Milivojevic", "Kouyate", "Schlupp", "Townsend", "Batshuayi", "Zaha"},
            {"Heaton", "Bardsley", "Tarkowski", "Mee", "Taylor",
                    "Gudmundsson", "Westwood", "Cork", "Brady", "Wood", "Barnes"},
            {"Dubravka", "Yedlin", "Schar", "Lascelles", "Dummett",
                    "Ritchie", "Yueng", "Hayden", "Perez", "Almiron", "Rondon"},
            {"Gunn", "Valery", "Bednarek", "Stephens", "Vestergaard",
                    "Targett", "Ward-Prowse", "Romeu", "Hojbjerg", "Redmond", "Ings"},
            {"Ryan", "Montoya", "Duffy", "Dunk", "Bernardo",
                    "Stephens", "Gros", "Propper", "Knockaert", "Jahanbakhsh", "Murray"},
            {"Etheridge", "Peltier", "Morrison", "Manga", "Bennett",
                    "Gunnarsson", "Arter", "Murphy", "Hoilett", "Camarasa", "Niasse"},
            {"Rico", "Odoi", "Nordtveit", "Ream", "Bryan",
                    "McDonald", "Seri", "Babel", "Sessegnon", "Cairney", "Mitrovic"},
            {"Lossl", "Hadergjonaj", "Jorgensen", "Schindler", "Durm",
                    "Hogg", "Bacuna", "Mooy", "Puncheon", "Pritchard", "Mounie"}
    };
    static int[][] OVERALL = {
            {8, 7, 8, 7, 7, 8, 8, 7, 7, 9, 10},
            {9, 8, 8, 9, 7, 9, 9, 10, 9, 9, 10},
            {9, 7, 7, 10, 8, 9, 7, 8, 9, 10, 9},
            {10, 7, 7, 7, 7, 8, 8, 10, 8, 8, 9},
            {8, 8, 7, 8, 8, 10, 8, 7, 8, 10, 9},
            {9, 7, 9, 9, 7, 7, 7, 9, 8, 9, 10},
            {9, 7, 7, 7, 7, 8, 7, 7, 7, 8, 6},
            {8, 7, 7, 7, 7, 7, 7, 7, 6, 6, 7},
            {8, 6, 5, 5, 6, 7, 7, 7, 8, 7, 8},
            {7, 7, 6, 7, 7, 7, 7, 7, 7, 7, 7},
            {8, 6, 7, 7, 6, 6, 7, 6, 7, 8, 7},
            {6, 7, 4, 7, 6, 6, 6, 7, 7, 7, 7},
            {7, 7, 7, 6, 6, 7, 6, 6, 7, 7, 7},
            {7, 4, 7, 7, 6, 7, 6, 6, 6, 7, 7},
            {7, 6, 6, 7, 6, 7, 7, 5, 6, 7, 7},
            {6, 3, 6, 6, 7, 5, 7, 7, 7, 6, 6},
            {7, 7, 7, 7, 6, 6, 7, 7, 6, 7, 7},
            {6, 4, 6, 6, 5, 5, 6, 5, 6, 6, 5},
            {8, 4, 6, 5, 6, 6, 6, 7, 6, 7, 7},
            {6, 6, 6, 7, 6, 6, 5, 7, 5, 6, 6},
    };

    static int[][] SCORING = {
            {0, 1, 1, 1, 1, 1, 3, 3, 4, 7, 10},
            {0, 1, 1, 1, 1, 1, 2, 4, 6, 8, 10},
            {0, 1, 1, 2, 1, 1, 2, 2, 8, 10, 7},
            {0, 1, 1, 1, 1, 2, 1, 7, 5, 7, 9},
            {0, 1, 1, 2, 1, 1, 1, 3, 5, 8, 9},
            {0, 1, 2, 1, 1, 2, 2, 4, 5, 8, 10},
            {0, 1, 1, 1, 1, 1, 2, 6, 5, 7, 6},
            {0, 1, 1, 1, 1, 3, 2, 3, 4, 5, 8},
            {0, 1, 1, 1, 1, 2, 2, 4, 4, 6, 8},
            {0, 1, 1, 1, 1, 2, 2, 3, 4, 7, 7},
            {0, 1, 1, 1, 1, 2, 3, 3, 4, 7, 7},
            {0, 1, 1, 1, 1, 2, 3, 4, 5, 7, 7},
            {0, 1, 1, 1, 1, 2, 2, 3, 5, 7, 8},
            {0, 1, 1, 1, 1, 2, 3, 4, 5, 7, 7},
            {0, 1, 1, 1, 1, 2, 3, 4, 5, 6, 8},
            {0, 1, 1, 1, 1, 2, 4, 4, 4, 6, 7},
            {0, 1, 1, 1, 1, 2, 3, 5, 5, 5, 7},
            {0, 1, 1, 1, 1, 2, 4, 4, 5, 6, 8},
            {0, 1, 1, 1, 1, 2, 2, 4, 6, 5, 9},
            {0, 1, 1, 1, 1, 2, 3, 4, 6, 6, 6},
    };

    static int[][] ASSISTING = {
            {0, 3, 1, 1, 3, 3, 5, 2, 5, 9, 6},
            {0, 3, 1, 1, 2, 3, 8, 9, 8, 8, 5},
            {0, 2, 1, 1, 4, 3, 5, 4, 7, 6, 6},
            {0, 2, 1, 1, 1, 3, 2, 8, 6, 6, 5},
            {0, 2, 1, 2, 3, 2, 4, 4, 6, 10, 5},
            {0, 2, 1, 1, 2, 2, 3, 9, 6, 7, 5},
            {0, 2, 1, 1, 2, 2, 2, 6, 6, 8, 6},
            {0, 2, 1, 1, 2, 2, 2, 5, 7, 7, 5},
            {0, 2, 1, 1, 2, 2, 3, 6, 6, 6, 6},
            {0, 2, 1, 1, 2, 2, 3, 5, 6, 8, 5},
            {0, 2, 1, 1, 2, 2, 3, 4, 6, 8, 7},
            {0, 3, 1, 1, 2, 2, 3, 4, 8, 6, 5},
            {0, 1, 1, 1, 2, 3, 3, 4, 7, 6, 6},
            {0, 2, 1, 1, 2, 2, 3, 5, 5, 6, 6},
            {0, 2, 1, 1, 2, 2, 3, 4, 5, 6, 5},
            {0, 2, 1, 1, 2, 2, 4, 5, 5, 7, 6},
            {0, 2, 1, 1, 2, 2, 3, 3, 6, 5, 5},
            {0, 2, 1, 1, 2, 2, 3, 3, 5, 6, 6},
            {0, 2, 1, 1, 2, 2, 3, 4, 7, 6, 5},
            {0, 2, 1, 1, 2, 2, 3, 5, 7, 6, 5},
    };

    // TODO: Add logos
    // TODO: Club stats
    // TODO: Coaches and formations
    // TODO: Coach decisions
    // TODO: Add fatigue
    static Map<String, List<Footballer>> SQUADS = new HashMap<>();
    static Integer USER;
    static int OFFENSE;
    static int[] SCORING_TOTAL = new int[20];
    static int[] ASSISTING_TOTAL = new int[20];
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
    static int FANS = 7;
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
//                File file = new File("data/" + Data.TEAMS[team] + ".txt");
//                file.delete();
//                file.createNewFile();
//                FileWriter write = new FileWriter(file, true);
//                PrintWriter line = new PrintWriter(write);

                int count = 0;
                Scanner inputStream = new Scanner(data);
                inputStream.next();

                List<Footballer> footballers = new ArrayList<>();
                while (inputStream.hasNext() && count < 5000) {
                    String footballer = inputStream.next();
                    String[] values = footballer.split(",");
                    if (Data.TEAMS[team].equals(values[9])) {
                        // TODO: Handle nulls
                        footballers.add(new Footballer(values[2], Integer.parseInt(values[3]), values[5],
                                Integer.parseInt(values[7]), Integer.parseInt(values[8]),
                                Double.parseDouble(values[11].substring(1, values[11].length() - 1)),
                                Double.parseDouble(values[12].substring(1, values[12].length() - 1)),
                                values.length > 21 && !values[21].isEmpty() ? Position.valueOf(values[21]) : null,
                                values.length > 22 && !values[22].isEmpty() ? Integer.parseInt(values[22]) : 0,
                                values.length > 56 && !values[56].isEmpty() ? Integer.parseInt(values[56]) : 0,
                                values.length > 78 && !values[78].isEmpty() ? Integer.parseInt(values[78]) : 0));
//                        footballers.forEach(f -> System.out.println(f.toString()));
//                        line.printf("%s %s %s %s %s %s %s %s %s %s %s " + "%n", values[2], values[3], values[5],
//                                values[7], values[8], values[11], values[12],
//                                values.length > 21 && !values[21].isEmpty() ? values[21] : "0",
//                                values.length > 22 && !values[22].isEmpty() ? values[22] : "0",
//                                values.length > 55 && !values[55].isEmpty() ? values[55] : "0",
//                                values.length > 77 && !values[77].isEmpty() ? values[77] : "0");
//                        System.out.println(values.length);
                    }
                    count++;
                }

                SQUADS.put(TEAMS[team], footballers);
                inputStream.close();
//                line.close();
            }
        } catch (IOException e) {
            System.out.println("Exception thrown!");
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
        for (int team = 0; team < 20; team++) {
            FORM[team] = 10;
            SCORING_TOTAL[team] = 10 + IntStream.of(SCORING[team]).map(Rater::scoringChance).sum();
            ASSISTING_TOTAL[team] = 50 + IntStream.of(ASSISTING[team]).map(Rater::assistingChance).sum();
            System.out.println(String.format("%s %d %d %d", TEAMS[team], IntStream.of(OVERALL[team]).sum(),
                    SCORING_TOTAL[team], ASSISTING_TOTAL[team]));
        }

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
