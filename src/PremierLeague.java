import competitions.*;
import players.Footballer;
import simulation.Controller;
import simulation.Printer;
import teams.Club;
import teams.League;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Map;

import static simulation.Printer.*;

class PremierLeague {
    // TODO: Cup and CL
    private final JComboBox leagueBox;
    private final JLabel resultsLabel;
    private final JTable goalsTable;
    private final JTable assistsTable;
    private final JTable ratingsTable;
    private final JTable cleanSheetsTable;
    private final JTable standingsTable;

    private static final Color COLOR = Color.getHSBColor(0.6f, 0.9f, 0.95f);
    private static final String FONT_NAME = "Times New Roman";
    private static final int FONT_SIZE = 20;

    private static int resultsX;
    private static int resultsY;
    private static int resultsWidth;
    private static int resultsHeight;
    private static int goalsY;
    private static int goalsWidth;
    private static int goalsHeight;
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

        final JFrame frame = new JFrame("Football Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setUndecorated(true);
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        frame.setSize(screenSize.width, screenSize.height);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        calculatePositions(frame.getWidth(), frame.getHeight());
        frame.getContentPane().setBackground(COLOR);

        final int width = frame.getWidth();
        final int height = frame.getHeight();

        String[] leagues = {"Premier League", "La Liga", "Bundesliga", "Serie A", "Ligue 1"};
        leagueBox = new JComboBox(leagues);
        leagueBox.setBounds(9 * width / 20, height / 80, width / 11, height / 16);
        leagueBox.setFont(new Font(FONT_NAME, Font.PLAIN, height / 50));

        resultsLabel = new JLabel();
        resultsLabel.setBounds(resultsX, resultsY, resultsWidth, resultsHeight);
        TitledBorder borderResults = BorderFactory.createTitledBorder("RESULTS");
        borderResults.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        resultsLabel.setBorder(borderResults);
        resultsLabel.setFont(new Font(FONT_NAME, Font.PLAIN, resultsHeight / 14));
        resultsLabel.setForeground(Color.WHITE);

        String[] stats = {"N", "PLAYER", "COUNT"};

        goalsTable = new JTable(new String[10][3], stats);
        goalsTable.setBounds(resultsX, goalsY, goalsWidth, goalsHeight);
        goalsTable.setRowHeight(goalsHeight / 14);
        goalsTable.setEnabled(false);
        goalsTable.setFont(new Font(FONT_NAME, Font.PLAIN, statsFontSize));
        goalsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setJTableColumnsWidth(goalsTable, goalsWidth, 15, 60, 25);
        JScrollPane tableGoalsScrollPane = new JScrollPane(goalsTable);
        TitledBorder borderGoals = BorderFactory.createTitledBorder("GOALS");
        borderGoals.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        tableGoalsScrollPane.setBorder(borderGoals);
        tableGoalsScrollPane.setBounds(resultsX, goalsY, goalsWidth, goalsHeight);

        assistsTable = new JTable(new String[10][3], stats);
        assistsTable.setBounds(resultsX, assistsY, goalsWidth, goalsHeight);
        assistsTable.setRowHeight(goalsHeight / 14);
        assistsTable.setEnabled(false);
        assistsTable.setFont(new Font(FONT_NAME, Font.PLAIN, statsFontSize));
        setJTableColumnsWidth(assistsTable, goalsWidth, 15, 60, 25);
        JScrollPane tableAssistsScrollPane = new JScrollPane(assistsTable);
        TitledBorder borderAssists = BorderFactory.createTitledBorder("ASSISTS");
        borderAssists.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        tableAssistsScrollPane.setBorder(borderAssists);
        tableAssistsScrollPane.setBounds(resultsX, assistsY, goalsWidth, goalsHeight);
//        tableAssistsScrollPane.setForeground(Color.getHSBColor(0.6f, 0.9f, 0.95f));

        ratingsTable = new JTable(new String[10][3], stats);
        ratingsTable.setBounds(cleanSheetsX, goalsY, goalsWidth, goalsHeight);
        ratingsTable.setRowHeight(goalsHeight / 14);
        ratingsTable.setEnabled(false);
        ratingsTable.setFont(new Font(FONT_NAME, Font.PLAIN, statsFontSize));
        setJTableColumnsWidth(ratingsTable, goalsWidth, 15, 60, 25);
        JScrollPane tableRatingsScrollPane = new JScrollPane(ratingsTable);
        TitledBorder borderRatings = BorderFactory.createTitledBorder("RATINGS");
        borderRatings.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        tableRatingsScrollPane.setBorder(borderRatings);
        tableRatingsScrollPane.setBounds(cleanSheetsX, goalsY, goalsWidth, goalsHeight);

        cleanSheetsTable = new JTable(new String[10][3], stats);
        cleanSheetsTable.setBounds(cleanSheetsX, assistsY, goalsWidth, goalsHeight);
        cleanSheetsTable.setRowHeight(goalsHeight / 14);
        cleanSheetsTable.setEnabled(false);
        cleanSheetsTable.setFont(new Font(FONT_NAME, Font.PLAIN, statsFontSize));
        setJTableColumnsWidth(cleanSheetsTable, width / 4, 15, 60, 25);
        JScrollPane tableCleanSheetsScrollPane = new JScrollPane(cleanSheetsTable);
        TitledBorder borderCleanSheets = BorderFactory.createTitledBorder("CLEAN SHEETS");
        borderCleanSheets.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        tableCleanSheetsScrollPane.setBorder(borderCleanSheets);
        tableCleanSheetsScrollPane.setBounds(cleanSheetsX, assistsY, goalsWidth, goalsHeight);

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
        frame.add(nextButton);
        frame.add(resultsLabel);
        frame.add(tableGoalsScrollPane);
        frame.add(tableAssistsScrollPane);
        frame.add(tableRatingsScrollPane);
        frame.add(tableCleanSheetsScrollPane);
        frame.add(tableScrollPane);

        nextButton.addActionListener(e -> nextRound());
        leagueBox.addActionListener(e -> updateStats());

        updateStats();
    }

