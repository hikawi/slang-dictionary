package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.interfaces.Overrideable;

import javax.swing.*;
import java.awt.*;

/**
 * The frame to show when game is over.
 */
public final class GameOverFrame implements Overrideable {

    private static GameOverFrame instance;

    private final JPanel outerPane = new JPanel(new BorderLayout());
    private final JPanel contentPane = new JPanel();

    private GameOverFrame() {
        setup();
    }

    public static GameOverFrame getInstance() {
        return instance == null ? instance = new GameOverFrame() : instance;
    }

    private void setup() {
        outerPane.add(new JLabel("Game Over"), BorderLayout.CENTER);
    }

    @Override
    public JPanel getOverridingPane() {
        return outerPane;
    }

}
