package gui;

import competitions.*;
import simulation.PremierLeague;
import simulation.Printer;
import teams.Club;
import teams.League;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Map;

public class App {
    // TODO: Name of the app
    // TODO: Labels for results
    // TODO: Stats
    // TODO: Cup and CL
    private JLabel resultsLabel;
    private JComboBox cb;
    private JTable jt;

    private App() {
        PremierLeague.initialise();

        JFrame frame = new JFrame("Football Manager");
//        frame.setLayout(new BorderLayout());
//        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(mainPanel, BorderLayout.CENTER);
//        frame.pack();
        frame.setSize(new Dimension(1000, 700));
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setResizable(true);
        frame.setUndecorated(true);
        frame.setVisible(true);
//        frame.getContentPane().setBackground(Color.CYAN);

        final int width = frame.getWidth();
        final int height = frame.getHeight();

        String[] leagues = {"Premier League", "La Liga", "Bundesliga", "Serie A", "Ligue 1"};
        cb = new JComboBox(leagues);
        cb.setBounds(width / 2 - 50, 10, 130, 60);

        resultsLabel = new JLabel("<html>Results<br/>dsafdsafdas</html>");
        resultsLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        resultsLabel.setBounds(10, 80, width / 2, height / 2 - 100);
//        resultsLabel.setForeground(Color.BLUE);
//        adjustFont(resultsLabel);

        // TODO: Remove
        String[][] standings = { {"1","Arsenal","90","38","26","12","0","89","19","70"},
                {"2","Man City","81","38","26","12","0","89","19","70"},
                {"3","Liverpool","80","38","26","12","0","89","19","70"},
                {"4","Tottenham","70","38","26","12","0","89","19","70"},
                {"5","Man Utd","65","38","26","12","0","89","19","70"},
                {"6","Chelsea","65","38","26","12","0","89","19","70"},
                {"7","Everton","60","38","26","12","0","89","19","70"},
                {"8","Wolves","60","38","26","12","0","89","19","70"},
                {"9","Leicester","60","38","26","12","0","89","19","70"},
                {"10","West Ham","50","38","26","12","0","89","19","70"},
                {"11","Southampton","50","38","26","12","0","89","19","70"},
                {"12","Bournemouth","50","38","26","12","0","89","19","70"},
                {"13","Burnley","50","38","26","12","0","89","19","70"},
                {"14","Crystal Palace","50","38","26","12","0","89","19","70"},
                {"15","Norwich","45","38","26","12","0","89","19","70"},
                {"16","Aston Villa","45","38","26","12","0","89","19","70"},
                {"17","Newcastle","40","38","26","12","0","89","19","70"},
                {"18","Sunderland","30","38","26","12","0","89","19","70"},
                {"19","Derby","30","38","26","12","0","89","19","70"},
                {"20","Oxford","20","38","26","12","0","89","19","70"}};
        String[] column = {"N", "TEAM", "G", "W", "D", "L", "GS", "GA", "GD", "P"};

        jt = new JTable(standings, column);
        jt.setBounds(width / 2 + 20, 80, width / 2 - 50, height / 2 + 50);
        setJTableColumnsWidth(jt, width / 2 - 50, 8, 28, 8, 8, 8, 8, 8, 8, 8, 8);
        JScrollPane tableScrollPane = new JScrollPane(jt);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder ("STANDINGS"));
        tableScrollPane.setBounds(width / 2 + 20, 80, width / 2 - 50, height / 2 + 60);
        JButton nextB = new JButton("Next");
        nextB.setBounds(width - 200, height - 100, 150, 80);
        resultsLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));

        frame.add(cb);
        frame.add(nextB);
        frame.add(resultsLabel);
        frame.add(tableScrollPane);

        nextB.addActionListener(e -> nextRound());
        cb.addActionListener(e -> updateStats());
    }

    private void updateStats() {
        Club[] league = null;
        switch (String.valueOf(cb.getSelectedItem())) {
            case England.LEAGUE: league = England.CLUBS; break;
            case Spain.LEAGUE: league = Spain.CLUBS; break;
            case Italy.LEAGUE: league = Italy.CLUBS; break;
            case France.LEAGUE: league = France.CLUBS; break;
            case Germany.LEAGUE: league = Germany.CLUBS; break;
        }

        Printer.standings(league);
        final Map<Club, Integer> sorted = Printer.sortLeague(league);
        int row = 0;
        for (final Club team : sorted.keySet()) {
            // TODO: Modify when showing Bundesliga
            final League stats = team.getSeason().getLeague();

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

        resultsLabel.setText("<html>RESULTS<br/>" + PremierLeague.results.get(cb.getSelectedItem()) + "</html>");
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
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

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
