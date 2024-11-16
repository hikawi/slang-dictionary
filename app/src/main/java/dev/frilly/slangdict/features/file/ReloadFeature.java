package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.gui.ViewFrame;

/**
 * Implementation for the Reload Feature.
 * 
 * Just reloads the database.
 */
public final class ReloadFeature implements Runnable {

    @Override
    public void run() {
        Dictionary.getInstance().load();
//        ViewFrame.getInstance().query();
    }

}
