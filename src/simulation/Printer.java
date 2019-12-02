package simulation;

import competitions.England;
import competitions.France;
import players.Competition;
import players.Footballer;
import teams.Club;
import teams.League;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static players.Position.GK;
import static simulation.Data.LEAGUES;
import static simulation.Finances.leaguePrizes;
import static simulation.Utils.sortLeague;
import static simulation.Utils.sortMap;

public class Printer {
    private static final Random random = new Random();
    public static Map<Footballer, Integer> ratings = new LinkedHashMap<>();
    private static Map<Footballer, Integer> motm = new LinkedHashMap<>();
    public static Map<Footballer, Integer> goals = new LinkedHashMap<>();
    public static Map<Footballer, Integer> assists = new LinkedHashMap<>();
    public static Map<Footballer, Integer> cleanSheets = new LinkedHashMap<>();
    private static Map<Footballer, Integer> yellowCards = new LinkedHashMap<>();
    private static Map<Footballer, Integer> redCards = new LinkedHashMap<>();
    static Map<Footballer, Integer> topTeam = new LinkedHashMap<>();
    static int offset;

    static Club[] pickChampionsLeagueTeams() {
        final Map<Club, Integer> teams = new LinkedHashMap<>();
        for (final Club[] league : LEAGUES) {
            final Map<Club, Integer> sorted = sortLeague(league);
            int slots = 7;
            for (final Club team : sorted.keySet()) {
                teams.put(team, team.getSeason().getLeague().getPoints());
                slots--;
                if (slots == 0) break;
            }
        }

        final List<Map.Entry<Club, Integer>> toSort = new ArrayList<>(teams.entrySet());
        toSort.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        final Club[] selected = new Club[32];
        int limit = 0;

        for (final Map.Entry<Club, Integer> clubIntegerEntry : toSort) {
            selected[limit] = clubIntegerEntry.getKey();
            if (limit++ == 31) break;
        }

        for (final Club team : selected) {
            team.getSeason().getChampionsLeague().setAlive(true);
        }

        return selected;
    }

    static void standings(final Club[] league) {
        int goals = 0;
        final Map<Club, Integer> sorted = sortLeague(league);

        int position = 1;
        System.out.println(String.format("No  Teams %" + (offset - 3) + "s G  W  D  L   GF:GA  P", ""));
        for (final Club team : sorted.keySet()) {
            final League stats = team.getSeason().getLeague();
            goals += stats.getScored();
            System.out.println(String.format("%2d. %-" + (offset + 3) + "s %-2d %-2d %-2d %-2d %3d:%-3d %-3d", position++,
                    team.getName(), stats.getMatches(), stats.getWins(), stats.getDraws(),  stats.getLosses(),
                    stats.getScored(), stats.getConceded(), stats.getPoints()));
        }
//        System.out.println();
//        System.out.println("Total goals for the season: " + goals + " // 1072");
//        System.out.println(homeWins + " - " + (380 - homeWins - awayWins) +
//                " - " + awayWins + " // 181 - 71 - 128");
//        System.out.println();
//        System.out.println("Average rating for the season: " + RATINGS / (22 * 380));
//        System.out.println();

        final Club first = sorted.keySet().toArray(new Club[0])[0];

        if (first.getSeason().getLeague().getMatches() == 2 * league.length - 2) {
            first.getGlory().addLeague();
            for (final Footballer footballer : first.getFootballers()) {
                footballer.getResume().getGlory().addLeague();
            }

            leaguePrizes(sorted.keySet().toArray(new Club[0]));
        }
    }

    public static void playerStats(final Club[] league, final int type) {
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
                final Competition stats;
                final int games;

                switch (type) {
                    case 0:
                        stats = f.getResume().getSeason().getLeague();
                        games = club.getSeason().getLeague().getMatches() / 2;
                        break;
                    case 1:
                        stats = f.getResume().getSeason().getCup();
                        games = 1;
                        break;
                    default:
                        stats = f.getResume().getSeason().getContinental();
                        games = 1;
                        break;
                }

                if (stats.getMatches() >= games) {
                    ratings.put(f, stats.getRating());
                    topTeam.put(f, stats.getRating());
                }

                motm.put(f, stats.getMotmAwards());
                goals.put(f, stats.getGoals());
                assists.put(f, stats.getAssists());
                yellowCards.put(f, stats.getYellowCards());
                redCards.put(f, stats.getRedCards());

                if (f.getPosition() == GK) {
                    cleanSheets.put(f, stats.getCleanSheets());
                }
            }
        }

        goals = sortMap(goals);
        assists = sortMap(assists);
        ratings = sortMap(ratings);
        cleanSheets = sortMap(cleanSheets);

//        System.out.println();
//        topPlayers(ratings, "Top Players");
//        topPlayers(motm, "Most MOTM Awards");
//        topPlayers(goals, "Top Goalscorer");
//        topPlayers(assists, "Most Assists");
//        topPlayers(cleanSheets, "Most Clean Sheets");
//        topPlayers(yellowCards, "Most Yellow Cards");
//        topPlayers(redCards, "Most Red Cards");

