import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toMap;

// TODO: Separate into classes
public class PremierLeague {
    private static Random random = new Random();
    // TODO: Add logos
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
    private static int[][] OVERALL = {
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
    // TODO: Club stats
    // TODO: Age and effect on ratings
    // TODO: Goal and assist stats
    // TODO: Coach decisions
    // TODO: Add subs
    // TODO: Add positions
    // TODO: Add fatigue
    private static int[] TITLES = {13, 5, 18, 20, 6, 2, 9, 1, 3, 0, 0, 0, 0, 2, 4, 0, 0, 0, 0, 3};
    private static int[] POINTS = new int[20];
    private static int[] GOALS_FOR = new int[20];
    private static int[] GOALS_AGAINST = new int[20];
    private static int[] GAMES = new int[20];
    private static int[] WINS = new int[20];
    private static int[] DRAWS = new int[20];
    private static int[] LOSES = new int[20];
//    private static int[] STRENGTH = {83, 96, 93, 87, 85, 84, 71, 67, 62, 60, 57, 55, 51, 47, 46, 42, 37, 34, 29, 22};
    private static int[][] HOME = new int[38][10];
    private static int[][] AWAY = new int[38][10];
    private static int FANS = 7;
    private static int[] FORM = new int[20];
    private static float[][] RATINGS = new float[20][11];
    private static int[][] GOALS = new int[20][11];
    private static int[][] ASSISTS = new int[20][11];
    private static int[] CLEAN_SHEETS = new int[20];

    public static void main(String[] args) {
        for (int year = 0; year < 10; year++) {
            prepare();
            pause();
            makeDraw();
            for (int round = 0; round < 38; round++) {
//                pause();

                play(round);
            }

            finish();
            preSeason();
        }
    }

    private static void pause() {
        System.out.println("Press Enter to continue");
        try {
            System.in.read();
        } catch(Exception e){
            System.out.println("Exception thrown!");
        }
    }

    private static void prepare() {
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
        GOALS = new int[20][11];
        ASSISTS = new int[20][11];
        CLEAN_SHEETS = new int[20];

        System.out.println("The Premier League begins!");
        for (int i = 0; i < 20; i++) {
            FORM[i] = 10;
            System.out.println(String.format("%s %d", TEAMS[i], IntStream.of(OVERALL[i]).sum()));
        }

        System.out.println();
    }

    private static void makeDraw() {
        int[] draw = new int[20];
        boolean[] drawn = new boolean[20];
        for (int team = 0; team < 20; team++) {
            int r = random.nextInt(20 - team);
            int current = 0;

            while (true) {
                if (!drawn[current]) {
                    if (r == 0) {
                        drawn[current] = true;
                        draw[team] = current;
                        break;
                    }

                    r--;
                }

                current++;
            }
        }

        for (int round = 0; round < 19; round++) {
            int[] current = new int[20];
            current[0] = 0;
            for (int i = 1; i < 20; i++) {
                current[i] = (i + round - 1) % 19 + 1;
            }

            for (int game = 0; game < 10; game++) {
                if (round % 2 == 0) {
                    HOME[round][game] = draw[current[game]];
                    AWAY[round][game] = draw[current[19 - game]];
                    HOME[19 + round][game] = draw[current[19 - game]];
                    AWAY[19 + round][game] = draw[current[game]];
                }
                else{
                    HOME[round][game] = draw[current[19 - game]];
                    AWAY[round][game] = draw[current[game]];
                    HOME[19 + round][game] = draw[current[game]];
                    AWAY[19 + round][game] = draw[current[19 - game]];
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
        if (round < 37) {
            System.out.println(String.format("Standings after round %d:", round + 1));
            printStandings();
        }
        System.out.println();
    }

    private static void finish() {
        printPlayerStats();
        System.out.println("FINAL STANDINGS");
        printStandings();
        printAllTimeStats();
        System.out.println();
        System.out.println("The Premier League ends!");
    }

    private static void simulateGame(int home, int away) {
        int goalsHome = calculateGoals(home, away, true);
        int goalsAway = calculateGoals(away, home, false);
        int ratingHomeAttack = goalsHome;
        int ratingHomeDefence = 3 - goalsAway;
        int ratingAwayAttack = goalsAway;
        int ratingAwayDefence = 3 - goalsHome;

        if (goalsHome > goalsAway) {
            POINTS[home] += 3;
            WINS[home]++;
            LOSES[away]++;
            ratingHomeAttack++;
            ratingHomeDefence++;
            ratingAwayAttack--;
            ratingAwayDefence--;

            if (goalsHome > 2) {
                ratingHomeAttack++;
                ratingAwayDefence--;
            }

            if (goalsAway == 0) {
                ratingHomeDefence++;
                ratingAwayAttack--;
                CLEAN_SHEETS[home]++;
            }

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
            ratingAwayAttack++;
            ratingAwayDefence++;
            ratingHomeAttack--;
            ratingHomeDefence--;

            if (goalsAway > 2) {
                ratingAwayAttack++;
                ratingHomeDefence--;
            }

            if (goalsHome == 0) {
                ratingAwayDefence++;
                ratingHomeAttack--;
                CLEAN_SHEETS[away]++;
            }

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

            if (goalsHome > 2) {
                ratingHomeAttack++;
                ratingAwayAttack++;
                ratingHomeDefence--;
                ratingAwayDefence--;
            }

            if (goalsHome == 0) {
                ratingHomeDefence++;
                ratingAwayDefence++;
                ratingHomeAttack--;
                ratingAwayAttack--;
                CLEAN_SHEETS[home]++;
                CLEAN_SHEETS[away]++;
            }

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
        ratePlayers(home, ratingHomeAttack, ratingHomeDefence, goalsHome, goalsAway == 0);
        ratePlayers(away, ratingAwayAttack, ratingAwayDefence, goalsAway, goalsHome == 0);
        System.out.println(String.format("%s - %s %d:%d", TEAMS[home], TEAMS[away], goalsHome, goalsAway));
        // TODO: Print goalscorers and assists per game
        // TODO: Man of the Match award
    }

    private static void form(int team, int change) {
        if (change > 0) {
            FORM[team] = FORM[team] < 21 - change ? FORM[team] + change : 20;
        }
        else {
            FORM[team] = FORM[team] > 1 - change ? FORM[team] + change : 0;
        }
    }

    private static int calculateGoals(int attacking, int defending, boolean isAtHome) {
        int attackValue = isAtHome ? FANS : 0;
        int defenceValue = isAtHome ? 0 : FANS;
        for (int i = 0; i < 11; i++) {
            if (i < 6) {
                defenceValue += OVERALL[defending][i];
            }
            else {
                attackValue += OVERALL[attacking][i];
            }
        }

        //System.out.println(FORM[attacking]);
        //System.out.println(FORM[defending]);
        int average = 30 + 8 * attackValue - 5 * defenceValue + 2 * FORM[attacking];
        int goals = poisson(average);
        //System.out.println(average);
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

    private static void ratePlayers(int team, int attack, int defence, int goals, boolean isCleanSheet) {
        int goalscorerChance = 5;
        int assistChance = 10;
        int goalsRemaining = goals;
        int assistsRemaining = goals;
        int[] goalPtc = new int[11];
        int[] assistPtc = new int[11];

        for (int player = 0; player < 11; player++) {
            if (player == 0) {
                assistPtc[player] = 1;
            }
            else if (player < 5) {
                goalPtc[player] = 1;
                assistPtc[player] = 1;
            }
            else if (player < 7) {
                goalPtc[player] = OVERALL[team][player] / 2;
                assistPtc[player] = OVERALL[team][player];
            }
            else if (player < 10) {
                goalPtc[player] = OVERALL[team][player];
                assistPtc[player] = 3 * OVERALL[team][player] / 2;
            }
            else {
                goalPtc[player] = 3 * OVERALL[team][player] / 2;
                assistPtc[player] = OVERALL[team][player];
            }

            goalscorerChance += goalPtc[player];
            assistChance += assistPtc[player];
        }

        for (int defender = 0; defender < 6; defender++) {
            float r = random.nextFloat();
            float rating = 3 + (float) defence / 4 + (float) OVERALL[team][defender] / 3 + r;
            for (int i = 0; i < goalsRemaining; i++) {
                int g = random.nextInt(goalscorerChance);
                if (g < goalPtc[defender]) {
                    GOALS[team][defender]++;
                    rating += 1.5;
                    goalsRemaining--;
                }
            }
            goalscorerChance -= goalPtc[defender];

            for (int i = 0; i < assistsRemaining; i++) {
                int g = random.nextInt(assistChance);
                if (g < assistPtc[defender]) {
                    ASSISTS[team][defender]++;
                    rating += 1;
                    assistsRemaining--;
                }
            }
            assistChance -= assistPtc[defender];


            if (rating > 10) {
                rating = 10;
            }
            else if (rating < 0) {
                 rating = 0;
            }

            if (isCleanSheet) {
                if (defender == 0) rating++;
                else rating += 0.5;
            }

            RATINGS[team][defender] += rating;
            //System.out.println(String.format("%s %.2f", PLAYERS[team][defender], rating));
        }


        for (int attacker = 6; attacker < 11; attacker++) {
            float r = random.nextFloat();
            float rating = 3 + (float) attack / 4 + (float) OVERALL[team][attacker] / 3 + r;
            for (int i = 0; i < goalsRemaining; i++) {
                int g = random.nextInt(goalscorerChance);
                if (g < goalPtc[attacker]) {
                    GOALS[team][attacker]++;
                    rating += 1.5;
                    goalsRemaining--;
                }
            }
            goalscorerChance -= goalPtc[attacker];

            for (int i = 0; i < assistsRemaining; i++) {
                int g = random.nextInt(assistChance);
                if (g < assistPtc[attacker]) {
                    ASSISTS[team][attacker]++;
                    rating += 1;
                    assistsRemaining--;
                }
            }
            assistChance -= assistPtc[attacker];

            if (rating > 10) {
                rating = 10;
            }
            else if (rating < 0) {
                 rating = 0;
            }

            RATINGS[team][attacker] += rating;
            //System.out.println(String.format("%s %.2f", PLAYERS[team][attacker], rating));
        }
    }

    private static void preSeason() {
        // TODO: Transfers
        // TODO: Seasonal changes to all players based on ratings
        for (int team = 0; team < 20; team++) {
            for (int player = 0; player < 11; player++) {
                int r = random.nextInt(4);
                if (r == 0) {
                    OVERALL[team][player] = OVERALL[team][player] < 10 ? OVERALL[team][player] + 1 : 10;
                }
                else if (r == 3){
                    OVERALL[team][player] = OVERALL[team][player] > 1 ? OVERALL[team][player] - 1 : 1;
                }
            }
        }
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
            System.out.println(String.format("%2d. %-20s %-2d %-2d %-2d %-2d %3d:%-3d %-3d", i + 1,
                    TEAMS[index], GAMES[index], WINS[index], DRAWS[index], LOSES[index],
                    GOALS_FOR[index], GOALS_AGAINST[index], POINTS[index]));
        }
        System.out.println();

        int first = (Integer) sorted.keySet().toArray()[0];

        if (GAMES[first] == 38) {
            TITLES[first]++;
        }

        // TODO: Goal difference tiebreaker
    }

    private static void printAllTimeStats() {
        Map<String, Integer> titles = new LinkedHashMap<>();
        for (int i = 0; i < 20; i++) {
            titles.put(TEAMS[i], TITLES[i]);
        }

        Map<String, Integer> sorted = titles.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        System.out.println();
        System.out.println("Winners");
        for (int i = 0; i < 20; i++) {
            System.out.println(String.format("%2d. %-20s %d", i+1, sorted.keySet().toArray()[i],
                    sorted.values().toArray()[i]));
        }
    }

    private static void printPlayerStats() {
        Map<String, Double> ratings = new LinkedHashMap<>();
        Map<String, Integer> goals = new LinkedHashMap<>();
        Map<String, Integer> assists = new LinkedHashMap<>();
        Map<String, Integer> cleanSheets = new LinkedHashMap<>();

        for (int team = 0; team < 20; team++) {
//            System.out.println();
//            System.out.println(TEAMS[team]);
            cleanSheets.put(PLAYERS[team][0], CLEAN_SHEETS[team]);
            for (int player = 0; player < 11; player++) {
                ratings.put(PLAYERS[team][player], (double) (RATINGS[team][player] / 38));
                goals.put(PLAYERS[team][player], GOALS[team][player]);
                assists.put(PLAYERS[team][player], ASSISTS[team][player]);
//                System.out.println(String.format("%s %.2f %d %d", PLAYERS[team][player], RATINGS[team][player] / 38,
//                        GOALS[team][player], ASSISTS[team][player]));
            }
        }

        Map<String, Double> sortedRatings = ratings.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        Map<String, Integer> sortedGoals = goals.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        Map<String, Integer> sortedAssists = assists.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        Map<String, Integer> sortedCleanSheets = cleanSheets.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        System.out.println();
        System.out.println("Top Players");
        for (int i = 0; i < 20; i++) {
            System.out.println(String.format("%2d. %-15s %.2f", i + 1, sortedRatings.keySet().toArray()[i],
                    sortedRatings.values().toArray()[i]));
        }

        System.out.println();
        System.out.println("Top Goalscorer");
        for (int i = 0; i < 20 && i < sortedGoals.size(); i++) {
            System.out.println(String.format("%2d. %-15s %d", i + 1, sortedGoals.keySet().toArray()[i],
                    sortedGoals.values().toArray()[i]));
        }

        System.out.println();
        System.out.println("Most Assists");
        for (int i = 0; i < 20 && i < sortedAssists.size(); i++) {
            System.out.println(String.format("%2d. %-15s %d", i + 1, sortedAssists.keySet().toArray()[i],
                    sortedAssists.values().toArray()[i]));
        }

        System.out.println();
        System.out.println("Most Clean Sheets");
        for (int i = 0; i < 20 && i < sortedCleanSheets.size(); i++) {
            System.out.println(String.format("%2d. %-15s %2d", i + 1, sortedCleanSheets.keySet().toArray()[i],
                    sortedCleanSheets.values().toArray()[i]));
        }

        System.out.println();
    }
}
