package simulation.dynamics;

import static simulation.Data.LEAGUES;
import static simulation.Helper.sortLeague;

import player.Footballer;
import player.Position;
import player.Resume;
import player.Statistics;
import simulation.Data;
import simulation.Simulator;
import team.Club;
import team.League;
import team.Season;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Preseason {

    private static final PrintStream STREAM = new PrintStream(new FileOutputStream(FileDescriptor.out));
    
    static final Map<Footballer, Club> transfers = new HashMap<>();
    static final Map<Club, Integer> sold = new HashMap<>();
    protected static final List<Footballer> retired = new ArrayList<>();
    static int deals;
    private static int academy = 0;

    private Preseason() {
    }

    public static void prepare(final int year) {
        League.clearLeagueStats();

        STREAM.printf("Season %d-%d begins!%n", 2019 + year, 2020 + year);
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

        STREAM.println();
    }

    public static void progression() {
        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
                STREAM.println(club.getName());
                final Set<Footballer> retiring = new HashSet<>();
                for (final Footballer footballer : club.getFootballers()) {
                    footballer.incrementAge();

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
        Arrays.stream(LEAGUES).flatMap(Arrays::stream).forEach(Preseason::promote);
        STREAM.println(academy + " youngsters promoted");
    }

    private static void promote(final Club club) {
        int goalkeepers = 0;
        int defenders = 0;
        int midfielders = 0;
        int forwards = 0;

        youngster(club, null);
        youngster(club, null);

        for (final Footballer footballer : club.getFootballers()) {
            switch (footballer.getPosition().getRole()) {
                case GOALKEEPER: goalkeepers++; break;
                case DEFENDER: defenders++; break;
                case MIDFIELDER: midfielders++; break;
                case FORWARD: forwards++; break;
            }
        }

        for (int g = goalkeepers; g < 3; g++) youngster(club, Position.Role.GOALKEEPER);
        for (int d = defenders; d < 7; d++) youngster(club, Position.Role.DEFENDER);
        for (int m = midfielders; m < 7; m++) youngster(club, Position.Role.MIDFIELDER);
        for (int f = forwards; f < 4; f++) youngster(club, Position.Role.FORWARD);
    }

    public static void youngster(final Club club, Position.Role role) {
        club.addFootballer(generateFootballer(club.getName(), role));
    }

    private static Footballer generateFootballer(final String clubName, Position.Role role) {
        if (role == null) {
            switch (Simulator.getInt(4)) {
                case 0: role = Position.Role.GOALKEEPER; break;
                case 1: role = Position.Role.DEFENDER; break;
                case 2: role = Position.Role.MIDFIELDER; break;
                default: role = Position.Role.FORWARD; break;
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
        final long wage = 10000L * (1 + Simulator.getInt(5));
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

        for (int i = 0; i < 32; i++) {
            championsLeagueTeams[i] = sorted.get(i).getKey();
            STREAM.println(championsLeagueTeams[i]);
        }

        for (int i = 32; i < 80; i++) {
            europaLeagueTeams[i - 32] = sorted.get(i).getKey();
            STREAM.println(europaLeagueTeams[i - 32]);
        }
    }

}
