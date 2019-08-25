package gui;

import competitions.England;
import simulation.PremierLeague;
import simulation.Printer;
import teams.Club;
import teams.League;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Map;

public class App {
    JLabel resultsLabel;
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
        frame.setResizable(true);
        frame.setUndecorated(true);
        frame.setVisible(true);

        final int width = frame.getWidth();
        final int height = frame.getHeight();

        String[] leagues = {"England", "Spain", "Italy", "France", "Germany"};
        JComboBox cb = new JComboBox(leagues);
        cb.setBounds(width / 2 - 50, 10, 100, 50);

        resultsLabel = new JLabel("<html>Results<br/>dsafdsafdas</html>");
        resultsLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        /*Font labelFont = label.getFont();
        String labelText = label.getText();

        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = label.getWidth();

// Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;

        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        int componentHeight = label.getHeight();

// Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

// Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));*/

        resultsLabel.setBounds(10, 10, width / 2, height - 100);

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
        jt.setBounds(width / 2 + 20, 100, width / 2 - 50, height / 2);
        setJTableColumnsWidth(jt, width / 2 - 50, 8, 28, 8, 8, 8, 8, 8, 8, 8, 8);
        JScrollPane tableScrollPane = new JScrollPane(jt);
        tableScrollPane.setBounds(width / 2 + 20, 100, width / 2 - 50, height / 2);
        JButton nextB = new JButton("Next");
        nextB.setBounds(width - 200, height - 100, 150, 80);

        frame.add(cb);
        frame.add(nextB);
        frame.add(resultsLabel);
        frame.add(tableScrollPane);
        String[][] standings2 = { {"1","Arsenal","90","38","26","12","0","89","19","70"}};
//        jt = new JTable(standings2, column);


        nextB.addActionListener(e -> next());
    }

    private void next() {
        PremierLeague.connection("Premier League");
        Printer.standings(England.CLUBS);
        final Map<Club, Integer> sorted = Printer.sortLeague(England.CLUBS);
        int row = 0;
        for (final Club team : sorted.keySet()) {
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

        resultsLabel.setText("<html>" + PremierLeague.results + "</html>");
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
