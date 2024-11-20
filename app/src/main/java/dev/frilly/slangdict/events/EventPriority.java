package dev.frilly.slangdict.events;

/**
 * Sets the priority of the event.
 * <p>
 * LOWEST will be called first, up until HIGHEST.
 */
public enum EventPriority {

    LOWEST,
    LOW,
    MEDIUM,
    HIGH,
    HIGHEST;

}
