package dev.frilly.slangdict.gui.component;

import dev.frilly.slangdict.events.DamageEvent;
import dev.frilly.slangdict.events.EventManager;
import dev.frilly.slangdict.gui.GameFrame;

import javax.swing.*;
import java.util.List;

/**
 * The swing worker thread that handles reading and displaying values
 * on screen.
 */
public final class GameWorker extends SwingWorker<Void, Long> {

    private final GameState state;

    public GameWorker(GameState state) {
        this.state = state;
    }

    @Override
    protected Void doInBackground() {
        try {
            while (true) {
                publish(state.getSeconds() - 1);
                Thread.sleep(1000);

                if (state.getSeconds() == 0) {
                    return null;
                }
            }
        } catch (final InterruptedException ex) {
            return null;
        }
    }

    @Override
    protected void process(List<Long> chunks) {
        state.setSeconds(chunks.get(0));

        final var damage = new DamageEvent(state.getHp(),
                                           state.getMaxHp() / 100.0,
                                           DamageEvent.DamageReason.CLOCK);
        EventManager.dispatchEvent(damage);
        if (!damage.isCancelled()) {
            state.setHp(state.getHp() - damage.getDamage());
        }
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    protected void done() {
        // When time is over, deal 50% of damage to user.
        final var damage = new DamageEvent(state.getHp(),
                                           state.getMaxHp() / 2.0,
                                           DamageEvent.DamageReason.CLOCK);
        EventManager.dispatchEvent(damage);
        if (!damage.isCancelled()) {
            state.setHp(state.getHp() - damage.getDamage());
        }

        GameFrame.getInstance().randomQuiz();
        GameFrame.getInstance().updateDisplay();
    }

}
