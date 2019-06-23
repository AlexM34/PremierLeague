import java.util.*;
import java.util.stream.Collectors;

class Match {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    static Footballer[] homeSquad;
    static Footballer[] awaySquad;
    static int[][] bookings;

    static void userTactics(final Club opponent, final boolean isHome) {
        // TODO: Add other choices
        System.out.println("vs " + opponent.getName() + (isHome ? " Home" : " Away"));
        System.out.println("Pick how offensive the team should be from 0 to 20");
        while (true) {
            final int offense = scanner.nextInt();
            if(offense < 0 || offense > 20) {
                System.out.println("Wrong offense value.");
                continue;
            }
            Data.USER_STYLE = offense - 10;
            break;
        }
    }

    static void leagueSimulation(final Club home, final Club away) {
        final int result = simulateGame(home, away, false, -1, -1);

        Rater.finalWhistle(home, away, result / 100, result % 100, 0);
    }

    static int cupSimulation(final Club home, final Club away, final boolean last, final int homeGoals, final int awayGoals) {
        final int result = simulateGame(home, away, last, homeGoals, awayGoals);

        Rater.finalWhistle(home, away, result / 100, result % 100, 1);
        return result;
    }

    static int continentalSimulation(final Club home, final Club away, final boolean last, final int homeGoals, final int awayGoals) {
        final int result = simulateGame(home, away, last, homeGoals, awayGoals);

        Rater.finalWhistle(home, away, result / 100, result % 100, 2);
        return result;
    }

