package simulation;

import competition.England;
import competition.France;
import competition.Spain;
import players.Footballer;
import team.Club;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static simulation.Data.LEAGUES;
import static simulation.Data.USER;
import static simulation.Data.addDummies;
import static simulation.Data.buildSquads;
import static simulation.Data.prepare;
import static simulation.Draw.league;
import static simulation.Finance.knockoutPrizes;
import static simulation.Finance.profits;
import static simulation.Finance.salaries;
import static simulation.Helper.cup;
import static simulation.Knockout.knockoutRound;
import static simulation.League.concludeGroups;
import static simulation.League.groupRound;
import static simulation.League.leagueRound;
import static simulation.Preseason.progression;
import static simulation.Printer.pickContinentalTeams;
import static simulation.Printer.standings;
import static simulation.Transfer.transfers;

public class Controller {
    private static final Scanner scanner = new Scanner(System.in);

    private static int year = 0;
    static int round = 0;

    public static final String CHAMPIONS_LEAGUE_NAME = "Champions League";
    public static final String EUROPA_LEAGUE_NAME = "Europa League";
    public static final Club[] CHAMPIONS_LEAGUE = new Club[32];
    public static final Club[] EUROPA_LEAGUE = new Club[48];

    private static final Map<String, int[][][]> leagueDraw = new HashMap<>();
    static final Map<String, int[][][]> continentalDraw = new HashMap<>();
    private static final Map<String, Club[]> leagueCup = new HashMap<>();
    private static final Map<String, Club[]> nationalCup = new HashMap<>();
    static final Map<String, Club[]> continentalCup = new HashMap<>();
    public static final Map<String, String> leagueResults = new HashMap<>();
    public static final Map<String, String> leagueCupResults = new HashMap<>();
    public static final Map<String, String> nationalCupResults = new HashMap<>();
    public static final Map<String, String> continentalCupResults = new HashMap<>();

    public static void main(final String[] args) {
        initialise();
        while(scanner.nextInt() != 0) {
            for (int i = 0; i < 38; i++) Controller.proceed();
        }
    }

    public static void initialise() {
        buildSquads();
        addDummies();
    }

    public static void proceed() {
        if (round == 0) {
            pickContinentalTeams(CHAMPIONS_LEAGUE, EUROPA_LEAGUE);
            prepare(year);

            leagueDraw.clear();
            continentalDraw.clear();
            leagueCup.clear();
            nationalCup.clear();
            continentalCup.clear();
            leagueResults.clear();
            leagueCupResults.clear();
            nationalCupResults.clear();
            continentalCupResults.clear();

            for (final Club[] league : LEAGUES) {
                final String leagueName = league[0].getLeague();
                leagueDraw.put(leagueName, league(league.length));
                nationalCup.put(leagueName, cup(league));
            }

            for (final Club[] league : new Club[][]{England.CLUBS, France.CLUBS}) {
                final String leagueName = league[0].getLeague();
                leagueCup.put(leagueName, cup(league));
            }

            continentalDraw.put(CHAMPIONS_LEAGUE_NAME, league(4));
            continentalDraw.put(EUROPA_LEAGUE_NAME, league(4));
        }

        for (final Club[] league : LEAGUES) {
            if (round < 34 || league.length > 18) leagueRound(league, leagueDraw.get(league[0].getLeague()), round);
        }

        if (round % 10 == 3) {
            for (final Club[] league : new Club[][]{England.CLUBS, France.CLUBS}) {
                final String leagueName = league[0].getLeague();
                leagueCup.put(leagueName, knockoutRound(leagueCup.get(leagueName),
                        1, 2, false));
            }
        }

        if (round % 10 == 7) {
            for (final Club[] league : LEAGUES) {
                final String leagueName = league[0].getLeague();
                nationalCup.put(leagueName, knockoutRound(nationalCup.get(leagueName),
                        leagueName.equals(Spain.LEAGUE) ? 2 : 1, 1, leagueName.equals(England.LEAGUE)));
            }
        }

        if (round % 4 == 0) {
            if (round < 21) {
                groupRound(CHAMPIONS_LEAGUE_NAME, CHAMPIONS_LEAGUE, round / 4);
                groupRound(EUROPA_LEAGUE_NAME, EUROPA_LEAGUE, round / 4);
            } else {
                continentalCup.put(EUROPA_LEAGUE_NAME,
                        knockoutRound(continentalCup.get(EUROPA_LEAGUE_NAME), 2, 4, false));

                continentalCup.put(CHAMPIONS_LEAGUE_NAME,
                        knockoutRound(continentalCup.get(CHAMPIONS_LEAGUE_NAME), 2, 3, false));
            }
        }

        if (round == 21) {
            concludeGroups(EUROPA_LEAGUE_NAME, EUROPA_LEAGUE);
            concludeGroups(CHAMPIONS_LEAGUE_NAME, CHAMPIONS_LEAGUE);

            continentalCup.put(EUROPA_LEAGUE_NAME,
                    knockoutRound(continentalCup.get(EUROPA_LEAGUE_NAME), 2, 4, false));
        }

        if (++round == 38) {
            round = 0;
            for (final Club[] league : LEAGUES) {
                standings(league);
                final String leagueName = league[0].getLeague();
                if (leagueCup.containsKey(leagueName)) {
                    final Club leagueCupWinner = leagueCup.get(leagueName)[0];
                    System.out.println(leagueCupWinner.getName() + " win the League Cup!");
                    leagueCupWinner.getGlory().addLeagueCup();
                    for (final Footballer footballer : leagueCupWinner.getFootballers()) {
                        footballer.getResume().getGlory().addLeagueCup();
                    }

                    knockoutPrizes(leagueCup.get(leagueName), false);
                }

                final Club nationalCupWinner = nationalCup.get(leagueName)[0];
                System.out.println(nationalCupWinner.getName() + " win the National Cup!");
                nationalCupWinner.getGlory().addNationalCup();
                for (final Footballer footballer : nationalCupWinner.getFootballers()) {
                    footballer.getResume().getGlory().addNationalCup();
                }

                knockoutPrizes(nationalCup.get(leagueName), false);

                profits(league);
                salaries(league);

                Arrays.stream(league).forEach(Printer::review);
            }

            final Club europaLeagueWinner = continentalCup.get(EUROPA_LEAGUE_NAME)[0];
            System.out.println(europaLeagueWinner.getName() + " win the Europa League!");
            europaLeagueWinner.getGlory().addEuropaLeague();
            for (final Footballer footballer : europaLeagueWinner.getFootballers()) {
                footballer.getResume().getGlory().addEuropaLeague();
            }

            final Club championsLeagueWinner = continentalCup.get(CHAMPIONS_LEAGUE_NAME)[0];
            System.out.println(championsLeagueWinner.getName() + " win the Champions League!");
            championsLeagueWinner.getGlory().addChampionsLeague();
            for (final Footballer footballer : championsLeagueWinner.getFootballers()) {
                footballer.getResume().getGlory().addChampionsLeague();
            }

            knockoutPrizes(EUROPA_LEAGUE, true);
            knockoutPrizes(CHAMPIONS_LEAGUE, true);
            year++;

            progression();
            transfers();
        }
    }

    private static void pickTeam() {
        System.out.println("Pick a team from 1 to 20");
        while (true) {
            final int team = scanner.nextInt();
            if (team < 1 || team > 20) {
                System.out.println("Wrong number");
                continue;
            }

            USER = team - 1;
            break;
        }
    }
}
