package view;

import simulation.Simulator;
import team.League;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.net.URL;
import java.text.DecimalFormat;

import static simulation.competition.League.leagueAssists;
import static simulation.competition.League.leagueAverageRatings;
import static simulation.competition.League.leagueRedCards;
import static simulation.competition.League.leagueYellowCards;

class Helper {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    private static int song = 0;

    private Helper() {
    }

    static void playMusic() {
        int r;
        do {
            r = Simulator.getInt(5) + 1;
        } while (r == song);

        song = r;
        final String fileName = "/" + song + ".wav";
        try {
            final URL url = Helper.class.getResource("/audio" + fileName);
            final AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);

            final Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            clip.loop(0);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    playMusic();
                }
            });

        } catch (final Exception e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }

    static int getInteger(final String stringValue) {
        int intValue = 0;
        for (char c : stringValue.toCharArray()) {
            intValue *= 10;
            intValue += (c - '0');
        }

        return intValue;
    }

    static void insertStandingsEntry(final JTable table, final String name, final League league, final int row) {
        table.setValueAt(String.valueOf(row + 1), row, 0);
        table.setValueAt(name, row, 1);
        table.setValueAt(String.valueOf(league.getMatches()), row, 2);
        table.setValueAt(String.valueOf(league.getWins()), row, 3);
        table.setValueAt(String.valueOf(league.getDraws()), row, 4);
        table.setValueAt(String.valueOf(league.getLosses()), row, 5);
        table.setValueAt(String.valueOf(league.getScored()), row, 6);
        table.setValueAt(String.valueOf(league.getConceded()), row, 7);
        table.setValueAt(String.valueOf((league.getScored() - league.getConceded())), row, 8);
        table.setValueAt(String.valueOf(league.getPoints()), row, 9);
    }

    static void leagueStats(final JTable table, final String leagueName) {
        table.setValueAt(String.valueOf(League.getLeagueGames(leagueName)), 0, 1);
        table.setValueAt(League.getLeagueHomeWins(leagueName), 1, 1);
        table.setValueAt(League.getLeagueDraws(leagueName), 2, 1);
        table.setValueAt(League.getLeagueAwayWins(leagueName), 3, 1);
        table.setValueAt(League.getLeagueScoredHome(leagueName), 4, 1);
        table.setValueAt(League.getLeagueScoredAway(leagueName), 5, 1);
        table.setValueAt(String.valueOf(leagueAssists.getOrDefault(leagueName, 0)), 6, 1);
        table.setValueAt(String.valueOf(leagueYellowCards.getOrDefault(leagueName, 0)), 7, 1);
        table.setValueAt(String.valueOf(leagueRedCards.getOrDefault(leagueName, 0)), 8, 1);
        table.setValueAt(decimalFormat.format(leagueAverageRatings.getOrDefault(leagueName, 0.0f) /
                (League.getLeagueGames(leagueName) * 22)), 9, 1);
        table.setValueAt(League.getLeagueCleanSheets(leagueName), 10, 1);
    }
}
