package dev.frilly.slangdict.listener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import dev.frilly.slangdict.Application;

/**
 * A simple listener to call {@link Application#saveData} when the frame is
 * closed.
 */
public class FrameCloseListener implements WindowListener {

    public FrameCloseListener() {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("Closing...");
        Application.getInstance().saveData();
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
