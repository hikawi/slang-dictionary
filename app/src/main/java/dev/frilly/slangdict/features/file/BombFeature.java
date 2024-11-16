package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;

import javax.swing.*;

/**
 * Implementation for the "Bomb Database" feature.
 * 
 * This nukes every entry and clears everything.
 */
public final class BombFeature implements Runnable {

    @Override
    public void run() {
        final var opt = Dialogs.confirm("This will remove ALL entries of this database. Are you sure?");
        if(opt == JOptionPane.NO_OPTION)
            return;

        Dictionary.getInstance().bomb();
    }

}
