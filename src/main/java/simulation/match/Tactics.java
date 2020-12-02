package simulation.match;

import static simulation.Data.DEFENDER_1;
import static simulation.Data.DEFENDER_2;
import static simulation.Data.FORWARD_1;
import static simulation.Data.FORWARD_2;
import static simulation.Data.GOALKEEPER_1;
import static simulation.Data.MIDFIELDER_1;
import static simulation.Data.MIDFIELDER_2;
import static simulation.Data.USER_STYLE;

import player.Footballer;
import player.MatchStats;
import team.Club;
import team.Formation;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Tactics {
    private static final Scanner scanner = new Scanner(System.in);

    public static void preMatch(final Club opponent, final boolean isHome) {
        System.out.println("vs " + opponent.getName() + (isHome ? " Home" : " Away"));
        System.out.println("Pick how offensive the team should be from 0 to 20");
        while (true) {
            final int attack = scanner.nextInt();
            if (attack < 0 || attack > 20) {
                System.out.println("Wrong attack value!");
                continue;
            }

            USER_STYLE = attack - 10;
            break;
        }
    }

    static Lineup pickSquad(final Club team) {
        final List<Footballer> footballers = team.getFootballers().stream()
                .sorted(Comparator.comparing(Footballer::getOverall).reversed())
                .collect(Collectors.toList());

        refreshDummies();
        final Formation formation = pickFormation(footballers);
        final Lineup lineup = new Lineup(formation);
        for (final Footballer footballer : footballers) {
            if (!footballer.canPlay()) {
                footballer.changeBan(-1);
                continue;
            }

            lineup.add(footballer);
        }

        lineup.print();
        return lineup;
    }

    private static void refreshDummies() {
        GOALKEEPER_1.changeCondition(100);
        DEFENDER_1.changeCondition(100);
        MIDFIELDER_1.changeCondition(100);
        FORWARD_1.changeCondition(100);
        DEFENDER_2.changeCondition(100);
        MIDFIELDER_2.changeCondition(100);
        FORWARD_2.changeCondition(100);
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

    static void substitute(final Report report, final int minute, final int stoppage, final boolean isHome) {
        final Lineup lineup = isHome ? report.getHomeLineup() : report.getAwayLineup();
        final List<MatchStats> squad = lineup.getSquad();
        final List<MatchStats> bench = lineup.getBench();
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
            final MatchStats subbedIn = bench.get(player);
            if (subbedIn == null) continue;
            if (subbedOut.getPosition().getRole().equals(subbedIn.getFootballer().getPosition().getRole())) {
                report.append(String.valueOf(minute)).append(stoppage != 0 ? "+" + stoppage : "").append("' ")
                        .append(subbedIn.getFootballer().getName()).append(" replaces ").append(subbedOut.getName()).append("<br/>");

                report.updateStats(squad.get(flop));
                subbedIn.setStarted(minute);
                squad.set(flop, subbedIn);
                bench.set(player, null);
                break;
            }
        }
    }
}
