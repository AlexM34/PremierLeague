package simulation;

import players.Footballer;
import players.Position;
import team.Club;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static simulation.Data.LEAGUES;
import static simulation.Preseason.academy;
import static simulation.Preseason.deals;
import static simulation.Preseason.sold;
import static simulation.Preseason.transfers;
import static simulation.Helper.sortMap;

class Transfer {
    private static final Random random = new Random();
    private static Map<String, Integer> summary = new HashMap<>();

    static void transfers() {
        summary.clear();
        deals = 0;
        int attempts = 0;
        Club[] league;
        Club buying;
        Club selling;
        transfers.clear();
        sold.clear();

        while (attempts < 10000) {
            league = LEAGUES[random.nextInt(5)];
            buying = league[random.nextInt(league.length)];
            league = LEAGUES[random.nextInt(5)];
            selling = league[random.nextInt(league.length)];
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
        if (random.nextInt(50) < selling.getReputation() - buying.getReputation()) return;

        float budget = buying.getBudget();
        final Footballer[] squad = selling.getFootballers().toArray(new Footballer[0]);
        final Footballer footballer = squad[random.nextInt(squad.length)];
        if (footballer.getId() > 123450 && footballer.getId() < 123470) return;

        float offered = interest(buying, footballer);
        if (offered == 0) return;
        float wanted = demand(selling, footballer);
        if (wanted == 0) return;

        while (offered < wanted) {
            if (random.nextInt(3) != 0) {
                if (random.nextInt(4) != 0) {
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
            case 0: return random.nextInt(3) != 0 ? 0 : footballer.getValue() * 2f;
            case 1: return footballer.getValue() * 1.8f;
            case 2: return footballer.getValue() * 1.4f;
            default: return footballer.getValue() * 1.2f;
        }
    }
}
