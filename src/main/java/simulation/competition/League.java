package simulation.competition;

import static simulation.Data.LEAGUES;
import static simulation.Data.USER;
import static simulation.Helper.getPerformance;
import static simulation.Helper.sortMap;
import static simulation.competition.Competition.LEAGUE;
import static simulation.competition.Draw.league;
import static simulation.competition.Draw.seededKnockout;
import static simulation.dynamics.Preseason.pickContinentalTeams;
import static simulation.match.Tactics.preMatch;

import player.MatchStats;
import simulation.match.Match;
import team.Club;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class League {

    public static final String CHAMPIONS_LEAGUE_NAME = "Champions League";
    public static final String EUROPA_LEAGUE_NAME = "Europa League";
    public static final Club[] CHAMPIONS_LEAGUE = new Club[32];
    public static final Club[] EUROPA_LEAGUE = new Club[48];

    private static final Map<String, int[][][]> leagueDraw = new HashMap<>();
    static final Map<String, int[][][]> continentalDraw = new HashMap<>();
    public static final Map<String, Club[]> continentalCup = new HashMap<>();
    public static final Map<String, String> leagueResults = new HashMap<>();
    public static final Map<String, String> continentalCupResults = new HashMap<>();
    public static final Map<String, Integer> leagueAssists = new HashMap<>();
    public static final Map<String, Integer> leagueYellowCards = new HashMap<>();
    public static final Map<String, Integer> leagueRedCards = new HashMap<>();
    public static final Map<String, Float> leagueAverageRatings = new HashMap<>();

    private static int leagueRound;
    private static int championsLeagueRound;
    private static int europaLeagueRound;

    public static void setupLeagues() {
        leagueDraw.clear();
        continentalDraw.clear();
        continentalCup.clear();
        leagueResults.clear();
        continentalCupResults.clear();
        leagueAssists.clear();
        leagueYellowCards.clear();
        leagueRedCards.clear();
        leagueAverageRatings.clear();

        pickContinentalTeams(CHAMPIONS_LEAGUE, EUROPA_LEAGUE);

        for (final Club[] league : LEAGUES) {
            final String leagueName = league[0].getLeague();
            leagueDraw.put(leagueName, league(league.length));
        }

        continentalDraw.put(CHAMPIONS_LEAGUE_NAME, league(4));
        continentalDraw.put(EUROPA_LEAGUE_NAME, league(4));
        leagueRound = 0;
        championsLeagueRound = 0;
        europaLeagueRound = 0;
    }

    public static void allLeagues() {
        for (final Club[] league : LEAGUES) {
            if (leagueRound < 34 || league.length > 18) leagueRound(league);
        }

        leagueRound++;
    }

    static void leagueRound(final Club[] league) {
        System.out.println(String.format("Round %d", leagueRound + 1));
        final StringBuilder scores = new StringBuilder();
        final StringBuilder reports = new StringBuilder();
        final int[][][] draw = leagueDraw.get(league[0].getLeague());
        for (int game = 0; game < league.length / 2; game++) {
            final int home = draw[leagueRound][game][0];
            final int away = draw[leagueRound][game][1];
            if (home == USER) preMatch(league[away], true);
            else if (away == USER) preMatch(league[home], false);

            final Match match = new Match(league[home], league[away], LEAGUE, -1, -1, false);
            match.simulate();
            match.appendScore(league[home], league[away], scores, reports);
        }

        leagueResults.put(league[0].getLeague() + (leagueRound + 1), scores.toString());
        leagueResults.put("reports: " + league[0].getLeague() + (leagueRound + 1), reports.toString());
    }

    public static void groupRound(final boolean isChampionsLeague) {
        final String competition;
        final Club[] teams;
        final int round;
        if (isChampionsLeague) {
            competition = CHAMPIONS_LEAGUE_NAME;
            teams = CHAMPIONS_LEAGUE;
            round = championsLeagueRound++;
        } else {
            competition = EUROPA_LEAGUE_NAME;
            teams = EUROPA_LEAGUE;
            round = europaLeagueRound++;
        }

        final int groups = teams.length / 4;
        final int[][][] draw = continentalDraw.get(competition);
        for (int group = 0; group < groups; group++) {
            final String letter = String.valueOf((char) ('A' + group));
            System.out.println("GROUP " + letter);

            final StringBuilder scores = new StringBuilder(continentalCupResults.getOrDefault(
                    competition + letter, ""));
            final StringBuilder reports = new StringBuilder(continentalCupResults.getOrDefault(
                    "reports: " + competition + letter, ""));

            final Club[] clubs = new Club[4];
            for (int team = 0; team < 4; team++) clubs[team] = teams[groups * team + group];
            System.out.println();
            System.out.println("Matchday " + (round + 1));
            if (scores.length() > 0) scores.append("<br/>");
            if (reports.length() > 0) reports.append("<br/>");

            for (int game = 0; game < 2; game++) {
                final int home = draw[round][game][0];
                final int away = draw[round][game][1];
                final Match match = new Match(clubs[home], clubs[away], Competition.CHAMPIONS_LEAGUE,
                        -1, -1, false);
                match.simulate();

                match.appendScore(clubs[home], clubs[away], scores, reports);
                final int homeGoals = match.getHomeGoals();
                final int awayGoals = match.getAwayGoals();

                clubs[home].getSeason().getContinental().getGroup().addFixture(clubs[away],
                        true, homeGoals, awayGoals);
                clubs[away].getSeason().getContinental().getGroup().addFixture(clubs[home],
                        false, awayGoals, homeGoals);
            }

            continentalCupResults.put(competition + letter, scores.toString());
            continentalCupResults.put("reports: " + competition + letter, reports.toString());
        }

        if (round == 5) concludeGroups(competition, teams);
    }

    static void concludeGroups(final String competition, final Club[] teams) {
        final Club[] advancing = new Club[2 * teams.length / 4];
        final int groups = teams.length / 4;
        int count = 0;

        final List<Club> thirds = new ArrayList<>();
        for (int group = 0; group < groups; group++) {
            System.out.println("GROUP " + (char) ('A' + group));

            final Map<Club, Integer> standing = new LinkedHashMap<>();
            for (int team = 0; team < 4; team++) {
                final team.League groupStats = teams[groups * team + group].getSeason().getContinental().getGroup();
                standing.put(teams[groups * team + group], getPerformance(groupStats));
            }

            System.out.println();
            final Map<Club, Integer> sorted = sortMap(standing);

            System.out.println();
            System.out.println("Standings for group " + (char) ('A' + group));
            System.out.println("No  Teams                     G  W  D  L  GF:GA  P");
            final Club[] rankedTeams = sorted.keySet().toArray(new Club[0]);
            for (int team = 0; team < sorted.size(); team++) {
                final Club club = rankedTeams[team];
                final team.League groupStats = club.getSeason().getContinental().getGroup();

                System.out.println(String.format("%2d. %-25s %-2d %-2d %-2d %-2d %2d:%-2d %2d", team + 1, club.getName(),
                        groupStats.getMatches(), groupStats.getWins(), groupStats.getDraws(), groupStats.getLosses(),
                        groupStats.getScored(), groupStats.getConceded(), groupStats.getPoints()));

                if (team < 2) advancing[count++] = club;
            }

            if (competition.equals(CHAMPIONS_LEAGUE_NAME)) thirds.add(rankedTeams[2]);
        }

        if (competition.equals(CHAMPIONS_LEAGUE_NAME)) {
            final Club[] europaLeagueTeams = new Club[32];
            thirds.forEach(t -> System.out.println(t.getName()));

            for (int team = 0; team < 8; team++) europaLeagueTeams[team] = thirds.remove(0);
            continentalCup.put(EUROPA_LEAGUE_NAME, europaLeagueTeams);
            continentalCup.put(CHAMPIONS_LEAGUE_NAME, seededKnockout(advancing));

        } else {
            final Club[] europaLeagueTeams = continentalCup.get(EUROPA_LEAGUE_NAME);
            System.arraycopy(advancing, 0, europaLeagueTeams, 8, 24);
            for (int team = 0; team < 32; team++) System.out.println(europaLeagueTeams[team].getName());

            continentalCup.put(EUROPA_LEAGUE_NAME, seededKnockout(europaLeagueTeams));
        }
    }

    public static void updateLeagueStats(final String leagueName, final List<MatchStats> squad) {
        for (final MatchStats footballer : squad) {
            leagueAverageRatings.merge(leagueName, footballer.getRating(), Float::sum);
            leagueAssists.merge(leagueName, footballer.getAssists(), Integer::sum);
            leagueYellowCards.merge(leagueName, footballer.isYellowCarded() ? 1 : 0, Integer::sum);
            leagueRedCards.merge(leagueName, footballer.isRedCarded() ? 1 : 0, Integer::sum);
        }
    }
}
