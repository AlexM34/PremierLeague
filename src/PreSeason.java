import java.util.Random;

class PreSeason {
    private static Random random = new Random();

    static void changes() {
        // TODO: Transfers
        for (int team = 0; team < 20; team++) {
            for (int player = 0; player < 11; player++) {
                int r = random.nextInt(5);
                if (r == 0) {
                    if (Data.RATINGS[team][player] > 300) {
                        increase(team, player);
                    } else if (Data.RATINGS[team][player] < 250) {
                        decrease(team, player);
                    }
                }
                r = random.nextInt(6);
                if (r == 0) {
                    increase(team, player);
                }
                else if (r == 1){
                    decrease(team, player);
                }
            }
        }
    }

    private static void increase(int team, int player) {
        Data.OVERALL[team][player] = Data.OVERALL[team][player] < 10 ? Data.OVERALL[team][player] + 1 : 10;
//        System.out.println(String.format("%s improves", Data.PLAYERS[team][player]));
    }

    private static void decrease(int team, int player) {
        Data.OVERALL[team][player] = Data.OVERALL[team][player] > 1 ? Data.OVERALL[team][player] - 1 : 1;
//        System.out.println(String.format("%s worsens", Data.PLAYERS[team][player]));
    }
}
