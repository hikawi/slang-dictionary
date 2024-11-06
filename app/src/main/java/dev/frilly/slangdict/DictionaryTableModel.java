package dev.frilly.slangdict;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.table.AbstractTableModel;

public class DictionaryTableModel extends AbstractTableModel {

    private final File dataFile;

    private final Map<String, String> dictionary;
    private final List<String> words;

    public DictionaryTableModel() {
        this.dataFile = new File("./data.bin");
        this.dictionary = new HashMap<>();
        this.words = new ArrayList<>();

        try {
            if (!this.dataFile.exists())
                this.dataFile.createNewFile();
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sort the collection of word indicies. This does not mess with the dictionary.
     */
    public void sort() {
        Collections.sort(this.words);
    }

    /**
     * Retrieve a definition if defined.
     * 
     * @param word The word
     * @return The definition or null.
     */
    public String get(final String word) {
        return this.dictionary.get(word.toLowerCase());
    }

    /**
     * Put a word into the dictionary, and catalogue into the index list.
     * 
     * @param word       The word to put.
     * @param definition The definition associated.
     */
    public void putWord(final String word, final String definition) {
        this.words.add(word);
        this.dictionary.put(word.toLowerCase(), definition);
    }

    /**
     * Remove a word from the index list and its corresponding definition.
     * 
     * @param word The word.
     */
    public void removeWord(final String word) {
        this.words.remove(word);
        this.dictionary.remove(word.toLowerCase());
    }

    public void clear() {
        this.words.clear();
        this.dictionary.clear();
    }

    /**
     * Load saved data from the data.bin file in the same working directory.
     * 
     * This will override everything.
     */
    public void load() {
        clear();
        if (!this.dataFile.exists())
            return;

        try (var stream = new DataInputStream(new FileInputStream(this.dataFile))) {
            while (stream.available() > 0) {
                final var word = stream.readUTF();
                final var definition = stream.readUTF();
                putWord(word, definition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.fireTableDataChanged();
    }

    /**
     * Attempts to load defaults value provided in the program.
     */
    public void loadDefaults() {
        clear();
        try {
            final var file = getClass().getResourceAsStream("/slang.txt");
            final var scanner = new Scanner(file);

            while (scanner.hasNext()) {
                final var line = scanner.nextLine();
                System.out.println(line);
                final var array = line.split("`");
                putWord(array[0], array[1]);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.fireTableDataChanged();
    }

    /**
     * Searches through the dictionary with a provided input.
     * 
     * @param input The input to search with.
     * @return A collection of entries involving the search.
     */
    public Collection<Entry<String, String>> search(final String input) {
        return this.words.parallelStream()
                .map(word -> Map.entry(word, this.get(word)))
                .filter(entry -> entry.getValue() != null)
                .filter(entry -> entry.getKey().contains(input.toLowerCase())
                        || entry.getValue().contains(input.toLowerCase()))
                .toList();
    }

    /**
     * Save all current entries to the provided data file.
     */
    public void save() {
        try {
            if (!this.dataFile.exists())
                this.dataFile.createNewFile();

            var stream = new DataOutputStream(new FileOutputStream(this.dataFile));
            for (var entry : this.dictionary.entrySet()) {
                stream.writeUTF(entry.getKey());
                stream.writeUTF(entry.getValue());
            }

            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        return this.words.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final var word = this.words.get(rowIndex);
        return Map.entry(word, this.get(word));
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        final var columns = new String[] { "Word", "Definition" };
        return columns[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

}
