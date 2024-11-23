package dev.frilly.slangdict.listener;

import dev.frilly.slangdict.events.*;
import dev.frilly.slangdict.gui.GameFrame;
import dev.frilly.slangdict.gui.GameOverFrame;
import dev.frilly.slangdict.gui.MainFrame;
import dev.frilly.slangdict.interfaces.EventHandler;
import dev.frilly.slangdict.interfaces.Listener;

/**
 * The listener that handles quiz events.
 */
@SuppressWarnings("unused")
public final class QuizEventsListener implements Listener {

    public static volatile boolean IMMUNE_CLOCK = false;
    public static volatile boolean IMMUNE_INCORRECT = false;

    public static volatile boolean NOAH_ACTIVE = false;
    public static volatile boolean MIO_ACTIVE = false;
    public static volatile int TAION_ACTIVE = 0;
    public static volatile boolean SENA_ACTIVE = false;
    public static volatile boolean LANZ_ACTIVE = false;

    public static volatile boolean NIKOL_ACTIVE = false;
    public static volatile int GLIMMER_ACTIVE = 0;
    public static volatile boolean REX_ACTIVE = false;
    public static volatile boolean SHULK_ACTIVE = false;

    public QuizEventsListener() {
        EventManager.registerListener(this);
    }

    /**
     * Resets all volatile variables.
     */
    public static void reset() {
        IMMUNE_CLOCK = false;
        IMMUNE_INCORRECT = false;

        NOAH_ACTIVE = false;
        MIO_ACTIVE = false;
        TAION_ACTIVE = 0;
        SENA_ACTIVE = false;
        LANZ_ACTIVE = false;
        NIKOL_ACTIVE = false;
        GLIMMER_ACTIVE = 0;
        REX_ACTIVE = false;
        SHULK_ACTIVE = false;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onComboChange(final ComboChangeEvent event) {
        if(NIKOL_ACTIVE && event.getNewCombo() > event.getOldCombo()) {
            event.setNewCombo(0);
        }
        if(NOAH_ACTIVE && event.getNewCombo() < event.getOldCombo()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFirstDamage(final DamageEvent event) {
        if((IMMUNE_CLOCK && event.getDamageReason() == DamageEvent.DamageReason.CLOCK)
        || (IMMUNE_INCORRECT && event.getDamageReason() == DamageEvent.DamageReason.INCORRECT))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MEDIUM)
    public void onDamageMonitor(final DamageEvent event) {
        final var state = GameFrame.getInstance().getState();
        if(TAION_ACTIVE > 0 && event.getDamageReason() == DamageEvent.DamageReason.INCORRECT) {
            TAION_ACTIVE--;
            event.setCancelled(true);
        }

        if(LANZ_ACTIVE && !event.isCancelled()) {
            event.setDamage(event.getDamage() * 0.3);
        }

        if(NIKOL_ACTIVE && !event.isCancelled()) {
            event.setDamage(event.getDamage() * 0.5);
        }

        if(MIO_ACTIVE && !event.isCancelled()) {
            state.setScore(event.getDamage() * 0.3 + state.getScore());
        }

        GameFrame.getInstance().updateDisplay();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFatalDamage(final DamageEvent event) {
        if(SHULK_ACTIVE) {
            event.setCancelled(true);
            final var state = GameFrame.getInstance().getState();
            state.setHp(state.getHp() + state.getMaxHp() * 0.1);
            GameFrame.getInstance().updateDisplay();
            SHULK_ACTIVE = false;
        }

        if (event.isCancelled())
            return;

        if (event.getDamage() >= event.getCurrentHp()) {
            GameFrame.getInstance().stop();
            MainFrame.getInstance().replace(GameOverFrame.getInstance());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onScoreGain(final ScoreGainEvent event) {
        if(SENA_ACTIVE) {
            event.setGain(event.getGain() * 4);
            SENA_ACTIVE = false;
        }

        if(LANZ_ACTIVE) {
            event.setGain(event.getGain() * 0.5);
        }

        if(GLIMMER_ACTIVE > 0) {
            event.setGain(event.getGain() * 1.5);
            GLIMMER_ACTIVE--;
        }

        if(REX_ACTIVE) {
            event.setGain(event.getGain() * 10);
            REX_ACTIVE = false;
        }
    }

}
