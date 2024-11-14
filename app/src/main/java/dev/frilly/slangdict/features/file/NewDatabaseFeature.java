package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.gui.MainFrame;
import dev.frilly.slangdict.gui.ProgressFrame;
import dev.frilly.slangdict.gui.ViewFrame;
import java.io.File;
import java.util.UUID;

/**
 * Implementation for the "New Database" feature.
 */
public final class NewDatabaseFeature implements Runnable {

    public static int NO_BOOTSTRAP = 0;
    public static int BOOTSTRAP_DEFAULT = 1;
    public static int BOOTSTRAP_100K = 2;

    private final String name;
    private final int bootstrap;

    /**
     * Creates an instance of the feature with the specified params.
     *
     * @param name      The name of the new database.
     * @param bootstrap Whether to bootstrap with defaults. 0 = no bootstrap, 1 =
     *                  default, 2 = 100k random.
     */
    public NewDatabaseFeature(final String name, final int bootstrap) {
        this.name = name;
        this.bootstrap = bootstrap;
    }

    @Override
    public void run() {
        final File dataFolder = new File("./.data/dbs");
        if (!dataFolder.exists()) dataFolder.mkdirs();

        final var prog = ProgressFrame.getInstance();
        prog.setValues("Creating database \"%s\"".formatted(name), "Initializing creation...");

        ProgressFrame.getInstance()
            .startTask(
                () -> {
                    try {
                        // Save before opening.
                        prog.setMessage("Saving the current dictionary...");
                        prog.setProgress(10);
                        Thread.sleep(1000);
                        Dictionary.getInstance().save();

                        // Create new files.
                        prog.setMessage("Creating files...");
                        prog.setProgress(20);
                        Thread.sleep(1000);
                        final var uuid = UUID.randomUUID();
                        final var file = new File(dataFolder, uuid + ".dict");
                        file.createNewFile();

                        // Binding files.
                        prog.setMessage("Binding files...");
                        prog.setProgress(30);
                        Thread.sleep(1000);
                        Dictionary.getInstance().setFile(file);
                        Dictionary.getInstance().rename(name);

                        // Loading defaults to the dictionary.
                        if (bootstrap != NO_BOOTSTRAP) {
                            prog.setMessage("Bootstrapping dictionary...");
                            prog.setProgress(50);
                            Thread.sleep(1000);
                            Dictionary.getInstance().loadDefaults(bootstrap == BOOTSTRAP_100K);
                        }

                        // Trigger saving the dictionary.
                        prog.setMessage("Finalizing...");
                        prog.setProgress(99);
                        Thread.sleep(1000);
                        Dictionary.getInstance().save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                () -> {
                    MainFrame.getInstance().back();
                    MainFrame.getInstance().replace(ViewFrame.getInstance());
                },
                null
            );
    }
}
