package dev.frilly.slangdict.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import dev.frilly.slangdict.Application;
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

    private final JPanel outerPane = new JPanel();
    private final JPanel pane = new JPanel();

    private final JLabel title = new JLabel();
    private final JLabel name = new JLabel();
    private final JTextField field = new JTextField("My Database");

    private final JRadioButton bootstrapDefault = new JRadioButton();
    private final JRadioButton bootstrap100k = new JRadioButton();
    private final JButton cancel = new JButton();
    private final JButton create = new JButton();

    private CreationFrame() {
        this.updateTranslations();
        this.setup();
        this.setupActions();
        I18n.register(this);
    }

    private void setup() {
        outerPane.setLayout(new GridLayout(1, 1));
        outerPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        final var layout = new GroupLayout(pane);
        pane.setLayout(layout);

        final var buttonGroup = new ButtonGroup();
        buttonGroup.add(bootstrap100k);
        buttonGroup.add(bootstrapDefault);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addComponent(title)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(name)
                        .addGap(8, 12, 16)
                        .addComponent(field))
                .addComponent(bootstrapDefault)
                .addComponent(bootstrap100k)
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
                .addComponent(bootstrapDefault)
                .addComponent(bootstrap100k)
                .addGap(16, 20, 24)
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(cancel)
                        .addComponent(create)));

        title.putClientProperty("FlatLaf.styleClass", "h2");
        cancel.setIcon(Application.getIcon(getClass().getResource("/icons/close.png"), 16, 16));
        create.setIcon(Application.getIcon(getClass().getResource("/icons/library-add.png"), 16, 16));
        field.setPreferredSize(new Dimension(400, 24));
        outerPane.add(pane);
    }

    private void setupActions() {
        field.getDocument().addDocumentListener((DocumentChangeListener) e -> {
            create.setEnabled(!field.getText().isBlank());
        });

        cancel.addActionListener(e -> {
            MainFrame.getInstance().back();
        });
        create.addActionListener(e -> {
            final var bootstrap = bootstrap100k.isSelected() ? 2 : bootstrapDefault.isSelected() ? 1 : 0;
            new NewDatabaseFeature(field.getText(), bootstrap).run();
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
        bootstrapDefault.setText(I18n.tl("file.new.bootstrap"));
        bootstrap100k.setText(I18n.tl("file.new.100k"));
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
