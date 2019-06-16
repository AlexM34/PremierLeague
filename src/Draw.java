import java.util.Random;

class Draw {
    private static Random random = new Random();

    static int[][][] league(int teams) {
        int[][][] schedule = new int[teams * 2 - 2][teams / 2][2];
        int[] draw = new int[teams];
        boolean[] drawn = new boolean[teams];
        for (int team = 0; team < teams; team++) {
            int r = random.nextInt(teams - team);
            int current = 0;

            while (true) {
                if (!drawn[current]) {
                    if (r == 0) {
                        drawn[current] = true;
                        draw[team] = current;
                        break;
                    }

                    r--;
                }

                current++;
            }
        }

        for (int round = 0; round < teams - 1; round++) {
            int[] current = new int[teams];
            current[0] = 0;
            for (int i = 1; i < teams; i++) {
                current[i] = (i + round - 1) % (teams - 1) + 1;
            }

            for (int game = 0; game < teams / 2; game++) {
                if (round % 2 == 0) {
                    schedule[round][game][0] = draw[current[game]];
                    schedule[round][game][1] = draw[current[teams - 1 - game]];
                    schedule[teams - 1 + round][game][0] = draw[current[teams - 1 - game]];
                    schedule[teams - 1 + round][game][1] = draw[current[game]];
                }
                else{
                    schedule[round][game][0] = draw[current[teams - 1 - game]];
                    schedule[round][game][1] = draw[current[game]];
                    schedule[teams - 1 + round][game][0] = draw[current[game]];
                    schedule[teams - 1 + round][game][1] = draw[current[teams - 1 - game]];
                }
            }
        }

        return schedule;
    }

    static Club[] championsLeague(Club[] advanced) {
        int teams = advanced.length;
        Club[] draw = new Club[teams];
        boolean[] drawn = new boolean[teams];
        for (int team = 0; team < teams; team++) {
//            System.out.println(team + advanced[team].getName());
            int r = random.nextInt(teams / 2 - team / 2);
            int current = team % 2 == 0 ? 8 : 0;

            while (true) {
                if (!drawn[current]) {
                    if (r == 0) {
                        drawn[current] = true;
                        draw[current] = advanced[team];
//                        System.out.println("Draws " + current);
                        break;
                    }

                    r--;
                }

                current++;
            }
        }

        return draw;
    }
}
