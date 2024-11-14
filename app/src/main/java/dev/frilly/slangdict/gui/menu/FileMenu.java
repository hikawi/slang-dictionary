package dev.frilly.slangdict.gui.menu;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.features.file.BombFeature;
import dev.frilly.slangdict.features.file.CloseDatabaseFeature;
import dev.frilly.slangdict.features.file.OpenDatabaseFeature;
import dev.frilly.slangdict.features.file.QuitFeature;
import dev.frilly.slangdict.features.file.ResetDatabaseFeature;
import dev.frilly.slangdict.features.file.SaveFeature;
import dev.frilly.slangdict.gui.CreationFrame;
import dev.frilly.slangdict.gui.MainFrame;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * The component menu for the "File" option on the menu bar.
 */
public final class FileMenu {

    private static FileMenu instance;

    private final JMenu menu = new JMenu("File");

    private final JMenuItem newDatabase = new JMenuItem("New Database");
    private final JMenuItem openDatabase = new JMenuItem("Open Database");
    private final JMenuItem closeDatabase = new JMenuItem("Save Database");

    private final JMenuItem saveDatabase = new JMenuItem("Save Database");
    private final JMenuItem duplicateDatabase = new JMenuItem("Duplicate Database");
    private final JMenuItem deleteDatabase = new JMenuItem("Delete Database");
    private final JMenuItem resetDatabase = new JMenu("Reset Database");
    private final JMenuItem bombDatabase = new JMenu("Bomb Database");
    private final JMenuItem quit = new JMenu("Quit");

    private final JMenuItem resetDefault = new JMenuItem("Reset to defaults");
    private final JMenuItem reset100k = new JMenuItem("Reset to 100k");

    private FileMenu() {
        this.setup();
    }

    private void setup() {
        closeDatabase.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_W, Application.getMaskedMetaKey() | KeyEvent.SHIFT_DOWN_MASK)
        );
        closeDatabase.addActionListener(e -> new CloseDatabaseFeature().run());

        saveDatabase.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Application.getMaskedMetaKey()));
        saveDatabase.addActionListener(e -> new SaveFeature().run());

        newDatabase.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Application.getMaskedMetaKey()));
        newDatabase.addActionListener(e -> MainFrame.getInstance().override(CreationFrame.getInstance()));

        openDatabase.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Application.getMaskedMetaKey()));
        openDatabase.addActionListener(e -> new OpenDatabaseFeature().run());

        bombDatabase.addActionListener(e -> {
            new BombFeature().run();
            MainFrame.getInstance().revalidate();
            MainFrame.getInstance().repaint();
        });

        quit.addActionListener(e -> new QuitFeature().run());

        resetDefault.addActionListener(e -> new ResetDatabaseFeature(false).run());
        reset100k.addActionListener(e -> new ResetDatabaseFeature(true).run());

        menu.add(newDatabase);
        menu.add(openDatabase);
        menu.addSeparator();

        menu.add(closeDatabase);
        menu.add(saveDatabase);
        menu.add(duplicateDatabase);
        menu.add(deleteDatabase);
        menu.addSeparator();

        resetDatabase.add(resetDefault);
        resetDatabase.add(reset100k);
        menu.add(resetDatabase);
        menu.add(bombDatabase);
        menu.addSeparator();

        menu.add(quit);
    }

    /**
     * Initializes the provided bar with components;
     *
     * @param bar
     */
    public void init(final JMenuBar bar) {
        bar.add(menu);
    }

    /**
     * Retrieves the FileMenu instance.
     *
     * @return the instance
     */
    public static FileMenu getInstance() {
        return switch (instance) {
            case null -> instance = new FileMenu();
            default -> instance;
        };
    }
}
