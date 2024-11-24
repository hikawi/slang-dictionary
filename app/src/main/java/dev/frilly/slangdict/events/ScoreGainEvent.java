package dev.frilly.slangdict.events;

import dev.frilly.slangdict.interfaces.Cancellable;

/**
 * An event called when the quiz's score changed. If the gain is negative,
 * you're losing score.
 */
public final class ScoreGainEvent extends Event implements Cancellable {

    private final double currentScore;
    private boolean cancelled = false;
    private       double gain;

    public ScoreGainEvent(double currentScore, double gain) {
        this.currentScore = currentScore;
        this.gain         = gain;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    public double getCurrentScore() {
        return currentScore;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

}
