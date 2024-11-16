package dev.frilly.slangdict;

import java.io.*;
import java.util.*;

/**
 * The singleton instance holding the dictionary database.
 */
public final class Dictionary {

    private static Dictionary instance;
    private final Map<String, Word> words = new HashMap<>();
    private String name;
    private File file;

    private Dictionary() {
    }

    public static Dictionary getInstance() {
        return switch (instance) {
            case null -> instance = new Dictionary();
            default -> instance;
        };
    }

    /**
     * Gets the name of the current dictionary.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves a view of the words map.
     *
     * @return The words map, unmodifiable.
     */
    public Map<String, Word> getWords() {
        return Collections.unmodifiableMap(words);
    }

    public Word getWord(final String key) {
        if (key == null) return null;
        return this.words.get(key.toLowerCase());
    }

    /**
     * Edits the word to a new word.
     *
     * @param word    The word to match.
     * @param newWord The new word.
     * @return The word if it was changed, or null.
     */
    public Word editWord(final String word, final String newWord) {
        final var wordObj = getWord(word);
        if (wordObj == null)
            return null;

        wordObj.word = newWord;
        words.remove(word.toLowerCase());
        words.put(newWord.toLowerCase(), wordObj);
        return wordObj;
    }

    /**
     * Deletes a word.
     *
     * @param word The word.
     */
    public void deleteWord(final String word) {
        words.remove(word.toLowerCase());
    }

    /**
     * Nukes all entries of this dictionary.
     */
    public void bomb() {
        words.clear();
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
     * Retrieves the file instance.
     *
     * @return The file.
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the associated file of this dictionary.
     *
     * @param file The file.
     */
    public void setFile(final File file) {
        this.file = file;
        if (file == null) words.clear();
    }

    /**
     * Loads the specified file.
     */
    public void load() {
        if (file == null || !file.exists()) {
            words.clear();
            return;
        }

        try {
            final var tempDict = new HashMap<String, Word>();
            final var input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            rename(input.readUTF());

            while (input.available() > 0) {
                final var word = new Word();
                word.word = input.readUTF();
                word.definition = input.readUTF();
                word.favorite = input.readBoolean();
                word.locked = input.readBoolean();
                tempDict.put(word.word.toLowerCase(), word);
            }

            input.close();

            // No errors happened.
            words.clear();
            words.putAll(tempDict);
        } catch (Exception e) {
            e.printStackTrace();
            Dialogs.error("file.load.error", file.getName());
        }
    }

    /**
     * Loads the defaults dictionary.
     * <p>
     * The defaults slang.txt in the .jar file has a different format from the normally saved dictionary.
     */
    public void loadDefaults() {
        loadDefaults(false);
    }

    /**
     * Loads the defaults dictionary.
     * <p>
     * The defaults slang.txt in the .jar file has a different format from the
     * normally saved
     * dictionary.
     */
    public void loadDefaults(boolean is100k) {
        final var file = is100k ? "/slang_100k.txt" : "/slang.txt";
        words.clear();

        try {
            final var res = Objects.requireNonNull(getClass().getResourceAsStream(file));
            final var input = new BufferedInputStream(res);
            final var scanner = new Scanner(input);

            while (scanner.hasNext()) {
                final var line = scanner.nextLine().split("`");

                final var word = new Word();
                word.word = line[0];
                word.definition = line[1];
                words.put(word.word.toLowerCase(), word);
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
            Dialogs.error("file.load.error", file);
        }
    }

    /**
     * Saves the current dictionary.
     */
    public void save() {
        if (file == null) return;

        try {
            if (!file.exists()) file.createNewFile();

            final var output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            output.writeUTF(name);

            for (final var word : words.values()) {
                output.writeUTF(word.word);
                output.writeUTF(word.definition);
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

}
