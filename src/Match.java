import java.util.*;

class Match {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    static Footballer[] homeSquad;
    static Footballer[] awaySquad;
    static float[] homeRatings = new float[11];
    static float[] awayRatings = new float[11];

    static void userTactics(int opponent, boolean isHome) {
        // TODO: Add other choices
        System.out.println("vs " + Data.TEAMS[opponent] + (isHome ? " Home" : " Away"));
        System.out.println("Pick how offensive the team should be from 0 to 20");
        while (true) {
            int offense = scanner.nextInt();
            if(offense < 0 || offense > 20) {
                System.out.println("Wrong offense value.");
                continue;
            }
            Data.OFFENSE = offense - 10;
            break;
        }
    }

    static void simulateGame(int home, int away) {
        // TODO: Match odds
        kickoff();
        homeSquad = pickSquad(home);
        awaySquad = pickSquad(away);

        // TODO: Use form and tactics dynamics
        int balance = Data.FANS + 100 *
                (Arrays.stream(homeSquad).mapToInt(Footballer::getOverall).sum() - 500) /
                (Arrays.stream(awaySquad).mapToInt(Footballer::getOverall).sum() - 500) - 50;
        System.out.println(balance);

        int homeGoals = 0;
        int awayGoals = 0;
        for (int minute = 1; minute <= 90; minute++) {
            // TODO: Add stoppage time
            // TODO: Negative ratings
            int r = random.nextInt(100);

            if (r < balance) {
                if (r < balance / 10 - 3) {
                    goal(minute, true);
                    homeGoals++;
                    balance -= 10;
                    updateRatings(3);
                } else if (r < balance / 2) {
                    balance++;
                    updateRatings(1);
                }
            } else {
                if (r > 93 + balance / 10) {
                    goal(minute, false);
                    awayGoals++;
                    balance += 10;
                    updateRatings(-3);
                } else if (r > 100 - balance / 2) {
                    balance--;
                    updateRatings(-1);
                }
            }

            System.out.println(balance);
        }

        finalWhistle(awayGoals == 0, homeGoals == 0);
        // TODO: Form plus match result
//        finalWhistleOld(home, away, homeGoals, awayGoals);
    }

    private static void finalWhistle(boolean homeCleanSheet, boolean awayCleanSheet) {
        boolean motmHomeTeam = true;
        int motmPlayer = -1;
        float motmRating = 0;

        Map<String, Integer> stat = Data.STATS.get(homeSquad[0].getId());
        stat.merge("Clean Sheets", homeCleanSheet ? 1 : 0, Integer::sum);

        stat = Data.STATS.get(awaySquad[0].getId());
        stat.merge("Clean Sheets", awayCleanSheet ? 1 : 0, Integer::sum);

        for (int player = 0; player < 11; player++) {
            stat = Data.STATS.get(homeSquad[player].getId());
            homeRatings[player] = homeRatings[player] < 10 ? homeRatings[player] : 10;
            homeRatings[player] = homeRatings[player] > 4 ? homeRatings[player] : 4;
            if (homeRatings[player] > motmRating) {
                motmHomeTeam = true;
                motmPlayer = player;
                motmRating = homeRatings[player];
            }

            stat.merge("Games", 1, Integer::sum);
            stat.merge("Ratings", (int) homeRatings[player] * 100, Integer::sum);

            stat = Data.STATS.get(awaySquad[player].getId());
            awayRatings[player] = awayRatings[player] < 10 ? awayRatings[player] : 10;
            awayRatings[player] = awayRatings[player] > 4 ? awayRatings[player] : 4;
            if (awayRatings[player] > motmRating) {
                motmHomeTeam = false;
                motmPlayer = player;
                motmRating = awayRatings[player];
            }
            stat.merge("Games", 1, Integer::sum);
            stat.merge("Ratings", (int) awayRatings[player] * 100, Integer::sum);
        }

        stat = Data.STATS.get((motmHomeTeam ? homeSquad : awaySquad) [motmPlayer].getId());
        stat.merge("MOTM", 1, Integer::sum);
    }

