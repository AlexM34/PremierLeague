package simulation;

import players.Footballer;
import players.Position;
import players.Resume;
import team.Club;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static simulation.Data.LEAGUES;

class PreSeason {
    private static final Random random = new Random();
    static int deals;
    static final Map<Footballer, Club> transfers = new HashMap<>();
    static final Map<Club, Integer> sold = new HashMap<>();
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

    static void academy() {
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
        long value = 3;
        long wage = 1;
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
}
