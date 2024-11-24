package dev.frilly.slangdict.gui.component;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.Utilities;
import dev.frilly.slangdict.Word;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation for the panel to show a random word.
 */
public final class RandomWordPanel extends JPanel {

    // The pane to show an empty message if no entries are in dictionary.
    private final JPanel emptyPane  = new JPanel();
    private final JLabel emptyLabel = new JLabel("*cricket noises*");

    // The pane to show the actual content.
    private final JPanel         contentPane         = new JPanel();
    private final JLabel         wordLabel           = new JLabel();
    private final JTextArea      descriptionTextArea = new JTextArea();
    private final JButton        likeButton          = new JButton(
        "I like this!");
    private final JButton        randomButton        = new JButton("Next word");
    private       Optional<Word> word                = Optional.empty();

    public RandomWordPanel() {
        setup();
        setupActions();
    }

    private void setup() {
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        setPreferredSize(new Dimension(800, 100));

        final var ol = new GroupLayout(this);
        setLayout(ol);

        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emptyLabel.setVerticalAlignment(SwingConstants.CENTER);
        emptyPane.setLayout(new BorderLayout());
        emptyPane.add(emptyLabel, BorderLayout.CENTER);

        final var l = Utilities.group(contentPane);

        descriptionTextArea.setOpaque(false);
        descriptionTextArea.setRows(1);
        descriptionTextArea.setEditable(false);
        likeButton.setIcon(Application.getIcon("/icons/like.png", 16, 16));
        randomButton.setIcon(Application.getIcon("/icons/random.png", 16, 16));

        ol.setHorizontalGroup(
            ol.createSequentialGroup().addComponent(emptyPane));
        ol.setVerticalGroup(ol.createSequentialGroup().addComponent(emptyPane));

        l.setHorizontalGroup(
            l.createParallelGroup(GroupLayout.Alignment.LEADING, true)
                .addComponent(wordLabel)
                .addComponent(descriptionTextArea)
                .addGroup(GroupLayout.Alignment.TRAILING,
                          l.createSequentialGroup()
                              .addComponent(likeButton)
                              .addComponent(randomButton)));
        l.setVerticalGroup(l.createSequentialGroup()
                               .addComponent(wordLabel)
                               .addGap(4, 6, 8)
                               .addComponent(descriptionTextArea)
                               .addGap(10, 14, 18)
                               .addGroup(l.createParallelGroup(
                                       GroupLayout.Alignment.CENTER, true)
                                             .addComponent(likeButton)
                                             .addComponent(randomButton)));

        l.linkSize(likeButton, randomButton);
        wordLabel.putClientProperty("FlatLaf.styleClass", "h3");
    }

    private void setupActions() {
        randomButton.addActionListener(e -> randomize());
        likeButton.addActionListener(e -> setFavorite());
    }

    /**
     * Randomize the word and update.
     */
    public void randomize() {
        final var w = new SwingWorker<Word, Void>() {
            @Override
            protected Word doInBackground() {
                final var words = Dictionary.getInstance().getWords();
                if (words.isEmpty()) {
                    return null;
                }

                final var idx = ThreadLocalRandom.current()
                    .nextInt(words.size());
                final var word = words.entrySet()
                    .stream()
                    .skip(idx)
                    .findFirst();
                return word.orElseThrow().getValue();
            }

            @Override
            protected void done() {
                final Word w;
                try {
                    w = get();
                    updateWord(w);
                } catch (InterruptedException | ExecutionException e) {
                    e.fillInStackTrace();
                }
            }
        };
        w.execute();
    }

    private void setFavorite() {
        word.ifPresent(w -> w.favorite = true);
        wordLabel.setIcon(
            Application.getIcon("/icons/star-filled.png", 24, 24));
        likeButton.setEnabled(false);
    }

    /**
     * Updates this panel with the new word.
     *
     * @param w The word.
     */
    private void updateWord(final Word w) {
        word = Optional.ofNullable(w);
        if (word.isEmpty()) {
            final var l = (GroupLayout) getLayout();
            l.replace(contentPane, emptyPane);
        } else {
            wordLabel.setText(w.word);
            descriptionTextArea.setText(w.definition);

            final var icon = w.favorite ? "/icons/star-filled.png"
                                        : "/icons/star.png";
            wordLabel.setIcon(Application.getIcon(icon, 24, 24));
            likeButton.setEnabled(!w.favorite);

            final var l = (GroupLayout) getLayout();
            l.replace(emptyPane, contentPane);
        }
    }

}
