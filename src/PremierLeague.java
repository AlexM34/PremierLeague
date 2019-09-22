import competitions.England;
import competitions.France;
import competitions.Germany;
import competitions.Italy;
import competitions.Spain;
import players.Footballer;
import simulation.Controller;
import teams.Club;
import teams.League;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import java.awt.Color;
import java.awt.Font;
import java.util.Map;

import static simulation.Printer.assists;
import static simulation.Printer.cleanSheets;
import static simulation.Printer.goals;
import static simulation.Printer.ratings;
import static simulation.Printer.playerStats;
import static simulation.Utils.sortLeague;

class PremierLeague {
    // TODO: Name of the app
    // TODO: Cup and CL
    private final JFrame frame;
    private final JComboBox leagueBox;
    private final JComboBox competitionBox;
    private final JLabel resultsLabel;
    private final String[] stats = {"N", "PLAYER", "COUNT"};
    private final JTable goalsTable = new JTable(new String[10][3], stats);
    private final JTable assistsTable = new JTable(new String[10][3], stats);
    private final JTable ratingsTable = new JTable(new String[10][3], stats);
    private final JTable cleanSheetsTable = new JTable(new String[10][3], stats);
    private final JTable standingsTable;

    private static final Color COLOR = Color.getHSBColor(0.6f, 0.9f, 0.95f);
    private static final String FONT_NAME = "Times New Roman";
    private static final int FONT_SIZE = 19;

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
    private static int statsFontSize;
    private static int standingsFontSize;

    private PremierLeague() {
        Controller.initialise();

        frame = new JFrame("Football Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        calculatePositions(frame.getWidth(), frame.getHeight());
        frame.getContentPane().setBackground(COLOR);

        final int width = frame.getWidth();
        final int height = frame.getHeight();

        String[] leagues = {"Premier League", "La Liga", "Bundesliga", "Serie A", "Ligue 1"};
        leagueBox = new JComboBox(leagues);
        leagueBox.setBounds(2 * width / 5, height / 80, width / 11, height / 16);
        leagueBox.setFont(new Font(FONT_NAME, Font.PLAIN, height / 50));

        String[] competitions = {"League", "Cup"};
        competitionBox = new JComboBox(competitions);
        competitionBox.setBounds(10 * width / 20, height / 80, width / 11, height / 16);
        competitionBox.setFont(new Font(FONT_NAME, Font.PLAIN, height / 50));

        resultsLabel = new JLabel();
        resultsLabel.setBounds(resultsX, resultsY, resultsWidth, resultsHeight);
        TitledBorder borderResults = BorderFactory.createTitledBorder("RESULTS");
        borderResults.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        resultsLabel.setBorder(borderResults);
        resultsLabel.setFont(new Font(FONT_NAME, Font.PLAIN, resultsHeight / 14));
        resultsLabel.setForeground(Color.WHITE);

        setStatTables(goalsTable, resultsX, goalsY, "GOALS");
        setStatTables(assistsTable, resultsX, assistsY, "ASSISTS");
        setStatTables(ratingsTable, cleanSheetsX, goalsY, "RATINGS");
        setStatTables(cleanSheetsTable, cleanSheetsX, assistsY, "CLEAN SHEETS");

        String[] standings = {"N", "TEAM", "G", "W", "D", "L", "GS", "GA", "GD", "P"};
        standingsTable = new JTable(new String[20][10], standings);
        standingsTable.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);
        standingsTable.setRowHeight(standingsHeight / 23);
        standingsTable.setEnabled(false);
        standingsTable.setFont(new Font(FONT_NAME, Font.PLAIN, standingsFontSize));
        setJTableColumnsWidth(standingsTable, standingsWidth, 6, 38, 7, 7, 7, 7, 7, 7, 7, 7);
        JScrollPane tableScrollPane = new JScrollPane(standingsTable);
        TitledBorder borderStandings = BorderFactory.createTitledBorder("STANDINGS");
        borderStandings.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        tableScrollPane.setBorder(borderStandings);
        tableScrollPane.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);

        final JButton nextButton = new JButton("Next");
        nextButton.setBounds(7 * width / 8, 9 * height / 10, width / 12, height / 12);

        frame.add(leagueBox);
        frame.add(competitionBox);
        frame.add(nextButton);
        frame.add(resultsLabel);
        frame.add(tableScrollPane);

