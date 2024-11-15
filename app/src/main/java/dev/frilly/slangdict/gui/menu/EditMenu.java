package dev.frilly.slangdict.gui.menu;

import javax.swing.*;

import dev.frilly.slangdict.features.edit.DeleteWordFeature;

import java.awt.event.KeyEvent;

/**
 * The component responsible for the "Edit" tab on the menu bar.
 */
public final class EditMenu {

    private static EditMenu instance;

    private final JMenu menu = new JMenu("Edit");

    private final JMenuItem addWord = new JMenuItem("Add word");
    private final JMenuItem deleteWord = new JMenuItem("Delete word(s)");
    private final JMenuItem markFavorite = new JMenuItem("Mark favorite");
    private final JMenuItem unmarkFavorite = new JMenuItem("Unmark favorite");
    private final JMenuItem markLocked = new JMenuItem("Lock");
    private final JMenuItem markUnlocked = new JMenuItem("Unlock");

    private EditMenu() {
        this.setup();
    }

    private void setup() {
        deleteWord.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        deleteWord.addActionListener(e -> new DeleteWordFeature().run());

        menu.add(addWord);
        menu.add(deleteWord);
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
        System.out.println("EditMenu init");
        bar.add(menu);
    }

    public static EditMenu getInstance() {
        return switch (instance) {
            case null -> instance = new EditMenu();
            default -> instance;
        };
    }

}
