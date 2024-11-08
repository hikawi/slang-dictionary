package dev.frilly.slangdict;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import dev.frilly.slangdict.gui.ApplicationFrame;

/**
 * The singleton class that holds the application together.
 */
public class Application {

    private static Application INSTANCE;

    private final ApplicationFrame frame;

    private Application() {
        this.frame = new ApplicationFrame();
    }

    public static Application getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Application();
        return INSTANCE;
    }

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();

        final var app = Application.getInstance();
        app.start();
    }

    /**
     * Start the application.
     */
    public void start() {
        this.frame.setup();
        this.frame.pack();
        this.frame.setVisible(true);
    }

}
