package view;

import league.England;
import league.France;
import league.Germany;
import league.Italy;
import league.Spain;
import team.Club;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;
import java.util.stream.IntStream;

import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import static javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS;
import static simulation.Controller.MATCHDAYS;
import static simulation.Controller.proceed;
import static simulation.Helper.sortLeague;
import static simulation.competition.Knockout.leagueCupResults;
import static simulation.competition.Knockout.nationalCupResults;
import static simulation.competition.League.CHAMPIONS_LEAGUE;
import static simulation.competition.League.CHAMPIONS_LEAGUE_NAME;
import static simulation.competition.League.EUROPA_LEAGUE;
import static simulation.competition.League.EUROPA_LEAGUE_NAME;
import static simulation.competition.League.continentalCupResults;
import static simulation.competition.League.leagueResults;
import static simulation.dynamics.Postseason.assists;
import static simulation.dynamics.Postseason.cleanSheets;
import static simulation.dynamics.Postseason.goals;
import static simulation.dynamics.Postseason.playerStats;
import static simulation.dynamics.Postseason.ratings;
import static view.Helper.displayStats;
import static view.Helper.getImage;
import static view.Helper.getInteger;
import static view.Helper.insertStandingsEntry;
import static view.Helper.leagueStats;
import static view.Helper.setColumnWidths;
import static view.Helper.setTableValues;
import static view.View.historyView;
import static view.View.rankedView;
import static view.View.seasonalView;

class Seasonal {
    private static final String FONT_NAME = "Times New Roman";
    private static final int FONT_SIZE = 22;
    private static final String[] LEAGUES = {England.LEAGUE, Spain.LEAGUE, Germany.LEAGUE, Italy.LEAGUE, France.LEAGUE,
            CHAMPIONS_LEAGUE_NAME, EUROPA_LEAGUE_NAME};
    private static final String[] COMPETITIONS = {"League", "League Cup", "National Cup"};
    private static final String[] PHASES = {"Group Stage", "Knockout"};
    private static final String[] ROUNDS = IntStream.range(1, 39).mapToObj(String::valueOf).toArray(String[]::new);
    private static final String[] STAGES = {"Round of 32", "Round of 16", "Quarter-finals", "Semi-finals", "Final"};
    private static final String[] GROUPS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
    private static final String[] STATS = {"N", "PLAYER", "TEAM", "COUNT"};
    private static final String[] STANDINGS = {"N", "TEAM", "G", "W", "D", "L", "GS", "GA", "GD", "P"};
    private static final JComboBox<String> nationBox = new JComboBox<>(LEAGUES);
    private static final JComboBox<String> competitionBox = new JComboBox<>(COMPETITIONS);
    private static final JComboBox<String> continentalBox = new JComboBox<>(PHASES);
    private static final JComboBox<String> roundBox = new JComboBox<>(ROUNDS);
    private static final JComboBox<String> knockoutBox = new JComboBox<>(STAGES);
    private static final JComboBox<String> groupBox = new JComboBox<>(GROUPS);
    private static final JLabel resultsLabel = new JLabel();
    private static final JTable standingsTable = new JTable(new String[20][10], STANDINGS);
    private static final JTable gamesTable = new JTable(new String[11][10], new String[]{"STATS", "COUNT"});

    private static String reportString = "";
    private static int width;
    private static int height;
    private static int resultsX;
    private static int resultsY;
    private static int resultsWidth;
    private static int resultsHeight;
    private static int goalsY;
    private static int assistsY;
    private static int cleanSheetsX;
    private static int statsWidth;
    private static int statsRowHeight;
    private static int statsHeight;
    private static int statsFontSize;
    private static int standingsX;
    private static int standingsY;
    private static int standingsWidth;
    private static int standingsHeight;
    private static int standingsRowHeight;
    private static int standingsFontSize;
    private static int gamesY;
    private static int gamesWidth;
    private static int gamesHeight;
    private static int firstBoxX;
    private static int secondBoxX;
    private static int thirdBoxX;
    private static int boxY;
    private static int longBoxWidth;
    private static int shortBoxWidth;
    private static int boxHeight;
    private static int boxFontSize;

