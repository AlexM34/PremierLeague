import competitions.England;
import competitions.France;
import competitions.Germany;
import competitions.Italy;
import competitions.Spain;
import players.Footballer;
import simulation.Controller;
import simulation.Data;
import teams.Club;
import teams.League;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import java.awt.Color;
import java.awt.Font;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Random;

import static simulation.Data.averageRatings;
import static simulation.Data.awayWins;
import static simulation.Data.draws;
import static simulation.Data.homeWins;
import static simulation.Data.redCards;
import static simulation.Data.scoredAway;
import static simulation.Data.scoredHome;
import static simulation.Data.yellowCards;
import static simulation.Printer.assists;
import static simulation.Printer.cleanSheets;
import static simulation.Printer.goals;
import static simulation.Printer.playerStats;
import static simulation.Printer.ratings;
import static simulation.Utils.sortLeague;

class PremierLeague {
    // TODO: Name of the app
    // TODO: Champions League
    // TODO: Ignore class files
    private static final String[] STATS = {"N", "PLAYER", "COUNT"};
    private static final Color COLOR = Color.getHSBColor(0.6f, 0.9f, 0.95f);
    private static final String FONT_NAME = "Times New Roman";
    private static final int FONT_SIZE = 25;

    private final JFrame frame;
    private final JComboBox leagueBox;
    private final JComboBox competitionBox;
    private final JLabel resultsLabel;
    private final JTable goalsTable = new JTable(new String[10][3], STATS);
    private final JTable assistsTable = new JTable(new String[10][3], STATS);
    private final JTable ratingsTable = new JTable(new String[10][3], STATS);
    private final JTable cleanSheetsTable = new JTable(new String[10][3], STATS);
    private final JTable standingsTable;
    private final JTable gamesTable;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

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
    private static int statsFontSize;
    private static int standingsFontSize;
    private static int song = 0;

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

        String[] competitions = {"League", "League Cup", "National Cup"};
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
        JScrollPane standingsPane = new JScrollPane(standingsTable);
        TitledBorder borderStandings = BorderFactory.createTitledBorder("STANDINGS");
        borderStandings.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        standingsPane.setBorder(borderStandings);
        standingsPane.setBounds(standingsX, standingsY, standingsWidth, standingsHeight);

        gamesTable = new JTable(new String[11][10], new String[]{"STATS", "COUNT"});
        gamesTable.setBounds(standingsX, gamesY, gamesWidth, gamesHeight);
        gamesTable.setRowHeight(statsHeight / 13);
        gamesTable.setEnabled(false);
        gamesTable.setFont(new Font(FONT_NAME, Font.PLAIN, statsFontSize));
        gamesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setJTableColumnsWidth(gamesTable, gamesWidth, 70, 30);
        JScrollPane gamesPane = new JScrollPane(gamesTable);
        gamesPane.setBounds(standingsX, gamesY, gamesWidth, gamesHeight);

