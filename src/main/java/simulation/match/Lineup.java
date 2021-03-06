package simulation.match;

import player.Footballer;
import player.MatchStats;
import team.Formation;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

public class Lineup {

    private static final PrintStream STREAM = new PrintStream(new FileOutputStream(FileDescriptor.out));

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
        return squad.getTeamSheet();
    }

    List<MatchStats> getBench() {
        return bench.getTeamSheet();
    }

    MatchStats getStats(final int index) {
        return this.squad.getTeamSheet().get(index);
    }

    void print() {
        STREAM.println();
        STREAM.println("SQUAD");
        squad.getTeamSheet().forEach(x ->
                STREAM.println(x.getFootballer().getPosition().getAttackingDuty() + ", " + x));

        STREAM.println();
        STREAM.println("BENCH");
        bench.getTeamSheet().forEach(x ->
                STREAM.println(x.getFootballer().getPosition().getAttackingDuty() + ", " + x));
        STREAM.println();
    }
}
