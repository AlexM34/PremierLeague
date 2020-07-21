package simulation.match;

import player.Footballer;
import player.MatchStats;
import player.Position;
import team.Formation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Selection {

    private final List<MatchStats> selection;
    private final Map<Position.Role, Integer> available;

    Selection(final Formation formation) {
        this(formation.getDefenders(), formation.getMidfielders(), formation.getForwards());
    }

    Selection(final int defenders, final int midfielders, final int forwards) {
        this.selection = new ArrayList<>();
        this.available = new HashMap<>();

        this.available.put(Position.Role.Goalkeeper, 1);
        this.available.put(Position.Role.Defender, defenders);
        this.available.put(Position.Role.Midfielder, midfielders);
        this.available.put(Position.Role.Forward, forwards);
    }

    List<MatchStats> getSelection() {
        return selection;
    }

    boolean add(final Footballer footballer) {
        final Position.Role role = footballer.getPosition().getRole();
        final int available = this.available.get(role);
        final long count = this.selection.stream()
                .filter(f -> f.getFootballer().getPosition().getRole() == role).count();

        if (available == count) return false;

        int index = 0;
        for (int i = this.selection.size() - 1; i >= 0; i--) {
            if (this.selection.get(i).getFootballer().getPosition().getAttackingDuty() <=
                    footballer.getPosition().getAttackingDuty()) {
                index = i + 1;
                break;
            }
        }

        this.selection.add(index, new MatchStats(footballer));
        return true;
    }
}