//        pickTeam(topTeam, type == 0);
    }

    static void allTimeStats(final Club[] league) {
        final Map<String, Integer> leagues = new LinkedHashMap<>();
        final Map<String, Integer> nationalCups = new LinkedHashMap<>();
        final Map<String, Integer> leagueCups = new LinkedHashMap<>();

        for (final Club club : league) {
            leagues.put(club.getName(), club.getGlory().getLeague());
            nationalCups.put(club.getName(), club.getGlory().getNationalCup());
            leagueCups.put(club.getName(), club.getGlory().getLeagueCup());
        }

        final Map<String, Integer> sortedLeagues = sortMap(leagues);
        final Map<String, Integer> sortedNationalCups = sortMap(nationalCups);
        final Map<String, Integer> sortedLeagueCups = sortMap(leagueCups);

        System.out.println();
        System.out.println("League Winners");
        for (int i = 0; i < sortedLeagues.size(); i++) {
            System.out.println(String.format("%2d. %-25s %d", i + 1, sortedLeagues.keySet().toArray(new String[0])[i],
                    sortedLeagues.values().toArray(new Integer[0])[i]));
        }

        System.out.println();
        System.out.println("National Cup Winners");
        for (int i = 0; i < sortedLeagueCups.size(); i++) {
            System.out.println(String.format("%2d. %-25s %d", i + 1, sortedNationalCups.keySet().toArray(new String[0])[i],
                    sortedNationalCups.values().toArray(new Integer[0])[i]));
        }

        if (league[0].getLeague().equals(England.LEAGUE) || league[0].getLeague().equals(France.LEAGUE)) {
            System.out.println();
            System.out.println("League Cup Winners");
            for (int i = 0; i < sortedLeagueCups.size(); i++) {
                System.out.println(String.format("%2d. %-25s %d", i + 1, sortedLeagueCups.keySet().toArray(new String[0])[i],
                        sortedLeagueCups.values().toArray(new Integer[0])[i]));
            }
        }
    }

    static void continentalStats() {
        final Map<String, Integer> titles = new LinkedHashMap<>();

        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
                if (club.getGlory().getContinental() > 0) titles.put(club.getName(), club.getGlory().getContinental());
            }
        }

        final Map<String, Integer> sortedTitles = sortMap(titles);

        System.out.println();
        System.out.println("Champions League Winners");
        for (int i = 0; i < sortedTitles.size(); i++) {
            System.out.println(String.format("%2d. %-25s %d", i + 1, sortedTitles.keySet().toArray(new String[0])[i],
                    sortedTitles.values().toArray(new Integer[0])[i]));
        }
    }

    public static void allTimePlayerStats(final Club[] league) {
        ratings.clear();
        motm.clear();
        goals.clear();
        assists.clear();
        cleanSheets.clear();
        yellowCards.clear();
        redCards.clear();

        for (final Club club : league) {
            for (final Footballer f : club.getFootballers()) {
                final Competition stats = f.getResume().getTotal().getLeague();

                if (f.getResume().getTotal().getLeague().getMatches() > 100) {
                    ratings.put(f, f.getResume().getTotal().getLeague().getRating());
                }

                motm.put(f, stats.getMotmAwards());
                goals.put(f, stats.getGoals());
                assists.put(f, stats.getAssists());
                yellowCards.put(f, stats.getYellowCards());
                redCards.put(f, stats.getRedCards());

                if (f.getPosition() == GK) {
                    cleanSheets.put(f, stats.getCleanSheets());
                }
            }
        }

        topPlayers(ratings, "Top Players");
        topPlayers(motm, "Most MOTM Awards");
        topPlayers(goals, "Top Goalscorer");
        topPlayers(assists, "Most Assists");
        topPlayers(cleanSheets, "Most Clean Sheets");
        topPlayers(yellowCards, "Most Yellow Cards");
        topPlayers(redCards, "Most Red Cards");
    }

    private static void topPlayers(Map<Footballer, Integer> map, final String label) {
        System.out.println(label);
        final Map<Footballer, Integer> sorted = sortMap(map);

        for (int player = 0; player < 20 && player < sorted.size(); player++) {
            final int value = sorted.values().toArray(new Integer[0])[player];
            if (value == 0) break;
            System.out.println(String.format("%2d. %-20s %2d", player + 1,
                    sorted.keySet().toArray(new Footballer[0])[player].getName(),  value));
        }

        System.out.println();
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
                    if (goalkeepers > 0) {
                        team[--goalkeepers] = player;
                    }

                    break;

                case Defender:
                    if (defenders > 0) {
                        team[defenders--] = player;
                    }

                    break;

                case Midfielder:
                    if (midfielders > 0) {
                        team[4 + midfielders--] = player;
                    }

                    break;

                case Forward:
                    if (forwards > 0) {
                        team[7 + forwards--] = player;
                    }

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
}
