package simulation.match;

import player.Footballer;
import simulation.Simulator;

public class Penalty {
    
    public static int take(final Footballer striker, final Footballer goalkeeper, final Report report) {
        report.append(striker.getName()).append(" steps up to take the penalty against ")
                .append(goalkeeper.getName()).append("<br/>");

        if (Simulator.isSatisfied(70 + striker.getOverall() -
                goalkeeper.getOverall())) {

            report.append("He scores with a great shot! ");
            return 1;

        } else {
            report.append("The goalkeeper makes a wonderful save! ");
            return 0;
        }
    }
}
