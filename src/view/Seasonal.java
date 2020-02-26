package view;

import league.LeagueManager;
import team.Club;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import static javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS;
import static league.LeagueManager.getClubs;
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
import static view.Helper.getInteger;
import static view.Helper.insertStandingsEntry;
import static view.Helper.leagueStats;
import static view.ViewManager.historicalView;
import static view.ViewManager.rankedView;
import static view.ViewManager.seasonalView;

class Seasonal extends View {
    private static final String[] STATS = {"N", "PLAYER", "TEAM", "COUNT"};
    private static final String[] COMPETITIONS = {"League", "League Cup", "National Cup"};
    private static final String[] PHASES = {"Group Stage", "Knockout"};
    private static final String[] ROUNDS = IntStream.rangeClosed(1, 38)
            .mapToObj(String::valueOf).toArray(String[]::new);
    private static final String[] STAGES = Arrays.stream(Stage.values()).map(Stage::getName).toArray(String[]::new);
    private static final String[] GROUPS = IntStream.rangeClosed('A', 'L')
            .mapToObj(x -> String.valueOf((char) x)).toArray(String[]::new);
    private static final String[] STANDINGS = {"N", "TEAM", "G", "W", "D", "L", "GS", "GA", "GD", "P"};

    private static final JComboBox<String> NATION_BOX = new JComboBox<>(LeagueManager.getLeagues());
    private static final JComboBox<String> COMPETITION_BOX = new JComboBox<>(COMPETITIONS);
    private static final JComboBox<String> CONTINENTAL_BOX = new JComboBox<>(PHASES);
    private static final JComboBox<String> ROUND_BOX = new JComboBox<>(ROUNDS);
    private static final JComboBox<String> KNOCKOUT_BOX = new JComboBox<>(STAGES);
    private static final JComboBox<String> GROUP_BOX = new JComboBox<>(GROUPS);

    private static final JLabel RESULTS_LABEL = new JLabel();
    private static final JTable STANDINGS_TABLE = new JTable(new String[20][10], STANDINGS);
    private static final JTable GAMES_TABLE = new JTable(new String[11][10], new String[]{"STATS", "COUNT"});
    private static final JTable GOALS_TABLE = new JTable(new String[10][4], STATS);
    private static final JTable ASSISTS_TABLE = new JTable(new String[10][4], STATS);
    private static final JTable RATINGS_TABLE = new JTable(new String[10][4], STATS);
    private static final JTable CLEAN_SHEETS_TABLE = new JTable(new String[10][4], STATS);

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
    private static int nextButtonX;
    private static int nextButtonY;
    private static int nextButtonWidth;
    private static int buttonHeight;
    private static int rankedButtonX;
    private static int rankedButtonWidth;
    private static int historicalButtonY;

    static void setup(final int frameWidth, final int frameHeight) {
        calculatePositions(frameWidth, frameHeight);
        setResults();
        placeBoxes();
        setPlayerTables();
        setTeamTables();
        setButtons();
        loadImage();
    }

    private static void setTeamTables() {
        final JScrollPane trophiesPane = createTeamPane(STANDINGS_TABLE, "STANDINGS",
                6, 38, 7, 7, 7, 7, 7, 7, 7, 7);
        seasonalView.add(trophiesPane);

        GAMES_TABLE.setBounds(standingsX, gamesY, gamesWidth, gamesHeight);
        GAMES_TABLE.setRowHeight(statsRowHeight);
        GAMES_TABLE.setEnabled(false);
        GAMES_TABLE.setFont(new Font(FONT_NAME, PLAIN, statsFontSize));
        GAMES_TABLE.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);

        setTableValues(GAMES_TABLE, "Games", "Home Wins", "Draws", "Away Wins", "Home Goals", "Away Goals",
                "Assists", "Yellow Cards", "Red Cards", "Average Rating", "Clean Sheets");

