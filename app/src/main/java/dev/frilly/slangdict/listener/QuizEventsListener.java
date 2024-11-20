package dev.frilly.slangdict.listener;

import dev.frilly.slangdict.events.DamageEvent;
import dev.frilly.slangdict.events.EventManager;
import dev.frilly.slangdict.events.EventPriority;
import dev.frilly.slangdict.gui.GameFrame;
import dev.frilly.slangdict.gui.GameOverFrame;
import dev.frilly.slangdict.gui.MainFrame;
import dev.frilly.slangdict.interfaces.EventHandler;
import dev.frilly.slangdict.interfaces.Listener;

/**
 * The listener that handles quiz events.
 */
public final class QuizEventsListener implements Listener {

    public QuizEventsListener() {
        EventManager.registerListener(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFatalDamage(final DamageEvent event) {
        if (event.isCancelled())
            return;

        if (event.getDamage() >= event.getCurrentHp()) {
            GameFrame.getInstance().stop();
            MainFrame.getInstance().replace(GameOverFrame.getInstance());
        }
    }

}
