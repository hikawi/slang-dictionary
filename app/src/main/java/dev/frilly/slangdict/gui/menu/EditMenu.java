package dev.frilly.slangdict.gui.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.interfaces.Translatable;

/**
 * The component responsible for the "Edit" tab on the menu bar.
 */
public final class EditMenu implements Translatable {

    private static EditMenu instance;

    private final JMenu menu;

    private final JMenuItem addWord;
    private final JMenuItem addDefinition;
    private final JMenuItem deleteWord;
    private final JMenuItem deleteDefinition;
    private final JMenuItem markFavorite;
    private final JMenuItem unmarkFavorite;
    private final JMenuItem markLocked;
    private final JMenuItem markUnlocked;

    private EditMenu() {
        menu = new JMenu(I18n.tl("bar.edit"));

        addWord = new JMenuItem(I18n.tl("bar.edit.addWord"));
        addDefinition = new JMenuItem(I18n.tl("bar.edit.addDefinition"));
        deleteWord = new JMenuItem(I18n.tl("bar.edit.deleteWord"));
        deleteDefinition = new JMenuItem(I18n.tl("bar.edit.deleteDefinition"));
        markFavorite = new JMenuItem(I18n.tl("bar.edit.markFavorite"));
        unmarkFavorite = new JMenuItem(I18n.tl("bar.edit.unmarkFavorite"));
        markLocked = new JMenuItem(I18n.tl("bar.edit.markLocked"));
        markUnlocked = new JMenuItem(I18n.tl("bar.edit.markUnlocked"));

        this.setup();
        I18n.register(this);
    }

    private void setup() {
        menu.add(addWord);
        menu.add(addDefinition);
        menu.addSeparator();

        menu.add(deleteWord);
        menu.add(deleteDefinition);
        menu.addSeparator();

        menu.add(markFavorite);
        menu.add(unmarkFavorite);
        menu.addSeparator();

        menu.add(markLocked);
        menu.add(markUnlocked);
    }

    /**
     * Adds the "Edit" tab to the menu bar.
     * 
     * @param bar The bar
     */
    public void init(final JMenuBar bar) {
        bar.add(menu);
    }

    public static EditMenu getInstance() {
        return switch (instance) {
            case null -> instance = new EditMenu();
            default -> instance;
        };
    }

    @Override
    public void updateTranslations() {
        menu.setText(I18n.tl("bar.edit"));

        addWord.setText(I18n.tl("bar.edit.addWord"));
        addDefinition.setText(I18n.tl("bar.edit.addDefinition"));
        deleteWord.setText(I18n.tl("bar.edit.deleteWord"));
        deleteDefinition.setText(I18n.tl("bar.edit.deleteDefinition"));
        markFavorite.setText(I18n.tl("bar.edit.markFavorite"));
        unmarkFavorite.setText(I18n.tl("bar.edit.unmarkFavorite"));
        markLocked.setText(I18n.tl("bar.edit.markLocked"));
        markUnlocked.setText(I18n.tl("bar.edit.markUnlocked"));
    }

}
