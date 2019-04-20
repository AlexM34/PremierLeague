import com.sun.javafx.image.IntPixelGetter;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class PremierLeague {
    private static Random random = new Random();
    private static String[] TEAMS = {"Arsenal", "Manchester City", "Liverpool", "Manchester United",
            "Chelsea", "Tottenham", "Everton", "Leicester", "Wolverhampton", "Watford", "West Ham", "Bournemouth",
            "Crystal Palace", "Burnley", "Newcastle", "Southampton", "Brighton", "Cardiff", "Fulham", "Huddersfield"};
    private static int[] POINTS = new int[20];
    private static int[] GOALS_FOR = new int[20];
    private static int[] GOALS_AGAINST = new int[20];
    private static int[] STRENGTH = {83, 96, 93, 87, 85, 84, 71, 67, 62, 60, 57, 55, 51, 47, 46, 42, 37, 34, 29, 22};
    private static int[][] HOME = new int[38][10];
    private static int[][] AWAY = new int[38][10];
    private static int CROWD = 30;

    public static void main(String[] args) {
        prepare();
        makeDraw();

        for (int round = 0; round < 38; round++) {
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
        printStandings();
        System.out.println();
    }

    private static void finish() {
        System.out.println("Final Standings:");
        printStandings();
        System.out.println();
        System.out.println("The Premier League ends!");
    }

    private static void simulateGame(int home, int away) {
        int goalsHome = calculateGoals(STRENGTH[home] + CROWD, STRENGTH[away]);
        int goalsAway = calculateGoals(STRENGTH[away] + CROWD, STRENGTH[home]);
        if (goalsHome > goalsAway) {
            POINTS[home] += 3;
        }
        else if (goalsHome < goalsAway){
            POINTS[away] += 3;
        }
        else {
            POINTS[home]++;
            POINTS[away]++;
        }

        GOALS_FOR[home] += goalsHome;
        GOALS_AGAINST[home] += goalsAway;
        GOALS_FOR[away] += goalsAway;
        GOALS_AGAINST[away] += goalsHome;
        System.out.println(String.format("%s - %s %d:%d", TEAMS[home], TEAMS[away], goalsHome, goalsAway));
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

        for (int i = 0; i < 20; i++) {
            Integer index = (Integer) sorted.keySet().toArray()[i];
            System.out.println(String.format("%d. %s %d:%d %d", i + 1, TEAMS[index],
                    GOALS_FOR[index], GOALS_AGAINST[index], POINTS[index]));
        }

    }

    private static int calculateGoals(int home, int away) {
        int perf = 100 * home / (home + away);
        int goals = 0;
        while (random.nextInt(100) < perf && goals < 5) {
            goals++;
        }
        System.out.println(perf);
        return goals;
    }
}
