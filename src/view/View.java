package view;

import competition.England;
import competition.France;
import competition.Germany;
import competition.Italy;
import competition.Spain;
import simulation.Data;
import team.Club;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Image.SCALE_SMOOTH;
import static javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static simulation.Controller.CHAMPIONS_LEAGUE;
import static simulation.Controller.CHAMPIONS_LEAGUE_NAME;
import static simulation.Controller.EUROPA_LEAGUE;
import static simulation.Controller.EUROPA_LEAGUE_NAME;
import static simulation.Controller.continentalCupResults;
import static simulation.Controller.initialise;
import static simulation.Controller.leagueCupResults;
import static simulation.Controller.leagueResults;
import static simulation.Controller.nationalCupResults;
import static simulation.Controller.proceed;
import static simulation.Helper.sortLeague;
import static simulation.Printer.allTime;
import static simulation.Printer.assists;
import static simulation.Printer.cleanSheets;
import static simulation.Printer.goals;
import static simulation.Printer.playerStats;
import static simulation.Printer.ratings;
import static view.Helper.displayStats;
import static view.Helper.getInteger;
import static view.Helper.insertStandingsEntry;
import static view.Helper.leagueStats;
import static view.Helper.playMusic;
import static view.Helper.setColumnWidths;
import static view.Helper.setTableValues;

public class View {
    static final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    private static final String FONT_NAME = "Times New Roman";
    private static final int FONT_SIZE = 22;

    private static final String[] STATS = {"N", "PLAYER", "TEAM", "COUNT"};
    private static final String[] LEAGUES = {England.LEAGUE, Spain.LEAGUE, Germany.LEAGUE, Italy.LEAGUE, France.LEAGUE,
            CHAMPIONS_LEAGUE_NAME, EUROPA_LEAGUE_NAME};
    private static final String[] COMPETITIONS = {"League", "League Cup", "National Cup"};
    private static final String[] PHASES = {"Group Stage", "Knockout"};
    private static final String[] ROUNDS = IntStream.range(1, 39).mapToObj(String::valueOf).toArray(String[]::new);
    private static final String[] STAGES = {"Round of 32", "Round of 16", "Quarter-finals", "Semi-finals", "Final"};
    private static final String[] GROUPS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
    private static final String[] STANDINGS = {"N", "TEAM", "G", "W", "D", "L", "GS", "GA", "GD", "P"};
    private static final String[] TROPHIES = {"N", "TEAM", "TROPHIES"};

    private static final JFrame frame = new JFrame("Gladiators");
    private static final JPanel currentSeason = new JPanel();
    private static final JPanel history = new JPanel();
    private static final JComboBox<String> nationBox = new JComboBox<>(LEAGUES);
    private static final JComboBox<String> competitionBox = new JComboBox<>(COMPETITIONS);
    private static final JComboBox<String> continentalBox = new JComboBox<>(PHASES);
    private static final JComboBox<String> roundBox = new JComboBox<>(ROUNDS);
    private static final JComboBox<String> knockoutBox = new JComboBox<>(STAGES);
    private static final JComboBox<String> groupBox = new JComboBox<>(GROUPS);
    private static final JComboBox<String> historyNationBox = new JComboBox<>(LEAGUES);
    private static final JComboBox<String> historyCompetitionBox = new JComboBox<>(COMPETITIONS);
    private static final JLabel resultsLabel = new JLabel();
    private static final JTable standingsTable = new JTable(new String[20][10], STANDINGS);
    private static final JTable trophiesTable = new JTable(new String[30][3], TROPHIES);
    private static final JTable gamesTable = new JTable(new String[11][10], new String[]{"STATS", "COUNT"});

    private static int width;
    private static int height;
    private static int resultsX;
    private static int resultsY;
    private static int resultsWidth;
    private static int resultsHeight;
    private static int goalsY;
    private static int historyGoalsX;
    private static int historyGoalsY;
    private static int statsWidth;
    private static int statsHeight;
    private static int historyStatsWidth;
    private static int historyStatsHeight;
    private static int cleanSheetsX;
    private static int historyCleanSheetsX;
    private static int assistsY;
    private static int historyAssistsY;
    private static int standingsX;
    private static int standingsY;
    private static int historyStandingsX;
    private static int standingsWidth;
    private static int standingsHeight;
    private static int historyStandingsHeight;
    private static int gamesY;
    private static int gamesWidth;
    private static int gamesHeight;
    private static int boxY;
    private static int boxHeight;
    private static int boxFontSize;
    private static int statsFontSize;
    private static int standingsFontSize;

