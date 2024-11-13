package dev.frilly.slangdict.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dev.frilly.slangdict.Application;
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

    private final JPanel outerPane = new JPanel(new GridBagLayout());
    private final JPanel panel = new JPanel();

    private final JLabel bookIcon = new JLabel(
            Application.getIcon(getClass().getResource("/images/book.png"), 64, 64));
    private final JLabel programName = new JLabel(I18n.tl("welcome.programName"));
    private final JLabel versionName = new JLabel(I18n.tl("welcome.version", Application.getVersion()));

    private final JButton newDatabase = new JButton(
            Application.getIcon(getClass().getResource("/icons/library-add.png"), 32, 32));
    private final JButton openDatabase = new JButton(
            Application.getIcon(getClass().getResource("/icons/file-open.png"), 32, 32));
    private final JButton quitProgram = new JButton(
            Application.getIcon(getClass().getResource("/icons/exit.png"), 32, 32));

    private final JLabel newDatabaseLabel = new JLabel();
    private final JLabel openDatabaseLabel = new JLabel();
    private final JLabel quitProgramLabel = new JLabel();

    private WelcomeFrame() {
        updateTranslations();
        setup();
        setupActions();
        I18n.register(this);
    }

    private void setup() {
        final var layout = new GroupLayout(panel);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        programName.putClientProperty("FlatLaf.styleClass", "h1");
        versionName.putClientProperty("FlatLaf.styleClass", "semibold");
        newDatabase.setPreferredSize(new Dimension(64, 64));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(bookIcon)
                .addGap(8)
                .addComponent(programName)
                .addGap(4)
                .addComponent(versionName)
                .addGap(32)
                .addGroup(layout.createParallelGroup(Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(newDatabase)
                                .addGap(4)
                                .addComponent(newDatabaseLabel))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(openDatabase)
                                .addGap(4)
                                .addComponent(openDatabaseLabel))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(quitProgram)
                                .addGap(4)
                                .addComponent(quitProgramLabel))));

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER)
                .addComponent(bookIcon)
                .addComponent(programName)
                .addComponent(versionName)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(Alignment.CENTER)
                                .addComponent(newDatabase)
                                .addComponent(newDatabaseLabel))
                        .addGap(32)
                        .addGroup(layout.createParallelGroup(Alignment.CENTER)
                                .addComponent(openDatabase)
                                .addComponent(openDatabaseLabel))
                        .addGap(32)
                        .addGroup(layout.createParallelGroup(Alignment.CENTER)
                                .addComponent(quitProgram)
                                .addComponent(quitProgramLabel))));

        layout.linkSize(SwingConstants.HORIZONTAL, newDatabase, openDatabase, quitProgram);
        layout.linkSize(SwingConstants.VERTICAL, newDatabase, openDatabase, quitProgram);
        outerPane.add(panel);
    }

    private void setupActions() {
        newDatabase.addActionListener(e -> MainFrame.getInstance().override(CreationFrame.getInstance()));
        openDatabase.addActionListener(e -> new OpenDatabaseFeature().run());
        quitProgram.addActionListener(e -> new QuitFeature().run());
    }

    @Override
    public JPanel getOverridingPane() {
        return outerPane;
    }

    @Override
    public void updateTranslations() {
        programName.setText(I18n.tl("welcome.programName"));
        versionName.setText(I18n.tl("welcome.version", Application.getVersion()));

        newDatabaseLabel.setText(I18n.tl("welcome.new"));
        openDatabaseLabel.setText(I18n.tl("welcome.open"));
        quitProgramLabel.setText(I18n.tl("welcome.quit"));
    }

    public static WelcomeFrame getInstance() {
        return switch (instance) {
            case null -> instance = new WelcomeFrame();
            default -> instance;
        };
    }

}
