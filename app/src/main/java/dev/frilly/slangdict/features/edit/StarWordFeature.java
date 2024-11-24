package dev.frilly.slangdict.features.edit;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;

import java.util.List;

/**
 * Implementation for the "Star all selection" feature.
 */
public final class StarWordFeature implements Runnable {

    private final List<String> choices;
    private final boolean      state;

    public StarWordFeature(final List<String> choices, final boolean state) {
        this.choices = choices;
        this.state   = state;
    }

    @Override
    public void run() {
        choices.stream()
            .map(Dictionary.getInstance()::getWord)
            .forEach(w -> w.favorite = state);
        if (state) {
            Dialogs.info(
                "Marked %d word%s as favorite. These can no longer be deleted.",
                choices.size(), choices.size() == 1 ? "" : "s");
        } else {
            Dialogs.info(
                "Unmarked %d word%s from favorite. These can now be deleted.",
                choices.size(), choices.size() == 1 ? "" : "s");
        }
    }

}
