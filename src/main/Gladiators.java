package main;

import main.view.ViewManager;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

class Gladiators {
    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(ViewManager::new);
    }
}
