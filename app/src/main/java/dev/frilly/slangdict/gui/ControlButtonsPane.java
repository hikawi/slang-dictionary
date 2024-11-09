package dev.frilly.slangdict.gui;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Configuration;
import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.gui.delete.ConfirmDeleteFrame;

/**
 * A group of buttons to control the dictionary.
 */
public final class ControlButtonsPane extends JComponent implements Translatable {

    private final JButton loadDefaultsButton;
    private final JButton deleteEntriesButton;

    public ControlButtonsPane() {
        loadDefaultsButton = new JButton(I18n.tl("load-defaults"));
        deleteEntriesButton = new JButton(I18n.tl("delete-entries"));
        I18n.register(this);
    }

    public void add(final JFrame frame) {
        final var layout = new FlowLayout(FlowLayout.CENTER, 16, 16);
        final var pane = new JPanel(layout);

        loadDefaultsButton.addActionListener(e -> {
            Application.getInstance().getDictionaryModel().loadDefaults();
        });
        pane.add(loadDefaultsButton);

        deleteEntriesButton.addActionListener(e -> {
            if (Configuration.getSelectedWords().length == 0) {
                Dialogs.error(I18n.tl("no-entries-chosen"));
                return;
            }

            EventQueue.invokeLater(() -> {
                final var fr = new ConfirmDeleteFrame();
                fr.setup();
                fr.start();
            });
        });
        pane.add(deleteEntriesButton);

        frame.getContentPane().add(Box.createVerticalStrut(8));
        frame.getContentPane().add(pane);
        frame.getContentPane().add(Box.createVerticalStrut(8));
    }

    @Override
    public void updateTranslations() {
        loadDefaultsButton.setText(I18n.tl("load-defaults"));
        deleteEntriesButton.setText(I18n.tl("delete-entries"));
    }

}
