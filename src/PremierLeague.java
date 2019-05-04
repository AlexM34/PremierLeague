import java.util.stream.IntStream;

public class PremierLeague {
    // TODO: Add tests
    // TODO: Make it playable by user

    public static void main(String[] args) {
        IntStream.range(0, 10).forEach(year -> {
            Data.prepare(year);
            Draw.makeDraw();
            IntStream.range(0, 38).forEach(PremierLeague::play);
            finish(year);
            PreSeason.changes();
        });
    }

    static void pause() {
        System.out.println("Press any Key to continue");
        try {
            System.in.read();
        } catch(Exception e){
            System.out.println("Exception thrown!");
        }
    }

    private static void play(int round) {
//        pause();
        System.out.println(String.format("Round %d", round + 1));
        for (int game = 0; game < 10; game++) {
            Match.simulateGame(Data.HOME[round][game], Data.AWAY[round][game]);
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
