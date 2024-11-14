package dev.frilly.slangdict.model;

import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.interfaces.Translatable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * The model that holds the data to display in the View Frame.
 */
public final class DictionaryViewModel
    extends AbstractTableModel
    implements Translatable {

    /**
     * The record to show how much time was taken and how many queries returned.
     */
    public record QueryResult(int count, double time) {}

    private final String[] columnNames = new String[] {
        I18n.tl("view.word"),
        I18n.tl("view.definition"),
        I18n.tl("view.favorite"),
        I18n.tl("view.locked"),
    };

    public DictionaryViewModel() {
        I18n.register(this);
    }

    private final List<String> displayedWords = new CopyOnWriteArrayList<>();

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
            return new QueryResult(displayedWords.size(), time);
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
    public void updateTranslations() {
        columnNames[0] = I18n.tl("view.word");
        columnNames[1] = I18n.tl("view.definition");
        columnNames[2] = I18n.tl("view.favorite");
        columnNames[3] = I18n.tl("view.locked");
        fireTableStructureChanged();
    }
}
