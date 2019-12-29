package view;

import players.Footballer;
import simulation.Data;
import team.League;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import java.net.URL;
import java.util.Map;
import java.util.Random;

import static simulation.Data.averageRatings;
import static simulation.Data.awayWins;
import static simulation.Data.draws;
import static simulation.Data.homeWins;
import static simulation.Data.redCards;
import static simulation.Data.scoredAway;
import static simulation.Data.scoredHome;
import static simulation.Data.yellowCards;
import static view.View.decimalFormat;

class Helper {
    private static int song = 0;

    static void playMusic() {
        int r;
        do {
            r = new Random().nextInt(5) + 1;
        } while (r == song);

        song = r;
        final String fileName = "/" + song + ".wav";
        try {
            final URL url = Helper.class.getResource(fileName);
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

    static void setColumnWidths(final JTable table, final int tablePreferredWidth, final double... percentages) {
        double total = 0;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            total += percentages[i];
        }

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth((int)
                    (tablePreferredWidth * (percentages[i] / total)));
        }
    }

    static void setTableValues(final JTable table, final String... values) {
        for (int row = 0; row < values.length; row++) table.setValueAt(values[row], row, 0);
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

    static void displayStats(final JTable table, final Map<Footballer, Integer> map, final boolean format) {
        int row = 0;
        for (final Footballer footballer : map.keySet()) {
            if (row > 9 || map.getOrDefault(footballer, 0) == 0) break;

            table.setValueAt(String.valueOf(row + 1), row, 0);
            table.setValueAt(footballer.getName(), row, 1);

            if (format) {
                table.setValueAt(String.valueOf(decimalFormat.format((float) map.getOrDefault(footballer,
                        0) / 100)), row++, 2);
            } else {
                table.setValueAt(String.valueOf(map.getOrDefault(footballer, 0)), row++, 2);
            }
        }

        for (int i = row; i < 10; i++) {
            table.setValueAt("", i, 0);
            table.setValueAt("", i, 1);
            table.setValueAt("", i, 2);
        }
    }

    static void leagueStats(final JTable table, final String leagueName) {
        final int games = homeWins.getOrDefault(leagueName, 0)
                + draws.getOrDefault(leagueName, 0) + awayWins.getOrDefault(leagueName, 0);

        table.setValueAt(String.valueOf(games), 0, 1);
        table.setValueAt(String.valueOf(homeWins.getOrDefault(leagueName, 0)), 1, 1);
        table.setValueAt(String.valueOf(draws.getOrDefault(leagueName, 0)), 2, 1);
        table.setValueAt(String.valueOf(awayWins.getOrDefault(leagueName, 0)), 3, 1);
        table.setValueAt(String.valueOf(scoredHome.getOrDefault(leagueName, 0)), 4, 1);
        table.setValueAt(String.valueOf(scoredAway.getOrDefault(leagueName, 0)), 5, 1);
        table.setValueAt(String.valueOf(Data.assists.getOrDefault(leagueName, 0)), 6, 1);
        table.setValueAt(String.valueOf(yellowCards.getOrDefault(leagueName, 0)), 7, 1);
        table.setValueAt(String.valueOf(redCards.getOrDefault(leagueName, 0)), 8, 1);
        table.setValueAt(decimalFormat.format(averageRatings.getOrDefault(leagueName, 0.0f) / (games * 22)), 9, 1);
        table.setValueAt(String.valueOf(Data.cleanSheets.getOrDefault(leagueName, 0)), 10, 1);
    }
}
