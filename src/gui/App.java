package gui;

import simulation.PremierLeague;

import javax.swing.*;

public class App {
    private App() {

        JFrame frame = new JFrame("Football Manager");
//        frame.setLayout(new BorderLayout());
//        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(mainPanel, BorderLayout.CENTER);
//        frame.pack();
//        frame.setSize(new Dimension(1000, 700));
//        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setVisible(true);

        final int width = frame.getWidth();
        final int height = frame.getHeight();

        String[] leagues = {"England", "Spain", "Italy", "France", "Germany"};
        JComboBox cb = new JComboBox(leagues);
        cb.setBounds(width / 2 - 50, 10, 100, 50);

        JLabel resultsLabel = new JLabel("Results");
        resultsLabel.setBounds(10, 10, width / 2, 200);

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
        String[] column = {"N", "TEAM", "POINTS", "G", "W", "D", "L", "GS", "GA", "GD"};

        JTable jt = new JTable(standings, column);
        jt.setBounds(width / 2 + 20, 100, width / 2 - 50, height / 2);
        JScrollPane tableScrollPane = new JScrollPane(jt);
        tableScrollPane.setBounds(width / 2 + 20, 100, width / 2 - 50, height / 2);

        JButton nextB = new JButton("Next");
        nextB.setBounds(width - 150, height - 100, 150, 80);

        frame.add(cb);
        frame.add(nextB);
        frame.add(resultsLabel);
        frame.add(tableScrollPane);

        nextB.addActionListener(e -> JOptionPane.showMessageDialog(null,
                PremierLeague.connection()));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new App();
        });
    }
}
