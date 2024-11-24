package dev.frilly.slangdict.features.quiz;

import dev.frilly.slangdict.gui.GameFrame;
import dev.frilly.slangdict.gui.component.QuizLifelinePanel;
import dev.frilly.slangdict.listener.QuizEventsListener;

import java.util.concurrent.CompletableFuture;

/**
 * Implementation for activating a partner's Lifeline effect.
 */
public final class LifelineFeature implements Runnable {

    public static final int                       PARTNERS_COUNT = 4;
    private final       QuizLifelinePanel.Partner partner;

    public LifelineFeature(final QuizLifelinePanel.Partner partner) {
        this.partner = partner;
    }

    @Override
    public void run() {
        switch (partner) {
            case NOAH -> runNoah();
            case MIO -> runMio();
            case EUNIE -> runEunie();
            case TAION -> runTaion();
            case SENA -> runSena();
            case LANZ -> runLanz();
            case MATTHEW -> runMatthew();
            case A -> runA();
            case NIKOL -> runNikol();
            case GLIMMER -> runGlimmer();
            case REX -> runRex();
            case SHULK -> runShulk();
        }
    }

    // Noah grants combo and damage immunity for 10s.
    private void runNoah() {
        QuizEventsListener.IMMUNE_INCORRECT.set(true);
        QuizEventsListener.IMMUNE_CLOCK.set(true);
        QuizEventsListener.NOAH_ACTIVE.set(true);
        doAfter(10000, () -> {
            QuizEventsListener.IMMUNE_INCORRECT.set(false);
            QuizEventsListener.IMMUNE_CLOCK.set(false);
        });
    }

    // Mio converts 30% damage into raw points.
    private void runMio() {
        QuizEventsListener.MIO_ACTIVE.set(true);
    }

    // Eunie heals 20% of your max HP.
    private void runEunie() {
        final var state = GameFrame.getInstance().getState();
        state.setHp(state.getHp() + state.getMaxHp() * 0.2);
        GameFrame.getInstance().updateDisplay();
    }

    // Taion reduces the next 3 incorrect answers by 50%.
    private void runTaion() {
        QuizEventsListener.TAION_ACTIVE.set(3);
    }

    // Sena quadruples the next score gain.
    private void runSena() {
        QuizEventsListener.SENA_ACTIVE.set(true);
    }

    // Lanz reduces score gain by half for the rest of the game, but grants
    // defense against clock and incorrect damages.
    private void runLanz() {
        QuizEventsListener.LANZ_ACTIVE.set(true);
    }

    // Matthew increases Combo Multiplier by 1.5.
    private void runMatthew() {
        final var state = GameFrame.getInstance().getState();
        state.setComboMultiplier(state.getComboMultiplier() + 1.5);
        GameFrame.getInstance().updateDisplay();
    }

    // A (Ei) heals 5% of Max HP and blocks damage taken for the next 10s.
    private void runA() {
        final var state = GameFrame.getInstance().getState();
        state.setHp(state.getHp() + state.getMaxHp() * 0.05);
        GameFrame.getInstance().updateDisplay();

        QuizEventsListener.IMMUNE_INCORRECT.set(true);
        QuizEventsListener.IMMUNE_CLOCK.set(true);
        doAfter(10000, () -> {
            QuizEventsListener.IMMUNE_INCORRECT.set(false);
            QuizEventsListener.IMMUNE_CLOCK.set(false);
        });
    }

    // Nikol reduces damage and clock ticks like Lanz, but combo stack is now
    // disabled.
    private void runNikol() {
        final var state = GameFrame.getInstance().getState();
        state.setComboMultiplier(1.0);
        GameFrame.getInstance().updateDisplay();
        QuizEventsListener.NIKOL_ACTIVE.set(true);
    }

    // Glimmer heals 10% of Max HP and increases score gain for the next 3
    // questions.
    private void runGlimmer() {
        final var state = GameFrame.getInstance().getState();
        state.setHp(state.getHp() + state.getMaxHp() * 0.1);
        GameFrame.getInstance().updateDisplay();
        QuizEventsListener.GLIMMER_ACTIVE.set(3);
    }

    // Rex deals damage to self and next score gain takes 10x multiplier.
    private void runRex() {
        final var state = GameFrame.getInstance().getState();
        state.setHp(state.getHp() - state.getMaxHp() * 0.1);
        GameFrame.getInstance().updateDisplay();
        QuizEventsListener.REX_ACTIVE.set(true);
    }

    // Shulk prevents a fatal damage blow.
    private void runShulk() {
        QuizEventsListener.SHULK_ACTIVE.set(true);
    }

    private void doAfter(final long millis, final Runnable runnable) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            runnable.run();
        });
    }

}
