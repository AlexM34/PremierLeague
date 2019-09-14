package gui;

import competitions.*;
import players.Footballer;
import simulation.PremierLeague;
import simulation.Printer;
import teams.Club;
import teams.League;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Map;

public class App {
    // TODO: Cup and CL
    private JLabel resultsLabel;
    private JComboBox cb;
    private JTable jt;
    private JTable jtg;
    private JTable jta;
    private JTable jtr;
    private JTable jtc;

    private static final String FONT_NAME = "Times New Roman";
    private static final int FONT_SIZE = 18;
    private static int rX;
    private static int rY;
    private static int rW;
    private static int rH;
    private static int gY;
    private static int gW;
    private static int gH;
    private static int cX;
    private static int aY;
    private static int sX;
    private static int sY;
    private static int sW;
    private static int sH;

    private App() {
        PremierLeague.initialise();

        JFrame frame = new JFrame("Football Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setSize(new Dimension(1000, 700));
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        calculatePositions(frame.getWidth(), frame.getHeight());
//        frame.setResizable(true);
//        frame.setUndecorated(true);
        frame.getContentPane().setBackground(Color.getHSBColor(0.6f, 0.9f, 0.95f));

        final int width = frame.getWidth();
        final int height = frame.getHeight();

        String[] leagues = {"Premier League", "La Liga", "Bundesliga", "Serie A", "Ligue 1"};
        cb = new JComboBox(leagues);
        cb.setBounds(9 * width / 20, height / 50, width / 8, height / 20);

        resultsLabel = new JLabel();
        resultsLabel.setBounds(rX, rY, rW, rH);
        TitledBorder borderResults = BorderFactory.createTitledBorder("RESULTS");
        borderResults.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        resultsLabel.setBorder(borderResults);
        resultsLabel.setFont(new Font(FONT_NAME, Font.PLAIN, rH / 14));
        resultsLabel.setForeground(Color.WHITE);

        String[] columnP = {"N", "PLAYER", "COUNT"};

        jtg = new JTable(new String[10][3], columnP);
        jtg.setBounds(rX, gY, gW, gH);
        jtg.setRowHeight(gH / 14);
        jtg.setEnabled(false);
//        jtg.setFont(new Font(FONT_NAME, Font.PLAIN, jtg.getHeight() / 12));
        jtg.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setJTableColumnsWidth(jtg, gW, 15, 60, 25);
        JScrollPane tableGoalsScrollPane = new JScrollPane(jtg);
        TitledBorder borderGoals = BorderFactory.createTitledBorder("GOALS");
        borderGoals.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        tableGoalsScrollPane.setBorder(borderGoals);
        tableGoalsScrollPane.setBounds(rX, gY, gW, gH);
        tableGoalsScrollPane.setFont(new Font(FONT_NAME, Font.PLAIN, gH / 12));

        jta = new JTable(new String[10][3], columnP);
        jta.setBounds(rX, aY, gW, gH);
        jta.setRowHeight(gH / 14);
        jta.setEnabled(false);
        setJTableColumnsWidth(jta, gW, 15, 60, 25);
        JScrollPane tableAssistsScrollPane = new JScrollPane(jta);
        TitledBorder borderAssists = BorderFactory.createTitledBorder("ASSISTS");
        borderAssists.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        tableAssistsScrollPane.setBorder(borderAssists);
        tableAssistsScrollPane.setBounds(rX, aY, gW, gH);
//        tableAssistsScrollPane.setForeground(Color.getHSBColor(0.6f, 0.9f, 0.95f));

        jtr = new JTable(new String[10][3], columnP);
        jtr.setBounds(cX, gY, gW, gH);
        jtr.setRowHeight(gH / 14);
        jtr.setEnabled(false);
        setJTableColumnsWidth(jtr, gW, 15, 60, 25);
        JScrollPane tableRatingsScrollPane = new JScrollPane(jtr);
        TitledBorder borderRatings = BorderFactory.createTitledBorder("RATINGS");
        borderRatings.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        tableRatingsScrollPane.setBorder(borderRatings);
        tableRatingsScrollPane.setBounds(cX, gY, gW, gH);

        jtc = new JTable(new String[10][3], columnP);
        jtc.setBounds(cX, aY, gW, gH);
        jtc.setRowHeight(gH / 14);
        jtc.setEnabled(false);
        setJTableColumnsWidth(jtc, width / 4, 15, 60, 25);
        JScrollPane tableCleanSheetsScrollPane = new JScrollPane(jtc);
        TitledBorder borderCleanSheets = BorderFactory.createTitledBorder("CLEAN SHEETS");
        borderCleanSheets.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        tableCleanSheetsScrollPane.setBorder(borderCleanSheets);
        tableCleanSheetsScrollPane.setBounds(cX, aY, gW, gH);

        String[][] standings = new String[20][10];
        String[] column = {"N", "TEAM", "G", "W", "D", "L", "GS", "GA", "GD", "P"};

        jt = new JTable(standings, column);
        jt.setBounds(sX, sY, sW, sH);
        jt.setRowHeight(sH / 22);
        jt.setEnabled(false);
        setJTableColumnsWidth(jt, sW, 8, 28, 8, 8, 8, 8, 8, 8, 8, 8);
        JScrollPane tableScrollPane = new JScrollPane(jt);
        TitledBorder borderStandings = BorderFactory.createTitledBorder("STANDINGS");
        borderStandings.setTitleFont(new Font(FONT_NAME, Font.ITALIC, FONT_SIZE));
        tableScrollPane.setBorder(borderStandings);
        tableScrollPane.setBounds(sX, sY, sW, sH);

        JButton nextB = new JButton("Next");
        nextB.setBounds(width - 200, height - 100, 150, 80);

        frame.add(cb);
        frame.add(nextB);
        frame.add(resultsLabel);
        frame.add(tableGoalsScrollPane);
        frame.add(tableAssistsScrollPane);
        frame.add(tableRatingsScrollPane);
        frame.add(tableCleanSheetsScrollPane);
        frame.add(tableScrollPane);

        nextB.addActionListener(e -> nextRound());
        cb.addActionListener(e -> updateStats());

        updateStats();
    }

    private void calculatePositions(final int width, final int height) {
        rX = width / 80;
        rY = height / 15;
        rW = width / 2;
        rH = 7 * height / 20;
        gY = 9 * height / 20;
        gW = 2 * width / 9;
        gH = height / 4;
        aY = 7 * height / 10;
        cX = 7 * width / 25;
        sX = 21 * width / 40;
        sY = height / 10;
        sW = 17 * width / 40;
        sH = 3 * height / 5;
    }

    private void updateStats() {
        Club[] league;
        switch (String.valueOf(cb.getSelectedItem())) {
            case England.LEAGUE: league = England.CLUBS; break;
            case Spain.LEAGUE: league = Spain.CLUBS; break;
            case Italy.LEAGUE: league = Italy.CLUBS; break;
            case France.LEAGUE: league = France.CLUBS; break;
            case Germany.LEAGUE: league = Germany.CLUBS; break;
            default: return;
        }

        final Map<Club, Integer> sorted = Printer.sortLeague(league);
        int row = 0;
        for (final Club team : sorted.keySet()) {
            final League stats = team.getSeason().getLeague();

            jt.setValueAt("" + (row + 1), row, 0);
            jt.setValueAt(team.getName(), row, 1);
            jt.setValueAt("" + stats.getMatches(), row, 2);
            jt.setValueAt("" + stats.getWins(), row, 3);
            jt.setValueAt("" + stats.getDraws(), row, 4);
            jt.setValueAt("" + stats.getLosses(), row, 5);
            jt.setValueAt("" + stats.getScored(), row, 6);
            jt.setValueAt("" + stats.getConceded(), row, 7);
            jt.setValueAt("" + (stats.getScored() - stats.getConceded()), row, 8);
            jt.setValueAt("" + stats.getPoints(), row, 9);
            row++;
        }

        for (int i = row; i < 20; i++) {
            for (int j = 0; j < jt.getColumnCount(); j++) {
                jt.setValueAt("", i, j);
            }
        }

        final Map<Footballer, Integer> sortedGoals = Printer.sortPlayersG(league);
        final Map<Footballer, Integer> sortedAssists = Printer.sortPlayersA(league);
        final Map<Footballer, Integer> sortedRatings = Printer.sortPlayersR(league);
        final Map<Footballer, Integer> sortedCleanSheets = Printer.sortPlayersCS(league);
        row = 0;
        for (final Footballer footballer : sortedGoals.keySet()) {
            jtg.setValueAt("" + (row + 1), row, 0);
            jtg.setValueAt(footballer.getName(), row, 1);
            jtg.setValueAt("" + sortedGoals.getOrDefault(footballer, 0), row, 2);

            if (++row > 9) break;
        }
        row = 0;
        for (final Footballer footballer : sortedAssists.keySet()) {
            jta.setValueAt("" + (row + 1), row, 0);
            jta.setValueAt(footballer.getName(), row, 1);
            jta.setValueAt("" + sortedAssists.getOrDefault(footballer, 0), row, 2);

            if (++row > 9) break;
        }
        row = 0;
        for (final Footballer footballer : sortedRatings.keySet()) {
            jtr.setValueAt("" + (row + 1), row, 0);
            jtr.setValueAt(footballer.getName(), row, 1);
            jtr.setValueAt("" + sortedRatings.getOrDefault(footballer, 0), row, 2);

            if (++row > 9) break;
        }
        row = 0;
        for (final Footballer footballer : sortedCleanSheets.keySet()) {
            jtc.setValueAt("" + (row + 1), row, 0);
            jtc.setValueAt(footballer.getName(), row, 1);
            jtc.setValueAt("" + sortedCleanSheets.getOrDefault(footballer, 0), row, 2);

            if (++row > 9) break;
        }

        resultsLabel.setText("<html>" + PremierLeague.results.getOrDefault(cb.getSelectedItem(), "") + "</html>");
        System.out.println(PremierLeague.results.get(cb.getSelectedItem()));
    }

    private void nextRound() {
        PremierLeague.proceed();
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

        SwingUtilities.invokeLater(App::new);
    }
}
