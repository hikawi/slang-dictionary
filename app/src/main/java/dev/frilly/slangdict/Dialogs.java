package dev.frilly.slangdict;

import javax.swing.JOptionPane;

import dev.frilly.slangdict.gui.MainFrame;

/**
 * A quick and easy way to instantly show simple dialogs.
 */
public final class Dialogs {

    private Dialogs() {
    }

    /**
     * Shows an error message.
     * 
     * @param key  The key to translate.
     * @param args The args to pass.
     */
    public static void error(final String key, final Object... args) {
        JOptionPane.showMessageDialog(MainFrame.getInstance(), I18n.tl(key, args), I18n.tl("dialog.error"),
                JOptionPane.ERROR_MESSAGE);
    }

}
