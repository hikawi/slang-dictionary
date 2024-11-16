package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.Dialogs;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.interfaces.Overrideable;

import javax.swing.*;

/**
 * The frame for choosing which dictionary to reset to.
 */
public final class ResettingFrame implements Overrideable {

    private static ResettingFrame instance;

    private final JPanel panel = new JPanel();

    private final JLabel chooseLabel = new JLabel("Pick a dictionary to re-populate this database with?");
    private final JRadioButton dict100k = new JRadioButton("Use 100k testing entries");
    private final JRadioButton dictDefault = new JRadioButton("Use default entries", true);

    private final JButton confirm = new JButton("Confirm");
    private final JButton cancel = new JButton("Cancel");

    public static ResettingFrame getInstance() {
        return instance == null ? instance = new ResettingFrame() : instance;
    }

    private ResettingFrame() {
        setup();
        setupActions();
    }

    private void setup() {
        panel.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        final var g = new ButtonGroup();
        g.add(dict100k);
        g.add(dictDefault);

        final var l = new GroupLayout(panel);
        panel.setLayout(l);
        l.setAutoCreateGaps(true);
        l.setAutoCreateContainerGaps(true);

        l.setHorizontalGroup(l.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(chooseLabel)
            .addComponent(dict100k)
            .addComponent(dictDefault)
            .addGroup(GroupLayout.Alignment.TRAILING, l.createSequentialGroup()
                .addComponent(cancel)
                .addComponent(confirm)));

        l.setVerticalGroup(l.createSequentialGroup()
            .addComponent(chooseLabel)
            .addGap(16, 20, 24)
            .addComponent(dict100k)
            .addGap(4, 6, 8)
            .addComponent(dictDefault)
            .addGroup(l.createParallelGroup()
                .addComponent(cancel)
                .addComponent(confirm)));

        chooseLabel.putClientProperty("FlatLaf.styleClass", "h3");
        l.linkSize(cancel, confirm);
    }

    private void setupActions() {
        cancel.addActionListener(e -> MainFrame.getInstance().back());
        confirm.addActionListener(e -> {
            final var opt = Dialogs.confirm("Are you sure you want to reset all entries to default?");
            if(opt == JOptionPane.YES_OPTION) {
                Dictionary.getInstance().loadDefaults(dict100k.isSelected());
                MainFrame.getInstance().back();
            }
        });
    }

    @Override
    public JPanel getOverridingPane() {
        return panel;
    }

}
