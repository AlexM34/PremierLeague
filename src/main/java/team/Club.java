package team;

import static java.nio.charset.StandardCharsets.UTF_8;

import player.Footballer;
import player.Glory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Club {

    private final int id;
    private final String name;
    private final int established;
    private final Stadium stadium;
    private final String location;
    private final String league;
    private final Glory glory;
    private int reputation;
    private final int value;
    private float budget;
    private final Owner owner;
    private final Coach coach;
    private final Set<Footballer> footballers;
    private Season season;

    public Club(final int id, final String name, final int established, final Stadium stadium, final String location,
                final String league, final Glory glory, final int reputation, final int value, final float budget,
                final Owner owner, final Coach coach) {
        this.id = id;
        this.name = new String(name.getBytes(), UTF_8);
        this.established = established;
        this.stadium = stadium;
        this.location = location;
        this.league = league;
        this.glory = glory;
        this.reputation = reputation;
        this.value = value;
        this.budget = budget;
        this.owner = owner;
        this.coach = coach;
        this.footballers = new HashSet<>();
        this.season = new Season(league);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLeague() {
        return league;
    }

    public Glory getGlory() {
        return glory;
    }

    public int getReputation() {
        return reputation;
    }

    public void changeReputation(final int reputation) {
        System.out.println("Reputation of " + this.name + " - from " + this.reputation + " with " + (reputation - this.reputation) / 3);
        this.reputation += (reputation - this.reputation) / 3;
    }

    public float getBudget() {
        return budget;
    }

    public void changeBudget(final float change) {
        this.budget += change;
    }

    public Set<Footballer> getFootballers() {
        return footballers;
    }

    public void addFootballer(final Footballer footballer) {
        this.footballers.add(footballer);
    }

    public void removeFootballer(final Footballer footballer) {
        this.footballers.remove(footballer);
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(final Season season) {
        this.season = season;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Club)) return false;
        final Club club = (Club) o;
        return id == club.id &&
                established == club.established &&
                reputation == club.reputation &&
                value == club.value &&
                Float.compare(club.budget, budget) == 0 &&
                Objects.equals(name, club.name) &&
                Objects.equals(stadium, club.stadium) &&
                Objects.equals(location, club.location) &&
                Objects.equals(league, club.league) &&
                Objects.equals(glory, club.glory) &&
                Objects.equals(owner, club.owner) &&
                Objects.equals(coach, club.coach) &&
                Objects.equals(footballers, club.footballers) &&
                Objects.equals(season, club.season);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, name, established, stadium, location, league, glory,
                reputation, value, budget, owner, coach, footballers, season);
    }

    @Override
    public String toString() {
        return "Club{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", established=" + established +
                ", stadium=" + stadium +
                ", location='" + location + '\'' +
                ", league='" + league + '\'' +
                ", glory=" + glory +
                ", reputation=" + reputation +
                ", value=" + value +
                ", budget=" + budget +
                ", owner=" + owner +
                ", coach=" + coach +
                ", footballers=" + footballers +
                ", season=" + season +
                '}';
    }
}
