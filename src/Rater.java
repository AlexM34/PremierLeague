import java.util.Random;

class Rater {
    private static Random random = new Random();

    // TODO: Fix goals, assists and ratings
    static void ratePlayers(int team, int attack, int defence, int goals, boolean isCleanSheet) {
        int goalsRemaining = goals;
        int assistsRemaining = goals;
        int goalscorerChance = Data.SCORING_TOTAL[team];
        int assistChance = Data.ASSISTING_TOTAL[team];

        for (int player = 0; player < 11; player++) {
            float rating = 3 + random.nextFloat() + (float) Data.OVERALL[team][player] / 3;
            for (int goal = 0; goal < goalsRemaining; goal++) {
                int g = random.nextInt(goalscorerChance);
                if (g < scoringChance(Data.SCORING[team][player])) {
                    rating += 1.25;
                    if (player < 5) rating += 0.5;
                    else if (player < 8) rating += 0.25;

                    Data.GOALS[team][player]++;
                    System.out.println(Data.PLAYERS[team][player] + " scores!");
                    goalsRemaining--;
                }
            }
            goalscorerChance -= scoringChance(Data.SCORING[team][player]);

            for (int assist = 0; assist < assistsRemaining; assist++) {
                int a = random.nextInt(assistChance);
                if (a < assistingChance(Data.ASSISTING[team][player])) {
                    rating += 1;
                    if (player < 5) rating += 0.5;
                    else if (player < 8) rating += 0.25;

                    Data.ASSISTS[team][player]++;
                    System.out.println(Data.PLAYERS[team][player] + " assists!");
                    // TODO: Cannot assist yourself
                    assistsRemaining--;
                }
            }
            assistChance -= assistingChance(Data.ASSISTING[team][player]);

            if (player < 6) {
                rating += (float) defence / 4;
            }
            else {
                rating += (float) attack / 5;
            }

            if (isCleanSheet) {
                if (player == 0) rating += 1.25;
                else if (player < 5) rating += 0.75;
                else if (player < 8) rating += 0.25;
            }

            if (rating > 10) {
                rating = 10;
            }
            else if (rating < 0) {
                rating = 0;
            }

            if (Match.motmRating < rating) {
                Match.motmTeam = team;
                Match.motmPlayer = player;
                Match.motmRating = rating;
            }

            Data.RATINGS[team][player] += rating;
            System.out.println(String.format("%s %.2f", Data.PLAYERS[team][player], rating));
        }
    }

    static int scoringChance(int attribute) {
        if (attribute == 0) return 0;
        return attribute * attribute / 2 + 10;
    }

    static int assistingChance(int attribute) {
        if (attribute == 0) return 0;
        return attribute * attribute / 2 + 5;
    }
}
