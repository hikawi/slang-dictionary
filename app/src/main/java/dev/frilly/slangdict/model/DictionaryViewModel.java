package dev.frilly.slangdict.model;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.table.AbstractTableModel;

import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.I18n;

/**
 * The model that holds the data to display in the View Frame.
 */
public final class DictionaryViewModel extends AbstractTableModel {

    public record QueryResult(int queryCount, double time) {
    }

    private final List<String> displayedWords = new CopyOnWriteArrayList<>();

    /**
     * Queries the list and updates accordingly async, and retrieves a QueryResult.
     * 
     * @param query The query.
     * @return The future holding the result.
     */
    public CompletableFuture<QueryResult> query(final String query) {
        return CompletableFuture.supplyAsync(() -> {
            displayedWords.clear();
            final var start = System.currentTimeMillis();
            Dictionary.getInstance().getWords().entrySet().parallelStream()
                    .filter(e -> e.getKey().toLowerCase().contains(query.toLowerCase())
                            || e.getValue().definition.toLowerCase().contains(query.toLowerCase()))
                    .map(e -> e.getKey())
                    .sorted()
                    .forEach(displayedWords::add);
            fireTableDataChanged();
            return new QueryResult(displayedWords.size(), (System.currentTimeMillis() - start) / 1000.0);
        });
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 1 -> String.class;
            case 2, 3 -> Boolean.class;
            default -> Object.class;
        };
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
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> I18n.tl("view.word");
            case 1 -> I18n.tl("view.definition");
            case 2 -> I18n.tl("view.favorite");
            case 3 -> I18n.tl("view.locked");
            default -> "null";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final var key = displayedWords.get(rowIndex);
        final var word = Dictionary.getInstance().getWord(key);

        return switch (columnIndex) {
            case 0 -> key;
            case 1 -> word.definition;
            case 2 -> word.favorite;
            case 3 -> word.locked;
            default -> null;
        };
    }

}
