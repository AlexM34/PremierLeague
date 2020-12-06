package view;

import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import static java.awt.Image.SCALE_SMOOTH;
import static javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS;

import player.Footballer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Map;

public class View {

    private static final PrintStream STREAM = new PrintStream(new FileOutputStream(FileDescriptor.out));
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    static final String FONT_NAME = "Times New Roman";
    static final int FONT_SIZE = 22;

    static int standingsX;
    static int standingsY;
    static int standingsWidth;
    static int standingsHeight;
    static int standingsRowHeight;
    static int standingsFontSize;
    static int statsWidth;
    static int statsHeight;
    static int statsRowHeight;
    static int statsFontSize;

    public View() {
    }

    static JScrollPane createStatsPane(final JTable table, final int x, final int y, final String label) {
        table.setBounds(x, y, statsWidth, statsHeight);
        table.setRowHeight(statsRowHeight);
        table.setEnabled(false);
        table.setFont(new Font(FONT_NAME, PLAIN, statsFontSize));
        table.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
        setColumnWidths(table, statsWidth, 8, 40, 35, 17);

        final JScrollPane scrollPane = new JScrollPane(table);
        final TitledBorder titledBorder = BorderFactory.createTitledBorder(label);
        titledBorder.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));
        scrollPane.setBorder(titledBorder);
        scrollPane.setBounds(x, y, statsWidth, statsHeight);
        return scrollPane;
    }

    static JButton createButton(final String name, final int x, final int y,
                                     final int width, final int height, final ActionListener action) {

        final JButton button = new JButton(name);
        button.setBounds(x, y, width, height);
        button.setFont(new Font(FONT_NAME, PLAIN, FONT_SIZE));
        button.addActionListener(action);
        return button;
    }

    static JScrollPane createTeamPane(final JTable table, final String label, final double... percentages) {
        table.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);
        table.setRowHeight(standingsRowHeight);
        table.setEnabled(false);
        table.setFont(new Font(FONT_NAME, PLAIN, standingsFontSize));
        setColumnWidths(table, standingsWidth, percentages);

        final JScrollPane pane = new JScrollPane(table);
        final TitledBorder titledBorder = BorderFactory.createTitledBorder(label);
        titledBorder.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));
        pane.setBorder(titledBorder);
        pane.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);
        return pane;
    }

    static void setTableValues(final JTable table, final String... values) {
        for (int row = 0; row < values.length; row++) table.setValueAt(values[row], row, 0);
        for (int row = 0; row < values.length; row++) table.setValueAt("", row, 1);
    }

    static void displayStats(final JTable table, final Map<Footballer, Integer> map, final boolean format) {
        int row = 0;
        for (final Footballer footballer : map.keySet()) {
            if (row > 9 || map.getOrDefault(footballer, 0) == 0) break;

            table.setValueAt(String.valueOf(row + 1), row, 0);
            table.setValueAt(footballer.getName(), row, 1);
            table.setValueAt(footballer.getTeam(), row, 2);

            if (format) {
                table.setValueAt(String.valueOf(decimalFormat.format((float) map.getOrDefault(footballer,
                        0) / 100)), row++, 3);
            } else {
                table.setValueAt(String.valueOf(map.getOrDefault(footballer, 0)), row++, 3);
            }
        }

        for (int i = row; i < 10; i++) {
            table.setValueAt("", i, 0);
            table.setValueAt("", i, 1);
            table.setValueAt("", i, 2);
        }
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

    static JLabel getImage(final String name, final int width, final int height) {
        final BufferedImage image;
        try {
            image = ImageIO.read(View.class.getResource("/images" + name));
        } catch (IOException e) {
            STREAM.println("Exception thrown while extracting images! " + e);
            return null;
        }

        final Image scaledInstance = image.getScaledInstance(width, height, SCALE_SMOOTH);
        final ImageIcon icon = new ImageIcon(scaledInstance);

        final JLabel label = new JLabel();
        label.setIcon(icon);
        label.setSize(width, height);
        label.setLocation(0, 0);

        return label;
    }
}
