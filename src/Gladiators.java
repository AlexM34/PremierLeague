import competition.England;
import competition.France;
import competition.Germany;
import competition.Italy;
import competition.Spain;
import players.Footballer;
import simulation.Controller;
import simulation.Data;
import team.Club;
import team.League;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import static simulation.Controller.CHAMPIONS_LEAGUE;
import static simulation.Controller.CHAMPIONS_LEAGUE_NAME;
import static simulation.Data.averageRatings;
import static simulation.Data.awayWins;
import static simulation.Data.draws;
import static simulation.Data.homeWins;
import static simulation.Data.redCards;
import static simulation.Data.scoredAway;
import static simulation.Data.scoredHome;
import static simulation.Data.yellowCards;
import static simulation.Helper.sortLeague;
import static simulation.Printer.assists;
import static simulation.Printer.cleanSheets;
import static simulation.Printer.goals;
import static simulation.Printer.playerStats;
import static simulation.Printer.ratings;

class Gladiators {
    private static final String[] STATS = {"N", "PLAYER", "COUNT"};
    private static final Color COLOR = Color.getHSBColor(0.6f, 0.9f, 0.95f);
    private static final String FONT_NAME = "Times New Roman";
    private static final int FONT_SIZE = 25;

    private final JFrame frame;
    private final JComboBox<String> nationBox;
    private final JComboBox<String> competitionBox;
    private final JComboBox<String> continentalBox;
    private final JComboBox<String> roundBox;
    private final JComboBox<String> knockoutBox;
    private final JComboBox<String> groupBox;
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

