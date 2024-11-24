package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.interfaces.Overrideable;

import javax.swing.*;
import java.awt.*;

/**
 * The frame to show when game is over.
 */
public final class GameOverFrame implements Overrideable {

    private static GameOverFrame instance;

    private final JPanel outerPane   = new JPanel(new BorderLayout());
    private final JPanel contentPane = new JPanel();

    private final JLabel gameoverText = new JLabel("Game Over");
    private final JLabel scoreLabel   = new JLabel("Score");
    private final JLabel scoreValue   = new JLabel();

    private final JButton backButton = new JButton("Back");

    private GameOverFrame() {
        setup();
        setupStyles();
        setupActions();
    }

    private void setup() {
        final var l = new GroupLayout(contentPane);
        contentPane.setLayout(l);
        l.setAutoCreateGaps(true);
        l.setAutoCreateContainerGaps(true);

        l.setVerticalGroup(l.createSequentialGroup()
            .addComponent(gameoverText)
            .addGap(8, 10, 12)
            .addGroup(l.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(scoreLabel)
                .addComponent(scoreValue))
            .addGap(32)
            .addComponent(backButton));
        l.setHorizontalGroup(l.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(gameoverText)
            .addGroup(l.createSequentialGroup()
                .addComponent(scoreLabel)
                .addComponent(scoreValue))
            .addComponent(backButton));
        outerPane.add(contentPane, BorderLayout.CENTER);
    }

    private void setupStyles() {
        gameoverText.putClientProperty("FlatLaf.styleClass", "h1");
        scoreValue.putClientProperty("FlatLaf.styleClass", "h3");
    }

    private void setupActions() {
        backButton.addActionListener(
            e -> MainFrame.getInstance().replace(QuizFrame.getInstance()));
    }

    public static GameOverFrame getInstance() {
        return instance == null ? instance = new GameOverFrame() : instance;
    }

    @Override
    public JPanel getOverridingPane() {
        scoreValue.setText(String.valueOf(
            (long) GameFrame.getInstance().getState().getScore()));
        return outerPane;
    }

}
