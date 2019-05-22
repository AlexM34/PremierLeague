import java.util.Objects;

class Stadium {
    private int id;
    private String name;
    private int built;
    private String location;
    private int capacity;
    private int reputation;

    public Stadium(int id, String name, int built, String location, int capacity, int reputation) {
        this.id = id;
        this.name = name;
        this.built = built;
        this.location = location;
        this.capacity = capacity;
        this.reputation = reputation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBuilt() {
        return built;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getReputation() {
        return reputation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stadium stadium = (Stadium) o;
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
