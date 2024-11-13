package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.Dictionary;

/**
 * Implementation for the file saving feature.
 * 
 * If the dictionary isn't set, this just does nothing.
 */
public final class SaveFeature implements Runnable {

    @Override
    public void run() {
        Dictionary.getInstance().save();
    }

}
