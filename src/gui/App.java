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

    private App() {
        PremierLeague.initialise();

        JFrame frame = new JFrame("Football Manager");
//        frame.setLayout(new BorderLayout());
//        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(mainPanel, BorderLayout.CENTER);
//        frame.pack();
//        frame.setSize(new Dimension(1000, 700));
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setResizable(true);
        frame.setUndecorated(true);
        frame.setVisible(true);
//        frame.getContentPane().setBackground(Color.CYAN);

        final int width = frame.getWidth();
        final int height = frame.getHeight();

        String[] leagues = {"Premier League", "La Liga", "Bundesliga", "Serie A", "Ligue 1"};
        cb = new JComboBox(leagues);
        cb.setBounds(width / 2 - 50, 10, 130, 40);

        TitledBorder borderResults = BorderFactory.createTitledBorder("RESULTS");
        borderResults.setTitleFont(new Font("Times New Roman", Font.PLAIN, 18));
        TitledBorder borderGoalscorers = BorderFactory.createTitledBorder("GOALS");
        borderGoalscorers.setTitleFont(new Font("Times New Roman", Font.PLAIN, 18));
        TitledBorder borderAssisters = BorderFactory.createTitledBorder("ASSISTS");
        borderAssisters.setTitleFont(new Font("Times New Roman", Font.PLAIN, 18));
        TitledBorder borderRatings = BorderFactory.createTitledBorder("RATINGS");
        borderRatings.setTitleFont(new Font("Times New Roman", Font.PLAIN, 18));
        TitledBorder borderCleanSheets = BorderFactory.createTitledBorder("CLEAN SHEETS");
        borderCleanSheets.setTitleFont(new Font("Times New Roman", Font.PLAIN, 18));
        TitledBorder borderStandings = BorderFactory.createTitledBorder("STANDINGS");
        borderStandings.setTitleFont(new Font("Times New Roman", Font.PLAIN, 18));

        resultsLabel = new JLabel();
        resultsLabel.setBorder(borderResults);
//        resultsLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        resultsLabel.setBounds(10, 80, width / 2, height / 2 - 100);
        adjustFont(resultsLabel);

//        goalscorersLabel = new JLabel();
//        goalscorersLabel.setBorder(borderGoalscorers);
//        goalscorersLabel.setBounds(10, height / 2, width / 4, height / 2 - 100);
//        adjustFont(goalscorersLabel);

//        String[][] leaders = new String[10][3];
        String[] columnP = {"N", "PLAYER", "COUNT"};

        jtg = new JTable(new String[10][3], columnP);
        jtg.setBounds(10, height / 2, width / 4, height / 4);
        setJTableColumnsWidth(jtg, width / 4, 15, 60, 25);
        JScrollPane tableGoalscorersScrollPane = new JScrollPane(jtg);
        tableGoalscorersScrollPane.setBorder(borderGoalscorers);
        tableGoalscorersScrollPane.setBounds(10, height / 2, width / 4, height / 4);

        jta = new JTable(new String[10][3], columnP);
        jta.setBounds(10, 3 * height / 4, width / 4, height / 4);
        setJTableColumnsWidth(jta, width / 4, 15, 60, 25);
        JScrollPane tableAssistersScrollPane = new JScrollPane(jta);
        tableAssistersScrollPane.setBorder(borderAssisters);
        tableAssistersScrollPane.setBounds(10, 3 * height / 4, width / 4, height / 4);

        jtr = new JTable(new String[10][3], columnP);
        jtr.setBounds(10 + height / 4, height / 2, width / 4, height / 4);
        setJTableColumnsWidth(jtr, width / 4, 15, 60, 25);
        JScrollPane tableRatingsScrollPane = new JScrollPane(jtr);
        tableRatingsScrollPane.setBorder(borderRatings);
        tableRatingsScrollPane.setBounds(10 + width / 4, height / 2, width / 4, height / 4);

        jtc = new JTable(new String[10][3], columnP);
        jtc.setBounds(10 + height / 4, 3 * height / 4, width / 4, height / 4);
        setJTableColumnsWidth(jta, width / 4, 15, 60, 25);
        JScrollPane tableCleanSheetsScrollPane = new JScrollPane(jtc);
        tableCleanSheetsScrollPane.setBorder(borderCleanSheets);
        tableCleanSheetsScrollPane.setBounds(10 + width / 4, 3 * height / 4, width / 4, height / 4);

        String[][] standings = new String[20][10];
        String[] column = {"N", "TEAM", "G", "W", "D", "L", "GS", "GA", "GD", "P"};

        jt = new JTable(standings, column);
        jt.setBounds(width / 2 + 20, 80, width / 2 - 50, height / 2 + 50);
        setJTableColumnsWidth(jt, width / 2 - 50, 8, 28, 8, 8, 8, 8, 8, 8, 8, 8);
        JScrollPane tableScrollPane = new JScrollPane(jt);
        tableScrollPane.setBorder(borderStandings);
        tableScrollPane.setBounds(width / 2 + 20, 80, width / 2 - 50, height / 2 + 60);

        JButton nextB = new JButton("Next");
        nextB.setBounds(width - 200, height - 100, 150, 80);

        frame.add(cb);
        frame.add(nextB);
        frame.add(resultsLabel);
        frame.add(tableGoalscorersScrollPane);
        frame.add(tableAssistersScrollPane);
        frame.add(tableRatingsScrollPane);
        frame.add(tableCleanSheetsScrollPane);
        frame.add(tableScrollPane);

        nextB.addActionListener(e -> nextRound());
        cb.addActionListener(e -> updateStats());

        updateStats();
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

//        Printer.standings(league);
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
    }

    private void nextRound() {
        PremierLeague.proceed(String.valueOf(cb.getSelectedItem()));
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

    private void adjustFont(final JLabel label) {
        // TODO: Fix method
        Font labelFont = label.getFont();
        String labelText = label.getText();

        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = label.getWidth();
        double widthRatio = (double) componentWidth / (double) stringWidth;

        int newFontSize = (int) (labelFont.getSize() * widthRatio);
        int componentHeight = label.getHeight();
        int fontSizeToUse = Math.min(newFontSize, componentHeight / 15);

        label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
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
