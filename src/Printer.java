import java.util.*;

import static java.util.stream.Collectors.toMap;

class Printer {
    private static int offset;

    private static Map<Club, Integer> sortLeague(Club[] league) {
        Map<Club, Integer> standings = new LinkedHashMap<>();
        offset = 0;
        for (Club team : league) {
            if (offset < team.getName().length()) offset = team.getName().length();
            League stats = team.getSeason().getLeague();
            standings.put(team, 10000 * stats.getPoints() + 100 * (stats.getScored() - stats.getConceded()) +
                    stats.getScored());
        }

        return standings.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));
    }

    static Club[] pickChampionsLeagueTeams() {
        Map<Club, Integer> teams = new LinkedHashMap<>();
        for (Club[] league : Data.LEAGUES) {
            Map<Club, Integer> sorted = sortLeague(league);
            int slots = 7;
            for (Club team : sorted.keySet()) {
                teams.put(team, team.getSeason().getLeague().getPoints());
                slots--;
                if (slots == 0) break;
            }
        }

        List<Map.Entry<Club, Integer>> toSort = new ArrayList<>(teams.entrySet());
        toSort.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        Club[] selected = new Club[32];
        int limit = 0;

        for (Map.Entry<Club, Integer> clubIntegerEntry : toSort) {
            selected[limit] = clubIntegerEntry.getKey();
            if (limit++ == 31) break;
        }

        return selected;
    }

    static void standings(Club[] league) {
        int goals = 0;
        Map<Club, Integer> sorted = sortLeague(league);

        int position = 1;
        System.out.println(String.format("No  Teams %" + (offset - 3) + "s G  W  D  L   GF:GA  P", ""));
        for (Club team : league) {
            League stats = team.getSeason().getLeague();
            goals += stats.getScored();
            System.out.println(String.format("%2d. %-" + (offset + 3) + "s %-2d %-2d %-2d %-2d %3d:%-3d %-3d", position++,
                    team.getName(), stats.getMatches(), stats.getWins(), stats.getDraws(),  stats.getLosses(),
                    stats.getScored(), stats.getConceded(), stats.getPoints()));
        }
        System.out.println();
        System.out.println("Total goals for the season: " + goals + " // 1072");
        System.out.println(Data.HOME_WINS + " - " + (380 - Data.HOME_WINS - Data.AWAY_WINS) +
                " - " + Data.AWAY_WINS + " // 181 - 71 - 128");
        System.out.println();
        System.out.println("Average rating for the season: " + Data.RATINGS / (22 * 380));
        System.out.println();

        Club first = sorted.keySet().toArray(new Club[0])[0];

        if (first.getSeason().getLeague().getMatches() == 2 * league.length - 2) {
            first.getGlory().addLeague();
            for (Footballer footballer : first.getFootballers()) {
                footballer.getResume().getGlory().addLeague();
            }
        }
    }

    static void playerStats(Club[] league, int type) {
        Map<String, Integer> ratings = new LinkedHashMap<>();
        Map<String, Integer> motm = new LinkedHashMap<>();
        Map<String, Integer> goals = new LinkedHashMap<>();
        Map<String, Integer> assists = new LinkedHashMap<>();
        Map<String, Integer> cleanSheets = new LinkedHashMap<>();
        Map<String, Integer> yellowCards = new LinkedHashMap<>();
        Map<String, Integer> redCards = new LinkedHashMap<>();

        for (Club club : league) {
            for (Footballer f : club.getFootballers()) {
                String name = f.getName();
                Competition stats;
                int games;

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

                if (stats.getMatches() > games) ratings.put(name, stats.getRating());
                motm.put(name, stats.getMotmAwards());
                goals.put(name, stats.getGoals());
                assists.put(name, stats.getAssists());
                yellowCards.put(name, stats.getYellowCards());
                redCards.put(name, stats.getRedCards());

                if (f.getPosition() == Position.GK) {
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
    }

    static void allTimeStats(Club[] league) {
        Map<String, Integer> leagues = new LinkedHashMap<>();
        Map<String, Integer> nationalCups = new LinkedHashMap<>();
        Map<String, Integer> leagueCups = new LinkedHashMap<>();

        for (Club club : league) {
            leagues.put(club.getName(), club.getGlory().getLeague());
            nationalCups.put(club.getName(), club.getGlory().getNationalCup());
            leagueCups.put(club.getName(), club.getGlory().getLeagueCup());
        }

        Map<String, Integer> sortedLeagues = leagues.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        Map<String, Integer> sortedNationalCups = nationalCups.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        Map<String, Integer> sortedLeagueCups = leagueCups.entrySet().stream().sorted(
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
        Map<String, Integer> titles = new LinkedHashMap<>();

        for (Club[] league : Data.LEAGUES) {
            for (Club club : league) {
                if (club.getGlory().getContinental() > 0) titles.put(club.getName(), club.getGlory().getContinental());
            }
        }

        Map<String, Integer> sortedTitles = titles.entrySet().stream().sorted(
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

    static void allTimePlayerStats(Club[] league) {
        Map<String, Integer> ratings = new LinkedHashMap<>();
        Map<String, Integer> motm = new LinkedHashMap<>();
        Map<String, Integer> goals = new LinkedHashMap<>();
        Map<String, Integer> assists = new LinkedHashMap<>();
        Map<String, Integer> cleanSheets = new LinkedHashMap<>();
        Map<String, Integer> yellowCards = new LinkedHashMap<>();
        Map<String, Integer> redCards = new LinkedHashMap<>();

        for (int team = 0; team < league.length; team++) {
            for (Footballer f : league[team].getFootballers()) {
                String name = f.getName();
                Competition stats = f.getResume().getTotal().getLeague();

                if (f.getResume().getTotal().getLeague().getMatches() > 100) {
                    ratings.put(name, f.getResume().getTotal().getLeague().getRating());
                }

                motm.put(name, stats.getMotmAwards());
                goals.put(name, stats.getGoals());
                assists.put(name, stats.getAssists());
                yellowCards.put(name, stats.getYellowCards());
                redCards.put(name, stats.getRedCards());

                if (f.getPosition() == Position.GK) {
                    cleanSheets.put(name, stats.getCleanSheets());
                }

                if (team < 6) System.out.println(String.format("%s %s", name, f.toString()));
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

    private static void topPlayers(Map<String, Integer> map, String label) {
        System.out.println(label);
        Map<String, Integer> sorted = map.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        for (int i = 0; i < 20 && i < sorted.size(); i++) {
            System.out.println(String.format("%2d. %-20s %2d", i + 1, sorted.keySet().toArray(new String[0])[i],
                    sorted.values().toArray(new Integer[0])[i]));
        }

        System.out.println();
    }
}