    private static final JTable goalsTable = new JTable(new String[10][4], STATS);
    private static final JTable assistsTable = new JTable(new String[10][4], STATS);
    private static final JTable ratingsTable = new JTable(new String[10][4], STATS);
    private static final JTable cleanSheetsTable = new JTable(new String[10][4], STATS);

    static void setup(final int frameWidth, final int frameHeight) {
        calculatePositions(frameWidth, frameHeight);

        resultsLabel.setBounds(resultsX, resultsY, resultsWidth, resultsHeight);
        resultsLabel.setFont(new Font(FONT_NAME, PLAIN, resultsHeight / 14));
        resultsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                changeReportType();
            }
        });

        final TitledBorder borderResults = BorderFactory.createTitledBorder("RESULTS");
        borderResults.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));
        final JScrollPane resultsPane = new JScrollPane(resultsLabel);
        resultsPane.setBorder(borderResults);
        resultsPane.setBounds(resultsX, resultsY, resultsWidth, resultsHeight);
        seasonalView.add(resultsPane);

        placeBox(nationBox, firstBoxX, longBoxWidth);
        placeBox(competitionBox, secondBoxX, shortBoxWidth);
        placeBox(continentalBox, secondBoxX, shortBoxWidth);
        placeBox(roundBox, thirdBoxX, shortBoxWidth);
        placeBox(knockoutBox, thirdBoxX, shortBoxWidth);
        placeBox(groupBox, thirdBoxX, shortBoxWidth);

        setStatTables(goalsTable, resultsX, goalsY, "GOALS");
        setStatTables(assistsTable, resultsX, assistsY, "ASSISTS");
        setStatTables(ratingsTable, cleanSheetsX, goalsY, "RATINGS");
        setStatTables(cleanSheetsTable, cleanSheetsX, assistsY, "CLEAN SHEETS");

        standingsTable.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);
        standingsTable.setRowHeight(standingsRowHeight);
        standingsTable.setEnabled(false);
        standingsTable.setFont(new Font(FONT_NAME, PLAIN, standingsFontSize));
        setColumnWidths(standingsTable, standingsWidth, 6, 38, 7, 7, 7, 7, 7, 7, 7, 7);

        final JScrollPane standingsPane = new JScrollPane(standingsTable);
        final TitledBorder borderStandings = BorderFactory.createTitledBorder("STANDINGS");
        borderStandings.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));
        standingsPane.setBorder(borderStandings);
        standingsPane.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);
        seasonalView.add(standingsPane);

        gamesTable.setBounds(standingsX, gamesY, gamesWidth, gamesHeight);
        gamesTable.setRowHeight(statsRowHeight);
        gamesTable.setEnabled(false);
        gamesTable.setFont(new Font(FONT_NAME, PLAIN, statsFontSize));
        gamesTable.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);

        setTableValues(gamesTable, "Games", "Home Wins", "Draws", "Away Wins", "Home Goals", "Away Goals",
                "Assists", "Yellow Cards", "Red Cards", "Average Rating", "Clean Sheets");

        setColumnWidths(gamesTable, gamesWidth, 70, 30);
        final JScrollPane gamesPane = new JScrollPane(gamesTable);
        gamesPane.setBounds(standingsX, gamesY, gamesWidth, gamesHeight);
        seasonalView.add(gamesPane);

        final JButton nextButton = new JButton("Next");
        nextButton.setBounds(7 * width / 8, 9 * height / 10, width / 12, height / 12);
        nextButton.setFont(new Font(FONT_NAME, PLAIN, FONT_SIZE));
        nextButton.addActionListener(e -> nextRound());
        seasonalView.add(nextButton);

        final JButton rankedButton = new JButton("Rankings");
        rankedButton.setBounds(3 * width / 4, 9 * height / 10, width / 10, height / 12);
        rankedButton.setFont(new Font(FONT_NAME, PLAIN, FONT_SIZE));
        rankedButton.addActionListener(e -> rankedView());
        seasonalView.add(rankedButton);

        final JButton historicalButton = new JButton("History");
        historicalButton.setBounds(7 * width / 8, 4 * height / 5, width / 12, height / 12);
        historicalButton.setFont(new Font(FONT_NAME, PLAIN, FONT_SIZE));
        historicalButton.addActionListener(e -> historyView());
        seasonalView.add(historicalButton);

        try {
            seasonalView.add(getImage("/current.jpg", width, height));
        } catch (IOException e) {
            System.out.println("Exception thrown while extracting images! " + e);
        }
    }

    private static void setStatTables(final JTable table, final int x, final int y, final String label) {
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
        seasonalView.add(scrollPane);
    }

    private static void nextRound() {
        for (int i = 0; i < MATCHDAYS.size(); i++) proceed();
        update();
    }

    private static void changeReportType() {
        update();
        reportString = reportString.equals("") ? "reports: " : "";
    }

    static void update() {
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

    private static void leagueView(final Club[] league) {
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
        resultsLabel.setText("<html>" + leagueResults.getOrDefault(
                reportString + leagueName + round, "") + "</html>");
        System.out.println(leagueResults.get(leagueName));
    }

    private static void cupView(final Club[] league, final boolean leagueCup, final int teams) {
        boxVisibility(1);

        final String leagueName = league[0].getLeague();
        if (leagueCup) {
            resultsLabel.setText("<html>" + leagueCupResults.getOrDefault(
                    reportString + leagueName + teams, "") + "</html>");
            System.out.println(leagueCupResults.get(leagueName));
        } else {
            resultsLabel.setText("<html>" + nationalCupResults.getOrDefault(
                    reportString + leagueName + teams, "") + "</html>");
            System.out.println(nationalCupResults.get(leagueName));
        }
    }

    private static void continentalView(final String competition, final int teams) {
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
                    reportString + competition + teams, "") + "</html>");
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
                    reportString + competition + groupBox.getSelectedItem(), "") + "</html>");
        }

        System.out.println(continentalCupResults);
        if (competition.equals(CHAMPIONS_LEAGUE_NAME)) playerStats(CHAMPIONS_LEAGUE, 3, true);
        else playerStats(EUROPA_LEAGUE, 4, true);

        displayStats(goalsTable, goals, false);
        displayStats(assistsTable, assists, false);
        displayStats(ratingsTable, ratings, true);
        displayStats(cleanSheetsTable, cleanSheets, false);
    }

    private static void boxVisibility(final int competition) {
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

    private static void placeBox(final JComboBox<String> box, final int x, final int width) {
        box.setBounds(x, boxY, width, boxHeight);
        box.setFont(new Font(FONT_NAME, PLAIN, boxFontSize));
        box.addActionListener(e -> update());
        seasonalView.add(box);
    }

    private static void calculatePositions(final int frameWidth, final int frameHeight) {
        width = frameWidth;
        height = frameHeight;
        resultsX = width / 80;
        resultsY = height / 15;
        resultsWidth = width / 2;
        resultsHeight = 7 * height / 20;
        goalsY = 33 * height / 80;
        assistsY = 55 * height / 80;
        cleanSheetsX = 7 * width / 25;
        statsWidth = 2 * width / 9;
        statsHeight = 7 * height / 25;
        statsRowHeight = statsHeight / 15;
        statsFontSize = statsHeight / 17;
        standingsX = 21 * width / 40;
        standingsY = height / 10;
        standingsWidth = 9 * width / 20;
        standingsHeight = 3 * height / 5;
        standingsRowHeight = standingsHeight / 23;
        standingsFontSize = standingsHeight / 30;
        gamesY = 7 * height / 10;
        gamesWidth = standingsWidth / 3;
        gamesHeight = height / 4;
        firstBoxX = 9 * width / 25;
        secondBoxX = 19 * width / 40;
        thirdBoxX = 23 * width / 40;
        boxY = height / 80;
        longBoxWidth = width / 9;
        shortBoxWidth = width / 11;
        boxHeight = height / 16;
        boxFontSize = height / 50;
    }
}
