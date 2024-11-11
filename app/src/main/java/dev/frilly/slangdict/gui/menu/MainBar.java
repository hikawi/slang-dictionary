package dev.frilly.slangdict.gui.menu;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

/**
 * The menu bar that sits on the main frame.
 */
public final class MainBar {

    private static MainBar instance;

    private MainBar() {
    }

    /**
     * Adds shared components to the frame.
     * 
     * @param frame The frame to add to.
     */
    public void init(final JFrame frame) {
        final var menuBar = new JMenuBar();

        FileMenu.getInstance().init(menuBar);
        EditMenu.getInstance().init(menuBar);
        ViewMenu.getInstance().init(menuBar);
        SearchMenu.getInstance().init(menuBar);

        frame.setJMenuBar(menuBar);
    }

    /**
     * Retrieves the instance of the main menu bar of the application.
     * 
     * @return The main menu bar.
     */
    public static MainBar getInstance() {
        return switch (instance) {
            case null -> instance = new MainBar();
            default -> instance;
        };
    }

}
