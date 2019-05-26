import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

class PremierLeague {
    private static Random random = new Random();
    private static Scanner scanner = new Scanner(System.in);
    // TODO: Add tests
    // TODO: User bets

    public static void main(String[] args) {
//        Data.extractData();
        Data.buildSquads();
        IntStream.range(0, 10).forEach(year -> {
            Data.prepare(year);
//            pickTeam();
            Map<Club[], int[][][]> draw = new HashMap<>();
            for (Club[] league : Data.LEAGUES) draw.put(league, Draw.makeDraw(league.length));

            for (int round = 0; round < 38; round ++) {
                for (Club[] league : Data.LEAGUES) {
                    if (round < 34 || league.length > 18) play(league, draw.get(league), round);
                }
            }

            for (Club[] league : Data.LEAGUES) {
                Club nationalCupWinner = cup(league, 16);
                System.out.println(nationalCupWinner.getName() + " win the National Cup!");
                nationalCupWinner.getGlory().addNationalCup();

                if (league[0].getLeague().equals("England") || league[0].getLeague().equals("France")) {
                    Club leagueCupWinner = cup(league, 16);
                    System.out.println(leagueCupWinner.getName() + " win the League Cup!");
                    leagueCupWinner.getGlory().addLeagueCup();
                }
            }

            finish(year);
            PreSeason.changes();
        });

        finishSimulation();
    }

    private static void pickTeam() {
        System.out.println("Pick a team from 1 to 20");
        while (true) {
            int team = scanner.nextInt();
            if(team < 0 || team > 20) {
                System.out.println("Wrong team number.");
                continue;
            }
            Data.USER = team - 1;
            break;
        }
    }

    static void pause() {
        System.out.println("Press any Key to continue");
        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println("Exception thrown!");
        }
    }

    private static void play(Club[] league, int[][][] draw, int round) {
//        pause();
        System.out.println(String.format("Round %d", round + 1));
        for (int game = 0; game < league.length / 2; game++) {
            int home = draw[round][game][0];
            int away = draw[round][game][1];
            if (home == Data.USER) Match.userTactics(league[away], true);
            else if (away == Data.USER) Match.userTactics(league[home], false);
            Match.leagueSimulation(league[home], league[away]);
            if (home == Data.USER || away == Data.USER) pause();
        }

        System.out.println();
        if (round < 37) {
            System.out.println(String.format("Standings after round %d:", round + 1));
            Printer.printStandings(league);
        }
        System.out.println();
    }

    private static Club cup(Club[] league, int teams) {
        int count = 0;
        Club[] selected = new Club[teams];
        boolean[] playing = new boolean[league.length];
        while (count < teams) {
            while (true) {
                int r = random.nextInt(league.length);
                if (!playing[r]) {
                    selected[count++] = league[r];
                    System.out.println(selected[count - 1]);
                    playing[r] = true;
                    break;
                }
            }
        }

        return knockout(selected);
    }

    private static Club knockout(Club[] selected) {
        int count = selected.length;

        for (int round = 1; round <= Math.sqrt(selected.length); round++) {
            System.out.println();
            if (count == 4) System.out.println("Semi-finals");
            else if (count == 2) System.out.println("Final");
            else System.out.println("Round " + round);
            System.out.println();

            for (int team = 0; team < count / 2; team++) {
                // TODO: Cup stats should be separate
                // TODO: Indexing
                int result = Match.cupSimulation(selected[team], selected[count - team - 1]);
                System.out.println(String.format("%s - %s %d:%d", selected[team].getName(),
                        selected[count - team - 1].getName(), result / 100, result % 100));

                if (result / 100 <= result % 100) {
                    selected[team] = selected[count - team - 1];
                }
            }

            count /= 2;
        }

        return selected[0];
    }

    private static void finish(int year) {
        for (Club[] league : Data.LEAGUES) {
            Printer.printPlayerStats(league);
            System.out.println("FINAL STANDINGS");
            Printer.printStandings(league);
            Printer.printAllTimeStats(league);
            System.out.println();
            // TODO: Rate simulation with review
            System.out.println(String.format("Season %d-%d ends!", 2019 + year, 2020 + year));
        }
    }

    private static void finishSimulation() {
        Data.prepare(10);
        for (Club[] league : Data.LEAGUES) Printer.printAllTimePlayerStats(league);
    }
}
