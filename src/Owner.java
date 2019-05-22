import java.util.Objects;

class Owner {
    private int id;
    private String name;
    private String nationality;
    private int wealth;
    private int ambition;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    public int getWealth() {
        return wealth;
    }

    public int getAmbition() {
        return ambition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return id == owner.id &&
                wealth == owner.wealth &&
                ambition == owner.ambition &&
                name.equals(owner.name) &&
                nationality.equals(owner.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nationality, wealth, ambition);
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", wealth=" + wealth +
                ", ambition=" + ambition +
                '}';
    }
}
