package main.view;

import java.util.Arrays;
import java.util.Optional;

public enum Stage {
    R32("Round of 32", 32), R16("Round of 16", 16), QF("Quarter-finals", 8),
    SF("Semi-finals", 4), F("Final", 2);

    private final String name;
    private final int teams;

    Stage(final String name, final int teams) {
        this.name = name;
        this.teams = teams;
    }

    String getName() {
        return name;
    }

    int getTeams() {
        return teams;
    }

    static int getTeams(final String name) {
        final Optional<Stage> stage = Arrays.stream(Stage.values()).filter(s -> s.getName().equals(name)).findFirst();
        return stage.isEmpty() ? 1 : stage.get().getTeams();
    }
}
