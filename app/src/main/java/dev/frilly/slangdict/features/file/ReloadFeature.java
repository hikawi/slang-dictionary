package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;

import javax.swing.*;

/**
 * Implementation for the Reload Feature.
 * <p>
 * Just reloads the database.
 */
public final class ReloadFeature implements Runnable {

    @Override
    public void run() {
        final var opt = Dialogs.confirm(
            "This will drop all changes and reload the data from disk for " +
            "this database. Are you sure");

        if (opt == JOptionPane.YES_OPTION) {
            Dictionary.getInstance().load();
            Dialogs.info("Reloaded data from disk.");
        }
    }

}
