package main.player;

import main.simulation.Simulator;

import java.util.Objects;

public class Footballer {
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

    public void increaseAge() {
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
        this.changeOverall(change);
        if (age < 30 && Simulator.isSatisfied(33)) this.changePotential(change);
    }

    private void decline(final int change) {
        this.changeOverall(-change);
        if (age >= 30 || Simulator.isSatisfied(33)) this.changePotential(-change);
    }

    private void retire() {
        System.out.println(name+ "(" + age + ") retires at " + name);
        team = "";
    }

    public String getNationality() {
        return nationality;
    }

    public int getOverall() {
        return overall;
    }

    public void changeOverall(final int change) {
        overall += change;
        overall = Math.max(50, Math.min(99, Math.min(overall, potential)));
    }

    public int getPotential() {
        return potential;
    }

    public void changePotential(final int change) {
        this.potential += change;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(final String team) {
        this.team = team;
    }

    public long getValue() {
        int overall = getOverall() - 70;
        overall = overall > 4 ? overall * overall * overall / 100 : 1;

        float age = getAge() != 28 ? (float) Math.sqrt(Math.abs(28 - getAge())) : 1;
        age = getAge() < 28 ? age : 1 / age;

        float position = getPosition().getAttackingDuty() + 1;
        position = (float) Math.sqrt(position);

        float potential = (float) (getPotential() - getOverall()) / 3;
        potential = potential > 1 ? (float) Math.sqrt(potential) : 1;

        value = (long) (overall * age * position * potential);
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

    public int getScoringChance() {
        final int overall = Math.max(1, this.overall - 60);
        final int finishing = Math.max(1, this.finishing - 30);
        switch (this.position.getRole()) {
            case Goalkeeper: return 1;
            case Defender: return overall * finishing;
            case Midfielder: return overall * finishing * 2;
            case Forward: return overall * finishing * 4;
            default: return 0;
        }
    }

    public int getAssistChance() {
        final int overall = Math.max(1, this.overall - 60);
        final int vision = Math.max(1, this.vision - 30);
        switch (this.position.getRole()) {
            case Goalkeeper: return 10;
            case Defender: return overall * vision;
            case Midfielder: return overall * vision * 5;
            case Forward: return overall * vision * 3;
            default: return 0;
        }
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