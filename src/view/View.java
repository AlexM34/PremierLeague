package view;

import competition.England;
import competition.France;
import competition.Germany;
import competition.Italy;
import competition.Spain;
import simulation.Controller;
import team.Club;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.stream.IntStream;

import static simulation.Controller.CHAMPIONS_LEAGUE;
import static simulation.Controller.CHAMPIONS_LEAGUE_NAME;
import static simulation.Helper.sortLeague;
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
    private static final int FONT_SIZE = 25;

    private static final String[] STATS = {"N", "PLAYER", "COUNT"};
    private static final String[] LEAGUES = {England.LEAGUE, Spain.LEAGUE, Germany.LEAGUE, Italy.LEAGUE, France.LEAGUE, CHAMPIONS_LEAGUE_NAME};
    private static final String[] COMPETITIONS = {"League", "League Cup", "National Cup"};
    private static final String[] PHASES = {"Group Stage", "Knockout"};
    private static final String[] ROUNDS = IntStream.range(1, 39).mapToObj(String::valueOf).toArray(String[]::new);
    private static final String[] STAGES = {"Round of 16", "Quarter-finals", "Semi-finals", "Final"};
    private static final String[] GROUPS = {"A", "B", "C", "D", "E", "F", "G", "H"};
    private static final String[] STANDINGS = {"N", "TEAM", "G", "W", "D", "L", "GS", "GA", "GD", "P"};

    private static final JFrame frame = new JFrame("Gladiators");
    private static final JComboBox<String> nationBox = new JComboBox<>(LEAGUES);
    private static final JComboBox<String> competitionBox = new JComboBox<>(COMPETITIONS);
    private static final JComboBox<String> continentalBox = new JComboBox<>(PHASES);
    private static final JComboBox<String> roundBox = new JComboBox<>(ROUNDS);
    private static final JComboBox<String> knockoutBox = new JComboBox<>(STAGES);
    private static final JComboBox<String> groupBox = new JComboBox<>(GROUPS);
    private static final JLabel resultsLabel = new JLabel();
    private static final JTable standingsTable = new JTable(new String[20][10], STANDINGS);
    private static final JTable gamesTable = new JTable(new String[11][10], new String[]{"STATS", "COUNT"});

    private static int resultsX;
    private static int resultsY;
    private static int resultsWidth;
    private static int resultsHeight;
    private static int goalsY;
    private static int statsWidth;
    private static int statsHeight;
    private static int cleanSheetsX;
    private static int assistsY;
    private static int standingsX;
    private static int standingsY;
    private static int standingsWidth;
    private static int standingsHeight;
    private static int gamesY;
    private static int gamesWidth;
    private static int gamesHeight;
    private static int boxY;
    private static int boxHeight;
    private static int boxFontSize;
    private static int statsFontSize;
    private static int standingsFontSize;

    private final JTable goalsTable = new JTable(new String[10][3], STATS);
    private final JTable assistsTable = new JTable(new String[10][3], STATS);
    private final JTable ratingsTable = new JTable(new String[10][3], STATS);
    private final JTable cleanSheetsTable = new JTable(new String[10][3], STATS);

    public View() {
        Controller.initialise();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        final int width = frame.getWidth();
        final int height = frame.getHeight();
        calculatePositions(width, height);

        final JLabel background = new JLabel();

        try {
            BufferedImage ball = ImageIO.read(getClass().getResource("/ball.jpg"));
            frame.setIconImage(ball);

            BufferedImage image = ImageIO.read(getClass().getResource("/gladiators.jpg"));
            Image scaledInstance = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledInstance);

            background.setIcon(icon);
            background.setSize(width, height);
            background.setLocation(0, 0);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        nationBox.setBounds(9 * width / 25, boxY, width / 9, boxHeight);
        nationBox.setFont(new Font(FONT_NAME, Font.PLAIN, boxFontSize));

        competitionBox.setBounds(19 * width / 40, boxY, width / 11, boxHeight);
        competitionBox.setFont(new Font(FONT_NAME, Font.PLAIN, boxFontSize));

        continentalBox.setBounds(19 * width / 40, boxY, width / 11, boxHeight);
        continentalBox.setFont(new Font(FONT_NAME, Font.PLAIN, boxFontSize));

        roundBox.setBounds(23 * width / 40, boxY, width / 11, boxHeight);
        roundBox.setFont(new Font(FONT_NAME, Font.PLAIN, boxFontSize));

        knockoutBox.setBounds(23 * width / 40, boxY, width / 11, boxHeight);
        knockoutBox.setFont(new Font(FONT_NAME, Font.PLAIN, boxFontSize));

        groupBox.setBounds(23 * width / 40, boxY, width / 11, boxHeight);
        groupBox.setFont(new Font(FONT_NAME, Font.PLAIN, boxFontSize));

        resultsLabel.setBounds(resultsX, resultsY, resultsWidth, resultsHeight);
        resultsLabel.setFont(new Font(FONT_NAME, Font.PLAIN, resultsHeight / 14));
        TitledBorder borderResults = BorderFactory.createTitledBorder("RESULTS");
        borderResults.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        JScrollPane resultsPane = new JScrollPane(resultsLabel);
        resultsPane.setBorder(borderResults);
        resultsPane.setBounds(resultsX, resultsY, resultsWidth, resultsHeight);

        setStatTables(goalsTable, resultsX, goalsY, "GOALS");
        setStatTables(assistsTable, resultsX, assistsY, "ASSISTS");
        setStatTables(ratingsTable, cleanSheetsX, goalsY, "RATINGS");
        setStatTables(cleanSheetsTable, cleanSheetsX, assistsY, "CLEAN SHEETS");

        standingsTable.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);
        standingsTable.setRowHeight(standingsHeight / 23);
        standingsTable.setEnabled(false);
        standingsTable.setFont(new Font(FONT_NAME, Font.PLAIN, standingsFontSize));
        setColumnWidths(standingsTable, standingsWidth, 6, 38, 7, 7, 7, 7, 7, 7, 7, 7);
        JScrollPane standingsPane = new JScrollPane(standingsTable);
        TitledBorder borderStandings = BorderFactory.createTitledBorder("STANDINGS");
        borderStandings.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        standingsPane.setBorder(borderStandings);
        standingsPane.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);

        gamesTable.setBounds(standingsX, gamesY, gamesWidth, gamesHeight);
        gamesTable.setRowHeight(statsHeight / 13);
        gamesTable.setEnabled(false);
        gamesTable.setFont(new Font(FONT_NAME, Font.PLAIN, statsFontSize));
        gamesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        setTableValues(gamesTable, "Games", "Home Wins", "Draws", "Away Wins", "Home Goals", "Away Goals",
                "Assists", "Yellow Cards", "Red Cards", "Average Ratings", "Clean Sheets");

        setColumnWidths(gamesTable, gamesWidth, 70, 30);
        JScrollPane gamesPane = new JScrollPane(gamesTable);
        gamesPane.setBounds(standingsX, gamesY, gamesWidth, gamesHeight);

        final JButton nextButton = new JButton("Next");
        nextButton.setBounds(7 * width / 8, 9 * height / 10, width / 12, height / 12);
        nextButton.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));

        frame.add(nationBox);
        frame.add(competitionBox);
        frame.add(continentalBox);
        frame.add(roundBox);
        frame.add(knockoutBox);
        frame.add(groupBox);
        frame.add(resultsPane);
        frame.add(standingsPane);
        frame.add(gamesPane);
        frame.add(nextButton);
        frame.getContentPane().add(background);

        nationBox.addActionListener(e -> updateStats());
        competitionBox.addActionListener(e -> updateStats());
        continentalBox.addActionListener(e -> updateStats());
        roundBox.addActionListener(e -> updateStats());
        knockoutBox.addActionListener(e -> updateStats());
        groupBox.addActionListener(e -> updateStats());
        nextButton.addActionListener(e -> nextRound());

        playMusic();
        updateStats();
    }

    private void nextRound() {
        for (int i = 0; i < 38; i++) Controller.proceed();
        updateStats();
    }

    private void updateStats() {
        final String competition = String.valueOf(competitionBox.getSelectedItem());
        final int teams;
        switch (String.valueOf(knockoutBox.getSelectedItem())) {
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
            case CHAMPIONS_LEAGUE_NAME: continentalView(teams);
            default: return;
        }

        if (competition.equals("League")) leagueView(league);
        else cupView(league, competition.equals("League Cup"), teams);

        playerStats(league, competition.equals("League") ? 0 : 1);
        displayStats(goalsTable, goals, false);
        displayStats(assistsTable, assists, false);
        displayStats(ratingsTable, ratings, true);
        displayStats(cleanSheetsTable, cleanSheets, false);
    }

    private void leagueView(final Club[] league) {
        competitionBox.setVisible(true);
        continentalBox.setVisible(false);
        roundBox.setVisible(true);
        knockoutBox.setVisible(false);
        groupBox.setVisible(false);
        standingsTable.setVisible(true);
        gamesTable.setVisible(true);

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
        resultsLabel.setText("<html>" + Controller.leagueResults.getOrDefault(
                leagueName + round, "") + "</html>");
        System.out.println(Controller.leagueResults.get(leagueName));
    }

    private void cupView(final Club[] league, final boolean leagueCup, final int teams) {
        competitionBox.setVisible(true);
        continentalBox.setVisible(false);
        roundBox.setVisible(false);
        knockoutBox.setVisible(true);
        groupBox.setVisible(false);
        standingsTable.setVisible(false);
        gamesTable.setVisible(false);

        final String leagueName = league[0].getLeague();
        if (leagueCup) {
            resultsLabel.setText("<html>" + Controller.leagueCupResults.getOrDefault(
                    leagueName + teams, "") + "</html>");
            System.out.println(Controller.leagueCupResults.get(leagueName));
        } else {
            resultsLabel.setText("<html>" + Controller.nationalCupResults.getOrDefault(
                    leagueName + teams, "") + "</html>");
            System.out.println(Controller.nationalCupResults.get(leagueName));
        }
    }

    private void continentalView(final int teams) {
        competitionBox.setVisible(false);
        continentalBox.setVisible(true);
        roundBox.setVisible(false);
        gamesTable.setVisible(false);

        final boolean knockout = String.valueOf(continentalBox.getSelectedItem()).equals("Knockout");
        knockoutBox.setVisible(knockout);
        groupBox.setVisible(!knockout);
        standingsTable.setVisible(!knockout);

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < standingsTable.getColumnCount(); j++) {
                standingsTable.setValueAt("", i, j);
            }
        }

        if (knockout) {
            resultsLabel.setText("<html>" + Controller.continentalCupResults.getOrDefault(
                    CHAMPIONS_LEAGUE_NAME + teams, "") + "</html>");
        } else {
            final Club[] league = new Club[4];
            for (int team = 0; team < 4; team++)
                league[team] = CHAMPIONS_LEAGUE[8 * team +
                        ((int) String.valueOf(groupBox.getSelectedItem()).charAt(0) - 'A')];

            final Map<Club, Integer> sorted = sortLeague(league, 3);
            int row = 0;
            for (final Club team : sorted.keySet()) {
                insertStandingsEntry(standingsTable, team.getName(), team.getSeason().getChampionsLeague().getGroup(), row++);
            }

            resultsLabel.setText("<html>" + Controller.continentalCupResults.getOrDefault(
                    CHAMPIONS_LEAGUE_NAME + groupBox.getSelectedItem(), "") + "</html>");
        }

        System.out.println(Controller.continentalCupResults);
        playerStats(Controller.CHAMPIONS_LEAGUE, 2);
        displayStats(goalsTable, goals, false);
        displayStats(assistsTable, assists, false);
        displayStats(ratingsTable, ratings, true);
        displayStats(cleanSheetsTable, cleanSheets, false);
    }

    private static void setStatTables(final JTable table, final int x, final int y, final String label) {
        table.setBounds(x, y, statsWidth, statsHeight);
        table.setRowHeight(statsHeight / 15);
        table.setEnabled(false);
        table.setFont(new Font(FONT_NAME, Font.PLAIN, statsFontSize));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setColumnWidths(table, statsWidth, 15, 60, 25);

        JScrollPane scrollPane = new JScrollPane(table);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(label);
        titledBorder.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        scrollPane.setBorder(titledBorder);
        scrollPane.setBounds(x, y, statsWidth, statsHeight);
        frame.add(scrollPane);
    }

    private static void calculatePositions(final int width, final int height) {
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
    }
}