    private final JTable goalsTable = new JTable(new String[10][4], STATS);
    private final JTable assistsTable = new JTable(new String[10][4], STATS);
    private final JTable ratingsTable = new JTable(new String[10][4], STATS);
    private final JTable cleanSheetsTable = new JTable(new String[10][4], STATS);
    private final JTable historyGoalsTable = new JTable(new String[10][4], STATS);
    private final JTable historyAssistsTable = new JTable(new String[10][4], STATS);
    private final JTable historyRatingsTable = new JTable(new String[10][4], STATS);
    private final JTable historyCleanSheetsTable = new JTable(new String[10][4], STATS);

    public View() {
        initialise();
        calculatePositions();
        placeBoxes();
        placeResults();
        placeTables();
        placeButtons();
        placeImages();
        playMusic();
        updateStats();
    }

    private void nextRound() {
        for (int i = 0; i < 38; i++) proceed();
        updateStats();
    }

    private void history() {
        currentSeason.setVisible(false);
        history.setVisible(true);

        final String nation = String.valueOf(historyNationBox.getSelectedItem());
        final boolean international = nation.equals(CHAMPIONS_LEAGUE_NAME) || nation.equals(EUROPA_LEAGUE_NAME);
        if (international) historyCompetitionBox.setVisible(false);
        else historyCompetitionBox.setVisible(true);

        final Club[] league;
        switch (nation) {
            case England.LEAGUE: league = England.CLUBS; break;
            case Spain.LEAGUE: league = Spain.CLUBS; break;
            case Germany.LEAGUE: league = Germany.CLUBS; break;
            case Italy.LEAGUE: league = Italy.CLUBS; break;
            case France.LEAGUE: league = France.CLUBS; break;
            default: league = Arrays.stream(Data.LEAGUES).flatMap(Arrays::stream).toArray(Club[]::new);
        }

        int competition;
        switch(String.valueOf(historyCompetitionBox.getSelectedItem())) {
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
        displayStats(historyGoalsTable, goals, false);
        displayStats(historyAssistsTable, assists, false);
        displayStats(historyRatingsTable, ratings, true);
        displayStats(historyCleanSheetsTable, cleanSheets, false);
    }

    private void updateStats() {
        history.setVisible(false);
        currentSeason.setVisible(true);

        final String competition = String.valueOf(competitionBox.getSelectedItem());
        final int teams;
        switch (String.valueOf(knockoutBox.getSelectedItem())) {
            case "Round of 32": teams = 32; break;
            case "Round of 16": teams = 16; break;
            case "Quarter-finals": teams = 8; break;
            case "Semi-finals": teams = 4; break;
            case "Final": teams = 2; break;
            default: teams = 1; break;
        }

        final Club[] league;
        switch (String.valueOf(nationBox.getSelectedItem())) {
            case England.LEAGUE: league = England.CLUBS; break;
            case Spain.LEAGUE: league = Spain.CLUBS; break;
            case Germany.LEAGUE: league = Germany.CLUBS; break;
            case Italy.LEAGUE: league = Italy.CLUBS; break;
            case France.LEAGUE: league = France.CLUBS; break;
            case CHAMPIONS_LEAGUE_NAME: continentalView(CHAMPIONS_LEAGUE_NAME, teams); return;
            case EUROPA_LEAGUE_NAME: continentalView(EUROPA_LEAGUE_NAME, teams);
            default: return;
        }

        if (competition.equals("League")) leagueView(league);
        else cupView(league, competition.equals("League Cup"), teams);

        playerStats(league, competition.equals("League") ? 0 : competition.equals("National Cup") ? 1 : 2, true);
        displayStats(goalsTable, goals, false);
        displayStats(assistsTable, assists, false);
        displayStats(ratingsTable, ratings, true);
        displayStats(cleanSheetsTable, cleanSheets, false);
    }

    private void leagueView(final Club[] league) {
        boxVisibility(0);

        final String leagueName = league[0].getLeague();
        final Map<Club, Integer> sorted = sortLeague(league, 0);
        int row = 0;
        for (final Club team : sorted.keySet()) {
            insertStandingsEntry(standingsTable, team.getName(), team.getSeason().getLeague(), row++);
        }

        for (int i = row; i < 20; i++) {
            for (int j = 0; j < standingsTable.getColumnCount(); j++) {
                standingsTable.setValueAt("", i, j);
            }
        }

        leagueStats(gamesTable, leagueName);

        final int round = getInteger(String.valueOf(roundBox.getSelectedItem()));
        resultsLabel.setText("<html>" + leagueResults.getOrDefault(leagueName + round, "") + "</html>");
        System.out.println(leagueResults.get(leagueName));
    }

    private void cupView(final Club[] league, final boolean leagueCup, final int teams) {
        boxVisibility(1);

        final String leagueName = league[0].getLeague();
        if (leagueCup) {
            resultsLabel.setText("<html>" + leagueCupResults.getOrDefault(
                    leagueName + teams, "") + "</html>");
            System.out.println(leagueCupResults.get(leagueName));
        } else {
            resultsLabel.setText("<html>" + nationalCupResults.getOrDefault(
                    leagueName + teams, "") + "</html>");
            System.out.println(nationalCupResults.get(leagueName));
        }
    }

    private void continentalView(final String competition, final int teams) {
        boxVisibility(2);

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < standingsTable.getColumnCount(); j++) {
                standingsTable.setValueAt("", i, j);
            }
        }

