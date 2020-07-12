package team;

import java.util.Objects;

public class Stadium {
    private final int id;
    private final String name;
    private final int built;
    private final String location;
    private final int capacity;
    private final int reputation;

    public Stadium(final int id, final String name, final int built, final String location, final int capacity, final int reputation) {
        this.id = id;
        this.name = name;
        this.built = built;
        this.location = location;
        this.capacity = capacity;
        this.reputation = reputation;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Stadium)) return false;
        final Stadium stadium = (Stadium) o;
        return id == stadium.id &&
                built == stadium.built &&
                capacity == stadium.capacity &&
                reputation == stadium.reputation &&
                Objects.equals(name, stadium.name) &&
                Objects.equals(location, stadium.location);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, name, built, location, capacity, reputation);
    }

    @Override
    public String toString() {
        return "Stadium{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", built=" + built +
                ", location='" + location + '\'' +
                ", capacity=" + capacity +
                ", reputation=" + reputation +
                '}';
    }
}
