package simulation.competition;

import simulation.Simulator;
import team.Club;

class Draw {
    static int[][][] league(final int teams) {
        final int[][][] schedule = new int[teams * 2 - 2][teams / 2][2];
        final int[] draw = new int[teams];
        final boolean[] drawn = new boolean[teams];
        for (int team = 0; team < teams; team++) {
            int r = Simulator.getInt(teams - team);
            int current = 0;

            while (true) {
                if (!drawn[current]) {
                    if (r-- == 0) {
                        drawn[current] = true;
                        draw[team] = current;
                        break;
                    }
                }

                current++;
            }
        }

        for (int round = 0; round < teams - 1; round++) {
            final int[] current = new int[teams];
            current[0] = 0;
            for (int i = 1; i < teams; i++) {
                current[i] = (i + round - 1) % (teams - 1) + 1;
            }

            for (int game = 0; game < teams / 2; game++) {
                final int reverse = teams - 1 + round;
                final int team1 = draw[current[game]];
                final int team2 = draw[current[teams - 1 - game]];

                if (round % 2 == 0) {
                    schedule[round][game][0] = team1;
                    schedule[round][game][1] = team2;
                    schedule[reverse][game][0] = team2;
                    schedule[reverse][game][1] = team1;
                }
                else{
                    schedule[round][game][0] = team2;
                    schedule[round][game][1] = team1;
                    schedule[reverse][game][0] = team1;
                    schedule[reverse][game][1] = team2;
                }
            }
        }

        return schedule;
    }

    static Club[] seededKnockout(final Club[] advancing) {
        final int teams = advancing.length;
        final Club[] draw = new Club[teams];
        final boolean[] drawn = new boolean[teams];
        for (int team = 0; team < teams; team++) {
            int r = Simulator.getInt(teams / 2 - team / 2);
            int current = team % 2 == 0 ? 8 : 0;

            while (true) {
                if (!drawn[current]) {
                    if (r-- == 0) {
                        drawn[current] = true;
                        draw[current] = advancing[team];
                        break;
                    }
                }

                current++;
            }
        }

        return draw;
    }
}
