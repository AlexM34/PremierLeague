package simulation.competition;

import league.LeagueManager;
import simulation.match.Fixture;
import simulation.match.Match;
import simulation.match.Report;
import team.Club;
import team.Cup;
import team.Season;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.shuffle;
import static simulation.Data.LEAGUES;
import static simulation.competition.Competition.NATIONAL_CUP;
import static simulation.competition.League.CHAMPIONS_LEAGUE_NAME;
import static simulation.competition.League.EUROPA_LEAGUE_NAME;
import static simulation.competition.League.continentalCup;
import static simulation.competition.League.continentalCupResults;
import static simulation.match.Match.simulate;

public class Knockout {
    public static final Map<String, Club[]> leagueCup = new HashMap<>();
    public static final Map<String, Club[]> nationalCup = new HashMap<>();
    public static final Map<String, String> leagueCupResults = new HashMap<>();
    public static final Map<String, String> nationalCupResults = new HashMap<>();

    public static void setupCups() {
        leagueCup.clear();
        nationalCup.clear();
        leagueCupResults.clear();
        nationalCupResults.clear();

        for (final Club[] league : LEAGUES) nationalCup.put(league[0].getLeague(), startCup(league));

        for (final Club[] league : LeagueManager.getLeagueCupClubs()) {
            leagueCup.put(league[0].getLeague(), startCup(league));
        }
    }

    public static void nationalCupRound() {
        for (final Club[] league : LEAGUES) {
            final String leagueName = league[0].getLeague();
            knockoutRound(nationalCup, leagueName, LeagueManager.getNationalCupGames(leagueName),
                    NATIONAL_CUP, LeagueManager.isReplayed(leagueName));
        }
    }

    public static void leagueCupRound() {
        for (final Club[] league : LeagueManager.getLeagueCupClubs()) {
            knockoutRound(leagueCup, league[0].getLeague(), 1, Competition.LEAGUE_CUP, false);
        }
    }

    public static void championsLeague() {
        knockoutRound(continentalCup, CHAMPIONS_LEAGUE_NAME, 2, Competition.CHAMPIONS_LEAGUE, false);
    }

    public static void europaLeague() {
        knockoutRound(continentalCup, EUROPA_LEAGUE_NAME, 2, Competition.EUROPA_LEAGUE, false);
    }

    static void knockoutRound(final Map<String, Club[]> cup, final String competition,
                              final int games, final Competition type, final boolean replay) {

        final Club[] clubs = cup.get(competition);
        final int count = clubs.length;
        final Map<String, String> results;
        switch (type) {
            case NATIONAL_CUP: results = nationalCupResults; break;
            case LEAGUE_CUP: results = leagueCupResults; break;
            case CHAMPIONS_LEAGUE:
            case EUROPA_LEAGUE: results = continentalCupResults; break;
            default: results = new HashMap<>(); break;
        }

        results.put("new", "");
        results.put("reports: new", "");
        final List<Club> draw = Arrays.asList(clubs);
        shuffle(draw);

        if (count == 2) Match.fans = 0;
        for (int team = 0; team < count / 2; team++) {
            final Report report = new Report(draw.get(team), draw.get(count - team - 1), type,
                    -1, -1,
                    (games == 1 && (!replay || count < 8)) || count == 2);
            clubs[team] = knockoutFixture(report, games, replay, results, count);
        }

        results.put(competition + count, results.remove("new"));
        results.put("reports: " + competition + count, results.remove("reports: new"));
        cup.put(competition, Arrays.copyOf(clubs, count / 2));
    }

    private static Club knockoutFixture(final Report report, final int games, final boolean replay,
                                           final Map<String, String> results, final int teams) {

        final Club first = report.getHome();
        final Club second = report.getAway();
        int firstGoals = 0;
        int secondGoals = 0;

        simulate(report);
        firstGoals += report.getHomeGoals();
        secondGoals += report.getAwayGoals();

        final StringBuilder scores = new StringBuilder(String.valueOf(results.get("new")));
        final StringBuilder reports = new StringBuilder(String.valueOf(results.get("reports: new")));
        if (games == 2 && scores.length() > 0) scores.append("<br/>");
        report.appendScore(scores, reports);

        if ((games == 2 && teams > 2) || (replay && firstGoals == secondGoals)) {
            final Report report2 = new Report(second, first, report.getCompetition(),
                    replay ? -1 : secondGoals, replay ? -1 : firstGoals, true);
            simulate(report2);
            report2.appendScore(scores, reports);

            secondGoals += report2.getHomeGoals();
            firstGoals += report2.getAwayGoals();

            if (firstGoals == secondGoals) {
                if (report.getAwayGoals() < report2.getAwayGoals()) firstGoals++;
                else secondGoals++;
            }
        }

        updateFixtures(first, second, firstGoals, secondGoals, report.getCompetition().getType(), teams);
        results.put("new", scores.toString());
        results.put("reports: new", reports.toString());
        return firstGoals > secondGoals ? first : second;
    }

    private static void updateFixtures(final Club first, final Club second, final int firstGoals,
                                       final int secondGoals, final int type, final int teams) {

        getCup(first.getSeason(), type).getFixtures().add(
                new Fixture(second, true, new int[]{firstGoals, secondGoals}));
        getCup(second.getSeason(), type).getFixtures().add(
                new Fixture(first, false, new int[]{secondGoals, firstGoals}));

        getCup(first.getSeason(), type).setStage(String.valueOf(firstGoals > secondGoals ? teams / 2 : teams));
        getCup(second.getSeason(), type).setStage(String.valueOf(secondGoals > firstGoals ? teams / 2 : teams));
    }

    public static Club[] startCup(final Club[] league) {
        final Club[] selected = new Club[16];
        System.arraycopy(league, 0, selected, 0, 16);
        return selected;
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
