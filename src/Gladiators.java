import view.View;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

class Gladiators {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(View::new);
    }
}