        setColumnWidths(GAMES_TABLE, gamesWidth, 70, 30);
        final JScrollPane gamesPane = new JScrollPane(GAMES_TABLE);
        gamesPane.setBounds(standingsX, gamesY, gamesWidth, gamesHeight);
        seasonalView.add(gamesPane);
    }

    private static void setResults() {
        RESULTS_LABEL.setBounds(resultsX, resultsY, resultsWidth, resultsHeight);
        RESULTS_LABEL.setFont(new Font(FONT_NAME, PLAIN, resultsHeight / 14));
        RESULTS_LABEL.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                changeReportType();
            }
        });

        final TitledBorder borderResults = BorderFactory.createTitledBorder("RESULTS");
        borderResults.setTitleFont(new Font(FONT_NAME, ITALIC, FONT_SIZE));
        final JScrollPane resultsPane = new JScrollPane(RESULTS_LABEL);
        resultsPane.setBorder(borderResults);
        resultsPane.setBounds(resultsX, resultsY, resultsWidth, resultsHeight);
        seasonalView.add(resultsPane);
    }

    private static void setButtons() {
        addButton("Next", nextButtonX, nextButtonY, nextButtonWidth, buttonHeight, e -> nextRound());
        addButton("Rankings", rankedButtonX, nextButtonY, rankedButtonWidth, buttonHeight, e -> rankedView());
        addButton("History", nextButtonX, historicalButtonY, nextButtonWidth, buttonHeight, e -> historicalView());
    }

    private static void addButton(final String name, final int x, final int y,
                                  final int width, final int height, final ActionListener action) {

        final JButton button = createButton(name, x, y, width, height, action);
        seasonalView.add(button);
    }

    private static void loadImage() {
        seasonalView.add(getImage("/current.jpg", width, height));
    }

    private static void setPlayerTables() {
        addStatsPane(GOALS_TABLE, resultsX, goalsY, "GOALS");
        addStatsPane(ASSISTS_TABLE, resultsX, assistsY, "ASSISTS");
        addStatsPane(RATINGS_TABLE, cleanSheetsX, goalsY, "RATINGS");
        addStatsPane(CLEAN_SHEETS_TABLE, cleanSheetsX, assistsY, "CLEAN SHEETS");
    }

    private static void addStatsPane(final JTable table, final int x, final int y, final String label) {
        final JScrollPane pane = createStatsPane(table, x, y, label);
        seasonalView.add(pane);
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
        final String competition = String.valueOf(COMPETITION_BOX.getSelectedItem());
        final int teams = Stage.getTeams(String.valueOf(KNOCKOUT_BOX.getSelectedItem()));

        final String nation = String.valueOf(NATION_BOX.getSelectedItem());
        final boolean international = nation.equals(CHAMPIONS_LEAGUE_NAME) || nation.equals(EUROPA_LEAGUE_NAME);
        if (international) {
            continentalView(nation, teams);
            return;
        }

        final Club[] league = getClubs(String.valueOf(NATION_BOX.getSelectedItem()));

        if (competition.equals("League")) leagueView(league);
        else cupView(league, competition.equals("League Cup"), teams);

        playerStats(league, competition.equals("League") ? 0 : competition.equals("National Cup") ? 1 : 2, true);
        displayPlayerStats();
    }

    static void displayPlayerStats() {
        displayStats(GOALS_TABLE, goals, false);
        displayStats(ASSISTS_TABLE, assists, false);
        displayStats(RATINGS_TABLE, ratings, true);
        displayStats(CLEAN_SHEETS_TABLE, cleanSheets, false);
    }

    private static void leagueView(final Club[] league) {
        boxVisibility(0);

        final String leagueName = league[0].getLeague();
        final Map<Club, Integer> sorted = sortLeague(league, 0);
        int row = 0;
        for (final Club team : sorted.keySet()) {
            insertStandingsEntry(STANDINGS_TABLE, team.getName(), team.getSeason().getLeague(), row++);
        }

        for (int i = row; i < 20; i++) {
            for (int j = 0; j < STANDINGS_TABLE.getColumnCount(); j++) {
                STANDINGS_TABLE.setValueAt("", i, j);
            }
        }

        leagueStats(GAMES_TABLE, leagueName);

        final int round = getInteger(String.valueOf(ROUND_BOX.getSelectedItem()));
        RESULTS_LABEL.setText("<html>" + leagueResults.getOrDefault(
                reportString + leagueName + round, "") + "</html>");
        System.out.println(leagueResults.get(leagueName));
    }

    private static void cupView(final Club[] league, final boolean leagueCup, final int teams) {
        boxVisibility(1);

        final String leagueName = league[0].getLeague();
        if (leagueCup) {
            RESULTS_LABEL.setText("<html>" + leagueCupResults.getOrDefault(
                    reportString + leagueName + teams, "") + "</html>");
            System.out.println(leagueCupResults.get(leagueName));
        } else {
            RESULTS_LABEL.setText("<html>" + nationalCupResults.getOrDefault(
                    reportString + leagueName + teams, "") + "</html>");
            System.out.println(nationalCupResults.get(leagueName));
        }
    }

    private static void continentalView(final String competition, final int teams) {
        boxVisibility(2);

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < STANDINGS_TABLE.getColumnCount(); j++) {
                STANDINGS_TABLE.setValueAt("", i, j);
            }
        }

        final boolean isChampionsLeague = competition.equals(CHAMPIONS_LEAGUE_NAME);
        final int group = (int) String.valueOf(GROUP_BOX.getSelectedItem()).charAt(0) - 'A';
        if (String.valueOf(CONTINENTAL_BOX.getSelectedItem()).equals("Knockout")) {
            RESULTS_LABEL.setText("<html>" + continentalCupResults.getOrDefault(
                    reportString + competition + teams, "") + "</html>");
        } else if (!isChampionsLeague || group < 8) {
            final Club[] league = new Club[4];
            for (int team = 0; team < 4; team++) {
                league[team] = isChampionsLeague ? CHAMPIONS_LEAGUE[8 * team + group] : EUROPA_LEAGUE[12 * team + group];
            }

            final Map<Club, Integer> sorted = sortLeague(league, 3);
            int row = 0;
            for (final Club team : sorted.keySet()) {
                insertStandingsEntry(STANDINGS_TABLE, team.getName(), team.getSeason().getContinental().getGroup(), row++);
            }

            RESULTS_LABEL.setText("<html>" + continentalCupResults.getOrDefault(
                    reportString + competition + GROUP_BOX.getSelectedItem(), "") + "</html>");
        }

        System.out.println(continentalCupResults);
        if (competition.equals(CHAMPIONS_LEAGUE_NAME)) playerStats(CHAMPIONS_LEAGUE, 3, true);
        else playerStats(EUROPA_LEAGUE, 4, true);

        displayPlayerStats();
    }

    private static void boxVisibility(final int competition) {
        COMPETITION_BOX.setVisible(competition != 2);
        CONTINENTAL_BOX.setVisible(competition == 2);
        ROUND_BOX.setVisible(competition == 0);
        GAMES_TABLE.setVisible(competition == 0);

        final boolean knockout = competition == 1 ||
                (competition != 0 && String.valueOf(CONTINENTAL_BOX.getSelectedItem()).equals("Knockout"));

        KNOCKOUT_BOX.setVisible(knockout);
        GROUP_BOX.setVisible(!knockout);
        STANDINGS_TABLE.setVisible(!knockout);
    }

    private static void placeBoxes() {
        placeBox(NATION_BOX, firstBoxX, longBoxWidth);
        placeBox(COMPETITION_BOX, secondBoxX, shortBoxWidth);
        placeBox(CONTINENTAL_BOX, secondBoxX, shortBoxWidth);
        placeBox(ROUND_BOX, thirdBoxX, shortBoxWidth);
        placeBox(KNOCKOUT_BOX, thirdBoxX, shortBoxWidth);
        placeBox(GROUP_BOX, thirdBoxX, shortBoxWidth);
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
        nextButtonX = 7 * width / 8;
        nextButtonY = 9 * height / 10;
        nextButtonWidth = width / 12;
        buttonHeight = height / 12;
        buttonHeight = height / 12;
        rankedButtonX = 3 * width / 4;
        rankedButtonWidth = width / 10;
        historicalButtonY = 4 * height / 5;
    }
}
