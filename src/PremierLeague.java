import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

class PremierLeague {
    private static Random random = new Random();
    private static Scanner scanner = new Scanner(System.in);
    // TODO: Add tests
    // TODO: Betting
    // TODO: Put final fields

    public static void main(String[] args) {
//        Data.extractData();
        Data.buildSquads();
        Data.addDummies();
        IntStream.range(0, 10).forEach(year -> {
            Data.prepare(year);
//            pickTeam();
            Map<Club[], int[][][]> draw = new HashMap<>();
            for (Club[] league : Data.LEAGUES) draw.put(league, Draw.league(league.length));

            for (int round = 0; round < 38; round ++) {
                for (Club[] league : Data.LEAGUES) {
                    if (round < 34 || league.length > 18) play(league, draw.get(league), round);
                }
            }

            for (Club[] league : Data.LEAGUES) {
                Club nationalCupWinner = cup(league, 16);
                System.out.println(nationalCupWinner.getName() + " win the National Cup!");
                nationalCupWinner.getGlory().addNationalCup();
                for (Footballer footballer : nationalCupWinner.getFootballers()) {
                    footballer.getResume().getGlory().addNationalCup();
                }

                if (league[0].getLeague().equals(England.LEAGUE) || league[0].getLeague().equals(France.LEAGUE)) {
                    Club leagueCupWinner = cup(league, 16);
                    System.out.println(leagueCupWinner.getName() + " win the League Cup!");
                    leagueCupWinner.getGlory().addLeagueCup();
                    for (Footballer footballer : leagueCupWinner.getFootballers()) {
                        footballer.getResume().getGlory().addLeagueCup();
                    }
                }
            }

            Data.CHAMPIONS_LEAGUE = Printer.pickChampionsLeagueTeams();
            for (Club team : Data.CHAMPIONS_LEAGUE) {
                team.getSeason().getChampionsLeague().setAlive(true);
            }
            Club[] advanced = groups(Data.CHAMPIONS_LEAGUE, 4, 2);
            Club[] drawn = Draw.championsLeague(advanced);
            Club championsLeagueWinner = knockout(drawn, 2);
            System.out.println(championsLeagueWinner.getName() + " win the Champions League!");
            championsLeagueWinner.getGlory().addContinental();
            for (Footballer footballer : championsLeagueWinner.getFootballers()) {
                footballer.getResume().getGlory().addContinental();
            }

            finish(year);
            PreSeason.changes();
        });

        finishSimulation();
    }

    private static Club[] groups(Club[] teams, int groupSize, int advancingPerGroup) {
        Club[] advancing = new Club[2 * teams.length / groupSize];
        int groups = teams.length / groupSize;
        int count = 0;

        int[][][] draw = Draw.league(groupSize);
        for (int group = 0; group < groups; group++) {
            System.out.println();
            System.out.println("GROUP " + (char)('A' + group));
            System.out.println();

            Club[] clubs = new Club[groupSize];
            for (int team = 0; team < groupSize; team++) clubs[team] = teams[groups * team + group];

            for (int round = 0; round < draw.length; round++) {
                System.out.println();
                System.out.println("Matchday " + (round + 1));
                System.out.println();

                for (int game = 0; game < groupSize / 2; game++) {
                    int home = draw[round][game][0];
                    int away = draw[round][game][1];
                    int result = Match.cupSimulation(clubs[home], clubs[away], false, -1, -1);
                    int homeGoals = result / 100;
                    int awayGoals = result % 100;

                    System.out.println(String.format("%s - %s %d:%d", clubs[home].getName(),
                            clubs[away].getName(), homeGoals, awayGoals));

                    if (result / 100 > result % 100) {
                        clubs[home].getSeason().getChampionsLeague().getGroup().addPoints(3);
                        clubs[home].getSeason().getChampionsLeague().getGroup().addWin();
                        clubs[away].getSeason().getChampionsLeague().getGroup().addLoss();
                    }
                    else if (result / 100 < result % 100) {
                        clubs[away].getSeason().getChampionsLeague().getGroup().addPoints(3);
                        clubs[away].getSeason().getChampionsLeague().getGroup().addWin();
                        clubs[home].getSeason().getChampionsLeague().getGroup().addLoss();
                    }
                    else {
                        clubs[home].getSeason().getChampionsLeague().getGroup().addPoints(1);
                        clubs[away].getSeason().getChampionsLeague().getGroup().addPoints(1);
                        clubs[home].getSeason().getChampionsLeague().getGroup().addDraw();
                        clubs[away].getSeason().getChampionsLeague().getGroup().addDraw();
                    }

                    if (awayGoals == 0) clubs[home].getSeason().getChampionsLeague().getGroup().addCleanSheet();
                    if (homeGoals == 0) clubs[away].getSeason().getChampionsLeague().getGroup().addCleanSheet();

                    clubs[home].getSeason().getChampionsLeague().getGroup().addMatch();
                    clubs[away].getSeason().getChampionsLeague().getGroup().addMatch();
                    clubs[home].getSeason().getChampionsLeague().getGroup().addScored(homeGoals);
                    clubs[away].getSeason().getChampionsLeague().getGroup().addScored(awayGoals);
                    clubs[home].getSeason().getChampionsLeague().getGroup().addConceded(awayGoals);
                    clubs[away].getSeason().getChampionsLeague().getGroup().addConceded(homeGoals);
                }
            }

            Map<Club, Integer> standing = new LinkedHashMap<>();
            for (int team = 0; team < groupSize; team++) standing.put(clubs[team],
                    10000 * clubs[team].getSeason().getChampionsLeague().getGroup().getPoints() +
                    100 * (clubs[team].getSeason().getChampionsLeague().getGroup().getScored() -
                           clubs[team].getSeason().getChampionsLeague().getGroup().getConceded()) +
                    clubs[team].getSeason().getChampionsLeague().getGroup().getScored());

            Map<Club, Integer> sorted = standing.entrySet().stream().sorted(
                    Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                            LinkedHashMap::new));

            System.out.println();
            System.out.println("Standings for group " + (char)('A' + group));
            System.out.println("No  Teams                     G  W  D  L  GF:GA  P");
            Club[] rankedTeams = sorted.keySet().toArray(new Club[0]);
            for (int team = 0; team < sorted.size(); team++) {
                Club club = rankedTeams[team];
                League groupStats = club.getSeason().getChampionsLeague().getGroup();

                System.out.println(String.format("%2d. %-25s %-2d %-2d %-2d %-2d %2d:%-2d %2d", team + 1, club.getName(),
                        groupStats.getMatches(), groupStats.getWins(), groupStats.getDraws(), groupStats.getLosses(),
                        groupStats.getScored(), groupStats.getConceded(), groupStats.getPoints()));

                if (team < advancingPerGroup) advancing[count++] = club;
            }

            System.out.println();
        }

