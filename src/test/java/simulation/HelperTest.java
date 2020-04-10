package simulation;

import league.England;
import org.junit.jupiter.api.Test;
import team.Club;
import team.League;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelperTest {

    private final Random random = new Random();

    @Test
    void sortLeague() {
        final int maxPoints = 114;
        final Club[] league = England.getClubs();
        Arrays.stream(league).forEach(club -> club.getSeason().getLeague().addPoints(random.nextInt(maxPoints)));

        final List<Club> sorted = Helper.sortLeague(league, 0);
        int last = maxPoints;
        for (final Club club : sorted) {
            final int points = club.getSeason().getLeague().getPoints();
            assertTrue(last >= points);
            last = points;
        }
    }

    @Test
    void sortGroup() {
        final int maxPoints = 18;
        final Club[] league = England.getClubs();
        Arrays.stream(league).forEach(club ->
                club.getSeason().getContinental().getGroup().addPoints(random.nextInt(maxPoints)));

        final List<Club> sorted = Helper.sortLeague(league, 3);
        int last = maxPoints;
        for (final Club club : sorted) {
            final int points = club.getSeason().getContinental().getGroup().getPoints();
            assertTrue(last >= points);
            last = points;
        }
    }

    @Test
    void sortCup() {
        assertEquals(Collections.emptyList(), Helper.sortLeague(England.getClubs(), 1));
    }

    @Test
    void sortMap() {
        final Map<String, Integer> map = Map.of("Fourth", 48, "Second", 189, "First", 241,
                "Fifth", 22, "Third", 103);

        final Map<String, Integer> expected = Map.of("First", 241, "Second", 189, "Third", 103,
                "Fourth", 48, "Fifth", 22);

        assertEquals(expected, Helper.sortMap(map));
    }

    @Test
    void getPerformance() {
        final League stats = new League("test");
        stats.addPoints(70);
        stats.addScored(61, true);
        stats.addConceded(43);

        assertEquals(701861, Helper.getPerformance(stats));
    }
}
