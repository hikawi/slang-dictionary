package dev.frilly.slangdict.gui.menu;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import dev.frilly.slangdict.Application;
import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.features.file.NewDatabaseFeature;
import dev.frilly.slangdict.features.file.OpenDatabaseFeature;
import dev.frilly.slangdict.features.file.QuitFeature;
import dev.frilly.slangdict.interfaces.Translatable;

/**
 * The component menu for the "File" option on the menu bar.
 */
public final class FileMenu implements Translatable {

    private static FileMenu instance;

    private final JMenu menu;

    private final JMenuItem newDatabase;
    private final JMenuItem openDatabase;
    private final JMenuItem renameDatabase;
    private final JMenuItem saveDatabase;
    private final JMenuItem duplicateDatabase;
    private final JMenuItem deleteDatabase;
    private final JMenuItem resetDatabase;
    private final JMenuItem quit;

    private FileMenu() {
        menu = new JMenu(I18n.tl("bar.file"));

        newDatabase = new JMenuItem(I18n.tl("bar.file.new"));
        openDatabase = new JMenuItem(I18n.tl("bar.file.open"));
        renameDatabase = new JMenuItem(I18n.tl("bar.file.rename"));
        saveDatabase = new JMenuItem(I18n.tl("bar.file.save"));
        duplicateDatabase = new JMenuItem(I18n.tl("bar.file.duplicate"));
        deleteDatabase = new JMenuItem(I18n.tl("bar.file.delete"));
        resetDatabase = new JMenuItem(I18n.tl("bar.file.reset"));
        quit = new JMenuItem(I18n.tl("bar.file.quit"));

        this.setup();
        I18n.register(this);
    }

    private void setupRenameDatabase() {
        renameDatabase.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
    }

    private void setupSaveDatabase() {
        saveDatabase.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Application.getMaskedMetaKey()));
    }

    private void setup() {
        setupRenameDatabase();
        setupSaveDatabase();

        newDatabase.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Application.getMaskedMetaKey()));
        newDatabase.addActionListener(e -> new NewDatabaseFeature().run());

        openDatabase.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Application.getMaskedMetaKey()));
        openDatabase.addActionListener(e -> new OpenDatabaseFeature().run());

        quit.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_W, Application.getMaskedMetaKey() | KeyEvent.SHIFT_DOWN_MASK));
        quit.addActionListener(e -> new QuitFeature().run());

        menu.add(newDatabase);
        menu.add(openDatabase);
        menu.addSeparator();

        menu.add(renameDatabase);
        menu.add(saveDatabase);
        menu.add(duplicateDatabase);
        menu.add(deleteDatabase);
        menu.addSeparator();

        menu.add(resetDatabase);
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

    @Override
    public void updateTranslations() {
        menu.setText(I18n.tl("bar.file"));

        newDatabase.setText(I18n.tl("bar.file.new"));
        openDatabase.setText(I18n.tl("bar.file.open"));
        renameDatabase.setText(I18n.tl("bar.file.rename"));
        saveDatabase.setText(I18n.tl("bar.file.save"));
        duplicateDatabase.setText(I18n.tl("bar.file.duplicate"));
        deleteDatabase.setText(I18n.tl("bar.file.delete"));
        resetDatabase.setText(I18n.tl("bar.file.reset"));
        quit.setText(I18n.tl("bar.file.quit"));
    }

}
