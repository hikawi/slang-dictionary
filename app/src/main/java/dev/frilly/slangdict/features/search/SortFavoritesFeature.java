package dev.frilly.slangdict.features.search;

import dev.frilly.slangdict.Word;
import dev.frilly.slangdict.gui.ViewFrame;

import java.util.Comparator;

/**
 * Implementation of the "Sort Favorites" feature, that allows you to put favorite
 * words above stuff.
 */
public final class SortFavoritesFeature implements Runnable {

    public static int TOP = 0;
    public static int BOTTOM = 1;
    public static int HIDDEN = 2;
    public static int ONLY = 3;
    public static int ORDINARY = 4;

    // The current option for the sorting.
    public static int CURRENT = TOP;

    // A comparator that puts favorite words at top.
    public static Comparator<Word> TOP_COMPARATOR = (e1, e2) -> {
        if(e1.favorite && !e2.favorite)
            return -1; // Put favorite before.
        else if(e2.favorite && !e1.favorite)
            return 1;
        else return e1.word.compareTo(e2.word);
    };

    private final int selection;

    public SortFavoritesFeature(final int selection) {
        this.selection = selection;
    }

    @Override
    public void run() {
        CURRENT = Math.clamp(selection, TOP, ORDINARY);
//        ViewFrame.getInstance().query();
    }
}
