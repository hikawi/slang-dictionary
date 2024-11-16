package dev.frilly.slangdict.features.search;

import dev.frilly.slangdict.Word;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Implementation of the "Sort Favorites" feature, that allows you to put favorite
 * words above stuff.
 */
public final class SortFavoritesFeature implements Function<Stream<Word>, Stream<Word>> {

    public static final int TOP = 0;
    public static final int BOTTOM = 1;
    public static final int HIDDEN = 2;
    public static final int ONLY = 3;
    public static final int ORDINARY = 4;

    // A comparator that puts favorite words at top.
    public static Comparator<Word> TOP_COMPARATOR = (e1, e2) -> {
        if (e1.favorite && !e2.favorite)
            return -1; // Put favorite before.
        else if (e2.favorite && !e1.favorite)
            return 1;
        else return e1.word.compareTo(e2.word);
    };

    private final int selection;

    public SortFavoritesFeature(final int selection) {
        this.selection = selection;
    }

    @Override
    public Stream<Word> apply(final Stream<Word> wordStream) {
        return switch (selection) {
            case SortFavoritesFeature.TOP -> wordStream.sorted(TOP_COMPARATOR);
            case SortFavoritesFeature.BOTTOM -> wordStream.sorted(TOP_COMPARATOR.reversed());
            case SortFavoritesFeature.HIDDEN -> wordStream.filter(w -> !w.favorite);
            case SortFavoritesFeature.ONLY -> wordStream.filter(w -> w.favorite);
            case SortFavoritesFeature.ORDINARY -> wordStream.sorted((w1, w2) -> w1.word.compareToIgnoreCase(w2.word));
            default -> wordStream;
        };
    }

}
