package dev.frilly.slangdict.listener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A simple listener to make it shorter to deal with document listener.
 */
@FunctionalInterface
public interface DocumentChangeListener extends DocumentListener {
    /**
     * Fires anytime when this document changes.
     */
    public void onChange(final DocumentEvent e);

    @Override
    default void changedUpdate(DocumentEvent e) {}

    @Override
    default void insertUpdate(DocumentEvent e) {
        onChange(e);
    }

    @Override
    default void removeUpdate(DocumentEvent e) {
        onChange(e);
    }
}
