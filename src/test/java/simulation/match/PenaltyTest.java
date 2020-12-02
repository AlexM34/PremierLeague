package simulation.match;

import static org.junit.jupiter.api.Assertions.assertEquals;

import builders.FootballerBuilder;
import org.junit.jupiter.api.Test;
import player.Footballer;

class PenaltyTest {

    @Test
    void calculateChanceMin() {
        final Footballer striker = new FootballerBuilder().withOverall(1).withFinishing(1).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(99).build();

        assertEquals(30, Penalty.calculateChance(striker, goalkeeper));
    }

    @Test
    void calculateChanceMax() {
        final Footballer striker = new FootballerBuilder().withOverall(99).withFinishing(99).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(1).build();

        assertEquals(90, Penalty.calculateChance(striker, goalkeeper));
    }

    @Test
    void calculateChanceTopStrikerTopKeeper() {
        final Footballer striker = new FootballerBuilder().withOverall(90).withFinishing(90).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(90).build();

        assertEquals(80, Penalty.calculateChance(striker, goalkeeper), 5);
    }

    @Test
    void calculateChanceTopStrikerAverageKeeper() {
        final Footballer striker = new FootballerBuilder().withOverall(90).withFinishing(90).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(75).build();

        assertEquals(85, Penalty.calculateChance(striker, goalkeeper), 5);
    }

    @Test
    void calculateChanceTopStrikerBadKeeper() {
        final Footballer striker = new FootballerBuilder().withOverall(90).withFinishing(90).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(60).build();

        assertEquals(90, Penalty.calculateChance(striker, goalkeeper), 5);
    }

    @Test
    void calculateChanceTopPlayerAverageFinishingTopKeeper() {
        final Footballer striker = new FootballerBuilder().withOverall(90).withFinishing(60).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(90).build();

        assertEquals(75, Penalty.calculateChance(striker, goalkeeper), 5);
    }

    @Test
    void calculateChanceAveragePlayerTopFinishingAverageKeeper() {
        final Footballer striker = new FootballerBuilder().withOverall(75).withFinishing(90).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(75).build();

        assertEquals(80, Penalty.calculateChance(striker, goalkeeper), 5);
    }

    @Test
    void calculateChanceAveragePlayerTopFinishingBadKeeper() {
        final Footballer striker = new FootballerBuilder().withOverall(75).withFinishing(90).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(60).build();

        assertEquals(90, Penalty.calculateChance(striker, goalkeeper), 5);
    }

    @Test
    void calculateChanceAverageStrikerTopKeeper() {
        final Footballer striker = new FootballerBuilder().withOverall(75).withFinishing(60).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(90).build();

        assertEquals(65, Penalty.calculateChance(striker, goalkeeper), 5);
    }

    @Test
    void calculateChanceAverageStrikerAverageKeeper() {
        final Footballer striker = new FootballerBuilder().withOverall(75).withFinishing(60).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(75).build();

        assertEquals(70, Penalty.calculateChance(striker, goalkeeper), 5);
    }

    @Test
    void calculateChanceAverageStrikerBadKeeper() {
        final Footballer striker = new FootballerBuilder().withOverall(75).withFinishing(60).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(60).build();

        assertEquals(75, Penalty.calculateChance(striker, goalkeeper), 5);
    }

    @Test
    void calculateChanceBadStrikerStrikerTopKeeper() {
        final Footballer striker = new FootballerBuilder().withOverall(70).withFinishing(35).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(90).build();

        assertEquals(55, Penalty.calculateChance(striker, goalkeeper), 5);
    }

    @Test
    void calculateChanceBadStrikerAverageKeeper() {
        final Footballer striker = new FootballerBuilder().withOverall(70).withFinishing(35).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(75).build();

        assertEquals(60, Penalty.calculateChance(striker, goalkeeper), 5);
    }

    @Test
    void calculateChanceBadStrikerBadKeeper() {
        final Footballer striker = new FootballerBuilder().withOverall(70).withFinishing(35).build();
        final Footballer goalkeeper = new FootballerBuilder().withOverall(60).build();

        assertEquals(65, Penalty.calculateChance(striker, goalkeeper), 5);
    }
}
