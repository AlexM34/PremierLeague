import java.io.IOException;
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
            Draw.makeDraw();

            IntStream.range(0, 38).forEach(PremierLeague::play);

            Club nationalCupWinner = cup(Data.LEAGUES[0], 16);
            System.out.println(nationalCupWinner.getName() + " win the National Cup!");
            nationalCupWinner.getGlory().addNationalCup();

            Club leagueCupWinner = cup(Data.LEAGUES[0], 16);
            System.out.println(leagueCupWinner.getName() + " win the League Cup!");
            leagueCupWinner.getGlory().addLeagueCup();
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

    private static void play(int round) {
//        pause();
        System.out.println(String.format("Round %d", round + 1));
        for (int game = 0; game < 10; game++) {
            int home = Data.HOME[round][game];
            int away = Data.AWAY[round][game];
            if (home == Data.USER) Match.userTactics(away, true);
            else if (away == Data.USER) Match.userTactics(home, false);
            Match.leagueSimulation(home, away);
            if (home == Data.USER || away == Data.USER) pause();
        }

        System.out.println();
        if (round < 37) {
            System.out.println(String.format("Standings after round %d:", round + 1));
            Printer.printStandings();
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
                int result = Match.cupSimulation(selected[team].getId() - 1, selected[count - team - 1].getId() - 1);
                System.out.println(String.format("%s - %s %d:%d", Data.DRAW.get(selected[team].getId() - 1).getName(),
                        Data.DRAW.get(selected[count - team - 1].getId() - 1).getName(), result / 100,
                        result % 100));
                if (result / 100 <= result % 100) {
                    selected[team] = selected[count - team - 1];
                }
            }

            count /= 2;
        }

        return selected[0];
    }

    private static void finish(int year) {
        Printer.printPlayerStats();
        System.out.println("FINAL STANDINGS");
        Printer.printStandings();
        Printer.printAllTimeStats();
        System.out.println();
        // TODO: Rate simulation with review
        System.out.println(String.format("The Premier League %d-%d ends!", 2019 + year, 2020 + year));
    }

    private static void finishSimulation() {
        Data.prepare(10);
        Printer.printAllTimePlayerStats();
    }
}
