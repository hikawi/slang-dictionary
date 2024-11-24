package dev.frilly.slangdict;

import dev.frilly.slangdict.gui.MainFrame;

import javax.swing.*;

/**
 * A quick and easy way to instantly show simple dialogs.
 */
public final class Dialogs {

    private Dialogs() {
    }

    /**
     * Shows an information message.
     *
     * @param key  The key to translate.
     * @param args The args to pass.
     */
    public static void info(final String key, final Object... args) {
        JOptionPane.showMessageDialog(MainFrame.getInstance(),
            key.formatted(args), "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a confirmation message.
     *
     * @param key  The key to translate.
     * @param args The args to pass.
     *
     * @return The value chosen.
     */
    public static int confirm(final String key, final Object... args) {
        return JOptionPane.showConfirmDialog(MainFrame.getInstance(),
            key.formatted(args), "Confirm", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Shows an error message.
     *
     * @param key  The key to translate.
     * @param args The args to pass.
     */
    public static void error(final String key, final Object... args) {
        JOptionPane.showMessageDialog(MainFrame.getInstance(),
            key.formatted(args), "Error", JOptionPane.ERROR_MESSAGE);
    }

}
