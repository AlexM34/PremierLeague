package simulation;

import players.Competition;
import players.Footballer;
import players.Statistics;
import team.Club;
import team.League;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static players.Position.GK;
import static simulation.Controller.matchday;
import static simulation.Data.LEAGUES;
import static simulation.Finance.leaguePrizes;
import static simulation.Helper.sortLeague;
import static simulation.Helper.sortMap;

public class Printer {
    private static final Random random = new Random();

    public static Map<Footballer, Integer> ratings = new LinkedHashMap<>();
    public static Map<Footballer, Integer> goals = new LinkedHashMap<>();
    public static Map<Footballer, Integer> assists = new LinkedHashMap<>();
    public static Map<Footballer, Integer> cleanSheets = new LinkedHashMap<>();
    private static final Map<Footballer, Integer> motm = new LinkedHashMap<>();
    private static final Map<Footballer, Integer> yellowCards = new LinkedHashMap<>();
    private static final Map<Footballer, Integer> redCards = new LinkedHashMap<>();
    private static final Map<Footballer, Integer> topTeam = new LinkedHashMap<>();

    static int offset;

    static void pickContinentalTeams(final Club[] championsLeagueTeams, final Club[] europaLeagueTeams) {
        final Map<Club, Integer> teams = new LinkedHashMap<>();
        for (final Club[] league : LEAGUES) {
            final Map<Club, Integer> sorted = sortLeague(league, 0);
            int slots = 16;
            for (final Club team : sorted.keySet()) {
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

    static void standings(final Club[] league) {
        final Map<Club, Integer> sorted = sortLeague(league, 0);

        int position = 1;
        System.out.println(String.format("No  Teams %" + (offset - 3) + "s G  W  D  L   GF:GA  P", ""));
        for (final Club team : sorted.keySet()) {
            final League stats = team.getSeason().getLeague();
            System.out.println(String.format("%2d. %-" + (offset + 3) + "s %-2d %-2d %-2d %-2d %3d:%-3d %-3d", position++,
                    team.getName(), stats.getMatches(), stats.getWins(), stats.getDraws(),  stats.getLosses(),
                    stats.getScored(), stats.getConceded(), stats.getPoints()));
        }

        final Club first = sorted.keySet().toArray(new Club[0])[0];
        if (first.getSeason().getLeague().getMatches() == 2 * league.length - 2) {
            first.getGlory().addLeague();
            for (final Footballer footballer : first.getFootballers()) {
                footballer.getResume().getGlory().addLeague();
            }

            reputationUpdate(sorted.keySet().toArray(new Club[0]));
            leaguePrizes(sorted.keySet().toArray(new Club[0]));
        }
    }

    private static void reputationUpdate(final Club[] league) {
        int performance = 95;
        for (final Club club : league) {
            club.changeReputation(performance);
            performance -= 4;
        }
    }

    public static void playerStats(final Club[] league, final int type, final boolean seasonal) {
        ratings.clear();
        motm.clear();
        goals.clear();
        assists.clear();
        cleanSheets.clear();
        yellowCards.clear();
        redCards.clear();
        topTeam.clear();

        for (final Club club : league) {
            for (final Footballer f : club.getFootballers()) {
                final Statistics stats = seasonal ? f.getResume().getSeason() : f.getResume().getTotal();
                final Competition competition;
                final int games;

                switch (type) {
                    case 0:
                        competition = stats.getLeague();
                        games = matchday / 2;
                        break;
                    case 1:
                        competition = stats.getNationalCup();
                        games = matchday / 18;
                        break;
                    case 2:
                        competition = stats.getLeagueCup();
                        games = matchday / 18;
                        break;
                    case 3:
                        competition = stats.getChampionsLeague();
                        games = matchday / 8;
                        break;
                    default:
                        competition = stats.getEuropaLeague();
                        games = matchday / 8;
                        break;
                }

                if (competition.getMatches() > games) {
                    ratings.put(f, competition.getRating());
                    topTeam.put(f, competition.getRating());
                }

                motm.put(f, competition.getMotmAwards());
                goals.put(f, competition.getGoals());
                assists.put(f, competition.getAssists());
                yellowCards.put(f, competition.getYellowCards());
                redCards.put(f, competition.getRedCards());

                if (f.getPosition() == GK) cleanSheets.put(f, competition.getCleanSheets());
            }
        }

        goals = sortMap(goals);
        assists = sortMap(assists);
        ratings = sortMap(ratings);
        cleanSheets = sortMap(cleanSheets);
    }

    public static Map<String, Integer> allTime(final Club[] league, final int competition) {
        final Map<String, Integer> winners = new LinkedHashMap<>();

        for (final Club club : league) {
            final int cups;
            switch (competition) {
                case 0: cups = club.getGlory().getLeague(); break;
                case 1: cups = club.getGlory().getNationalCup(); break;
                case 2: cups = club.getGlory().getLeagueCup(); break;
                case 3: cups = club.getGlory().getChampionsLeague(); break;
                case 4: cups = club.getGlory().getEuropaLeague(); break;
                default: cups = 0; break;
            }

            if (cups == 0) continue;
            winners.put(club.getName(), cups);
        }

        final Map<String, Integer> sorted = sortMap(winners);

        System.out.println();
        System.out.println("Winners");
        for (int i = 0; i < sorted.size(); i++) {
            System.out.println(String.format("%2d. %-25s %d", i + 1,
                    sorted.keySet().toArray(new String[0])[i], sorted.values().toArray(new Integer[0])[i]));
        }

        return sorted;
    }

    public static List<Footballer> topPlayers() {
        final Map<Footballer, Integer> players = new HashMap<>();
        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
                for (final Footballer f : club.getFootballers()) {
                    players.put(f, f.getOverall());
                }
            }
        }

        final Map<Footballer, Integer> sorted = sortMap(players);
        return new ArrayList<>(sorted.keySet());
    }

    static void pickTeam(final Map<Footballer, Integer> ratings, final boolean isLeague) {
        topTeam.clear();
        int[] team = new int[11];
        int goalkeepers = 1;
        int defenders = 4;
        int midfielders = 3;
        int forwards = 3;

        final Map<Footballer, Integer> sorted = sortMap(ratings);

        for (int player = 0; player < sorted.size(); player++) {
            switch (sorted.keySet().toArray(new Footballer[0])[player].getPosition().getRole()) {
                case Goalkeeper:
                    if (goalkeepers > 0) team[--goalkeepers] = player;
                    break;
                case Defender:
                    if (defenders > 0) team[defenders--] = player;
                    break;
                case Midfielder:
                    if (midfielders > 0) team[4 + midfielders--] = player;
                    break;
                case Forward:
                    if (forwards > 0) team[7 + forwards--] = player;
                    break;
            }

            if (goalkeepers + defenders + midfielders + forwards == 0) break;
        }

        System.out.println("TEAM OF THE SEASON");
        for (int player = 0; player < 11; player++) {
            Footballer footballer = sorted.keySet().toArray(new Footballer[0])[team[player]];
            int rating = sorted.values().toArray(new Integer[0])[team[player]];

            System.out.println(String.format("%2d. %-20s %2d", player + 1, footballer.getName(), rating));

            if (isLeague) topTeam.put(footballer, rating);
        }

        System.out.println();
    }

    static void voting(final Map<Footballer, Integer> contenders) {
        Footballer[] players = new Footballer[10];
        int[] chance = new int[10];
        int[] votes = new int[10];
        int total = 0;
        int current = 0;
        for (Map.Entry<Footballer, Integer> entry : contenders.entrySet()) {
            Footballer c = entry.getKey();
            Integer p = entry.getValue();
            System.out.println(c.getName() + " with " + p);

            players[current] = c;
            chance[current] = Math.max(p - 750, 1);
            total += chance[current++];
        }

        for (int v = 0; v < 100; v++) {
            int pick = random.nextInt(total);
            for (int player = 0; player < 10; player++) {
                pick -= chance[player];
                if (pick < 0) {
                    votes[player]++;
                    break;
                }
            }
        }

        Map<Footballer, Integer> results = new HashMap<>();
        for (int i = 0; i < 10; i++) results.put(players[i], votes[i]);

        final Map<Footballer, Integer> sorted = sortMap(results);

        System.out.println("FOOTBALLER OF THE YEAR");
        for (int player = 0; player < 10; player++) {
            Footballer footballer = sorted.keySet().toArray(new Footballer[0])[player];
            int count = sorted.values().toArray(new Integer[0])[player];

            System.out.println(String.format("%2d. %-20s %2d", player + 1, footballer.getName(), count));
        }

        System.out.println();
    }

    static void review(final Club club) {
        System.out.println("Seasonal performance review of " + club.getName());
        System.out.println("League");
        club.getSeason().getLeague().getFixtures()
                .forEach(f -> System.out.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));

        System.out.println();
        System.out.println("National Cup");
        club.getSeason().getNationalCup().getFixtures()
                .forEach(f -> System.out.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));

        System.out.println();
        System.out.println("League Cup");
        club.getSeason().getLeagueCup().getFixtures()
                .forEach(f -> System.out.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));

        System.out.println();
        System.out.println("Continental");
        club.getSeason().getContinental().getGroup().getFixtures()
                .forEach(f -> System.out.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));
        System.out.println();
        club.getSeason().getContinental().getKnockout().getFixtures()
                .forEach(f -> System.out.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));
        System.out.println();
    }
}
