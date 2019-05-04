import java.util.Random;

class Rater {
    private static Random random = new Random();

    static void ratePlayers(int team, int attack, int defence, int goals, boolean isCleanSheet) {
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
                goalPtc[player] = Data.OVERALL[team][player] / 2;
                assistPtc[player] = Data.OVERALL[team][player];
            }
            else if (player < 10) {
                goalPtc[player] = Data.OVERALL[team][player];
                assistPtc[player] = 3 * Data.OVERALL[team][player] / 2;
            }
            else {
                goalPtc[player] = 3 * Data.OVERALL[team][player] / 2;
                assistPtc[player] = Data.OVERALL[team][player];
            }

            goalscorerChance += goalPtc[player];
            assistChance += assistPtc[player];
        }

        for (int defender = 0; defender < 6; defender++) {
            float r = random.nextFloat();
            float rating = 3 + (float) defence / 4 + (float) Data.OVERALL[team][defender] / 3 + r;
            for (int i = 0; i < goalsRemaining; i++) {
                int g = random.nextInt(goalscorerChance);
                if (g < goalPtc[defender]) {
                    Data.GOALS[team][defender]++;
                    rating += 1.5;
                    goalsRemaining--;
                }
            }
            goalscorerChance -= goalPtc[defender];

            for (int i = 0; i < assistsRemaining; i++) {
                int g = random.nextInt(assistChance);
                if (g < assistPtc[defender]) {
                    Data.ASSISTS[team][defender]++;
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

            Data.RATINGS[team][defender] += rating;
            //System.out.println(String.format("%s %.2f", PLAYERS[team][defender], rating));
        }


        for (int attacker = 6; attacker < 11; attacker++) {
            float r = random.nextFloat();
            float rating = 3 + (float) attack / 4 + (float) Data.OVERALL[team][attacker] / 3 + r;
            for (int i = 0; i < goalsRemaining; i++) {
                int g = random.nextInt(goalscorerChance);
                if (g < goalPtc[attacker]) {
                    Data.GOALS[team][attacker]++;
                    rating += 1.5;
                    goalsRemaining--;
                }
            }
            goalscorerChance -= goalPtc[attacker];

            for (int i = 0; i < assistsRemaining; i++) {
                int g = random.nextInt(assistChance);
                if (g < assistPtc[attacker]) {
                    Data.ASSISTS[team][attacker]++;
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

            Data.RATINGS[team][attacker] += rating;
            //System.out.println(String.format("%s %.2f", PLAYERS[team][attacker], rating));
        }
    }
}
