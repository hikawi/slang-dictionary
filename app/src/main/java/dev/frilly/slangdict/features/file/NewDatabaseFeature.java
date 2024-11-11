package dev.frilly.slangdict.features.file;

import java.io.File;
import java.util.UUID;

import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.gui.MainFrame;
import dev.frilly.slangdict.gui.ProgressFrame;
import dev.frilly.slangdict.gui.ViewFrame;

/**
 * Implementation for the "New Database" feature.
 */
public final class NewDatabaseFeature implements Runnable {

    private final String name;
    private final boolean bootstrap;

    /**
     * Creates an instance of the feature with the specified params.
     * 
     * @param name      The name of the new database.
     * @param bootstrap Whether to bootstrap with defaults.
     */
    public NewDatabaseFeature(final String name, final boolean bootstrap) {
        this.name = name;
        this.bootstrap = bootstrap;
    }

    @Override
    public void run() {
        final File dataFolder = new File("./.data/dbs");
        if (!dataFolder.exists())
            dataFolder.mkdirs();

        ProgressFrame.getInstance().setValues(I18n.tl("file.new.progress", name), I18n.tl("file.new.progress.init"));
        ProgressFrame.getInstance().startTask(() -> {
            try {
                ProgressFrame.getInstance().setIndeterminate(true);

                // Save before opening.
                ProgressFrame.getInstance().setMessage(I18n.tl("file.new.progress.save"));
                Dictionary.getInstance().save();

                // Create new files.
                ProgressFrame.getInstance().setMessage(I18n.tl("file.new.progress.createFiles"));
                final var uuid = UUID.randomUUID();
                final var file = new File(dataFolder, uuid + ".dict");
                file.createNewFile();

                // Binding files.
                ProgressFrame.getInstance().setMessage(I18n.tl("file.new.progress.bind"));
                Dictionary.getInstance().setFile(file);
                Dictionary.getInstance().rename(name);

                // Loading defaults to the dictionary.
                if (bootstrap) {
                    ProgressFrame.getInstance().setMessage(I18n.tl("file.new.progress.bootstrap"));
                    Dictionary.getInstance().loadDefaults();
                }

                // Trigger saving the dictionary.
                ProgressFrame.getInstance().setMessage(I18n.tl("file.new.progress.final"));
                Dictionary.getInstance().save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, () -> {
            MainFrame.getInstance().back();
            MainFrame.getInstance().replace(ViewFrame.getInstance());
        }, null);
    }

}
