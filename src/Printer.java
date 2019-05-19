import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

class Printer {

    static void printStandings() {
        int goals = 0;
        Map<Integer, Integer> standings = new LinkedHashMap<>();
        for (int teams = 0; teams < 20; teams++) {
            standings.put(teams, 10000 * Data.POINTS[teams] + 100 *
                    (Data.GOALS_FOR[teams] - Data.GOALS_AGAINST[teams]) + Data.GOALS_FOR[teams]);
        }

        Map<Integer, Integer> sorted = standings.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        System.out.println("No  Teams                     G  W  D  L   GF:GA  P");
        for (int i = 0; i < 20; i++) {
            Integer index = (Integer) sorted.keySet().toArray()[i];
            goals += Data.GOALS_FOR[index];
            System.out.println(String.format("%2d. %-25s %-2d %-2d %-2d %-2d %3d:%-3d %-3d", i + 1,
                    Data.TEAMS[index], Data.GAMES[index], Data.WINS[index], Data.DRAWS[index], Data.LOSES[index],
                    Data.GOALS_FOR[index], Data.GOALS_AGAINST[index], Data.POINTS[index]));
        }
        System.out.println();
        System.out.println("Total goals for the season: " + goals + " // 1072");
        System.out.println(Data.HOME_WINS + " - " + (380 - Data.HOME_WINS - Data.AWAY_WINS) +
                " - " + Data.AWAY_WINS + " // 181 - 71 - 128");
        System.out.println();

        int first = (Integer) sorted.keySet().toArray()[0];

        if (Data.GAMES[first] == 38) {
            Data.TITLES[first]++;
        }
    }

    static void printPlayerStats() {
        Map<String, Float> ratings = new LinkedHashMap<>();
        Map<String, Integer> motm = new LinkedHashMap<>();
        Map<String, Integer> goals = new LinkedHashMap<>();
        Map<String, Integer> assists = new LinkedHashMap<>();
        Map<String, Integer> cleanSheets = new LinkedHashMap<>();

        for (int team = 0; team < 20; team++) {
            for (Footballer f : Data.SQUADS.get(Data.TEAMS[team])) {
                Integer id = f.getId();
                String name = f.getName();
                Map<String, Integer> stat = Data.STATS.get(id);

                ratings.put(name, stat.get("Games") > 0 ? (float) stat.get("Ratings") / stat.get("Games") / 100 : 0);
                motm.put(name, stat.get("MOTM"));
                goals.put(name, stat.get("Goals"));
                assists.put(name, stat.get("Assists"));

                if (stat.get("Clean Sheets") > 0) {
                    cleanSheets.put(name, stat.get("Clean Sheets"));
                }
//                System.out.println(String.format("%s %.2f %d %d", Data.PLAYERS[team][player], Data.RATINGS[team][player] / 38,
//                        Data.GOALS[team][player], Data.ASSISTS[team][player]));
            }
        }

        Map<String, Float> sortedRatings = ratings.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        Map<String, Integer> sortedMotm = motm.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        Map<String, Integer> sortedGoals = goals.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        Map<String, Integer> sortedAssists = assists.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        Map<String, Integer> sortedCleanSheets = cleanSheets.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        // TODO: Sorted values don't match with integer
        System.out.println();
        System.out.println("Top Players");
        for (int i = 0; i < 20 && i < sortedRatings.size(); i++) {
            System.out.println(String.format("%2d. %-15s %.2f", i + 1, sortedRatings.keySet().toArray()[i],
                    sortedRatings.values().toArray()[i]));
        }

        System.out.println();
        System.out.println("Most MOTM Awards");
        for (int i = 0; i < 20 && i < sortedMotm.size(); i++) {
            System.out.println(String.format("%2d. %-15s %d", i + 1, sortedMotm.keySet().toArray()[i],
                    sortedMotm.values().toArray()[i]));
        }

        System.out.println();
        System.out.println("Top Goalscorer");
        for (int i = 0; i < 20 && i < sortedGoals.size(); i++) {
            System.out.println(String.format("%2d. %-15s %d", i + 1, sortedGoals.keySet().toArray()[i],
                    sortedGoals.values().toArray()[i]));
        }

        System.out.println();
        System.out.println("Most Assists");
        for (int i = 0; i < 20 && i < sortedAssists.size(); i++) {
            System.out.println(String.format("%2d. %-15s %d", i + 1, sortedAssists.keySet().toArray()[i],
                    sortedAssists.values().toArray()[i]));
        }

        System.out.println();
        System.out.println("Most Clean Sheets");
        for (int i = 0; i < 20 && i < sortedCleanSheets.size(); i++) {
            System.out.println(String.format("%2d. %-15s %2d", i + 1, sortedCleanSheets.keySet().toArray()[i],
                    sortedCleanSheets.values().toArray()[i]));
        }

        System.out.println();
    }

    static void printAllTimeStats() {
        Map<String, Integer> titles = new LinkedHashMap<>();
        for (int i = 0; i < 20; i++) {
            titles.put(Data.TEAMS[i], Data.TITLES[i]);
        }

        Map<String, Integer> sorted = titles.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        System.out.println();
        System.out.println("Winners");
        for (int i = 0; i < 20; i++) {
            System.out.println(String.format("%2d. %-25s %d", i+1, sorted.keySet().toArray()[i],
                    sorted.values().toArray()[i]));
        }
    }
}
