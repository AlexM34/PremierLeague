package player;

import simulation.Simulator;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Objects;

public class Footballer {

    private static final PrintStream STREAM = new PrintStream(new FileOutputStream(FileDescriptor.out));

    private final int id;
    private final String name;
    private int age;
    private final String nationality;
    private int overall;
    private int potential;
    private String team;
    private long value;
    private final long wage;
    private final Position position;
    private final int number;
    private final int finishing;
    private final int vision;
    private final Resume resume;
    private int condition;
    private int ban;

    public Footballer(final int id, final String name, final int age, final String nationality, final int overall,
                      final int potential, final String team, final long value, final long wage, final Position position,
                      final int number, final int finishing, final int vision, final Resume resume) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.nationality = nationality;
        this.overall = overall;
        this.potential = potential;
        this.team = team;
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

    public void incrementAge() {
        age++;

        if (this.number > 100) return;
        this.changeCondition(100);

        if (Simulator.isSatisfied(50)) {
            final int rating = resume.getSeason().getLeague().getRating();
            if (rating > 750) improve(1);
            else if (rating < 650) decline(1);
        }

        final int r = Simulator.getInt(6);
        if (r == 0) improve(1);
        else if (r == 1) decline(1);

        if (overall + 10 < potential && Simulator.isSatisfied(50)) improve(2);

        if (age < 20) improve(1 + Simulator.getInt(3));

        if (age < 25 && Simulator.isSatisfied(33)) improve(2);

        if (age > 30 && Simulator.isSatisfied(50)) decline(2);

        if (age > 34) {
            if (Simulator.isSatisfied(50)) decline(2);
            if (Simulator.isSatisfied(1, 42 - Math.min(age, 41))) retire();
        }
    }

    private void improve(final int change) {
        if (age < 30 && Simulator.isSatisfied(33)) this.changePotential(change);
        this.changeOverall(change);
    }

    private void decline(final int change) {
        this.changeOverall(-change);
        if (age >= 30 || Simulator.isSatisfied(33)) this.changePotential(-change);
    }

    private void retire() {
        STREAM.println(name + "(" + age + ") retires at " + team);
        team = "";
    }

    public String getNationality() {
        return nationality;
    }

    public int getOverall() {
        return overall;
    }

    public void changeOverall(final int change) {
        overall = Math.min(99, Math.max(50, Math.min(potential, overall + change)));
    }

    public int getPotential() {
        return potential;
    }

    public void changePotential(final int change) {
        this.potential = Math.min(99, Math.max(50, Math.max(overall, potential + change)));
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(final String team) {
        this.team = team;
    }

    public long getValue() {
        float overallFactor = (float) (this.overall - 70) / 3;
        overallFactor = overallFactor > 1 ? overallFactor * overallFactor : 1;

        float ageFactor = (float) Math.cbrt(Math.abs(28 - this.age) + 1);
        ageFactor = this.age < 28 ? ageFactor : 1 / ageFactor;

        float positionFactor = this.position.getAttackingDuty() + 3;
        positionFactor = (float) Math.sqrt(positionFactor);

        float potentialFactor = (float) (this.potential - this.overall) / 2;
        potentialFactor = potentialFactor > 1 ? (float) Math.sqrt(potentialFactor) : 1;

        value = (long) (overallFactor * ageFactor * positionFactor * potentialFactor);
        return value;
    }

    public float getWage() {
        return wage;
    }

    public Position getPosition() {
        return position;
    }

    public int getNumber() {
        return number;
    }

    public int getFinishing() {
        return finishing;
    }

    public int getVision() {
        return vision;
    }

    public Resume getResume() {
        return resume;
    }

    public int getCondition() {
        return condition;
    }

    public void changeCondition(final int change) {
        this.condition = Math.min(100, Math.max(50, this.condition + change));
    }

    public int getBan() {
        return ban;
    }

    public void changeBan(final int change) {
        this.ban = Math.max(0, this.ban + change);
    }

    public boolean canPlay() {
        return this.position != null && this.condition > 70 && this.ban == 0;
    }

    public int getScoringChance() {
        final int overallFactor = Math.max(1, this.overall / 2 - 30);
        final int finishingFactor = Math.max(1, this.finishing / 3 - 15);
        final int positionFactor = this.position.getAttackingDuty();

        return (overallFactor * overallFactor + finishingFactor * finishingFactor + positionFactor * positionFactor) * positionFactor;
    }

    public int getAssistChance() {
        final int overallFactor = Math.max(1, this.overall / 2 - 30);
        final int visionFactor = Math.max(1, this.vision / 3 - 15);

        final int positionFactor;
        switch (this.position.getAttackingDuty()) {
            case 0: positionFactor = 1; break;
            case 1: positionFactor = 2; break;
            case 2: positionFactor = 5; break;
            case 3: positionFactor = 9; break;
            case 4: positionFactor = 11; break;
            case 5: positionFactor = 8; break;
            default: return 0;
        }

        return (overallFactor * overallFactor + visionFactor * visionFactor + positionFactor * positionFactor) * positionFactor;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Footballer)) return false;
        final Footballer that = (Footballer) o;
        return id == that.id &&
                age == that.age &&
                overall == that.overall &&
                potential == that.potential &&
                value == that.value &&
                wage == that.wage &&
                number == that.number &&
                finishing == that.finishing &&
                vision == that.vision &&
                condition == that.condition &&
                ban == that.ban &&
                position == that.position &&
                Objects.equals(name, that.name) &&
                Objects.equals(nationality, that.nationality) &&
                Objects.equals(team, that.team) &&
                Objects.equals(resume, that.resume);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, name, age, nationality, overall, potential, team, value, wage, position, number, finishing, vision, resume, condition, ban);
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
                ", team='" + team + '\'' +
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