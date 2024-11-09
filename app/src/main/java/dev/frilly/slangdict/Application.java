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

import dev.frilly.slangdict.gui.ApplicationFrame;

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
        } catch (SecurityException e) {
            e.printStackTrace();
            System.err.println("The app was not given enough permissions to save data.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
