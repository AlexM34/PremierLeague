import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

class PremierLeague {
    // TODO: Add tests
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    private static final boolean userFlag = false;
    private static final boolean standingsFlag = false;
    private static final boolean playerStatsFlag = true;
    private static final boolean teamStatsFlag = false;
    static final boolean matchFlag = false;

    public static void main(final String[] args) {
//        Data.extractData();
        Data.buildSquads();
        Data.addDummies();
        IntStream.range(0, 10).forEach(year -> {
            Data.prepare(year);
            if (userFlag) pickTeam();
            final Map<Club[], int[][][]> draw = new HashMap<>();
            for (final Club[] league : Data.LEAGUES) draw.put(league, Draw.league(league.length));

            for (int team = 0; team < 6; team++) {
                for (int league = 0; league < Data.LEAGUES.length; league++) {
                    final Club squad = Data.LEAGUES[league][team];
                    System.out.println(squad.getBudget());
                    squad.getFootballers().forEach(footballer ->
                            System.out.println(footballer.getName() + ", " + footballer.getPosition() + ", " +
                                    footballer.getOverall() + ", " + footballer.getPotential() + ", " +
                                    footballer.getAge() + " => " + (int) footballer.getValue()));
                }
            }
            pause();

            for (int round = 0; round < 38; round ++) {
                for (final Club[] league : Data.LEAGUES) {
                    if (round < 34 || league.length > 18) play(league, draw.get(league), round);
                }
            }

            for (final Club[] league : Data.LEAGUES) {
                final Club nationalCupWinner = cup(league, 16,
                        league[0].getLeague().equals(Spain.LEAGUE) ? 2 : 1,
                        league[0].getLeague().equals(England.LEAGUE));
                System.out.println(nationalCupWinner.getName() + " win the National Cup!");
                nationalCupWinner.getGlory().addNationalCup();
                for (final Footballer footballer : nationalCupWinner.getFootballers()) {
                    footballer.getResume().getGlory().addNationalCup();
                }

                if (league[0].getLeague().equals(England.LEAGUE) || league[0].getLeague().equals(France.LEAGUE)) {
                    final Club leagueCupWinner = cup(league, 16, 1, false);
                    System.out.println(leagueCupWinner.getName() + " win the League Cup!");
                    leagueCupWinner.getGlory().addLeagueCup();
                    for (final Footballer footballer : leagueCupWinner.getFootballers()) {
                        footballer.getResume().getGlory().addLeagueCup();
                    }
                }

                if (playerStatsFlag) Printer.playerStats(league, 1);
            }

            Data.CHAMPIONS_LEAGUE = Printer.pickChampionsLeagueTeams();
            for (final Club team : Data.CHAMPIONS_LEAGUE) {
                team.getSeason().getChampionsLeague().setAlive(true);
            }
            final Club[] advanced = groups(Data.CHAMPIONS_LEAGUE);
            for (Club club : advanced) club.changeBudget(5f);
            final Club[] drawn = Draw.championsLeague(advanced);
            final Club championsLeagueWinner = knockout(drawn, 2, 2, false);
            System.out.println(championsLeagueWinner.getName() + " win the Champions League!");
            championsLeagueWinner.getGlory().addContinental();
            for (final Footballer footballer : championsLeagueWinner.getFootballers()) {
                footballer.getResume().getGlory().addContinental();
            }

            if (playerStatsFlag) Printer.playerStats(Data.CHAMPIONS_LEAGUE, 2);

            finish(year);
            PreSeason.progression();
            PreSeason.transfers();
        });

        finishSimulation();
    }

    private static Club[] groups(final Club[] teams) {
        final Club[] advancing = new Club[2 * teams.length / 4];
        final int groups = teams.length / 4;
        int count = 0;

        final int[][][] draw = Draw.league(4);
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
                    final int result = Match.simulation(clubs[home], clubs[away], false, -1, -1, 2);
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

            final Map<Club, Integer> sorted = standing.entrySet().stream().sorted(
                    Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                            LinkedHashMap::new));

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
            Data.USER = team - 1;
            break;
        }
    }

    static void pause() {
        System.out.println("Press any Key to continue");
        try {
            System.in.read();
        } catch (final IOException e) {
            System.out.println("Exception thrown!");
        }
    }

    private static void play(final Club[] league, final int[][][] draw, final int round) {
//        pause();
        System.out.println(String.format("Round %d", round + 1));
        for (int game = 0; game < league.length / 2; game++) {
            final int home = draw[round][game][0];
            final int away = draw[round][game][1];
            if (home == Data.USER) Match.userTactics(league[away], true);
            else if (away == Data.USER) Match.userTactics(league[home], false);
            Match.simulation(league[home], league[away], false, -1, -1, 0);
            if (home == Data.USER || away == Data.USER) pause();
        }

        if (round < 2 * league.length - 3 && standingsFlag) {
            System.out.println();
            System.out.println(String.format("Standings after round %d:", round + 1));
            Printer.standings(league);
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
        Printer.knockoutPrizes(selected, type == 2);

        return selected[0];
    }

    private static boolean knockoutFixture(final Club first, final Club second, final int games,
                                           final int type, final boolean replay, final boolean neutral) {
        int firstGoals = 0;
        int secondGoals = 0;

        int result;
        if (neutral) Data.FANS = 0;
        result = Match.simulation(first, second, games == 1 && !replay, -1, -1, type);

        firstGoals += result / 100;
        secondGoals += result % 100;

        if (games == 2 || (replay && firstGoals == secondGoals)) {
            result = Match.simulation(second, first, true, replay ? -1 : secondGoals, replay ? -1 : firstGoals, type);
            secondGoals += result / 100;
            firstGoals += result % 100;

            if (firstGoals == secondGoals) {
                if (result % 11 > firstGoals) firstGoals++;
                else secondGoals++;
            }
        }

        return secondGoals > firstGoals;
    }

    private static void finish(final int year) {
        for (final Club[] league : Data.LEAGUES) {
            if (playerStatsFlag) Printer.playerStats(league, 0);
            if (standingsFlag) {
                System.out.println("FINAL STANDINGS");
                Printer.standings(league);
            }
            if (teamStatsFlag) Printer.allTimeStats(league);
            System.out.println();
            PreSeason.profits(league);
            // TODO: Rate simulation with review
        }

        if (teamStatsFlag) Printer.continentalStats();
        if (playerStatsFlag) {
            System.out.println();
            System.out.println("DUMMIES");
            System.out.println(Data.GOALKEEPER_1.getResume().getSeason());
            System.out.println(Data.DEFENDER_1.getResume().getSeason());
            System.out.println(Data.MIDFIELDER_1.getResume().getSeason());
            System.out.println(Data.FORWARD_1.getResume().getSeason());
            System.out.println(Data.DEFENDER_2.getResume().getSeason());
            System.out.println(Data.MIDFIELDER_2.getResume().getSeason());
            System.out.println(Data.FORWARD_2.getResume().getSeason());
        }

        System.out.println();
        Printer.pickTeam(Printer.topTeam, false);
        Printer.topTeam = new HashMap<>();
        Printer.voting(Rater.contenders);
        System.out.println(String.format("Season %d-%d ends!", 2019 + year, 2020 + year));
    }

    private static void finishSimulation() {
        Data.prepare(10);
        for (final Club[] league : Data.LEAGUES) Printer.allTimePlayerStats(league);
    }
}
