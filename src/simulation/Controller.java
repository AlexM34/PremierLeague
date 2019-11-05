package simulation;

import competitions.England;
import competitions.France;
import competitions.Spain;
import players.Footballer;
import teams.Club;
import teams.League;

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
import static simulation.Draw.championsLeague;
import static simulation.Draw.league;
import static simulation.Finances.knockoutPrizes;
import static simulation.Finances.profits;
import static simulation.Finances.salaries;
import static simulation.Match.simulation;
import static simulation.PreSeason.progression;
import static simulation.Printer.pickChampionsLeagueTeams;
import static simulation.Printer.standings;
import static simulation.Printer.topTeam;
import static simulation.Printer.voting;
import static simulation.Rater.contenders;
import static simulation.Tactics.preMatch;
import static simulation.Transfers.transfers;
import static simulation.Utils.sortMap;

public class Controller {
    private static final Scanner scanner = new Scanner(System.in);

    private static final boolean userFlag = false;
    private static final boolean standingsFlag = false;
    static final boolean matchFlag = false;
    private static int year = 0;
    private static int round = 0;
    public static String CHAMPIONS_LEAGUE_NAME = "Champions League";
    public static Club[] CHAMPIONS_LEAGUE = new Club[32];
    private static Map<String, int[][][]> leagueDraw = new HashMap<>();
    private static Map<String, int[][][]> continentalDraw = new HashMap<>();
    private static Map<String, Club[]> nationalCup = new HashMap<>();
    private static Map<String, Club[]> leagueCup = new HashMap<>();
    private static Map<String, Club[]> continentalCup = new HashMap<>();
    public static Map<String, String> leagueResults = new HashMap<>();
    public static Map<String, String> nationalCupResults = new HashMap<>();
    public static Map<String, String> leagueCupResults = new HashMap<>();
    public static Map<String, String> continentalCupResults = new HashMap<>();

    public static void initialise() {
//        extractData();
        buildSquads();
        addDummies();
    }