        final boolean isChampionsLeague = competition.equals(CHAMPIONS_LEAGUE_NAME);
        final int group = (int) String.valueOf(groupBox.getSelectedItem()).charAt(0) - 'A';
        if (String.valueOf(continentalBox.getSelectedItem()).equals("Knockout")) {
            resultsLabel.setText("<html>" + continentalCupResults.getOrDefault(
                    competition + teams, "") + "</html>");
        } else if (!isChampionsLeague || group < 8) {
            final Club[] league = new Club[4];
            for (int team = 0; team < 4; team++) {
                league[team] = isChampionsLeague ? CHAMPIONS_LEAGUE[8 * team + group] : EUROPA_LEAGUE[12 * team + group];
            }

            final Map<Club, Integer> sorted = sortLeague(league, 3);
            int row = 0;
            for (final Club team : sorted.keySet()) {
                insertStandingsEntry(standingsTable, team.getName(), team.getSeason().getContinental().getGroup(), row++);
            }

            resultsLabel.setText("<html>" + continentalCupResults.getOrDefault(
                    competition + groupBox.getSelectedItem(), "") + "</html>");
        }

        System.out.println(continentalCupResults);
        if (competition.equals(CHAMPIONS_LEAGUE_NAME)) playerStats(CHAMPIONS_LEAGUE, 3, true);
        else playerStats(EUROPA_LEAGUE, 4, true);

