import java.util.Objects;

class Stadium {
    private final int id;
    private final String name;
    private final int built;
    private final String location;
    private final int capacity;
    private final int reputation;

    Stadium(final int id, final String name, final int built, final String location, final int capacity, final int reputation) {
        this.id = id;
        this.name = name;
        this.built = built;
        this.location = location;
        this.capacity = capacity;
        this.reputation = reputation;
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getBuilt() {
        return built;
    }

    String getLocation() {
        return location;
    }

    int getCapacity() {
        return capacity;
    }

    int getReputation() {
        return reputation;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Stadium stadium = (Stadium) o;
        return id == stadium.id &&
                built == stadium.built &&
                capacity == stadium.capacity &&
                reputation == stadium.reputation &&
                name.equals(stadium.name) &&
                location.equals(stadium.location);
    }

    @Override
    public int hashCode() {
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
