package dev.frilly.slangdict.features.edit;

import dev.frilly.slangdict.gui.ViewFrame;

/**
 * Implementation for the "Star all selection" feature.
 */
public final class StarWordFeature implements Runnable {

    private final boolean state;

    public StarWordFeature(boolean state) {
        this.state = state;
    }

    @Override
    public void run() {
//        final var words = ViewFrame.getInstance().getSelectedWords();
//        words.forEach(w -> w.favorite = state);
    }

}