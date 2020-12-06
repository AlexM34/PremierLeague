package simulation.match;

import static simulation.Data.DEFENDER_1;
import static simulation.Data.DEFENDER_2;
import static simulation.Data.FORWARD_1;
import static simulation.Data.FORWARD_2;
import static simulation.Data.GOALKEEPER_1;
import static simulation.Data.MIDFIELDER_1;
import static simulation.Data.MIDFIELDER_2;
import static simulation.Data.userStyle;

import player.Footballer;
import player.MatchStats;
import team.Club;
import team.Formation;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Tactics {

    private static final PrintStream STREAM = new PrintStream(new FileOutputStream(FileDescriptor.out));
    private static final Scanner scanner = new Scanner(System.in);

    private Tactics() {
    }

    public static void preMatch(final Club opponent, final boolean isHome) {
        STREAM.println("vs " + opponent.getName() + (isHome ? " Home" : " Away"));
        STREAM.println("Pick how offensive the team should be from 0 to 20");
        while (true) {
            final int attack = scanner.nextInt();
            if (attack < 0 || attack > 20) {
                STREAM.println("Wrong attack value!");
                continue;
            }

            userStyle = attack - 10;
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
                case DEFENDER:
                    defenders++;
                    break;
                case MIDFIELDER:
                    midfielders++;
                    break;
                case FORWARD:
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

        STREAM.println("Could not pick appropriate formation");
        for (final Footballer f : footballers) {
            STREAM.println(f.getName() + " " + f.getPosition().getRole() + " " + f.getCondition());
        }
        return Formation.F5;
    }

    static MatchStats substitute(final Lineup lineup, final int minute, final int stoppage, final StringBuilder report) {
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
                report.append(minute).append(stoppage != 0 ? "+" + stoppage : "").append("' ")
                        .append(subbedIn.getFootballer().getName()).append(" replaces ").append(subbedOut.getName()).append("<br/>");

                subbedIn.setStarted(minute);
                squad.set(flop, subbedIn);
                bench.set(player, null);
                break;
            }
        }

        return squad.get(flop);
    }
}
