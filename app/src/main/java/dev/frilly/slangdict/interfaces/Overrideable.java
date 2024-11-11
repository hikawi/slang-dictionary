package dev.frilly.slangdict.interfaces;

import javax.swing.JPanel;

/**
 * An interface for a frame that can override the Main Frame,
 * not a frame that CAN BE OVERRIDDEN.
 */
public interface Overrideable {

    public JPanel getOverridingPane();

}
