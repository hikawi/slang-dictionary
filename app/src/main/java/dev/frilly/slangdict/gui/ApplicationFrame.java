package dev.frilly.slangdict.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dev.frilly.slangdict.listener.FrameCloseListener;

/**
 * The main frame of the program.
 */
public class ApplicationFrame extends JFrame {

    private final Map<String, JComponent> componentMap; // For easier retrieving components.

    public ApplicationFrame() {
        super("Slang Dictionary");
        this.componentMap = new HashMap<>();

        final var mainPane = new JPanel();
        final var layout = new BoxLayout(mainPane, BoxLayout.Y_AXIS);

        mainPane.setLayout(layout);
        mainPane.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        this.setContentPane(mainPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Sets up the frame and adds components.
     */
    public void setup() {
        final var translateButtons = new TranslateButtons();
        this.componentMap.put("translate-buttons", translateButtons);
        translateButtons.add(this);

        final var searchBox = new SearchBox();
        this.componentMap.put("search-box", searchBox);
        searchBox.add(this);

        final var autosavePane = new AutosavePane();
        this.componentMap.put("autosave", autosavePane);
        autosavePane.add(this);
    }

    /**
     * Sets up a saving data for the window close event.
     */
    public void setupCloseListener() {
        this.addWindowListener(new FrameCloseListener());
    }

    /**
     * Retrieves the component with the key.
     * 
     * @param <T> The type of the component.
     * @param key The key to retrieve with.
     * @return The component, hopefully.
     */
    @SuppressWarnings("unchecked")
    public <T> T getComponent(final String key) {
        return (T) componentMap.get(key);
    }

}
