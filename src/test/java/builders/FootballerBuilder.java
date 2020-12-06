package builders;

import player.Footballer;
import player.Position;
import player.Resume;
import simulation.Data;
import simulation.Simulator;

import java.util.Arrays;

public class FootballerBuilder {

    private static final String CLUB_NAME = "Test";
    private static final long VALUE = 50;
    private static final Position.Role ROLE = Position.Role.FORWARD;
    private static final Object[] POSITIONS = Arrays.stream(Position.values())
            .filter(p -> p.getRole().equals(ROLE)).toArray();

    private final int id = Simulator.getInt(100000);
    private final String name = (char) ('A' + Simulator.getInt(26)) + ". " +
            Data.SURNAMES[Simulator.getInt(Data.SURNAMES.length)];
    private int age = 17 + Simulator.getInt(20);
    private final String nationality = Data.NATIONS[Simulator.getInt(Data.NATIONS.length)];
    private int overall = 60 + Simulator.getInt(30);
    private int potential = overall + Simulator.getInt(10);
    private final long wage = 10000 * (1 + Simulator.getInt(5));
    private Position position = (Position) POSITIONS[Simulator.getInt(POSITIONS.length)];
    private final int number = Simulator.getInt(99) + 1;
    private int finishing = overall + 10 * position.getAttackingDuty() - 50;
    private int vision = overall + 10 -
            (3 - position.getAttackingDuty()) * (3 - position.getAttackingDuty()) * 5;

    public FootballerBuilder withAge(final int age) {
        this.age = age;
        return this;
    }

    public FootballerBuilder withOverall(final int overall) {
        this.overall = overall;
        return this;
    }

    public FootballerBuilder withPotential(final int potential) {
        this.potential = potential;
        return this;
    }

    public FootballerBuilder withPosition(final Position position) {
        this.position = position;
        return this;
    }

    public FootballerBuilder withFinishing(final int finishing) {
        this.finishing = finishing;
        return this;
    }

    public FootballerBuilder withVision(final int vision) {
        this.vision = vision;
        return this;
    }

    public Footballer build() {

        return new Footballer(id, name, age, nationality, overall, potential,
                CLUB_NAME, VALUE, wage, position, number, finishing, vision, new Resume());
    }
}
