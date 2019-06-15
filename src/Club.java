import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Club {
    private int id;
    private String name;
    private int established;
    private Stadium stadium;
    private String location;
    private String league;
    private Glory glory;
    private int reputation;
    private int value;
    private int budget;
    private Owner owner;
    private Coach coach;
    private Set<Footballer> footballers;
    private Season season;

    Club(int id, String name, int established, Stadium stadium, String location, String league, Glory glory,
                int reputation, int value, int budget, Owner owner, Coach coach) {
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
        this.season = new Season(
                new League(0, 0, 0, 0, 0, 0, 0, 0),
                new Cup(), new Cup(), new ChampionsLeague(), 100, 100);
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

    void setLeague(String league) {
        this.league = league;
    }

    Glory getGlory() {
        return glory;
    }

    void setGlory(Glory glory) {
        this.glory = glory;
    }

    int getReputation() {
        return reputation;
    }

    void setReputation(int reputation) {
        this.reputation = reputation;
    }

    int getValue() {
        return value;
    }

    void setValue(int value) {
        this.value = value;
    }

    int getBudget() {
        return budget;
    }

    void setBudget(int budget) {
        this.budget = budget;
    }

    Owner getOwner() {
        return owner;
    }

    void setOwner(Owner owner) {
        this.owner = owner;
    }

    Coach getCoach() {
        return coach;
    }

    void setCoach(Coach coach) {
        this.coach = coach;
    }

    Set<Footballer> getFootballers() {
        return footballers;
    }

    void setFootballers(Set<Footballer> footballers) {
        this.footballers = footballers;
    }

    void addFootballer(Footballer footballer) {
        this.footballers.add(footballer);
    }

    Season getSeason() {
        return season;
    }

    void setSeason(Season season) {
        this.season = season;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Club club = (Club) o;
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
