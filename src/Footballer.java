import java.util.Objects;

class Footballer {
    private int id;
    private String name;
    private int age;
    private String nationality;
    private int overall;
    private int potential;
    private float value;
    private float wage;
    private Position position;
    private int number;
    private int finishing;
    private int vision;
    private Resume resume;

    Footballer(int id, String name, int age, String nationality, int overall,
               int potential, float value, float wage, Position position,
               int number, int finishing, int vision, Resume resume) {
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
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getAge() {
        return age;
    }

    void setAge(int age) {
        this.age = age;
    }

    String getNationality() {
        return nationality;
    }

    int getOverall() {
        return overall;
    }

    void setOverall(int overall) {
        this.overall = overall;
    }

    int getPotential() {
        return potential;
    }

    void setPotential(int potential) {
        this.potential = potential;
    }

    float getValue() {
        return value;
    }

    void setValue(float value) {
        this.value = value;
    }

    float getWage() {
        return wage;
    }

    void setWage(float wage) {
        this.wage = wage;
    }

    Position getPosition() {
        return position;
    }

    void setPosition(Position position) {
        this.position = position;
    }

    int getNumber() {
        return number;
    }

    void setNumber(int number) {
        this.number = number;
    }

    int getFinishing() {
        return finishing;
    }

    void setFinishing(int finishing) {
        this.finishing = finishing;
    }

    int getVision() {
        return vision;
    }

    void setVision(int vision) {
        this.vision = vision;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Footballer that = (Footballer) o;
        return id == that.id &&
                age == that.age &&
                overall == that.overall &&
                potential == that.potential &&
                Float.compare(that.value, value) == 0 &&
                Float.compare(that.wage, wage) == 0 &&
                number == that.number &&
                finishing == that.finishing &&
                vision == that.vision &&
                name.equals(that.name) &&
                nationality.equals(that.nationality) &&
                position.equals(that.position) &&
                resume.equals(that.resume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, nationality, overall, potential, value, wage, position, number, finishing, vision, resume);
    }

    @Override
    public String toString() {
        return "Footballer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", nationality='" + nationality + '\'' +
                ", overall=" + overall +
                ", potential=" + potential +
                ", value=" + value +
                ", wage=" + wage +
                ", position=" + position +
                ", number=" + number +
                ", finishing=" + finishing +
                ", vision=" + vision +
                ", resume=" + resume +
                '}';
    }
}