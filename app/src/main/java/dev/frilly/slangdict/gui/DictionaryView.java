package dev.frilly.slangdict.gui;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dev.frilly.slangdict.dat.DictionaryModel;

/**
 * A scrolling pane to view the table.
 */
public final class DictionaryView extends JComponent {

    private final DictionaryModel model;

    public DictionaryView() {
        this.model = new DictionaryModel();
    }

    public DictionaryModel getModel() {
        return model;
    }

    public void add(final JFrame frame) {
        final var table = new JTable(model);
        final var pane = new JScrollPane(table);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(600);
        table.getColumnModel().getColumn(1).setPreferredWidth(1800);
        table.getColumnModel().getColumn(2).setPreferredWidth(600);
        model.updateView();

        frame.getContentPane().add(Box.createVerticalStrut(4));
        frame.getContentPane().add(pane);
        frame.getContentPane().add(Box.createVerticalStrut(4));
    }

}
