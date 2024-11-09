package dev.frilly.slangdict;

import java.awt.EventQueue;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import dev.frilly.slangdict.dat.DictionaryModel;
import dev.frilly.slangdict.gui.ApplicationFrame;
import dev.frilly.slangdict.gui.DictionaryView;

/**
 * The singleton class that holds the application together.
 */
public class Application {

    private static Application INSTANCE;

    private final ApplicationFrame frame;

    private Application() {
        this.frame = new ApplicationFrame();
    }

    public static Application getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Application();
        return INSTANCE;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            FlatMacDarkLaf.setup();

            final var app = Application.getInstance();
            app.start();
        });
    }

    /**
     * Retrieves the main application frame.
     * 
     * @return The frame.
     */
    public ApplicationFrame getFrame() {
        return frame;
    }

    /**
     * Retrieves the inner dictionary model.
     * 
     * @return The model.
     */
    public DictionaryModel getDictionaryModel() {
        return frame.<DictionaryView>getComponent("dictionary-view").getModel();
    }

    /**
     * Start the application.
     */
    public void start() {
        this.loadData();

        this.frame.setup();
        this.frame.setupCloseListener();
        this.frame.pack();
        this.frame.setLocationRelativeTo(null); // Center the app.
        this.frame.setVisible(true);
    }

    /**
     * Loads the configuration's data and puts into Configuration class.
     * 
     * @param file The file
     * @throws Exception May throw random stuff idk.
     */
    public void loadConfigData(final File file) throws Exception {
        if (!file.exists())
            return; // Nothing to load lmao.

        final var input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        I18n.setLocale(Locale.of(input.readUTF()));
        Configuration.setAutosave(input.readBoolean());
        Configuration.setShowOnlyFavorites(input.readBoolean());

        input.close();
    }

    /**
     * Loads the saved dictionary.
     * 
     * @param file The file
     * @throws Exception
     */
    public void loadDictionaryData(final File file) throws Exception {
        if (!file.exists())
            return;

        final var input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        Configuration.clearWords();
        Configuration.clearFavorites();

        while (input.available() > 0) {
            final var key = input.readUTF();
            final var definition = input.readUTF();
            final var favorite = input.readBoolean();
            Configuration.putWord(key, definition);
            if (favorite)
                Configuration.addFavorite(key);
        }
        Configuration.sortWords();

        input.close();
    }

    /**
     * Saves the data from Configuration.
     * 
     * @param file the file
     * @throws Exception May throw random stuff idk.
     */
    public void saveConfigData(final File file) throws Exception {
        if (!file.exists())
            file.createNewFile();

        final var output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        output.writeUTF(Configuration.getLocale().getLanguage());
        output.writeBoolean(Configuration.isAutosave());
        output.writeBoolean(Configuration.isShowOnlyFavorites());

        output.flush();
        output.close();
    }

    /**
     * Saves the dictionary data down to a file.
     * 
     * @param file The file to save to.
     * @throws Exception
     */
    public void saveDictionaryData(final File file) throws Exception {
        if (!file.exists())
            file.createNewFile();

        final var output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        for (final var entry : Configuration.getWordsEntries()) {
            output.writeUTF(entry.getKey());
            output.writeUTF(entry.getValue());
            output.writeBoolean(Configuration.isFavorite(entry.getKey()));
        }

        output.flush();
        output.close();
    }

    /**
     * Loads the application data.
     */
    public void loadData() {
        try {
            final var dataFolder = new File("./data");
            if (!dataFolder.exists())
                dataFolder.mkdirs();

            final var configFile = new File(dataFolder, "config.bin");
            loadConfigData(configFile);

            final var dataFile = new File(dataFolder, "dictionary.bin");
            loadDictionaryData(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the application data.
     */
    public void saveData() {
        try {
            final var dataFolder = new File("./data");
            if (!dataFolder.exists())
                dataFolder.mkdirs();

            final var configFile = new File(dataFolder, "config.bin");
            saveConfigData(configFile);

            final var dataFile = new File(dataFolder, "dictionary.bin");
            saveDictionaryData(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
