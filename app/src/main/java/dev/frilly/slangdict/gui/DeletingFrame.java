package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.Word;
import dev.frilly.slangdict.interfaces.Overrideable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.GroupLayout.Alignment;

/**
 * The frame to open when the deletion happens.
 */
public final class DeletingFrame implements Overrideable {

    private static DeletingFrame instance;

    public static DeletingFrame getInstance() {
        return instance == null ? instance = new DeletingFrame() : instance;
    }

    private List<Word> selectedWords;

    // UI Components

    private final JPanel panel = new JPanel();
    private final JLabel confirmText = new JLabel("Are you sure you want to delete these words?");

    private final JList<String> list = new JList<>();
    private final JScrollPane scrollPane = new JScrollPane(list);

    private final JButton confirmButton = new JButton("Confirm");
    private final JButton cancelButton = new JButton("Cancel");

    private DeletingFrame() {
        setup();
        setupActions();
    }

    private void setup() {
        final var l = new GroupLayout(panel);
        panel.setLayout(l);
        l.setAutoCreateGaps(true);
        l.setAutoCreateContainerGaps(true);

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
        confirmButton.addActionListener(e -> {
            selectedWords.stream().map(w -> w.word).forEach(Dictionary.getInstance()::deleteWord);
            MainFrame.getInstance().back();
            Dialogs.info("Deleted %d word%s.", selectedWords.size(), selectedWords.size() != 1 ? "s" : "");
        });
    }

    @Override
    public JPanel getOverridingPane() {
//        selectedWords = ViewFrame.getInstance().getSelectedWords().toList();
//        list.setListData(selectedWords.stream().map(w -> w.word).toArray(String[]::new));
        return panel;
    }

}
