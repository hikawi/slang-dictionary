package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.features.file.NewDatabaseFeature;
import dev.frilly.slangdict.interfaces.Overrideable;
import dev.frilly.slangdict.listener.DocumentChangeListener;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;

/**
 * The simple frame to do a creation of a new database.
 */
public final class CreationFrame implements Overrideable {

    private static CreationFrame instance;

    private final JPanel outerPane = new JPanel();
    private final JPanel pane = new JPanel();

    private final JLabel title = new JLabel("New Database");
    private final JLabel name = new JLabel("Name");
    private final JTextField field = new JTextField("My Database");
    private final JButton cancel = new JButton("Cancel");
    private final JButton create = new JButton("Create");

    private final JRadioButton bootstrapDefault = new JRadioButton("Bootstrap with default values");
    private final JRadioButton bootstrap100k = new JRadioButton("Bootstrap with 100,000 entries");
    private final JRadioButton bootstrapNothing = new JRadioButton("Don't bootstrap with anything");

    private CreationFrame() {
        this.setup();
        this.setupActions();
    }

    public static CreationFrame getInstance() {
        return instance == null ? instance = new CreationFrame() : instance;
    }

    @Override
    public JPanel getOverridingPane() {
        return outerPane;
    }

    private void setup() {
        outerPane.setLayout(new BorderLayout());
        outerPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        final var layout = new GroupLayout(pane);
        pane.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        final var buttonGroup = new ButtonGroup();
        buttonGroup.add(bootstrap100k);
        buttonGroup.add(bootstrapDefault);
        buttonGroup.add(bootstrapNothing);
        bootstrapNothing.setSelected(true);

        layout.setHorizontalGroup(
            layout
                .createParallelGroup(Alignment.LEADING)
                .addComponent(title)
                .addGroup(layout.createSequentialGroup().addComponent(name).addGap(8, 12, 16).addComponent(field))
                .addComponent(bootstrapDefault)
                .addComponent(bootstrap100k)
                .addComponent(bootstrapNothing)
                .addGroup(Alignment.TRAILING, layout.createSequentialGroup().addComponent(cancel).addComponent(create))
        );
        layout.setVerticalGroup(
            layout
                .createSequentialGroup()
                .addComponent(title)
                .addGap(32, 48, 64)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(name).addComponent(field))
                .addGap(6, 8, 10)
                .addComponent(bootstrapDefault)
                .addComponent(bootstrap100k)
                .addComponent(bootstrapNothing)
                .addGap(16, 20, 24)
                .addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(cancel).addComponent(create))
        );

        title.putClientProperty("FlatLaf.styleClass", "h2");
        cancel.setIcon(Application.getIcon(getClass().getResource("/icons/close.png"), 16, 16));
        create.setIcon(Application.getIcon(getClass().getResource("/icons/library-add.png"), 16, 16));
        field.setPreferredSize(new Dimension(400, 24));
        outerPane.add(pane, BorderLayout.CENTER);
    }

    private void setupActions() {
        field.getDocument().addDocumentListener((DocumentChangeListener) e -> create.setEnabled(!field.getText().isBlank()));

        cancel.addActionListener(e -> MainFrame.getInstance().back());
        create.addActionListener(e -> {
            final var bootstrap = bootstrap100k.isSelected()
                ? NewDatabaseFeature.BOOTSTRAP_100K
                : bootstrapDefault.isSelected()
                ? NewDatabaseFeature.BOOTSTRAP_DEFAULT
                : NewDatabaseFeature.NO_BOOTSTRAP;
            new NewDatabaseFeature(field.getText(), bootstrap).run();
        });
    }

}
