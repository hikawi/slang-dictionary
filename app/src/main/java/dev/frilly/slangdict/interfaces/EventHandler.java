package dev.frilly.slangdict.interfaces;

import dev.frilly.slangdict.events.Event;
import dev.frilly.slangdict.events.EventPriority;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation marked a method as an event handler.
 * <p>
 * An event handler must be a public method and take 1 parameter of type
 * {@link Event}.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    EventPriority priority() default EventPriority.MEDIUM;

}
