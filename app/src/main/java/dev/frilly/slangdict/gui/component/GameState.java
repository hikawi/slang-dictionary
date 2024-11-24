package dev.frilly.slangdict.gui.component;

import com.google.common.util.concurrent.AtomicDouble;
import dev.frilly.slangdict.events.ComboChangeEvent;
import dev.frilly.slangdict.events.DamageEvent;
import dev.frilly.slangdict.events.EventManager;
import dev.frilly.slangdict.events.ScoreGainEvent;
import dev.frilly.slangdict.gui.GameFrame;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents the quiz's game state.
 */
public final class GameState {

    private final static int          maxHp           = 5000;
    private final        AtomicDouble score           = new AtomicDouble();
    private final        AtomicLong   seconds         = new AtomicLong();
    private final        AtomicLong   combo           = new AtomicLong();
    private final        AtomicDouble hp              = new AtomicDouble();
    private final        AtomicDouble comboMultiplier = new AtomicDouble();

    private String correctAnswer;

    /**
     * Resets the game state.
     */
    public void reset() {
        correctAnswer = "";
        score.set(0);
        seconds.set(20);
        combo.set(0);
        hp.set(maxHp);
        comboMultiplier.set(1D);
    }

    /**
     * Sets the correct answer.
     *
     * @param correctAnswer The correct answer
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Gets the current combo.
     *
     * @return The combo value.
     */
    public long getCombo() {
        return combo.get();
    }

    /**
     * Gets the current combo multiplier.
     *
     * @return The combo multiplier.
     */
    public double getComboMultiplier() {
        return comboMultiplier.get();
    }

    /**
     * Sets the combo multiplier.
     *
     * @param comboMultiplier
     */
    public void setComboMultiplier(double comboMultiplier) {
        this.comboMultiplier.set(comboMultiplier);
    }

    /**
     * Retrieves the current score.
     *
     * @return The score.
     */
    public double getScore() {
        return score.get();
    }

    /**
     * Sets the score.
     *
     * @param score the score to set to
     */
    public void setScore(final double score) {
        this.score.set(score);
    }

    /**
     * Gets the current HP.
     *
     * @return the current HP.
     */
    public double getHp() {
        return hp.get();
    }

    /**
     * Sets the HP.
     *
     * @param hp the hp.
     */
    public void setHp(double hp) {
        this.hp.set(Math.min(hp, maxHp));
    }

    /**
     * Retrieves the max HP.
     *
     * @return the max HP.
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Retrieves the seconds left.
     *
     * @return Time left
     */
    public long getSeconds() {
        return seconds.get();
    }

    /**
     * Sets the seconds left.
     *
     * @param seconds The seconds left
     */
    public void setSeconds(long seconds) {
        this.seconds.set(seconds);
    }

    /**
     * Enacts on an answer.
     *
     * @param answer The answer
     */
    public void actAnswer(final String answer) {
        if (checkAnswer(answer)) {
            actCorrectAnswer();
        } else {
            actIncorrectAnswer();
        }
        GameFrame.getInstance().updateDisplay();
        GameFrame.getInstance().randomQuiz();
        seconds.set(20);
    }

    /**
     * Checks if the provided answer is correct.
     *
     * @param answer The answer
     *
     * @return True if it matched correct answer.
     */
    public boolean checkAnswer(String answer) {
        return answer.equalsIgnoreCase(this.correctAnswer);
    }

    /**
     * Enacts an action when the answer is correct.
     */
    public void actCorrectAnswer() {
        final var comboEvent = new ComboChangeEvent(combo.get(),
                                                    combo.get() + 1);
        EventManager.dispatchEvent(comboEvent);
        if (!comboEvent.isCancelled()) {
            combo.set(comboEvent.getNewCombo());
            comboMultiplier.set(Math.max(
                1.0 + ((Math.pow(combo.get(), 2) - Math.pow(combo.get() - 1,
                                                            2)) / 100.0), 1.0));
        }

        final var event = new ScoreGainEvent(score.get(),
                                             100 * comboMultiplier.get());
        EventManager.dispatchEvent(event);
        if (!event.isCancelled()) {
            score.getAndAdd(event.getGain());
        }
    }

    /**
     * Enacts an action when the answer is incorrect.
     */
    public void actIncorrectAnswer() {
        final var comboEvent = new ComboChangeEvent(combo.get(), 0);
        EventManager.dispatchEvent(comboEvent);
        if (!comboEvent.isCancelled()) {
            combo.set(comboEvent.getNewCombo());
            comboMultiplier.set(1.0);
        }

        final var scoreEvent = new ScoreGainEvent(score.get(), -100);
        EventManager.dispatchEvent(scoreEvent);
        if (!scoreEvent.isCancelled()) {
            score.addAndGet(scoreEvent.getGain());
        }

        final var damageEvent = new DamageEvent(hp.get(),
                                                ThreadLocalRandom.current()
                                                    .nextDouble(maxHp / 10),
                                                DamageEvent.DamageReason.INCORRECT);
        EventManager.dispatchEvent(damageEvent);
        if (!damageEvent.isCancelled()) {
            hp.addAndGet(-damageEvent.getDamage());
        }
    }

}
