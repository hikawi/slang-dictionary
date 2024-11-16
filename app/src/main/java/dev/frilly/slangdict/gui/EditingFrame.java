package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.Word;
import dev.frilly.slangdict.features.edit.AddWordFeature;
import dev.frilly.slangdict.features.edit.EditWordFeature;
import dev.frilly.slangdict.interfaces.Overrideable;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("DuplicatedCode")
public class EditingFrame implements Overrideable {

    private static EditingFrame instance;
    private final JPanel panel = new JPanel();

    private final JLabel addingToLabel = new JLabel();
    private final JLabel wordLabel = new JLabel("Word");
    private final JTextField wordField = new JTextField();
    private final JLabel definitionLabel = new JLabel("Definition");
    private final JTextArea definitionTextArea = new JTextArea();

    private final JButton confirmButton = new JButton("Confirm");
    private final JButton cancelButton = new JButton("Cancel");

    private Word word = null;

    private EditingFrame() {
        setup();
        setupActions();
    }

    public static EditingFrame getInstance() {
        return instance == null ? instance = new EditingFrame() : instance;
    }

    private void setup() {
        final var l = new GroupLayout(panel);
        l.setAutoCreateContainerGaps(true);
        l.setAutoCreateGaps(true);

        panel.setLayout(l);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));
        addingToLabel.putClientProperty("FlatLaf.styleClass", "h3");
        wordLabel.putClientProperty("FlatLaf.styleClass", "semibold");
        definitionLabel.putClientProperty("FlatLaf.styleClass", "semibold");

        wordField.setPreferredSize(new Dimension(400, 20));
        definitionTextArea.setRows(3);
        definitionTextArea.setWrapStyleWord(true);
        definitionTextArea.setLineWrap(true);

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
            .addGap(24, 28, 32)
            .addGroup(l.createParallelGroup()
                .addComponent(cancelButton)
                .addComponent(confirmButton)));

        l.setHorizontalGroup(l.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(addingToLabel)
            .addComponent(wordLabel)
            .addComponent(wordField)
            .addComponent(definitionLabel)
            .addComponent(definitionTextArea)
            .addGroup(GroupLayout.Alignment.TRAILING, l.createSequentialGroup()
                .addComponent(cancelButton)
                .addComponent(confirmButton)));

        l.linkSize(SwingConstants.HORIZONTAL, wordField, definitionTextArea);
        l.linkSize(SwingConstants.HORIZONTAL, confirmButton, cancelButton);
    }

    /**
     * Sets the word related for the edit option.
     *
     * @param word the word object.
     */
    public void setWord(Word word) {
        this.word = word;
    }

    private void setupActions() {
        cancelButton.addActionListener(e -> MainFrame.getInstance().back());
        confirmButton.addActionListener(e -> new EditWordFeature(word.word, wordField.getText(), definitionTextArea.getText()).run());
    }

    @Override
    public JPanel getOverridingPane() {
        addingToLabel.setText("Editing word in \"%s\"...".formatted(Dictionary.getInstance().getName()));
        if(word != null) {
            wordField.setText(word.word);
            definitionTextArea.setText(word.definition);
        } else {
            wordField.setText("");
            definitionTextArea.setText("");
        }
        return panel;
    }

}
