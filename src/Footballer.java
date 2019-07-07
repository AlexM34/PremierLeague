import java.util.Objects;

class Footballer {
    // TODO: Add more stats
    private final int id;
    private final String name;
    private int age;
    private final String nationality;
    private int overall;
    private int potential;
    private float value;
    private float wage;
    private Position position;
    private int number;
    private int finishing;
    private int vision;
    private Resume resume;
    private int condition;
    private int ban;

    Footballer(final int id, final String name, final int age, final String nationality, final int overall,
               final int potential, final float value, final float wage, final Position position,
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

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getAge() {
        return age;
    }

    void setAge(final int age) {
        this.age = age;
    }

    String getNationality() {
        return nationality;
    }

    int getOverall() {
        return overall;
    }

    void changeOverall(final int change) {
        this.overall += change;
    }

    int getPotential() {
        return potential;
    }

    void changePotential(final int change) {
        this.potential += change;
    }

    float getValue() {
        return value;
    }

    void setValue(final float value) {
        this.value = value;
    }

    float getWage() {
        return wage;
    }

    void setWage(final float wage) {
        this.wage = wage;
    }

    Position getPosition() {
        return position;
    }

    void setPosition(final Position position) {
        this.position = position;
    }

    int getNumber() {
        return number;
    }

    void setNumber(final int number) {
        this.number = number;
    }

    int getFinishing() {
        return finishing;
    }

    void setFinishing(final int finishing) {
        this.finishing = finishing;
    }

    int getVision() {
        return vision;
    }

    void setVision(final int vision) {
        this.vision = vision;
    }

    Resume getResume() {
        return resume;
    }

    void setResume(final Resume resume) {
        this.resume = resume;
    }

    int getCondition() {
        return condition;
    }

    void changeCondition(final int change) {
        this.condition += change;
        if (this.condition > 100) this.condition = 100;
        else if (this.condition < 0) this.condition = 0;
    }

    int getBan() {
        return ban;
    }

    void changeBan(final int ban) {
        this.ban += ban;
        if (this.ban < 0) this.ban = 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Footballer that = (Footballer) o;
        return id == that.id &&
                age == that.age &&
                overall == that.overall &&
                potential == that.potential &&
                Float.compare(that.value, value) == 0 &&
                Float.compare(that.wage, wage) == 0 &&
                number == that.number &&
                finishing == that.finishing &&
                vision == that.vision &&
                condition == that.condition &&
                ban == that.ban &&
                name.equals(that.name) &&
                nationality.equals(that.nationality) &&
                position == that.position &&
                resume.equals(that.resume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, nationality, overall, potential, value, wage, position, number,
                finishing, vision, resume, condition, ban);
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
                ", condition=" + condition +
                ", ban=" + ban +
                '}';
    }
}