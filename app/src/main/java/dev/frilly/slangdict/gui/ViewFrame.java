package dev.frilly.slangdict.gui;

/**
 * Implementation of the probably most used frame of this application.
 * 
 * This is where the dictionary is shown, along with options and tools
 * to manage said dictionary.
 */
public final class ViewFrame {

    private static ViewFrame instance;

    private ViewFrame() {
    }

    public static ViewFrame getInstance() {
        return switch (instance) {
            case null -> instance = new ViewFrame();
            default -> instance;
        };
    }

}
