package dev.frilly.slangdict.features.file;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.gui.MainFrame;
import dev.frilly.slangdict.gui.ViewFrame;

/**
 * Implementation for the "Open Database" feature.
 * 
 * This opens a file chooser to pick a database.
 */
public final class OpenDatabaseFeature implements Runnable {

    @Override
    public void run() {
        final var file = new JFileChooser("./.data/dbs");
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
        file.setFileFilter(new FileNameExtensionFilter(I18n.tl("file.nameFilter", ".dict"), "dict"));

        switch (file.showOpenDialog(MainFrame.getInstance())) {
            case JFileChooser.CANCEL_OPTION:
                break;
            case JFileChooser.APPROVE_OPTION:
                final var f = file.getSelectedFile();
                Dictionary.getInstance().setFile(f);
                Dictionary.getInstance().load();
                MainFrame.getInstance().override(ViewFrame.getInstance());
                break;
        }
    }

}