    public static void proceed() {
        if (round == 0) {
            prepare(year);
            if (userFlag) pickTeam();

            for (final Club[] league : LEAGUES) {
                final String leagueName = league[0].getLeague();
                leagueDraw.put(leagueName, league(league.length));
                nationalCup.put(leagueName, cup(league));
            }

            for (final Club[] league : new Club[][]{England.CLUBS, France.CLUBS}) {
                final String leagueName = league[0].getLeague();
                leagueCup.put(leagueName, cup(league));
            }

            CHAMPIONS_LEAGUE = pickChampionsLeagueTeams();
            continentalDraw.put(CHAMPIONS_LEAGUE_NAME, league(4));
        }

        for (final Club[] league : LEAGUES) {
            if (round < 34 || league.length > 18) play(league, leagueDraw.get(league[0].getLeague()), round);
        }

        if (round % 8 == 3) {
            for (final Club[] league : new Club[][]{England.CLUBS, France.CLUBS}) {
                final String leagueName = league[0].getLeague();
                leagueCup.put(leagueName, knockoutRound(leagueCup.get(leagueName),
                        1, 2, false));
            }
        }

        if (round % 8 == 7) {
            for (final Club[] league : LEAGUES) {
                final String leagueName = league[0].getLeague();
                nationalCup.put(leagueName, knockoutRound(nationalCup.get(leagueName),
                        leagueName.equals(Spain.LEAGUE) ? 2 : 1, 1, leagueName.equals(England.LEAGUE)));
            }
        }

        if (round % 4 == 0) {
            if (round < 21) {
                groupStage(CHAMPIONS_LEAGUE, round / 4);
            } else {
                continentalCup.put(CHAMPIONS_LEAGUE_NAME, knockoutRound(
                        continentalCup.get(CHAMPIONS_LEAGUE_NAME), 2, 3, false));
            }
        }

        if (round == 21) {
            concludeGroups(CHAMPIONS_LEAGUE);
        }

        if (++round == 38) {
            round = 0;
            for (final Club[] league : LEAGUES) {
                final String leagueName = league[0].getLeague();
                final Club leagueCupWinner = leagueCup.get(leagueName)[0];
                if (leagueCupWinner != null) {
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

            final Club championsLeagueWinner = CHAMPIONS_LEAGUE[0];
            System.out.println(championsLeagueWinner.getName() + " win the Champions League!");
            championsLeagueWinner.getGlory().addContinental();
            for (final Footballer footballer : championsLeagueWinner.getFootballers()) {
                footballer.getResume().getGlory().addContinental();
            }

            knockoutPrizes(CHAMPIONS_LEAGUE, true);

            year++;
            Printer.pickTeam(topTeam, false);
            voting(contenders);

            progression();
            transfers();
        }
    }

    private static void concludeGroups(final Club[] teams) {
        final Club[] advancing = new Club[2 * teams.length / 4];
        final int groups = teams.length / 4;
        int count = 0;

        for (int group = 0; group < groups; group++) {
            System.out.println("GROUP " + (char) ('A' + group));

            final Map<Club, Integer> standing = new LinkedHashMap<>();
            for (int team = 0; team < 4; team++) {
                final League groupStats = teams[4 * group + team].getSeason().getChampionsLeague().getGroup();

                standing.put(teams[4 * group + team], 10000 * groupStats.getPoints() +
                        100 * (groupStats.getScored() - groupStats.getConceded()) + groupStats.getScored());
            }

            System.out.println();
            final Map<Club, Integer> sorted = sortMap(standing);

            System.out.println();
            System.out.println("Standings for group " + (char) ('A' + group));
            System.out.println("No  Teams                     G  W  D  L  GF:GA  P");
            final Club[] rankedTeams = sorted.keySet().toArray(new Club[0]);
            for (int team = 0; team < sorted.size(); team++) {
                final Club club = rankedTeams[team];
                final League groupStats = club.getSeason().getChampionsLeague().getGroup();

                System.out.println(String.format("%2d. %-25s %-2d %-2d %-2d %-2d %2d:%-2d %2d", team + 1, club.getName(),
                        groupStats.getMatches(), groupStats.getWins(), groupStats.getDraws(), groupStats.getLosses(),
                        groupStats.getScored(), groupStats.getConceded(), groupStats.getPoints()));

                if (team < 2) advancing[count++] = club;
            }
        }

        continentalCup.put(CHAMPIONS_LEAGUE_NAME, championsLeague(advancing));
    }

    private static void groupStage(final Club[] teams, final int round) {
        final int groups = teams.length / 4;
        final int[][][] draw = continentalDraw.get(CHAMPIONS_LEAGUE_NAME);
        final StringBuilder scores = new StringBuilder();
        for (int group = 0; group < groups; group++) {
            System.out.println("GROUP " + (char) ('A' + group));

            final Club[] clubs = new Club[4];
            for (int team = 0; team < 4; team++) clubs[team] = teams[groups * team + group];
            System.out.println();
            System.out.println("Matchday " + (round + 1));

            for (int game = 0; game < 2; game++) {
                final int home = draw[round][game][0];
                final int away = draw[round][game][1];
                final int[] result = simulation(clubs[home], clubs[away],
                        false, -1, -1, 3);

                final int homeGoals = result[0];
                final int awayGoals = result[1];

                scores.append(clubs[home].getName())
                        .append(" - ").append(clubs[away].getName())
                        .append(" ").append(homeGoals).append(":")
                        .append(awayGoals).append("<br/>");

                final League homeStats = clubs[home].getSeason().getChampionsLeague().getGroup();
                final League awayStats = clubs[away].getSeason().getChampionsLeague().getGroup();

                if (homeGoals > awayGoals) {
                    homeStats.addPoints(3);
                    homeStats.addWin();
                    awayStats.addLoss();
                } else if (homeGoals < awayGoals) {
                    awayStats.addPoints(3);
                    awayStats.addWin();
                    homeStats.addLoss();
                } else {
                    homeStats.addPoints(1);
                    awayStats.addPoints(1);
                    homeStats.addDraw();
                    awayStats.addDraw();
                }

                if (awayGoals == 0) homeStats.addCleanSheet();
                if (homeGoals == 0) awayStats.addCleanSheet();

                homeStats.addMatch();
                awayStats.addMatch();
                homeStats.addScored(homeGoals);
                awayStats.addScored(awayGoals);
                homeStats.addConceded(awayGoals);
                awayStats.addConceded(homeGoals);
            }
        }

        continentalCupResults.put(CHAMPIONS_LEAGUE_NAME, scores.toString());
    }

    private static void play(final Club[] league, final int[][][] draw, final int round) {
        System.out.println(String.format("Round %d", round + 1));
        StringBuilder scores = new StringBuilder();
        for (int game = 0; game < league.length / 2; game++) {
            final int home = draw[round][game][0];
            final int away = draw[round][game][1];
            if (home == USER) preMatch(league[away], true);
            else if (away == USER) preMatch(league[home], false);
            final int[] score = simulation(league[home], league[away], false, -1, -1, 0);

            scores.append(league[home].getName())
                    .append(" - ").append(league[away].getName())
                    .append(" ").append(score[0]).append(":")
                    .append(score[1]).append("<br/>");
        }

        leagueResults.put(league[0].getLeague(), scores.toString());
        if (round < 2 * league.length - 3 && standingsFlag) {
            System.out.println();
            System.out.println(String.format("Standings after round %d:", round + 1));
            standings(league);
        }

        System.out.println();
    }

    private static Club[] cup(final Club[] league) {
        final Club[] selected = new Club[16];
        System.arraycopy(league, 0, selected, 0, 16);

        return selected;
    }

    private static Club[] knockoutRound(final Club[] clubs, final int games, final int type, final boolean replay) {
        final int count = clubs.length;

        final List<Club> draw = Arrays.asList(clubs);
        shuffle(draw);
        for (int team = 0; team < count / 2; team++) {
            if (knockoutFixture(draw.get(team), draw.get(count - team - 1), round < Math.sqrt(count)
                    ? games : 1, type, replay && count >= 8, round == Math.sqrt(count))) {
                clubs[team] = draw.get(count - team - 1);
            } else {
                clubs[team] = draw.get(team);
            }
        }

        return Arrays.copyOf(clubs, count / 2);
    }

    private static boolean knockoutFixture(final Club first, final Club second, final int games, final int type,
                                           final boolean replay, final boolean neutral) {
        int firstGoals = 0;
        int secondGoals = 0;

        if (neutral) FANS = 0;
        int[] result = simulation(first, second, games == 1 && !replay, -1, -1, type);
        Map<String, String> results;
        switch (type) {
            case 1:
                results = nationalCupResults;
                break;
            case 2:
                results = leagueCupResults;
                break;
            case 3:
                results = continentalCupResults;
                break;
            default:
                results = new HashMap<>();
                break;
        }

        final StringBuilder scores = new StringBuilder(results.getOrDefault(first.getLeague(), ""));
        scores.append(first.getName())
                .append(" - ").append(second.getName())
                .append(" ").append(result[0]).append(":")
                .append(result[1]).append("<br/>");

        firstGoals += result[0];
        secondGoals += result[1];

        if (games == 2 || (replay && firstGoals == secondGoals)) {
            result = simulation(second, first, true, replay ? -1 : secondGoals, replay ? -1 : firstGoals, type);
            scores.append(second.getName())
                    .append(" - ").append(first.getName())
                    .append(" ").append(result[0]).append(":")
                    .append(result[1]).append("<br/>");

            secondGoals += result[0];
            firstGoals += result[1];

            if (firstGoals == secondGoals) {
                if (result[0] + result[1] > firstGoals) firstGoals++;
                else secondGoals++;
            }
        }

        final String leagueName = type < 3 ? first.getLeague() : CHAMPIONS_LEAGUE_NAME;
        results.put(leagueName, scores.toString());

        return secondGoals > firstGoals;
    }

    private static void pickTeam() {
        System.out.println("Pick a team from 1 to 20");
        while (true) {
            final int team = scanner.nextInt();
            if (team < 0 || team > 20) {
                System.out.println("Wrong team number.");
                continue;
            }

            USER = team - 1;
            break;
        }
    }
}
