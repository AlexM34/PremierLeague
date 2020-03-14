package helpers;

import main.player.Footballer;
import main.player.Position;
import main.player.Resume;
import main.simulation.Data;
import main.simulation.Simulator;

import java.util.Arrays;

public class FootballerBuilder {

    private static final String CLUB_NAME = "Test";
    private static final long VALUE = 50;
    private static final Position.Role ROLE = Position.Role.Forward;
    private static final Object[] POSITIONS = Arrays.stream(Position.values())
            .filter(p -> p.getRole().equals(ROLE)).toArray();

    private final int id = Simulator.getInt(100000);
    private final String name = (char) ('A' + Simulator.getInt(26)) + ". " +
            Data.SURNAMES[Simulator.getInt(Data.SURNAMES.length)];
    private final int age = 17 + Simulator.getInt(20);
    private final String nationality = Data.NATIONS[Simulator.getInt(Data.NATIONS.length)];
    private final int overall = 60 + Simulator.getInt(30);
    private final int potential = overall + Simulator.getInt(10);
    private final long wage = 10000 * (1 + Simulator.getInt(5));
    private final Position position = (Position) POSITIONS[Simulator.getInt(POSITIONS.length)];
    private final int number = Simulator.getInt(99) + 1;
    private final int finishing = overall + 10 * position.getAttackingDuty() - 50;
    private final int vision = overall + 10 -
            (3 - position.getAttackingDuty()) * (3 - position.getAttackingDuty()) * 5;

    public Footballer build() {

        return new Footballer(id, name, age, nationality, overall, potential,
                CLUB_NAME, VALUE, wage, position, number, finishing, vision, new Resume());
    }
}
