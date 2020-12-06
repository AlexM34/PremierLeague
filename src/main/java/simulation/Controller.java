package simulation;

import static simulation.Data.LEAGUES;
import static simulation.Data.addDummies;
import static simulation.Data.buildSquads;
import static simulation.competition.Knockout.championsLeague;
import static simulation.competition.Knockout.europaLeague;
import static simulation.competition.Knockout.leagueCupRound;
import static simulation.competition.Knockout.nationalCupRound;
import static simulation.competition.Knockout.setupCups;
import static simulation.competition.League.allLeagues;
import static simulation.competition.League.groupRound;
import static simulation.competition.League.setupLeagues;
import static simulation.dynamics.Postseason.announceCupWinners;
import static simulation.dynamics.Postseason.standings;
import static simulation.dynamics.Preseason.prepare;
import static simulation.dynamics.Preseason.progression;
import static simulation.dynamics.Transfer.transfers;

import team.Club;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Controller {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<Integer> MATCHDAYS = Arrays.asList(-1, 0, 0, 0, 31, 41, 0, 0, 31, 41, 0, 0, 0, 31,
            41, 2, 0, 0, 1, 31, 41, 0, 0, 2, 0, 31, 41, 0, 0, 0, 31, 41, 0, 1, 0, 2, 0, 42, 0, 0, 32, 42, 0, 1,
            0, 0, 0, 32, 42, 0, 2, 0, 0, 42, 0, 0, 32, 0, 0, 1, 0, 0, 0, 0, 0, 0, 42, 32, -2);

    private static int year = 0;
    private static int matchday = 0;

    public static void main(final String[] args) {
        initialise();
        while (SCANNER.nextInt() != 0) {
            for (int i = 0; i < getTotalMatchdays(); i++) Controller.proceed();
        }
    }

    public static void initialise() {
        buildSquads();
        addDummies();
    }

    public static void proceed() {
        switch (MATCHDAYS.get(matchday)) {
            case -2:
                completeSeason();
                break;
            case -1:
                setupSeason();
                break;
            case 0:
                allLeagues();
                break;
            case 1:
                nationalCupRound();
                break;
            case 2:
                leagueCupRound();
                break;
            case 31:
                groupRound(true);
                break;
            case 32:
                championsLeague();
                break;
            case 41:
                groupRound(false);
                break;
            case 42:
                europaLeague();
        }

        matchday++;
    }

    public static int getMatchday() {
        return matchday;
    }

    public static int getTotalMatchdays() {
        return MATCHDAYS.size();
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
