package dev.frilly.slangdict;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DisablingButtonModelListener implements ListSelectionListener {

    private final JTable table;
    private final JButton button;

    public DisablingButtonModelListener(final JTable table, final JButton button) {
        this.table = table;
        this.button = button;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (this.table.isEditing() || this.table.getSelectedRowCount() == 0 || e.getValueIsAdjusting()) {
            button.setEnabled(false);
            return;
        }
        button.setEnabled(true);
    }

}
