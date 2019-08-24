package gui;

import simulation.PremierLeague;

import javax.swing.*;
import java.awt.*;

public class App {
    private JPanel mainPanel;
    private JButton next;

    private App() {
        next.addActionListener(e -> JOptionPane.showMessageDialog(null,
                PremierLeague.connection()));
    }

    public static void main(String[] args) {
        JPanel panel = new JPanel(new BorderLayout());

        JFrame frame = new JFrame("App");
        frame.setTitle("Football Manager");
        frame.setLayout(new BorderLayout());
        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(panel, BorderLayout.CENTER);

        frame.setSize(new Dimension(1200, 1000));
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
