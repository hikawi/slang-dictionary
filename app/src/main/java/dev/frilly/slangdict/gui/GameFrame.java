package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.Word;
import dev.frilly.slangdict.events.DamageEvent;
import dev.frilly.slangdict.events.EventManager;
import dev.frilly.slangdict.events.ScoreGainEvent;
import dev.frilly.slangdict.gui.component.QuizLifelinePanel;
import dev.frilly.slangdict.interfaces.Overrideable;
import dev.frilly.slangdict.listener.QuizEventsListener;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
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
    private final List<JButton> partnerIcons = new ArrayList<>();
    private final List<JLabel> partnerNames = new ArrayList<>();

    // The quiz.
    private final JTextArea quizArea = new JTextArea();
    private final JPanel quizChoicesPanel = new JPanel(new GridLayout(2, 2, 8, 8));
    private final List<JButton> choices = IntStream.range(0, 4).mapToObj(i -> new JButton(String.valueOf(i))).toList();

    // Quiz Data.
    private String correctAnswer;
    private volatile double score = 0;
    private volatile long seconds = 20;
    private volatile long combo = 0;
    private volatile int maxHp = 5000;
    private volatile double hp = 0.0;
    private volatile double comboMultiplier = 1.0;

    private SwingWorker<Void, Long> gameWorker;

    private GameFrame() {
        init();
        setup();
        setupStyles();
        setupActions();

        new QuizEventsListener();
    }

    public static GameFrame getInstance() {
        return instance == null ? instance = new GameFrame() : instance;
    }

    private void updateDisplay() {
        final var format = new DecimalFormat("#,###");
        scoreValue.setText(format.format(score));
        hpValue.setText(format.format(hp));
        comboValue.setText("%d (%.2fx)".formatted(combo, comboMultiplier));
        hpBar.setValue((int) (hp * 100 / maxHp));
        clockValue.setText("00:%d".formatted(seconds));
    }

    private void init() {
        final var l = new GroupLayout(contentPane);
        contentPane.setLayout(l);
        l.setAutoCreateGaps(true);
        l.setAutoCreateContainerGaps(true);

        Stream.of(0, 2, 1, 3).forEach(i -> quizChoicesPanel.add(choices.get(i)));
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
        });
        scoreLabel.putClientProperty("FlatLaf.styleClass", "h4");
        comboLabel.putClientProperty("FlatLaf.styleClass", "h4");

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

        gameWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    while(true) {
                        publish(seconds - 1);
                        Thread.sleep(1000);

                        if (seconds == 0)
                            return null;
                    }
                } catch (final InterruptedException ex) {
                    return null;
                }
            }

            @Override
            protected void process(List<Long> chunks) {
                seconds = chunks.get(0);

                final var damage = new DamageEvent(hp, maxHp / 100.0);
                EventManager.dispatchEvent(damage);
                if (!damage.isCancelled())
                    hp -= damage.getDamage();
                updateDisplay();
            }

            @Override
            protected void done() {
                // When time is over, deal 50% of damage to user.
                final var damage = new DamageEvent(hp, maxHp / 2.0);
                EventManager.dispatchEvent(damage);
                if (!damage.isCancelled())
                    hp -= damage.getDamage();

                randomQuiz();
                updateDisplay();
            }
        };
        gameWorker.execute();
    }

    private void calculateComboMultiplier() {
        // Just a simple quadratic multiplier. 1 combo = 1.01x, 2 combo = 1.04x, 3 combo = 1.09x.
        final var additional = (combo * combo) / 100.0;
        comboMultiplier = 1.0 + additional;
    }

    private void actCorrectAnswer() {
        combo++;
        calculateComboMultiplier();

        final var event = new ScoreGainEvent(score, 100 * comboMultiplier);
        EventManager.dispatchEvent(event);
        if (!event.isCancelled())
            score += event.getGain();

        // Update display and randomize the quiz again.
        updateDisplay();
        randomQuiz();
    }

    private void actIncorrectAnswer() {
        combo = 0;
        calculateComboMultiplier();

        final var scoreEvent = new ScoreGainEvent(score, -100);
        EventManager.dispatchEvent(scoreEvent);
        if (!scoreEvent.isCancelled())
            score += scoreEvent.getGain();

        final var damageEvent = new DamageEvent(hp, maxHp / 10);
        EventManager.dispatchEvent(damageEvent);
        if (!damageEvent.isCancelled())
            hp -= damageEvent.getDamage();

        // Update display and randomize the quiz again.
        updateDisplay();
        randomQuiz();
    }

    private void setupActions() {
        choices.forEach(btn -> btn.addActionListener(e -> {
            if (btn.getText().equals(correctAnswer)) {
                actCorrectAnswer();
            } else {
                actIncorrectAnswer();
            }

            seconds = 20;
            updateDisplay();
        }));
    }

    private Word getRandomWord() {
        final var skipping = ThreadLocalRandom.current().nextInt(Dictionary.getInstance().getWords().size());
        return Dictionary.getInstance().getWords().values().stream().skip(skipping).findFirst().get();
    }

    private void randomQuiz() {
        final var question = getRandomWord();
        final var others = new ArrayList<Word>();

        // Make sure to add 3 other answers not the question.
        while (true) {
            if (others.size() == 3)
                break;

            final var next = getRandomWord();
            if (next == question || others.contains(next))
                continue;
            others.add(next);
        }

        if (ThreadLocalRandom.current().nextBoolean()) {
            // Ask word, answer definitions.
            quizArea.setText(question.word);

            final var answers = new ArrayList<String>();
            answers.add(question.definition);
            others.forEach(o -> answers.add(o.definition));
            Collections.shuffle(answers, ThreadLocalRandom.current());
            correctAnswer = question.definition;

            IntStream.range(0, 4).forEach(i -> choices.get(i).setText(answers.get(i)));
        } else {
            // Ask definition, answer words.
            quizArea.setText(question.definition);

            final var answers = new ArrayList<String>();
            answers.add(question.word);
            others.forEach(o -> answers.add(o.word));
            Collections.shuffle(answers, ThreadLocalRandom.current());
            correctAnswer = question.word;

            IntStream.range(0, 4).forEach(i -> choices.get(i).setText(answers.get(i)));
        }
    }

    /**
     * Sets new partners.
     *
     * @param partners The partners to set to.
     */
    public void setPartners(List<QuizLifelinePanel.Partner> partners) {
        this.partners.clear();
        this.partnerIcons.clear();
        this.partnerNames.clear();

        partners.forEach(p -> {
            this.partners.add(p);
            this.partnerIcons.add(new JButton(Application.getIcon("/images/%s.png".formatted(p.name().toLowerCase()), 64, 64)));
            this.partnerNames.add(new JLabel(p.name().substring(0, 1).toUpperCase() + p.name().substring(1).toLowerCase()));
        });

        setup();
    }

    @Override
    public JPanel getOverridingPane() {
        hp = maxHp;
        combo = 0;
        comboMultiplier = 1.0;
        score = 0.0;
        updateDisplay();
        randomQuiz();
        setupWorker();
        return outerPane;
    }

}
