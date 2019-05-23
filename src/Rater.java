import java.util.Map;
import java.util.Random;

class Rater {
    private static Random random = new Random();

    static float[] homeRatings = new float[11];
    static float[] awayRatings = new float[11];

    static void kickoff() {
        for (int player = 0; player < 11; player++) {
            Rater.homeRatings[player] = 6;
            Rater.awayRatings[player] = 6;
        }
    }
    static void updateRatings(int scale) {
        int home = random.nextInt(7) + scale * 2 + 1;
        int away = random.nextInt(7) - scale * 2 + 1;

        for (int i = 0; i < home; i++) {
            int player = random.nextInt(11);
            homeRatings[player] += 0.1;
        }

        for (int i = 0; i < away; i++) {
            int player = random.nextInt(11);
            awayRatings[player] += 0.1;
        }
    }

    static void goal(int minute, boolean isHome) {
        int scoring = 20;
        int assisting = 150;
        Footballer goalscorer = null;
        Footballer assistmaker = null;
        Footballer[] squad = isHome ? Match.homeSquad : Match.awaySquad;

        for (int player = 0; player < 11; player++) {
            // TODO: Own goals
            scoring += scoringChance(squad[player]);
            assisting += assistingChance(squad[player]);
        }

        int r = random.nextInt(scoring);
        for (int player = 0; player < 11; player++) {
            r -= scoringChance(squad[player]);
            if (r < 0) {
                goalscorer = squad[player];

                if (isHome) {
                    homeRatings[player] += 1.25;
                    if (player < 5) homeRatings[player] += 0.5;
                    else if (player < 8) homeRatings[player] += 0.25;
                }
                else {
                    awayRatings[player] += 1.25;
                    if (player < 5) awayRatings[player] += 0.5;
                    else if (player < 8) awayRatings[player] += 0.25;
                }

                squad[player].getResume().getSeason().addGoals(1);
                break;
            }
        }

        r = random.nextInt(assisting);
        for (int player = 0; player < 11; player++) {
            r -= assistingChance(squad[player]);
            if (r < 0) {
                assistmaker = squad[player];
                if (assistmaker.equals(goalscorer)) {
                    assistmaker = null;
                }
                else {
                    if (isHome) {
                        homeRatings[player] += 1;
                        if (player < 5) homeRatings[player] += 0.5;
                        else if (player < 8) homeRatings[player] += 0.25;
                    }
                    else {
                        awayRatings[player] += 1;
                        if (player < 5) awayRatings[player] += 0.5;
                        else if (player < 8) awayRatings[player] += 0.25;
                    }

                    squad[player].getResume().getSeason().addAssists(1);
                }

                break;
            }
        }

        System.out.println(minute + "' " + (goalscorer != null ? (goalscorer.getName() +
                (assistmaker != null ? " scores after a pass from " + assistmaker.getName()
                        : " scores after a solo run")) : "own goal"));
    }

    private static int scoringChance(Footballer footballer) {
        switch (footballer.getPosition().getRole()) {
            case Goalkeeper:
                return 0;
            case Defender:
                return footballer.getFinishing() * 2;
            case Midfielder:
                return footballer.getFinishing() * 5;
            case Forward:
                return footballer.getFinishing() * 10;
        }

        return 0;
    }

    private static int assistingChance(Footballer footballer) {
        switch (footballer.getPosition().getRole()) {
            case Goalkeeper:
                return footballer.getVision();
            case Defender:
                return footballer.getVision() * 2;
            case Midfielder:
                return footballer.getVision() * 5;
            case Forward:
                return footballer.getVision() * 10;
        }

        return 0;
    }

