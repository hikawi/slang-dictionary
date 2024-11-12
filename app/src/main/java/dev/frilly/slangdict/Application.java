package dev.frilly.slangdict;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import com.formdev.flatlaf.util.SystemInfo;

import dev.frilly.slangdict.gui.MainFrame;
import dev.frilly.slangdict.gui.WelcomeFrame;

/**
 * The main entrypoint of the application.
 */
public final class Application {

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
            FlatAtomOneDarkIJTheme.setup();

            final var frame = MainFrame.getInstance();
            frame.start();
            MainFrame.getInstance().override(WelcomeFrame.getInstance());
        });
    }

}
