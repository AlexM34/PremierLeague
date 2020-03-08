package simulation;

import main.simulation.Simulator;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimulatorTest {

    private final Random random = new Random();
    private final int iterations = 100000;

    @Test
    void getBoolean() {
        int count = 0;
        for (int i = 0; i < iterations; i++) {
            if (Simulator.getBoolean()) count++;
        }

        assertTrue(count < iterations * 51 / 100);
        assertTrue(count > iterations * 49 / 100);
    }

    @Test
    void getInt() {
        final int limit = 100;
        final int[] count = new int[limit];
        for (int i = 0; i < iterations; i++) count[Simulator.getInt(limit)]++;

        for (int i = 0; i < limit; i++) assertTrue(count[i] > iterations / 120);
    }

    @Test
    void isSatisfiedAlwaysTrue() {
        for (int i = 0; i < iterations; i++) assertTrue(Simulator.isSatisfied(100));
    }

    @Test
    void isSatisfiedAlwaysFalse() {
        for (int i = 0; i < iterations; i++) assertFalse(Simulator.isSatisfied(0));
    }

    @Test
    void isSatisfiedRandomPercentage() {
        final int percentage = 1 + random.nextInt(99);
        int count = 0;
        for (int i = 0; i < iterations; i++) {
            if (Simulator.isSatisfied(percentage)) count++;
        }

        assertTrue(count > iterations * (percentage - 1) / 100);
        assertTrue(count < iterations * (percentage + 1) / 100);
    }

    @Test
    void isSatisfiedWithLimitAlwaysTrue() {
        final int limit = random.nextInt(10000);
        for (int i = 0; i < iterations; i++) assertTrue(Simulator.isSatisfied(limit, limit));
    }

    @Test
    void isSatisfiedWithLimitAlwaysFalse() {
        final int limit = random.nextInt(10000);
        for (int i = 0; i < iterations; i++) assertFalse(Simulator.isSatisfied(0, limit));
    }

    @Test
    void isSatisfiedWithLimit() {
        final int limit = 2 + random.nextInt(1000);
        final int chance = 1 + random.nextInt(limit - 1);
        int count = 0;
        for (int i = 0; i < iterations; i++) {
            if (Simulator.isSatisfied(chance, limit)) count++;
        }

        assertTrue(count > iterations * (chance * 24 / 25 - 1) / limit);
        assertTrue(count < iterations * (chance * 26 / 25 + 1) / limit);
    }

}
