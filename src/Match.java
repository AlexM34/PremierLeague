import java.util.*;
import java.util.stream.Collectors;

class Match {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    static Footballer[] homeSquad;
    static Footballer[] awaySquad;
    static int[][] bookings;

    static void userTactics(Club opponent, boolean isHome) {
        // TODO: Add other choices
        System.out.println("vs " + opponent.getName() + (isHome ? " Home" : " Away"));
        System.out.println("Pick how offensive the team should be from 0 to 20");
        while (true) {
            int offense = scanner.nextInt();
            if(offense < 0 || offense > 20) {
                System.out.println("Wrong offense value.");
                continue;
            }
            Data.USER_STYLE = offense - 10;
            break;
        }
    }

    static void leagueSimulation(Club home, Club away) {
        int result = simulateGame(home, away, false, -1, -1);

        Rater.leagueFinalWhistle(home, away, result / 100, result % 100);
    }

    static int cupSimulation(Club home, Club away, boolean last, int homeGoals, int awayGoals) {
        int result = simulateGame(home, away, last, homeGoals, awayGoals);

        Rater.cupFinalWhistle(home, away, result / 100, result % 100);
        return result;
    }

    static int continentalSimulation(Club home, Club away, boolean last, int homeGoals, int awayGoals) {
        int result = simulateGame(home, away, last, homeGoals, awayGoals);

        Rater.continentalFinalWhistle(home, away, result / 100, result % 100);
        return result;
    }

    private static int simulateGame(Club home, Club away, boolean last, int aggregateHomeGoals, int aggregateAwayGoals) {
        // TODO: Bench
        // TODO: Subs
        Rater.kickoff(home, away);
        homeSquad = pickSquad(home, true);
        awaySquad = pickSquad(away, false);
        bookings = new int[2][11];

        // TODO: Separate variables for scoring
        // TODO: Red card effect
        // TODO: Finals are played on neutral stadium
        int balance = Data.FANS + 100 *
                (Arrays.stream(homeSquad).mapToInt(Footballer::getOverall).sum() + home.getSeason().getForm() * 2 +
                 Arrays.stream(homeSquad).mapToInt(Footballer::getCondition).sum() / 5 - 500) /
                (Arrays.stream(awaySquad).mapToInt(Footballer::getOverall).sum() + away.getSeason().getForm() * 2 +
                 Arrays.stream(awaySquad).mapToInt(Footballer::getCondition).sum() / 5 - 500) - 50;

        int style = Arrays.stream(homeSquad).mapToInt(f -> f.getPosition().getAttackingDuty()).sum()
                + Arrays.stream(awaySquad).mapToInt(f -> f.getPosition().getAttackingDuty()).sum() - 53;

        if (home.getId() == Data.USER || away.getId() == Data.USER) style += Data.USER_STYLE;
        style += (home.getCoach().getStyle() + away.getCoach().getStyle() - 100)  / 10;
        System.out.println();
        System.out.println(balance);
//        System.out.println(style);

        int homeGoals = 0;
        int awayGoals = 0;
        int extra = 0;
        for (int minute = 1; minute <= 90 + extra; minute++) {
            // TODO: Add stoppage time
            // TODO: Too many draws
            int r = random.nextInt(1000);

            if (r < 10 * balance) {
                if (r < balance + style - 38) {
                    homeGoals++;
                    Rater.goal(minute, homeGoals, awayGoals, true);
                    balance -= 10;
                    Rater.updateRatings(3);
                } else if (r < 5 * balance) {
                    balance++;
                    Rater.updateRatings(1);
                }
            } else {
                if (r > 937 + balance - style) {
                    awayGoals++;
                    Rater.goal(minute, homeGoals, awayGoals, false);
                    balance += 10;
                    Rater.updateRatings(-3);
                } else if (r > 999 - 5 * balance) {
                    balance--;
                    Rater.updateRatings(-1);
                }
            }

            // TODO: Use attributes for bookings
            if (random.nextInt(20) == 0) {
                int t = random.nextInt(2);
                int p = random.nextInt(11);

                if (bookings[t][p] == 0) {
                    bookings[t][p]++;
                    balance += 10 * t - 5;
                    (t == 0 ? Rater.homeRatings : Rater.awayRatings)[p] -= 0.5;
                    (t == 0 ? homeSquad : awaySquad)[p].getResume().getSeason().getLeague().addYellowCards(1);
                    System.out.println(minute + "' " + (t == 0 ? homeSquad : awaySquad)[p].getName() + " gets a yellow card");
                }
            }

            else if (random.nextInt(200) == 0) {
                int t = random.nextInt(2);
                int p = random.nextInt(10) + 1;

                if (bookings[t][p] == 2) break;
                bookings[t][p] = 2;
                balance += 30 * t - 15;
                (t == 0 ? Rater.homeRatings : Rater.awayRatings)[p] -= 2;
                (t == 0 ? homeSquad : awaySquad)[p].getResume().getSeason().getLeague().addRedCards(1);
                (t == 0 ? homeSquad : awaySquad)[p].changeCondition(-35);
                System.out.println(minute + "' " + (t == 0 ? homeSquad : awaySquad)[p].getName() + " gets a red card");
            }

            if (minute == 90 && last && (aggregateHomeGoals == -1 && homeGoals == awayGoals ||
                    homeGoals == aggregateAwayGoals && awayGoals == aggregateHomeGoals)) {
                System.out.println("Extra time begins!");
                extra = 30;
            }

//            System.out.println(balance);
        }

        if (last && (aggregateHomeGoals == -1 && homeGoals == awayGoals ||
                homeGoals == aggregateAwayGoals && awayGoals == aggregateHomeGoals)) {
            System.out.println("It's time for the penalty shootout!");
            boolean homeWin = penaltyShootout(homeSquad, awaySquad);
            if (homeWin) homeGoals++;
            else awayGoals++;
        }

        return homeGoals * 100 + awayGoals;
    }

