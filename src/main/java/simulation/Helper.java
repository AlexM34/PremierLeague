package simulation;

import team.Club;
import team.League;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static simulation.dynamics.Postseason.offset;

public class Helper {

    public static List<Club> sortLeague(final Club[] league, final int type) {
        final Map<Club, Integer> standings = new LinkedHashMap<>();
        offset = 0;
        for (final Club team : league) {
            if (offset < team.getName().length()) offset = team.getName().length();
            final League stats;
            switch (type) {
                case 0: stats = team.getSeason().getLeague(); break;
                case 3: stats = team.getSeason().getContinental().getGroup(); break;
                default: return Collections.emptyList();
            }

            standings.put(team, getPerformance(stats));
        }

        return new ArrayList<>(sortMap(standings).keySet());
    }

    public static <T> Map<T, Integer> sortMap(final Map<T, Integer> clubs) {
        return clubs.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static int getPerformance(final League stats) {
        return 10000 * stats.getPoints() + 100 * (stats.getScored() - stats.getConceded()) + stats.getScored();
    }
}
