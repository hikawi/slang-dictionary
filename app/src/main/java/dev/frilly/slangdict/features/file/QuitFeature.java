package dev.frilly.slangdict.features.file;

import dev.frilly.slangdict.gui.MainFrame;

import java.awt.event.WindowEvent;

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
