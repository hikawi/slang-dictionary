package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.Utilities;
import dev.frilly.slangdict.features.edit.AddWordFeature;
import dev.frilly.slangdict.interfaces.Overrideable;

import javax.swing.*;
import java.awt.*;

/**
 * The frame for adding a new word to a dictionary.
 */
public final class AddingFrame implements Overrideable {

    private static AddingFrame instance;
    private final  JPanel      panel = new JPanel();

    private final JLabel     addingToLabel      = new JLabel();
    private final JLabel     wordLabel          = new JLabel("Word");
    private final JTextField wordField          = new JTextField();
    private final JLabel     definitionLabel    = new JLabel("Definition");
    private final JTextArea  definitionTextArea = new JTextArea();

    private final JCheckBox favoriteCheckBox = new JCheckBox("Favorite");
    private final JCheckBox lockCheckBox     = new JCheckBox("Lock");

    private final JButton confirmButton = new JButton("Confirm");
    private final JButton cancelButton  = new JButton("Cancel");

    private AddingFrame() {
        setup();
        setupActions();
    }

    private void setup() {
        final var l = Utilities.group(panel);

        panel.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));
        addingToLabel.putClientProperty("FlatLaf.styleClass", "h3");
        wordLabel.putClientProperty("FlatLaf.styleClass", "semibold");
        definitionLabel.putClientProperty("FlatLaf.styleClass", "semibold");

        wordField.setPreferredSize(new Dimension(400, 20));
        definitionTextArea.setRows(3);
        definitionTextArea.setWrapStyleWord(true);
        definitionTextArea.setLineWrap(true);

        favoriteCheckBox.setIcon(
            Application.getIcon("/icons/star.png", 20, 20));
        lockCheckBox.setIcon(
            Application.getIcon("/icons/lock-open.png", 20, 20));
        favoriteCheckBox.setSelectedIcon(
            Application.getIcon("/icons/star-filled.png", 20, 20));
        lockCheckBox.setSelectedIcon(
            Application.getIcon("/icons/lock.png", 20, 20));

        l.setVerticalGroup(l.createSequentialGroup()
            .addComponent(addingToLabel)
            .addGap(24, 28, 32)
            .addComponent(true, wordLabel)
            .addGap(4, 6, 8)
            .addComponent(wordField)
            .addGap(12, 16, 20)
            .addComponent(definitionLabel)
            .addGap(4, 6, 8)
            .addComponent(definitionTextArea)
            .addGap(12, 16, 20)
            .addComponent(favoriteCheckBox)
            .addGap(4, 6, 8)
            .addComponent(lockCheckBox)
            .addGap(24, 28, 32)
            .addGroup(l.createParallelGroup()
                .addComponent(cancelButton)
                .addComponent(confirmButton)));

        l.setHorizontalGroup(
            l.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(addingToLabel)
                .addComponent(wordLabel)
                .addComponent(wordField)
                .addComponent(definitionLabel)
                .addComponent(definitionTextArea)
                .addComponent(favoriteCheckBox)
                .addComponent(lockCheckBox)
                .addGroup(GroupLayout.Alignment.TRAILING,
                    l.createSequentialGroup()
                        .addComponent(cancelButton)
                        .addComponent(confirmButton)));

        l.linkSize(SwingConstants.HORIZONTAL, wordField, definitionTextArea);
        l.linkSize(SwingConstants.HORIZONTAL, confirmButton, cancelButton);
    }

    private void setupActions() {
        cancelButton.addActionListener(e -> MainFrame.getInstance().back());
        confirmButton.addActionListener(
            e -> new AddWordFeature(wordField.getText(),
                definitionTextArea.getText(), favoriteCheckBox.isSelected(),
                lockCheckBox.isSelected()).run());
    }

    public static AddingFrame getInstance() {
        return instance == null ? instance = new AddingFrame() : instance;
    }

    @Override
    public JPanel getOverridingPane() {
        addingToLabel.setText("Adding to \"%s\"...".formatted(
            Dictionary.getInstance().getName()));
        wordField.setText("");
        definitionTextArea.setText("");
        favoriteCheckBox.setSelected(false);
        lockCheckBox.setSelected(false);
        return panel;
    }

}
