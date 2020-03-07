package main.simulation;

import java.util.Scanner;

import static main.simulation.Data.USER;

public class User {
    private static final Scanner SCANNER = new Scanner(System.in);

    private static void pickTeam() {
        System.out.println("Pick a main.team from 1 to 20");
        while (true) {
            final int team = SCANNER.nextInt();
            if (team < 1 || team > 20) {
                System.out.println("Wrong number");
                continue;
            }

            USER = team - 1;
            break;
        }
    }
}
