package simulation;

import competition.Cup;
import competition.Fixture;
import team.Club;
import team.Season;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.shuffle;
import static simulation.Controller.CHAMPIONS_LEAGUE_NAME;
import static simulation.Controller.EUROPA_LEAGUE_NAME;
import static simulation.Controller.continentalCupResults;
import static simulation.Controller.leagueCupResults;
import static simulation.Controller.nationalCupResults;
import static simulation.Data.FANS;
import static simulation.Helper.appendScore;
import static simulation.Match.simulation;

public class Knockout {

    static Club[] knockoutRound(final Club[] clubs, final int games, final int type, final boolean replay) {
        final int count = clubs.length;
        final String competition;
        final Map<String, String> results;
        switch (type) {
            case 1: competition = clubs[0].getLeague(); results = nationalCupResults; break;
            case 2: competition = clubs[0].getLeague(); results = leagueCupResults; break;
            case 3: competition = CHAMPIONS_LEAGUE_NAME; results = continentalCupResults; break;
            case 4: competition = EUROPA_LEAGUE_NAME; results = continentalCupResults; break;
            default: competition = ""; results = new HashMap<>(); break;
        }

        results.put("new", "");
        results.put("reports: new", "");
        final List<Club> draw = Arrays.asList(clubs);
        shuffle(draw);

        for (int team = 0; team < count / 2; team++) {
            if (knockoutFixture(draw.get(team), draw.get(count - team - 1), count > 2 ? games : 1,
                    type, replay && count >= 8, count == 2, results, count)) {
                clubs[team] = draw.get(count - team - 1);
            } else {
                clubs[team] = draw.get(team);
            }
        }

        results.put(competition + count, results.remove("new"));
        results.put("reports: " + competition + count, results.remove("reports: new"));
        return Arrays.copyOf(clubs, count / 2);
    }

    private static boolean knockoutFixture(final Club first, final Club second, final int games, final int type,
                                           final boolean replay, final boolean neutral,
                                           final Map<String, String> results, final int teams) {
        int firstGoals = 0;
        int secondGoals = 0;
        if (neutral) FANS = 0;

        int[] result = simulation(first, second, games == 1 && !replay, -1, -1, type);
        firstGoals += result[0];
        secondGoals += result[1];

        final StringBuilder scores = new StringBuilder(String.valueOf(results.get("new")));
        final StringBuilder reports = new StringBuilder(String.valueOf(results.get("reports: new")));
        if (games == 2 && scores.length() > 0) scores.append("<br/>");
        appendScore(scores, reports, first, second, result);

        if (games == 2 || (replay && firstGoals == secondGoals)) {
            result = simulation(second, first, true, replay ? -1 : secondGoals, replay ? -1 : firstGoals, type);
            appendScore(scores, reports, second, first, result);

            secondGoals += result[0];
            firstGoals += result[1];

            if (firstGoals == secondGoals) {
                if (result[0] + result[1] > firstGoals) firstGoals++;
                else secondGoals++;
            }
        }

        updateFixtures(first, second, firstGoals, secondGoals, type, teams);
        results.put("new", scores.toString());
        results.put("reports: new", reports.toString());
        return secondGoals > firstGoals;
    }

    private static void updateFixtures(final Club first, final Club second, final int firstGoals,
                                       final int secondGoals, final int type, final int teams) {
        getCup(first.getSeason(), type).getFixtures().add(new Fixture(second, new int[]{firstGoals, secondGoals}));
        getCup(second.getSeason(), type).getFixtures().add(new Fixture(first, new int[]{secondGoals, firstGoals}));

        getCup(first.getSeason(), type).setStage(String.valueOf(firstGoals > secondGoals ? teams / 2 : teams));
        getCup(second.getSeason(), type).setStage(String.valueOf(secondGoals > firstGoals ? teams / 2 : teams));
    }

    private static Cup getCup(final Season season, final int type) {
        switch (type) {
            case 1: return season.getNationalCup();
            case 2: return season.getLeagueCup();
            case 3: case 4: return season.getContinental().getKnockout();
            default: throw new NullPointerException("No cup exists!");
        }
    }
}
