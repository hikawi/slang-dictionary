package dev.frilly.slangdict.interfaces;

/**
 * An interface for cancellable events.
 */
public interface Cancellable {

    void setCancelled(boolean cancelled);
    boolean isCancelled();

}
