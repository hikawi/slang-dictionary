package dev.frilly.slangdict.model;

import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public final class DictionaryViewRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        final var comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (column != 1)
            return comp;

        @SuppressWarnings("unchecked")
        final var val = (List<String>) value;
        return new JLabel(String.valueOf(val.size()));
    }

}
