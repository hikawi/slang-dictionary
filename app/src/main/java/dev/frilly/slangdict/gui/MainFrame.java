package dev.frilly.slangdict.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dev.frilly.slangdict.gui.menu.MainBar;

/**
 * The application's frame. This is the one and only frame, no more frames
 * shall be created.
 */
public final class MainFrame extends JFrame {

    private static MainFrame instance;

    private MainFrame() {
        super("Slang Dictionary");
        this.getRootPane().putClientProperty("apple.awt.windowTitleVisible", false);
        this.setup();
    }

    private void setup() {
        MainBar.getInstance().init(this);
        this.add(new JLabel("Test Component"));
    }

    /**
     * Create the frame.
     */
    public final void start() {
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Revalidates and repaints the frame.
     */
    public final void override(final JPanel contentPane) {
        this.setContentPane(contentPane);
        this.pack();
        this.setLocationRelativeTo(null);
        this.revalidate();
        this.repaint();
    }

    /**
     * Retrieves the instance of the main frame.
     * 
     * @return The frame.
     */
    public static MainFrame getInstance() {
        return switch (instance) {
            case null -> instance = new MainFrame();
            default -> instance;
        };
    }

}
