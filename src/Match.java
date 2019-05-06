import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.LongStream;

class Match {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    static int motmTeam;
    static int motmPlayer;
    static float motmRating;
    static Footballer[] homeSquad;
    static Footballer[] awaySquad;

    static void userTactics(int opponent, boolean isHome) {
        // TODO: Add other choices
        System.out.println("vs " + Data.TEAMS[opponent] + (isHome ? " Home" : " Away"));
        System.out.println("Pick how offensive the team should be from 0 to 20");
        while (true) {
            int offense = scanner.nextInt();
            if(offense < 0 || offense > 20) {
                System.out.println("Wrong offense number.");
                continue;
            }
            Data.OFFENSE = offense - 10;
            break;
        }

    }

    static void simulateGame(int home, int away) {
        // TODO: Match odds
        // TODO: Minute-by-minute simulation
        kickoff();
        homeSquad = pickSquad(home);
        awaySquad = pickSquad(away);
        int homePerf = 50;
        int awayPerf = 50;
        int homeAttack = 50;
        int homeDefense = 50;
        int awayAttack = 50;
        int awayDefense = 50;

        int goalsHome = 0;//calculateGoals(home, away, true);
        int goalsAway = 0;//calculateGoals(away, home, false);
        for (int minute = 1; minute <= 90; minute++) {
            // TODO: Add stoppage time

        }

        finalWhistle(home, away, goalsHome, goalsAway);
    }

    private static Footballer[] pickSquad(int team) {
        // TODO: Pick formation
        int goalkeeper = 1;
        int defenders = 4;
        int midfielders = 3;
        int forwards = 3;
        Footballer[] selected = new Footballer[11];
        List<Footballer> squad = Data.SQUADS.get(Data.TEAMS[team]);

        for (Footballer footballer : squad) {
            if (footballer.getPosition() == null) {
                continue;
            }

            switch (footballer.getPosition().getRole()) {
                case Goalkeeper:
                    if (goalkeeper > 0) {
                        goalkeeper--;
                        selected[goalkeeper] = footballer;
                    }
                    break;

                case Defender:
                    if (defenders > 0) {
                        defenders--;
                        selected[1 + defenders] = footballer;
                    }
                    break;

                case Midfielder:
                    if (midfielders > 0) {
                        midfielders--;
                        selected[5 + midfielders] = footballer;
                    }
                    break;

                case Forward:
                    if (forwards > 0) {
                        forwards--;
                        selected[8 + forwards] = footballer;
                    }
                    break;

                default:
                    System.out.println("Unknown position for footballer " + footballer);
                    break;
            }
        }

        Arrays.stream(selected).forEach(System.out::println);
        return selected;
    }

    private static void kickoff() {
        motmTeam = 0;
        motmPlayer = 0;
        motmRating = 0;
    }

