package dev.frilly.slangdict.gui.component;

import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.Word;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The model for the dictionary to query.
 */
public final class DictionaryModel extends AbstractTableModel {

    private final List<Word> query = Collections.synchronizedList(new ArrayList<>());
    private final JLabel queryResult;
    private SwingWorker<Void, Void> worker;

    public DictionaryModel(final JLabel queryResult) {
        this.queryResult = queryResult;
    }

    /**
     * Applies a query and fires table data changed.
     *
     * @param q The query to query with.
     */
    public void query(final String q, final Runnable runnable) {
        if (worker != null)
            worker.cancel(true);

        worker = new SwingWorker<>() {
            private long time;

            @Override
            protected Void doInBackground() throws Exception {
                time = System.currentTimeMillis();
                query.clear();
                Dictionary.getInstance().query(q).forEach(query::add);
                return null;
            }

            @Override
            protected void done() {
                try {
                    final var elapsed = System.currentTimeMillis() - time;
                    queryResult.setText("Queried %d entries in %.3fs.".formatted(query.size(), elapsed / 1000.0));
                    fireTableDataChanged();

                    if (runnable != null)
                        runnable.run();
                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }
        };
        worker.execute();
    }

    @Override
    public int getRowCount() {
        return query.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch(columnIndex) {
            case 0, 1 -> String.class;
            case 2, 3 -> Boolean.class;
            default -> Object.class;
        };
    }

    @Override
    public String getColumnName(int column) {
        final var arr = List.of("Word", "Definition", "Favorite", "Locked");
        return arr.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final var word = query.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> word.word;
            case 1 -> word.definition;
            case 2 -> word.favorite;
            case 3 -> word.locked;
            default -> null;
        };
    }

}
