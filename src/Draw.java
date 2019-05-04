import java.util.Random;

class Draw {
    private static Random random = new Random();

    static void makeDraw() {
        int[] draw = new int[20];
        boolean[] drawn = new boolean[20];
        for (int team = 0; team < 20; team++) {
            int r = random.nextInt(20 - team);
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

        for (int round = 0; round < 19; round++) {
            int[] current = new int[20];
            current[0] = 0;
            for (int i = 1; i < 20; i++) {
                current[i] = (i + round - 1) % 19 + 1;
            }

            for (int game = 0; game < 10; game++) {
                if (round % 2 == 0) {
                    Data.HOME[round][game] = draw[current[game]];
                    Data.AWAY[round][game] = draw[current[19 - game]];
                    Data.HOME[19 + round][game] = draw[current[19 - game]];
                    Data.AWAY[19 + round][game] = draw[current[game]];
                }
                else{
                    Data.HOME[round][game] = draw[current[19 - game]];
                    Data.AWAY[round][game] = draw[current[game]];
                    Data.HOME[19 + round][game] = draw[current[game]];
                    Data.AWAY[19 + round][game] = draw[current[19 - game]];
                }
            }
        }
    }
}
