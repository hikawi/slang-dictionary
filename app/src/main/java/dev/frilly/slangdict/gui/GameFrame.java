package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.Word;
import dev.frilly.slangdict.features.quiz.LifelineFeature;
import dev.frilly.slangdict.gui.component.GameState;
import dev.frilly.slangdict.gui.component.GameWorker;
import dev.frilly.slangdict.gui.component.QuizLifelinePanel;
import dev.frilly.slangdict.interfaces.Overrideable;
import dev.frilly.slangdict.listener.QuizEventsListener;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class GameFrame implements Overrideable {

    private static GameFrame instance;

    // Panels.
    private final JPanel outerPane = new JPanel(new BorderLayout());
    private final JPanel contentPane = new JPanel();

    // The status bar: score and combo stack.
    private final JLabel scoreLabel = new JLabel("Score: ");
    private final JLabel scoreValue = new JLabel("0");
    private final JLabel comboLabel = new JLabel("Combo: ");
    private final JLabel comboValue = new JLabel("0");

    // The HP bar, and the clock.
    private final JProgressBar hpBar = new JProgressBar();
    private final JLabel hpValue = new JLabel();
    private final JLabel clockValue = new JLabel();

    // The lifelines.
    private final List<QuizLifelinePanel.Partner> partners = new ArrayList<>();
    private final List<JButton> partnerIcons = List.of(new JButton(), new JButton(), new JButton(), new JButton());
    private final List<JLabel> partnerNames = List.of(new JLabel(), new JLabel(), new JLabel(), new JLabel());

    // The quiz.
    private final JTextArea quizArea = new JTextArea();
    private final JPanel quizChoicesPanel = new JPanel(new GridLayout(2, 2, 8, 8));
    private final List<JButton> choices = IntStream.range(0, 4).mapToObj(i -> new JButton(String.valueOf(i))).toList();

    private final GameState state = new GameState();
    private SwingWorker<Void, Long> gameWorker;

    private GameFrame() {
        final var l = new GroupLayout(contentPane);
        contentPane.setLayout(l);
        l.setAutoCreateGaps(true);
        l.setAutoCreateContainerGaps(true);
        Stream.of(0, 2, 1, 3).forEach(i -> quizChoicesPanel.add(choices.get(i)));

        setup();
        setupStyles();
        setupActions();

        new QuizEventsListener();
    }

    public static GameFrame getInstance() {
        return instance == null ? instance = new GameFrame() : instance;
    }

    /**
     * Retrieves the game state.
     *
     * @return The state.
     */
    public GameState getState() {
        return state;
    }

    /**
     * Updates the current display of the game frame.
     */
    public void updateDisplay() {
        final var format = new DecimalFormat("#,###");
        scoreValue.setText(format.format(state.getScore()));
        hpValue.setText(format.format(state.getHp()));
        comboValue.setText("%d (%.2fx)".formatted(state.getCombo(), state.getComboMultiplier()));
        hpBar.setValue((int) (state.getHp() * 100 / state.getMaxHp()));
        clockValue.setText("00:%02d".formatted(state.getSeconds()));
    }

    /**
     * Stops the game.
     */
    public void stop() {
        gameWorker.cancel(true);
    }

    private void setupStyles() {
        quizArea.putClientProperty("FlatLaf.styleClass", "h3");
        choices.forEach(c -> {
            c.putClientProperty("FlatLaf.styleClass", "large");
            c.putClientProperty("JButton.buttonType", "roundRect");
            c.setMaximumSize(new Dimension(600, 48));
        });
        scoreLabel.putClientProperty("FlatLaf.styleClass", "h4");
        comboLabel.putClientProperty("FlatLaf.styleClass", "h4");
        clockValue.putClientProperty("FlatLaf.styleClass", "h2");

        quizArea.setEditable(false);
        quizArea.setLineWrap(true);
        quizArea.setWrapStyleWord(true);
        quizArea.setOpaque(false);
        quizArea.setRows(2);
        quizArea.setMargin(new Insets(0, 0, 0, 0));
        quizArea.setPreferredSize(new Dimension(1200, 100));
    }

    private void createHStack() {
        final var l = (GroupLayout) contentPane.getLayout();

        final var hStack = l.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(l.createSequentialGroup()
                .addComponent(scoreLabel)
                .addComponent(scoreValue)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(comboLabel)
                .addComponent(comboValue))
            .addGroup(l.createSequentialGroup()
                .addComponent(hpBar)
                .addComponent(hpValue)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(clockValue));

        final var partnersRow = l.createSequentialGroup();
        partnerIcons.forEach(i -> partnersRow.addGroup(l.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(i).addComponent(partnerNames.get(partnerIcons.indexOf(i)))));

        l.setHorizontalGroup(hStack.addGroup(partnersRow).addComponent(quizArea).addComponent(quizChoicesPanel));
    }

    private void createVStack() {
        final var l = (GroupLayout) contentPane.getLayout();

        final var vStack = l.createSequentialGroup()
            .addGroup(l.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(scoreLabel)
                .addComponent(scoreValue)
                .addComponent(comboLabel)
                .addComponent(comboValue))
            .addGroup(l.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                .addComponent(hpBar)
                .addComponent(hpValue)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(clockValue));

        final var partnersRow = l.createParallelGroup(GroupLayout.Alignment.CENTER);
        partnerIcons.forEach(i -> partnersRow.addGroup(l.createSequentialGroup()
            .addComponent(i)
            .addGap(4)
            .addComponent(partnerNames.get(partnerIcons.indexOf(i)))));

        l.setVerticalGroup(vStack
            .addGap(24, 28, 32)
            .addGroup(partnersRow)
            .addGap(24, 28, 32)
            .addComponent(quizArea)
            .addGap(6, 8, 10)
            .addComponent(quizChoicesPanel));
    }

    private void setup() {
        createHStack();
        createVStack();
        outerPane.add(contentPane, BorderLayout.CENTER);
    }

    private void setupWorker() {
        if (gameWorker != null)
            gameWorker.cancel(true);

        gameWorker = new GameWorker(state);
        gameWorker.execute();
    }

    private void setupActions() {
        choices.forEach(btn -> btn.addActionListener(e -> state.actAnswer(btn.getText())));
        partnerIcons.forEach(p -> p.addActionListener(e -> {
            final var idx = partnerIcons.indexOf(p);
            p.setEnabled(false);
            new LifelineFeature(partners.get(idx)).run();
        }));
    }

    private List<Word> getRandomWord() {
        final var words = Dictionary.getInstance().getWords().values().stream().toList();
        final var indicies = new HashSet<Word>();
        while (indicies.size() < 4)
            indicies.add(words.get(ThreadLocalRandom.current().nextInt(words.size())));
        return new ArrayList<>(indicies);
    }

    /**
     * Requests a random quiz be put up the frame.
     */
    public void randomQuiz() {
        final var questions = getRandomWord();

        if (ThreadLocalRandom.current().nextBoolean()) {
            // Ask word, answer definitions.
            quizArea.setText(questions.get(0).word);
            state.setCorrectAnswer(questions.get(0).definition);
            Collections.shuffle(questions);
            IntStream.range(0, 4).forEach(i -> choices.get(i).setText(questions.get(i).definition));
        } else {
            // Ask definition, answer words.
            quizArea.setText(questions.get(0).definition);
            state.setCorrectAnswer(questions.get(0).word);
            Collections.shuffle(questions);
            IntStream.range(0, 4).forEach(i -> choices.get(i).setText(questions.get(i).word));
        }
    }

    /**
     * Sets new partners.
     *
     * @param partners The partners to set to.
     */
    public void setPartners(List<QuizLifelinePanel.Partner> partners) {
        this.partners.clear();
        for(int i = 0; i < partners.size(); i++) {
            final var p = partners.get(i);
            this.partners.add(p);
            this.partnerIcons.get(i).setIcon(Application.getIcon("/images/%s.png".formatted(p.name().toLowerCase()), 64, 64));
            this.partnerNames.get(i).setText(partners.get(i).name().substring(0, 1).toUpperCase() + p.name().substring(1).toLowerCase());
        }

        setup();
    }

    @Override
    public JPanel getOverridingPane() {
        state.reset();
        updateDisplay();
        randomQuiz();
        setupWorker();
        QuizEventsListener.reset();
        partnerIcons.forEach(c -> c.setEnabled(true));
        return outerPane;
    }

}
