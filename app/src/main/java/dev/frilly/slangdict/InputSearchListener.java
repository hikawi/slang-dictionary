package dev.frilly.slangdict;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A listener to apply the search input to the dictionary model,
 * and filter out words.
 */
public class InputSearchListener implements DocumentListener {

    private final DictionaryTableModel model;
    private final JTextField field;

    public InputSearchListener(final DictionaryTableModel model, final JTextField field) {
        this.model = model;
        this.field = field;
    }

    private void onChange(final String text) {
        this.model.setFilter(text);
        this.model.updateView();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        this.onChange(this.field.getText());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        this.onChange(this.field.getText());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        this.onChange(this.field.getText());
    }

}
