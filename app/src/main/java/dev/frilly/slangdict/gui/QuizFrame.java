package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.features.quiz.LifelineFeature;
import dev.frilly.slangdict.gui.component.QuizLifelinePanel;
import dev.frilly.slangdict.interfaces.Overrideable;

import javax.swing.*;

/**
 * The implementation for the frame for showing the quiz.
 */
public final class QuizFrame implements Overrideable {

    private static QuizFrame instance;

    // UI Components
    private final JPanel panel = new JPanel();

    // Title section
    private final JLabel    titleText       = new JLabel("Dictionary Quiz");
    private final JTextArea descriptionText = new JTextArea(
        "You will be given random words and definitions as questions, and " + "you" + " must choose the correct corresponding answer.",
        3, 40);
    private final JTextArea cautionText     = new JTextArea(
        "You will start with a specified amount of HP. The timer ticking " +
        "will" + " drain your HP, and incorrect choices critically damage " +
        "you" + ". Correct" + " answers grant \"Points\", affected by the " +
        "combo you " + "can get.",
        3, 40);

    // Lifeline sections
    private final QuizLifelinePanel lifelinePanel = new QuizLifelinePanel();

    // Start quiz button
    private final JButton nevermindButton = new JButton("Nevermind");
    private final JButton startButton     = new JButton("Start");

    private QuizFrame() {
        setupStyles();
        setup();
        setupActions();
    }

    private void setupStyles() {
        titleText.putClientProperty("FlatLaf.styleClass", "h2");

        cautionText.setWrapStyleWord(true);
        cautionText.setLineWrap(true);
        cautionText.setOpaque(false);
        cautionText.setEditable(false);

        descriptionText.setWrapStyleWord(true);
        descriptionText.setLineWrap(true);
        descriptionText.setOpaque(false);
        descriptionText.setEditable(false);
    }

    private void setup() {
        final var l = new GroupLayout(panel);
        panel.setLayout(l);
        l.setAutoCreateContainerGaps(true);
        l.setAutoCreateGaps(true);

        l.setHorizontalGroup(l.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(titleText)
            .addComponent(descriptionText)
            .addComponent(cautionText)
            .addComponent(lifelinePanel)
            .addGroup(l.createSequentialGroup()
                .addComponent(nevermindButton)
                .addComponent(startButton)));

        l.setVerticalGroup(l.createSequentialGroup()
            .addComponent(titleText)
            .addGap(16, 20, 24)
            .addComponent(descriptionText)
            .addGap(4)
            .addComponent(cautionText)
            .addGap(16, 20, 24)
            .addComponent(lifelinePanel)
            .addGroup(l.createBaselineGroup(true, false)
                .addComponent(nevermindButton)
                .addComponent(startButton)));
    }

    private void setupActions() {
        nevermindButton.addActionListener(e -> MainFrame.getInstance().back());
        startButton.addActionListener(e -> {
            if (lifelinePanel.getQueue()
                    .size() < LifelineFeature.PARTNERS_COUNT) {
                Dialogs.error("You must choose 4 partners.");
                return;
            }

            GameFrame.getInstance().setPartners(lifelinePanel.getQueue());
            MainFrame.getInstance().replace(GameFrame.getInstance());
        });
    }

    public static QuizFrame getInstance() {
        return instance == null ? instance = new QuizFrame() : instance;
    }

    @Override
    public JPanel getOverridingPane() {
        return panel;
    }

}
