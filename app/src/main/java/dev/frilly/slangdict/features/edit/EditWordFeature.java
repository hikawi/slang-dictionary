package dev.frilly.slangdict.features.edit;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.gui.MainFrame;

/**
 * Implementation for the "Edit word" feature.
 */
public final class EditWordFeature implements Runnable {

    private final String word;
    private final String newWord;
    private final String newDefinition;

    public EditWordFeature(final String word, final String newWord, final String newDefinition) {
        this.word = word;
        this.newWord = newWord;
        this.newDefinition = newDefinition;
    }

    @Override
    public void run() {
        final var n = Dictionary.getInstance().getWord(newWord);
        if (n != null && !newWord.equals(word)) {
            Dialogs.error("That word \"%s\" already exists. Choose another to edit to.", newWord);
            return;
        }

        final var w = Dictionary.getInstance().getWord(word);
        Dictionary.getInstance().deleteWord(word);

        w.word = newWord;
        w.definition = newDefinition;
        Dictionary.getInstance().addWord(w);
        Dialogs.info("Edited word \"%s\".", word);
        MainFrame.getInstance().back();
    }

}
