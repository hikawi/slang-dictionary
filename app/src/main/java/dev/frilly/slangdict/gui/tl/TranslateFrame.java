package dev.frilly.slangdict.gui.tl;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import dev.frilly.slangdict.I18n;

/**
 * A frame to list out options to pick a language.
 */
public class TranslateFrame extends JDialog {

    private final Locale[] locales = new Locale[] { Locale.ENGLISH, Locale.JAPANESE, Locale.of("vi") };

    public TranslateFrame(final JFrame parent) {
        super(parent, I18n.tl("pick-a-language"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setup();
        this.setLocationRelativeTo(null);
    }

    private void setup() {
        final var pane = new JPanel();
        final var layout = new BoxLayout(pane, BoxLayout.Y_AXIS);
        pane.setLayout(layout);
        this.setContentPane(pane);

        final var label = new JLabel(I18n.tl("pick-a-language"));
        final var labelLayout = new FlowLayout(FlowLayout.LEFT);
        final var labelPane = new JPanel(labelLayout);
        labelPane.add(label);

        final var list = new JList<String>(
                Arrays.asList(I18n.tl("english"), I18n.tl("japanese"), I18n.tl("vietnamese")).toArray(new String[0]));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        final var scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        this.getContentPane().add(Box.createVerticalStrut(16));
        this.getContentPane().add(label);
        this.getContentPane().add(Box.createVerticalStrut(16));
        this.getContentPane().add(scrollPane);
        this.getContentPane().add(Box.createVerticalStrut(16));
        this.setupButtonsPane(list);
    }

    private void setupButtonsPane(final JList<String> list) {
        final var layout = new FlowLayout(FlowLayout.CENTER, 16, 0);
        final var pane = new JPanel(layout);

        final var cancelButton = new JButton(I18n.tl("cancel"));
        cancelButton.addActionListener(e -> {
            dispose();
        });

        final var confirmButton = new JButton(I18n.tl("confirm"));
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(e -> {
            if (list.isSelectionEmpty())
                return;
            final var selectedVal = list.getSelectedIndex();
            I18n.setLocale(locales[selectedVal]);
            dispose();
        });

        list.addListSelectionListener(e -> {
            confirmButton.setEnabled(true);
        });

        pane.add(cancelButton);
        pane.add(confirmButton);
        this.getContentPane().add(pane);
        this.getContentPane().add(Box.createVerticalStrut(16));
    }

    public void start() {
        this.pack();
        this.setVisible(true);
    }

}
