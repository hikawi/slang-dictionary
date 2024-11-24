package dev.frilly.slangdict.interfaces;

/**
 * An interface for cancellable events.
 */
public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

}
