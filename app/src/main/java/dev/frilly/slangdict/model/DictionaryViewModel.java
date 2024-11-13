package dev.frilly.slangdict.model;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.AbstractListModel;
import dev.frilly.slangdict.Dictionary;
import dev.frilly.slangdict.Word;

/**
 * The model that holds the data to display in the View Frame.
 */
public final class DictionaryViewModel extends AbstractListModel<Word> {

    public record QueryResult(int queryCount, double time) {
    }

    private final List<Word> displayedWords = new CopyOnWriteArrayList<>();

    /**
     * Queries the list and updates accordingly async, and retrieves a QueryResult.
     * 
     * @param query The query.
     * @return The future holding the result.
     */
    public CompletableFuture<QueryResult> query(final String query) {
        return CompletableFuture.supplyAsync(() -> {
            displayedWords.clear();
            final var start = System.currentTimeMillis();
            Dictionary.getInstance().getWords().entrySet().parallelStream()
                    .filter(e -> e.getKey().toLowerCase().contains(query.toLowerCase())
                            || e.getValue().definition.stream()
                                    .anyMatch(s -> s.toLowerCase().contains(query.toLowerCase())))
                    .map(e -> e.getValue()).sorted((e1, e2) -> e1.word.compareTo(e2.word)).limit(20)
                    .forEach(displayedWords::add);
            this.fireContentsChanged(this, 0, displayedWords.size() - 1);
            return new QueryResult(displayedWords.size(),
                    (System.currentTimeMillis() - start) / 1000.0);
        });
    }

    @Override
    public Word getElementAt(int index) {
        return displayedWords.get(index);
    }

    @Override
    public int getSize() {
        return displayedWords.size();
    }



}
