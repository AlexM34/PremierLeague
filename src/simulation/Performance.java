package simulation;

import players.Footballer;
import players.MatchStats;

import java.util.List;
import java.util.Random;

import static simulation.Controller.matchFlag;

class Performance {
    private static final Random random = new Random();

    static void goal(final int homeGoals, final int awayGoals, final boolean isHome) {
        int scoring = 10000;
        int assisting = 20000;
        Footballer goalscorer = null;
        Footballer assistmaker = null;
        final List<MatchStats> squad = isHome ? Match.homeSquad : Match.awaySquad;

        for (int player = 0; player < 11; player++) {
            if (squad.get(player) == null || squad.get(player).isRedCarded()) continue;
            scoring += scoringChance(squad.get(player).getFootballer());
            assisting += assistingChance(squad.get(player).getFootballer());
        }

        int r = random.nextInt(scoring);
        for (int player = 0; player < 11; player++) {
            if (squad.get(player) == null || squad.get(player).isRedCarded()) continue;
            r -= scoringChance(squad.get(player).getFootballer());
            if (r < 0) {
                goalscorer = squad.get(player).getFootballer();
                squad.get(player).addGoals();
                break;
            }
        }

        if (goalscorer == null) {
            int footballer = ownGoal();
            if (squad.get(footballer).isRedCarded()) footballer = 0;
            if (matchFlag) System.out.println(Match.minute + (Match.stoppage != 0 ? "+" + Match.stoppage : "") + "' " +
                    "Own goal scored by " + squad.get(footballer).getFootballer().getName() + ". " + homeGoals + "-" + awayGoals);
        }
        else {
            r = random.nextInt(assisting);
            for (int player = 0; player < 11; player++) {
                if (squad.get(player) == null || squad.get(player).isRedCarded()) continue;
                r -= assistingChance(squad.get(player).getFootballer());
                if (r < 0) {
                    assistmaker = squad.get(player).getFootballer();
                    if (assistmaker.equals(goalscorer)) {
                        assistmaker = null;
                        squad.get(player).changeRating(0.25f);
                    } else squad.get(player).addAssists();

                    break;
                }
            }

            if (matchFlag) System.out.println(Match.minute + (Match.stoppage != 0 ? "+" + Match.stoppage : "") + "' " +
                    goalscorer.getName() + (assistmaker != null ? " scores after a pass from " + assistmaker.getName()
                    : " scores after a solo run") + ". " + homeGoals + "-" + awayGoals);
        }
    }

    private static int ownGoal() {
        if (random.nextInt(10) < 2) return 0;
        if (random.nextInt(5) < 3) return 1 + random.nextInt(4);
        if (random.nextInt(10) < 7) return 5 + random.nextInt(3);
        return 8 + random.nextInt(3);
    }

    private static int scoringChance(final Footballer footballer) {
        switch (footballer.getPosition().getRole()) {
            case Goalkeeper:
                return 1;
            case Defender:
                return footballer.getFinishing() * footballer.getOverall();
            case Midfielder:
                return footballer.getFinishing() * footballer.getOverall() * 2;
            case Forward:
                return footballer.getFinishing() * footballer.getOverall() * 3;
        }

        return 0;
    }

    private static int assistingChance(final Footballer footballer) {
        switch (footballer.getPosition().getRole()) {
            case Goalkeeper:
                return footballer.getVision() * 10;
            case Defender:
                return footballer.getVision() * footballer.getOverall();
            case Midfielder:
                return footballer.getVision() * footballer.getOverall() * 3;
            case Forward:
                return footballer.getVision() * footballer.getOverall() * 2;
        }

        return 0;
    }
}