package simulation.match;

import player.Footballer;
import simulation.Simulator;

public class Penalty {
    
    public static int take(final Footballer striker, final Footballer goalkeeper, final Report report) {
        report.append(striker.getName()).append(" steps up to take the penalty against ")
                .append(goalkeeper.getName()).append("<br/>");

        final int percentage = calculateChance(striker, goalkeeper);

        if (Simulator.isSatisfied(percentage)) {

            report.append("He scores with a great shot! ");
            return 1;

        } else {
            report.append("The goalkeeper makes a wonderful save! ");
            return 0;
        }
    }

    static int calculateChance(final Footballer striker, final Footballer goalkeeper) {
        final int attack = striker.getOverall() + striker.getFinishing() / 3;
        final int defence = 40 + goalkeeper.getOverall() / 3;

        return Math.max(30, Math.min(90, 50 * attack / defence));
    }
}
