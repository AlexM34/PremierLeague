import java.util.stream.IntStream;

public class PremierLeague {
    public static void main(String[] args) {
        IntStream.range(0, 10).forEach(year -> {
            Data.prepare();
            Draw.makeDraw();
            IntStream.range(0, 38).forEach(PremierLeague::play);
            finish();
            PreSeason.changes();
        });
    }

    static void pause() {
        System.out.println("Press Enter to continue");
        try {
            // TODO: Wait for Enter
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

    private static void finish() {
        Printer.printPlayerStats();
        System.out.println("FINAL STANDINGS");
        Printer.printStandings();
        Printer.printAllTimeStats();
        System.out.println();
        System.out.println("The Premier League ends!");
        // TODO: Put year into message
    }
}
