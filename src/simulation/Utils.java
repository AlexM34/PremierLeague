package simulation;

import teams.Club;
import teams.League;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static simulation.Printer.offset;

public class Utils {
    static <T> Map<T, Integer> sortMap(final Map<T, Integer> clubs) {
        return clubs.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static Map<Club, Integer> sortLeague(final Club[] league, final int type) {
        final Map<Club, Integer> standings = new LinkedHashMap<>();
        offset = 0;
        for (final Club team : league) {
            if (offset < team.getName().length()) offset = team.getName().length();
            final League stats;
            switch (type) {
                case 0: stats = team.getSeason().getLeague(); break;
                case 3: stats = team.getSeason().getChampionsLeague().getGroup(); break;
                default: return Collections.emptyMap();
            }
            standings.put(team, 10000 * stats.getPoints() + 100 * (stats.getScored() - stats.getConceded()) +
                    stats.getScored());
        }

        return sortMap(standings);
    }
}
