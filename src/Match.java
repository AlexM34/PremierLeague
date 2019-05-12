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
        kickoff();
        homeSquad = pickSquad(home);
        awaySquad = pickSquad(away);
        int balance = Data.FANS + 100 *
                (Arrays.stream(homeSquad).mapToInt(Footballer::getOverall).sum() - 500) /
                (Arrays.stream(awaySquad).mapToInt(Footballer::getOverall).sum() - 500) - 50;
        System.out.println(balance);

        int goalsHome = 0;
        int goalsAway = 0;
        for (int minute = 1; minute <= 90; minute++) {
            // TODO: Add stoppage time
            // TODO: Real-time ratings
            int r = random.nextInt(100);

            if (r < balance) {
                if (r < balance / 20) {
                    goal(minute, homeSquad);
                    goalsHome++;
                    balance -= 10;
                } else if (r < balance / 2) {
                    balance++;
                }
            } else {
                if (r > 100 - balance / 20) {
                    goal(minute, awaySquad);
                    goalsAway++;
                    balance += 10;
                } else if (r > 100 - balance / 2) {
                    balance--;
                }
            }

            System.out.println(balance);
        }

        finalWhistle(home, away, goalsHome, goalsAway);
    }

    private static void goal(int minute, Footballer[] squad) {
        int scoring = 20;
        int assisting = 150;
        Footballer goalscorer = null;
        Footballer assistmaker = null;
        for (int player = 0; player < 11; player++) {
            // TODO: Own goals
            scoring += scoringChance(squad[player]);
            assisting += assistingChance(squad[player]);
        }

        int r = random.nextInt(scoring);
        for (int player = 0; player < 11; player++) {
            r -= scoringChance(squad[player]);
            if (r < 0) {
                goalscorer = squad[player];
                break;
            }
        }

        r = random.nextInt(assisting);
        for (int player = 0; player < 11; player++) {
            r -= assistingChance(squad[player]);
            if (r < 0) {
                assistmaker = squad[player];
                if (assistmaker.equals(goalscorer)) assistmaker = null;
                break;
            }
        }

        // TODO: Fix message
        System.out.println(minute + "' " + (goalscorer != null ? goalscorer.getName() : "no one") +
                " scores after a pass from " + (assistmaker != null ? assistmaker.getName() : "none"));
    }

    private static int scoringChance(Footballer footballer) {
        switch (footballer.getPosition().getRole()) {
            case Goalkeeper:
                return 0;
            case Defender:
                return footballer.getFinishing() * 2;
            case Midfielder:
                return footballer.getFinishing() * 5;
            case Forward:
                return footballer.getFinishing() * 10;
        }

        return 0;
    }

    private static int assistingChance(Footballer footballer) {
        switch (footballer.getPosition().getRole()) {
            case Goalkeeper:
                return footballer.getVision();
            case Defender:
                return footballer.getVision() * 2;
            case Midfielder:
                return footballer.getVision() * 5;
            case Forward:
                return footballer.getVision() * 10;
        }

        return 0;
    }

    private static Footballer[] pickSquad(int team) {
        Formation formation = pickFormation(Data.SQUADS.get(Data.TEAMS[team]));
        int defenders = formation.getDefenders();
        int midfielders = formation.getMidfielders();
        int forwards = formation.getForwards();
        Footballer[] selected = new Footballer[11];
        List<Footballer> squad = Data.SQUADS.get(Data.TEAMS[team]);

        int g = 1;
        int d = defenders;
        int m = midfielders;
        int f = forwards;
        for (Footballer footballer : squad) {
            if (footballer.getPosition() == null) {
                continue;
            }

            switch (footballer.getPosition().getRole()) {
                case Goalkeeper:
                    if (g > 0) {
                        g--;
                        selected[g] = footballer;
                    }
                    break;

                case Defender:
                    if (d > 0) {
                        d--;
                        selected[1 + d] = footballer;
                    }
                    break;

                case Midfielder:
                    if (m > 0) {
                        m--;
                        selected[1 + defenders + m] = footballer;
                    }
                    break;

                case Forward:
                    if (f > 0) {
                        f--;
                        selected[1 + defenders + midfielders + f] = footballer;
                    }
                    break;

                default:
                    System.out.println("Unknown position for footballer " + footballer);
                    break;
            }
        }

//        Arrays.stream(selected).forEach(System.out::println);
        return selected;
    }

    private static Formation pickFormation(List<Footballer> footballers) {
        // TODO: Smart formation pick - opponent, fatigue, form
        int defenders = 0;
        int midfielders = 0;
        int forwards = 0;

        for (Footballer f : footballers) {
            switch (f.getPosition().getRole()) {
                case Defender:
                    defenders++;
                    break;
                case Midfielder:
                    midfielders++;
                    break;
                case Forward:
                    forwards++;
                    break;
            }

            if (defenders + midfielders + forwards > 9 &&
                defenders > 2 && midfielders > 1 && forwards > 0) {
                for (Formation formation : Formation.values()) {
                    if (formation.getDefenders() <= defenders &&
                        formation.getMidfielders() <= midfielders &&
                        formation.getForwards() <= forwards) {
                        return formation;
                    }
                }
            }
        }

        System.out.println("Could not pick an appropriate formation");
        return Formation.F5;
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
                goalsAway, Data.SQUADS.get(Data.TEAMS[motmTeam]).get(motmPlayer), motmRating));
    }

    private static void form(int team, int change) {
        if (change > 0) {
            Data.FORM[team] = Data.FORM[team] < 21 - change ? Data.FORM[team] + change : 20;
        }
        else {
            Data.FORM[team] = Data.FORM[team] > 1 - change ? Data.FORM[team] + change : 0;
        }
    }
}
