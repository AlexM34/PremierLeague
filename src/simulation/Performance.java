package simulation;

import players.Footballer;
import players.MatchStats;

import java.util.List;
import java.util.Random;

import static simulation.Match.gameReport;

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
            final List<MatchStats> opponent = isHome ? Match.awaySquad : Match.homeSquad;
            if (opponent.get(footballer).isRedCarded()) footballer = 0;
            opponent.get(footballer).changeRating(-1.5f);
            gameReport.append(Match.minute).append(Match.stoppage != 0 ? "+" + Match.stoppage : "")
                    .append("' ").append("Own goal scored by ").append(opponent.get(footballer).getFootballer().getName())
                    .append(". ").append(homeGoals).append("-").append(awayGoals).append("<br/>");
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

            gameReport.append(Match.minute).append(Match.stoppage != 0 ? "+" + Match.stoppage : "").append("' ")
                    .append(goalscorer.getName()).append(assistmaker != null ? " scores after a pass from "
                    + assistmaker.getName() : " scores after a solo run").append(". ").append(homeGoals).append("-")
                    .append(awayGoals).append("<br/>");
        }
    }

    private static int ownGoal() {
        if (random.nextInt(10) < 2) return 0;
        if (random.nextInt(5) < 3) return 1 + random.nextInt(4);
        if (random.nextInt(10) < 7) return 5 + random.nextInt(3);
        return 8 + random.nextInt(3);
    }

    private static int scoringChance(final Footballer footballer) {
        final int overall = Math.max(1, footballer.getOverall() - 60);
        final int finishing = Math.max(1, footballer.getFinishing() - 30);
        switch (footballer.getPosition().getRole()) {
            case Goalkeeper:
                return 1;
            case Defender:
                return overall * finishing;
            case Midfielder:
                return overall * finishing * 2;
            case Forward:
                return overall * finishing * 4;
        }

        return 0;
    }

    private static int assistingChance(final Footballer footballer) {
        final int overall = Math.max(1, footballer.getOverall() - 60);
        final int vision = Math.max(1, footballer.getVision() - 30);
        switch (footballer.getPosition().getRole()) {
            case Goalkeeper:
                return 10;
            case Defender:
                return overall * vision;
            case Midfielder:
                return overall * vision * 5;
            case Forward:
                return overall * vision * 3;
        }

        return 0;
    }
}
