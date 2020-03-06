package simulation;

import java.util.Random;

public class Simulator {
    private static final Random RANDOM = new Random();

    public static boolean getBoolean() {
        return RANDOM.nextBoolean();
    }

    public static int getInt(final int limit) {
        return RANDOM.nextInt(limit);
    }

    public static boolean isSatisfied(final int percentage) {
        return isSatisfied(percentage, 100);
    }

    public static boolean isSatisfied(final int chance, final int limit) {
        return RANDOM.nextInt(limit) < chance;
    }
}
