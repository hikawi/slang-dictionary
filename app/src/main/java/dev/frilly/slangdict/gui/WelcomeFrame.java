package dev.frilly.slangdict.gui;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.Utilities;
import dev.frilly.slangdict.features.file.OpenDatabaseFeature;
import dev.frilly.slangdict.features.file.QuitFeature;
import dev.frilly.slangdict.interfaces.Overrideable;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;

/**
 * The frame that welcomes you when you start up the program.
 */
public final class WelcomeFrame implements Overrideable {

    private static WelcomeFrame instance;

    // The panel where everything happens, uses GroupLayout.
    private final JPanel panel = new JPanel();

    // The header, shows the logo and the version.
    private final JLabel bookIcon    = new JLabel(
        Application.getIcon("/images/book.png", 64, 64));
    private final JLabel programName = new JLabel("Dictionary");
    private final JLabel versionName = new JLabel(
        String.format("Version %s", Application.getVersion()));

    // Buttons and labels are separate. Buttons stack on top of labels.
    private final JButton newDatabase       = new JButton(
        Application.getIcon("/icons/library-add.png", 32, 32));
    private final JButton openDatabase      = new JButton(
        Application.getIcon("/icons/file-open.png", 32, 32));
    private final JButton quitProgram       = new JButton(
        Application.getIcon("/icons/exit.png", 32, 32));
    private final JLabel  newDatabaseLabel  = new JLabel("New Database");
    private final JLabel  openDatabaseLabel = new JLabel("Open Existing");
    private final JLabel  quitProgramLabel  = new JLabel("Quit Program");

    private WelcomeFrame() {
        setup();
        setupActions();
    }

    private void setup() {
        final var layout = Utilities.group(panel);
        panel.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

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

        layout.linkSize(newDatabase, openDatabase, quitProgram);
    }

    private void setupActions() {
        newDatabase.addActionListener(
            e -> MainFrame.getInstance().override(CreationFrame.getInstance()));
        openDatabase.addActionListener(e -> new OpenDatabaseFeature().run());
        quitProgram.addActionListener(e -> new QuitFeature().run());
    }

    public static WelcomeFrame getInstance() {
        return instance == null ? instance = new WelcomeFrame() : instance;
    }

    @Override
    public JPanel getOverridingPane() {
        return panel;
    }

}
