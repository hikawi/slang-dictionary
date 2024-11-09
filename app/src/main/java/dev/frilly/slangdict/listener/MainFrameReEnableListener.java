package dev.frilly.slangdict.listener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import dev.frilly.slangdict.Application;

/**
 * Re-enable the main frame when this window closes.
 */
public final class MainFrameReEnableListener implements WindowListener {

    @Override
    public void windowOpened(WindowEvent e) {
        Application.getInstance().getFrame().setEnabled(false);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Application.getInstance().getFrame().setEnabled(true);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

}
