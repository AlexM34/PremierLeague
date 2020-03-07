package main.simulation.dynamics;

import main.player.Competition;
import main.player.Footballer;
import main.player.Statistics;
import main.team.Club;
import main.team.League;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static main.player.Position.GK;
import static main.simulation.Controller.getMatchday;
import static main.simulation.Data.LEAGUES;
import static main.simulation.Helper.sortLeague;
import static main.simulation.Helper.sortMap;
import static main.simulation.competition.Knockout.leagueCup;
import static main.simulation.competition.Knockout.nationalCup;
import static main.simulation.competition.League.CHAMPIONS_LEAGUE;
import static main.simulation.competition.League.CHAMPIONS_LEAGUE_NAME;
import static main.simulation.competition.League.EUROPA_LEAGUE;
import static main.simulation.competition.League.EUROPA_LEAGUE_NAME;
import static main.simulation.competition.League.continentalCup;
import static main.simulation.dynamics.Finance.knockoutPrizes;
import static main.simulation.dynamics.Finance.leaguePrizes;
import static main.simulation.dynamics.Finance.merchandise;
import static main.simulation.dynamics.Finance.salaries;

public class Postseason {
    public static Map<Footballer, Integer> ratings = new LinkedHashMap<>();
    public static Map<Footballer, Integer> goals = new LinkedHashMap<>();
    public static Map<Footballer, Integer> assists = new LinkedHashMap<>();
    public static Map<Footballer, Integer> cleanSheets = new LinkedHashMap<>();
    private static final Map<Footballer, Integer> motm = new LinkedHashMap<>();
    private static final Map<Footballer, Integer> yellowCards = new LinkedHashMap<>();
    private static final Map<Footballer, Integer> redCards = new LinkedHashMap<>();
    static final Map<Footballer, Integer> topTeam = new LinkedHashMap<>();

    public static int offset;

    public static void standings(final Club[] league) {
        final List<Club> sorted = sortLeague(league, 0);

        int position = 1;
        System.out.println(String.format("No  Teams %" + (offset - 3) + "s G  W  D  L   GF:GA  P", ""));
        for (final Club team : sorted) {
            final League stats = team.getSeason().getLeague();
            System.out.println(String.format("%2d. %-" + (offset + 3) + "s %-2d %-2d %-2d %-2d %3d:%-3d %-3d", position++,
                    team.getName(), stats.getMatches(), stats.getWins(), stats.getDraws(),  stats.getLosses(),
                    stats.getScored(), stats.getConceded(), stats.getPoints()));
        }

        final Club first = sorted.get(0);
        if (first.getSeason().getLeague().getMatches() == 2 * league.length - 2) {
            first.getGlory().addLeague();
            for (final Footballer footballer : first.getFootballers()) {
                footballer.getResume().getGlory().addLeague();
            }

            reputationUpdate(sorted.toArray(new Club[0]));
            leaguePrizes(sorted.toArray(new Club[0]));
        }
    }

    private static void reputationUpdate(final Club[] league) {
        int performance = 95;
        for (final Club club : league) {
            club.changeReputation(performance);
            performance -= 4;
        }
    }

    public static void playerStats(final Club[] league, final int type, final boolean seasonal) {
        ratings.clear();
        motm.clear();
        goals.clear();
        assists.clear();
        cleanSheets.clear();
        yellowCards.clear();
        redCards.clear();
        topTeam.clear();

        for (final Club club : league) {
            for (final Footballer f : club.getFootballers()) {
                final Statistics stats = seasonal ? f.getResume().getSeason() : f.getResume().getTotal();
                final Competition competition;
                final int games;

                switch (type) {
                    case 0:
                        competition = stats.getLeague();
                        games = getMatchday() / 2;
                        break;
                    case 1:
                        competition = stats.getNationalCup();
                        games = getMatchday() / 18;
                        break;
                    case 2:
                        competition = stats.getLeagueCup();
                        games = getMatchday() / 18;
                        break;
                    case 3:
                        competition = stats.getChampionsLeague();
                        games = getMatchday() / 8;
                        break;
                    default:
                        competition = stats.getEuropaLeague();
                        games = getMatchday() / 8;
                        break;
                }

                if (competition.getMatches() > games) {
                    ratings.put(f, competition.getRating());
                    topTeam.put(f, competition.getRating());
                }

                motm.put(f, competition.getMotmAwards());
                goals.put(f, competition.getGoals());
                assists.put(f, competition.getAssists());
                yellowCards.put(f, competition.getYellowCards());
                redCards.put(f, competition.getRedCards());

                if (f.getPosition() == GK) cleanSheets.put(f, competition.getCleanSheets());
            }
        }

        goals = sortMap(goals);
        assists = sortMap(assists);
        ratings = sortMap(ratings);
        cleanSheets = sortMap(cleanSheets);
    }

    public static Map<String, Integer> allTime(final Club[] league, final int competition) {
        final Map<String, Integer> winners = new LinkedHashMap<>();

        for (final Club club : league) {
            final int cups;
            switch (competition) {
                case 0: cups = club.getGlory().getLeague(); break;
                case 1: cups = club.getGlory().getNationalCup(); break;
                case 2: cups = club.getGlory().getLeagueCup(); break;
                case 3: cups = club.getGlory().getChampionsLeague(); break;
                case 4: cups = club.getGlory().getEuropaLeague(); break;
                default: cups = 0; break;
            }

            if (cups == 0) continue;
            winners.put(club.getName(), cups);
        }

        final Map<String, Integer> sorted = sortMap(winners);

        System.out.println();
        System.out.println("Winners");
        for (int i = 0; i < sorted.size(); i++) {
            System.out.println(String.format("%2d. %-25s %d", i + 1,
                    sorted.keySet().toArray(new String[0])[i], sorted.values().toArray(new Integer[0])[i]));
        }

        return sorted;
    }

    public static List<Footballer> topPlayers() {
        final Map<Footballer, Integer> players = new HashMap<>();
        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
                for (final Footballer f : club.getFootballers()) {
                    players.put(f, f.getOverall());
                }
            }
        }

        final Map<Footballer, Integer> sorted = sortMap(players);
        return new ArrayList<>(sorted.keySet());
    }

    public static void announceCupWinners() {
        for (final Club[] league : LEAGUES) {
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

            merchandise(league);
            salaries(league);

            Arrays.stream(league).forEach(Postseason::review);
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
    }

    static void review(final Club club) {
        System.out.println("Seasonal performance review of " + club.getName());
        System.out.println("League");
        club.getSeason().getLeague().getFixtures()
                .forEach(f -> System.out.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));

        System.out.println();
        System.out.println("National Cup");
        club.getSeason().getNationalCup().getFixtures()
                .forEach(f -> System.out.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));

        System.out.println();
        System.out.println("League Cup");
        club.getSeason().getLeagueCup().getFixtures()
                .forEach(f -> System.out.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));

        System.out.println();
        System.out.println("Continental");
        club.getSeason().getContinental().getGroup().getFixtures()
                .forEach(f -> System.out.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));
        System.out.println();
        club.getSeason().getContinental().getKnockout().getFixtures()
                .forEach(f -> System.out.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));
        System.out.println();
    }
}
