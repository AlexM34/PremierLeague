import java.util.Random;

class PreSeason {
    private static Random random = new Random();

    static void changes() {
        // TODO: Age updates
        // TODO: Transfers
        for (int team = 0; team < 20; team++) {
            for (Footballer f : Data.SQUADS.get(Data.TEAMS[team])) {
                int r = random.nextInt(5);
                if (r == 0) {
                    if (f.getResume().getSeason().getRating() > 700) {
                        increase(team, f);
                    } else if (f.getResume().getSeason().getRating() < 600) {
                        decrease(team, f);
                    }
                }
                r = random.nextInt(6);
                if (r == 0) {
                    increase(team, f);
                }
                else if (r == 1){
                    decrease(team, f);
                }
            }
        }
    }

    private static void increase(int team, Footballer footballer) {
//        Data.OVERALL[team][player] = Data.OVERALL[team][player] < 10 ? Data.OVERALL[team][player] + 1 : 10;
//        System.out.println(String.format("%s improves", Data.PLAYERS[team][player]));
    }

    private static void decrease(int team, Footballer footballer) {
//        Data.OVERALL[team][player] = Data.OVERALL[team][player] > 1 ? Data.OVERALL[team][player] - 1 : 1;
//        System.out.println(String.format("%s worsens", Data.PLAYERS[team][player]));
    }
}
