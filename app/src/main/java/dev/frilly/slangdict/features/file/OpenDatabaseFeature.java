package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.gui.MainFrame;
import dev.frilly.slangdict.gui.ProgressFrame;
import dev.frilly.slangdict.gui.ViewFrame;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
        file.setFileFilter(
            new FileNameExtensionFilter("Only .dict files", "dict")
        );

        switch (file.showOpenDialog(MainFrame.getInstance())) {
            case JFileChooser.CANCEL_OPTION:
                break;
            case JFileChooser.APPROVE_OPTION:
                ProgressFrame.getInstance().setHeading("Opening dictionary");
                ProgressFrame.getInstance()
                    .startTask(
                        () -> {
                            ProgressFrame.getInstance()
                                .setMessage("Initializing...");
                            ProgressFrame.getInstance().setProgress(1);
                            final var f = file.getSelectedFile();

                            ProgressFrame.getInstance()
                                .setMessage("Loading the dictionary file");
                            ProgressFrame.getInstance().setProgress(20);
                            Dictionary.getInstance().setFile(f);
                            Dictionary.getInstance().load();

                            ProgressFrame.getInstance()
                                .setMessage("Drawing the dictionary on panels");
                            ProgressFrame.getInstance().setProgress(50);
                            ViewFrame.getInstance(); // Make it load first.
                            MainFrame.getInstance()
                                .replace(ViewFrame.getInstance());
                        },
                        null,
                        null
                    );
                break;
        }
    }
}
