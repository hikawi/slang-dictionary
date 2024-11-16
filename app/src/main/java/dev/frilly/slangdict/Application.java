package dev.frilly.slangdict;

import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatXcodeDarkIJTheme;
import com.formdev.flatlaf.util.SystemInfo;
import dev.frilly.slangdict.gui.MainFrame;
import dev.frilly.slangdict.gui.WelcomeFrame;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * The main entrypoint of the application.
 */
public final class Application {

    private static Application instance;

    private Application() {
    }

    public static Application getInstance() {
        return instance == null ? instance = new Application() : instance;
    }

    /**
     * Retrieves the version string of this app.
     *
     * @return The version.
     */
    public static String getVersion() {
        return "2.0";
    }

    /**
     * Shortcut to call the getMenuShortcutKeyMaskEx() from default toolkit.
     *
     * @return The int code.
     */
    public static int getMaskedMetaKey() {
        return Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
    }

    /**
     * Retrieves a scaled version of the icon on the classpath.
     *
     * @param url    The load url.
     * @param width  The width to scale to.
     * @param height The height to scale to.
     * @return The scaled image.
     */
    public static ImageIcon getIcon(final URL url, final int width, final int height) {
        final var raw = new ImageIcon(url);
        return new ImageIcon(raw.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    /**
     * Retrieves a scaled icon on the classpath.
     *
     * @param name   The URL as a string.
     * @param width  The width.
     * @param height The height.
     * @return The scaled image.
     */
    public static ImageIcon getIcon(final String name, final int width, final int height) {
        return getIcon(getInstance().getClass().getResource(name), width, height);
    }

    public static void main(String[] args) {
        // Enable Linux default LaF.
        if (SystemInfo.isLinux) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }

        // Setup embedded bar.
        System.setProperty("flatlaf.menuBarEmbedded", "true");
        System.setProperty("flatlaf.useWindowDecorations", "true");

        // Setup MacOS screen menu.
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.name", "Slang Dictionary");
        System.setProperty("apple.awt.application.appearance", "system");

        // Start the program.
        SwingUtilities.invokeLater(() -> {
            FlatXcodeDarkIJTheme.setup();

            final var frame = MainFrame.getInstance();
            frame.start();
            frame.override(WelcomeFrame.getInstance());
        });
    }

}