        displayStats(goalsTable, goals, false);
        displayStats(assistsTable, assists, false);
        displayStats(ratingsTable, ratings, true);
        displayStats(cleanSheetsTable, cleanSheets, false);
    }

    private void boxVisibility(final int competition) {
        competitionBox.setVisible(competition != 2);
        continentalBox.setVisible(competition == 2);
        roundBox.setVisible(competition == 0);
        gamesTable.setVisible(competition == 0);

        final boolean knockout;
        switch (competition) {
            case 0:
                knockout = false;
                break;
            case 1:
                knockout = true;
                break;
            default:
                knockout = String.valueOf(continentalBox.getSelectedItem()).equals("Knockout");
        }

        knockoutBox.setVisible(knockout);
        groupBox.setVisible(!knockout);
        standingsTable.setVisible(!knockout);
    }

    private void placeBoxes() {
        placeBox(currentSeason, nationBox, 9 * width / 25, width / 9);
        placeBox(currentSeason, competitionBox, 19 * width / 40, width / 11);
        placeBox(currentSeason, continentalBox, 19 * width / 40, width / 11);
        placeBox(currentSeason, roundBox, 23 * width / 40, width / 11);
        placeBox(currentSeason, knockoutBox, 23 * width / 40, width / 11);
        placeBox(currentSeason, groupBox, 23 * width / 40, width / 11);

        placeBox(history, historyNationBox, 17 * width / 40, width / 9);
        placeBox(history, historyCompetitionBox, 23 * width / 40, width / 11);
    }

    private void placeBox(final JPanel panel, final JComboBox<String> box, final int x, final int width) {
        box.setBounds(x, boxY, width, boxHeight);
        box.setFont(new Font(FONT_NAME, PLAIN, boxFontSize));
        panel.add(box);

        if (panel == currentSeason) box.addActionListener(e -> updateStats());
        else box.addActionListener(e -> history());
    }

    private void placeResults() {
        resultsLabel.setBounds(resultsX, resultsY, resultsWidth, resultsHeight);
        resultsLabel.setFont(new Font(FONT_NAME, PLAIN, resultsHeight / 14));
        final TitledBorder borderResults = BorderFactory.createTitledBorder("RESULTS");
        borderResults.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));

        final JScrollPane resultsPane = new JScrollPane(resultsLabel);
        resultsPane.setBorder(borderResults);
        resultsPane.setBounds(resultsX, resultsY, resultsWidth, resultsHeight);
        currentSeason.add(resultsPane);
    }

    private void placeTables() {
        setStatTables(goalsTable, resultsX, goalsY, "GOALS");
        setStatTables(assistsTable, resultsX, assistsY, "ASSISTS");
        setStatTables(ratingsTable, cleanSheetsX, goalsY, "RATINGS");
        setStatTables(cleanSheetsTable, cleanSheetsX, assistsY, "CLEAN SHEETS");

        setHistoryStatTables(historyGoalsTable, historyGoalsX, historyGoalsY, "GOALS");
        setHistoryStatTables(historyAssistsTable, historyGoalsX, historyAssistsY, "ASSISTS");
        setHistoryStatTables(historyRatingsTable, historyCleanSheetsX, historyGoalsY, "RATINGS");
        setHistoryStatTables(historyCleanSheetsTable, historyCleanSheetsX, historyAssistsY, "CLEAN SHEETS");

        standingsTable.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);
        standingsTable.setRowHeight(standingsHeight / 23);
        standingsTable.setEnabled(false);
        standingsTable.setFont(new Font(FONT_NAME, PLAIN, standingsFontSize));
        setColumnWidths(standingsTable, standingsWidth, 6, 38, 7, 7, 7, 7, 7, 7, 7, 7);

        final JScrollPane standingsPane = new JScrollPane(standingsTable);
        final TitledBorder borderStandings = BorderFactory.createTitledBorder("STANDINGS");
        borderStandings.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));
        standingsPane.setBorder(borderStandings);
        standingsPane.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);

        trophiesTable.setBounds(historyStandingsX, standingsY, standingsWidth, historyStandingsHeight);
        trophiesTable.setRowHeight(standingsHeight / 23);
        trophiesTable.setEnabled(false);
        trophiesTable.setFont(new Font(FONT_NAME, PLAIN, standingsFontSize));
        setColumnWidths(trophiesTable, standingsWidth, 7, 60, 33);

        final JScrollPane trophiesPane = new JScrollPane(trophiesTable);
        final TitledBorder borderTrophies = BorderFactory.createTitledBorder("TROPHIES");
        borderTrophies.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));
        trophiesPane.setBorder(borderTrophies);
        trophiesPane.setBounds(historyStandingsX, standingsY, standingsWidth, historyStandingsHeight);

        gamesTable.setBounds(standingsX, gamesY, gamesWidth, gamesHeight);
        gamesTable.setRowHeight(statsHeight / 13);
        gamesTable.setEnabled(false);
        gamesTable.setFont(new Font(FONT_NAME, PLAIN, statsFontSize));
        gamesTable.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);

        setTableValues(gamesTable, "Games", "Home Wins", "Draws", "Away Wins", "Home Goals", "Away Goals",
                "Assists", "Yellow Cards", "Red Cards", "Average Ratings", "Clean Sheets");

        setColumnWidths(gamesTable, gamesWidth, 70, 30);
        final JScrollPane gamesPane = new JScrollPane(gamesTable);
        gamesPane.setBounds(standingsX, gamesY, gamesWidth, gamesHeight);

        currentSeason.add(standingsPane);
        currentSeason.add(gamesPane);
        history.add(trophiesPane);
    }

    private static void setStatTables(final JTable table, final int x, final int y, final String label) {
        table.setBounds(x, y, statsWidth, statsHeight);
        table.setRowHeight(statsHeight / 15);
        table.setEnabled(false);
        table.setFont(new Font(FONT_NAME, PLAIN, statsFontSize));
        table.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
        setColumnWidths(table, statsWidth, 8, 40, 35, 17);

        final JScrollPane scrollPane = new JScrollPane(table);
        final TitledBorder titledBorder = BorderFactory.createTitledBorder(label);
        titledBorder.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));
        scrollPane.setBorder(titledBorder);
        scrollPane.setBounds(x, y, statsWidth, statsHeight);
        currentSeason.add(scrollPane);
    }

    private static void setHistoryStatTables(final JTable table, final int x, final int y, final String label) {
        table.setBounds(x, y, historyStatsWidth, historyStatsHeight);
        table.setRowHeight(historyStatsHeight / 13);
        table.setEnabled(false);
        table.setFont(new Font(FONT_NAME, PLAIN, statsFontSize));
        table.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
        setColumnWidths(table, historyStatsWidth, 8, 40, 35, 17);

        final JScrollPane scrollPane = new JScrollPane(table);
        final TitledBorder titledBorder = BorderFactory.createTitledBorder(label);
        titledBorder.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));
        scrollPane.setBorder(titledBorder);
        scrollPane.setBounds(x, y, historyStatsWidth, historyStatsHeight);
        history.add(scrollPane);
    }

    private void placeButtons() {
        final JButton nextButton = new JButton("Next");
        nextButton.setBounds(7 * width / 8, 9 * height / 10, width / 12, height / 12);
        nextButton.setFont(new Font(FONT_NAME, PLAIN, FONT_SIZE));
        nextButton.addActionListener(e -> nextRound());
        currentSeason.add(nextButton);

        final JButton historyButton = new JButton("History");
        historyButton.setBounds(7 * width / 8, 4 * height / 5, width / 12, height / 12);
        historyButton.setFont(new Font(FONT_NAME, PLAIN, FONT_SIZE));
        historyButton.addActionListener(e -> history());
        currentSeason.add(historyButton);

        final JButton currentButton = new JButton("Current");
        currentButton.setBounds(7 * width / 8, 9 * height / 10, width / 12, height / 12);
        currentButton.setFont(new Font(FONT_NAME, PLAIN, FONT_SIZE));
        currentButton.addActionListener(e -> updateStats());
        history.add(currentButton);
    }

    private void placeImages() {
        try {
            frame.setIconImage(ImageIO.read(getClass().getResource("/ball.jpg")));
            currentSeason.add(getImage("/current.jpg"));
            history.add(getImage("/history.jpg"));
        } catch (final IOException e) {
            System.out.println("Exception thrown while extracting images! " + e);
        }
    }

    private JLabel getImage(final String name) throws IOException {
        final BufferedImage image = ImageIO.read(getClass().getResource(name));
        final Image scaledInstance = image.getScaledInstance(width, height, SCALE_SMOOTH);
        final ImageIcon icon = new ImageIcon(scaledInstance);

        final JLabel label = new JLabel();
        label.setIcon(icon);
        label.setSize(width, height);
        label.setLocation(0, 0);

        return label;
    }

    private static void calculatePositions() {
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setExtendedState(MAXIMIZED_BOTH);
        frame.setVisible(true);
        width = frame.getWidth();
        height = frame.getHeight();

        currentSeason.setLayout(null);
        currentSeason.setVisible(true);
        currentSeason.setSize(width, height);
        frame.getContentPane().add(currentSeason);

        history.setLayout(null);
        history.setVisible(false);
        history.setSize(width, height);
        frame.getContentPane().add(history);

        resultsX = width / 80;
        resultsY = height / 15;
        resultsWidth = width / 2;
        resultsHeight = 7 * height / 20;
        goalsY = 33 * height / 80;
        statsWidth = 2 * width / 9;
        statsHeight = 27 * height / 100;
        assistsY = 55 * height / 80;
        cleanSheetsX = 7 * width / 25;
        standingsX = 21 * width / 40;
        standingsY = height / 10;
        standingsWidth = 9 * width / 20;
        standingsHeight = 3 * height / 5;
        gamesY = 7 * height / 10;
        gamesWidth = standingsWidth / 3;
        gamesHeight = height / 4;
        boxY = height / 80;
        boxHeight = height / 16;
        boxFontSize = height / 50;
        statsFontSize = statsHeight / 16;
        standingsFontSize = standingsHeight / 30;

        historyStandingsX = width / 50;
        historyStandingsHeight = 4 * height / 5;
        historyGoalsX = 12 * width / 25;
        historyGoalsY = height / 10;
        historyStatsWidth = width / 4;
        historyStatsHeight = 2 * height / 5;
        historyCleanSheetsX = 37 * width / 50;
        historyAssistsY = height / 2;
    }
}