    private Gladiators() {
        Controller.initialise();

        frame = new JFrame("Gladiators");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        final int width = frame.getWidth();
        final int height = frame.getHeight();
        calculatePositions(width, height);
        frame.getContentPane().setBackground(COLOR);

        JLabel background = new JLabel();

        try {
            BufferedImage ball = ImageIO.read(getClass().getResource("ball.jpg"));
            frame.setIconImage(ball);

            BufferedImage image = ImageIO.read(getClass().getResource("gladiators.jpg"));
            Image scaledInstance = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledInstance);

            background.setIcon(icon);
            background.setSize(width, height);
            background.setLocation(0, 0);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        final String[] leagues = {England.LEAGUE, Spain.LEAGUE, Germany.LEAGUE, Italy.LEAGUE, France.LEAGUE, CHAMPIONS_LEAGUE_NAME};
        nationBox = new JComboBox<>(leagues);
        nationBox.setBounds(9 * width / 25, height / 80, width / 9, height / 16);
        nationBox.setFont(new Font(FONT_NAME, Font.PLAIN, height / 50));

        final String[] competitions = {"League", "League Cup", "National Cup"};
        competitionBox = new JComboBox<>(competitions);
        competitionBox.setBounds(19 * width / 40, height / 80, width / 11, height / 16);
        competitionBox.setFont(new Font(FONT_NAME, Font.PLAIN, height / 50));

        final String[] phases = {"Group Stage", "Knockout"};
        continentalBox = new JComboBox<>(phases);
        continentalBox.setBounds(19 * width / 40, height / 80, width / 11, height / 16);
        continentalBox.setFont(new Font(FONT_NAME, Font.PLAIN, height / 50));

        final String[] rounds = IntStream.range(1, 39).mapToObj(String::valueOf).toArray(String[]::new);
        roundBox = new JComboBox<>(rounds);
        roundBox.setBounds(23 * width / 40, height / 80, width / 11, height / 16);
        roundBox.setFont(new Font(FONT_NAME, Font.PLAIN, height / 50));

        final String[] stages = {"Round of 16", "Quarter-finals", "Semi-finals", "Final"};
        knockoutBox = new JComboBox<>(stages);
        knockoutBox.setBounds(23 * width / 40, height / 80, width / 11, height / 16);
        knockoutBox.setFont(new Font(FONT_NAME, Font.PLAIN, height / 50));

        final String[] groups = {"A", "B", "C", "D", "E", "F", "G", "H"};
        groupBox = new JComboBox<>(groups);
        groupBox.setBounds(23 * width / 40, height / 80, width / 11, height / 16);
        groupBox.setFont(new Font(FONT_NAME, Font.PLAIN, height / 50));

        resultsLabel = new JLabel();
        resultsLabel.setBounds(resultsX, resultsY, resultsWidth, resultsHeight);
        TitledBorder borderResults = BorderFactory.createTitledBorder("RESULTS");
        borderResults.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        resultsLabel.setBorder(borderResults);
        resultsLabel.setFont(new Font(FONT_NAME, Font.PLAIN, resultsHeight / 14));

        setStatTables(goalsTable, resultsX, goalsY, "GOALS");
        setStatTables(assistsTable, resultsX, assistsY, "ASSISTS");
        setStatTables(ratingsTable, cleanSheetsX, goalsY, "RATINGS");
        setStatTables(cleanSheetsTable, cleanSheetsX, assistsY, "CLEAN SHEETS");

        final String[] standings = {"N", "TEAM", "G", "W", "D", "L", "GS", "GA", "GD", "P"};
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

        setTableValues(gamesTable, "Games", "Home Wins", "Draws", "Away Wins", "Home Goals", "Away Goals",
                "Assists", "Yellow Cards", "Red Cards", "Average Ratings", "Clean Sheets");

        setJTableColumnsWidth(gamesTable, gamesWidth, 70, 30);
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
        frame.add(resultsLabel);
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

    private void setTableValues(final JTable table, final String... values) {
        for (int row = 0; row < values.length; row++) table.setValueAt(values[row], row, 0);
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

        final String leagueName = league[0].getLeague();
        final Map<Club, Integer> sorted = sortLeague(league, 0);
        int row = 0;
        for (final Club team : sorted.keySet()) {
            insertStandingsEntry(team.getName(), team.getSeason().getLeague(), row++);
        }

        for (int i = row; i < 20; i++) {
            for (int j = 0; j < standingsTable.getColumnCount(); j++) {
                standingsTable.setValueAt("", i, j);
            }
        }

        final int games = homeWins.getOrDefault(leagueName, 0)
                + draws.getOrDefault(leagueName, 0)
                + awayWins.getOrDefault(leagueName, 0);

        gamesTable.setValueAt(String.valueOf(games), 0, 1);
        gamesTable.setValueAt(String.valueOf(homeWins.getOrDefault(leagueName, 0)), 1, 1);
        gamesTable.setValueAt(String.valueOf(draws.getOrDefault(leagueName, 0)), 2, 1);
        gamesTable.setValueAt(String.valueOf(awayWins.getOrDefault(leagueName, 0)), 3, 1);
        gamesTable.setValueAt(String.valueOf(scoredHome.getOrDefault(leagueName, 0)), 4, 1);
        gamesTable.setValueAt(String.valueOf(scoredAway.getOrDefault(leagueName, 0)), 5, 1);
        gamesTable.setValueAt(String.valueOf(Data.assists.getOrDefault(leagueName, 0)), 6, 1);
        gamesTable.setValueAt(String.valueOf(yellowCards.getOrDefault(leagueName, 0)), 7, 1);
        gamesTable.setValueAt(String.valueOf(redCards.getOrDefault(leagueName, 0)), 8, 1);
        gamesTable.setValueAt(df.format(averageRatings.getOrDefault(leagueName, 0.0f) / (games * 22)), 9, 1);
        gamesTable.setValueAt(String.valueOf(Data.cleanSheets.getOrDefault(leagueName, 0)), 10, 1);

        final int round = getInteger(String.valueOf(roundBox.getSelectedItem()));
        resultsLabel.setText("<html>" + Controller.leagueResults.getOrDefault(
                leagueName + round, "") + "</html>");
        System.out.println(Controller.leagueResults.get(leagueName));
    }

    private int getInteger(final String stringValue) {
        int intValue = 0;
        for (char c : stringValue.toCharArray()) {
            intValue *= 10;
            intValue += (c - '0');
        }

        return intValue;
    }

    private void cupView(final Club[] league, final boolean leagueCup, final int teams) {
        competitionBox.setVisible(true);
        continentalBox.setVisible(false);
        roundBox.setVisible(false);
        knockoutBox.setVisible(true);
        groupBox.setVisible(false);
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

        final boolean knockout = String.valueOf(continentalBox.getSelectedItem()).equals("Knockout");
        roundBox.setVisible(false);
        knockoutBox.setVisible(knockout);
        groupBox.setVisible(!knockout);

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
            for (int team = 0; team < 4; team++) league[team] = CHAMPIONS_LEAGUE[8 * team +
                    ((int) String.valueOf(groupBox.getSelectedItem()) .charAt(0) - 'A')];

            final Map<Club, Integer> sorted = sortLeague(league, 3);
            int row = 0;
            for (final Club team : sorted.keySet()) {
                insertStandingsEntry(team.getName(), team.getSeason().getChampionsLeague().getGroup(), row++);
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

    private void insertStandingsEntry(final String name, final League league, final int row) {
        standingsTable.setValueAt(String.valueOf(row + 1), row, 0);
        standingsTable.setValueAt(name, row, 1);
        standingsTable.setValueAt(String.valueOf(league.getMatches()), row, 2);
        standingsTable.setValueAt(String.valueOf(league.getWins()), row, 3);
        standingsTable.setValueAt(String.valueOf(league.getDraws()), row, 4);
        standingsTable.setValueAt(String.valueOf(league.getLosses()), row, 5);
        standingsTable.setValueAt(String.valueOf(league.getScored()), row, 6);
        standingsTable.setValueAt(String.valueOf(league.getConceded()), row, 7);
        standingsTable.setValueAt(String.valueOf((league.getScored() - league.getConceded())), row, 8);
        standingsTable.setValueAt(String.valueOf(league.getPoints()), row, 9);
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
        for (int i = 0; i < 38; i++) Controller.proceed();
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

        SwingUtilities.invokeLater(Gladiators::new);
    }
}
