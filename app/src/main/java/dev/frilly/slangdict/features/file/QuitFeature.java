package dev.frilly.slangdict.features.file;

import java.awt.event.WindowEvent;

import dev.frilly.slangdict.gui.MainFrame;

/**
 * Implementation for the quit program feature.
 */
public final class QuitFeature implements Runnable {

    @Override
    public void run() {
        final var frame = MainFrame.getInstance();
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

}
