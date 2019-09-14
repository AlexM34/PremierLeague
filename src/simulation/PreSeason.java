package simulation;

import players.Footballer;
import players.Position;
import players.Resume;
import teams.Club;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static simulation.Data.LEAGUES;

class PreSeason {
    private static final Random random = new Random();
    private static int deals;
    private static Map<Footballer, Club> transfers;
    private static Map<Club, Integer> sold;
    private static int academy = 0;

    static void progression() {
        for (final Club[] league : LEAGUES) {
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
                        if (random.nextInt(42 - Math.min(f.getAge(), 41)) == 0) {
                            retire(f, club);
                            break;
                        }

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

    private static void retire(final Footballer footballer, final Club club) {
        System.out.println(footballer.getName() + "(" + footballer.getAge() + ") retires at " + club.getName());
        club.removeFootballer(footballer);
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

    private static void academy() {
        academy = 0;
        int goalkeepers = 0;
        int defenders = 0;
        int midfielders = 0;
        int forwards = 0;

        for (Club[] league : LEAGUES) {
            for (Club club : league) {
                youngster(club, null);
                youngster(club, null);
                for (Footballer footballer : club.getFootballers()) {
                    switch (footballer.getPosition().getRole()) {
                        case Goalkeeper: goalkeepers++; break;
                        case Defender: defenders++; break;
                        case Midfielder: midfielders++; break;
                        case Forward: forwards++; break;
                    }
                }

                for (int g = goalkeepers; g < 3; g++) youngster(club, Position.Role.Goalkeeper);
                for (int d = defenders; d < 7; d++) youngster(club, Position.Role.Defender);
                for (int m = midfielders; m < 7; m++) youngster(club, Position.Role.Midfielder);
                for (int f = forwards; f < 4; f++) youngster(club, Position.Role.Forward);
            }
        }

        System.out.println(academy + " youngsters promoted");
    }

    private static void youngster(final Club club, Position.Role role) {
        // TODO: Remove dummies
        if (role == null) {
            switch (random.nextInt(4)) {
                case 0: role = Position.Role.Goalkeeper; break;
                case 1: role = Position.Role.Defender; break;
                case 2: role = Position.Role.Midfielder; break;
                default: role = Position.Role.Forward; break;
            }
        }

        final int id = 1000000 + academy++;
        final String name = (char) ('A' + random.nextInt(26)) + ". " +
                Data.SURNAMES[random.nextInt(Data.SURNAMES.length)] + (academy - 1);
        final int age = 17 + random.nextInt(3);
        final String nationality = Data.NATIONS[random.nextInt(Data.NATIONS.length)];
        int overall = 60 + random.nextInt(10);
        int potential = overall + random.nextInt(15) + 10;
        float value = 3;
        float wage = 1;
        final Position.Role finalRole = role;
        Object[] positions = Arrays.stream(Position.values()).filter(p -> p.getRole().equals(finalRole)).toArray();
        Position position = (Position) positions[random.nextInt(positions.length)];
        int number = random.nextInt(97) + 2;
        int finishing = overall + 10 * position.getAttackingDuty() - 50;
        int vision = overall + 10 - (3 - position.getAttackingDuty()) * (3 - position.getAttackingDuty()) * 5;
        Resume resume = new Resume();
        Footballer footballer = new Footballer(id, name, age, nationality, overall, potential,
                value, wage, position, number, finishing, vision, resume);

        club.addFootballer(footballer);
//        System.out.println(footballer + " is promoted to " + club.getName());
    }

    static void profits(final Club[] league) {
        for (Club club : league) club.changeBudget(club.getReputation() * club.getReputation() / 100);
    }

    static void salaries(final Club[] league) {
        for (Club club : league) {
            for (Footballer footballer : club.getFootballers()) {
                club.changeBudget(-footballer.getWage() / 20);
            }
        }
    }
}
