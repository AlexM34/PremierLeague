import java.io.IOException;
import java.util.Scanner;
import java.util.stream.IntStream;

public class PremierLeague {
    private static Scanner scanner = new Scanner(System.in);
    // TODO: Add tests
    // TODO: User selects a team
    // TODO: User bets

    public static void main(String[] args) {
        Data.extractData();
        IntStream.range(0, 0).forEach(year -> {
            Data.prepare(year);
            pickTeam();
            Draw.makeDraw();
            // TODO: FA Cup
            // TODO: Carling Cup
            IntStream.range(0, 38).forEach(PremierLeague::play);
            finish(year);
            PreSeason.changes();
        });
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
            if (Data.USER == home) Match.userTactics(away, true);
            else if (Data.USER == away) Match.userTactics(home, false);
            Match.simulateGame(home, away);
            if (Data.USER == home || Data.USER == away) pause();
        }

        System.out.println();
        if (round < 37) {
            System.out.println(String.format("Standings after round %d:", round + 1));
            Printer.printStandings();
        }
        System.out.println();
    }

    private static void finish(int year) {
        Printer.printPlayerStats();
        System.out.println("FINAL STANDINGS");
        Printer.printStandings();
        Printer.printAllTimeStats();
        System.out.println();
        System.out.println(String.format("The Premier League %d-%d ends!", 2018 + year, 2019 + year));
    }
}