        return advancing;
    }

    private static void pickTeam() {
        System.out.println("Pick a team from 1 to 20");
        while (true) {
            int team = scanner.nextInt();
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
        } catch (IOException e) {
            System.out.println("Exception thrown!");
        }
    }

    private static void play(Club[] league, int[][][] draw, int round) {
//        pause();
        System.out.println(String.format("Round %d", round + 1));
        for (int game = 0; game < league.length / 2; game++) {
            int home = draw[round][game][0];
            int away = draw[round][game][1];
            if (home == Data.USER) Match.userTactics(league[away], true);
            else if (away == Data.USER) Match.userTactics(league[home], false);
            Match.leagueSimulation(league[home], league[away]);
            if (home == Data.USER || away == Data.USER) pause();
        }

        System.out.println();
        System.out.println(String.format("Standings after round %d:", round + 1));
        Printer.printStandings(league);
        System.out.println();
    }

    private static Club cup(Club[] league, int teams) {
        int count = 0;
        Club[] selected = new Club[teams];
        boolean[] playing = new boolean[league.length];
        while (count < teams) {
            while (true) {
                int r = random.nextInt(league.length);
                if (!playing[r]) {
                    selected[count++] = league[r];
                    System.out.println(selected[count - 1]);
                    playing[r] = true;
                    break;
                }
            }
        }

        return knockout(selected, 1);
    }

    private static Club knockout(Club[] selected, int games) {
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
                        ? games : 1)) {
                    selected[team] = selected[count - team - 1];
                    selected[count - team - 1] = null;
                }
            }

            count /= 2;
        }

        return selected[0];
    }

    private static boolean knockoutFixture(Club first, Club second, int games) {
        // TODO: Replay for FA Cup
        int firstGoals = 0;
        int secondGoals = 0;
        for (int game = 0; game < games; game++) {
            boolean last = game + 1 == games;
            if (game % 2 == 0) {
                int result = Match.cupSimulation(first, second, last, -1, -1);
                firstGoals += result / 100;
                secondGoals += result % 100;
                System.out.println(String.format("%s - %s %d:%d", first.getName(),
                        second.getName(), result / 100, result % 100));
            }
            else {
                int result = Match.cupSimulation(second, first, last, secondGoals, firstGoals);
                secondGoals += result / 100;
                firstGoals += result % 100;

                if (game == 1 && games == 2 && firstGoals == secondGoals) {
                    if (result % 11 > firstGoals) firstGoals++;
                    else secondGoals++;
                }

                System.out.println(String.format("%s - %s %d:%d", second.getName(),
                        first.getName(), result / 100, result % 100));
            }
        }

        return secondGoals > firstGoals;
    }

    private static void finish(int year) {
        for (Club[] league : Data.LEAGUES) {
            Printer.printPlayerStats(league);
            System.out.println("FINAL STANDINGS");
            Printer.printStandings(league);
            Printer.printAllTimeStats(league);
            System.out.println();
            // TODO: Rate simulation with review
            System.out.println(String.format("Season %d-%d ends!", 2019 + year, 2020 + year));
        }

        Printer.printChampionsLeagueStats();
        System.out.println("DUMMIES");
        System.out.println(Data.DEFENDER_1.getResume().getSeason());
        System.out.println(Data.MIDFIELDER_1.getResume().getSeason());
        System.out.println(Data.FORWARD_1.getResume().getSeason());
        System.out.println(Data.DEFENDER_2.getResume().getSeason());
        System.out.println(Data.MIDFIELDER_2.getResume().getSeason());
        System.out.println(Data.FORWARD_2.getResume().getSeason());
    }

    private static void finishSimulation() {
        Data.prepare(10);
        for (Club[] league : Data.LEAGUES) Printer.printAllTimePlayerStats(league);
    }
}
