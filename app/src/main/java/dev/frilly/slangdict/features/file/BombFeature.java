package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.Dictionary;

/**
 * Implementation for the "Bomb Database" feature.
 * 
 * This nukes every entry and clears everything.
 */
public final class BombFeature implements Runnable {

    @Override
    public void run() {
        Dictionary.getInstance().bomb();
    }

}
