package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.gui.newdb.CreationFrame;

/**
 * Implementation for the "New Database" feature.
 */
public final class NewDatabaseFeature implements Runnable {

    @Override
    public void run() {
        CreationFrame.getInstance().takeControl();
    }

}
