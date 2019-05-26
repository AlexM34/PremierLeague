import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

class Printer {

    static void printStandings(Club[] league) {
        int goals = 0;
        Map<Integer, Integer> standings = new LinkedHashMap<>();
        for (int team = 0; team < league.length; team++) {
            standings.put(team, 10000 * league[team].getSeason().getLeague().getPoints() + 100 *
                    (league[team].getSeason().getLeague().getScored() - league[team].getSeason().getLeague().getConceded()) +
                    league[team].getSeason().getLeague().getScored());
        }

        Map<Integer, Integer> sorted = standings.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        System.out.println("No  Teams                     G  W  D  L   GF:GA  P");
        for (int i = 0; i < league.length; i++) {
            Integer index = (Integer) sorted.keySet().toArray()[i];
            goals += league[index].getSeason().getLeague().getScored();
            System.out.println(String.format("%2d. %-25s %-2d %-2d %-2d %-2d %3d:%-3d %-3d", i + 1,
                    league[index].getName(), league[index].getSeason().getLeague().getMatches(), league[index].getSeason().getLeague().getWins(),
                    league[index].getSeason().getLeague().getDraws(), league[index].getSeason().getLeague().getLosses(),
                    league[index].getSeason().getLeague().getScored(), league[index].getSeason().getLeague().getConceded(),
                    league[index].getSeason().getLeague().getPoints()));
        }
        System.out.println();
        System.out.println("Total goals for the season: " + goals + " // 1072");
        System.out.println(Data.HOME_WINS + " - " + (380 - Data.HOME_WINS - Data.AWAY_WINS) +
                " - " + Data.AWAY_WINS + " // 181 - 71 - 128");
        System.out.println();
        System.out.println("Average rating for the season: " + Data.RATINGS / (22 * 380));
        System.out.println();

        int first = (Integer) sorted.keySet().toArray()[0];

        if (league[first].getSeason().getLeague().getMatches() == 38) {
            league[first].getGlory().addLeague(1);
        }
    }

    static void printPlayerStats(Club[] league) {
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

                if (f.getResume().getSeason().getMatches() > 20) ratings.put(name, f.getResume().getSeason().getRating());
                motm.put(name, f.getResume().getSeason().getMotmAwards());
                goals.put(name, f.getResume().getSeason().getGoals());
                assists.put(name, f.getResume().getSeason().getAssists());
                yellowCards.put(name, f.getResume().getSeason().getYellowCards());
                redCards.put(name, f.getResume().getSeason().getRedCards());

                if (f.getPosition() == Position.GK) {
                    cleanSheets.put(name, f.getResume().getSeason().getCleanSheets());
                }

                if (team < 6 && f.getResume().getSeason().getMatches() > 0) {
//                    System.out.println(String.format("%s %s", name, f.getResume().getSeason().toString()));
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

    static void printAllTimeStats(Club[] league) {
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
            System.out.println(String.format("%2d. %-25s %d", i + 1, sortedLeagues.keySet().toArray()[i],
                    sortedLeagues.values().toArray()[i]));
        }

        System.out.println();
        System.out.println("National Cup Winners");
        for (int i = 0; i < sortedLeagueCups.size(); i++) {
            System.out.println(String.format("%2d. %-25s %d", i + 1, sortedNationalCups.keySet().toArray()[i],
                    sortedNationalCups.values().toArray()[i]));
        }

        if (league[0].getLeague().equals(England.LEAGUE) || league[0].getLeague().equals(France.LEAGUE)) {
            System.out.println();
            System.out.println("League Cup Winners");
            for (int i = 0; i < sortedLeagueCups.size(); i++) {
                System.out.println(String.format("%2d. %-25s %d", i + 1, sortedLeagueCups.keySet().toArray()[i],
                        sortedLeagueCups.values().toArray()[i]));
            }
        }
    }

    static void printAllTimePlayerStats(Club[] league) {
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

                if (f.getResume().getTotal().getMatches() > 100) {
                    ratings.put(name, f.getResume().getTotal().getRating());
                }

                motm.put(name, f.getResume().getTotal().getMotmAwards());
                goals.put(name, f.getResume().getTotal().getGoals());
                assists.put(name, f.getResume().getTotal().getAssists());
                yellowCards.put(name, f.getResume().getTotal().getYellowCards());
                redCards.put(name, f.getResume().getTotal().getRedCards());

                if (f.getPosition() == Position.GK) {
                    cleanSheets.put(name, f.getResume().getTotal().getCleanSheets());
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

        // TODO: Sorted values don't match with integer
        for (int i = 0; i < 20 && i < sorted.size(); i++) {
            System.out.println(String.format("%2d. %-15s %2d", i + 1, sorted.keySet().toArray()[i],
                    sorted.values().toArray()[i]));
        }

        System.out.println();
    }
}
