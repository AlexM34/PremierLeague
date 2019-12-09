package players;

import java.util.Objects;

public class Footballer {
    private final int id;
    private final String name;
    private int age;
    private final String nationality;
    private int overall;
    private int potential;
    private long value;
    private long wage;
    private Position position;
    private int number;
    private int finishing;
    private int vision;
    private Resume resume;
    private int condition;
    private int ban;

    public Footballer(final int id, final String name, final int age, final String nationality, final int overall,
                      final int potential, final long value, final long wage, final Position position,
                      final int number, final int finishing, final int vision, final Resume resume) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.nationality = nationality;
        this.overall = overall;
        this.potential = potential;
        this.value = value;
        this.wage = wage;
        this.position = position;
        this.number = number;
        this.finishing = finishing;
        this.vision = vision;
        this.resume = resume;
        this.condition = 100;
        this.ban = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    String getNationality() {
        return nationality;
    }

    public int getOverall() {
        return overall;
    }

    public void changeOverall(final int change) {
        this.overall += change;
    }

    public int getPotential() {
        return potential;
    }

    public void changePotential(final int change) {
        this.potential += change;
    }

    public long getValue() {
        int overall = getOverall() - 70;
        overall = overall > 4 ? overall * overall * overall / 100 : 1;
        float age = getAge() != 27 ? (float) Math.sqrt(Math.abs(27 - getAge())) : 1;
        age = getAge() < 27 ? age : 1 / age;
        float position = getPosition().getAttackingDuty() + 1;
        position = (float) Math.sqrt(position);
        float potential = (float) (getPotential() - getOverall()) / 3;
        potential = potential > 1 ? (float) Math.sqrt(potential) : 1;

        value = (long) (overall * age * position * potential);
        return value;
    }

    void setValue(final long value) {
        this.value = value;
    }

    public float getWage() {
        return wage;
    }

    void setWage(final long wage) {
        this.wage = wage;
    }

    public Position getPosition() {
        return position;
    }

    void setPosition(final Position position) {
        this.position = position;
    }

    public int getNumber() {
        return number;
    }

    void setNumber(final int number) {
        this.number = number;
    }

    public int getFinishing() {
        return finishing;
    }

    void setFinishing(final int finishing) {
        this.finishing = finishing;
    }

    public int getVision() {
        return vision;
    }

    void setVision(final int vision) {
        this.vision = vision;
    }

    public Resume getResume() {
        return resume;
    }

    void setResume(final Resume resume) {
        this.resume = resume;
    }

    public int getCondition() {
        return condition;
    }

    public void changeCondition(final int change) {
        this.condition += change;
        if (this.condition > 100) this.condition = 100;
        else if (this.condition < 0) this.condition = 0;
    }

    public int getBan() {
        return ban;
    }

    public void changeBan(final int ban) {
        this.ban += ban;
        if (this.ban < 0) this.ban = 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Footballer that = (Footballer) o;
        return id == that.id &&
                name.equals(that.name) &&
                age == that.age &&
                nationality.equals(that.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nationality);
    }

    @Override
    public String toString() {
        return "Footballer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}