    private static boolean penaltyShootout(Footballer[] homeSquad, Footballer[] awaySquad) {
        // TODO: Red-carded players are off
        int homeGoals = 0;
        int awayGoals = 0;
        int current = 10;
        int taken = 0;
        while(true) {
            homeGoals += penalty(homeSquad[current], awaySquad[0]);
            System.out.println(homeGoals + "-" + awayGoals);
            if (taken < 5 && (homeGoals > awayGoals + 5 - taken || homeGoals + 4 - taken < awayGoals)) break;

            awayGoals += penalty(awaySquad[current], homeSquad[0]);
            System.out.println(homeGoals + "-" + awayGoals);
            if (taken < 4 && (awayGoals > homeGoals + 4 - taken || awayGoals + 4 - taken < homeGoals)
                    || taken >= 4 && homeGoals != awayGoals) break;

            taken++;
            current = (current + 10) % 11;
        }

        return homeGoals > awayGoals;
    }

    private static int penalty(Footballer taker, Footballer keeper) {
        // TODO: Use stats for penalties
        System.out.println(taker.getName() + " steps up to take the penalty vs " + keeper.getName());
        if (random.nextInt(100) < 70) {
            System.out.println("He scores with a great shot!");
            return 1;
        }
        else {
            System.out.println("The goalkeeper makes a wonderful save!");
            return 0;
        }
    }

    private static Footballer[] pickSquad(Club team, boolean isHome) {
        List<Footballer> squad = team.getFootballers().stream()
                .sorted(Comparator.comparing(Footballer::getOverall).reversed())
                .collect(Collectors.toList());

        Formation formation = pickFormation(squad);
        int defenders = formation.getDefenders();
        int midfielders = formation.getMidfielders();
        int forwards = formation.getForwards();
        Footballer[] selected = new Footballer[11];

        int g = 1;
        int d = defenders;
        int m = midfielders;
        int f = forwards;
        Data.DEFENDER_1.changeCondition(100);
        Data.MIDFIELDER_1.changeCondition(100);
        Data.FORWARD_1.changeCondition(100);
        for (Footballer footballer : squad) {
            if (footballer.getPosition() == null || footballer.getCondition() < 70) {
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
//            System.out.println(f);
            if (f.getPosition() == null || f.getCondition() < 70) {
                continue;
            }

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
        for (Footballer f : footballers) {
            System.out.println(f.getName() + " " + f.getPosition().getRole() + " " + f.getCondition());
        }
        return Formation.F5;
    }
}
