package dev.frilly.slangdict.gui.delete;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Configuration;
import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.listener.MainFrameReEnableListener;

/**
 * The frame to confirm the deletion.
 */
public final class ConfirmDeleteFrame extends JFrame {

    public ConfirmDeleteFrame() {
        super(I18n.tl("delete-confirm"));
        this.addWindowListener(new MainFrameReEnableListener());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final var pane = new JPanel();
        final var layout = new BoxLayout(pane, BoxLayout.Y_AXIS);
        pane.setLayout(layout);
        this.setContentPane(pane);
        pane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
    }

    public void setup() {
        final var labelLayout = new FlowLayout(FlowLayout.LEFT);
        final var labelPane = new JPanel(labelLayout);
        final var label = new JLabel(I18n.tl("delete-confirm-message"));
        labelPane.add(label);

        final var listPane = new JScrollPane(new JList<>(Configuration.getSelectedWords()));
        listPane.setPreferredSize(new Dimension(400, 400));

        final var buttonsLayout = new FlowLayout(FlowLayout.CENTER, 16, 0);
        final var buttonsPane = new JPanel(buttonsLayout);

        final var cancelButton = new JButton(I18n.tl("cancel"));
        cancelButton.addActionListener(e -> {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        buttonsPane.add(cancelButton);

        final var confirmButton = new JButton(I18n.tl("confirm"));
        confirmButton.addActionListener(e -> {
            final String[] deleting = Configuration.getSelectedWords();
            Configuration.setSelectedWords(new String[0]);
            for (final var word : deleting)
                Configuration.removeWord(word);

            Application.getInstance().getDictionaryModel().updateView();
            Dialogs.success(I18n.tl("delete-success"));
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        buttonsPane.add(confirmButton);

        this.getContentPane().add(labelPane);
        this.getContentPane().add(Box.createVerticalStrut(16));
        this.getContentPane().add(listPane);
        this.getContentPane().add(Box.createVerticalStrut(16));
        this.getContentPane().add(buttonsPane);
    }

    public void start() {
        this.pack();
        this.setLocationRelativeTo(Application.getInstance().getFrame());
        this.setVisible(true);
    }

}
