package simulation;

import players.Footballer;
import players.Position;
import teams.Club;

import java.util.HashMap;
import java.util.Random;

import static simulation.Data.LEAGUES;
import static simulation.PreSeason.academy;
import static simulation.PreSeason.deals;
import static simulation.PreSeason.sold;
import static simulation.PreSeason.transfers;

class Transfers {
    private static final Random random = new Random();

    static void transfers() {
        deals = 0;
        int attempts = 0;
        Club[] league;
        Club buying;
        Club selling;
        transfers = new HashMap<>();
        sold = new HashMap<>();

        while (attempts < 30000) {
            league = LEAGUES[random.nextInt(5)];
            buying = league[random.nextInt(league.length)];
            league = LEAGUES[random.nextInt(5)];
            selling = league[random.nextInt(league.length)];
            if (buying == selling) continue;

            negotiate(buying, selling);
            attempts++;
        }

        System.out.println(deals + " transfers made");
        transfers.forEach(((footballer, club) -> club.addFootballer(footballer)));
        academy();
    }

    private static void negotiate(final Club buying, final Club selling) {
        float budget = buying.getBudget();
        Footballer[] squad = selling.getFootballers().toArray(new Footballer[0]);
        Footballer footballer = squad[random.nextInt(squad.length)];
        if (footballer.getId() > 123450 && footballer.getId() < 123470) return;

        float offered = interest(buying, footballer);
        if (offered == 0) return;
        float wanted = demand(selling, footballer);

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

        if (wanted < budget) {
            buying.changeBudget(-wanted);
            selling.changeBudget(wanted);
            selling.removeFootballer(footballer);
            transfers.put(footballer, buying);
            sold.merge(buying, 1, Integer::sum);
            deals++;

//            System.out.println(deals + ". " + footballer.getName() + " joins " + buying.getName() + " from " +
//                selling.getName() + " for €" + (int) wanted + " million");
        }
    }

    private static float interest(final Club buying, final Footballer footballer) {
        final Position.Role role = footballer.getPosition().getRole();
        final int potential = footballer.getPotential();
        int better = 0;

        for (Footballer f : buying.getFootballers()) {
            if (f.getPosition().getRole().equals(role) && f.getOverall() > potential) better++;
        }

        switch (better) {
            case 0: return footballer.getValue() * 1f;
            case 1: return footballer.getValue() * 0.8f;
            default: return 0;
        }
    }

    private static float demand(final Club selling, final Footballer footballer) {
        final Position.Role role = footballer.getPosition().getRole();
        final int potential = footballer.getPotential();
        int better = 0;

        for (Footballer f : selling.getFootballers()) {
            if (f.getPosition().getRole().equals(role) && f.getOverall() > potential) better++;
        }

        if (sold.containsKey(selling) && sold.get(selling) > 5) return footballer.getValue() * 2.5f;

        switch (better) {
            case 0: return footballer.getValue() * 2f;
            case 1: return footballer.getValue() * 1.5f;
            default: return footballer.getValue() * 1.2f;
        }
    }
}
