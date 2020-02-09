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
import static simulation.Helper.appendScore;
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

        for (final Club[] league : LEAGUES) {
            final String leagueName = league[0].getLeague();
            nationalCup.put(leagueName, cup(league));
        }

        for (final Club[] league : new Club[][]{England.CLUBS, France.CLUBS}) {
            final String leagueName = league[0].getLeague();
            leagueCup.put(leagueName, cup(league));
        }
    }

    static void nationalCup() {
        for (final Club[] league : LEAGUES) {
            final String leagueName = league[0].getLeague();
            nationalCup.put(leagueName, knockoutRound(nationalCup.get(leagueName),
                    leagueName.equals(Spain.LEAGUE) ? 2 : 1, NATIONAL_CUP, leagueName.equals(England.LEAGUE)));
        }
    }

    static void leagueCup() {
        for (final Club[] league : new Club[][]{England.CLUBS, France.CLUBS}) {
            final String leagueName = league[0].getLeague();
            leagueCup.put(leagueName, knockoutRound(leagueCup.get(leagueName),
                    1, Competition.LEAGUE_CUP, false));
        }
    }

    static void championsLeague() {
        continentalCup.put(CHAMPIONS_LEAGUE_NAME, knockoutRound(continentalCup.get(CHAMPIONS_LEAGUE_NAME),
                2, Competition.CHAMPIONS_LEAGUE, false));
    }

    static void europaLeague() {
        continentalCup.put(EUROPA_LEAGUE_NAME, knockoutRound(continentalCup.get(EUROPA_LEAGUE_NAME),
                2, Competition.EUROPA_LEAGUE, false));
    }

    static Club[] knockoutRound(final Club[] clubs, final int games, final Competition type, final boolean replay) {
        final int count = clubs.length;
        final String competition;
        final Map<String, String> results;
        switch (type) {
            case NATIONAL_CUP: competition = clubs[0].getLeague(); results = nationalCupResults; break;
            case LEAGUE_CUP: competition = clubs[0].getLeague(); results = leagueCupResults; break;
            case CHAMPIONS_LEAGUE: competition = CHAMPIONS_LEAGUE_NAME; results = continentalCupResults; break;
            case EUROPA_LEAGUE: competition = EUROPA_LEAGUE_NAME; results = continentalCupResults; break;
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

    private static boolean knockoutFixture(final Club first, final Club second, final int games, final Competition type,
                                           final boolean replay, final boolean neutral,
                                           final Map<String, String> results, final int teams) {
        int firstGoals = 0;
        int secondGoals = 0;
        if (neutral) FANS = 0;

        final Report report = new Report(first, second, type, -1, -1, games == 1 && !replay);
        int[] result = simulate(report);
        firstGoals += result[0];
        secondGoals += result[1];

        final StringBuilder scores = new StringBuilder(String.valueOf(results.get("new")));
        final StringBuilder reports = new StringBuilder(String.valueOf(results.get("reports: new")));
        if (games == 2 && scores.length() > 0) scores.append("<br/>");
        appendScore(scores, reports, first, second, result);

        if (games == 2 || (replay && firstGoals == secondGoals)) {
            final Report report2 = new Report(second, first, type,
                    replay ? -1 : secondGoals, replay ? -1 : firstGoals, true);
            result = simulate(report2);
            appendScore(scores, reports, second, first, result);

            secondGoals += result[0];
            firstGoals += result[1];

            if (firstGoals == secondGoals) {
                if (result[0] + result[1] > firstGoals) firstGoals++;
                else secondGoals++;
            }
        }

        updateFixtures(first, second, firstGoals, secondGoals, type.getType(), teams);
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
