package dev.frilly.slangdict.listener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.frilly.slangdict.Application;

/**
 * A listener to apply the search input to the dictionary model,
 * and filter out words.
 */
public class InputSearchListener implements DocumentListener {

    private final JTextField field;

    public InputSearchListener(final JTextField field) {
        this.field = field;
    }

    private void onChange(final String text) {
        Application.getInstance().getDictionaryModel().setFilter(text);
        Application.getInstance().getDictionaryModel().updateView();
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
