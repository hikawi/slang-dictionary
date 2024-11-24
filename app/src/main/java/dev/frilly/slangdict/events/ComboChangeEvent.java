package dev.frilly.slangdict.events;

import dev.frilly.slangdict.interfaces.Cancellable;

/**
 * Event fired when the combo stack changes.
 */
public final class ComboChangeEvent extends Event implements Cancellable {

    private final long    oldCombo;
    private       boolean cancelled = false;
    private       long    newCombo;

    public ComboChangeEvent(long oldCombo, long newCombo) {
        this.oldCombo = oldCombo;
        this.newCombo = newCombo;
    }

    public long getOldCombo() {
        return oldCombo;
    }

    public long getNewCombo() {
        return newCombo;
    }

    public void setNewCombo(long newCombo) {
        this.newCombo = newCombo;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
