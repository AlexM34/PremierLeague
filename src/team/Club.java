package team;

import competition.ChampionsLeague;
import competition.Cup;
import players.Footballer;
import players.Glory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Club {
    private final int id;
    private final String name;
    private final int established;
    private final Stadium stadium;
    private final String location;
    private String league;
    private Glory glory;
    private int reputation;
    private int value;
    private float budget;
    private Owner owner;
    private Coach coach;
    private Set<Footballer> footballers;
    private Season season;

    public Club(final int id, final String name, final int established, final Stadium stadium, final String location, final String league, final Glory glory,
                final int reputation, final int value, final float budget, final Owner owner, final Coach coach) {
        this.id = id;
        this.name = name;
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
        this.season = new Season(new League(), new Cup(), new Cup(), new ChampionsLeague(), 100, 100);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    int getEstablished() {
        return established;
    }

    Stadium getStadium() {
        return stadium;
    }

    String getLocation() {
        return location;
    }

    public String getLeague() {
        return league;
    }

    void setLeague(final String league) {
        this.league = league;
    }

    public Glory getGlory() {
        return glory;
    }

    void setGlory(final Glory glory) {
        this.glory = glory;
    }

    public int getReputation() {
        return reputation;
    }

    void setReputation(final int reputation) {
        this.reputation = reputation;
    }

    int getValue() {
        return value;
    }

    void setValue(final int value) {
        this.value = value;
    }

    public float getBudget() {
        return budget;
    }

    public void changeBudget(final float change) {
        this.budget += change;
    }

    Owner getOwner() {
        return owner;
    }

    void setOwner(final Owner owner) {
        this.owner = owner;
    }

    Coach getCoach() {
        return coach;
    }

    void setCoach(final Coach coach) {
        this.coach = coach;
    }

    public Set<Footballer> getFootballers() {
        return footballers;
    }

    void setFootballers(final Set<Footballer> footballers) {
        this.footballers = footballers;
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Club club = (Club) o;
        return id == club.id &&
                established == club.established &&
                reputation == club.reputation &&
                value == club.value &&
                budget == club.budget &&
                name.equals(club.name) &&
                stadium.equals(club.stadium) &&
                location.equals(club.location) &&
                league.equals(club.league) &&
                glory.equals(club.glory) &&
                owner.equals(club.owner) &&
                coach.equals(club.coach) &&
                footballers.equals(club.footballers) &&
                season.equals(club.season);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, established, stadium, location, league, glory, reputation, value, budget, owner, coach, footballers, season);
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
