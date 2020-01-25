package simulation;

import team.Club;
import team.League;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static simulation.Match.report;
import static simulation.Printer.offset;

public class Helper {

    static void groupGameOutcome(final League team, final int scored, final int conceded) {
        if (scored > conceded) {
            team.addPoints(3);
            team.addWin();
        } else if (scored == conceded) {
            team.addPoints(1);
            team.addDraw();
        } else team.addLoss();

        team.addMatch();
        team.addScored(scored);
        team.addConceded(conceded);
        if (conceded == 0) team.addCleanSheet();
    }

    static Club[] cup(final Club[] league) {
        final Club[] selected = new Club[16];
        System.arraycopy(league, 0, selected, 0, 16);
        return selected;
    }

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
                case 3: stats = team.getSeason().getContinental().getGroup(); break;
                default: return Collections.emptyMap();
            }

            standings.put(team, getPerformance(stats));
        }

        return sortMap(standings);
    }

    static int getPerformance(final League stats) {
        return 10000 * stats.getPoints() + 100 * (stats.getScored() - stats.getConceded()) + stats.getScored();
    }

    static void appendScore(final StringBuilder scores, final StringBuilder reports, final Club home,
                            final Club away, final int[] result) {
        final String score = home.getName() + " - " + away.getName() + " " + result[0] + ":" + result[1] + "<br/>";
        scores.append(score);

        final String teams = home.getName() + " vs " + away.getName() + "<br/>";
        final String finalScore = "Final score: " + result[0] + ":" + result[1] + "<br/>" + "<br/>";
        reports.append(teams).append(report).append(finalScore);
    }
}
