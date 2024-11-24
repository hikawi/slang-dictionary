package dev.frilly.slangdict.listener;

import dev.frilly.slangdict.events.*;
import dev.frilly.slangdict.gui.GameFrame;
import dev.frilly.slangdict.gui.GameOverFrame;
import dev.frilly.slangdict.gui.MainFrame;
import dev.frilly.slangdict.interfaces.EventHandler;
import dev.frilly.slangdict.interfaces.Listener;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The listener that handles quiz events.
 */
@SuppressWarnings("unused")
public final class QuizEventsListener implements Listener {

    public static final AtomicBoolean IMMUNE_CLOCK     = new AtomicBoolean();
    public static final AtomicBoolean IMMUNE_INCORRECT = new AtomicBoolean();

    public static final AtomicBoolean NOAH_ACTIVE  = new AtomicBoolean();
    public static final AtomicBoolean MIO_ACTIVE   = new AtomicBoolean();
    public static final AtomicInteger TAION_ACTIVE = new AtomicInteger();
    public static final AtomicBoolean SENA_ACTIVE  = new AtomicBoolean();
    public static final AtomicBoolean LANZ_ACTIVE  = new AtomicBoolean();

    public static final AtomicBoolean NIKOL_ACTIVE   = new AtomicBoolean();
    public static final AtomicInteger GLIMMER_ACTIVE = new AtomicInteger();
    public static final AtomicBoolean REX_ACTIVE     = new AtomicBoolean();
    public static final AtomicBoolean SHULK_ACTIVE   = new AtomicBoolean();

    public QuizEventsListener() {
        EventManager.registerListener(this);
    }

    /**
     * Resets all volatile variables.
     */
    public static void reset() {
        IMMUNE_CLOCK.set(false);
        IMMUNE_INCORRECT.set(false);

        NOAH_ACTIVE.set(false);
        MIO_ACTIVE.set(false);
        TAION_ACTIVE.set(0);
        SENA_ACTIVE.set(false);
        LANZ_ACTIVE.set(false);
        NIKOL_ACTIVE.set(false);
        GLIMMER_ACTIVE.set(0);
        REX_ACTIVE.set(false);
        SHULK_ACTIVE.set(false);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onComboChange(final ComboChangeEvent event) {
        if (NIKOL_ACTIVE.get() && event.getNewCombo() > event.getOldCombo()) {
            event.setNewCombo(0);
        }
        if (NOAH_ACTIVE.get() && event.getNewCombo() < event.getOldCombo()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFirstDamage(final DamageEvent event) {
        if ((IMMUNE_CLOCK.get() && event.getDamageReason() == DamageEvent.DamageReason.CLOCK) || (IMMUNE_INCORRECT.get() && event.getDamageReason() == DamageEvent.DamageReason.INCORRECT)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MEDIUM)
    public void onDamageMonitor(final DamageEvent event) {
        final var state = GameFrame.getInstance().getState();
        if (TAION_ACTIVE.get() > 0 && event.getDamageReason() == DamageEvent.DamageReason.INCORRECT) {
            TAION_ACTIVE.decrementAndGet();
            event.setCancelled(true);
        }

        if (LANZ_ACTIVE.get() && !event.isCancelled()) {
            event.setDamage(event.getDamage() * 0.3);
        }

        if (NIKOL_ACTIVE.get() && !event.isCancelled()) {
            event.setDamage(event.getDamage() * 0.5);
        }

        if (MIO_ACTIVE.get() && !event.isCancelled()) {
            state.setScore(event.getDamage() * 0.3 + state.getScore());
        }

        GameFrame.getInstance().updateDisplay();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFatalDamage(final DamageEvent event) {
        if (SHULK_ACTIVE.get()) {
            event.setCancelled(true);
            final var state = GameFrame.getInstance().getState();
            state.setHp(state.getHp() + state.getMaxHp() * 0.1);
            GameFrame.getInstance().updateDisplay();
            SHULK_ACTIVE.set(false);
        }

        if (event.isCancelled()) {
            return;
        }

        if (event.getDamage() >= event.getCurrentHp()) {
            GameFrame.getInstance().stop();
            MainFrame.getInstance().replace(GameOverFrame.getInstance());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onScoreGain(final ScoreGainEvent event) {
        if (SENA_ACTIVE.get()) {
            event.setGain(event.getGain() * 4);
            SENA_ACTIVE.set(false);
        }

        if (LANZ_ACTIVE.get()) {
            event.setGain(event.getGain() * 0.5);
        }

        if (GLIMMER_ACTIVE.get() > 0) {
            event.setGain(event.getGain() * 1.5);
            GLIMMER_ACTIVE.decrementAndGet();
        }

        if (REX_ACTIVE.get()) {
            event.setGain(event.getGain() * 10);
            REX_ACTIVE.set(false);
        }
    }

}
