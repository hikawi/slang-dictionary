package dev.frilly.slangdict;

import javax.swing.*;

/**
 * Simple utilities class.
 */
public final class Utilities {

    private Utilities() {
    }

    /**
     * Creates a GroupLayout instance bound to the provided panel.
     *
     * @param panel The panel
     * @return The GroupLayout
     */
    public static GroupLayout group(final JPanel panel) {
        final var l = new GroupLayout(panel);
        panel.setLayout(l);
        l.setAutoCreateGaps(true);
        l.setAutoCreateContainerGaps(true);
        return l;
    }

}
