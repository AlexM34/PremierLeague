package simulation;

import players.Competition;
import players.Footballer;
import players.MatchStats;
import teams.Club;
import teams.Formation;

import java.util.*;
import java.util.stream.Collectors;

import static simulation.Data.*;
import static simulation.PremierLeague.matchFlag;

class Match {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    static int competition = 0;
    static int minute = 1;
    static int stoppage = 0;
    static List<MatchStats> homeSquad = new ArrayList<>();
    static List<MatchStats> awaySquad = new ArrayList<>();
    private static List<Footballer> homeBench = new ArrayList<>();
    private static List<Footballer> awayBench = new ArrayList<>();
    private static int homeSubs;
    private static int awaySubs;

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
            USER_STYLE = offense - 10;
            break;
        }
    }

    static int simulation(final Club home, final Club away, final boolean last, final int homeGoals,
                          final int awayGoals, final int type) {
        competition = type;
        final int result = simulateGame(home, away, last, homeGoals, awayGoals);
        FANS = 3;

        Rater.finalWhistle(home, away, result / 100, result % 100);
        return result;
    }

    private static int simulateGame(final Club home, final Club away, final boolean last,
                                    final int aggregateHomeGoals, final int aggregateAwayGoals) {
        Rater.kickoff(home, away);
        int homeGoals = 0;
        int awayGoals = 0;
        minute = 1;
        int extra = 0;
        int added;

        pickSquad(home, true);
        pickSquad(away, false);
        homeSubs = 3;
        awaySubs = 3;

        final int homeAttack = getAttack(homeSquad, awaySquad);
        final int awayAttack = getAttack(awaySquad, homeSquad);
        int balance = FANS + 6 * (homeAttack - awayAttack) / 7  +
                (home.getSeason().getForm() - away.getSeason().getForm()) / 4 + 50;

        int momentum = balance;
        int style = (homeAttack + awayAttack) / 10 - 6;

        if (home.getId() == USER || away.getId() == USER) style += USER_STYLE / 2 - 5;

        if (matchFlag) {
            System.out.println();
            System.out.println(balance);
            System.out.println(style);
        }
        while (minute <= 90 + extra) {
            added = (minute % 45 == 0) ? 2 * minute / 45 : 0;
            stoppage = 0;

            while (stoppage <= added) {
                final int r = random.nextInt(1000);
                if (r < 10 * momentum) {
                    if (r < momentum + style - 41) {
                        homeGoals++;
                        Rater.goal(homeGoals, awayGoals, true);
                        momentum = balance;
                        Rater.updateRatings(3);
                    } else if (r < 5 * momentum) {
                        momentum++;
                        Rater.updateRatings(1);
                    }
                } else {
                    if (r > 940 + momentum - style) {
                        awayGoals++;
                        Rater.goal(homeGoals, awayGoals, false);
                        momentum = balance;
                        Rater.updateRatings(-3);
                    } else if (r > 999 - 5 * momentum) {
                        momentum--;
                        Rater.updateRatings(-1);
                    }
                }

                // TODO: Use attributes for bookings
                if (random.nextInt(25) == 0) {
                    final boolean t = random.nextBoolean();
                    final int p = random.nextInt(11);
                    final MatchStats footballer = (t ? homeSquad : awaySquad).get(p);

                    if (!footballer.isRedCarded()) {
                        if (!footballer.isYellowCarded()) {
                            footballer.addYellowCard();
                            balance += (t ? -1 : 1);
                            if (matchFlag)
                                System.out.println(minute + (stoppage != 0 ? "+" + stoppage : "") + "' " +
                                        footballer.getFootballer().getName() + " gets a yellow card");
                        } else {
                            footballer.addRedCard();
                            balance += (t ? -10 : 10);
                            footballer.getFootballer().changeBan(1);
                            if (matchFlag) {
                                System.out.println(minute + (stoppage != 0 ? "+" + stoppage : "") + "' " +
                                        footballer.getFootballer().getName() + " gets a second yellow card and he is ejected");
                            }

                            if (p == 0) goalkeeperEjected(t);
                        }
                    }
                } else if (random.nextInt(200) == 0) {
                    final boolean t = random.nextBoolean();
                    final int p = random.nextInt(11);
                    final MatchStats footballer = (t ? homeSquad : awaySquad).get(p);

                    if (!footballer.isRedCarded()) {
                        footballer.addRedCard();
                        balance += (t ? -10 : 10);
                        footballer.getFootballer().changeBan(random.nextInt(5) == 0 ? 2 : 1);
                        if (matchFlag) {
                            System.out.println(minute + (stoppage != 0 ? "+" + stoppage : "") + "' " +
                                    footballer.getFootballer().getName() + " gets a red card");
                        }

                        if (p == 0) goalkeeperEjected(t);
                    }
                }

                if (minute > 60) {
                    if (homeSubs > 0 && random.nextInt(10) == 0) substitute(true);
                    if (awaySubs > 0 && random.nextInt(10) == 0) substitute(false);
                }

                stoppage++;
            }

            if (minute == 90 && last && (aggregateHomeGoals == -1 && homeGoals == awayGoals ||
                    homeGoals == aggregateAwayGoals && awayGoals == aggregateHomeGoals)) {
                System.out.println("Extra time begins!");
                extra = 30;
            }

            minute++;
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

    private static void substitute(final boolean isHome) {
        final List<MatchStats> squad = isHome ? homeSquad : awaySquad;
        final List<Footballer> bench = isHome ? homeBench : awayBench;
        float worst = 10;
        int flop = 0;

        for (int player = 1; player < 11; player++) {
            if (squad.get(player).isRedCarded() || squad.get(player).getStarted() != 1) continue;
            final float rating = squad.get(player).getRating();
            if (rating < worst) {
                worst = rating;
                flop = player;
            }
        }

        final Footballer subbedOut = squad.get(flop).getFootballer();
        for (int player = 0; player < bench.size(); player++) {
            final Footballer subbedIn = bench.get(player);
            if (subbedIn == null) continue;
            if (subbedOut.getPosition().getRole().equals(subbedIn.getPosition().getRole())) {
                if (matchFlag) {
                    System.out.println(minute + (stoppage != 0 ? "+" + stoppage : "") + "' " + subbedIn.getName() + " replaces " + subbedOut.getName());
                }

                Competition season = Rater.getCompetition(subbedOut.getResume().getSeason(), competition);
                updateStats(season, squad.get(flop));
//                System.out.println(flop);
//                System.out.println(squad.get(flop));
                squad.set(flop, new MatchStats(subbedIn, minute));
                bench.set(player, null);
//                System.out.println(squad.get(flop));

                if (isHome) homeSubs--;
                else awaySubs--;
                break;
            }
        }
    }

    private static void goalkeeperEjected(final boolean isHome) {
        final List<MatchStats> squad = isHome ? homeSquad : awaySquad;
        final List<Footballer> bench = isHome ? homeBench : awayBench;
        float worst = 10;
        int flop = 5;

        for (int player = 6; player < 11; player++) {
            if (squad.get(player).isRedCarded() || squad.get(player).getStarted() != 1) continue;
            final float rating = squad.get(player).getRating();
            if (rating < worst) {
                worst = rating;
                flop = player;
            }
        }

        final Footballer subbedOut = squad.get(flop).getFootballer();
        final Footballer subbedIn = bench.get(0) != null ? bench.get(0) :
                bench.stream().filter(Objects::nonNull).findFirst().get();

        if (matchFlag) {
            System.out.println(minute + (stoppage != 0 ? "+" + stoppage : "") + "' " +
                    subbedIn.getName() + " replaces " + subbedOut.getName());
        }

        Competition season = Rater.getCompetition(subbedOut.getResume().getSeason(), competition);
        updateStats(season, squad.get(flop));
        squad.set(flop, new MatchStats(subbedIn, minute));
        bench.set(0, null);

        if (isHome) homeSubs--;
        else awaySubs--;
    }

    private static void updateStats(final Competition season, final MatchStats matchStats) {
        season.addMatches(1);
        season.addRating((int) matchStats.getRating() * 100, 1);
        season.addGoals(matchStats.getGoals());
        season.addAssists(matchStats.getAssists());
        if (matchStats.isYellowCarded()) season.addYellowCards(1);
        if (matchStats.isRedCarded()) season.addRedCards(1);
    }

    private static int getAttack(final List<MatchStats> homeSquad, final List<MatchStats> awaySquad) {
        int attack = 0;
        int defence = 0;
        for (final MatchStats footballer : homeSquad) {
            attack += footballer.getFootballer().getOverall() * footballer.getFootballer().getPosition().getAttackingDuty();
        }

        for (final MatchStats footballer : awaySquad) {
            defence += footballer.getFootballer().getOverall() * (5 - footballer.getFootballer().getPosition().getAttackingDuty());
        }

//        if (matchFlag) {
//            System.out.println(attack);
//            System.out.println(defence);
//        }
        return 50 * attack / defence;
    }

    private static boolean penaltyShootout(final List<MatchStats> homeSquad, final List<MatchStats> awaySquad) {
        int homeGoals = 0;
        int awayGoals = 0;
        int currentHome = 10;
        int currentAway = 10;
        int taken = 0;

        while(true) {
            while (homeSquad.get(currentHome) == null) currentHome = (currentHome + 10) % 11;
            homeGoals += penalty(homeSquad.get(currentHome), awaySquad.get(0));
            System.out.println(homeGoals + "-" + awayGoals);
            if (taken < 5 && (homeGoals > awayGoals + 5 - taken || homeGoals + 4 - taken < awayGoals)) break;
            currentHome = (currentHome + 10) % 11;

            while (awaySquad.get(currentAway) == null) currentAway = (currentAway + 10) % 11;
            awayGoals += penalty(awaySquad.get(currentAway), homeSquad.get(0));
            System.out.println(homeGoals + "-" + awayGoals);
            if (taken < 4 && (awayGoals > homeGoals + 4 - taken || awayGoals + 4 - taken < homeGoals)
                    || taken >= 4 && homeGoals != awayGoals) break;
            currentAway = (currentAway + 10) % 11;

            taken++;
        }

        return homeGoals > awayGoals;
    }

    private static int penalty(final MatchStats striker, final MatchStats goalkeeper) {
        if (matchFlag) System.out.println(striker.getFootballer().getName() +
                " steps up to take the penalty vs " + goalkeeper.getFootballer().getName());
        if (random.nextInt(100) < 70 + striker.getFootballer().getOverall() - goalkeeper.getFootballer().getOverall()) {
            if (matchFlag) System.out.println("He scores with a great shot!");
            return 1;
        }
        else {
            if (matchFlag) System.out.println("The goalkeeper makes a wonderful save!");
            return 0;
        }
    }

    private static void pickSquad(final Club team, final boolean isHome) {
        final List<Footballer> footballers = team.getFootballers().stream()
                .sorted(Comparator.comparing(Footballer::getOverall).reversed())
                .collect(Collectors.toList());

        final Formation formation = pickFormation(footballers);
        final int defenders = formation.getDefenders();
        final int midfielders = formation.getMidfielders();
        final int forwards = formation.getForwards();
        final MatchStats[] squad = new MatchStats[11];
        final Footballer[] bench = new Footballer[7];

        int g = 1;
        int d = defenders;
        int m = midfielders;
        int f = forwards;
        int bg = 1;
        int bf = 6;
        GOALKEEPER_1.changeCondition(100);
        DEFENDER_1.changeCondition(100);
        MIDFIELDER_1.changeCondition(100);
        FORWARD_1.changeCondition(100);
        DEFENDER_2.changeCondition(100);
        MIDFIELDER_2.changeCondition(100);
        FORWARD_2.changeCondition(100);
        for (final Footballer footballer : footballers) {
            if (footballer.getPosition() == null || footballer.getCondition() < 70
            || footballer.getBan() > 0) {
                footballer.changeBan(-1);
                continue;
            }

            switch (footballer.getPosition().getRole()) {
                case Goalkeeper:
                    if (g > 0) squad[--g] = new MatchStats(footballer, minute);
                    else if (bg > 0) bench[--bg] = footballer;
                    break;

                case Defender:
                    if (d > 0) squad[d--] = new MatchStats(footballer, minute);
                    else if (bf > 0) bench[bf--] = footballer;
                    break;

                case Midfielder:
                    if (m > 0) squad[--m + defenders + 1] = new MatchStats(footballer, minute);
                    else if (bf > 0) bench[bf--] = footballer;
                    break;

                case Forward:
                    if (f > 0) squad[--f + defenders + midfielders + 1] = new MatchStats(footballer, minute);
                    else if (bf > 0) bench[bf--] = footballer;
                    break;
            }
        }

        if (squad[0] == null) footballers.forEach(f1 -> System.out.println(f1.getPosition() + "" + f1.getCondition()));

//        Arrays.stream(squad).forEach(System.out::println);
//        Arrays.stream(bench).forEach(System.out::println);

        if (isHome) {
            homeSquad = Arrays.asList(squad);
            homeBench = Arrays.asList(bench);
        }
        else {
            awaySquad = Arrays.asList(squad);
            awayBench = Arrays.asList(bench);
        }
    }

    private static Formation pickFormation(final List<Footballer> footballers) {
        // TODO: Smart formation pick - opponent, fatigue, form
        int defenders = 0;
        int midfielders = 0;
        int forwards = 0;

        for (final Footballer f : footballers) {
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
