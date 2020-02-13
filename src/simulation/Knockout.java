package simulation;

import leagues.England;
import leagues.France;
import leagues.Spain;
import players.Footballer;
import team.Club;
import team.Season;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.shuffle;
import static simulation.Competition.NATIONAL_CUP;
import static simulation.Data.FANS;
import static simulation.Data.LEAGUES;
import static simulation.Finance.knockoutPrizes;
import static simulation.Finance.profits;
import static simulation.Finance.salaries;
import static simulation.Helper.cup;
import static simulation.League.CHAMPIONS_LEAGUE;
import static simulation.League.CHAMPIONS_LEAGUE_NAME;
import static simulation.League.EUROPA_LEAGUE;
import static simulation.League.EUROPA_LEAGUE_NAME;
import static simulation.League.continentalCup;
import static simulation.League.continentalCupResults;
import static simulation.Match.simulate;

public class Knockout {
    private static final Map<String, Club[]> leagueCup = new HashMap<>();
    private static final Map<String, Club[]> nationalCup = new HashMap<>();
    public static final Map<String, String> leagueCupResults = new HashMap<>();
    public static final Map<String, String> nationalCupResults = new HashMap<>();

    static void setupCups() {
        leagueCup.clear();
        nationalCup.clear();
        leagueCupResults.clear();
        nationalCupResults.clear();

        for (final Club[] league : LEAGUES) nationalCup.put(league[0].getLeague(), cup(league));

        for (final Club[] league : new Club[][]{England.CLUBS, France.CLUBS}) {
            leagueCup.put(league[0].getLeague(), cup(league));
        }
    }

    static void nationalCup() {
        for (final Club[] league : LEAGUES) {
            final String leagueName = league[0].getLeague();
            knockoutRound(nationalCup, leagueName, leagueName.equals(Spain.LEAGUE) ? 2 : 1,
                    NATIONAL_CUP, leagueName.equals(England.LEAGUE));
        }
    }

    static void leagueCup() {
        for (final Club[] league : new Club[][]{England.CLUBS, France.CLUBS}) {
            knockoutRound(leagueCup, league[0].getLeague(), 1, Competition.LEAGUE_CUP, false);
        }
    }

    static void championsLeague() {
        knockoutRound(continentalCup, CHAMPIONS_LEAGUE_NAME, 2, Competition.CHAMPIONS_LEAGUE, false);
    }

    static void europaLeague() {
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

        if (count == 2) FANS = 0;
        for (int team = 0; team < count / 2; team++) {
            final Report report = new Report(draw.get(team), draw.get(count - team - 1), type,
                    -1, -1, games == 1 && (!replay || count < 8));
            if (knockoutFixture(report, games, replay, results, count)) {
                clubs[team] = draw.get(count - team - 1);
            } else {
                clubs[team] = draw.get(team);
            }
        }

        results.put(competition + count, results.remove("new"));
        results.put("reports: " + competition + count, results.remove("reports: new"));
        cup.put(competition, Arrays.copyOf(clubs, count / 2));
    }

    private static boolean knockoutFixture(final Report report, final int games, final boolean replay,
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

        if (games == 2 || (replay && firstGoals == secondGoals)) {
            final Report report2 = new Report(second, first, report.getCompetition(),
                    replay ? -1 : secondGoals, replay ? -1 : firstGoals, true);
            simulate(report2);
            report2.appendScore(scores, reports);

            secondGoals += report.getHomeGoals();
            firstGoals += report.getAwayGoals();

            if (firstGoals == secondGoals) {
                if (report.getHomeGoals() + report.getAwayGoals() > firstGoals) firstGoals++;
                else secondGoals++;
            }
        }

        updateFixtures(first, second, firstGoals, secondGoals, report.getCompetition().getType(), teams);
        results.put("new", scores.toString());
        results.put("reports: new", reports.toString());
        return secondGoals > firstGoals;
    }

    static void announceCupWinners() {
        for (final Club[] league : LEAGUES) {
            final String leagueName = league[0].getLeague();
            if (leagueCup.containsKey(leagueName)) {
                final Club leagueCupWinner = leagueCup.get(leagueName)[0];
                System.out.println(leagueCupWinner.getName() + " win the League Cup!");
                leagueCupWinner.getGlory().addLeagueCup();
                for (final Footballer footballer : leagueCupWinner.getFootballers()) {
                    footballer.getResume().getGlory().addLeagueCup();
                }

                knockoutPrizes(leagueCup.get(leagueName), false);
            }

            final Club nationalCupWinner = nationalCup.get(leagueName)[0];
            System.out.println(nationalCupWinner.getName() + " win the National Cup!");
            nationalCupWinner.getGlory().addNationalCup();
            for (final Footballer footballer : nationalCupWinner.getFootballers()) {
                footballer.getResume().getGlory().addNationalCup();
            }

            knockoutPrizes(nationalCup.get(leagueName), false);

            profits(league);
            salaries(league);

            Arrays.stream(league).forEach(Printer::review);
        }

        final Club europaLeagueWinner = continentalCup.get(EUROPA_LEAGUE_NAME)[0];
        System.out.println(europaLeagueWinner.getName() + " win the Europa League!");
        europaLeagueWinner.getGlory().addEuropaLeague();
        for (final Footballer footballer : europaLeagueWinner.getFootballers()) {
            footballer.getResume().getGlory().addEuropaLeague();
        }

        final Club championsLeagueWinner = continentalCup.get(CHAMPIONS_LEAGUE_NAME)[0];
        System.out.println(championsLeagueWinner.getName() + " win the Champions League!");
        championsLeagueWinner.getGlory().addChampionsLeague();
        for (final Footballer footballer : championsLeagueWinner.getFootballers()) {
            footballer.getResume().getGlory().addChampionsLeague();
        }

        knockoutPrizes(EUROPA_LEAGUE, true);
        knockoutPrizes(CHAMPIONS_LEAGUE, true);
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
