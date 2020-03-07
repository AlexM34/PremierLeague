package main.league;

import main.team.Club;

import java.util.Arrays;
import java.util.List;

import static main.simulation.competition.League.CHAMPIONS_LEAGUE_NAME;
import static main.simulation.competition.League.EUROPA_LEAGUE_NAME;

public class LeagueManager {

    public static String[] getLeagues() {
        return new String[]{England.getLeague(), Spain.getLeague(), Germany.getLeague(),
                Italy.getLeague(), France.getLeague(), CHAMPIONS_LEAGUE_NAME, EUROPA_LEAGUE_NAME};
    }

    public static Club[] getClubs(final String league) {
        switch (league) {
            case "Premier League": return England.getClubs();
            case "La Liga": return Spain.getClubs();
            case "Bundesliga": return Germany.getClubs();
            case "Serie A": return Italy.getClubs();
            case "Ligue 1": return France.getClubs();
            default: return Arrays.stream(getAllClubs()).flatMap(Arrays::stream).toArray(Club[]::new);
        }
    }

    public static Club[][] getAllClubs() {
        return new Club[][]{England.getClubs(), Spain.getClubs(), Germany.getClubs(),
                Italy.getClubs(), France.getClubs()};
    }

    public static int getNationalCupGames(final String leagueName) {
        return leagueName.equals(Spain.getLeague()) ? 2 : 1;
    }

    public static List<Club[]> getLeagueCupClubs() {
        return Arrays.asList(England.getClubs(), France.getClubs());
    }

    public static boolean isReplayed(final String leagueName) {
        return leagueName.equals(England.getLeague());
    }
}
