import java.util.*;
import java.util.stream.Collectors;

class Match {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    static Footballer[] homeSquad;
    static Footballer[] awaySquad;
    static int[][] bookings;

    static void userTactics(int opponent, boolean isHome) {
        // TODO: Add other choices
        System.out.println("vs " + Data.DRAW.get(opponent).getName() + (isHome ? " Home" : " Away"));
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

    static void simulateGame(int home, int away) {
        // TODO: Match odds
        // TODO: Bench
        // TODO: Subs
        Rater.kickoff(home, away);
        homeSquad = pickSquad(home, true);
        awaySquad = pickSquad(away, false);
        bookings = new int[2][11];

        // TODO: Separate variables for scoring
        int balance = Data.FANS + 100 *
                (Arrays.stream(homeSquad).mapToInt(Footballer::getOverall).sum() + Data.DRAW.get(home).getSeason().getForm() +
                 Arrays.stream(homeSquad).mapToInt(Footballer::getCondition).sum() / 5 - 300) /
                (Arrays.stream(awaySquad).mapToInt(Footballer::getOverall).sum() + Data.DRAW.get(away).getSeason().getForm() +
                 Arrays.stream(awaySquad).mapToInt(Footballer::getCondition).sum() / 5 - 300) - 50;

        int style = Arrays.stream(homeSquad).mapToInt(f -> f.getPosition().getAttackingDuty()).sum()
                + Arrays.stream(awaySquad).mapToInt(f -> f.getPosition().getAttackingDuty()).sum() - 56;

        if (home == Data.USER || away == Data.USER) style += Data.USER_STYLE;
        style += (Data.DRAW.get(home).getCoach().getStyle() + Data.DRAW.get(away).getCoach().getStyle() - 100)  / 10;
        System.out.println(balance);
        System.out.println(style);

        int homeGoals = 0;
        int awayGoals = 0;
        for (int minute = 1; minute <= 90; minute++) {
            // TODO: Add stoppage time
            int r = random.nextInt(1000);

            if (r < 10 * balance) {
                if (r < balance + style - 38) {
                    Rater.goal(minute, true);
                    homeGoals++;
                    balance -= 10;
                    Rater.updateRatings(3);
                } else if (r < 5 * balance) {
                    balance++;
                    Rater.updateRatings(1);
                }
            } else {
                if (r > 937 + balance - style) {
                    Rater.goal(minute, false);
                    awayGoals++;
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
                    (t == 0 ? homeSquad : awaySquad)[p].getResume().getSeason().addYellowCards(1);
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
                (t == 0 ? homeSquad : awaySquad)[p].getResume().getSeason().addRedCards(1);
                (t == 0 ? homeSquad : awaySquad)[p].changeCondition(-30);
                System.out.println(minute + "' " + (t == 0 ? homeSquad : awaySquad)[p].getName() + " gets a red card");
            }

//            System.out.println(balance);
        }

        Rater.finalWhistle(home, away, homeGoals, awayGoals);
    }

    private static Footballer[] pickSquad(int team, boolean isHome) {
        List<Footballer> squad =  Data.DRAW.get(team).getFootballers().stream()
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
        return Formation.F5;
    }
}
