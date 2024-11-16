package dev.frilly.slangdict.gui.component;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data class for search history model.
 */
public final class SearchHistory extends AbstractListModel<String> {

    private final List<String> history = new ArrayList<>();

    /**
     * Push to history.
     *
     * @param q The query.
     */
    public void push(final String q) {
        history.add(q);
        fireIntervalAdded(this, history.size() - 1, history.size() - 1);
    }

    /**
     * Clears the search history.
     */
    public void clear() {
        final var size = history.size();
        history.clear();
        fireIntervalRemoved(this, 0, size - 1);
    }

    @Override
    public int getSize() {
        return history.size();
    }

    @Override
    public String getElementAt(int index) {
        return history.get(index);
    }

}
