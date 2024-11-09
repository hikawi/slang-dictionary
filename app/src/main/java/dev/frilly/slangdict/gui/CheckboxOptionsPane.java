package dev.frilly.slangdict.gui;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Configuration;
import dev.frilly.slangdict.I18n;

/**
 * A pane that displays a simple checkbox "Autosave on close",
 * to automatically save data when the program exits.
 */
public class CheckboxOptionsPane extends JComponent implements Translatable {

    private final JCheckBox autosave;
    private final JCheckBox showOnlyFavorites;

    public CheckboxOptionsPane() {
        this.autosave = new JCheckBox(I18n.tl("autosave-on-close"));
        this.autosave.setSelected(Configuration.isAutosave());

        this.showOnlyFavorites = new JCheckBox(I18n.tl("show-only-favorites"));
        this.showOnlyFavorites.setSelected(Configuration.isShowOnlyFavorites());

        I18n.register(this);
    }

    public void add(final JFrame frame) {
        final var layout = new FlowLayout(FlowLayout.LEFT, 16, 16);
        final var pane = new JPanel(layout);

        autosave.addActionListener(e -> {
            Configuration.setAutosave(autosave.isSelected());
        });
        pane.add(autosave);

        showOnlyFavorites.addActionListener(e -> {
            Configuration.setShowOnlyFavorites(showOnlyFavorites.isSelected());
            Application.getInstance().getDictionaryModel().updateView();
        });
        pane.add(showOnlyFavorites);

        frame.getContentPane().add(Box.createVerticalStrut(4));
        frame.getContentPane().add(pane);
        frame.getContentPane().add(Box.createVerticalStrut(4));
    }

    @Override
    public void updateTranslations() {
        this.autosave.setText(I18n.tl("autosave-on-close"));
        this.showOnlyFavorites.setText(I18n.tl("show-only-favorites"));
    }

}
