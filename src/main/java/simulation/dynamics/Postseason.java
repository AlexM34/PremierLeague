package simulation.dynamics;

import static player.Position.GK;
import static simulation.Controller.getMatchday;
import static simulation.Data.LEAGUES;
import static simulation.Helper.sortLeague;
import static simulation.Helper.sortMap;
import static simulation.competition.Knockout.leagueCup;
import static simulation.competition.Knockout.nationalCup;
import static simulation.competition.League.CHAMPIONS_LEAGUE_NAME;
import static simulation.competition.League.EUROPA_LEAGUE_NAME;
import static simulation.competition.League.continentalCup;
import static simulation.dynamics.Finance.knockoutPrizes;
import static simulation.dynamics.Finance.leaguePrizes;
import static simulation.dynamics.Finance.merchandise;
import static simulation.dynamics.Finance.salaries;

import player.Competition;
import player.Footballer;
import player.Statistics;
import team.Club;
import team.League;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Postseason {

    private static final PrintStream STREAM = new PrintStream(new FileOutputStream(FileDescriptor.out));
    public static Map<Footballer, Integer> ratings = new LinkedHashMap<>();
    public static Map<Footballer, Integer> goals = new LinkedHashMap<>();
    public static Map<Footballer, Integer> assists = new LinkedHashMap<>();
    public static Map<Footballer, Integer> cleanSheets = new LinkedHashMap<>();
    private static final Map<Footballer, Integer> motm = new LinkedHashMap<>();
    private static final Map<Footballer, Integer> yellowCards = new LinkedHashMap<>();
    private static final Map<Footballer, Integer> redCards = new LinkedHashMap<>();
    static final Map<Footballer, Integer> topTeam = new LinkedHashMap<>();

    public static int offset;

    private Postseason() {
    }

    public static void standings(final Club[] league) {
        final List<Club> sorted = sortLeague(league, 0);
        printStandings(sorted);
        finaliseLeague(sorted);
    }

    private static void printStandings(final List<Club> league) {
        int position = 1;
        STREAM.printf("No  Teams %" + (offset - 3) + "s G  W  D  L   GF:GA  P%n", "");
        for (final Club team : league) {
            final League stats = team.getSeason().getLeague();
            STREAM.printf("%2d. %-" + (offset + 3) + "s %-2d %-2d %-2d %-2d %3d:%-3d %-3d%n",
                    position++, team.getName(), stats.getMatches(), stats.getWins(), stats.getDraws(),
                    stats.getLosses(), stats.getScored(), stats.getConceded(), stats.getPoints());
        }
    }

    private static void finaliseLeague(final List<Club> league) {
        final Club first = league.get(0);
        if (first.getSeason().getLeague().getMatches() == 2 * league.size() - 2) {
            giveTrophy(first);
            reputationUpdate(league.toArray(new Club[0]));
            leaguePrizes(league.toArray(new Club[0]));
        }
    }

    private static void giveTrophy(final Club champion) {
        champion.getGlory().addLeague();
        for (final Footballer footballer : champion.getFootballers()) {
            footballer.getResume().getGlory().addLeague();
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
        clearStats();

        Arrays.stream(league).forEach(club -> club.getFootballers()
                .forEach(footballer -> addStats(footballer, type, seasonal)));

        sortStats();
    }

    private static void addStats(final Footballer f, final int type, final boolean seasonal) {
        final Statistics stats = seasonal ? f.getResume().getSeason() : f.getResume().getTotal();
        final Competition competition = getCompetition(stats, type);
        final int minGames;

        switch (type) {
            case 0: minGames = getMatchday() / 4; break;
            case 1: case 2: minGames = getMatchday() / 30; break;
            case 3: case 4: minGames = getMatchday() / 12; break;
            default: minGames = 0;
        }

        if (competition.getMatches() > minGames) {
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

    private static Competition getCompetition(final Statistics stats, final int type) {
        switch (type) {
            case 0: return stats.getLeague();
            case 1: return stats.getNationalCup();
            case 2: return stats.getLeagueCup();
            case 3: return stats.getChampionsLeague();
            case 4: return stats.getEuropaLeague();
            default: throw new IllegalArgumentException("Illegal competition type!");
        }
    }

    private static void clearStats() {
        ratings.clear();
        motm.clear();
        goals.clear();
        assists.clear();
        cleanSheets.clear();
        yellowCards.clear();
        redCards.clear();
        topTeam.clear();
    }

    private static void sortStats() {
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

            if (cups > 0) winners.put(club.getName(), cups);
        }

        final Map<String, Integer> sorted = sortMap(winners);

        STREAM.println();
        STREAM.println("Winners");
        for (int i = 0; i < sorted.size(); i++) {
            STREAM.println(String.format("%2d. %-25s %d", i + 1,
                    sorted.keySet().toArray(new String[0])[i], sorted.values().toArray(new Integer[0])[i]));
        }

        return sorted;
    }

    public static List<Footballer> topPlayers() {
        final Map<Footballer, Integer> players = new HashMap<>();
        Arrays.stream(LEAGUES).forEach(league -> Arrays.stream(league)
                .forEach(club -> club.getFootballers().forEach(f -> players.put(f, f.getOverall()))));

        final Map<Footballer, Integer> sorted = sortMap(players);
        return new ArrayList<>(sorted.keySet());
    }

    public static void announceCupWinners() {
        for (final Club[] league : LEAGUES) {
            final String leagueName = league[0].getLeague();
            if (leagueCup.containsKey(leagueName)) giveCupTrophy(leagueCup, leagueName, 2);

            giveCupTrophy(nationalCup, leagueName, 1);

            merchandise(league);
            salaries(league);

            Arrays.stream(league).forEach(Postseason::review);
        }

        giveCupTrophy(continentalCup, EUROPA_LEAGUE_NAME, 4);
        giveCupTrophy(continentalCup, CHAMPIONS_LEAGUE_NAME, 3);
    }

    private static void giveCupTrophy(final Map<String, Club[]> cup, final String leagueName, final int competition) {
        final Club champion = cup.get(leagueName)[0];
        addCup(champion, competition);
        knockoutPrizes(cup.get(leagueName), competition > 2);
    }

    private static void addCup(final Club champion, final int competition) {
        switch(competition) {
            case 1: addLeagueCup(champion); break;
            case 2: addNationalCup(champion); break;
            case 3: addChampionsLeague(champion); break;
            case 4: addEuropaLeague(champion);
        }
    }

    private static void addLeagueCup(final Club champion) {
        STREAM.println(champion.getName() + " win the League Cup!");
        champion.getGlory().addLeagueCup();
        for (final Footballer footballer : champion.getFootballers()) {
            footballer.getResume().getGlory().addLeagueCup();
        }
    }

    private static void addNationalCup(final Club champion) {
        STREAM.println(champion.getName() + " win the National Cup!");
        champion.getGlory().addNationalCup();
        for (final Footballer footballer : champion.getFootballers()) {
            footballer.getResume().getGlory().addNationalCup();
        }
    }

    private static void addChampionsLeague(final Club champion) {
        STREAM.println(champion.getName() + " win the Champions League!");
        champion.getGlory().addChampionsLeague();
        for (final Footballer footballer : champion.getFootballers()) {
            footballer.getResume().getGlory().addChampionsLeague();
        }
    }

    private static void addEuropaLeague(final Club champion) {
        STREAM.println(champion.getName() + " win the Europa League!");
        champion.getGlory().addEuropaLeague();
        for (final Footballer footballer : champion.getFootballers()) {
            footballer.getResume().getGlory().addEuropaLeague();
        }
    }

    static void review(final Club club) {
        STREAM.println("Seasonal performance review of " + club.getName());
        leagueReview(club);
        nationalCupReview(club);
        leagueCupReview(club);
        continentalReview(club);
    }

    private static void leagueReview(final Club club) {
        STREAM.println("League");
        club.getSeason().getLeague().getFixtures()
                .forEach(f -> STREAM.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));
    }

    private static void nationalCupReview(final Club club) {
        STREAM.println();
        STREAM.println("National Cup");
        club.getSeason().getNationalCup().getFixtures()
                .forEach(f -> STREAM.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));
    }

    private static void leagueCupReview(final Club club) {
        STREAM.println();
        STREAM.println("League Cup");
        club.getSeason().getLeagueCup().getFixtures()
                .forEach(f -> STREAM.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));
    }

    private static void continentalReview(final Club club) {
        STREAM.println();
        STREAM.println("Continental");
        club.getSeason().getContinental().getGroup().getFixtures()
                .forEach(f -> STREAM.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));
        STREAM.println();
        club.getSeason().getContinental().getKnockout().getFixtures()
                .forEach(f -> STREAM.println(f.getOpponent().getName() + Arrays.toString(f.getScore())));
        STREAM.println();
    }
}
