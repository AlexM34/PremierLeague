package simulation.match;

import player.Footballer;
import player.MatchStats;
import player.Position;
import team.Formation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Selection {

    private final List<MatchStats> teamSheet;
    private final Map<Position.Role, Integer> available;

    Selection(final Formation formation) {
        this(formation.getDefenders(), formation.getMidfielders(), formation.getForwards());
    }

    Selection(final int defenders, final int midfielders, final int forwards) {
        this.teamSheet = new ArrayList<>();
        this.available = new EnumMap<>(Position.Role.class);

        this.available.put(Position.Role.GOALKEEPER, 1);
        this.available.put(Position.Role.DEFENDER, defenders);
        this.available.put(Position.Role.MIDFIELDER, midfielders);
        this.available.put(Position.Role.FORWARD, forwards);
    }

    List<MatchStats> getTeamSheet() {
        return teamSheet;
    }

    boolean add(final Footballer footballer) {
        final Position.Role role = footballer.getPosition().getRole();
        final long alreadySelected = this.teamSheet.stream()
                .filter(f -> f.getFootballer().getPosition().getRole() == role).count();

        if (this.available.get(role) == alreadySelected) return false;

        int index = 0;
        for (int i = this.teamSheet.size() - 1; i >= 0; i--) {
            if (this.teamSheet.get(i).getFootballer().getPosition().getAttackingDuty() <=
                    footballer.getPosition().getAttackingDuty()) {
                index = i + 1;
                break;
            }
        }

        this.teamSheet.add(index, new MatchStats(footballer));
        return true;
    }

}
