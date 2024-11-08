package dev.frilly.slangdict.gui;

import java.awt.FlowLayout;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dev.frilly.slangdict.Application;

/**
 * A set of buttons to set the locale.
 */
public class TranslateButtons extends JComponent {

    public TranslateButtons() {
    }

    public void add(final JFrame frame) {
        final FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 8, 0);
        final JPanel panel = new JPanel(layout);

        final JButton english = new JButton("English");
        english.addActionListener(e -> {
            Application.getInstance().getI18n().setLocale(Locale.ENGLISH);
        });

        final JButton japanese = new JButton("Japanese");
        japanese.addActionListener(e -> {
            Application.getInstance().getI18n().setLocale(Locale.JAPANESE);
        });

        final JButton vietnamese = new JButton("Vietnamese");
        vietnamese.addActionListener(e -> {
            Application.getInstance().getI18n().setLocale(Locale.of("vi"));
        });

        panel.add(english);
        panel.add(japanese);
        panel.add(vietnamese);
        frame.getContentPane().add(panel);
    }

}
