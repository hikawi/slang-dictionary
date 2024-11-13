package dev.frilly.slangdict;

import javax.swing.JOptionPane;
import dev.frilly.slangdict.gui.MainFrame;

/**
 * A quick and easy way to instantly show simple dialogs.
 */
public final class Dialogs {

    private Dialogs() {}

    /**
     * Shows an information message.
     * 
     * @param key The key to translate.
     * @param args The args to pass.
     */
    public static void info(final String key, final Object... args) {
        JOptionPane.showMessageDialog(MainFrame.getInstance(), I18n.tl(key, args),
                I18n.tl("dialog.info"), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a confirmation message.
     * 
     * @param key The key to translate.
     * @param args The args to pass.
     * @return The value chosen.
     */
    public static int confirm(final String key, final Object... args) {
        return JOptionPane.showConfirmDialog(MainFrame.getInstance(), I18n.tl(key, args),
                I18n.tl("dialog.confirm"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Shows an error message.
     * 
     * @param key The key to translate.
     * @param args The args to pass.
     */
    public static void error(final String key, final Object... args) {
        JOptionPane.showMessageDialog(MainFrame.getInstance(), I18n.tl(key, args),
                I18n.tl("dialog.error"), JOptionPane.ERROR_MESSAGE);
    }

}
