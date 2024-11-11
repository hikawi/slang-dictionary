package dev.frilly.slangdict.gui.menu;

import java.util.Arrays;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.interfaces.Translatable;

/**
 * The component responsible for the "Search" tab on the menu bar.
 */
public final class SearchMenu implements Translatable {

    private static SearchMenu instance;

    private final JMenu menu;

    private final JCheckBoxMenuItem matchWord;
    private final JCheckBoxMenuItem matchDefinition;
    private final JCheckBoxMenuItem matchCase;
    private final JCheckBoxMenuItem matchRegex;
    private final JCheckBoxMenuItem instantMatch;

    private SearchMenu() {
        menu = new JMenu(I18n.tl("bar.search"));

        matchWord = new JCheckBoxMenuItem(I18n.tl("bar.search.matchWord"));
        matchDefinition = new JCheckBoxMenuItem(I18n.tl("bar.search.matchDefinition"));
        matchCase = new JCheckBoxMenuItem(I18n.tl("bar.search.matchCase"));
        matchRegex = new JCheckBoxMenuItem(I18n.tl("bar.search.matchRegex"));
        instantMatch = new JCheckBoxMenuItem(I18n.tl("bar.search.instantMatch"));

        this.setup();
        I18n.register(this);
    }

    private void setup() {
        Arrays.asList(matchWord, matchDefinition, matchCase, matchRegex, instantMatch).forEach(menu::add);
    }

    public void init(final JMenuBar bar) {
        bar.add(menu);
    }

    @Override
    public void updateTranslations() {
        menu.setText(I18n.tl("bar.search"));

        matchWord.setText(I18n.tl("bar.search.matchWord"));
        matchDefinition.setText(I18n.tl("bar.search.matchDefinition"));
        matchCase.setText(I18n.tl("bar.search.matchCase"));
        matchRegex.setText(I18n.tl("bar.search.matchRegex"));
        instantMatch.setText(I18n.tl("bar.search.instantMatch"));
    }

    public static SearchMenu getInstance() {
        return switch (instance) {
            case null -> instance = new SearchMenu();
            default -> instance;
        };
    }

}
