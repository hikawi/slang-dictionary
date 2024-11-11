package dev.frilly.slangdict.gui;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.features.file.OpenDatabaseFeature;
import dev.frilly.slangdict.features.file.QuitFeature;
import dev.frilly.slangdict.interfaces.Overrideable;
import dev.frilly.slangdict.interfaces.Translatable;

/**
 * The frame that welcomes you when you start up the program.
 */
public final class WelcomeFrame implements Translatable, Overrideable {

    private static WelcomeFrame instance;

    private final JPanel overarch;

    private final JPanel panel;
    private final GroupLayout layout;

    private final JLabel welcomeText;
    private final JButton newDatabase;
    private final JButton openDatabase;
    private final JButton quit;

    private WelcomeFrame() {
        overarch = new JPanel();
        overarch.setLayout(new GridBagLayout());

        panel = new JPanel();
        layout = new GroupLayout(panel);
        panel.setLayout(layout);

        welcomeText = new JLabel(I18n.tl("welcome"));
        newDatabase = new JButton(I18n.tl("file.new"));
        openDatabase = new JButton(I18n.tl("file.open"));
        quit = new JButton(I18n.tl("file.quit"));

        setup();
        setupActions();
        I18n.register(this);
    }

    private void setup() {
        welcomeText.putClientProperty("FlatLaf.styleClass", "h00");
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        panel.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(welcomeText)
                .addGap(16, 24, 32)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(newDatabase)
                        .addComponent(openDatabase)
                        .addComponent(quit)));

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(welcomeText)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(newDatabase)
                        .addComponent(openDatabase)
                        .addComponent(quit)));

        layout.linkSize(SwingConstants.HORIZONTAL, newDatabase, openDatabase, quit);
        overarch.add(panel);
    }

    private void setupActions() {
        newDatabase.addActionListener(e -> MainFrame.getInstance().override(CreationFrame.getInstance()));
        openDatabase.addActionListener(e -> new OpenDatabaseFeature().run());
        quit.addActionListener(e -> new QuitFeature().run());
    }

    @Override
    public JPanel getOverridingPane() {
        return overarch;
    }

    @Override
    public void updateTranslations() {
        welcomeText.setText(I18n.tl("welcome"));
        newDatabase.setText(I18n.tl("file.new"));
        openDatabase.setText(I18n.tl("file.open"));
        quit.setText(I18n.tl("file.quit"));
    }

    public static WelcomeFrame getInstance() {
        return switch (instance) {
            case null -> instance = new WelcomeFrame();
            default -> instance;
        };
    }

}
