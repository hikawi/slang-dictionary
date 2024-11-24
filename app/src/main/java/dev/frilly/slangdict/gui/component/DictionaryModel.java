package dev.frilly.slangdict.gui.component;

import dev.frilly.slangdict.Word;
import dev.frilly.slangdict.features.search.QueryFeature;
import dev.frilly.slangdict.features.search.SortFavoritesFeature;
import dev.frilly.slangdict.gui.HistoryFrame;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The model for the dictionary to query.
 */
public final class DictionaryModel extends AbstractTableModel {

    private final List<Word>        queryList = Collections.synchronizedList(
        new ArrayList<>());
    private final JLabel            queryResult;
    private final JCheckBox         matchWord;
    private final JCheckBox         matchDefinition;
    private final JCheckBox         matchCase;
    private final JCheckBox         matchRegex;
    private final JComboBox<String> comboBox;

    private SwingWorker<Void, Void> worker;

    public DictionaryModel(
        final JLabel queryResult, final JCheckBox matchWord,
        final JCheckBox matchDefinition, final JCheckBox matchCase,
        final JCheckBox matchRegex, final JComboBox<String> comboBox
    ) {
        this.queryResult     = queryResult;
        this.matchWord       = matchWord;
        this.matchDefinition = matchDefinition;
        this.matchCase       = matchCase;
        this.matchRegex      = matchRegex;
        this.comboBox        = comboBox;
    }

    public void query(final String q) {
        query(q, null);
    }

    /**
     * Applies a query and fires table data changed.
     *
     * @param q The query to query with.
     */
    public void query(final String q, final Runnable runnable) {
        if (worker != null) {
            worker.cancel(true);
        }

        worker = new SwingWorker<>() {
            private long time;

            @Override
            protected Void doInBackground() throws Exception {
                time = System.currentTimeMillis();
                queryList.clear();

                final var s = new QueryFeature(q, matchWord.isSelected(),
                                               matchDefinition.isSelected(),
                                               matchCase.isSelected(),
                                               matchRegex.isSelected()).get();
                final var sel = comboBox.getSelectedIndex();
                new SortFavoritesFeature(sel).apply(s).forEach(queryList::add);
                return null;
            }

            @Override
            protected void done() {
                final var elapsed = System.currentTimeMillis() - time;
                queryResult.setText(
                    "Queried %d entries in %.3fs.".formatted(queryList.size(),
                                                             elapsed / 1000.0));
                fireTableDataChanged();
                Optional.ofNullable(runnable).ifPresent(Runnable::run);

                HistoryFrame.getInstance()
                    .push(q, queryList.size(), elapsed / 1000.0);
            }
        };
        worker.execute();
    }

    @Override
    public int getRowCount() {
        return queryList.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final var word = queryList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> word.word;
            case 1 -> word.definition;
            case 2 -> word.favorite;
            case 3 -> word.locked;
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        final var arr = List.of("Word", "Definition", "Favorite", "Locked");
        return arr.get(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 1 -> String.class;
            case 2, 3 -> Boolean.class;
            default -> Object.class;
        };
    }

}
