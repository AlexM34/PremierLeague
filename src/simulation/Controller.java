package simulation;

import teams.Club;
import teams.League;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

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
    // TODO: Split large classes
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    private static final boolean userFlag = false;
    private static final boolean standingsFlag = false;
    static final boolean matchFlag = false;
    private static int year = 0;
    private static int round = 0;
    private static Map<Club[], int[][][]> draw = new HashMap<>();
    public static Map<String, String> results = new HashMap<>();

    public static void initialise() {
        buildSquads();
        addDummies();
    }

    public static void proceed() {
        results = new HashMap<>();

        if (round == 0) {
            prepare(year);
            if (userFlag) pickTeam();
            draw = new HashMap<>();
            for (final Club[] league : LEAGUES) draw.put(league, league(league.length));
        }

        for (int team = 0; team < 6; team++) {
            for (final Club[] clubs : LEAGUES) {
                final Club squad = clubs[team];
                System.out.println(squad.getBudget());
                squad.getFootballers().forEach(footballer ->
                        System.out.println(footballer.getName() + ", " + footballer.getPosition() + ", " +
                                footballer.getOverall() + ", " + footballer.getPotential() + ", " +
                                footballer.getAge() + " => " + (int) footballer.getValue()));
            }
        }

        for (final Club[] league : LEAGUES) {
            if (round < 34 || league.length > 18) play(league, draw.get(league), round);
        }

        if (++round == 38) {
            round = 0;
            for (final Club[] league : LEAGUES) {
                profits(league);
                salaries(league);
            }

            year++;
            Printer.pickTeam(topTeam, false);
            voting(contenders);

            progression();
            transfers();
        }

//        for (final Club[] league : LEAGUES) {
//            final Club nationalCupWinner = cup(league, 16,
//                    league[0].getLeague().equals(Spain.LEAGUE) ? 2 : 1,
//                    league[0].getLeague().equals(England.LEAGUE));
//            System.out.println(nationalCupWinner.getName() + " win the National Cup!");
//            nationalCupWinner.getGlory().addNationalCup();
//            for (final Footballer footballer : nationalCupWinner.getFootballers()) {
//                footballer.getResume().getGlory().addNationalCup();
//            }
//
//            if (league[0].getLeague().equals(England.LEAGUE) || league[0].getLeague().equals(France.LEAGUE)) {
//                final Club leagueCupWinner = cup(league, 16, 1, false);
//                System.out.println(leagueCupWinner.getName() + " win the League Cup!");
//                leagueCupWinner.getGlory().addLeagueCup();
//                for (final Footballer footballer : leagueCupWinner.getFootballers()) {
//                    footballer.getResume().getGlory().addLeagueCup();
//                }
//            }
//
//            playerStats(league, 1);
//        }
//
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
            System.out.println("GROUP " + (char)('A' + group));

            final Club[] clubs = new Club[4];
            for (int team = 0; team < 4; team++) clubs[team] = teams[groups * team + group];

            for (int round = 0; round < draw.length; round++) {
                System.out.println();
                System.out.println("Matchday " + (round + 1));

                for (int game = 0; game < 4 / 2; game++) {
                    final int home = draw[round][game][0];
                    final int away = draw[round][game][1];
                    final int result = simulation(clubs[home], clubs[away], false, -1, -1, 2);
                    final int homeGoals = result / 100;
                    final int awayGoals = result % 100;
                    final League homeStats = clubs[home].getSeason().getChampionsLeague().getGroup();
                    final League awayStats = clubs[away].getSeason().getChampionsLeague().getGroup();

                    if (result / 100 > result % 100) {
                        homeStats.addPoints(3);
                        homeStats.addWin();
                        awayStats.addLoss();
                    }
                    else if (result / 100 < result % 100) {
                        awayStats.addPoints(3);
                        awayStats.addWin();
                        homeStats.addLoss();
                    }
                    else {
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
            for (int team = 0; team < 4; team++) standing.put(clubs[team],
                    10000 * clubs[team].getSeason().getChampionsLeague().getGroup().getPoints() +
                    100 * (clubs[team].getSeason().getChampionsLeague().getGroup().getScored() -
                           clubs[team].getSeason().getChampionsLeague().getGroup().getConceded()) +
                    clubs[team].getSeason().getChampionsLeague().getGroup().getScored());

            final Map<Club, Integer> sorted = sortMap(standing);

            System.out.println();
            System.out.println("Standings for group " + (char)('A' + group));
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
            if(team < 0 || team > 20) {
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
            final int score = simulation(league[home], league[away], false, -1, -1, 0);

            scores.append(league[home].getName())
                    .append(" - ").append(league[away].getName())
                    .append(" ").append(score / 100).append(":")
                    .append(score % 100).append("<br/>");
        }

        results.put(league[0].getLeague(), scores.toString());
        if (round < 2 * league.length - 3 && standingsFlag) {
            System.out.println();
            System.out.println(String.format("Standings after round %d:", round + 1));
            standings(league);
        }

        System.out.println();
    }

    private static Club cup(final Club[] league, final int teams, final int games, final boolean replay) {
        int count = 0;
        final Club[] selected = new Club[teams];
        final boolean[] playing = new boolean[league.length];
        while (count < teams) {
            while (true) {
                final int r = random.nextInt(league.length);
                if (!playing[r]) {
                    selected[count++] = league[r];
                    if (standingsFlag) System.out.println(selected[count - 1]);
                    playing[r] = true;
                    break;
                }
            }
        }

        return knockout(selected, games, 1, replay);
    }

    private static Club knockout(final Club[] selected, final int games, final int type, final boolean replay) {
        int count = selected.length;

        for (int round = 1; round <= Math.sqrt(selected.length); round++) {
            System.out.println();
            switch (count) {
                case 2: System.out.println("FINAL"); break;
                case 4: System.out.println("SEMI-FINALS"); break;
                case 8: System.out.println("QUARTER-FINALS"); break;
                default: System.out.println("ROUND OF " + count); break;
            }

            System.out.println();

            for (int team = 0; team < count / 2; team++) {
                if (knockoutFixture(selected[team], selected[count - team - 1], round < Math.sqrt(selected.length)
                        ? games : 1, type, replay && count >= 8, round == Math.sqrt(selected.length))) {
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

    private static boolean knockoutFixture(final Club first, final Club second, final int games,
                                           final int type, final boolean replay, final boolean neutral) {
        int firstGoals = 0;
        int secondGoals = 0;

        int result;
        if (neutral) FANS = 0;
        result = simulation(first, second, games == 1 && !replay, -1, -1, type);

        firstGoals += result / 100;
        secondGoals += result % 100;

        if (games == 2 || (replay && firstGoals == secondGoals)) {
            result = simulation(second, first, true, replay ? -1 : secondGoals, replay ? -1 : firstGoals, type);
            secondGoals += result / 100;
            firstGoals += result % 100;

            if (firstGoals == secondGoals) {
                if (result % 11 > firstGoals) firstGoals++;
                else secondGoals++;
            }
        }

        return secondGoals > firstGoals;
    }
}
