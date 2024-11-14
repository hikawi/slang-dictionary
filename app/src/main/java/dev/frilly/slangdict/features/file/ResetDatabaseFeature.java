package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;
import javax.swing.JOptionPane;

/**
 * The implementation for the "reset database to default" feature.
 */
public final class ResetDatabaseFeature implements Runnable {

    private final boolean is100k;

    public ResetDatabaseFeature(final boolean is100k) {
        this.is100k = is100k;
    }

    @Override
    public void run() {
        if (Dictionary.getInstance().getFile() == null) {
            Dialogs.error("There is no dictionary open.");
            return;
        }

        final var res = Dialogs.confirm(
            "This will override all entries within \"%s\". Are you sure?",
            Dictionary.getInstance().getName()
        );
        if (res != JOptionPane.YES_OPTION) return;

        Dictionary.getInstance().loadDefaults(is100k);
    }
}
