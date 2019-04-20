import java.util.*;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toMap;

public class PremierLeague {
    private static Random random = new Random();
    private static String[] TEAMS = {"Arsenal", "Manchester City", "Liverpool", "Manchester United",
            "Chelsea", "Tottenham", "Everton", "Leicester", "Wolverhampton", "Watford", "West Ham", "Bournemouth",
            "Crystal Palace", "Burnley", "Newcastle", "Southampton", "Brighton", "Cardiff", "Fulham", "Huddersfield"};
    // TODO: Add players and coaches
    // TODO: Add subs
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
    private static int FANS = 30;

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
        int goalsHome = calculateGoals(STRENGTH[home], STRENGTH[away]);
        int goalsAway = calculateGoals(STRENGTH[away], STRENGTH[home]);
        // TODO: Calculate player ratings
        if (goalsHome > goalsAway) {
            POINTS[home] += 3;
            WINS[home]++;
            LOSES[away]++;
        }
        else if (goalsHome < goalsAway){
            POINTS[away] += 3;
            WINS[away]++;
            LOSES[home]++;
        }
        else {
            POINTS[home]++;
            POINTS[away]++;
            DRAWS[home]++;
            DRAWS[away]++;
        }

        GAMES[home]++;
        GAMES[away]++;
        GOALS_FOR[home] += goalsHome;
        GOALS_AGAINST[home] += goalsAway;
        GOALS_FOR[away] += goalsAway;
        GOALS_AGAINST[away] += goalsHome;
        System.out.println(String.format("%s - %s %d:%d", TEAMS[home], TEAMS[away], goalsHome, goalsAway));
    }

    private static int calculateGoals(int home, int away) {
        // TODO: Consider fatigue
        // TODO: Fix formula
        int average = 100 * (home + FANS) / (away + FANS);
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
            //System.out.println(current);
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
