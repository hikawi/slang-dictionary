package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.Dictionary;

/**
 * Implementation for the Reload Feature.
 * 
 * Just reloads the database.
 */
public final class ReloadFeature implements Runnable {

    @Override
    public void run() {
        Dictionary.getInstance().load();
    }

}
