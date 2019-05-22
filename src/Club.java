import java.util.Arrays;
import java.util.Objects;

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
    private Footballer[] footballers;

    public Club(int id, String name, int established, Stadium stadium, String location, String league, Glory glory,
                int reputation, int value, int budget, Owner owner, Coach coach, Footballer[] footballers) {
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
        this.footballers = footballers;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getEstablished() {
        return established;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public String getLocation() {
        return location;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public Glory getGlory() {
        return glory;
    }

    public void setGlory(Glory glory) {
        this.glory = glory;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public Footballer[] getFootballers() {
        return footballers;
    }

    public void setFootballers(Footballer[] footballers) {
        this.footballers = footballers;
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
                Arrays.equals(footballers, club.footballers);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, established, stadium, location, league, glory, reputation, value, budget, owner, coach);
        result = 31 * result + Arrays.hashCode(footballers);
        return result;
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
                ", footballers=" + Arrays.toString(footballers) +
                '}';
    }
}