    private static int simulateGame(final Club home, final Club away, final boolean last, final int aggregateHomeGoals, final int aggregateAwayGoals) {
        // TODO: Bench
        // TODO: Subs
        Rater.kickoff(home, away);
        homeSquad = pickSquad(home, true);
        awaySquad = pickSquad(away, false);
        bookings = new int[2][11];

        // TODO: Fine tune the balance
        // TODO: Finals are played on neutral stadium
        final int homeAttack = getAttack(homeSquad, awaySquad);
        final int awayAttack = getAttack(awaySquad, homeSquad);
        int balance = Data.FANS + 5 * (homeAttack - awayAttack) / 5  +
                (home.getSeason().getForm() - away.getSeason().getForm()) / 4 + 50;
//                (Arrays.stream(homeSquad).mapToInt(Footballer::getOverall).sum() + home.getSeason().getForm() * 2 +
//                 Arrays.stream(homeSquad).mapToInt(Footballer::getCondition).sum() / 5 - 500) /
//                (Arrays.stream(awaySquad).mapToInt(Footballer::getOverall).sum() + away.getSeason().getForm() * 2 +
//                 Arrays.stream(awaySquad).mapToInt(Footballer::getCondition).sum() / 5 - 500) - 50;

        int momentum = balance;
        int style = (homeAttack + awayAttack) / 10 - 6;
//                Arrays.stream(homeSquad).mapToInt(f -> f.getPosition().getAttackingDuty()).sum()
//                + Arrays.stream(awaySquad).mapToInt(f -> f.getPosition().getAttackingDuty()).sum() - 53;

        if (home.getId() == Data.USER || away.getId() == Data.USER) style += Data.USER_STYLE;
        style += (home.getCoach().getStyle() + away.getCoach().getStyle() - 100) / 10;
        if (PremierLeague.matchFlag) {
            System.out.println();
            System.out.println(balance);
            System.out.println(style);
        }

        int homeGoals = 0;
        int awayGoals = 0;
        int extra = 0;
        for (int minute = 1; minute <= 90 + extra; minute++) {
            // TODO: Add stoppage time
            // TODO: Too many draws
            final int r = random.nextInt(1000);

            if (r < 10 * momentum) {
                if (r < momentum + style - 38) {
                    homeGoals++;
                    Rater.goal(minute, homeGoals, awayGoals, true);
                    momentum = balance;
                    Rater.updateRatings(3);
                } else if (r < 5 * momentum) {
                    momentum++;
                    Rater.updateRatings(1);
                }
            } else {
                if (r > 937 + momentum - style) {
                    awayGoals++;
                    Rater.goal(minute, homeGoals, awayGoals, false);
                    momentum = balance;
                    Rater.updateRatings(-3);
                } else if (r > 999 - 5 * momentum) {
                    momentum--;
                    Rater.updateRatings(-1);
                }
            }

            // TODO: Use attributes for bookings
            if (random.nextInt(20) == 0) {
                final int t = random.nextInt(2);
                final int p = random.nextInt(11);

                if (bookings[t][p] == 0) {
                    bookings[t][p]++;
                    balance += 2 * t - 1;
                    (t == 0 ? Rater.homeRatings : Rater.awayRatings)[p] -= 0.5;
                    Rater.yellows.add((t == 0 ? homeSquad : awaySquad)[p]);
                    if (PremierLeague.matchFlag) System.out.println(minute + "' " + (t == 0 ? homeSquad : awaySquad)[p].getName() + " gets a yellow card");
                }
            }

            else if (random.nextInt(200) == 0) {
                final int t = random.nextInt(2);
                final int p = random.nextInt(10) + 1;

                if (bookings[t][p] == 2) break;
                bookings[t][p] = 2;
                balance += 20 * t - 10;
                (t == 0 ? Rater.homeRatings : Rater.awayRatings)[p] -= 2;
                Rater.reds.add((t == 0 ? homeSquad : awaySquad)[p]);
                (t == 0 ? homeSquad : awaySquad)[p].changeCondition(-35);
                if (PremierLeague.matchFlag) System.out.println(minute + "' " + (t == 0 ? homeSquad : awaySquad)[p].getName() + " gets a red card");
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
            final boolean homeWin = penaltyShootout(homeSquad, awaySquad);
            if (homeWin) homeGoals++;
            else awayGoals++;
        }

        return homeGoals * 100 + awayGoals;
    }

    private static int getAttack(final Footballer[] homeSquad, final Footballer[] awaySquad) {
        int attack = 0;
        int defence = 0;
        for (Footballer footballer : homeSquad) {
            attack += footballer.getOverall() * footballer.getPosition().getAttackingDuty();
        }

        for (Footballer footballer : awaySquad) {
            defence += footballer.getOverall() * (5 - footballer.getPosition().getAttackingDuty());
        }

        if (PremierLeague.matchFlag) {
            System.out.println(attack);
            System.out.println(defence);
        }
        return 50 * attack / defence;
    }

    private static boolean penaltyShootout(final Footballer[] homeSquad, final Footballer[] awaySquad) {
        int homeGoals = 0;
        int awayGoals = 0;
        int currentHome = 10;
        int currentAway = 10;
        int taken = 0;

        while(true) {
            while (bookings[0][currentHome] > 1) currentHome = (currentHome + 10) % 11;
            homeGoals += penalty(homeSquad[currentHome], awaySquad[0]);
            System.out.println(homeGoals + "-" + awayGoals);
            if (taken < 5 && (homeGoals > awayGoals + 5 - taken || homeGoals + 4 - taken < awayGoals)) break;
            currentHome = (currentHome + 10) % 11;

            while (bookings[1][currentAway] > 1) currentAway = (currentAway + 10) % 11;
            awayGoals += penalty(awaySquad[currentAway], homeSquad[0]);
            System.out.println(homeGoals + "-" + awayGoals);
            if (taken < 4 && (awayGoals > homeGoals + 4 - taken || awayGoals + 4 - taken < homeGoals)
                    || taken >= 4 && homeGoals != awayGoals) break;
            currentAway = (currentAway + 10) % 11;

            taken++;
        }

        return homeGoals > awayGoals;
    }

    private static int penalty(final Footballer striker, final Footballer goalkeeper) {
        if (PremierLeague.matchFlag) System.out.println(striker.getName() + " steps up to take the penalty vs " + goalkeeper.getName());
        if (random.nextInt(100) < 70 + striker.getOverall() - goalkeeper.getOverall()) {
            if (PremierLeague.matchFlag) System.out.println("He scores with a great shot!");
            return 1;
        }
        else {
            if (PremierLeague.matchFlag) System.out.println("The goalkeeper makes a wonderful save!");
            return 0;
        }
    }

    private static Footballer[] pickSquad(final Club team, final boolean isHome) {
        final List<Footballer> squad = team.getFootballers().stream()
                .sorted(Comparator.comparing(Footballer::getOverall).reversed())
                .collect(Collectors.toList());

        final Formation formation = pickFormation(squad);
        final int defenders = formation.getDefenders();
        final int midfielders = formation.getMidfielders();
        final int forwards = formation.getForwards();
        final Footballer[] selected = new Footballer[11];

        int g = 1;
        int d = defenders;
        int m = midfielders;
        int f = forwards;
        Data.DEFENDER_1.changeCondition(100);
        Data.MIDFIELDER_1.changeCondition(100);
        Data.FORWARD_1.changeCondition(100);
        for (final Footballer footballer : squad) {
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

    private static Formation pickFormation(final List<Footballer> footballers) {
        // TODO: Smart formation pick - opponent, fatigue, form
        int defenders = 0;
        int midfielders = 0;
        int forwards = 0;

        for (final Footballer f : footballers) {
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
                for (final Formation formation : Formation.values()) {
                    if (formation.getDefenders() <= defenders &&
                        formation.getMidfielders() <= midfielders &&
                        formation.getForwards() <= forwards) {
                        return formation;
                    }
                }
            }
        }

        System.out.println("Could not pick an appropriate formation");
        for (final Footballer f : footballers) {
            System.out.println(f.getName() + " " + f.getPosition().getRole() + " " + f.getCondition());
        }
        return Formation.F5;
    }
}
