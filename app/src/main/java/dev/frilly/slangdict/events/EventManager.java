package dev.frilly.slangdict.events;

import dev.frilly.slangdict.interfaces.EventHandler;
import dev.frilly.slangdict.interfaces.Listener;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The events caller and managers.
 */
public final class EventManager {

    private static final Map<Class<? extends Event>, SortedSet<EventExecutor>> handlers = new HashMap<>();

    private static void addListener(final Class<? extends Event> cls, final EventPriority priority, final MethodHandle handler) {
        if (!handlers.containsKey(cls))
            handlers.put(cls, new TreeSet<>());
        handlers.get(cls).add(new EventExecutor(priority, handler));
    }

    /**
     * Registers a listener
     *
     * @param listener The listener.
     */
    @SuppressWarnings("unchecked")
    public static void registerListener(final Listener listener) {
        try {
            final var lookup = MethodHandles.lookup();
            outer:
            for (final var method : listener.getClass().getDeclaredMethods()) {
                // Skip bad methods (different params count, different return type, non-member methods).
                if (method.getParameterCount() != 1 || method.getReturnType() != void.class
                    || Modifier.isStatic(method.getModifiers()) || !method.isAnnotationPresent(EventHandler.class))
                    continue;

                var parent = method.getParameterTypes()[0];
                final var anno = method.getAnnotation(EventHandler.class);

                while (true) {
                    // Reach highest level of superclass but didn't get an Event instance.
                    if (parent == null)
                        continue outer;

                    // This event has Event as an ancestor.
                    if (parent == Event.class)
                        break;

                    parent = method.getParameterTypes()[0].getSuperclass();
                }

                // Obtain a handle to invoke when the listener caught an event.
                method.setAccessible(true);
                final var ev = (Class<? extends Event>) method.getParameterTypes()[0];
                final var handle = lookup.unreflect(method).bindTo(listener);
                addListener(ev, anno.priority(), handle);
            }
        } catch (final Exception e) {
            e.fillInStackTrace();
        }
    }

    /**
     * Dispatches the event and calls all handlers.
     *
     * @param event The event
     * @param <T>   The event type
     */
    public static <T extends Event> void dispatchEvent(final T event) {
        final var set = handlers.get(event.getClass());
        if (set == null || set.isEmpty()) return;

        for (final var handler : set)
            handler.execute(event);
    }

    private static class EventExecutor implements Comparable<EventExecutor> {

        private final EventPriority priority;
        private final MethodHandle methodHandle;

        public EventExecutor(EventPriority priority, MethodHandle handle) {
            this.priority = priority;
            this.methodHandle = handle;
        }

        @Override
        public int compareTo(EventExecutor o) {
            return priority.compareTo(o.priority);
        }

        public <T extends Event> void execute(final T event) {
            try {
                methodHandle.invoke(event);
            } catch (final Throwable e) {
                e.printStackTrace();
                System.err.printf("Unable to invoke event handler: %s\n", methodHandle.toString());
            }
        }

    }

}
