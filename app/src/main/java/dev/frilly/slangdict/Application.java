package dev.frilly.slangdict;

import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
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
            FlatMacDarkLaf.setup();

            final var frame = MainFrame.getInstance();
            frame.start();
            MainFrame.getInstance().override(WelcomeFrame.getInstance());
        });
    }

}
