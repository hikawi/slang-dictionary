package dev.frilly.slangdict;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import dev.frilly.slangdict.gui.ApplicationFrame;

/**
 * The singleton class that holds the application together.
 */
public class Application {

    private static Application INSTANCE;

    private final I18n i18n;
    private final ApplicationFrame frame;

    private Application() {
        this.i18n = new I18n();
        this.frame = new ApplicationFrame();
    }

    public static Application getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Application();
        return INSTANCE;
    }

    public static void main(String[] args) {
        System.out.println("Main");
        FlatMacDarkLaf.setup();
        System.out.println("FlatDarkLaf setup");

        final var app = Application.getInstance();
        System.out.println("App instance");
        app.start();
        System.out.println("App start");
    }

    /**
     * Retrieves the I18n instance of the application.
     * 
     * @return The i18n instance.
     */
    public I18n getI18n() {
        return i18n;
    }

    public void start() {
        this.frame.setup();
        this.frame.pack();
        this.frame.setVisible(true);
    }

}
