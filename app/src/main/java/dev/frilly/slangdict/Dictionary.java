package dev.frilly.slangdict;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The singleton instance holding the dictionary database.
 */
public final class Dictionary {

    private static Dictionary instance;

    private String name;
    private File file;
    private Map<String, Word> words = new HashMap<>();
    private final Map<String, Set<String>> definitionIndex = new HashMap<>();

    private Dictionary() {}

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
        return this.words.get(key.toLowerCase());
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
     * Sets the associated file of this dictionary.
     * 
     * @param file The file.
     */
    public void setFile(final File file) {
        this.file = file;
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
     * Attempts to query the entry list.
     * 
     * @param query The query
     * @return The queried results
     */
    public List<String> query(final String query) {
        final var q = query.toLowerCase();
        Set<String> matchedWords = new HashSet<>();

        // Check for direct matches in wordMap
        words.keySet().stream().filter(word -> word.contains(q)).forEach(matchedWords::add);

        // Check for matches in definitionIndex
        definitionIndex.entrySet().stream().filter(entry -> entry.getKey().contains(q))
                .flatMap(entry -> entry.getValue().stream()).forEach(matchedWords::add);

        return matchedWords.stream().sorted().toList();
    }

    /**
     * Loads the specified file.
     */
    public void load() {
        if (file == null || !file.exists())
            return;

        try {
            final var tempDict = new HashMap<String, Word>();
            final var input =
                    new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            rename(input.readUTF());

            while (input.available() > 0) {
                final var word = new Word();
                word.word = input.readUTF();

                final var length = input.readInt();
                for (int i = 0; i < length; i++)
                    word.definition.add(input.readUTF());

                word.favorite = input.readBoolean();
                word.locked = input.readBoolean();
                tempDict.put(word.word.toLowerCase(), word);
            }

            input.close();

            // No errors happened.
            words.clear();
            words.putAll(tempDict);
            for (var entry : words.entrySet()) {
                String word = entry.getKey().toLowerCase();

                // Build inverted index for each word in the definitions
                for (String definition : entry.getValue().definition) {
                    for (String term : definition.toLowerCase().split("\\s+")) {
                        definitionIndex.computeIfAbsent(term, k -> ConcurrentHashMap.newKeySet())
                                .add(word);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Dialogs.error("file.load.error", file.getName());
        }
    }

    /**
     * Loads the defaults dictionary.
     * 
     * The defaults slang.txt in the .jar file has a different format from the normally saved
     * dictionary.
     */
    public void loadDefaults() {
        loadDefaults(false);
    }

    /**
     * Loads the defaults dictionary.
     * 
     * The defaults slang.txt in the .jar file has a different format from the normally saved
     * dictionary.
     */
    public void loadDefaults(boolean is100k) {
        final var file = is100k ? "/slang_100k.txt" : "/slang.txt";
        words.clear();

        try {
            final var input = new BufferedInputStream(getClass().getResourceAsStream(file));
            if (input == null)
                throw new RuntimeException("No resource found");

            final var scanner = new Scanner(input);

            while (scanner.hasNext()) {
                final var line = scanner.nextLine().split("`");

                final var word = new Word();
                word.word = line[0];
                word.definition = Arrays.stream(line[1].split("\\|")).map(String::strip).toList();
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
        if (file == null)
            return;

        try {
            if (!file.exists())
                file.createNewFile();

            final var output =
                    new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            output.writeUTF(name);

            for (final var word : words.values()) {
                output.writeUTF(word.word);
                output.writeInt(word.definition.size());
                for (var def : word.definition)
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
