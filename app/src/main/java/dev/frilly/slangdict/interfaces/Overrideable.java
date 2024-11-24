package dev.frilly.slangdict.interfaces;

import javax.swing.*;

/**
 * An interface for a frame that can override the Main Frame,
 * not a frame that CAN BE OVERRIDDEN.
 */
public interface Overrideable {

    JPanel getOverridingPane();

}
