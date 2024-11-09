package dev.frilly.slangdict;

import javax.swing.JOptionPane;

/**
 * A simple static class for opening dialog boxes.
 */
public final class Dialogs {

    private Dialogs() {
    }

    public static void info(final String string) {
    }

    public static void success(final String message) {
        JOptionPane.showMessageDialog(Application.getInstance().getFrame(), message, I18n.tl("success"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(final String message) {
        JOptionPane.showMessageDialog(Application.getInstance().getFrame(), message, I18n.tl("error"),
                JOptionPane.ERROR_MESSAGE);
    }

}