    private static void updateRatings(int scale) {
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

    private static void goal(int minute, boolean isHome) {
        int scoring = 20;
        int assisting = 150;
        Footballer goalscorer = null;
        Footballer assistmaker = null;
        Footballer[] squad = isHome ? homeSquad : awaySquad;

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
                Map<String, Integer> stat = Data.STATS.get(squad[player].getId());

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

                stat.merge("Goals", 1, Integer::sum);
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
                    Map<String, Integer> stat = Data.STATS.get(squad[player].getId());
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

                    stat.merge("Assists", 1, Integer::sum);
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

    private static Footballer[] pickSquad(int team) {
        Formation formation = pickFormation(Data.SQUADS.get(Data.TEAMS[team]));
        int defenders = formation.getDefenders();
        int midfielders = formation.getMidfielders();
        int forwards = formation.getForwards();
        Footballer[] selected = new Footballer[11];
        List<Footballer> squad = Data.SQUADS.get(Data.TEAMS[team]);

        int g = 1;
        int d = defenders;
        int m = midfielders;
        int f = forwards;
        for (Footballer footballer : squad) {
            if (footballer.getPosition() == null) {
                continue;
            }

            switch (footballer.getPosition().getRole()) {
                case Goalkeeper:
                    if (g > 0) {
                        g--;
                        selected[g] = footballer;
                    }
                    break;

                case Defender:
                    if (d > 0) {
                        d--;
                        selected[1 + d] = footballer;
                    }
                    break;

                case Midfielder:
                    if (m > 0) {
                        m--;
                        selected[1 + defenders + m] = footballer;
                    }
                    break;

                case Forward:
                    if (f > 0) {
                        f--;
                        selected[1 + defenders + midfielders + f] = footballer;
                    }
                    break;

                default:
                    System.out.println("Unknown position for footballer " + footballer);
                    break;
            }
        }

//        Arrays.stream(selected).forEach(System.out::println);
        return selected;
    }

    private static Formation pickFormation(List<Footballer> footballers) {
        // TODO: Smart formation pick - opponent, fatigue, form
        int defenders = 0;
        int midfielders = 0;
        int forwards = 0;

        for (Footballer f : footballers) {
            switch (f.getPosition().getRole()) {
                case Defender:
                    defenders++;
                    break;
                case Midfielder:
                    midfielders++;
                    break;
                case Forward:
                    forwards++;
                    break;
            }

            if (defenders + midfielders + forwards > 9 &&
                defenders > 2 && midfielders > 1 && forwards > 0) {
                for (Formation formation : Formation.values()) {
                    if (formation.getDefenders() <= defenders &&
                        formation.getMidfielders() <= midfielders &&
                        formation.getForwards() <= forwards) {
                        return formation;
                    }
                }
            }
        }

        System.out.println("Could not pick an appropriate formation");
        return Formation.F5;
    }

    private static void kickoff() {
        for (int player = 0; player < 11; player++) {
            homeRatings[player] = 6;
            awayRatings[player] = 6;
        }
    }

    private static void finalWhistleOld(int home, int away, int goalsHome, int goalsAway) {
        int ratingHomeAttack = goalsHome;
        int ratingHomeDefence = 3 - goalsAway;
        int ratingAwayAttack = goalsAway;
        int ratingAwayDefence = 3 - goalsHome;

        if (goalsHome > 2) {
            ratingHomeAttack++;
            ratingAwayDefence--;
        }

        if (goalsAway == 0) {
            ratingHomeDefence++;
            ratingAwayAttack--;
            Data.CLEAN_SHEETS[home]++;
        }

        if (goalsAway > 2) {
            ratingAwayAttack++;
            ratingHomeDefence--;
        }

        if (goalsHome == 0) {
            ratingAwayDefence++;
            ratingHomeAttack--;
            Data.CLEAN_SHEETS[away]++;
        }

        if (goalsHome > goalsAway) {
            Data.HOME_WINS++;
            Data.POINTS[home] += 3;
            Data.WINS[home]++;
            Data.LOSES[away]++;
            ratingHomeAttack++;
            ratingHomeDefence++;
            ratingAwayAttack--;
            ratingAwayDefence--;

            if (goalsHome - goalsAway > 2) {
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

        else if (goalsHome < goalsAway){
            Data.AWAY_WINS++;
            Data.POINTS[away] += 3;
            Data.WINS[away]++;
            Data.LOSES[home]++;
            ratingAwayAttack++;
            ratingAwayDefence++;
            ratingHomeAttack--;
            ratingHomeDefence--;

            if (goalsAway - goalsHome > 2) {
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
        Data.GOALS_FOR[home] += goalsHome;
        Data.GOALS_AGAINST[home] += goalsAway;
        Data.GOALS_FOR[away] += goalsAway;
        Data.GOALS_AGAINST[away] += goalsHome;

        Rater.ratePlayers(home, ratingHomeAttack, ratingHomeDefence, goalsHome, goalsAway == 0);
        Rater.ratePlayers(away, ratingAwayAttack, ratingAwayDefence, goalsAway, goalsHome == 0);

//        Data.MOTM[motmTeam][motmPlayer]++;

//        System.out.println(String.format("%s - %s %d:%d   --- %s %.2f", Data.TEAMS[home], Data.TEAMS[away], goalsHome,
//                goalsAway, Data.SQUADS.get(Data.TEAMS[motmTeam]).get(motmPlayer), motmRating));
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
