package dev.frilly.slangdict.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.features.file.NewDatabaseFeature;
import dev.frilly.slangdict.interfaces.Overrideable;
import dev.frilly.slangdict.interfaces.Translatable;
import dev.frilly.slangdict.listener.DocumentChangeListener;

/**
 * The simple frame to do a creation of a new database.
 */
public final class CreationFrame implements Translatable, Overrideable {

    private static CreationFrame instance;

    private final JPanel outerPane;
    private final JPanel pane;

    private final JLabel title;
    private final JLabel name;
    private final JTextField field;
    private final JCheckBox bootstrap;
    private final JButton cancel;
    private final JButton create;

    private CreationFrame() {
        outerPane = new JPanel();
        pane = new JPanel();
        name = new JLabel(I18n.tl("file.new.name"));
        title = new JLabel(I18n.tl("file.new"));
        field = new JTextField();
        bootstrap = new JCheckBox(I18n.tl("file.new.bootstrap"));
        cancel = new JButton(I18n.tl("button.cancel"));
        create = new JButton(I18n.tl("button.create"));

        this.setup();
        this.setupActions();
        I18n.register(this);
    }

    private void setup() {
        outerPane.setLayout(new GridLayout(1, 1));
        outerPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        final var layout = new GroupLayout(pane);
        pane.setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(title)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(name)
                        .addGap(8, 12, 16)
                        .addComponent(field))
                .addComponent(bootstrap)
                .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cancel)
                        .addComponent(create)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(title)
                .addGap(32, 48, 64)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(name)
                        .addComponent(field))
                .addGap(6, 8, 10)
                .addComponent(bootstrap)
                .addGap(16, 20, 24)
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(cancel)
                        .addComponent(create)));

        title.putClientProperty("FlatLaf.styleClass", "h2");
        field.setPreferredSize(new Dimension(400, 24));
        outerPane.add(pane);
    }

    private void setupActions() {
        field.getDocument().addDocumentListener((DocumentChangeListener) e -> {
            create.setEnabled(!field.getText().isBlank());
        });
        field.setText("My Database");

        cancel.addActionListener(e -> {
            MainFrame.getInstance().back();
        });
        create.addActionListener(e -> {
            new NewDatabaseFeature(field.getText(), bootstrap.isSelected()).run();
        });
    }

    @Override
    public JPanel getOverridingPane() {
        return outerPane;
    }

    @Override
    public void updateTranslations() {
        name.setText(I18n.tl("file.new.name"));
        title.setText(I18n.tl("file.new"));
        bootstrap.setText(I18n.tl("file.new.bootstrap"));
        cancel.setText(I18n.tl("button.cancel"));
        create.setText(I18n.tl("button.create"));
    }

    public static CreationFrame getInstance() {
        return switch (instance) {
            case null -> instance = new CreationFrame();
            default -> instance;
        };
    }

}
