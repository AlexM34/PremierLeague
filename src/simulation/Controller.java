package simulation;

import team.Club;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static simulation.Data.LEAGUES;
import static simulation.Data.addDummies;
import static simulation.Data.buildSquads;
import static simulation.Data.prepare;
import static simulation.Knockout.announceCupWinners;
import static simulation.Knockout.championsLeague;
import static simulation.Knockout.europaLeague;
import static simulation.Knockout.leagueCup;
import static simulation.Knockout.nationalCup;
import static simulation.Knockout.setupCups;
import static simulation.League.allLeagues;
import static simulation.League.groupRound;
import static simulation.League.setupLeagues;
import static simulation.Preseason.progression;
import static simulation.Printer.standings;
import static simulation.Transfer.transfers;

public class Controller {
    private static final Scanner scanner = new Scanner(System.in);

    private static int year = 0;
    static int matchday = 0;

    public static final List<Integer> MATCHDAYS = Arrays.asList(-1, 0, 0, 0, 31, 41, 0, 0, 31, 41, 0, 0, 0, 31,
            41, 2, 0, 0, 1, 31, 41, 0, 0, 2, 0, 31, 41, 0, 0, 0, 31, 41, 0, 1, 0, 2, 0, 42, 0, 0, 32, 42, 0, 1,
            0, 0, 0, 32, 42, 0, 2, 0, 0, 42, 0, 0, 32, 0, 0, 1, 0, 0, 0, 0, 0, 0, 42, 32, -2);

    public static void main(final String[] args) {
        initialise();
        while(scanner.nextInt() != 0) {
            for (int i = 0; i < MATCHDAYS.size(); i++) Controller.proceed();
        }
    }

    public static void initialise() {
        buildSquads();
        addDummies();
    }

    public static void proceed() {
        switch (MATCHDAYS.get(matchday)) {
            case -2: completeSeason(); break;
            case -1: setupSeason(); break;
            case 0: allLeagues(); break;
            case 1: nationalCup(); break;
            case 2: leagueCup(); break;
            case 31: groupRound(true); break;
            case 32: championsLeague(); break;
            case 41: groupRound(false); break;
            case 42: europaLeague(); break;
        }

        matchday++;
    }

    private static void setupSeason() {
        prepare(year);
        setupLeagues();
        setupCups();
    }

    private static void completeSeason() {
        for (final Club[] league : LEAGUES) standings(league);
        announceCupWinners();

        progression();
        transfers();
        matchday = -1;
        year++;
    }
}
