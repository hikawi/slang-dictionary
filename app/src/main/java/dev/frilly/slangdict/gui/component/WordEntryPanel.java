package dev.frilly.slangdict.gui.component;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Word;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 * The panel to show a word's entry.
 */
public final class WordEntryPanel extends JPanel {

    // Empty pane for when there is no word.
    private final JPanel emptyPanel = new JPanel();

    // Content pane for when there is a word.
    private final JPanel contentPanel = new JPanel();
    private final JLabel wordLabel = new JLabel();
    private final JTextArea definitionArea = new JTextArea();
    private final JButton starButton = new JButton();
    private final JButton lockButton = new JButton();
    private final JButton deleteButton = new JButton();
    private final JButton editButton = new JButton();

    private Optional<Word> word = Optional.empty();

    public WordEntryPanel() {
        setup();
    }

    private void setup() {
        starButton.setIcon(Application.getIcon("/icons/star.png", 16, 16));
        lockButton.setIcon(Application.getIcon("/icons/lock.png", 16, 16));
        deleteButton.setIcon(Application.getIcon("/icons/remove.png", 16, 16));
        editButton.setIcon(Application.getIcon("/icons/edit.png", 16, 16));

        emptyPanel.setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
        setPreferredSize(new Dimension(800, 80));

        final var l = new GroupLayout(contentPanel);
        contentPanel.setLayout(l);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        l.setAutoCreateGaps(true);
        l.setAutoCreateContainerGaps(true);

        starButton.setOpaque(false);
        lockButton.setOpaque(false);
        definitionArea.setEditable(false);
        definitionArea.setOpaque(false);
        wordLabel.putClientProperty("FlatLaf.styleClass", "h4");

        l.setHorizontalGroup(l.createSequentialGroup()
            .addGroup(l.createSequentialGroup()
                .addComponent(starButton)
                .addComponent(lockButton))
            .addGap(8)
            .addGap(8)
            .addGroup(l.createSequentialGroup()
                .addComponent(editButton)
                .addComponent(deleteButton)));
        l.setVerticalGroup(l.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(l.createParallelGroup()
                .addComponent(starButton)
                .addComponent(lockButton))
            .addGap(8)
            .addGap(8)
            .addGroup(l.createParallelGroup()
                .addComponent(editButton)
                .addComponent(deleteButton)));

        l.linkSize(starButton, lockButton, deleteButton, editButton);
    }

    private void updateStar() {
        final var star = Application.getIcon("/icons/star.png", 16, 16);
        final var starFilled = Application.getIcon("/icons/star-filled.png", 16, 16);
        word.map(w -> w.favorite)
            .map(b -> b ? star : starFilled)
            .ifPresent(starButton::setIcon);
    }

    private void updateLock() {
        final var lock = Application.getIcon("/icons/lock.png", 16, 16);
        final var lockOpen = Application.getIcon("/icons/lock-open.png", 16, 16);
        word.map(w -> w.locked)
            .map(b -> b ? lock : lockOpen)
            .ifPresent(lockButton::setIcon);
    }

    /**
     * Updates with the new word.
     *
     * @param w The word to update with.
     */
    public void updateWord(final Word w) {
        word = Optional.ofNullable(w);

        if (word.isEmpty()) {
            add(emptyPanel, BorderLayout.CENTER);
        } else {
            add(contentPanel, BorderLayout.CENTER);
            wordLabel.setText(w.word);
            definitionArea.setText(w.definition);
            updateStar();
            updateLock();
        }

        revalidate();
        repaint();
    }

}
