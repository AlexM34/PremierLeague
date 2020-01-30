package simulation;

import team.Club;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static simulation.Controller.CHAMPIONS_LEAGUE_NAME;
import static simulation.Controller.EUROPA_LEAGUE_NAME;
import static simulation.Controller.continentalCup;
import static simulation.Controller.continentalCupResults;
import static simulation.Controller.continentalDraw;
import static simulation.Controller.leagueResults;
import static simulation.Data.USER;
import static simulation.Draw.seededKnockout;
import static simulation.Helper.appendScore;
import static simulation.Helper.getPerformance;
import static simulation.Helper.groupGameOutcome;
import static simulation.Helper.sortMap;
import static simulation.Match.simulation;
import static simulation.Tactics.preMatch;

public class League {

    static void leagueRound(final Club[] league, final int[][][] draw, final int round) {
        System.out.println(String.format("Round %d", round + 1));
        final StringBuilder scores = new StringBuilder();
        final StringBuilder reports = new StringBuilder();
        for (int game = 0; game < league.length / 2; game++) {
            final int home = draw[round][game][0];
            final int away = draw[round][game][1];
            if (home == USER) preMatch(league[away], true);
            else if (away == USER) preMatch(league[home], false);
            final int[] result = simulation(league[home], league[away], false, -1, -1, 0);
            appendScore(scores, reports, league[home], league[away], result);
        }

        leagueResults.put(league[0].getLeague() + (round + 1), scores.toString());
        leagueResults.put("reports: " + league[0].getLeague() + (round + 1), reports.toString());
    }

    static void groupRound(final String competition, final Club[] teams, final int round) {
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
                final int[] result = simulation(clubs[home], clubs[away],
                        false, -1, -1, 3);

                appendScore(scores, reports, clubs[home], clubs[away], result);
                final int homeGoals = result[0];
                final int awayGoals = result[1];

                groupGameOutcome(clubs[home].getSeason().getContinental().getGroup(), clubs[away], homeGoals, awayGoals);
                groupGameOutcome(clubs[away].getSeason().getContinental().getGroup(), clubs[home], awayGoals, homeGoals);
            }

            continentalCupResults.put(competition + letter, scores.toString());
            continentalCupResults.put("reports: " + competition + letter, reports.toString());
        }
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
            System.arraycopy(continentalCup.get(EUROPA_LEAGUE_NAME), 0, europaLeagueTeams, 0, 24);
            thirds.forEach(t -> System.out.println(t.getName()));

            for (int team = 24; team < 32; team++) europaLeagueTeams[team] = thirds.remove(0);
            for (int team = 0; team < 32; team++) System.out.println(europaLeagueTeams[team].getName());
            continentalCup.put(EUROPA_LEAGUE_NAME, europaLeagueTeams);
        }

        continentalCup.put(competition, seededKnockout(advancing));
    }
}
