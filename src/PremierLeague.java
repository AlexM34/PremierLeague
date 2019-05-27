import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

class PremierLeague {
    private static Random random = new Random();
    private static Scanner scanner = new Scanner(System.in);
    // TODO: Add tests
    // TODO: User bets

    public static void main(String[] args) {
//        Data.extractData();
        Data.buildSquads();
        Data.addDummies();
        IntStream.range(0, 10).forEach(year -> {
            Data.prepare(year);
//            pickTeam();
            Map<Club[], int[][][]> draw = new HashMap<>();
            for (Club[] league : Data.LEAGUES) draw.put(league, Draw.makeDraw(league.length));

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

            Club[] advanced = groups(Printer.pickChampionsLeagueTeams(), 4, 2);
            Club championsLeagueWinner = knockout(advanced);
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

    private static Club[] groups(Club[] teams, int groupSize, int games) {
        // TODO: Teams progressing
        // TODO: Draw
        Club[] advancing = new Club[2 * teams.length / groupSize];
        int count = 0;

        for (int group = 0; group < teams.length / groupSize; group++) {
            System.out.println();
            System.out.println("GROUP " + (char)('A' + group));
            System.out.println();

            Club[] clubs = new Club[groupSize];
            int[] points = new int[groupSize];
            for (int team = 0; team < groupSize; team++) clubs[team] = teams[group * groupSize + team];

            // TODO: Make it right
            for (int first = 0; first < groupSize; first++) {
                for (int second = first + 1; second < groupSize; second++) {
                    for (int game = 0; game < games; game++) {
                        if (game % 2 == 0) {
                            int result = Match.cupSimulation(clubs[first], clubs[second]);
                            System.out.println(String.format("%s - %s %d:%d", clubs[first].getName(),
                                    clubs[second].getName(), result / 100, result % 100));

                            if (result / 100 > result % 100) {
                                points[first] += 3;
                            }
                            else if (result / 100 < result % 100) {
                                points[second] += 3;
                            }
                            else {
                                points[first]++;
                                points[second]++;
                            }
                        }
                        else {
                            int result = Match.cupSimulation(clubs[second], clubs[first]);
                            System.out.println(String.format("%s - %s %d:%d", clubs[second].getName(),
                                    clubs[first].getName(), result / 100, result % 100));

                            if (result / 100 > result % 100) {
                                points[second] += 3;
                            }
                            else if (result / 100 < result % 100) {
                                points[first] += 3;
                            }
                            else {
                                points[second]++;
                                points[first]++;
                            }
                        }
                    }
                }
            }

            Map<Club, Integer> standing = new LinkedHashMap<>();
            for (int team = 0; team < groupSize; team++) standing.put(clubs[team], points[team]);

            Map<Club, Integer> sorted = standing.entrySet().stream().sorted(
                    Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                            LinkedHashMap::new));

            System.out.println();
            System.out.println("Standings for group " + (char)('A' + group));
            for (int team = 0; team < sorted.size(); team++) {
                Club club = (Club) sorted.keySet().toArray()[team];

                System.out.println(String.format("%2d. %-25s %d", team + 1, club.getName(),
                        sorted.values().toArray()[team]));

                if (team < 2) advancing[count++] = clubs[team];
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
        // TODO: Double-legger
        // TODO: Replay
        // TODO: Extra time and penalties
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

        return knockout(selected);
    }

    private static Club knockout(Club[] selected) {
        int count = selected.length;

        for (int round = 1; round <= Math.sqrt(selected.length); round++) {
            System.out.println();
            if (count == 4) System.out.println("Semi-finals");
            else if (count == 2) System.out.println("Final");
            else System.out.println("Round " + round);
            System.out.println();

            for (int team = 0; team < count / 2; team++) {
                // TODO: Cup stats should be separate
                int result = Match.cupSimulation(selected[team], selected[count - team - 1]);
                System.out.println(String.format("%s - %s %d:%d", selected[team].getName(),
                        selected[count - team - 1].getName(), result / 100, result % 100));

                if (result / 100 <= result % 100) {
                    selected[team] = selected[count - team - 1];
                }
            }

            count /= 2;
        }

        return selected[0];
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
