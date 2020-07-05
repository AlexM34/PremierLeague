package simulation.competition;

import team.Cup;

import java.util.Objects;

public class Continental {
    private final team.League group;
    private final Cup knockout;

    public Continental() {
        this.group = new team.League("Continental");
        this.knockout = new Cup();
    }

    public team.League getGroup() {
        return group;
    }

    public Cup getKnockout() {
        return knockout;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Continental that = (Continental) o;
        return group.equals(that.group) &&
                knockout.equals(that.knockout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, knockout);
    }

    @Override
    public String toString() {
        return "Continental{" +
                "group=" + group +
                ", knockout=" + knockout +
                '}';
    }
}