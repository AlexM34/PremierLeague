package team;

import java.util.Objects;

public class Coach {
    private final int id;
    private final String name;
    private final int overall;
    private final Formation formation;
    private final int style;
    private final int attack;
    private final int defence;

    public Coach(final int id, final String name, final int overall, final Formation formation, final int style, final int attack, final int defence) {
        this.id = id;
        this.name = name;
        this.overall = overall;
        this.formation = formation;
        this.style = style;
        this.attack = attack;
        this.defence = defence;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Coach coach = (Coach) o;
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