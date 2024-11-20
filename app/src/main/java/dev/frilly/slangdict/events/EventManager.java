package dev.frilly.slangdict.gui.events;

import dev.frilly.slangdict.interfaces.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * The events caller and managers.
 */
public final class EventManager {

    private static final List<Listener> listeners = new ArrayList<>();

    public static void registerListener(final Listener listener) {
        listeners.add(listener);
    }

}
