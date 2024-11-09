package dev.frilly.slangdict.gui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dev.frilly.slangdict.I18n;

/**
 * The search box for the slang dictionary.
 */
public class SearchBox extends JComponent implements Translatable {

    private final JLabel label;

    public SearchBox() {
        this.label = new JLabel(I18n.tl("search"));
        I18n.register(this);
    }

    public void add(final JFrame frame) {
        final var searchBoxPanel = new JPanel();
        final var input = new JTextField();

        final var layout = new BoxLayout(searchBoxPanel, BoxLayout.X_AXIS);
        searchBoxPanel.setLayout(layout);

        input.setPreferredSize(new Dimension(400, 20));

        searchBoxPanel.add(label);
        searchBoxPanel.add(Box.createRigidArea(new Dimension(32, 0)));
        searchBoxPanel.add(input);

        frame.getContentPane().add(Box.createRigidArea(new Dimension(0, 16)));
        frame.getContentPane().add(searchBoxPanel);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(0, 16)));
    }

    @Override
    public void updateTranslations() {
        this.label.setText(I18n.tl("search"));
    }

}
