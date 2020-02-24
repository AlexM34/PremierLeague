package view;

import league.LeagueManager;
import team.Club;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.io.IOException;
import java.util.Map;

import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import static javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS;
import static league.LeagueManager.getClubs;
import static simulation.competition.League.CHAMPIONS_LEAGUE_NAME;
import static simulation.competition.League.EUROPA_LEAGUE_NAME;
import static simulation.dynamics.Postseason.allTime;
import static simulation.dynamics.Postseason.playerStats;
import static simulation.dynamics.Postseason.goals;
import static simulation.dynamics.Postseason.assists;
import static simulation.dynamics.Postseason.ratings;
import static simulation.dynamics.Postseason.cleanSheets;
import static view.Helper.displayStats;
import static view.Helper.getImage;
import static view.Helper.setColumnWidths;
import static view.View.historicalView;
import static view.View.rankedView;
import static view.View.seasonalView;

class Historical {
    private static final String FONT_NAME = "Times New Roman";
    private static final int FONT_SIZE = 22;
    private static final String[] STATS = {"N", "PLAYER", "TEAM", "COUNT"};
    private static final String[] TROPHIES = {"N", "TEAM", "TROPHIES"};
    private static final String[] COMPETITIONS = {"League", "League Cup", "National Cup"};
    private static final JComboBox<String> nationBox = new JComboBox<>(LeagueManager.getLeagues());
    private static final JComboBox<String> competitionBox = new JComboBox<>(COMPETITIONS);
    private static final JTable trophiesTable = new JTable(new String[30][3], TROPHIES);

    private static int width;
    private static int height;
    private static int goalsX;
    private static int goalsY;
    private static int assistsY;
    private static int cleanSheetsX;
    private static int statsWidth;
    private static int statsHeight;
    private static int statsRowHeight;
    private static int statsFontSize;
    private static int standingsX;
    private static int standingsY;
    private static int standingsWidth;
    private static int standingsHeight;
    private static int standingsRowHeight;
    private static int standingsFontSize;
    private static int boxY;
    private static int boxHeight;
    private static int boxFontSize;
    private static int buttonY;
    private static int buttonHeight;

    private static final JTable goalsTable = new JTable(new String[10][4], STATS);
    private static final JTable assistsTable = new JTable(new String[10][4], STATS);
    private static final JTable ratingsTable = new JTable(new String[10][4], STATS);
    private static final JTable cleanSheetsTable = new JTable(new String[10][4], STATS);

    static void setup(final int frameWidth, final int frameHeight) {
        calculatePositions(frameWidth, frameHeight);

        placeBox(nationBox, 17 * width / 40, width / 9);
        placeBox(competitionBox, 23 * width / 40, width / 11);

        setStatsTable(goalsTable, goalsX, goalsY, "GOALS");
        setStatsTable(assistsTable, goalsX, assistsY, "ASSISTS");
        setStatsTable(ratingsTable, cleanSheetsX, goalsY, "RATINGS");
        setStatsTable(cleanSheetsTable, cleanSheetsX, assistsY, "CLEAN SHEETS");

        trophiesTable.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);
        trophiesTable.setRowHeight(standingsRowHeight);
        trophiesTable.setEnabled(false);
        trophiesTable.setFont(new Font(FONT_NAME, PLAIN, standingsFontSize));
        setColumnWidths(trophiesTable, standingsWidth, 7, 60, 33);

        final JScrollPane trophiesPane = new JScrollPane(trophiesTable);
        final TitledBorder borderTrophies = BorderFactory.createTitledBorder("TROPHIES");
        borderTrophies.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));
        trophiesPane.setBorder(borderTrophies);
        trophiesPane.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);
        historicalView.add(trophiesPane);

        final JButton seasonalButton = new JButton("Season");
        seasonalButton.setBounds(7 * width / 8, buttonY, width / 12, buttonHeight);
        seasonalButton.setFont(new Font(FONT_NAME, PLAIN, FONT_SIZE));
        seasonalButton.addActionListener(e -> seasonalView());
        historicalView.add(seasonalButton);

        final JButton rankedButton = new JButton("Rankings");
        rankedButton.setBounds(3 * width / 4, 9 * height / 10, width / 10, buttonHeight);
        rankedButton.setFont(new Font(FONT_NAME, PLAIN, FONT_SIZE));
        rankedButton.addActionListener(e -> rankedView());
        historicalView.add(rankedButton);

        try {
            historicalView.add(getImage("/history.jpg", width, height));
        } catch (final IOException e) {
            System.out.println("Exception thrown while extracting images! " + e);
        }
    }

    static void update() {
        final String nation = String.valueOf(nationBox.getSelectedItem());
        final boolean international = nation.equals(CHAMPIONS_LEAGUE_NAME) || nation.equals(EUROPA_LEAGUE_NAME);
        if (international) competitionBox.setVisible(false);
        else competitionBox.setVisible(true);

        final Club[] league = getClubs(nation);
        int competition;
        switch(String.valueOf(competitionBox.getSelectedItem())) {
            case "League": competition = 0; break;
            case "National Cup": competition = 1; break;
            case "League Cup": competition = 2; break;
            default: competition = -1;
        }

        if (international) competition = nation.equals(CHAMPIONS_LEAGUE_NAME) ? 3 : 4;
        final Map<String, Integer> winners = allTime(league, competition);
        int row = 0;
        for (final String club : winners.keySet()) {
            trophiesTable.setValueAt(String.valueOf(row + 1), row, 0);
            trophiesTable.setValueAt(club, row, 1);
            trophiesTable.setValueAt(String.valueOf(winners.get(club)), row++, 2);
        }

        for (int i = row; i < 30; i++) {
            trophiesTable.setValueAt("", i, 0);
            trophiesTable.setValueAt("", i, 1);
            trophiesTable.setValueAt("", i, 2);
        }

        playerStats(league, competition, false);
        displayStats(goalsTable, goals, false);
        displayStats(assistsTable, assists, false);
        displayStats(ratingsTable, ratings, true);
        displayStats(cleanSheetsTable, cleanSheets, false);
    }

    private static void setStatsTable(final JTable table, final int x, final int y, final String label) {
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
        historicalView.add(scrollPane);
    }

    private static void placeBox(final JComboBox<String> box, final int x, final int width) {
        box.setBounds(x, boxY, width, boxHeight);
        box.setFont(new Font(FONT_NAME, PLAIN, boxFontSize));
        box.addActionListener(e -> update());
        historicalView.add(box);
    }

    private static void calculatePositions(final int frameWidth, final int frameHeight) {
        width = frameWidth;
        height = frameHeight;
        goalsX = 12 * width / 25;
        goalsY = height / 10;
        assistsY = height / 2;
        cleanSheetsX = 37 * width / 50;
        statsWidth = width / 4;
        statsHeight = 2 * height / 5;
        statsRowHeight = statsHeight / 13;
        statsFontSize = statsHeight / 23;
        standingsX = width / 50;
        standingsY = height / 10;
        standingsWidth = 9 * width / 20;
        standingsHeight = 4 * height / 5;
        standingsRowHeight = standingsHeight / 25;
        standingsFontSize = standingsHeight / 40;
        boxY = height / 80;
        boxHeight = height / 16;
        boxFontSize = boxHeight / 3;
        buttonY = 9 * height / 10;
        buttonHeight = height / 12;
    }
}
