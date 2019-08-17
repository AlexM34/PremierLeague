import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class PreSeason {
    // TODO: Retirements
    private static final Random random = new Random();
    private static int deals = 0;
    private static Map<Footballer, Club> transfers;

    static void progression() {
        // TODO: Transfers
        // TODO: Youth academy
        for (final Club[] league : Data.LEAGUES) {
            for (final Club club : league) {
                for (final Footballer f : club.getFootballers()) {
                    if (f.getNumber() > 100) continue;
                    f.changeCondition(100);
                    int r = random.nextInt(2);
                    if (r == 0) {
                        if (f.getResume().getSeason().getLeague().getRating() > 750) {
                            improve(f);
                        } else if (f.getResume().getSeason().getLeague().getRating() < 650) {
                            decline(f);
                        }
                    }
                    r = random.nextInt(6);
                    if (r == 0) {
                        improve(f);
                    } else if (r == 1) {
                        decline(f);
                    }

                    f.setAge(f.getAge() + 1);

                    if (f.getAge() > 34) {
                        r = random.nextInt(2);
                        if (r == 0) {
                            decline(f);
                            decline(f);
                        }
                    }

                    if (f.getAge() > 30) {
                        r = random.nextInt(2);
                        if (r == 0) {
                            decline(f);
                            decline(f);
                        }
                    }

                    if (f.getAge() < 20) {
                        improve(f);
                        r = random.nextInt(2);
                        if (r == 0) {
                            improve(f);
                            improve(f);
                        }
                    }

                    if (f.getAge() < 25) {
                        r = random.nextInt(3);
                        if (r == 0) {
                            improve(f);
                            improve(f);
                        }
                    }

                    if (f.getOverall() + 10 < f.getPotential()) {
                        r = random.nextInt(2);
                        if (r == 0) {
                            improve(f);
                            improve(f);
                        }
                    }
//                System.out.println(f.getName() + f.getOverall());
                }
            }
        }
    }

    private static void improve(final Footballer footballer) {
        if (footballer.getOverall() < footballer.getPotential() && footballer.getOverall() < 100) {
            footballer.changeOverall(1);
            if (footballer.getAge() < 30 && random.nextInt(3) == 0) {
                footballer.changePotential(1);
            }
//            System.out.println(String.format("%s improves to %d %d", footballer.getName(), footballer.getOverall(), footballer.getPotential()));
        }
    }

    private static void decline(final Footballer footballer) {
        if (footballer.getOverall() > 0) {
            footballer.changeOverall(-1);
            if (footballer.getAge() >= 30 || random.nextInt(3) == 0) {
                footballer.changePotential(-1);
            }
//            System.out.println(String.format("%s declines to %d %d", footballer.getName(), footballer.getOverall(), footballer.getPotential()));
        }
    }

    static void transfers() {
        int attempts = 0;
        Club[] league;
        Club buying;
        Club selling;
        transfers = new HashMap<>();

        while (attempts < 10000) {
            league = Data.LEAGUES[random.nextInt(5)];
            buying = league[random.nextInt(league.length)];
            league = Data.LEAGUES[random.nextInt(5)];
            selling = league[random.nextInt(league.length)];
            if (buying == selling) continue;

            negotiate(buying, selling);
            attempts++;
        }

        transfers.forEach(((footballer, club) -> club.addFootballer(footballer)));
    }

    private static void negotiate(final Club buying, final Club selling) {
        // TODO: No dummy transfers
        int budget = buying.getBudget();
        Footballer[] squad = selling.getFootballers().toArray(new Footballer[0]);
        Footballer footballer = squad[random.nextInt(squad.length)];

        float offered = interest(buying, footballer);
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
            buying.changeBudget((int) -wanted);
            selling.changeBudget((int) wanted);
            selling.removeFootballer(footballer);
            transfers.put(footballer, buying);
            deals++;

            System.out.println(deals + ". " + footballer.getName() + " joins " + buying.getName() + " from " +
                selling.getName() + " for €" + (int) wanted + " million");
        }
    }

    private static float interest(final Club buying, final Footballer footballer) {
        return footballer.getValue() * 0.8f;
    }

    private static float demand(final Club selling, final Footballer footballer) {
        return footballer.getValue() * 1.3f;
    }
}