    static void finalWhistle(int home, int away, int homeGoals, int awayGoals) {
        boolean motmHomeTeam = true;
        int motmPlayer = -1;
        float motmRating = 0;
        Match.homeSquad[0].getResume().getSeason().addCleanSheets(awayGoals == 0 ? 1 : 0);
        Match.awaySquad[0].getResume().getSeason().addCleanSheets(homeGoals == 0 ? 1 : 0);

        for (int player = 0; player < 11; player++) {
            homeRatings[player] = homeRatings[player] < 10 ? homeRatings[player] : 10;
            homeRatings[player] = homeRatings[player] > 4 ? homeRatings[player] : 4;
            if (homeRatings[player] > motmRating) {
                motmHomeTeam = true;
                motmPlayer = player;
                motmRating = homeRatings[player];
            }
            Match.homeSquad[player].getResume().getSeason().addRating((int) homeRatings[player] * 100, 1);
            Match.homeSquad[player].getResume().getSeason().addMatches(1);

            awayRatings[player] = awayRatings[player] < 10 ? awayRatings[player] : 10;
            awayRatings[player] = awayRatings[player] > 4 ? awayRatings[player] : 4;
            if (awayRatings[player] > motmRating) {
                motmHomeTeam = false;
                motmPlayer = player;
                motmRating = awayRatings[player];
            }
            Match.awaySquad[player].getResume().getSeason().addRating((int) awayRatings[player] * 100, 1);
            Match.awaySquad[player].getResume().getSeason().addMatches(1);
        }

        (motmHomeTeam ? Match.homeSquad : Match.awaySquad)[motmPlayer].getResume().getSeason().addMotmAwards(1);


        if (awayGoals == 0) {
            Data.CLEAN_SHEETS[home]++;
        }

        if (homeGoals == 0) {
            Data.CLEAN_SHEETS[away]++;
        }

        if (homeGoals > awayGoals) {
            Data.HOME_WINS++;
            Data.POINTS[home] += 3;
            Data.WINS[home]++;
            Data.LOSES[away]++;

            if (homeGoals - awayGoals > 2) {
                form(home, 1);
                form(away, -1);
            }

            if (Data.FORM[home] < Data.FORM[away] + 10) {
                if (Data.FORM[home] < Data.FORM[away]) {
                    form(home, 3);
                    form(away, -3);
                }
                else {
                    form(home, 1);
                    form(away, -1);
                }
            }
        }

        else if (homeGoals < awayGoals){
            Data.AWAY_WINS++;
            Data.POINTS[away] += 3;
            Data.WINS[away]++;
            Data.LOSES[home]++;

            if (awayGoals - homeGoals > 2) {
                form(away, 1);
                form(home, -1);
            }

            if (Data.FORM[away] < Data.FORM[home] + 10) {
                if (Data.FORM[away] < Data.FORM[home]) {
                    form(away, 3);
                    form(home, -3);
                }
                else {
                    form(away, 1);
                    form(home, -1);
                }
            }
        }
        else {
            Data.POINTS[home]++;
            Data.POINTS[away]++;
            Data.DRAWS[home]++;
            Data.DRAWS[away]++;

            if (Data.FORM[home] < Data.FORM[away]) {
                form(home, 2);
                form(away, -2);
            }
            else if (Data.FORM[away] < Data.FORM[home]) {
                form(away, 2);
                form(home, -2);
            }
        }

        Data.GAMES[home]++;
        Data.GAMES[away]++;
        Data.GOALS_FOR[home] += homeGoals;
        Data.GOALS_AGAINST[home] += awayGoals;
        Data.GOALS_FOR[away] += awayGoals;
        Data.GOALS_AGAINST[away] += homeGoals;

        int motmId = (motmHomeTeam ? Match.homeSquad : Match.awaySquad) [motmPlayer].getId();

        System.out.println(String.format("%s - %s %d:%d   --- %s %.2f", Data.TEAMS[home], Data.TEAMS[away], homeGoals,
                awayGoals, Data.SQUADS.get(Data.TEAMS[motmHomeTeam ? home : away]).stream().filter(
                        f -> f.getId() == motmId).findFirst().get(), motmRating));
    }

    private static void form(int team, int change) {
        if (change > 0) {
            Data.FORM[team] = Data.FORM[team] < 21 - change ? Data.FORM[team] + change : 20;
        }
        else {
            Data.FORM[team] = Data.FORM[team] > 1 - change ? Data.FORM[team] + change : 0;
        }
    }
}
