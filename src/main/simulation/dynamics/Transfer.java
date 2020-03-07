package main.simulation.dynamics;

import main.player.Footballer;
import main.player.Position;
import main.simulation.Simulator;
import main.team.Club;

import java.util.HashMap;
import java.util.Map;

import static main.simulation.Data.LEAGUES;
import static main.simulation.Helper.sortMap;
import static main.simulation.dynamics.Preseason.academy;
import static main.simulation.dynamics.Preseason.deals;
import static main.simulation.dynamics.Preseason.sold;
import static main.simulation.dynamics.Preseason.transfers;

public class Transfer {
    private static Map<String, Integer> summary = new HashMap<>();

    public static void transfers() {
        summary.clear();
        deals = 0;
        int attempts = 0;
        Club[] league;
        Club buying;
        Club selling;
        transfers.clear();
        sold.clear();

        while (attempts < 10000) {
            league = LEAGUES[Simulator.getInt(5)];
            buying = league[Simulator.getInt(league.length)];
            league = LEAGUES[Simulator.getInt(5)];
            selling = league[Simulator.getInt(league.length)];
            if (buying == selling || selling.getReputation() - buying.getReputation() > 25) continue;

            negotiate(buying, selling);
            attempts++;
        }

        System.out.println(deals + " transfers made");
        transfers.forEach(((footballer, club) -> club.addFootballer(footballer)));

        summary = sortMap(summary);
        int row = 0;
        for (final String news : summary.keySet()) {
            System.out.println(++row + ". " + news);
            if (row >= 50) break;
        }

        academy();
    }

    private static void negotiate(final Club buying, final Club selling) {
        if (Simulator.isSatisfied(selling.getReputation() - buying.getReputation(), 50)) return;

        float budget = buying.getBudget();
        final Footballer[] squad = selling.getFootballers().toArray(new Footballer[0]);
        final Footballer footballer = squad[Simulator.getInt(squad.length)];
        if (footballer.getId() > 123450 && footballer.getId() < 123470) return;

        float offered = interest(buying, footballer);
        if (offered == 0) return;
        float wanted = demand(selling, footballer);
        if (wanted == 0) return;

        while (offered < wanted) {
            if (Simulator.isSatisfied(67)) {
                if (Simulator.isSatisfied(75)) {
                    offered *= 1.1f;
                } else {
                    wanted /= 1.1f;
                }
            }

            else return;
        }

        if (1.5 * wanted < budget) {
            buying.changeBudget(-wanted);
            selling.changeBudget(wanted);
            selling.removeFootballer(footballer);
            footballer.setTeam(buying.getName());
            transfers.put(footballer, buying);
            sold.merge(buying, 1, Integer::sum);
            deals++;

            summary.put(footballer.getName() + " joins " + buying.getName() + " from " +
                selling.getName() + " for €" + (int) wanted + " million", (int) wanted);
        }
    }

    private static float interest(final Club buying, final Footballer footballer) {
        final Position.Role role = footballer.getPosition().getRole();
        final int potential = footballer.getPotential();
        int better = 0;

        for (final Footballer f : buying.getFootballers()) {
            if (f.getPosition().getRole().equals(role) && f.getOverall() > potential) better++;
        }

        switch (better) {
            case 0: return footballer.getValue() * 1.4f;
            case 1: return footballer.getValue() * 1.2f;
            case 2: return footballer.getValue() * 1f;
            case 3: return footballer.getValue() * 0.9f;
            default: return 0;
        }
    }

    private static float demand(final Club selling, final Footballer footballer) {
        final Position.Role role = footballer.getPosition().getRole();
        final int potential = footballer.getPotential();
        int better = 0;

        for (final Footballer f : selling.getFootballers()) {
            if (f.getPosition().getRole().equals(role) && f.getOverall() > potential) better++;
        }

        if (sold.getOrDefault(selling, 0) > 4) return footballer.getValue() * 2.5f;

        switch (better) {
            case 0: return Simulator.isSatisfied(67) ? 0 : footballer.getValue() * 2f;
            case 1: return footballer.getValue() * 1.8f;
            case 2: return footballer.getValue() * 1.4f;
            default: return footballer.getValue() * 1.2f;
        }
    }
}
