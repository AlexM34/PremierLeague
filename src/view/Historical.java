package view;

import league.LeagueManager;
import team.Club;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Map;

import static java.awt.Font.PLAIN;
import static league.LeagueManager.getClubs;
import static simulation.competition.League.CHAMPIONS_LEAGUE_NAME;
import static simulation.competition.League.EUROPA_LEAGUE_NAME;
import static simulation.dynamics.Postseason.allTime;
import static simulation.dynamics.Postseason.assists;
import static simulation.dynamics.Postseason.cleanSheets;
import static simulation.dynamics.Postseason.goals;
import static simulation.dynamics.Postseason.playerStats;
import static simulation.dynamics.Postseason.ratings;
import static view.ViewManager.historicalView;
import static view.ViewManager.rankedView;
import static view.ViewManager.seasonalView;

class Historical extends View {
    private static final String[] STATS = {"N", "PLAYER", "TEAM", "COUNT"};
    private static final String[] COMPETITIONS = {"League", "League Cup", "National Cup"};
    private static final String[] TROPHIES = {"N", "TEAM", "TROPHIES"};

    private static final JComboBox<String> NATION_BOX = new JComboBox<>(LeagueManager.getLeagues());
    private static final JComboBox<String> COMPETITION_BOX = new JComboBox<>(COMPETITIONS);
    private static final JTable GOALS_TABLE = new JTable(new String[10][4], STATS);
    private static final JTable ASSISTS_TABLE = new JTable(new String[10][4], STATS);
    private static final JTable RATINGS_TABLE = new JTable(new String[10][4], STATS);
    private static final JTable CLEAN_SHEETS_TABLE = new JTable(new String[10][4], STATS);
    private static final JTable TROPHIES_TABLE = new JTable(new String[30][3], TROPHIES);

    private static int width;
    private static int height;
    private static int goalsX;
    private static int goalsY;
    private static int assistsY;
    private static int cleanSheetsX;
    private static int firstBoxX;
    private static int secondBoxX;
    private static int boxY;
    private static int longBoxWidth;
    private static int shortBoxWidth;
    private static int boxHeight;
    private static int boxFontSize;
    private static int seasonsButtonX;
    private static int rankingsButtonX;
    private static int buttonY;
    private static int seasonsButtonWidth;
    private static int rankingsButtonWidth;
    private static int buttonHeight;

    static void setup(final int frameWidth, final int frameHeight) {
        calculatePositions(frameWidth, frameHeight);
        placeBoxes();
        setPlayerTables();
        setTrophiesTable();
        setButtons();
        loadImage();
    }

    private static void placeBoxes() {
        placeBox(NATION_BOX, firstBoxX, longBoxWidth);
        placeBox(COMPETITION_BOX, secondBoxX, shortBoxWidth);
    }

    private static void setPlayerTables() {
        addStatsPane(GOALS_TABLE, goalsX, goalsY, "GOALS");
        addStatsPane(ASSISTS_TABLE, goalsX, assistsY, "ASSISTS");
        addStatsPane(RATINGS_TABLE, cleanSheetsX, goalsY, "RATINGS");
        addStatsPane(CLEAN_SHEETS_TABLE, cleanSheetsX, assistsY, "CLEAN SHEETS");
    }

    private static void addStatsPane(final JTable table, final int x, final int y, final String label) {
        final JScrollPane pane = createStatsPane(table, x, y, label);
        historicalView.add(pane);
    }

    private static void setTrophiesTable() {
        final JScrollPane trophiesPane = createTeamPane(TROPHIES_TABLE, "TROPHIES", 7, 60, 33);
        historicalView.add(trophiesPane);
    }

    private static void setButtons() {
        addButton("Season", seasonsButtonX, buttonY, seasonsButtonWidth, buttonHeight, e -> seasonalView());
        addButton("Rankings", rankingsButtonX, buttonY, rankingsButtonWidth, buttonHeight, e -> rankedView());
    }

    private static void addButton(final String name, final int x, final int y,
                                  final int width, final int height, final ActionListener action) {

        final JButton button = createButton(name, x, y, width, height, action);
        historicalView.add(button);
    }

    private static void loadImage() {
        historicalView.add(getImage("/history.jpg", width, height));
    }

    static void update() {
        final String nation = String.valueOf(NATION_BOX.getSelectedItem());
        final boolean international = nation.equals(CHAMPIONS_LEAGUE_NAME) || nation.equals(EUROPA_LEAGUE_NAME);
        if (international) COMPETITION_BOX.setVisible(false);
        else COMPETITION_BOX.setVisible(true);

        final Club[] league = getClubs(nation);
        int competition;
        switch(String.valueOf(COMPETITION_BOX.getSelectedItem())) {
            case "League": competition = 0; break;
            case "National Cup": competition = 1; break;
            case "League Cup": competition = 2; break;
            default: competition = -1;
        }

        if (international) competition = nation.equals(CHAMPIONS_LEAGUE_NAME) ? 3 : 4;
        final Map<String, Integer> winners = allTime(league, competition);
        int row = 0;
        for (final String club : winners.keySet()) {
            TROPHIES_TABLE.setValueAt(String.valueOf(row + 1), row, 0);
            TROPHIES_TABLE.setValueAt(club, row, 1);
            TROPHIES_TABLE.setValueAt(String.valueOf(winners.get(club)), row++, 2);
        }

        for (int i = row; i < 30; i++) {
            TROPHIES_TABLE.setValueAt("", i, 0);
            TROPHIES_TABLE.setValueAt("", i, 1);
            TROPHIES_TABLE.setValueAt("", i, 2);
        }

        playerStats(league, competition, false);
        displayPlayerStats();
    }

    static void displayPlayerStats() {
        displayStats(GOALS_TABLE, goals, false);
        displayStats(ASSISTS_TABLE, assists, false);
        displayStats(RATINGS_TABLE, ratings, true);
        displayStats(CLEAN_SHEETS_TABLE, cleanSheets, false);
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
        firstBoxX = 17 * width / 40;
        secondBoxX = 23 * width / 40;
        boxY = height / 80;
        longBoxWidth = width / 9;
        shortBoxWidth = width / 11;
        boxHeight = height / 16;
        boxFontSize = boxHeight / 3;
        seasonsButtonX = 7 * width / 8;
        rankingsButtonX = 3 * width / 4;
        buttonY = 9 * height / 10;
        seasonsButtonWidth = width / 12;
        rankingsButtonWidth = width / 10;
        buttonHeight = height / 12;
    }
}
