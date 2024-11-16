package dev.frilly.slangdict.features.edit;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.Word;
import dev.frilly.slangdict.gui.MainFrame;

import javax.swing.*;

/**
 * Implementation for the "Add Word" feature.
 */
public final class AddWordFeature implements Runnable {

    private final String word;
    private final String definition;
    private final boolean favorite;
    private final boolean locked;

    public AddWordFeature(String word, String definition, boolean favorite, boolean locked) {
        this.word = word;
        this.definition = definition;
        this.favorite = favorite;
        this.locked = locked;
    }

    @Override
    public void run() {
        if(Dictionary.getInstance().getWord(word) != null) {
            final var opt = Dialogs.confirm("The word \"%s\" already exists. Do you want to OVERWRITE?", word);
            if(opt == JOptionPane.NO_OPTION)
                return;
        }

        final var w = new Word();
        w.word = word;
        w.definition = definition;
        w.favorite = favorite;
        w.locked = locked;
        Dictionary.getInstance().addWord(w);
        Dialogs.info("Added word \"%s\"", word);
        MainFrame.getInstance().back();
    }

}
