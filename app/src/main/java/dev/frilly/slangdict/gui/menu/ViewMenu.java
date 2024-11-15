package dev.frilly.slangdict.gui.menu;

import dev.frilly.slangdict.I18n;
import dev.frilly.slangdict.features.search.SortFavoritesFeature;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

/**
 * The component responsible for handling the "View" tab on the menu.
 */
public final class ViewMenu {

    private static ViewMenu instance;

    public static ViewMenu getInstance() {
        return switch (instance) {
            case null -> instance = new ViewMenu();
            default -> instance;
        };
    }

    private final JMenu menu = new JMenu("View");
//    private final JMenu scrollBar;
//    private final JRadioButtonMenuItem scrollBarOnlyNeeded;
//    private final JRadioButtonMenuItem scrollBarAlways;
//    private final JRadioButtonMenuItem scrollBarNever;

    private final JMenu favorites = new JMenu("Favorites");
    private final JRadioButtonMenuItem favoritesTop = new JRadioButtonMenuItem("Sort favorites to top");
    private final JRadioButtonMenuItem favoritesBottom = new JRadioButtonMenuItem("Sort favorites to bottom");
    private final JRadioButtonMenuItem favoritesHidden = new JRadioButtonMenuItem("Hide favorites");
    private final JRadioButtonMenuItem favoritesOnly = new JRadioButtonMenuItem("Show favorites only");
    private final JRadioButtonMenuItem favoritesOrdinary = new JRadioButtonMenuItem("Don't sort favorites");

    private ViewMenu() {
        this.setup();
    }

    public void updateFavorites() {
        List.of(favoritesTop, favoritesBottom, favoritesHidden, favoritesOnly, favoritesOrdinary)
            .get(SortFavoritesFeature.CURRENT)
            .setSelected(true);
    }

    /**
     * Initializes the provided bar with components;
     *
     * @param bar
     */
    public void init(final JMenuBar bar) {
        System.out.println("ViewMenu init");
        bar.add(menu);
    }

    private void setup() {
        final var favoriteGroup = new ButtonGroup();
        final var favoritesList = List.of(favoritesTop, favoritesBottom, favoritesHidden, favoritesOnly, favoritesOrdinary);
        IntStream.range(0, favoritesList.size()).forEach(i -> {
            favoritesList.get(i).addActionListener(e -> new SortFavoritesFeature(i).run());
            favoriteGroup.add(favoritesList.get(i));
        });

//        scrollBar.add(scrollBarOnlyNeeded);
//        scrollBar.add(scrollBarAlways);
//        scrollBar.add(scrollBarNever);

        favorites.add(favoritesHidden);
        favorites.add(favoritesOnly);
        favorites.add(favoritesTop);
        favorites.add(favoritesBottom);
        menu.add(favorites);
    }
}
