package simulation;

import competition.England;
import competition.France;
import competition.Spain;
import players.Footballer;
import team.Club;
import team.League;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.util.Collections.shuffle;
import static simulation.Data.FANS;
import static simulation.Data.LEAGUES;
import static simulation.Data.USER;
import static simulation.Data.addDummies;
import static simulation.Data.buildSquads;
import static simulation.Data.prepare;
import static simulation.Draw.league;
import static simulation.Draw.seededKnockout;
import static simulation.Finance.knockoutPrizes;
import static simulation.Finance.profits;
import static simulation.Finance.salaries;
import static simulation.Helper.appendScore;
import static simulation.Helper.cup;
import static simulation.Helper.getPerformance;
import static simulation.Helper.groupGameOutcome;
import static simulation.Helper.sortMap;
import static simulation.Match.simulation;
import static simulation.Preseason.progression;
import static simulation.Printer.pickContinentalTeams;
import static simulation.Printer.standings;
import static simulation.Tactics.preMatch;
import static simulation.Transfer.transfers;

public class Controller {
    private static final Scanner scanner = new Scanner(System.in);

    private static int year = 0;
    static int round = 0;

    public static final String CHAMPIONS_LEAGUE_NAME = "Champions League";
    public static final String EUROPA_LEAGUE_NAME = "Europa League";
    public static final Club[] CHAMPIONS_LEAGUE = new Club[32];
    public static final Club[] EUROPA_LEAGUE = new Club[48];

    private static final Map<String, int[][][]> leagueDraw = new HashMap<>();
    private static final Map<String, int[][][]> continentalDraw = new HashMap<>();
    private static final Map<String, Club[]> leagueCup = new HashMap<>();
    private static final Map<String, Club[]> nationalCup = new HashMap<>();
    private static final Map<String, Club[]> continentalCup = new HashMap<>();
    public static final Map<String, String> leagueResults = new HashMap<>();
    public static final Map<String, String> leagueCupResults = new HashMap<>();
    public static final Map<String, String> nationalCupResults = new HashMap<>();
    public static final Map<String, String> continentalCupResults = new HashMap<>();

    public static void main(String[] args) {
        initialise();
        while(scanner.nextInt() != 0) {
            for (int i = 0; i < 38; i++) Controller.proceed();
        }
    }

    public static void initialise() {
//        extractData();
        buildSquads();
        addDummies();
    }

