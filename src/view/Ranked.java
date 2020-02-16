package view;

import player.Footballer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import static simulation.Postseason.topPlayers;
import static view.Helper.getImage;
import static view.Helper.setColumnWidths;
import static view.Helper.setTableValues;
import static view.View.seasonalView;
import static view.View.rankedView;

class Ranked {
    private static final String FONT_NAME = "Times New Roman";
    private static final int FONT_SIZE = 22;
    private static final String[] FOOTBALLERS = {"N", "FOOTBALLER", "AGE", "TEAM", "OVERALL", "POTENTIAL"};
    private static final String[] RECORD = {"", ""};
    private static final JTable footballersTable = new JTable(new String[100][6], FOOTBALLERS);
    private static final JTable footballerRecord = new JTable(new String[12][2], RECORD);
    private static List<Footballer> topPlayers = new ArrayList<>();

    private static int width;
    private static int height;
    private static int recordX;
    private static int recordWidth;
    private static int recordHeight;
    private static int recordRowHeight;
    private static int standingsX;
    private static int standingsY;
    private static int standingsWidth;
    private static int standingsHeight;
    private static int standingsRowHeight;
    private static int standingsFontSize;
    private static int buttonX;
    private static int buttonWidth;
    private static int buttonHeight;

    static void setup(final int frameWidth, final int frameHeight) {
        calculatePositions(frameWidth, frameHeight);

        footballersTable.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);
        footballersTable.setRowHeight(standingsRowHeight);
        footballersTable.setFont(new Font(FONT_NAME, PLAIN, standingsFontSize));
        footballersTable.getSelectionModel().addListSelectionListener(e ->
                playerRecord(topPlayers.get(footballersTable.getSelectedRow())));
        setColumnWidths(footballersTable, standingsWidth, 7, 25, 10, 30, 14, 14);

        final JScrollPane footballersPane = new JScrollPane(footballersTable);
        final TitledBorder borderFootballers = BorderFactory.createTitledBorder("RANKINGS");
        borderFootballers.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));
        footballersPane.setBorder(borderFootballers);
        footballersPane.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);
        rankedView.add(footballersPane);

        footballerRecord.setBounds(recordX, standingsY, recordWidth, recordHeight);
        footballerRecord.setRowHeight(recordRowHeight);
        footballerRecord.setEnabled(false);
        footballerRecord.setFont(new Font(FONT_NAME, PLAIN, standingsFontSize));
        setColumnWidths(footballerRecord, standingsWidth, 30, 70);

        final JScrollPane recordPane = new JScrollPane(footballerRecord);
        final TitledBorder borderRecord = BorderFactory.createTitledBorder("RECORD");
        borderRecord.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));
        recordPane.setBorder(borderRecord);
        recordPane.setBounds(recordX, standingsY, recordWidth, recordHeight);
        rankedView.add(recordPane);

        final JButton historicalButton = new JButton("History");
        historicalButton.setBounds(buttonX, 4 * height / 5, buttonWidth, buttonHeight);
        historicalButton.setFont(new Font(FONT_NAME, PLAIN, FONT_SIZE));
        historicalButton.addActionListener(e -> View.historyView());
        rankedView.add(historicalButton);

        final JButton seasonalButton = new JButton("Season");
        seasonalButton.setBounds(buttonX, 9 * height / 10, buttonWidth, buttonHeight);
        seasonalButton.setFont(new Font(FONT_NAME, PLAIN, FONT_SIZE));
        seasonalButton.addActionListener(e -> seasonalView());
        rankedView.add(seasonalButton);

        try {
            rankedView.add(getImage("/footballers.jpg", width, height));
        } catch (final IOException e) {
            System.out.println("Exception thrown while extracting images! " + e);
        }
    }

    static void update() {
        topPlayers = topPlayers();
        int row = 0;
        for (final Footballer footballer : topPlayers) {
            footballersTable.setValueAt(String.valueOf(row + 1), row, 0);
            footballersTable.setValueAt(footballer.getName(), row, 1);
            footballersTable.setValueAt(String.valueOf(footballer.getAge()), row, 2);
            footballersTable.setValueAt(footballer.getTeam(), row, 3);
            footballersTable.setValueAt(String.valueOf(footballer.getOverall()), row, 4);
            footballersTable.setValueAt(String.valueOf(footballer.getPotential()), row, 5);

            if (++row == 100) break;
        }

        for (int i = row; i < 100; i++) {
            footballersTable.setValueAt("", i, 0);
            footballersTable.setValueAt("", i, 1);
            footballersTable.setValueAt("", i, 2);
            footballersTable.setValueAt("", i, 3);
            footballersTable.setValueAt("", i, 4);
            footballersTable.setValueAt("", i, 5);
        }

        setTableValues(footballerRecord, "Name", "Age", "Nationality", "Overall", "Potential", "Team",
                "Value", "Wage", "Position", "Number", "Finishing", "Vision");
    }

    private static void playerRecord(final Footballer footballer) {
        footballerRecord.setValueAt(footballer.getName(), 0, 1);
        footballerRecord.setValueAt(String.valueOf(footballer.getAge()), 1, 1);
        footballerRecord.setValueAt(footballer.getNationality(), 2, 1);
        footballerRecord.setValueAt(String.valueOf(footballer.getOverall()), 3, 1);
        footballerRecord.setValueAt(String.valueOf(footballer.getPotential()), 4, 1);
        footballerRecord.setValueAt(footballer.getTeam(), 5, 1);
        footballerRecord.setValueAt(String.valueOf(footballer.getValue()), 6, 1);
        footballerRecord.setValueAt(String.valueOf(footballer.getWage()), 7, 1);
        footballerRecord.setValueAt(String.valueOf(footballer.getPosition()), 8, 1);
        footballerRecord.setValueAt(String.valueOf(footballer.getNumber()), 9, 1);
        footballerRecord.setValueAt(String.valueOf(footballer.getFinishing()), 10, 1);
        footballerRecord.setValueAt(String.valueOf(footballer.getVision()), 11, 1);
    }

    private static void calculatePositions(final int frameWidth, final int frameHeight) {
        width = frameWidth;
        height = frameHeight;
        recordX = width / 2;
        recordWidth = width / 5;
        recordHeight = 2 * height / 3;
        recordRowHeight = 2 * recordHeight / 27;
        standingsX = width / 50;
        standingsY = height / 10;
        standingsWidth = 9 * width / 20;
        standingsHeight = 4 * height / 5;
        standingsRowHeight = standingsHeight / 25;
        standingsFontSize = standingsHeight / 40;
        buttonX = 7 * width / 8;
        buttonWidth = width / 12;
        buttonHeight = height / 12;
    }
}
