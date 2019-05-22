import java.util.Objects;

class Coach {
    private int id;
    private String name;
    private int overall;
    private Formation formation;
    private int style;
    private int attack;
    private int defence;

    public Coach(int id, String name, int overall, Formation formation, int style, int attack, int defence) {
        this.id = id;
        this.name = name;
        this.overall = overall;
        this.formation = formation;
        this.style = style;
        this.attack = attack;
        this.defence = defence;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOverall() {
        return overall;
    }

    public Formation getFormation() {
        return formation;
    }

    public int getStyle() {
        return style;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return id == coach.id &&
                overall == coach.overall &&
                style == coach.style &&
                attack == coach.attack &&
                defence == coach.defence &&
                name.equals(coach.name) &&
                formation == coach.formation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, overall, formation, style, attack, defence);
    }

    @Override
    public String toString() {
        return "Coach{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", overall=" + overall +
                ", formation=" + formation +
                ", style=" + style +
                ", attack=" + attack +
                ", defence=" + defence +
                '}';
    }
}