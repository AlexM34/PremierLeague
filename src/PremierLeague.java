import java.util.*;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toMap;

public class PremierLeague {
    private static Random random = new Random();
    private static String[] TEAMS = {"Arsenal", "Manchester City", "Liverpool", "Manchester United",
            "Chelsea", "Tottenham", "Everton", "Leicester", "Wolverhampton", "Watford", "West Ham", "Bournemouth",
            "Crystal Palace", "Burnley", "Newcastle United", "Southampton", "Brighton", "Cardiff", "Fulham", "Huddersfield"};
    private static String[][] PLAYERS = {
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
    private static Integer[][] OVERALL = {
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
    // TODO: Add subs
    // TODO: Add positions
    private static int[] POINTS = new int[20];
    private static int[] GOALS_FOR = new int[20];
    private static int[] GOALS_AGAINST = new int[20];
    private static int[] GAMES = new int[20];
    private static int[] WINS = new int[20];
    private static int[] DRAWS = new int[20];
    private static int[] LOSES = new int[20];
    private static int[] STRENGTH = {83, 96, 93, 87, 85, 84, 71, 67, 62, 60, 57, 55, 51, 47, 46, 42, 37, 34, 29, 22};
    private static int[][] HOME = new int[38][10];
    private static int[][] AWAY = new int[38][10];
    private static int FANS = 7;
    private static int[] FORM = new int[20];

    public static void main(String[] args) {
        prepare();
        makeDraw();

        for (int round = 0; round < 38; round++) {
            // TODO: Ask to continue
            play(round);
        }

        finish();
    }

    private static void prepare() {
        System.out.println("The Premier League begins!");
        for (int i = 0; i < 20; i++) {
            FORM[i] = 10;
            System.out.println(String.format("%s %d", TEAMS[i], STRENGTH[i]));
        }

        System.out.println();
    }

    private static void makeDraw() {
        // TODO: Randomise it
        for (int round = 0; round < 19; round++) {
            int[] current = new int[20];
            current[0] = 0;
            for (int i = 1; i < 20; i++) {
                current[i] = (i + round - 1) % 19 + 1;
            }

            for (int game = 0; game < 10; game++) {
                if (round % 2 == 0) {
                    HOME[round][game] = current[game];
                    AWAY[round][game] = current[19 - game];
                    HOME[19 + round][game] = current[19 - game];
                    AWAY[19 + round][game] = current[game];
                }
                else{
                    HOME[round][game] = current[19 - game];
                    AWAY[round][game] = current[game];
                    HOME[19 + round][game] = current[game];
                    AWAY[19 + round][game] = current[19 - game];
                }
            }
        }
    }

    private static void play(int round) {
        System.out.println(String.format("Round %d", round + 1));
        for (int game = 0; game < 10; game++) {
            simulateGame(HOME[round][game], AWAY[round][game]);
        }

        System.out.println();
        System.out.println(String.format("Standings after round %d:", round + 1));
        if (round < 37) printStandings();
        System.out.println();
    }

    private static void finish() {
        System.out.println("FINAL STANDINGS");
        printStandings();
        // TODO: Player stats
        System.out.println();
        System.out.println("The Premier League ends!");
    }

    private static void simulateGame(int home, int away) {
        int goalsHome = calculateGoals(home, away, true);
        int goalsAway = calculateGoals(away, home, false);
        // TODO: Calculate player ratings
        if (goalsHome > goalsAway) {
            POINTS[home] += 3;
            WINS[home]++;
            LOSES[away]++;

            if (FORM[home] < FORM[away]) {
                form(home, 2);
                form(away, -2);
            }

            if (goalsHome - goalsAway > 2) {
                form(home, 1);
                form(away, -1);
            }

            if (FORM[away] - FORM[home] < 10) {
                form(home, 1);
                form(away, -1);
            }
        }
        else if (goalsHome < goalsAway){
            POINTS[away] += 3;
            WINS[away]++;
            LOSES[home]++;

            if (FORM[away] < FORM[home]) {
                form(away, 2);
                form(home, -2);
            }

            if (goalsAway - goalsHome > 2) {
                form(away, 1);
                form(home, -1);
            }

            if (FORM[away] - FORM[home] < 10) {
                form(away, 1);
                form(home, -1);
            }
        }
        else {
            POINTS[home]++;
            POINTS[away]++;
            DRAWS[home]++;
            DRAWS[away]++;

            if (FORM[home] < FORM[away]) {
                form(home, 2);
                form(away, -2);
            }
            else if (FORM[away] < FORM[home]) {
                form(away, 2);
                form(home, -2);
            }
        }

        GAMES[home]++;
        GAMES[away]++;
        GOALS_FOR[home] += goalsHome;
        GOALS_AGAINST[home] += goalsAway;
        GOALS_FOR[away] += goalsAway;
        GOALS_AGAINST[away] += goalsHome;
        System.out.println(String.format("%s - %s %d:%d", TEAMS[home], TEAMS[away], goalsHome, goalsAway));
    }

    private static void form(int team, int change) {
        if (change > 0) {
            FORM[team] = FORM[team] < 21 - change ? FORM[team] + change : 20;
        }
        else {
            FORM[team] = FORM[team] > 1 - change ? FORM[team] + change : 0;
        }
    }

    private static int calculateGoals(int home, int away, boolean isAtHome) {
        // TODO: Consider fatigue
        // TODO: Fix formula
        int attack = isAtHome ? FANS : 0;
        int defence = isAtHome ? 0 : FANS;
        for (int i = 0; i < 11; i++) {
            if (i < 6) {
                defence += OVERALL[away][i];
            }
            else {
                attack += OVERALL[home][i];
            }
        }

        System.out.println(FORM[home]);
        System.out.println(FORM[away]);
        int average = 30 + 10 * attack - 7 * defence + (FORM[home] - FORM[away]);
        int goals = poisson(average);
        System.out.println(average);
        return goals;
    }

    private static int poisson(int average) {
        double e = 2.74;
        double lambda = (double) average / 100;
        double r = random.nextDouble();
        int goals = 0;
        while (r > 0 && goals < 6) {
            double current = Math.pow(e, -lambda) * Math.pow(lambda, goals) / factorial(goals);
            r -= current;
            goals++;
        }

        return goals-1;
    }

    private static double factorial(int n) {
        return LongStream.rangeClosed(1, n)
                .reduce(1, (long x, long y) -> x * y);
    }

    private static void printStandings() {
        Map<Integer, Integer> standings = new LinkedHashMap<>();
        for (int i = 0; i < 20; i++) {
            standings.put(i, POINTS[i]);
        }

        Map<Integer, Integer> sorted = standings.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        System.out.println("No  Teams                G  W  D  L   GF:GA  P");
        for (int i = 0; i < 20; i++) {
            Integer index = (Integer) sorted.keySet().toArray()[i];
            System.out.println(String.format("%2d. %-20s %-2d %-2d %-2d %-2d %3d:%-3d %-3d", i + 1, TEAMS[index], GAMES[index],
                    WINS[index], DRAWS[index], LOSES[index],
                    GOALS_FOR[index], GOALS_AGAINST[index], POINTS[index]));
        }
    }
}
