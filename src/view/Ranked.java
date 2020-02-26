package view;

import player.Footballer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import static simulation.dynamics.Postseason.topPlayers;
import static view.ViewManager.historicalView;
import static view.ViewManager.rankedView;
import static view.ViewManager.seasonalView;

class Ranked extends View {
    private static final String[] FOOTBALLERS = {"N", "FOOTBALLER", "AGE", "TEAM", "OVERALL", "POTENTIAL"};
    private static final String[] RECORD = {"", ""};
    private static final JTable FOOTBALLERS_TABLE = new JTable(new String[100][6], FOOTBALLERS);
    private static final JTable FOOTBALLER_RECORD = new JTable(new String[12][2], RECORD);

    private static List<Footballer> topPlayers = new ArrayList<>();
    private static int width;
    private static int height;
    private static int recordX;
    private static int recordWidth;
    private static int recordHeight;
    private static int recordRowHeight;
    private static int buttonX;
    private static int historicalButtonY;
    private static int seasonalButtonY;
    private static int buttonWidth;
    private static int buttonHeight;

    static void setup(final int frameWidth, final int frameHeight) {
        calculatePositions(frameWidth, frameHeight);
        setTable();
        setRecord();
        setButtons();
        loadImage();
    }

    private static void setTable() {
        final JScrollPane footballersPane = createTeamPane(FOOTBALLERS_TABLE, "RANKINGS",
                7, 25, 10, 30, 14, 14);
        FOOTBALLERS_TABLE.setEnabled(true);
        FOOTBALLERS_TABLE.getSelectionModel().addListSelectionListener(e ->
                setPlayerRecord(topPlayers.get(FOOTBALLERS_TABLE.getSelectedRow())));
        rankedView.add(footballersPane);
    }

    private static void setRecord() {
        FOOTBALLER_RECORD.setBounds(recordX, standingsY, recordWidth, recordHeight);
        FOOTBALLER_RECORD.setRowHeight(recordRowHeight);
        FOOTBALLER_RECORD.setEnabled(false);
        FOOTBALLER_RECORD.setFont(new Font(FONT_NAME, PLAIN, standingsFontSize));
        setColumnWidths(FOOTBALLER_RECORD, standingsWidth, 30, 70);

        final JScrollPane recordPane = new JScrollPane(FOOTBALLER_RECORD);
        final TitledBorder borderRecord = BorderFactory.createTitledBorder("RECORD");
        borderRecord.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));
        recordPane.setBorder(borderRecord);
        recordPane.setBounds(recordX, standingsY, recordWidth, recordHeight);
        rankedView.add(recordPane);
    }

    private static void setButtons() {
        addButton("History", buttonX, historicalButtonY, buttonWidth, buttonHeight, e -> historicalView());
        addButton("Season", buttonX, seasonalButtonY, buttonWidth, buttonHeight, e -> seasonalView());
    }

    private static void addButton(final String name, final int x, final int y,
                                  final int width, final int height, final ActionListener action) {

        final JButton button = createButton(name, x, y, width, height, action);
        rankedView.add(button);
    }

    private static void loadImage() {
        rankedView.add(getImage("/footballers.jpg", width, height));
    }

    static void update() {
        topPlayers = topPlayers();
        int row = 0;
        for (final Footballer footballer : topPlayers) {
            FOOTBALLERS_TABLE.setValueAt(String.valueOf(row + 1), row, 0);
            FOOTBALLERS_TABLE.setValueAt(footballer.getName(), row, 1);
            FOOTBALLERS_TABLE.setValueAt(String.valueOf(footballer.getAge()), row, 2);
            FOOTBALLERS_TABLE.setValueAt(footballer.getTeam(), row, 3);
            FOOTBALLERS_TABLE.setValueAt(String.valueOf(footballer.getOverall()), row, 4);
            FOOTBALLERS_TABLE.setValueAt(String.valueOf(footballer.getPotential()), row, 5);

            if (++row == 100) break;
        }

        for (int i = row; i < 100; i++) {
            FOOTBALLERS_TABLE.setValueAt("", i, 0);
            FOOTBALLERS_TABLE.setValueAt("", i, 1);
            FOOTBALLERS_TABLE.setValueAt("", i, 2);
            FOOTBALLERS_TABLE.setValueAt("", i, 3);
            FOOTBALLERS_TABLE.setValueAt("", i, 4);
            FOOTBALLERS_TABLE.setValueAt("", i, 5);
        }

        setTableValues(FOOTBALLER_RECORD, "Name", "Age", "Nationality", "Overall", "Potential", "Team",
                "Value", "Wage", "Position", "Number", "Finishing", "Vision");
    }

    private static void setPlayerRecord(final Footballer footballer) {
        FOOTBALLER_RECORD.setValueAt(footballer.getName(), 0, 1);
        FOOTBALLER_RECORD.setValueAt(String.valueOf(footballer.getAge()), 1, 1);
        FOOTBALLER_RECORD.setValueAt(footballer.getNationality(), 2, 1);
        FOOTBALLER_RECORD.setValueAt(String.valueOf(footballer.getOverall()), 3, 1);
        FOOTBALLER_RECORD.setValueAt(String.valueOf(footballer.getPotential()), 4, 1);
        FOOTBALLER_RECORD.setValueAt(footballer.getTeam(), 5, 1);
        FOOTBALLER_RECORD.setValueAt(String.valueOf(footballer.getValue()), 6, 1);
        FOOTBALLER_RECORD.setValueAt(String.valueOf(footballer.getWage()), 7, 1);
        FOOTBALLER_RECORD.setValueAt(String.valueOf(footballer.getPosition()), 8, 1);
        FOOTBALLER_RECORD.setValueAt(String.valueOf(footballer.getNumber()), 9, 1);
        FOOTBALLER_RECORD.setValueAt(String.valueOf(footballer.getFinishing()), 10, 1);
        FOOTBALLER_RECORD.setValueAt(String.valueOf(footballer.getVision()), 11, 1);
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
        historicalButtonY = 4 * height / 5;
        seasonalButtonY = 9 * height / 10;
        buttonWidth = width / 12;
        buttonHeight = height / 12;
    }
}
