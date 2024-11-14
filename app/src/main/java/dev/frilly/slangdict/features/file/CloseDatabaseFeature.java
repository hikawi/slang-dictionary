package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.gui.MainFrame;
import javax.swing.JOptionPane;

/**
 * Implementation for closing the database.
 */
public final class CloseDatabaseFeature implements Runnable {

    @Override
    public void run() {
        if (Dictionary.getInstance().getFile() == null) return;

        final var res = Dialogs.confirm("file.close.confirm");
        if (res == JOptionPane.YES_OPTION) Dictionary.getInstance().save();
        MainFrame.getInstance().back();
        Dictionary.getInstance().setFile(null);
    }
}
