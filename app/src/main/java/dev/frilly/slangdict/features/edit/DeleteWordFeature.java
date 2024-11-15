package dev.frilly.slangdict.features.edit;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.gui.ViewFrame;

import javax.swing.*;

/**
 * Implementation for the "delete word(s)" feature.
 * <p>
 * Delete all selected words in a table.
 */
public final class DeleteWordFeature implements Runnable {

    public DeleteWordFeature() {
    }

    @Override
    public void run() {
        final var words = ViewFrame.getInstance().getSelectedWords().toList();

        // Can't delete favorites
        if (words.stream().anyMatch(w -> w.favorite)) {
            Dialogs.error("You can't delete favorite words. Deselect them before deleting.");
            return;
        }

        words.stream().map(w -> w.word).forEach(Dictionary.getInstance()::deleteWord);
        ViewFrame.getInstance().query();
        Dialogs.info("Deleted %d word%s.", words.size(), words.size() != 1 ? "s" : "");
    }

}
