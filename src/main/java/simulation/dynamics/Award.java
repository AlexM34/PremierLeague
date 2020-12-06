package simulation.dynamics;

import static simulation.Helper.sortMap;
import static simulation.dynamics.Postseason.topTeam;

import player.Footballer;
import player.Statistics;
import simulation.Simulator;
import team.Club;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class Award {

    private static final PrintStream STREAM = new PrintStream(new FileOutputStream(FileDescriptor.out));
    private static final Map<Footballer, Integer> contenders = new HashMap<>();

    private Award() {
    }

    static void pickTeam(final Map<Footballer, Integer> ratings, final boolean isLeague) {
        topTeam.clear();
        int[] team = new int[11];
        int goalkeepers = 1;
        int defenders = 4;
        int midfielders = 3;
        int forwards = 3;

        final Map<Footballer, Integer> sorted = sortMap(ratings);

        for (int player = 0; player < sorted.size(); player++) {
            switch (sorted.keySet().toArray(new Footballer[0])[player].getPosition().getRole()) {
                case GOALKEEPER:
                    if (goalkeepers > 0) team[--goalkeepers] = player;
                    break;
                case DEFENDER:
                    if (defenders > 0) team[defenders--] = player;
                    break;
                case MIDFIELDER:
                    if (midfielders > 0) team[4 + midfielders--] = player;
                    break;
                case FORWARD:
                    if (forwards > 0) team[7 + forwards--] = player;
                    break;
            }

            if (goalkeepers + defenders + midfielders + forwards == 0) break;
        }

        STREAM.println("TEAM OF THE SEASON");
        for (int player = 0; player < 11; player++) {
            Footballer footballer = sorted.keySet().toArray(new Footballer[0])[team[player]];
            int rating = sorted.values().toArray(new Integer[0])[team[player]];

            STREAM.printf("%2d. %-20s %2d%n", player + 1, footballer.getName(), rating);

            if (isLeague) topTeam.put(footballer, rating);
        }

        STREAM.println();
    }

    static void voting(final Map<Footballer, Integer> contenders) {
        Footballer[] players = new Footballer[10];
        int[] chance = new int[10];
        int[] votes = new int[10];
        int total = 0;
        int current = 0;
        for (final Map.Entry<Footballer, Integer> entry : contenders.entrySet()) {
            Footballer c = entry.getKey();
            Integer p = entry.getValue();
            STREAM.println(c.getName() + " with " + p);

            players[current] = c;
            chance[current] = Math.max(p - 750, 1);
            total += chance[current++];
        }

        for (int v = 0; v < 100; v++) {
            int pick = Simulator.getInt(total);
            for (int player = 0; player < 10; player++) {
                pick -= chance[player];
                if (pick < 0) {
                    votes[player]++;
                    break;
                }
            }
        }

        Map<Footballer, Integer> results = new HashMap<>();
        for (int i = 0; i < 10; i++) results.put(players[i], votes[i]);

        final Map<Footballer, Integer> sorted = sortMap(results);

        STREAM.println("FOOTBALLER OF THE YEAR");
        for (int player = 0; player < 10; player++) {
            Footballer footballer = sorted.keySet().toArray(new Footballer[0])[player];
            int count = sorted.values().toArray(new Integer[0])[player];

            STREAM.printf("%2d. %-20s %2d%n", player + 1, footballer.getName(), count);
        }

        STREAM.println();
    }

    static void topPlayers(final Club[] clubs) {
        contenders.clear();

        for (int i = 0; i < clubs.length; i++) {
            for (final Footballer footballer : clubs[i].getFootballers()) {
                Statistics season = footballer.getResume().getSeason();
                int matches = season.getLeague().getMatches() + season.getNationalCup().getMatches() * 2 +
                        season.getLeagueCup().getMatches() * 2 + season.getChampionsLeague().getMatches() * 5 +
                        season.getEuropaLeague().getMatches() * 3;
                float ratings = season.getLeague().getRating() * season.getLeague().getMatches() +
                        season.getNationalCup().getRating() * season.getNationalCup().getMatches() * 2 +
                        season.getLeagueCup().getRating() * season.getLeagueCup().getMatches() * 2 +
                        season.getChampionsLeague().getRating() * season.getChampionsLeague().getMatches() * 5 +
                        season.getEuropaLeague().getRating() * season.getEuropaLeague().getMatches() * 3;

                float performance = matches > 50 ? (ratings / ((float) matches)) : 0;

                if (i == 0) performance += 50;
                else if (i == 1) performance += 35;
                else if (i < 4) performance += 20;
                else if (i < 8) performance += 10;

                contenders.put(footballer, (int) performance);
            }
        }

        final Map<Footballer, Integer> sorted = sortMap(contenders);
        contenders.clear();

        for (int player = 0; player < 10; player++) {
            contenders.put(sorted.keySet().toArray(new Footballer[0])[player],
                    sorted.values().toArray(new Integer[0])[player]);
        }
    }

}