    private static void finalWhistle(int home, int away, int goalsHome, int goalsAway) {
        int ratingHomeAttack = goalsHome;
        int ratingHomeDefence = 3 - goalsAway;
        int ratingAwayAttack = goalsAway;
        int ratingAwayDefence = 3 - goalsHome;

        if (goalsHome > 2) {
            ratingHomeAttack++;
            ratingAwayDefence--;
        }

        if (goalsAway == 0) {
            ratingHomeDefence++;
            ratingAwayAttack--;
            Data.CLEAN_SHEETS[home]++;
        }

        if (goalsAway > 2) {
            ratingAwayAttack++;
            ratingHomeDefence--;
        }

        if (goalsHome == 0) {
            ratingAwayDefence++;
            ratingHomeAttack--;
            Data.CLEAN_SHEETS[away]++;
        }

        if (goalsHome > goalsAway) {
            Data.POINTS[home] += 3;
            Data.WINS[home]++;
            Data.LOSES[away]++;
            ratingHomeAttack++;
            ratingHomeDefence++;
            ratingAwayAttack--;
            ratingAwayDefence--;

            if (goalsHome - goalsAway > 2) {
                form(home, 1);
                form(away, -1);
            }

            if (Data.FORM[home] < Data.FORM[away] + 10) {
                if (Data.FORM[home] < Data.FORM[away]) {
                    form(home, 3);
                    form(away, -3);
                }
                else {
                    form(home, 1);
                    form(away, -1);
                }
            }
        }

        else if (goalsHome < goalsAway){
            Data.POINTS[away] += 3;
            Data.WINS[away]++;
            Data.LOSES[home]++;
            ratingAwayAttack++;
            ratingAwayDefence++;
            ratingHomeAttack--;
            ratingHomeDefence--;

            if (goalsAway - goalsHome > 2) {
                form(away, 1);
                form(home, -1);
            }

            if (Data.FORM[away] < Data.FORM[home] + 10) {
                if (Data.FORM[away] < Data.FORM[home]) {
                    form(away, 3);
                    form(home, -3);
                }
                else {
                    form(away, 1);
                    form(home, -1);
                }
            }
        }
        else {
            Data.POINTS[home]++;
            Data.POINTS[away]++;
            Data.DRAWS[home]++;
            Data.DRAWS[away]++;

            if (Data.FORM[home] < Data.FORM[away]) {
                form(home, 2);
                form(away, -2);
            }
            else if (Data.FORM[away] < Data.FORM[home]) {
                form(away, 2);
                form(home, -2);
            }
        }

        Data.GAMES[home]++;
        Data.GAMES[away]++;
        Data.GOALS_FOR[home] += goalsHome;
        Data.GOALS_AGAINST[home] += goalsAway;
        Data.GOALS_FOR[away] += goalsAway;
        Data.GOALS_AGAINST[away] += goalsHome;

        Rater.ratePlayers(home, ratingHomeAttack, ratingHomeDefence, goalsHome, goalsAway == 0);
        Rater.ratePlayers(away, ratingAwayAttack, ratingAwayDefence, goalsAway, goalsHome == 0);

        Data.MOTM[motmTeam][motmPlayer]++;

        System.out.println(String.format("%s - %s %d:%d   --- %s %.2f", Data.TEAMS[home], Data.TEAMS[away], goalsHome,
                goalsAway, Data.PLAYERS[motmTeam][motmPlayer], motmRating));
    }

    private static void form(int team, int change) {
        if (change > 0) {
            Data.FORM[team] = Data.FORM[team] < 21 - change ? Data.FORM[team] + change : 20;
        }
        else {
            Data.FORM[team] = Data.FORM[team] > 1 - change ? Data.FORM[team] + change : 0;
        }
    }

    private static int calculateGoals(int attacking, int defending, boolean isAtHome) {
        int attackValue = isAtHome ? Data.FANS : 0;
        int defenceValue = isAtHome ? 0 : Data.FANS;
        for (int i = 0; i < 11; i++) {
            if (i < 6) {
                defenceValue += Data.OVERALL[defending][i];
            }
            else {
                attackValue += Data.OVERALL[attacking][i];
            }
        }

        if (attacking == Data.USER) {
            attackValue += Data.OFFENSE;
        }
        else if (defending == Data.USER) {
            defenceValue -= Data.OFFENSE * 3 / 2;
        }

        //System.out.println(FORM[attacking]);
        //System.out.println(FORM[defending]);
        int average = 30 + 8 * attackValue - 5 * defenceValue + 2 * Data.FORM[attacking];
        //System.out.println(average);
        return poisson(average);
    }

    private static int poisson(int average) {
        double e = 2.74;
        double lambda = (double) average / 100;
        double r = random.nextDouble();
        int goals = 0;
        while (r > 0 && goals < 6) {
            double current = Math.pow(e, -lambda) * Math.pow(lambda, goals) / factorial(goals);
            r -= current;
            goals++;
        }

        return goals-1;
    }

    private static double factorial(int n) {
        return LongStream.rangeClosed(1, n)
                .reduce(1, (long x, long y) -> x * y);
    }
}
