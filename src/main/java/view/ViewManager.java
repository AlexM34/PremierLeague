package view;

import static java.awt.Frame.MAXIMIZED_BOTH;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static simulation.Controller.initialise;
import static view.Helper.playMusic;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ViewManager {

    private static final PrintStream STREAM = new PrintStream(new FileOutputStream(FileDescriptor.out));
    private static final JFrame frame = new JFrame("Gladiators");
    static final JPanel seasonalView = new JPanel();
    static final JPanel rankedView = new JPanel();
    static final JPanel historicalView = new JPanel();

    public ViewManager() {
        initialise();

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setUndecorated(true);
        frame.setExtendedState(MAXIMIZED_BOTH);
        frame.setVisible(true);
        final int width = frame.getWidth();
        final int height = frame.getHeight();

        seasonalView.setLayout(null);
        seasonalView.setVisible(true);
        seasonalView.setSize(width, height);
        frame.getContentPane().add(seasonalView);

        rankedView.setLayout(null);
        rankedView.setVisible(false);
        rankedView.setSize(width, height);
        frame.getContentPane().add(rankedView);

        historicalView.setLayout(null);
        historicalView.setVisible(false);
        historicalView.setSize(width, height);
        frame.getContentPane().add(historicalView);

        try {
            frame.setIconImage(ImageIO.read(getClass().getResource("/images/ball.jpg")));
        } catch (final IOException e) {
            STREAM.println("Exception thrown while extracting images! " + e);
        }

        playMusic();

        Seasonal.setup(width, height);
        Ranked.setup(width, height);
        Historical.setup(width, height);

        seasonalView();
    }

    static void seasonalView() {
        rankedView.setVisible(false);
        historicalView.setVisible(false);
        seasonalView.setVisible(true);
        Seasonal.update();
    }

    static void rankedView() {
        seasonalView.setVisible(false);
        historicalView.setVisible(false);
        rankedView.setVisible(true);
        Ranked.update();
    }

    static void historicalView() {
        seasonalView.setVisible(false);
        rankedView.setVisible(false);
        historicalView.setVisible(true);
        Historical.update();
    }
}
