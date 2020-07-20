package player;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static player.Helper.randomiseCompetition;

class StatisticsTest {

    @Test
    void clear() {
        final Statistics statistics = getRandomStatistics();
        statistics.clear();
        assertEquals(new Statistics(), statistics);
    }

    @Test
    void update() {
        final Statistics statistics = new Statistics();
        final Statistics season = getRandomStatistics();
        statistics.update(season);
        assertEquals(season, statistics);
    }

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Statistics.class).verify();
    }

    @Test
    void toStringVerifier() {
        ToStringVerifier.forClass(Statistics.class).verify();
    }

    private Statistics getRandomStatistics() {
        final Statistics statistics = new Statistics();
        randomiseCompetition(statistics.getLeague());
        randomiseCompetition(statistics.getNationalCup());
        randomiseCompetition(statistics.getLeagueCup());
        randomiseCompetition(statistics.getChampionsLeague());
        randomiseCompetition(statistics.getEuropaLeague());

        return statistics;
    }

}