package player;

import java.util.Random;

class Helper {

    static final Random random = new Random();

    static void randomiseCompetition(final Competition competition) {
        final int matches = random.nextInt(31) + 8;
        competition.addRating(random.nextInt(350) + 500, matches);
        competition.addGoals(random.nextInt(matches) / 2);
        competition.addAssists(random.nextInt(matches) / 2);
        competition.addCleanSheets(random.nextInt(matches) / 2);
        competition.addMotmAwards(random.nextInt(matches / 2));
        competition.addYellowCards(random.nextInt(matches / 3));
        competition.addRedCards(random.nextInt(matches / 4));
    }
}
