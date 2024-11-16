package dev.frilly.slangdict.features.search;

import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.Word;

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

    public QueryFeature(final String query, final boolean matchWord, final boolean matchDefinition, final boolean matchCase, final boolean matchRegex) {
        this.query = query;
        this.matchWord = matchWord;
        this.matchDefinition = matchDefinition;
        this.matchCase = matchCase;
        this.matchRegex = matchRegex;
    }

    private Stream<Word> matchRegex() {
        final var reg = Pattern.compile(query, matchCase ? 0 : Pattern.CASE_INSENSITIVE).asMatchPredicate();

        final var wordPred = (Predicate<Word>) w -> !matchWord || reg.test(w.word);
        final var defPred = (Predicate<Word>) w -> !matchDefinition || reg.test(w.definition);

        return Dictionary.getInstance()
            .getWords()
            .values()
            .stream()
            .filter(wordPred.or(defPred));
    }

    private Stream<Word> matchNormal() {
        final var q = matchWord ? query.toLowerCase() : query;

        final var wordPred = (Predicate<Word>) w -> !matchWord || (matchCase ? w.word : w.word.toLowerCase()).contains(q);
        final var defPred = (Predicate<Word>) w -> !matchDefinition || (matchCase ? w.definition : w.definition.toLowerCase()).contains(q);

        return Dictionary.getInstance()
            .getWords()
            .values()
            .stream()
            .filter(wordPred.or(defPred));
    }

    @Override
    public Stream<Word> get() {
        return matchRegex ? matchRegex() : matchNormal();
    }

}