        nextButton.addActionListener(e -> nextRound());
        leagueBox.addActionListener(e -> updateStats());
        competitionBox.addActionListener(e -> updateStats());

        updateStats();
    }

    private void setStatTables(final JTable table, final int x, final int y, final String label) {
        table.setBounds(x, y, statsWidth, statsHeight);
        table.setRowHeight(statsHeight / 15);
        table.setEnabled(false);
        table.setFont(new Font(FONT_NAME, Font.PLAIN, statsFontSize));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setJTableColumnsWidth(table, statsWidth, 15, 60, 25);

        JScrollPane scrollPane = new JScrollPane(table);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(label);
        titledBorder.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        scrollPane.setBorder(titledBorder);
        scrollPane.setBounds(x, y, statsWidth, statsHeight);
        frame.add(scrollPane);
    }

    private void calculatePositions(final int width, final int height) {
        resultsX = width / 80;
        resultsY = height / 15;
        resultsWidth = width / 2;
        resultsHeight = 7 * height / 20;
        goalsY = 9 * height / 20;
        statsWidth = 2 * width / 9;
        statsHeight = height / 4;
        assistsY = 7 * height / 10;
        cleanSheetsX = 7 * width / 25;
        standingsX = 21 * width / 40;
        standingsY = height / 10;
        standingsWidth = 9 * width / 20;
        standingsHeight = 3 * height / 5;
        statsFontSize = statsHeight / 15;
        standingsFontSize = standingsHeight / 30;
    }

    private void updateStats() {
        Club[] league;
        switch (String.valueOf(this.leagueBox.getSelectedItem())) {
            case England.LEAGUE: league = England.CLUBS; break;
            case Spain.LEAGUE: league = Spain.CLUBS; break;
            case Germany.LEAGUE: league = Germany.CLUBS; break;
            case Italy.LEAGUE: league = Italy.CLUBS; break;
            case France.LEAGUE: league = France.CLUBS; break;
            default: return;
        }

        final Map<Club, Integer> sorted = sortLeague(league);
        int row = 0;
        for (final Club team : sorted.keySet()) {
            final League stats = team.getSeason().getLeague();

            standingsTable.setValueAt("" + (row + 1), row, 0);
            standingsTable.setValueAt(team.getName(), row, 1);
            standingsTable.setValueAt("" + stats.getMatches(), row, 2);
            standingsTable.setValueAt("" + stats.getWins(), row, 3);
            standingsTable.setValueAt("" + stats.getDraws(), row, 4);
            standingsTable.setValueAt("" + stats.getLosses(), row, 5);
            standingsTable.setValueAt("" + stats.getScored(), row, 6);
            standingsTable.setValueAt("" + stats.getConceded(), row, 7);
            standingsTable.setValueAt("" + (stats.getScored() - stats.getConceded()), row, 8);
            standingsTable.setValueAt("" + stats.getPoints(), row++, 9);
        }

        for (int i = row; i < 20; i++) {
            for (int j = 0; j < standingsTable.getColumnCount(); j++) {
                standingsTable.setValueAt("", i, j);
            }
        }

        playerStats(league, String.valueOf(this.competitionBox.getSelectedItem()).equals("League") ? 0 : 1);
        displayStats(goalsTable, goals);
        displayStats(assistsTable, assists);
        displayStats(ratingsTable, ratings);
        displayStats(cleanSheetsTable, cleanSheets);

        resultsLabel.setText("<html>" + Controller.results.getOrDefault(leagueBox.getSelectedItem(), "") + "</html>");
        System.out.println(Controller.results.get(leagueBox.getSelectedItem()));
    }

    private void displayStats(final JTable table, final Map<Footballer, Integer> map) {
        int row = 0;
        for (final Footballer footballer : map.keySet()) {
            if (row > 9 || map.getOrDefault(footballer, 0) == 0) break;

            table.setValueAt("" + (row + 1), row, 0);
            table.setValueAt(footballer.getName(), row, 1);
            table.setValueAt("" + map.getOrDefault(footballer, 0), row++, 2);
        }

        for (int i = row; i < 10; i++) {
            table.setValueAt("", i, 0);
            table.setValueAt("", i, 1);
            table.setValueAt("", i, 2);
        }
    }

    private void nextRound() {
        Controller.proceed();
        updateStats();
    }

    private static void setJTableColumnsWidth(JTable table, int tablePreferredWidth,
                                              double... percentages) {
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(PremierLeague::new);
    }
}
