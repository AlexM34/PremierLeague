package team;

import simulation.match.Fixture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class League {
    public static final Map<String, Integer> leagueHomeWins = new HashMap<>();
    public static final Map<String, Integer> leagueDraws = new HashMap<>();
    public static final Map<String, Integer> leagueAwayWins = new HashMap<>();
    public static final Map<String, Integer> leagueScoredHome = new HashMap<>();
    public static final Map<String, Integer> leagueScoredAway = new HashMap<>();
    public static final Map<String, Integer> leagueCleanSheets = new HashMap<>();

    private final String leagueName;
    private final List<Fixture> fixtures;
    private int matches;
    private int points;
    private int wins;
    private int draws;
    private int losses;
    private int scored;
    private int conceded;
    private int cleanSheets;

    public League(final String leagueName) {
        this.leagueName = leagueName;
        this.fixtures = new ArrayList<>();
        this.matches = 0;
        this.points = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.scored = 0;
        this.conceded = 0;
        this.cleanSheets = 0;
    }

    public static void clearLeagueStats() {
        leagueHomeWins.clear();
        leagueDraws.clear();
        leagueAwayWins.clear();
        leagueScoredHome.clear();
        leagueScoredAway.clear();
        leagueCleanSheets.clear();
    }

    public static int getLeagueGames(final String leagueName) {
        return leagueHomeWins.getOrDefault(leagueName, 0) +
                leagueDraws.getOrDefault(leagueName, 0) +
                leagueAwayWins.getOrDefault(leagueName, 0);
    }

    public static String getLeagueHomeWins(final String leagueName) {
        return String.valueOf(leagueHomeWins.getOrDefault(leagueName, 0));
    }

    public static String getLeagueDraws(final String leagueName) {
        return String.valueOf(leagueDraws.getOrDefault(leagueName, 0));
    }

    public static String getLeagueAwayWins(final String leagueName) {
        return String.valueOf(leagueAwayWins.getOrDefault(leagueName, 0));
    }

    public static String getLeagueScoredHome(final String leagueName) {
        return String.valueOf(leagueScoredHome.getOrDefault(leagueName, 0));
    }

    public static String getLeagueScoredAway(final String leagueName) {
        return String.valueOf(leagueScoredAway.getOrDefault(leagueName, 0));
    }

    public static String getLeagueCleanSheets(final String leagueName) {
        return String.valueOf(leagueCleanSheets.getOrDefault(leagueName, 0));
    }

    public List<Fixture> getFixtures() {
        return fixtures;
    }

    public void addFixture(final Club opponent, final boolean isHome, final int scored, final int conceded) {
        fixtures.add(new Fixture(opponent, isHome, new int[]{scored, conceded}));

        if (conceded == 0) this.addCleanSheet();

        if (scored > conceded) this.addWin(isHome);
        else if (scored < conceded) this.addLoss();
        else this.addDraw(isHome);

        this.addMatch();
        this.addScored(scored, isHome);
        this.addConceded(conceded);
    }

    public int getMatches() {
        return matches;
    }

    public void addMatch() {
        this.matches++;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(final int points) {
        this.points += points;
    }

    public int getWins() {
        return wins;
    }

    public void addWin(final boolean isHome) {
        this.wins += 1;
        if (isHome) leagueHomeWins.merge(leagueName, 1, Integer::sum);
        else leagueAwayWins.merge(leagueName, 1, Integer::sum);
        this.addPoints(3);
    }

    public int getDraws() {
        return draws;
    }

    public void addDraw(final boolean isHome) {
        this.draws++;
        if (isHome) leagueDraws.merge(leagueName, 1, Integer::sum);
        this.addPoints(1);
    }

    public int getLosses() {
        return losses;
    }

    public void addLoss() {
        this.losses++;
    }

    public int getScored() {
        return scored;
    }

    public void addScored(final int goals, final boolean isHome) {
        this.scored += goals;
        if (isHome) leagueScoredHome.merge(leagueName, goals, Integer::sum);
        else leagueScoredAway.merge(leagueName, goals, Integer::sum);
    }

    public int getConceded() {
        return conceded;
    }

    public void addConceded(final int goals) {
        this.conceded += goals;
    }

    public void addCleanSheet() {
        this.cleanSheets++;
        leagueCleanSheets.merge(leagueName, 1, Integer::sum);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final League league = (League) o;
        return matches == league.matches &&
                points == league.points &&
                wins == league.wins &&
                draws == league.draws &&
                losses == league.losses &&
                scored == league.scored &&
                conceded == league.conceded &&
                cleanSheets == league.cleanSheets &&
                fixtures.equals(league.fixtures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fixtures, matches, points, wins, draws, losses, scored, conceded, cleanSheets);
    }

    @Override
    public String toString() {
        return "League{" +
                "fixtures=" + fixtures +
                ", matches=" + matches +
                ", points=" + points +
                ", wins=" + wins +
                ", draws=" + draws +
                ", losses=" + losses +
                ", scored=" + scored +
                ", conceded=" + conceded +
                ", cleanSheets=" + cleanSheets +
                '}';
    }
}
