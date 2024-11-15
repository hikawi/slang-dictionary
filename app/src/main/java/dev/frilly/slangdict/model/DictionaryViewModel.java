package dev.frilly.slangdict.model;

import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.gui.ViewFrame;
import dev.frilly.slangdict.interfaces.Translatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * The model that holds the data to display in the View Frame.
 */
public final class DictionaryViewModel extends AbstractTableModel {

    /**
     * The record to show how much time was taken and how many queries returned.
     */
    public record QueryResult(int count, double time) {
    }

    private final String[] columnNames = new String[]{
        "Word", "Definition", "Favorite", "Locked"
    };

    public DictionaryViewModel() {
    }

    private final List<String> displayedWords = Collections.synchronizedList(new ArrayList<>());

    /**
     * Apply a query and return the query result after finishing.
     */
    public CompletableFuture<QueryResult> query(final String query) {
        return CompletableFuture.supplyAsync(() -> {
            displayedWords.clear();
            final var start = System.currentTimeMillis();
            Dictionary.getInstance().query(query).forEach(displayedWords::add);
            return System.currentTimeMillis() - start;
        }).thenApply(time -> {
            fireTableDataChanged();
            return new QueryResult(displayedWords.size(), time / 1000.0);
        });
    }

    @Override
    public int getRowCount() {
        return displayedWords.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int index) {
        return columnNames[index];
    }

    @Override
    public Class<?> getColumnClass(int index) {
        return switch (index) {
            case 0, 1 -> String.class;
            case 2, 3 -> Boolean.class;
            default -> Object.class;
        };
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final var word = Dictionary.getInstance()
            .getWord(displayedWords.get(rowIndex));
        return switch (columnIndex) {
            case 0 -> word.word;
            case 1 -> word.definition;
            case 2 -> word.favorite;
            case 3 -> word.locked;
            default -> null;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        final var word = Dictionary.getInstance().getWord(displayedWords.get(rowIndex));
        switch (columnIndex) {
            case 0:
                Dictionary.getInstance().editWord(word.word, (String) aValue);
                break;
            case 2:
                word.favorite = (boolean) aValue;
                break;
            case 3:
                word.locked = (boolean) aValue;
                break;
        }

        // Re-query to make sure stuff gets sorted.
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

}
