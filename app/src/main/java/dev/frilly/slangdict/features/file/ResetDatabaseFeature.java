package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.gui.MainFrame;
import dev.frilly.slangdict.gui.ResettingFrame;

/**
 * The implementation for the "reset database to default" feature.
 */
public final class ResetDatabaseFeature implements Runnable {

    @Override
    public void run() {
        MainFrame.getInstance().override(ResettingFrame.getInstance());
    }

}
