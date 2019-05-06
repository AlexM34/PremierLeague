import java.util.Objects;

public class Footballer {
    private String name;
    private int age;
    private String nationality;
    private int overall;
    private int potential;
    private double value;
    private double wage;
    private String position;
    private int number;
    private int finishing;
    private int vision;

    Footballer(String name, int age, String nationality, int overall,
               int potential, double value, double wage, String position,
               int number, int finishing, int vision) {
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
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNationality() {
        return nationality;
    }

    public int getOverall() {
        return overall;
    }

    public void setOverall(int overall) {
        this.overall = overall;
    }

    public int getPotential() {
        return potential;
    }

    public void setPotential(int potential) {
        this.potential = potential;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFinishing() {
        return finishing;
    }

    public void setFinishing(int finishing) {
        this.finishing = finishing;
    }

    public int getVision() {
        return vision;
    }

    public void setVision(int vision) {
        this.vision = vision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Footballer that = (Footballer) o;
        return age == that.age &&
                overall == that.overall &&
                potential == that.potential &&
                Double.compare(that.value, value) == 0 &&
                Double.compare(that.wage, wage) == 0 &&
                number == that.number &&
                finishing == that.finishing &&
                vision == that.vision &&
                name.equals(that.name) &&
                nationality.equals(that.nationality) &&
                position.equals(that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, nationality, overall, potential, value, wage, position, number, finishing, vision);
    }

    @Override
    public String toString() {
        return "Footballer{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", nationality='" + nationality + '\'' +
                ", overall=" + overall +
                ", potential=" + potential +
                ", value=" + value +
                ", wage=" + wage +
                ", position='" + position + '\'' +
                ", number=" + number +
                ", finishing=" + finishing +
                ", vision=" + vision +
                '}';
    }
}