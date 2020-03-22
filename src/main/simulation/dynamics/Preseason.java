package simulation.dynamics;

import player.Footballer;
import player.Position;
import player.Resume;
import player.Statistics;
import simulation.Data;
import simulation.Simulator;
import team.Club;
import team.League;
import team.Season;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static simulation.Data.LEAGUES;
import static simulation.Helper.sortLeague;

public class Preseason {
    static final Map<Footballer, Club> transfers = new HashMap<>();
    static final Map<Club, Integer> sold = new HashMap<>();
    public static final List<Footballer> retired = new ArrayList<>();
    static int deals;
    private static int academy = 0;

    public static void prepare(final int year) {
        League.clearLeagueStats();

        System.out.println(String.format("Season %d-%d begins!", 2019 + year, 2020 + year));
        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
                club.setSeason(new Season(club.getLeague()));

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
                System.out.println(club.getName());
                final Set<Footballer> retiring = new HashSet<>();
                for (final Footballer footballer : club.getFootballers()) {
                    footballer.increaseAge();

                    if (footballer.getTeam().equals("")) retiring.add(footballer);
                }

                for (final Footballer footballer : retiring) {
                    club.removeFootballer(footballer);
                    retired.add(footballer);
                }
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

                for (final Footballer footballer : club.getFootballers()) {
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

    public static void youngster(final Club club, Position.Role role) {
        club.addFootballer(generateFootballer(club.getName(), role));
    }

    private static Footballer generateFootballer(final String clubName, Position.Role role) {
        if (role == null) {
            switch (Simulator.getInt(4)) {
                case 0: role = Position.Role.Goalkeeper; break;
                case 1: role = Position.Role.Defender; break;
                case 2: role = Position.Role.Midfielder; break;
                default: role = Position.Role.Forward; break;
            }
        }

        final int id = 1000000 + academy++;
        final String name = (char) ('A' + Simulator.getInt(26)) + ". " +
                Data.SURNAMES[Simulator.getInt(Data.SURNAMES.length)] + (academy - 1);
        final int age = 17 + Simulator.getInt(3);
        final String nationality = Data.NATIONS[Simulator.getInt(Data.NATIONS.length)];
        final int overall = 60 + Simulator.getInt(10);
        final int potential = overall + Simulator.getInt(15) + 10;
        final long value = 3;
        final long wage = 10000 * (1 + Simulator.getInt(5));
        final Position.Role finalRole = role;
        final Object[] positions = Arrays.stream(Position.values()).filter(p -> p.getRole().equals(finalRole)).toArray();
        final Position position = (Position) positions[Simulator.getInt(positions.length)];
        final int number = Simulator.getInt(97) + 2;
        final int finishing = overall + 10 * position.getAttackingDuty() - 50;
        final int vision = overall + 10 - (3 - position.getAttackingDuty()) * (3 - position.getAttackingDuty()) * 5;

        return new Footballer(id, name, age, nationality, overall, potential,
                clubName, value, wage, position, number, finishing, vision, new Resume());
    }

    public static void pickContinentalTeams(final Club[] championsLeagueTeams, final Club[] europaLeagueTeams) {
        final Map<Club, Integer> teams = new LinkedHashMap<>();
        for (final Club[] league : LEAGUES) {
            final List<Club> sorted = sortLeague(league, 0);
            int slots = 16;
            for (final Club team : sorted) {
                teams.put(team, team.getSeason().getLeague().getPoints() + Simulator.getInt(5) + slots--);
                if (slots == 0) break;
            }
        }

        final List<Map.Entry<Club, Integer>> sorted = new ArrayList<>(teams.entrySet());
        sorted.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        for (int i = 0; i < 32; i++) System.out.println(championsLeagueTeams[i] = sorted.get(i).getKey());
        for (int i = 32; i < 80; i++) System.out.println(europaLeagueTeams[i - 32] = sorted.get(i).getKey());
    }
}
