package dev.frilly.slangdict.gui.component;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data class for search history model.
 */
public final class SearchHistory extends AbstractTableModel {

    private final List<HistoryPoint> history = new ArrayList<>();

    /**
     * Push to history.
     *
     * @param q The query.
     */
    public void push(
        final String q,
        final int queries,
        final double timeTaken
    ) {
        final var sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final var s   = sdf.format(new Date());

        history.add(new HistoryPoint(q, queries, timeTaken, s));
        fireTableRowsInserted(history.size() - 1, history.size() - 1);
    }

    /**
     * Clears the search history.
     */
    public void clear() {
        history.clear();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return history.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final var point = history.get(history.size() - rowIndex - 1);
        return switch (columnIndex) {
            case 0 -> point.query();
            case 1 -> point.count();
            case 2 -> point.elapsed();
            case 3 -> point.time();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return List.of("Query", "Results", "Time taken", "When").get(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 3 -> String.class;
            case 1 -> Integer.class;
            case 2 -> Double.class;
            default -> Object.class;
        };
    }

    public record HistoryPoint(String query, int count, double elapsed,
                               String time) {

    }

}