        final JButton nextButton = new JButton("Next");
        nextButton.setBounds(7 * width / 8, 9 * height / 10, width / 12, height / 12);
        nextButton.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));

        frame.add(leagueBox);
        frame.add(competitionBox);
        frame.add(nextButton);
        frame.add(resultsLabel);
        frame.add(standingsPane);
        frame.add(gamesPane);

        nextButton.addActionListener(e -> nextRound());
        leagueBox.addActionListener(e -> updateStats());
        competitionBox.addActionListener(e -> updateStats());

        playMusic();
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
        statsFontSize = statsHeight / 16;
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

        if (String.valueOf(this.competitionBox.getSelectedItem()).equals("League")) {
            leagueView(league);
        } else {
            cupView(league, this.competitionBox.getSelectedItem().equals("League Cup"));
        }

        playerStats(league, String.valueOf(this.competitionBox.getSelectedItem()).equals("League") ? 0 : 1);
        displayStats(goalsTable, goals, false);
        displayStats(assistsTable, assists, false);
        displayStats(ratingsTable, ratings, true);
        displayStats(cleanSheetsTable, cleanSheets, false);
    }

    private void leagueView(final Club[] league) {
        final String leagueName = league[0].getLeague();
        final Map<Club, Integer> sorted = sortLeague(league);
        int row = 0;
        for (final Club team : sorted.keySet()) {
            final League stats = team.getSeason().getLeague();

            standingsTable.setValueAt(String.valueOf(row + 1), row, 0);
            standingsTable.setValueAt(team.getName(), row, 1);
            standingsTable.setValueAt(String.valueOf(stats.getMatches()), row, 2);
            standingsTable.setValueAt(String.valueOf(stats.getWins()), row, 3);
            standingsTable.setValueAt(String.valueOf(stats.getDraws()), row, 4);
            standingsTable.setValueAt(String.valueOf(stats.getLosses()), row, 5);
            standingsTable.setValueAt(String.valueOf(stats.getScored()), row, 6);
            standingsTable.setValueAt(String.valueOf(stats.getConceded()), row, 7);
            standingsTable.setValueAt(String.valueOf((stats.getScored() - stats.getConceded())), row, 8);
            standingsTable.setValueAt(String.valueOf(stats.getPoints()), row++, 9);
        }

        for (int i = row; i < 20; i++) {
            for (int j = 0; j < standingsTable.getColumnCount(); j++) {
                standingsTable.setValueAt("", i, j);
            }
        }

        if (homeWins == null) return;

        final int games = homeWins.getOrDefault(leagueName, 0)
                + draws.getOrDefault(leagueName, 0)
                + awayWins.getOrDefault(leagueName, 0);

        gamesTable.setValueAt("Games", 0, 0);
        gamesTable.setValueAt(String.valueOf(games), 0, 1);

        gamesTable.setValueAt("Home Wins", 1, 0);
        gamesTable.setValueAt(String.valueOf(homeWins.getOrDefault(leagueName, 0)), 1, 1);

        gamesTable.setValueAt("Draws", 2, 0);
        gamesTable.setValueAt(String.valueOf(draws.getOrDefault(leagueName, 0)), 2, 1);

        gamesTable.setValueAt("Away Wins", 3, 0);
        gamesTable.setValueAt(String.valueOf(awayWins.getOrDefault(leagueName, 0)), 3, 1);

        gamesTable.setValueAt("Home Goals", 4, 0);
        gamesTable.setValueAt(String.valueOf(scoredHome.getOrDefault(leagueName, 0)), 4, 1);

        gamesTable.setValueAt("Away Goals", 5, 0);
        gamesTable.setValueAt(String.valueOf(scoredAway.getOrDefault(leagueName, 0)), 5, 1);

        gamesTable.setValueAt("Assists", 6, 0);
        gamesTable.setValueAt(String.valueOf(Data.assists.getOrDefault(leagueName, 0)), 6, 1);

        gamesTable.setValueAt("Yellow Cards", 7, 0);
        gamesTable.setValueAt(String.valueOf(yellowCards.getOrDefault(leagueName, 0)), 7, 1);

        gamesTable.setValueAt("Red Cards", 8, 0);
        gamesTable.setValueAt(String.valueOf(redCards.getOrDefault(leagueName, 0)), 8, 1);

        gamesTable.setValueAt("Average Ratings", 9, 0);
        gamesTable.setValueAt(df.format(averageRatings.getOrDefault(leagueName, 0.0f) / (games * 22)), 9, 1);

        gamesTable.setValueAt("Clean Sheets", 10, 0);
        gamesTable.setValueAt(String.valueOf(Data.cleanSheets.getOrDefault(leagueName, 0)), 10, 1);

        resultsLabel.setText("<html>" + Controller.leagueResults.getOrDefault(leagueName, "") + "</html>");
        System.out.println(Controller.leagueResults.get(leagueName));
    }

    private void cupView(final Club[] league, final boolean leagueCup) {
        final String leagueName = league[0].getLeague();
        if (leagueCup) {
            resultsLabel.setText("<html>" + Controller.leagueCupResults.getOrDefault(leagueName, "") + "</html>");
            System.out.println(Controller.leagueCupResults.get(leagueName));
        } else {
            resultsLabel.setText("<html>" + Controller.nationalCupResults.getOrDefault(leagueName, "") + "</html>");
            System.out.println(Controller.nationalCupResults.get(leagueName));
        }
    }

    private void displayStats(final JTable table, final Map<Footballer, Integer> map, final boolean format) {
        int row = 0;
        for (final Footballer footballer : map.keySet()) {
            if (row > 9 || map.getOrDefault(footballer, 0) == 0) break;

            table.setValueAt(String.valueOf(row + 1), row, 0);
            table.setValueAt(footballer.getName(), row, 1);

            if (format) {
                table.setValueAt(String.valueOf(df.format((float) map.getOrDefault(footballer, 0) / 100)), row++, 2);
            } else {
                table.setValueAt(String.valueOf(map.getOrDefault(footballer, 0)), row++, 2);
            }
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

    private void playMusic() {
        int r;
        do {
            r = new Random().nextInt(5) + 1;
        } while (r == song);

        song = r;
        final String fileName = song + ".wav";
        try {
            final URL url = getClass().getResource(fileName);
            final AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);

            final Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            clip.loop(0);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    playMusic();
                }
            });

        } catch (final Exception e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
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
