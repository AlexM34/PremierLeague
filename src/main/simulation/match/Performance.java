package main.simulation.match;

import main.player.Footballer;
import main.player.MatchStats;
import main.simulation.Simulator;

import java.util.List;

import static main.simulation.match.Match.report;

class Performance {
    static void goal(final boolean isHome) {
        if (isHome) report.addHomeGoal();
        else report.addAwayGoal();

        final int homeGoals = report.getHomeGoals();
        final int awayGoals = report.getAwayGoals();
        int scoring = 3000;
        int assisting = 10000;
        Footballer goalscorer = null;
        Footballer assistmaker = null;
        final List<MatchStats> squad = isHome ? report.getHomeSquad() : report.getAwaySquad();

        for (int player = 0; player < 11; player++) {
            if (squad.get(player) == null || squad.get(player).isRedCarded()) continue;
            scoring += squad.get(player).getFootballer().getScoringChance();
            assisting += squad.get(player).getFootballer().getAssistChance();
        }

        int r = Simulator.getInt(scoring);
        for (int player = 0; player < 11; player++) {
            if (squad.get(player) == null || squad.get(player).isRedCarded()) continue;
            r -= squad.get(player).getFootballer().getScoringChance();
            if (r < 0) {
                goalscorer = squad.get(player).getFootballer();
                squad.get(player).addGoals();
                break;
            }
        }

        if (goalscorer == null) {
            int footballer = ownGoal();
            final List<MatchStats> opponent = isHome ? report.getAwaySquad() : report.getHomeSquad();
            if (opponent.get(footballer).isRedCarded()) footballer = 0;
            opponent.get(footballer).changeRating(-1.5f);
            report.append(String.valueOf(Match.minute)).append(Match.stoppage != 0 ? "+" + Match.stoppage : "")
                    .append("' ").append("Own goal scored by ").append(opponent.get(footballer).getFootballer().getName())
                    .append(". ").append(homeGoals).append("-").append(awayGoals).append("<br/>");
        }
        else {
            r = Simulator.getInt(assisting);
            for (int player = 0; player < 11; player++) {
                if (squad.get(player) == null || squad.get(player).isRedCarded()) continue;
                r -= squad.get(player).getFootballer().getAssistChance();
                if (r < 0) {
                    assistmaker = squad.get(player).getFootballer();
                    if (assistmaker.equals(goalscorer)) {
                        assistmaker = null;
                        squad.get(player).changeRating(0.25f);
                    } else squad.get(player).addAssists();

                    break;
                }
            }

            report.append(String.valueOf(Match.minute)).append(Match.stoppage != 0 ? "+" + Match.stoppage : "")
                    .append("' ").append(goalscorer.getName()).append(assistmaker != null
                    ? " scores after a pass from " + assistmaker.getName() : " scores after a solo run")
                    .append(". ").append(homeGoals).append("-").append(awayGoals).append("<br/>");
        }

        report.clearMomentum();
    }

    private static int ownGoal() {
        if (Simulator.isSatisfied(20)) return 0;
        if (Simulator.isSatisfied(60)) return 1 + Simulator.getInt(4);
        if (Simulator.isSatisfied(70)) return 5 + Simulator.getInt(3);
        return 8 + Simulator.getInt(3);
    }
}
