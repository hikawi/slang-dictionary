package dev.frilly.slangdict.gui.menu;

import java.util.Locale;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.interfaces.Translatable;

/**
 * The component responsible for handling the "View" tab on the menu.
 */
public final class ViewMenu implements Translatable {

    private static ViewMenu instance;

    private final JMenu menu;

    private final JMenu scrollBar;
    private final JRadioButtonMenuItem scrollBarOnlyNeeded;
    private final JRadioButtonMenuItem scrollBarAlways;
    private final JRadioButtonMenuItem scrollBarNever;

    private final JMenu favorites;
    private final JRadioButtonMenuItem favoritesHidden;
    private final JRadioButtonMenuItem favoritesOnly;
    private final JRadioButtonMenuItem favoritesTop;
    private final JRadioButtonMenuItem favoritesBottom;

    private final JMenu theme;
    private final JRadioButtonMenuItem themeLight;
    private final JRadioButtonMenuItem themeDark;
    private final JRadioButtonMenuItem themeMacLight;
    private final JRadioButtonMenuItem themeMacDark;

    private final JMenu languages;
    private final JRadioButtonMenuItem langEnglish;
    private final JRadioButtonMenuItem langJapanese;
    private final JRadioButtonMenuItem langVietnamese;

    private ViewMenu() {
        menu = new JMenu(I18n.tl("bar.view"));

        scrollBar = new JMenu(I18n.tl("bar.view.scrollBar"));
        scrollBarOnlyNeeded = new JRadioButtonMenuItem(I18n.tl("bar.view.scrollBar.needed"));
        scrollBarAlways = new JRadioButtonMenuItem(I18n.tl("bar.view.scrollBar.always"));
        scrollBarNever = new JRadioButtonMenuItem(I18n.tl("bar.view.scrollBar.never"));

        favorites = new JMenu(I18n.tl("bar.view.favorites"));
        favoritesHidden = new JRadioButtonMenuItem(I18n.tl("bar.view.favorites.hidden"));
        favoritesOnly = new JRadioButtonMenuItem(I18n.tl("bar.view.favorites.only"));
        favoritesTop = new JRadioButtonMenuItem(I18n.tl("bar.view.favorites.top"));
        favoritesBottom = new JRadioButtonMenuItem(I18n.tl("bar.view.favorites.bottom"));

        theme = new JMenu(I18n.tl("bar.view.theme"));
        themeLight = new JRadioButtonMenuItem(I18n.tl("bar.view.theme.light"));
        themeDark = new JRadioButtonMenuItem(I18n.tl("bar.view.theme.dark"));
        themeMacLight = new JRadioButtonMenuItem(I18n.tl("bar.view.theme.macLight"));
        themeMacDark = new JRadioButtonMenuItem(I18n.tl("bar.view.theme.macDark"));

        languages = new JMenu(I18n.tl("bar.view.languages"));
        langEnglish = new JRadioButtonMenuItem(I18n.tl("bar.view.languages.english"));
        langJapanese = new JRadioButtonMenuItem(I18n.tl("bar.view.languages.japanese"));
        langVietnamese = new JRadioButtonMenuItem(I18n.tl("bar.view.languages.vietnamese"));

        this.setup();
        I18n.register(this);
    }

    private void setupLanguages() {
        langEnglish.addActionListener(e -> {
            I18n.setLocale(Locale.ENGLISH);
        });
        langJapanese.addActionListener(e -> {
            I18n.setLocale(Locale.JAPANESE);
        });
        langVietnamese.addActionListener(e -> {
            I18n.setLocale(Locale.of("vi"));
        });
    }

    private void setup() {
        scrollBar.add(scrollBarOnlyNeeded);
        scrollBar.add(scrollBarAlways);
        scrollBar.add(scrollBarNever);

        favorites.add(favoritesHidden);
        favorites.add(favoritesOnly);
        favorites.add(favoritesTop);
        favorites.add(favoritesBottom);

        theme.add(themeLight);
        theme.add(themeDark);
        theme.add(themeMacLight);
        theme.add(themeMacDark);

        languages.add(langEnglish);
        languages.add(langJapanese);
        languages.add(langVietnamese);
        setupLanguages();

        menu.add(scrollBar);
        menu.add(favorites);
        menu.add(theme);
        menu.add(languages);
    }

    /**
     * Initializes the provided bar with components;
     * 
     * @param bar
     */
    public void init(final JMenuBar bar) {
        bar.add(menu);
    }

    @Override
    public void updateTranslations() {
        menu.setText(I18n.tl("bar.view"));

        scrollBar.setText(I18n.tl("bar.view.scrollBar"));
        scrollBarOnlyNeeded.setText(I18n.tl("bar.view.scrollBar.needed"));
        scrollBarAlways.setText(I18n.tl("bar.view.scrollBar.always"));
        scrollBarNever.setText(I18n.tl("bar.view.scrollBar.never"));

        favorites.setText(I18n.tl("bar.view.favorites"));
        favoritesHidden.setText(I18n.tl("bar.view.favorites.hidden"));
        favoritesOnly.setText(I18n.tl("bar.view.favorites.only"));
        favoritesTop.setText(I18n.tl("bar.view.favorites.top"));
        favoritesBottom.setText(I18n.tl("bar.view.favorites.bottom"));

        theme.setText(I18n.tl("bar.view.theme"));
        themeLight.setText(I18n.tl("bar.view.theme.light"));
        themeDark.setText(I18n.tl("bar.view.theme.dark"));
        themeMacLight.setText(I18n.tl("bar.view.theme.macLight"));
        themeMacDark.setText(I18n.tl("bar.view.theme.macDark"));

        languages.setText(I18n.tl("bar.view.languages"));
        langEnglish.setText(I18n.tl("bar.view.languages.english"));
        langJapanese.setText(I18n.tl("bar.view.languages.japanese"));
        langVietnamese.setText(I18n.tl("bar.view.languages.vietnamese"));
    }

    public static ViewMenu getInstance() {
        return switch (instance) {
            case null -> instance = new ViewMenu();
            default -> instance;
        };
    }

}
