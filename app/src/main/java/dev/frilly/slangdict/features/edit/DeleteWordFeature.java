package dev.frilly.slangdict.features.edit;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.gui.MainFrame;

import java.util.List;

/**
 * Implementation for the "delete word(s)" feature.
 * <p>
 * Delete all selected words in a table.
 */
public final class DeleteWordFeature implements Runnable {

    private final List<String> choices;

    public DeleteWordFeature(final List<String> choices) {
        this.choices = choices;
    }

    @Override
    public void run() {
        // Can't delete favorites
        if (choices.stream()
            .map(Dictionary.getInstance()::getWord)
            .anyMatch(w -> w.favorite)) {
            Dialogs.error(
                "You can't delete favorite words. Deselect them before deleting.");
            return;
        }

        // Well if it's empty somehow...
        if (choices.isEmpty()) {
            Dialogs.error("You can't delete nothing...");
            return;
        }

        choices.forEach(Dictionary.getInstance()::deleteWord);
        Dialogs.info("Deleted %d word%s.", choices.size(),
                     choices.size() == 1 ? "" : "s");
        MainFrame.getInstance().back();
    }

}
