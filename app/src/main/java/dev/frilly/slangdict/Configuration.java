package dev.frilly.slangdict;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Basic options and checkboxes states for the program as
 * the app's configuration.
 */
public final class Configuration {

    public static int SORT_TOP = 0;
    public static int SORT_BOTTOM = 1;
    public static int SORT_EXCLUDE = 2;
    public static int SORT_HIDDEN = 3;

    public static void setScrollBarPolicy(final int policy) {
    }

    public static void setSortingPolicy(final int policy) {

    }

    /**
     * Load the application.
     */
    public static void load() {
        try {
            final File dataFile = new File("./.data");
            if (!dataFile.exists())
                dataFile.mkdirs();

            final File data = new File(dataFile, "main.dat");
            if (!data.exists()) {
                data.createNewFile();
                return;
            }

            final var input = new DataInputStream(new BufferedInputStream(new FileInputStream(data)));
            setScrollBarPolicy(input.readInt());
            setSortingPolicy(input.readInt());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
