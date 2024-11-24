package dev.frilly.slangdict.features.search;

import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.Word;

import java.util.Locale;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Implementation for the dictionary querying feature.
 */
public final class QueryFeature implements Supplier<Stream<Word>> {

    private final String query;

    private final boolean matchWord;
    private final boolean matchDefinition;
    private final boolean matchCase;
    private final boolean matchRegex;

    public QueryFeature(
        final String query, final boolean matchWord,
        final boolean matchDefinition, final boolean matchCase,
        final boolean matchRegex
    ) {
        this.query           = query;
        this.matchWord       = matchWord;
        this.matchDefinition = matchDefinition;
        this.matchCase       = matchCase;
        this.matchRegex      = matchRegex;
    }

    @Override
    public Stream<Word> get() {
        return matchRegex ? searchRegex() : searchNormal();
    }

    private Stream<Word> searchRegex() {
        final var reg = Pattern.compile(query, matchCase ? 0
                                                         :
                                               Pattern.CASE_INSENSITIVE)
            .asMatchPredicate();

        final var wordPred = (Predicate<Word>) w -> {
            return matchWord && reg.test(w.word);
        };
        final var defPred = (Predicate<Word>) w -> {
            return matchDefinition && reg.test(w.definition);
        };

        return Dictionary.getInstance()
            .getWords()
            .values()
            .stream()
            .filter(wordPred.or(defPred));
    }

    private Stream<Word> searchNormal() {
        final var q = matchCase ? query : query.toLowerCase(Locale.US);

        final var wordPred = (Predicate<Word>) w -> {
            final var word = matchCase ? w.word.toLowerCase(Locale.US) : w.word;
            return matchWord && word.contains(q);
        };
        final var defPred = (Predicate<Word>) w -> {
            final var def = matchCase ? w.definition.toLowerCase(Locale.US)
                                      : w.definition;
            return matchDefinition && def.contains(q);
        };

        return Dictionary.getInstance()
            .getWords()
            .values()
            .stream()
            .filter(wordPred.or(defPred));
    }

}
