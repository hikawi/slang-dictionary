package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.gui.MainFrame;

import javax.swing.*;

/**
 * Implementation for closing the database.
 */
public final class CloseDatabaseFeature implements Runnable {

    @Override
    public void run() {
        if (Dictionary.getInstance().getFile() == null) {
            return;
        }

        final var res = Dialogs.confirm("Would you like to save the database?");
        if (res == JOptionPane.YES_OPTION) {
            Dictionary.getInstance().save();
        }
        MainFrame.getInstance().back();
        Dictionary.getInstance().setFile(null);
    }

}
