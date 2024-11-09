package dev.frilly.slangdict.gui;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.I18n;

/**
 * A group of buttons to control the dictionary.
 */
public final class ControlButtonsPane extends JComponent implements Translatable {

    private final JButton loadDefaultsButton;

    public ControlButtonsPane() {
        loadDefaultsButton = new JButton(I18n.tl("load-defaults"));
    }

    public void add(final JFrame frame) {
        final var layout = new FlowLayout(FlowLayout.CENTER, 16, 16);
        final var pane = new JPanel(layout);

        loadDefaultsButton.addActionListener(e -> {
            Application.getInstance().getDictionaryModel().loadDefaults();
        });
        pane.add(loadDefaultsButton);

        frame.getContentPane().add(Box.createVerticalStrut(8));
        frame.getContentPane().add(pane);
        frame.getContentPane().add(Box.createVerticalStrut(8));
    }

    @Override
    public void updateTranslations() {
        loadDefaultsButton.setText(I18n.tl("load-defaults"));
    }

}
