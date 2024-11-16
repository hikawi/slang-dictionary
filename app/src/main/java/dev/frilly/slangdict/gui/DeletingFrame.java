package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.features.edit.DeleteWordFeature;
import dev.frilly.slangdict.interfaces.Overrideable;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.util.List;

/**
 * The frame to open when the deletion happens.
 */
public final class DeletingFrame implements Overrideable {

    private static DeletingFrame instance;

    private final JPanel panel = new JPanel();
    private final JLabel confirmText = new JLabel("Are you sure you want to delete these words?");

    // UI Components
    private final JList<String> list = new JList<>();
    private final JScrollPane scrollPane = new JScrollPane(list);
    private final JButton confirmButton = new JButton("Confirm");
    private final JButton cancelButton = new JButton("Cancel");

    private List<String> selectedWords;

    private DeletingFrame() {
        setup();
        setupActions();
    }

    public static DeletingFrame getInstance() {
        return instance == null ? instance = new DeletingFrame() : instance;
    }

    private void setup() {
        final var l = new GroupLayout(panel);
        panel.setLayout(l);
        l.setAutoCreateGaps(true);
        l.setAutoCreateContainerGaps(true);

        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        scrollPane.setPreferredSize(new Dimension(200, 200));
        confirmText.putClientProperty("FlatLaf.styleClass", "semibold large");

        l.setHorizontalGroup(l.createParallelGroup(Alignment.LEADING)
            .addComponent(confirmText)
            .addComponent(scrollPane)
            .addGap(16, 20, 24)
            .addGroup(Alignment.TRAILING, l.createSequentialGroup().addComponent(cancelButton).addComponent(confirmButton)));
        l.setVerticalGroup(l.createSequentialGroup()
            .addComponent(confirmText)
            .addGap(8)
            .addComponent(scrollPane)
            .addGroup(l.createParallelGroup(Alignment.BASELINE).addComponent(cancelButton).addComponent(confirmButton)));
    }

    private void setupActions() {
        cancelButton.addActionListener(e -> MainFrame.getInstance().back());
        confirmButton.addActionListener(e -> new DeleteWordFeature(selectedWords).run());
    }

    /**
     * Sets selected words to delete.
     *
     * @param selectedWords The words to delete.
     */
    public void setSelectedWords(List<String> selectedWords) {
        this.selectedWords = selectedWords;
        list.setListData(selectedWords.toArray(String[]::new));
    }

    @Override
    public JPanel getOverridingPane() {
        return panel;
    }

}
