package simulation;

import players.Competition;
import players.Statistics;
import team.Club;
import team.League;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static simulation.Printer.offset;

public class Helper {

    static void groupGameOutcome(final League team, final Club opponent, final int scored, final int conceded) {
        team.addFixture(opponent, scored, conceded);

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

    static Competition getCompetition(final Statistics season, final int type) {
        switch (type) {
            case 0: return season.getLeague();
            case 1: return season.getNationalCup();
            case 2: return season.getLeagueCup();
            case 3: return season.getChampionsLeague();
            default: return season.getEuropaLeague();
        }
    }
}
