package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.interfaces.Overrideable;

import javax.swing.*;

/**
 * The frame that shows your search history.
 */
public final class HistoryFrame implements Overrideable {

    private static HistoryFrame instance;

    public static HistoryFrame getInstance() {
        return instance == null ? instance = new HistoryFrame() : instance;
    }

    private final JPanel panel = new JPanel();

    private final JLabel searchHistory = new JLabel("Your Search History");

    @Override
    public JPanel getOverridingPane() {
        return null;
    }

}
