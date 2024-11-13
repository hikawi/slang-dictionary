package dev.frilly.slangdict.model;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Word;

/**
 * The renderer for a cell pane for a word in the dictionary, for really immersive view.
 */
public class DictionaryCellRenderer extends JPanel {

    private Word word;

    private final JButton favoriteButton = new JButton();
    private final JButton lockButton = new JButton();
    private final JButton deleteButton = new JButton();
    private final JButton editButton = new JButton();
    private final JLabel wordLabel = new JLabel();
    private final JTextArea definition = new JTextArea();

    private final ImageIcon starIcon = getIcon("/icons/star.png", 16, 16);
    private final ImageIcon filledStarIcon = getIcon("/icons/star-filled.png", 16, 16);

    private final ImageIcon lockIcon = getIcon("/icons/lock.png", 16, 16);
    private final ImageIcon lockOpenIcon = getIcon("/icons/lock-open.png", 16, 16);

    public DictionaryCellRenderer() {
        this.setup();
    }

    /**
     * Sets the word to render.
     * 
     * @param word The word.
     */
    public void setWord(final Word word) {
        this.word = word;
        update();
    }

    /**
     * Updates this component to properly represent the word.
     */
    public void update() {
        if (this.word == null)
            return;
        this.wordLabel.setText(word.word);
        this.definition.setText(String.join("\n", word.definition));
        setFavorite(word.favorite);
        setLocked(word.locked);
    }

    private ImageIcon getIcon(final String key, final int width, final int height) {
        return Application.getIcon(getClass().getResource(key), width, height);
    }

    private void setFavorite(final boolean val) {
        favoriteButton.setIcon(val ? filledStarIcon : starIcon);
    }

    private void setLocked(final boolean val) {
        lockButton.setIcon(val ? lockIcon : lockOpenIcon);
        editButton.setEnabled(!val);
    }

    private void setup() {
        final var layout = new GroupLayout(this);
        setLayout(layout);
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        definition.setLineWrap(true);
        definition.setWrapStyleWord(true);
        definition.setOpaque(false);
        definition.setEditable(false);

        wordLabel.putClientProperty("FlatLaf.styleClass", "h3");
        definition.putClientProperty("FlatLaf.styleClass", "medium");

        favoriteButton.setIcon(starIcon);
        favoriteButton.setOpaque(false);
        lockButton.setIcon(lockIcon);
        lockButton.setOpaque(false);
        deleteButton.setIcon(getIcon("/icons/remove.png", 16, 16));
        editButton.setIcon(getIcon("/icons/edit.png", 16, 16));

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING, true)
                        .addComponent(favoriteButton).addComponent(lockButton))
                .addGap(8)
                .addGroup(layout.createParallelGroup(Alignment.LEADING, true)
                        .addComponent(wordLabel).addComponent(definition))
                .addGap(8).addGroup(layout.createSequentialGroup().addComponent(editButton)
                        .addComponent(deleteButton)));

        layout.setVerticalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup().addComponent(favoriteButton)
                        .addComponent(lockButton))
                .addGroup(layout.createSequentialGroup().addComponent(wordLabel)
                        .addComponent(definition))
                .addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(editButton)
                        .addComponent(deleteButton)));

        setPreferredSize(new Dimension(600, 100));
    }

}