    private void calculatePositions(final int width, final int height) {
        resultsX = width / 80;
        resultsY = height / 15;
        resultsWidth = width / 2;
        resultsHeight = 7 * height / 20;
        goalsY = 9 * height / 20;
        goalsWidth = 2 * width / 9;
        goalsHeight = height / 4;
        assistsY = 7 * height / 10;
        cleanSheetsX = 7 * width / 25;
        standingsX = 21 * width / 40;
        standingsY = height / 10;
        standingsWidth = 9 * width / 20;
        standingsHeight = 3 * height / 5;
        statsFontSize = goalsHeight / 15;
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

        final Map<Club, Integer> sorted = Printer.sortLeague(league);
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

        playerStats(league, 0);

        row = 0;
        for (final Footballer footballer : goals.keySet()) {
            goalsTable.setValueAt("" + (row + 1), row, 0);
            goalsTable.setValueAt(footballer.getName(), row, 1);
            goalsTable.setValueAt("" + goals.getOrDefault(footballer, 0), row, 2);

            if (++row > 9) break;
        }

        row = 0;
        for (final Footballer footballer : assists.keySet()) {
            assistsTable.setValueAt("" + (row + 1), row, 0);
            assistsTable.setValueAt(footballer.getName(), row, 1);
            assistsTable.setValueAt("" + assists.getOrDefault(footballer, 0), row, 2);

            if (++row > 9) break;
        }

        row = 0;
        for (final Footballer footballer : ratings.keySet()) {
            ratingsTable.setValueAt("" + (row + 1), row, 0);
            ratingsTable.setValueAt(footballer.getName(), row, 1);
            ratingsTable.setValueAt("" + ratings.getOrDefault(footballer, 0), row, 2);

            if (++row > 9) break;
        }

        row = 0;
        for (final Footballer footballer : cleanSheets.keySet()) {
            cleanSheetsTable.setValueAt("" + (row + 1), row, 0);
            cleanSheetsTable.setValueAt(footballer.getName(), row, 1);
            cleanSheetsTable.setValueAt("" + cleanSheets.getOrDefault(footballer, 0), row, 2);

            if (++row > 9) break;
        }

        resultsLabel.setText("<html>" + Controller.results.getOrDefault(leagueBox.getSelectedItem(), "") + "</html>");
        System.out.println(Controller.results.get(leagueBox.getSelectedItem()));
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
