package dev.frilly.slangdict.features.edit;

import dev.frilly.slangdict.gui.ViewFrame;

/**
 * Implementation for the "Lock word" feature.
 */
public final class LockWordFeature implements Runnable {

    private final boolean state;

    public LockWordFeature(boolean state) {
        this.state = state;
    }

    @Override
    public void run() {
//        final var words = ViewFrame.getInstance().getSelectedWords();
//        words.forEach(w -> w.locked = state);
    }

}
