package dev.frilly.slangdict.gui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.gui.tl.TranslateFrame;

/**
 * A set of buttons to set the locale.
 */
public class TranslateButtons extends JComponent implements Translatable {

    private final JButton changeLanguageButton;

    public TranslateButtons() {
        changeLanguageButton = new JButton(I18n.tl("pick-a-language"));
        I18n.register(this);
    }

    public void add(final JFrame frame) {
        final FlowLayout layout = new FlowLayout(FlowLayout.RIGHT, 8, 0);
        final JPanel panel = new JPanel(layout);
        changeLanguageButton.addActionListener(e -> {
            final var tlFrame = new TranslateFrame(frame);
            tlFrame.start();
        });

        panel.add(changeLanguageButton);
        frame.getContentPane().add(panel);
    }

    @Override
    public void updateTranslations() {
        changeLanguageButton.setText(I18n.tl("pick-a-language"));
    }

}
