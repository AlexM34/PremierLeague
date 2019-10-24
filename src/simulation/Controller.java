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
import static simulation.Draw.league;
import static simulation.Finances.knockoutPrizes;
import static simulation.Finances.profits;
import static simulation.Finances.salaries;
import static simulation.Match.simulation;
import static simulation.PreSeason.progression;
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
    private static Map<String, int[][][]> draw = new HashMap<>();
    private static Map<String, Club[]> nationalCup = new HashMap<>();
    private static Map<String, Club[]> leagueCup = new HashMap<>();
    public static Map<String, String> leagueResults = new HashMap<>();
    public static Map<String, String> nationalCupResults = new HashMap<>();
    public static Map<String, String> leagueCupResults = new HashMap<>();

    public static void initialise() {
//        extractData();
        buildSquads();
        addDummies();
    }

    public static void proceed() {
        leagueResults = new HashMap<>();

        if (round == 0) {
            prepare(year);
            if (userFlag) pickTeam();

            draw = new HashMap<>();

            for (final Club[] league : LEAGUES) {
                final String leagueName = league[0].getLeague();
                draw.put(leagueName, league(league.length));
                nationalCup.put(leagueName, cup(league));
                if (leagueName.equals("Premier League") || leagueName.equals("Ligue 1")) {
                    leagueCup.put(leagueName, cup(league));
                }
            }
        }

        for (final Club[] league : LEAGUES) {
            if (round < 34 || league.length > 18) play(league, draw.get(league[0].getLeague()), round);
        }

        if (round % 8 == 3) {
            leagueCupResults = new HashMap<>();
            for (final Club[] league : new Club[][]{England.CLUBS, France.CLUBS}) {
                final String leagueName = league[0].getLeague();
                leagueCup.put(leagueName, knockoutRound(leagueCup.get(leagueName),
                        1, 1, false, true));
            }
        }

        if (round % 8 == 7) {
            nationalCupResults = new HashMap<>();
            for (final Club[] league : LEAGUES) {
                final String leagueName = league[0].getLeague();
                nationalCup.put(leagueName, knockoutRound(nationalCup.get(leagueName),
                        leagueName.equals(Spain.LEAGUE) ? 2 : 1, 1, leagueName.equals(England.LEAGUE), false));
            }
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
                }

                final Club nationalCupWinner = leagueCup.get(leagueName)[0];
                System.out.println(nationalCupWinner.getName() + " win the National Cup!");
                nationalCupWinner.getGlory().addNationalCup();
                for (final Footballer footballer : nationalCupWinner.getFootballers()) {
                    footballer.getResume().getGlory().addNationalCup();
                }

                profits(league);
                salaries(league);
            }

            year++;
            Printer.pickTeam(topTeam, false);
            voting(contenders);

            progression();
            transfers();
        }

//        CHAMPIONS_LEAGUE = pickChampionsLeagueTeams();
//        for (final Club team : CHAMPIONS_LEAGUE) {
//            team.getSeason().getChampionsLeague().setAlive(true);
//        }
//        final Club[] advanced = groups(CHAMPIONS_LEAGUE);
//        for (Club club : advanced) club.changeBudget(5f);
//        final Club[] drawn = championsLeague(advanced);
//        final Club championsLeagueWinner = knockout(drawn, 2, 2, false);
//        System.out.println(championsLeagueWinner.getName() + " win the Champions League!");
//        championsLeagueWinner.getGlory().addContinental();
//        for (final Footballer footballer : championsLeagueWinner.getFootballers()) {
//            footballer.getResume().getGlory().addContinental();
//        }
//
//        playerStats(CHAMPIONS_LEAGUE, 2);
    }

    private static Club[] groups(final Club[] teams) {
        final Club[] advancing = new Club[2 * teams.length / 4];
        final int groups = teams.length / 4;
        int count = 0;

        final int[][][] draw = league(4);
        for (int group = 0; group < groups; group++) {
            System.out.println("GROUP " + (char) ('A' + group));

            final Club[] clubs = new Club[4];
            for (int team = 0; team < 4; team++) clubs[team] = teams[groups * team + group];

            for (int round = 0; round < draw.length; round++) {
                System.out.println();
                System.out.println("Matchday " + (round + 1));

                for (int game = 0; game < 4 / 2; game++) {
                    final int home = draw[round][game][0];
                    final int away = draw[round][game][1];
                    final int[] result = simulation(clubs[home], clubs[away], false, -1, -1, 2);
                    final int homeGoals = result[0];
                    final int awayGoals = result[1];
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

            final Map<Club, Integer> standing = new LinkedHashMap<>();
            for (int team = 0; team < 4; team++)
                standing.put(clubs[team],
                        10000 * clubs[team].getSeason().getChampionsLeague().getGroup().getPoints() +
                                100 * (clubs[team].getSeason().getChampionsLeague().getGroup().getScored() -
                                        clubs[team].getSeason().getChampionsLeague().getGroup().getConceded()) +
                                clubs[team].getSeason().getChampionsLeague().getGroup().getScored());

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

            System.out.println();
        }

        return advancing;
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

    private static Club knockout(final Club[] selected, final int games, final int type, final boolean replay) {
        int count = selected.length;

        for (int round = 1; round <= Math.sqrt(selected.length); round++) {
            System.out.println();
            switch (count) {
                case 2:
                    System.out.println("FINAL");
                    break;
                case 4:
                    System.out.println("SEMI-FINALS");
                    break;
                case 8:
                    System.out.println("QUARTER-FINALS");
                    break;
                default:
                    System.out.println("ROUND OF " + count);
                    break;
            }

            System.out.println();

            for (int team = 0; team < count / 2; team++) {
                if (knockoutFixture(selected[team], selected[count - team - 1], round < Math.sqrt(selected.length)
                        ? games : 1, type, replay && count >= 8, round == Math.sqrt(selected.length), false)) {
                    Club swap = selected[team];
                    selected[team] = selected[count - team - 1];
                    selected[count - team - 1] = swap;
                }
            }

            count /= 2;
        }

        if (type == 2) Rater.topPlayers(selected);
        knockoutPrizes(selected, type == 2);

        return selected[0];
    }

    private static Club[] knockoutRound(final Club[] clubs, final int games, final int type,
                                        final boolean replay, final boolean leagueCup) {
        final int count = clubs.length;

        final List<Club> draw = Arrays.asList(clubs);
        shuffle(draw);
        for (int team = 0; team < count / 2; team++) {
            if (knockoutFixture(draw.get(team), draw.get(count - team - 1), round < Math.sqrt(count)
                    ? games : 1, type, replay && count >= 8, round == Math.sqrt(count), leagueCup)) {
                clubs[team] = draw.get(count - team - 1);
            } else {
                clubs[team] = draw.get(team);
            }
        }

        return Arrays.copyOf(clubs, count / 2);
    }

    private static boolean knockoutFixture(final Club first, final Club second, final int games, final int type,
                                           final boolean replay, final boolean neutral, final boolean leagueCup) {
        int firstGoals = 0;
        int secondGoals = 0;

        if (neutral) FANS = 0;
        int[] result = simulation(first, second, games == 1 && !replay, -1, -1, type);
        Map<String, String> results = leagueCup ? leagueCupResults : nationalCupResults;
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

        results.put(first.getLeague(), scores.toString());

        return secondGoals > firstGoals;
    }
}
