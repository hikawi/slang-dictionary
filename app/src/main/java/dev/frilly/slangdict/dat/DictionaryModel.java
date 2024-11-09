package dev.frilly.slangdict.dat;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.table.AbstractTableModel;

import dev.frilly.slangdict.Configuration;
import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.gui.Translatable;

/**
 * The modeling for the dictionary.
 */
public class DictionaryModel extends AbstractTableModel implements Translatable {

    private static final String[] columns = new String[] {
            I18n.tl("word"),
            I18n.tl("definition"),
            I18n.tl("favorite"),
    };

    private final List<String> displayedWords = new ArrayList<>();
    private String filter = "";

    public DictionaryModel() {
        I18n.register(this);
    }

    /**
     * Sets the filter and sets the viewbox to only show what is being viewed.
     * 
     * @param filter
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * Update the view.
     */
    public void updateView() {
        this.displayedWords.clear();
        this.displayedWords.addAll(Configuration.getWordsEntries()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .filter(entry -> entry.getKey().toLowerCase().contains(filter.toLowerCase())
                        || entry.getValue().toLowerCase().contains(filter.toLowerCase()))
                .filter(entry -> !Configuration.isShowOnlyFavorites() || Configuration.isFavorite(entry.getKey()))
                .map(entry -> entry.getKey())
                .sorted((a, b) -> {
                    if (Configuration.isFavorite(a) && !Configuration.isFavorite(b))
                        return -1; // a must be sorted first.
                    if (Configuration.isFavorite(b) && !Configuration.isFavorite(a))
                        return 1; // a must be sorted after.

                    // That means both favorite values are equal, sort alphabetically.
                    return a.compareTo(b);
                })
                .toList());
        this.fireTableDataChanged();
    }

    /**
     * Attempts to load defaults value provided in the program.
     */
    public void loadDefaults() {
        Configuration.clearWords();
        Configuration.clearFavorites();

        try {
            final var file = getClass().getResourceAsStream("/slang.txt");
            final var scanner = new Scanner(file);

            while (scanner.hasNext()) {
                final var line = scanner.nextLine();
                final var array = line.split("`");
                Configuration.putWord(array[0], array[1]);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.updateView();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public int getRowCount() {
        return this.displayedWords.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 2)
            return Boolean.class;
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final var word = this.displayedWords.get(rowIndex);
        switch (columnIndex) {
            case 0: // Word column
                return word;
            case 1: // Definition column
                return Configuration.getDefinition(word);
            default: // Favorite column
                return Configuration.isFavorite(word);
        }
    }

    @Override
    public void updateTranslations() {
        columns[0] = I18n.tl("word");
        columns[1] = I18n.tl("definition");
        columns[2] = I18n.tl("favorite");
        this.fireTableStructureChanged();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        final var word = this.displayedWords.get(rowIndex);
        switch (columnIndex) {
            case 0: // Changing a word.
                if (Configuration.hasWord((String) aValue)) {
                    Dialogs.error(I18n.tl("word-exists"));
                } else if (((String) aValue).isBlank()) {
                    Dialogs.error(I18n.tl("word-cant-be-empty"));
                } else {
                    final var def = Configuration.getDefinition(word);
                    final var favorite = Configuration.isFavorite(word);
                    Configuration.removeWord(word);
                    Configuration.putWord((String) aValue, def);
                    if (favorite)
                        Configuration.addFavorite(word);

                    this.updateView();
                }
                break;
            case 1: // Changing the definition.
                if (((String) aValue).isBlank()) {
                    Dialogs.error(I18n.tl("definition-cant-be-empty"));
                } else {
                    Configuration.setDefinition(word, (String) aValue);
                    this.updateView();
                }
                break;
            case 2: // Favoriting
                if ((Boolean) aValue)
                    Configuration.addFavorite(word);
                else
                    Configuration.removeFavorite(word);
                break;
        }
    }

}
