package dev.frilly.slangdict.gui.component;

import dev.frilly.slangdict.events.ComboChangeEvent;
import dev.frilly.slangdict.events.DamageEvent;
import dev.frilly.slangdict.events.EventManager;
import dev.frilly.slangdict.events.ScoreGainEvent;
import dev.frilly.slangdict.gui.GameFrame;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the quiz's game state.
 */
public final class GameState {

    private final int maxHp = 5000;
    private String correctAnswer;

    private volatile double score = 0;
    private volatile long seconds = 20;
    private volatile long combo = 0;
    private volatile double hp = 0.0;
    private volatile double comboMultiplier = 1.0;

    /**
     * Resets the game state.
     */
    public void reset() {
        correctAnswer = "";
        score = 0;
        seconds = 20;
        combo = 0;
        hp = maxHp;
        comboMultiplier = 1.0;
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
     * Checks if the provided answer is correct.
     *
     * @param answer The answer
     * @return True if it matched correct answer.
     */
    public boolean checkAnswer(String answer) {
        return answer.equalsIgnoreCase(this.correctAnswer);
    }

    /**
     * Gets the current combo.
     *
     * @return The combo value.
     */
    public long getCombo() {
        return combo;
    }

    /**
     * Gets the current combo multiplier.
     *
     * @return The combo multiplier.
     */
    public double getComboMultiplier() {
        return comboMultiplier;
    }

    /**
     * Sets the combo multiplier.
     *
     * @param comboMultiplier
     */
    public void setComboMultiplier(double comboMultiplier) {
        this.comboMultiplier = comboMultiplier;
    }

    /**
     * Retrieves the current score.
     *
     * @return The score.
     */
    public double getScore() {
        return score;
    }

    /**
     * Sets the score.
     *
     * @param score the score to set to
     */
    public void setScore(final double score) {
        this.score = score;
    }

    /**
     * Gets the current HP.
     *
     * @return the current HP.
     */
    public double getHp() {
        return hp;
    }

    /**
     * Sets the HP.
     *
     * @param hp the hp.
     */
    public void setHp(double hp) {
        this.hp = Math.min(hp, maxHp);
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
        return seconds;
    }

    /**
     * Sets the seconds left.
     *
     * @param seconds The seconds left
     */
    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    /**
     * Enacts an action when the answer is correct.
     */
    public void actCorrectAnswer() {
        final var comboEvent = new ComboChangeEvent(combo, combo + 1);
        EventManager.dispatchEvent(comboEvent);
        if(!comboEvent.isCancelled()) {
            combo = comboEvent.getNewCombo();
            comboMultiplier = Math.max(1.0 + ((Math.pow(combo, 2) - Math.pow(combo - 1, 2)) / 100.0), 1.0);
        }

        final var event = new ScoreGainEvent(score, 100 * comboMultiplier);
        EventManager.dispatchEvent(event);
        if (!event.isCancelled())
            score += event.getGain();
    }

    /**
     * Enacts an action when the answer is incorrect.
     */
    public void actIncorrectAnswer() {
        final var comboEvent = new ComboChangeEvent(combo, 0);
        EventManager.dispatchEvent(comboEvent);
        if(!comboEvent.isCancelled()) {
            combo = comboEvent.getNewCombo();
            comboMultiplier = 1.0;
        }

        final var scoreEvent = new ScoreGainEvent(score, -100);
        EventManager.dispatchEvent(scoreEvent);
        if (!scoreEvent.isCancelled())
            score += scoreEvent.getGain();

        final var damageEvent = new DamageEvent(hp, ThreadLocalRandom.current().nextDouble(maxHp / 10), DamageEvent.DamageReason.INCORRECT);
        EventManager.dispatchEvent(damageEvent);
        if (!damageEvent.isCancelled())
            hp -= damageEvent.getDamage();
    }

    /**
     * Enacts on an answer.
     * @param answer The answer
     */
    public void actAnswer(final String answer) {
        if(checkAnswer(answer)) actCorrectAnswer(); else actIncorrectAnswer();
        GameFrame.getInstance().updateDisplay();
        GameFrame.getInstance().randomQuiz();
        seconds = 20;
    }

}
