package simulation.dynamics;

import player.Footballer;
import player.Position;
import player.Resume;
import player.Statistics;
import simulation.competition.Continental;
import team.Cup;
import simulation.Data;
import team.Club;
import team.League;
import team.Season;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static simulation.Data.LEAGUES;
import static simulation.Helper.sortLeague;

public class Preseason {
    private static final Random random = new Random();
    static int deals;
    static final Map<Footballer, Club> transfers = new HashMap<>();
    static final Map<Club, Integer> sold = new HashMap<>();
    private static int academy = 0;

    public static void prepare(final int year) {
        League.clearLeagueStats();

        System.out.println(String.format("Season %d-%d begins!", 2019 + year, 2020 + year));
        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
                club.setSeason(new Season(new League(club.getLeague()), new Cup(), new Cup(),
                        new Continental(), 100, 100));

                for (final Footballer f : club.getFootballers()) {
                    final Statistics career = f.getResume().getTotal();
                    final Statistics season = f.getResume().getSeason();

                    career.update(season);
                    season.clear();
                }
            }
        }

        System.out.println();
    }

    public static void progression() {
        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
                for (final Footballer f : club.getFootballers()) {
                    if (f.getNumber() > 100) continue;
                    f.changeCondition(100);

                    if (random.nextInt(2) == 0) {
                        if (f.getResume().getSeason().getLeague().getRating() > 750) {
                            improve(f);
                        } else if (f.getResume().getSeason().getLeague().getRating() < 650) {
                            decline(f);
                        }
                    }

                    final int r = random.nextInt(6);
                    if (r == 0) improve(f);
                    else if (r == 1) decline(f);

                    f.setAge(f.getAge() + 1);

                    if (f.getAge() > 34) {
                        if (random.nextInt(42 - Math.min(f.getAge(), 41)) == 0) {
                            retire(f, club);
                            break;
                        }

                        if (random.nextInt(2) == 0) {
                            decline(f);
                            decline(f);
                        }
                    }

                    if (f.getAge() > 30) {
                        if (random.nextInt(2) == 0) {
                            decline(f);
                            decline(f);
                        }
                    }

                    if (f.getAge() < 20) {
                        improve(f);
                        if (random.nextInt(2) == 0) {
                            improve(f);
                            improve(f);
                        }
                    }

                    if (f.getAge() < 25) {
                        if (random.nextInt(3) == 0) {
                            improve(f);
                            improve(f);
                        }
                    }

                    if (f.getOverall() + 10 < f.getPotential()) {
                        if (random.nextInt(2) == 0) {
                            improve(f);
                            improve(f);
                        }
                    }
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
        }
    }

    private static void decline(final Footballer footballer) {
        if (footballer.getOverall() > 0) {
            footballer.changeOverall(-1);
            if (footballer.getAge() >= 30 || random.nextInt(3) == 0) {
                footballer.changePotential(-1);
            }
        }
    }

    static void academy() {
        academy = 0;
        int goalkeepers = 0;
        int defenders = 0;
        int midfielders = 0;
        int forwards = 0;

        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
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
        final int overall = 60 + random.nextInt(10);
        final int potential = overall + random.nextInt(15) + 10;
        final long value = 3;
        final long wage = 1;
        final Position.Role finalRole = role;
        final Object[] positions = Arrays.stream(Position.values()).filter(p -> p.getRole().equals(finalRole)).toArray();
        final Position position = (Position) positions[random.nextInt(positions.length)];
        final int number = random.nextInt(97) + 2;
        final int finishing = overall + 10 * position.getAttackingDuty() - 50;
        final int vision = overall + 10 - (3 - position.getAttackingDuty()) * (3 - position.getAttackingDuty()) * 5;
        final Footballer footballer = new Footballer(id, name, age, nationality, overall, potential,
                club.getName(), value, wage, position, number, finishing, vision, new Resume());

        club.addFootballer(footballer);
    }

    public static void pickContinentalTeams(final Club[] championsLeagueTeams, final Club[] europaLeagueTeams) {
        final Map<Club, Integer> teams = new LinkedHashMap<>();
        for (final Club[] league : LEAGUES) {
            final List<Club> sorted = sortLeague(league, 0);
            int slots = 16;
            for (final Club team : sorted) {
                teams.put(team, team.getSeason().getLeague().getPoints() + random.nextInt(5) + slots--);
                if (slots == 0) break;
            }
        }

        final List<Map.Entry<Club, Integer>> sorted = new ArrayList<>(teams.entrySet());
        sorted.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        int limit = 0;

        for (final Map.Entry<Club, Integer> clubIntegerEntry : sorted) {
            if (limit < 32) System.out.println(championsLeagueTeams[limit] = clubIntegerEntry.getKey());
            else if (limit < 80) System.out.println(europaLeagueTeams[limit - 32] = clubIntegerEntry.getKey());
            else break;

            limit++;
        }
    }
}
