import java.util.Random;

class PreSeason {
    private static final Random random = new Random();

    static void changes() {
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
}
