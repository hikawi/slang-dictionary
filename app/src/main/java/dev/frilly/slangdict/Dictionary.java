package dev.frilly.slangdict;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The singleton instance holding the dictionary database.
 */
public final class Dictionary {

    private static Dictionary instance;

    private String name;
    private File file;

    private Map<String, Word> words = new HashMap<>();

    private Dictionary() {
    }

    /**
     * Renames the current dictionary instance.
     * 
     * @param name The name.
     */
    public void rename(final String name) {
        this.name = name;
    }

    /**
     * Sets the associated file of this dictionary.
     * 
     * @param file The file.
     */
    public void setFile(final File file) {
        this.file = file;
    }

    /**
     * Loads the defaults dictionary.
     * 
     * The defaults slang.txt in the .jar file has a different format
     * from the normally saved dictionary.
     */
    public void loadDefaults() {
        words.clear();

        try {
            final var input = new BufferedInputStream(
                    ClassLoader.getSystemClassLoader().getResourceAsStream("slang.txt"));
            final var scanner = new Scanner(input);

            while (scanner.hasNext()) {
                final var line = scanner.nextLine().split("`");
                final var word = new Word();
                word.word = line[0];
                Arrays.stream(line[1].split("|")).map(String::strip).forEach(word.definition::add);
                words.put(word.word.toLowerCase(), word);
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
            Dialogs.error("file.load.error");
        }
    }

    /**
     * Saves the current dictionary.
     */
    public void save() {
        if (file == null)
            throw new RuntimeException("No files are associated with the current dictionary.");

        try {
            if (!file.exists())
                file.createNewFile();

            final var output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            output.writeUTF(name);

            for (final var word : words.values()) {
                output.writeUTF(word.word);
                output.writeInt(word.definition.size());
                for (final var def : word.definition)
                    output.writeUTF(def);

                output.writeBoolean(word.favorite);
                output.writeBoolean(word.locked);
            }

            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
            Dialogs.error("file.save.error", name, file.getName());
        }
    }

    public static Dictionary getInstance() {
        return switch (instance) {
            case null -> instance = new Dictionary();
            default -> instance;
        };
    }

}
