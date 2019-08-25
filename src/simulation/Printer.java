package simulation;

import competitions.England;
import competitions.France;
import players.Competition;
import players.Footballer;
import teams.Club;
import teams.League;

import java.util.*;

import static java.util.stream.Collectors.toMap;
import static players.Position.GK;
import static simulation.Data.*;

public class Printer {
    private static final Random random = new Random();
    private static int offset;
    public static HashMap<Footballer, Integer> topTeam = new HashMap<>();

    public static Map<Club, Integer> sortLeague(final Club[] league) {
        final Map<Club, Integer> standings = new LinkedHashMap<>();
        offset = 0;
        for (final Club team : league) {
            if (offset < team.getName().length()) offset = team.getName().length();
            final League stats = team.getSeason().getLeague();
            standings.put(team, 10000 * stats.getPoints() + 100 * (stats.getScored() - stats.getConceded()) +
                    stats.getScored());
        }

        return standings.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));
    }

    public static Club[] pickChampionsLeagueTeams() {
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

        return selected;
    }

    public static void standings(final Club[] league) {
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
        System.out.println();
        System.out.println("Total goals for the season: " + goals + " // 1072");
        System.out.println(HOME_WINS + " - " + (380 - HOME_WINS - AWAY_WINS) +
                " - " + AWAY_WINS + " // 181 - 71 - 128");
        System.out.println();
        System.out.println("Average rating for the season: " + RATINGS / (22 * 380));
        System.out.println();

        final Club first = sorted.keySet().toArray(new Club[0])[0];

        if (first.getSeason().getLeague().getMatches() == 2 * league.length - 2) {
            first.getGlory().addLeague();
            for (final Footballer footballer : first.getFootballers()) {
                footballer.getResume().getGlory().addLeague();
            }

            leaguePrizes(sorted.keySet().toArray(new Club[0]));
        }
    }

    private static void leaguePrizes(final Club[] league) {
        for (int team = 0; team < league.length; team++) {
            if (team == 0) league[team].changeBudget(50f);
            else if (team < 4) league[team].changeBudget(40f - (team - 1) * 5f);
            else if (team < 7) league[team].changeBudget(26f - (team - 4) * 3f);
            else league[team].changeBudget(25f - team);
        }
    }

    public static void knockoutPrizes(final Club[] clubs, final boolean isContinental) {
        final float base = isContinental ? 10f : 1f;

        for (int team = 0; team < clubs.length; team++) {
            if (team == 0) clubs[team].changeBudget(10 * base);
            else if (team == 1) clubs[team].changeBudget(7 * base);
            else if (team < 4) clubs[team].changeBudget(4 * base);
            else if (team < 8) clubs[team].changeBudget(2 * base);
            else clubs[team].changeBudget(base);
        }
    }

    public static void playerStats(final Club[] league, final int type) {
        final Map<String, Integer> ratings = new LinkedHashMap<>();
        final Map<String, Integer> motm = new LinkedHashMap<>();
        final Map<String, Integer> goals = new LinkedHashMap<>();
        final Map<String, Integer> assists = new LinkedHashMap<>();
        final Map<String, Integer> cleanSheets = new LinkedHashMap<>();
        final Map<String, Integer> yellowCards = new LinkedHashMap<>();
        final Map<String, Integer> redCards = new LinkedHashMap<>();
        final Map<Footballer, Integer> topTeam = new LinkedHashMap<>();

        for (final Club club : league) {
            for (final Footballer f : club.getFootballers()) {
                final String name = f.getName();
                final Competition stats;
                final int games;

                switch (type) {
                    case 0:
                        stats = f.getResume().getSeason().getLeague();
                        games = 20;
                        break;
                    case 1:
                        stats = f.getResume().getSeason().getCup();
                        games = 2;
                        break;
                    default:
                        stats = f.getResume().getSeason().getContinental();
                        games = 6;
                        break;
                }

                if (stats.getMatches() > games) {
                    ratings.put(name, stats.getRating());
                    topTeam.put(f, stats.getRating());
                }
                motm.put(name, stats.getMotmAwards());
                goals.put(name, stats.getGoals());
                assists.put(name, stats.getAssists());
                yellowCards.put(name, stats.getYellowCards());
                redCards.put(name, stats.getRedCards());

                if (f.getPosition() == GK) {
                    cleanSheets.put(name, stats.getCleanSheets());
                }
            }
        }

        System.out.println();
        topPlayers(ratings, "Top Players");
        topPlayers(motm, "Most MOTM Awards");
        topPlayers(goals, "Top Goalscorer");
        topPlayers(assists, "Most Assists");
        topPlayers(cleanSheets, "Most Clean Sheets");
        topPlayers(yellowCards, "Most Yellow Cards");
        topPlayers(redCards, "Most Red Cards");

        pickTeam(topTeam, type == 0);
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

        final Map<String, Integer> sortedLeagues = leagues.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        final Map<String, Integer> sortedNationalCups = nationalCups.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        final Map<String, Integer> sortedLeagueCups = leagueCups.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

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

        final Map<String, Integer> sortedTitles = titles.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        System.out.println();
        System.out.println("Champions League Winners");
        for (int i = 0; i < sortedTitles.size(); i++) {
            System.out.println(String.format("%2d. %-25s %d", i + 1, sortedTitles.keySet().toArray(new String[0])[i],
                    sortedTitles.values().toArray(new Integer[0])[i]));
        }
    }

    public static void allTimePlayerStats(final Club[] league) {
        final Map<String, Integer> ratings = new LinkedHashMap<>();
        final Map<String, Integer> motm = new LinkedHashMap<>();
        final Map<String, Integer> goals = new LinkedHashMap<>();
        final Map<String, Integer> assists = new LinkedHashMap<>();
        final Map<String, Integer> cleanSheets = new LinkedHashMap<>();
        final Map<String, Integer> yellowCards = new LinkedHashMap<>();
        final Map<String, Integer> redCards = new LinkedHashMap<>();

        for (final Club club : league) {
            for (final Footballer f : club.getFootballers()) {
                final String name = f.getName();
                final Competition stats = f.getResume().getTotal().getLeague();

                if (f.getResume().getTotal().getLeague().getMatches() > 100) {
                    ratings.put(name, f.getResume().getTotal().getLeague().getRating());
                }

                motm.put(name, stats.getMotmAwards());
                goals.put(name, stats.getGoals());
                assists.put(name, stats.getAssists());
                yellowCards.put(name, stats.getYellowCards());
                redCards.put(name, stats.getRedCards());

                if (f.getPosition() == GK) {
                    cleanSheets.put(name, stats.getCleanSheets());
                }

//                if (team < 6) System.out.println(String.format("%s %s", name, f.toString()));
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

    private static void topPlayers(final Map<String, Integer> map, final String label) {
        System.out.println(label);
        final Map<String, Integer> sorted = map.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        for (int player = 0; player < 20 && player < sorted.size(); player++) {
            final int value = sorted.values().toArray(new Integer[0])[player];
            if (value == 0) break;
            System.out.println(String.format("%2d. %-20s %2d", player + 1, sorted.keySet().toArray(new String[0])[player],
                    value));
        }

        System.out.println();
    }

    public static void pickTeam(final Map<Footballer, Integer> ratings, final boolean isLeague) {
        int[] team = new int[11];
        int goalkeepers = 1;
        int defenders = 4;
        int midfielders = 3;
        int forwards = 3;

        final Map<Footballer, Integer> sorted = ratings.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

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

    public static void voting(final Map<Footballer, Integer> contenders) {
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

        final Map<Footballer, Integer> sorted = results.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        System.out.println("FOOTBALLER OF THE YEAR");
        for (int player = 0; player < 10; player++) {
            Footballer footballer = sorted.keySet().toArray(new Footballer[0])[player];
            int count = sorted.values().toArray(new Integer[0])[player];

            System.out.println(String.format("%2d. %-20s %2d", player + 1, footballer.getName(), count));
        }

        System.out.println();
    }
}
