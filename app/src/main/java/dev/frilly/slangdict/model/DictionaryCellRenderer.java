package dev.frilly.slangdict.model;

import java.awt.BorderLayout;
import java.awt.Color;
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
import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.Word;
import dev.frilly.slangdict.interfaces.Translatable;

/**
 * The renderer for a cell pane for a word in the dictionary, for really immersive view.
 */
public class DictionaryCellRenderer extends JPanel implements Translatable {

    private Word word;

    private final JPanel emptyPanel = new JPanel();
    private final JLabel emptyText = new JLabel();
    private final JPanel panel = new JPanel();

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
        this.setupActions();
        I18n.register(this);
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
        remove(emptyPanel);
        remove(panel);

        if (this.word == null) {
            add(emptyPanel);
            revalidate();
            repaint();
            return;
        }

        add(panel);
        wordLabel.setText(word.word);
        definition.setText(String.join("\n", word.definition));
        setFavorite(word.favorite);
        setLocked(word.locked);
        revalidate();
        repaint();
    }

    private ImageIcon getIcon(final String key, final int width, final int height) {
        return Application.getIcon(getClass().getResource(key), width, height);
    }

    private void setFavorite(final boolean val) {
        if (word != null) {
            word.favorite = val;
            favoriteButton.setIcon(val ? filledStarIcon : starIcon);
        }
    }

    private void setLocked(final boolean val) {
        if (word != null) {
            word.locked = val;
            lockButton.setIcon(val ? lockIcon : lockOpenIcon);
            editButton.setEnabled(!val);
        }
    }

    private void setup() {
        final var layout = new GroupLayout(panel);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        setLayout(new BorderLayout());

        emptyPanel.setLayout(new BorderLayout());
        emptyPanel.add(emptyText, BorderLayout.CENTER);

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
                        .addComponent(wordLabel).addGap(4).addComponent(definition))
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

    private void setupActions() {
        favoriteButton.addActionListener(e -> {
            if (word != null)
                setFavorite(!word.favorite);
        });

        lockButton.addActionListener(e -> {
            if (word != null)
                setLocked(!word.locked);
        });
    }

    @Override
    public void updateTranslations() {
        emptyText.setText(I18n.tl("view.empty"));
    }

}
