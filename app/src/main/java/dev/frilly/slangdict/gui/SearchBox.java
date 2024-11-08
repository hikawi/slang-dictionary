package dev.frilly.slangdict.gui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dev.frilly.slangdict.I18n;

/**
 * The search box for the slang dictionary.
 */
public class SearchBox extends JComponent implements Translatable {

    private final ApplicationFrame frame;

    private final JPanel searchBoxPanel;
    private final JLabel label;
    private final JTextField input;

    public SearchBox(final ApplicationFrame frame) {
        this.frame = frame;

        this.searchBoxPanel = new JPanel();
        this.label = new JLabel(I18n.tl("search"));
        this.input = new JTextField();

        final var layout = new BoxLayout(this.searchBoxPanel, BoxLayout.X_AXIS);
        this.searchBoxPanel.setLayout(layout);

        this.input.setPreferredSize(new Dimension(400, 20));

        this.searchBoxPanel.add(label);
        this.searchBoxPanel.add(Box.createRigidArea(new Dimension(32, 0)));
        this.searchBoxPanel.add(input);

        I18n.register(this);
    }

    public void add() {
        this.frame.getContentPane().add(Box.createRigidArea(new Dimension(0, 16)));
        this.frame.getContentPane().add(this.searchBoxPanel);
        this.frame.getContentPane().add(Box.createRigidArea(new Dimension(0, 16)));
    }

    @Override
    public void updateTranslations() {
        this.label.setText(I18n.tl("search"));
    }

}