    public static void proceed() {
        if (round == 0) {
            pickContinentalTeams(CHAMPIONS_LEAGUE, EUROPA_LEAGUE);
            prepare(year);

            leagueDraw.clear();
            continentalDraw.clear();
            leagueCup.clear();
            nationalCup.clear();
            continentalCup.clear();
            leagueResults.clear();
            leagueCupResults.clear();
            nationalCupResults.clear();
            continentalCupResults.clear();

            for (final Club[] league : LEAGUES) {
                final String leagueName = league[0].getLeague();
                leagueDraw.put(leagueName, league(league.length));
                nationalCup.put(leagueName, cup(league));
            }

            for (final Club[] league : new Club[][]{England.CLUBS, France.CLUBS}) {
                final String leagueName = league[0].getLeague();
                leagueCup.put(leagueName, cup(league));
            }

            continentalDraw.put(CHAMPIONS_LEAGUE_NAME, league(4));
            continentalDraw.put(EUROPA_LEAGUE_NAME, league(4));
        }

        for (final Club[] league : LEAGUES) {
            if (round < 34 || league.length > 18) leagueRound(league, leagueDraw.get(league[0].getLeague()), round);
        }

        if (round % 10 == 3) {
            for (final Club[] league : new Club[][]{England.CLUBS, France.CLUBS}) {
                final String leagueName = league[0].getLeague();
                leagueCup.put(leagueName, knockoutRound(leagueCup.get(leagueName),
                        1, 2, false));
            }
        }

        if (round % 10 == 7) {
            for (final Club[] league : LEAGUES) {
                final String leagueName = league[0].getLeague();
                nationalCup.put(leagueName, knockoutRound(nationalCup.get(leagueName),
                        leagueName.equals(Spain.LEAGUE) ? 2 : 1, 1, leagueName.equals(England.LEAGUE)));
            }
        }

        if (round % 4 == 0) {
            if (round < 21) {
                groupRound(CHAMPIONS_LEAGUE_NAME, CHAMPIONS_LEAGUE, round / 4);
                groupRound(EUROPA_LEAGUE_NAME, EUROPA_LEAGUE, round / 4);
            } else {
                continentalCup.put(EUROPA_LEAGUE_NAME,
                        knockoutRound(continentalCup.get(EUROPA_LEAGUE_NAME), 2, 4, false));

                continentalCup.put(CHAMPIONS_LEAGUE_NAME,
                        knockoutRound(continentalCup.get(CHAMPIONS_LEAGUE_NAME), 2, 3, false));
            }
        }

        if (round == 21) {
            concludeGroups(EUROPA_LEAGUE_NAME, EUROPA_LEAGUE);
            concludeGroups(CHAMPIONS_LEAGUE_NAME, CHAMPIONS_LEAGUE);

            continentalCup.put(EUROPA_LEAGUE_NAME,
                    knockoutRound(continentalCup.get(EUROPA_LEAGUE_NAME), 2, 4, false));
        }

        if (++round == 38) {
            round = 0;
            for (final Club[] league : LEAGUES) {
                standings(league);
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
//            Printer.pickTeam(topTeam, false);
//            voting(contenders);
            year++;

            progression();
            transfers();
        }
    }

    private static void leagueRound(final Club[] league, final int[][][] draw, final int round) {
        System.out.println(String.format("Round %d", round + 1));
        final StringBuilder scores = new StringBuilder();
        final StringBuilder reports = new StringBuilder();
        for (int game = 0; game < league.length / 2; game++) {
            final int home = draw[round][game][0];
            final int away = draw[round][game][1];
            if (home == USER) preMatch(league[away], true);
            else if (away == USER) preMatch(league[home], false);
            final int[] result = simulation(league[home], league[away], false, -1, -1, 0);
            appendScore(scores, reports, league[home], league[away], result);
        }

        leagueResults.put(league[0].getLeague() + (round + 1), scores.toString());
        leagueResults.put("reports: " + league[0].getLeague() + (round + 1), reports.toString());
    }

    private static void groupRound(final String competition, final Club[] teams, final int round) {
        final int groups = teams.length / 4;
        final int[][][] draw = continentalDraw.get(competition);
        for (int group = 0; group < groups; group++) {
            final String letter = String.valueOf((char) ('A' + group));
            System.out.println("GROUP " + letter);

            final StringBuilder scores = new StringBuilder(continentalCupResults.getOrDefault(
                    competition + letter, ""));
            final StringBuilder reports = new StringBuilder(continentalCupResults.getOrDefault(
                    "reports: " + competition + letter, ""));

            final Club[] clubs = new Club[4];
            for (int team = 0; team < 4; team++) clubs[team] = teams[groups * team + group];
            System.out.println();
            System.out.println("Matchday " + (round + 1));
            if (scores.length() > 0) scores.append("<br/>");
            if (reports.length() > 0) reports.append("<br/>");

            for (int game = 0; game < 2; game++) {
                final int home = draw[round][game][0];
                final int away = draw[round][game][1];
                final int[] result = simulation(clubs[home], clubs[away],
                        false, -1, -1, 3);

                appendScore(scores, reports, clubs[home], clubs[away], result);
                final int homeGoals = result[0];
                final int awayGoals = result[1];

                groupGameOutcome(clubs[home].getSeason().getContinental().getGroup(), homeGoals, awayGoals);
                groupGameOutcome(clubs[away].getSeason().getContinental().getGroup(), awayGoals, homeGoals);
            }

            continentalCupResults.put(competition + letter, scores.toString());
            continentalCupResults.put("reports: " + competition + letter, reports.toString());
        }
    }

    private static void concludeGroups(final String competition, final Club[] teams) {
        final Club[] advancing = new Club[2 * teams.length / 4];
        final int groups = teams.length / 4;
        int count = 0;

        final List<Club> thirds = new ArrayList<>();
        for (int group = 0; group < groups; group++) {
            System.out.println("GROUP " + (char) ('A' + group));

            final Map<Club, Integer> standing = new LinkedHashMap<>();
            for (int team = 0; team < 4; team++) {
                final League groupStats = teams[4 * group + team].getSeason().getContinental().getGroup();
                standing.put(teams[4 * group + team], getPerformance(groupStats));
            }

            System.out.println();
            final Map<Club, Integer> sorted = sortMap(standing);

            System.out.println();
            System.out.println("Standings for group " + (char) ('A' + group));
            System.out.println("No  Teams                     G  W  D  L  GF:GA  P");
            final Club[] rankedTeams = sorted.keySet().toArray(new Club[0]);
            for (int team = 0; team < sorted.size(); team++) {
                final Club club = rankedTeams[team];
                final League groupStats = club.getSeason().getContinental().getGroup();

                System.out.println(String.format("%2d. %-25s %-2d %-2d %-2d %-2d %2d:%-2d %2d", team + 1, club.getName(),
                        groupStats.getMatches(), groupStats.getWins(), groupStats.getDraws(), groupStats.getLosses(),
                        groupStats.getScored(), groupStats.getConceded(), groupStats.getPoints()));

                if (team < 2) advancing[count++] = club;
            }

            if (competition.equals(CHAMPIONS_LEAGUE_NAME)) thirds.add(rankedTeams[2]);
        }

        if (competition.equals(CHAMPIONS_LEAGUE_NAME)) {
            final Club[] europaLeagueTeams = new Club[32];
            System.arraycopy(continentalCup.get(EUROPA_LEAGUE_NAME), 0, europaLeagueTeams, 0, 24);
            System.out.println("Third are..");
            thirds.forEach(t -> System.out.println(t.getName()));
            for (int team = 24; team < 32; team++) europaLeagueTeams[team] = thirds.remove(0);
            System.out.println(thirds.size());
            for (int team = 0; team < 32; team++) System.out.println(europaLeagueTeams[team].getName());
            continentalCup.put(EUROPA_LEAGUE_NAME, europaLeagueTeams);
        }

        continentalCup.put(competition, seededKnockout(advancing));
    }

    private static Club[] knockoutRound(final Club[] clubs, final int games, final int type, final boolean replay) {
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
                    type, replay && count >= 8, count == 2, results)) {
                clubs[team] = draw.get(count - team - 1);
            } else clubs[team] = draw.get(team);
        }

        results.put(competition + count, results.remove("new"));
        results.put("reports: " + competition + count, results.remove("reports: new"));
        return Arrays.copyOf(clubs, count / 2);
    }

    private static boolean knockoutFixture(final Club first, final Club second, final int games, final int type,
                                           final boolean replay, final boolean neutral, final Map<String, String> results) {
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

        results.put("new", scores.toString());
        results.put("reports: new", reports.toString());
        return secondGoals > firstGoals;
    }

    private static void pickTeam() {
        System.out.println("Pick a team from 1 to 20");
        while (true) {
            final int team = scanner.nextInt();
            if (team < 1 || team > 20) {
                System.out.println("Wrong number.");
                continue;
            }

            USER = team - 1;
            break;
        }
    }
}
