package simulation.match;

import player.Footballer;
import player.MatchStats;
import team.Formation;

import java.util.List;

public class Lineup {

    private final Selection squad;
    private final Selection bench;

    Lineup(final Formation formation) {
        this.squad = new Selection(formation);
        this.bench = new Selection(2, 2, 2);
    }

    void add(final Footballer footballer) {
        if (!squad.add(footballer)) bench.add(footballer);
    }

    List<MatchStats> getSquad() {
        return squad.getSelection();
    }

    List<MatchStats> getBench() {
        return bench.getSelection();
    }

    void print() {
        System.out.println();
        System.out.println("SQUAD");
        squad.getSelection().forEach(x ->
                System.out.println(x.getFootballer().getPosition().getAttackingDuty() + ", " + x));

        System.out.println();
        System.out.println("BENCH");
        bench.getSelection().forEach(x ->
                System.out.println(x.getFootballer().getPosition().getAttackingDuty() + ", " + x));
        System.out.println();
    }

    Footballer getPlayer(final int index) {
        return this.squad.getSelection().get(index).getFootballer();
    }

    MatchStats getStats(final int index) {
        return this.squad.getSelection().get(index);
    }
}
