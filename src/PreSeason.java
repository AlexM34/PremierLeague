import java.util.Random;

class PreSeason {
    private static Random random = new Random();

    static void changes() {
        // TODO: Transfers
        // TODO: Seasonal changes to all players based on ratings
        for (int team = 0; team < 20; team++) {
            for (int player = 0; player < 11; player++) {
                int r = random.nextInt(4);
                if (r == 0) {
                    Data.OVERALL[team][player] = Data.OVERALL[team][player] < 10 ? Data.OVERALL[team][player] + 1 : 10;
                }
                else if (r == 3){
                    Data.OVERALL[team][player] = Data.OVERALL[team][player] > 1 ? Data.OVERALL[team][player] - 1 : 1;
                }
            }
        }
    }
}
