package dev.frilly.slangdict.gui;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dev.frilly.slangdict.Configuration;
import dev.frilly.slangdict.I18n;

/**
 * A pane that displays a simple checkbox "Autosave on close",
 * to automatically save data when the program exits.
 */
public class AutosavePane extends JComponent implements Translatable {

    private final JCheckBox checkBox;

    public AutosavePane() {
        this.checkBox = new JCheckBox(I18n.tl("autosave-on-close"));
        this.checkBox.setSelected(Configuration.isAutosave());

        I18n.register(this);
    }

    public void add(final JFrame frame) {
        final var layout = new FlowLayout(FlowLayout.LEFT);
        final var pane = new JPanel(layout);

        checkBox.addActionListener(e -> {
            System.out.println(checkBox.isSelected());
            Configuration.setAutosave(checkBox.isSelected());
        });
        pane.add(checkBox);

        frame.getContentPane().add(Box.createVerticalStrut(4));
        frame.getContentPane().add(pane);
        frame.getContentPane().add(Box.createVerticalStrut(4));
    }

    @Override
    public void updateTranslations() {
        this.checkBox.setText(I18n.tl("autosave-on-close"));
    }

}
