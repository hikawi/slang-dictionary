package dev.frilly.slangdict.gui;

import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.interfaces.Overrideable;

/**
 * Implementation of the probably most used frame of this application.
 * 
 * This is where the dictionary is shown,along with options and tools
 * to manage said dictionary.
 */
public final class ViewFrame implements Overrideable {

    private static ViewFrame instance;

    // UI Components.
    private final JPanel outerPane;
    private final JPanel pane;

    private final JLabel title;
    private final JLabel name;
    private final JLabel entryCount;
    private final JButton back;

    private ViewFrame() {
        this.outerPane = new JPanel();
        this.outerPane.setLayout(new GridBagLayout());
        this.pane = new JPanel();

        this.title = new JLabel();
        this.name = new JLabel();
        this.entryCount = new JLabel();
        this.back = new JButton("back");

        this.setup();
    }

    private void setup() {
        pane.setLayout(new BoxLayout(this.pane, 1));
        pane.add(title);
        pane.add(name);
        pane.add(entryCount);
        pane.add(back);

        outerPane.add(pane);
    }

    private void setupRendering() {
        title.setText("Slang Dictionary");
        name.setText("Using dict \"" + Dictionary.getInstance().getName() + "\"");
        entryCount.setText("There are " + Dictionary.getInstance().getWords().size() + " entries.");
    }

    public static ViewFrame getInstance() {
        return switch (instance) {
            case null -> instance = new ViewFrame();
            default -> instance;
        };
    }

    @Override
    public JPanel getOverridingPane() {
        setupRendering(); // To set the name and title, etc. to correctly display, not the layout.
        return outerPane;
    }

}
