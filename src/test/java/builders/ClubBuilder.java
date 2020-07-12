package builders;

import player.Glory;
import team.Club;
import team.Coach;
import team.Formation;
import team.Owner;
import team.Stadium;

public class ClubBuilder {

    private static final int ID = 0;
    private static final String NAME = "Club";
    private static final int ESTABLISHED = 1886;
    private static final Stadium STADIUM = new Stadium(1, "Stadium", 1970,
            "City", 60000, 70);
    private static final String LOCATION = "City";
    private static final String LEAGUE = "Premier";
    private static final Glory GLORY = new Glory(0, 0, 0, 0,
            0, 0, 0, 0);
    private static final int REPUTATION = 80;
    private static final int VALUE = 30;
    private static final float BUDGET = 20;
    private static final Owner OWNER = new Owner(99, "Boss", "French", 150, 95);
    private static final Coach COACH = new Coach(813, "Coach", 85, Formation.F5,
            60, 80, 70);

    private int id = ID;

    public ClubBuilder withId(final int id) {
        this.id = id;
        return this;
    }

    public Club build() {
        return new Club(id, NAME, ESTABLISHED, STADIUM, LOCATION, LEAGUE, GLORY,
                REPUTATION, VALUE, BUDGET, OWNER, COACH);
    }

}
