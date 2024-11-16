package dev.frilly.slangdict.features.edit;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;

import java.util.List;

/**
 * Implementation for the "Lock word" feature.
 */
public final class LockWordFeature implements Runnable {

    private final List<String> choices;
    private final boolean state;

    public LockWordFeature(final List<String> choices, final boolean state) {
        this.choices = choices;
        this.state = state;
    }

    @Override
    public void run() {
        choices.stream().map(Dictionary.getInstance()::getWord).forEach(w -> w.locked = state);

        if (state) Dialogs.info("Locked %d word%s. These can no longer be edited until unlocked.", choices.size(), choices.size() == 1 ? "" : "s");
        else Dialogs.info("Unlocked %d word%s. These can now be edited.", choices.size(), choices.size() == 1 ? "" : "s");
    }

}
