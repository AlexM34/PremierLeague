import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Club {
    private final int id;
    private final String name;
    private final int established;
    private final Stadium stadium;
    private final String location;
    private String league;
    private Glory glory;
    private int reputation;
    private int value;
    private int budget;
    private Owner owner;
    private Coach coach;
    private Set<Footballer> footballers;
    private Season season;

    Club(final int id, final String name, final int established, final Stadium stadium, final String location, final String league, final Glory glory,
         final int reputation, final int value, final int budget, final Owner owner, final Coach coach) {
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

    int getId() {
        return id;
    }

    String getName() {
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

    String getLeague() {
        return league;
    }

    void setLeague(final String league) {
        this.league = league;
    }

    Glory getGlory() {
        return glory;
    }

    void setGlory(final Glory glory) {
        this.glory = glory;
    }

    int getReputation() {
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

    int getBudget() {
        return budget;
    }

    void changeBudget(final int change) {
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

    Set<Footballer> getFootballers() {
        return footballers;
    }

    void setFootballers(final Set<Footballer> footballers) {
        this.footballers = footballers;
    }

    void addFootballer(final Footballer footballer) {
        this.footballers.add(footballer);
    }

    void removeFootballer(final Footballer footballer) {
        this.footballers.remove(footballer);
    }

    Season getSeason() {
        return season;
    }

    void setSeason(final Season season) {
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
