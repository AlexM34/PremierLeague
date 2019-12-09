package simulation;

import players.Competition;
import players.Footballer;
import players.MatchStats;
import team.Club;
import team.Formation;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static simulation.Controller.matchFlag;
import static simulation.Data.*;
import static simulation.Match.*;
import static simulation.Rater.getCompetition;

class Tactics {
    private static final Scanner scanner = new Scanner(System.in);

    static void preMatch(final Club opponent, final boolean isHome) {
        System.out.println("vs " + opponent.getName() + (isHome ? " Home" : " Away"));
        System.out.println("Pick how offensive the team should be from 0 to 20");
        while (true) {
            final int attack = scanner.nextInt();
            if(attack < 0 || attack > 20) {
                System.out.println("Wrong attack value!");
                continue;
            }

            USER_STYLE = attack - 10;
            break;
        }
    }

    static void pickSquad(final Club team, final boolean isHome) {
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

        System.out.println("Could not pick appropriate formation");
        for (final Footballer f : footballers) {
            System.out.println(f.getName() + " " + f.getPosition().getRole() + " " + f.getCondition());
        }
        return Formation.F5;
    }

    static void substitute(final boolean isHome) {
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

                Competition season = getCompetition(subbedOut.getResume().getSeason(), competition);
                updateStats(season, squad.get(flop));
                squad.set(flop, new MatchStats(subbedIn, minute));
                bench.set(player, null);

                if (isHome) homeSubs--;
                else awaySubs--;
                break;
            }
        }
    }
}
